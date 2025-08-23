package de.hybris.platform.cms2.version.service.impl;

import com.google.common.base.Strings;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.common.exceptions.PermissionExceptionUtils;
import de.hybris.platform.cms2.common.functions.Converter;
import de.hybris.platform.cms2.common.service.SessionSearchRestrictionsDisabler;
import de.hybris.platform.cms2.enums.CmsApprovalStatus;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.CMSVersionModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSVersionDao;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminContentSlotService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminItemService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cms2.version.service.CMSVersionService;
import de.hybris.platform.cms2.version.service.CMSVersionSessionContextProvider;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSVersionService implements CMSVersionService
{
    private PersistentKeyGenerator versionUidGenerator;
    private PersistentKeyGenerator transactionIdGenerator;
    private SessionService sessionService;
    private ModelService modelService;
    private TypeService typeService;
    private CMSAdminContentSlotService cmsAdminContentSlotService;
    private CMSAdminItemService cmsAdminItemService;
    private CMSAdminSiteService cmsAdminSiteService;
    private CMSVersionDao cmsVersionDao;
    private Converter<ItemModel, String> cmsVersionToDataConverter;
    private Converter<CMSVersionModel, ItemModel> cmsVersionToModelPreviewConverter;
    private Converter<CMSVersionModel, ItemModel> cmsVersionToModelRollbackConverter;
    private CMSVersionSessionContextProvider cmsVersionSessionContextProvider;
    private SessionSearchRestrictionsDisabler cmsSessionSearchRestrictionsDisabler;
    private PermissionCRUDService permissionCRUDService;
    private Predicate<ItemModel> pageTypePredicate;
    private Predicate<ItemModel> contentPageTypePredicate;


    protected String generatePayloadForVersion(CMSVersionModel version)
    {
        return (String)getCmsSessionSearchRestrictionsDisabler().execute(() -> {
            CMSItemModel itemModel = getCMSItemByVersion(version);
            return (String)getCmsVersionToDataConverter().convert(itemModel);
        });
    }


    public ItemModel createItemFromVersion(CMSVersionModel version)
    {
        if(!isPreview())
        {
            checkReadTypePermission("CMSVersion");
        }
        return (ItemModel)getCmsVersionToModelPreviewConverter().convert(version);
    }


    public ItemModel getItemFromVersion(CMSVersionModel version)
    {
        checkReadTypePermission("CMSVersion");
        return (ItemModel)getCmsVersionToModelRollbackConverter().convert(version);
    }


    public Optional<CMSVersionModel> getVersionByUid(String uid)
    {
        checkReadTypePermission("CMSVersion");
        return getCmsVersionDao().findByUid(uid);
    }


    public Optional<CMSVersionModel> getVersionByLabel(CMSItemModel itemModel, String label)
    {
        checkReadTypePermission("CMSVersion");
        return getCmsVersionDao().findByItemUidAndLabel(itemModel.getUid(), label, itemModel.getCatalogVersion());
    }


    protected List<CMSVersionModel> getVersionsForItem(CMSItemModel itemModel)
    {
        checkReadTypePermission("CMSVersion");
        return getCmsVersionDao().findAllByItemUidAndItemCatalogVersion(itemModel.getUid(), itemModel.getCatalogVersion());
    }


    public String getTransactionId()
    {
        String transactionId = (String)getSessionService().getAttribute("sessionVersionTransactionId");
        if(Strings.isNullOrEmpty(transactionId))
        {
            transactionId = generateTransactionId();
            getSessionService().setAttribute("sessionVersionTransactionId", transactionId);
        }
        return transactionId;
    }


    public CMSVersionModel createRevisionForItem(CMSItemModel itemModel)
    {
        return createVersionForItem(itemModel, null, null);
    }


    public CMSVersionModel createVersionForItem(CMSItemModel itemModel, String label, String description)
    {
        checkCreateTypePermission("CMSVersion");
        checkReadTypePermission("CMSItem");
        CMSVersionModel cmsVersionModel = createPartialVersionForItem(itemModel);
        cmsVersionModel.setLabel(label);
        cmsVersionModel.setDescription(description);
        cmsVersionModel.setPayload(generatePayloadForVersion(cmsVersionModel));
        if(isTaggedVersion(cmsVersionModel))
        {
            List<CMSVersionModel> relatedChildren = (List<CMSVersionModel>)getCmsVersionSessionContextProvider().getAllUnsavedVersionedItemsFromCached().stream().filter(cmsItemData -> !cmsVersionModel.getPk().equals(cmsItemData.getPk()))
                            .map(cmsItemData -> (CMSVersionModel)getModelService().get(cmsItemData.getPk())).collect(Collectors.toList());
            cmsVersionModel.setRelatedChildren(relatedChildren);
        }
        getModelService().save(cmsVersionModel);
        return cmsVersionModel;
    }


    public Optional<ItemModel> rollbackVersionForUid(String uid)
    {
        Optional<CMSVersionModel> versionModelOpt = getVersionByUid(uid);
        ItemModel itemModel = null;
        if(versionModelOpt.isPresent())
        {
            itemModel = getItemFromVersion(versionModelOpt.get());
            restoreHomepageAttribute(itemModel);
            updatePageApprovalStatus(itemModel);
            getModelService().saveAll();
        }
        return Optional.ofNullable(itemModel);
    }


    protected boolean isPreview()
    {
        return (getSessionService().getAttribute("cmsTicketId") != null);
    }


    protected void restoreHomepageAttribute(ItemModel itemModel)
    {
        if(getContentPageTypePredicate().test(itemModel))
        {
            boolean originalHomepageFlag = ((Boolean)itemModel.getItemModelContext().getOriginalValue("homepage")).booleanValue();
            ((ContentPageModel)itemModel).setHomepage(originalHomepageFlag);
        }
    }


    protected void updatePageApprovalStatus(ItemModel itemModel)
    {
        if(getPageTypePredicate().test(itemModel))
        {
            ((AbstractPageModel)itemModel).setApprovalStatus(CmsApprovalStatus.CHECK);
        }
    }


    protected CMSVersionModel createPartialVersionForItem(CMSItemModel itemModel)
    {
        CMSVersionModel cmsVersionModel = (CMSVersionModel)getModelService().create("CMSVersion");
        cmsVersionModel.setUid(generateVersionUid());
        cmsVersionModel.setTransactionId(getTransactionId());
        cmsVersionModel.setItemCatalogVersion(itemModel.getCatalogVersion());
        cmsVersionModel.setItemUid(itemModel.getUid());
        cmsVersionModel.setItemTypeCode(itemModel.getItemtype());
        getModelService().save(cmsVersionModel);
        getCmsVersionSessionContextProvider().addUnsavedVersionedItemToCache(cmsVersionModel);
        return cmsVersionModel;
    }


    public String generateVersionUid()
    {
        return getVersionUidGenerator().generate().toString();
    }


    public boolean isVersionable(CMSItemModel itemModel)
    {
        Predicate<CMSItemModel> isInCurrentCatalog = this::isInActiveSessionCatalog;
        Predicate<CMSItemModel> isContentSlot = item -> item instanceof ContentSlotModel;
        Predicate<CMSItemModel> belongsToPage = this::belongsToPage;
        return (isInCurrentCatalog.and(isContentSlot).and(belongsToPage).test(itemModel) || isInCurrentCatalog
                        .and(isContentSlot.negate()).test(itemModel));
    }


    public void deleteVersionsForItem(CMSItemModel itemModel)
    {
        checkRemoveTypePermission("CMSVersion");
        Objects.requireNonNull(getModelService());
        getVersionsForItem(itemModel).forEach(getModelService()::remove);
        getModelService().saveAll();
    }


    public Optional<AbstractPageModel> findPageVersionedByTransactionId(String transactionId)
    {
        if(getCmsVersionSessionContextProvider().isPageVersionedInTransactionCached())
        {
            return getCmsVersionSessionContextProvider().getPageVersionedInTransactionFromCache();
        }
        Optional<AbstractPageModel> pageVersioned = getCmsVersionDao().findPageVersionedByTransactionId(transactionId);
        getCmsVersionSessionContextProvider().addPageVersionedInTransactionToCache(pageVersioned);
        return pageVersioned;
    }


    protected boolean isTaggedVersion(CMSVersionModel cmsVersionModel)
    {
        return !Strings.isNullOrEmpty(cmsVersionModel.getLabel());
    }


    protected boolean isInActiveSessionCatalog(CMSItemModel itemModel)
    {
        if(getCmsAdminSiteService().getActiveCatalogVersion() != null)
        {
            return getCmsAdminSiteService().getActiveCatalogVersion().equals(itemModel.getCatalogVersion());
        }
        throw new IllegalStateException("No active catalog version found in the site for this session");
    }


    protected boolean belongsToPage(CMSItemModel itemModel)
    {
        return getCmsAdminContentSlotService().getAllRelationsForSlot((ContentSlotModel)itemModel).stream()
                        .anyMatch(relationModel -> getTypeService().isAssignableFrom(relationModel.getItemtype(), "ContentSlotForPage"));
    }


    protected String generateTransactionId()
    {
        return getTransactionIdGenerator().generate().toString();
    }


    protected CMSItemModel getCMSItemByVersion(CMSVersionModel version)
    {
        String itemUid = version.getItemUid();
        CatalogVersionModel catalogVersion = version.getItemCatalogVersion();
        try
        {
            return getCmsAdminItemService().findByUid(itemUid, catalogVersion);
        }
        catch(CMSItemNotFoundException e)
        {
            throw new UnknownIdentifierException(
                            String.format("Could not find ItemModel with uid [%s] on [%s] catalog version.", new Object[] {itemUid, catalogVersion}));
        }
    }


    protected void checkReadTypePermission(String typeCode)
    {
        if(!getPermissionCRUDService().canReadType(typeCode))
        {
            throwTypePermissionException("read", typeCode);
        }
    }


    protected void checkCreateTypePermission(String typeCode)
    {
        if(!getPermissionCRUDService().canCreateTypeInstance(typeCode))
        {
            throwTypePermissionException("create", typeCode);
        }
    }


    protected void checkRemoveTypePermission(String typeCode)
    {
        if(!getPermissionCRUDService().canRemoveTypeInstance(typeCode))
        {
            throwTypePermissionException("remove", typeCode);
        }
    }


    protected void checkChangeTypePermission(String typeCode)
    {
        if(!getPermissionCRUDService().canChangeType(typeCode))
        {
            throwTypePermissionException("change", typeCode);
        }
    }


    protected void throwTypePermissionException(String permissionName, String typeCode)
    {
        throw PermissionExceptionUtils.createTypePermissionException(permissionName, typeCode);
    }


    protected PersistentKeyGenerator getVersionUidGenerator()
    {
        return this.versionUidGenerator;
    }


    @Required
    public void setVersionUidGenerator(PersistentKeyGenerator versionUidGenerator)
    {
        this.versionUidGenerator = versionUidGenerator;
    }


    protected PersistentKeyGenerator getTransactionIdGenerator()
    {
        return this.transactionIdGenerator;
    }


    @Required
    public void setTransactionIdGenerator(PersistentKeyGenerator transactionIdGenerator)
    {
        this.transactionIdGenerator = transactionIdGenerator;
    }


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    protected CMSAdminItemService getCmsAdminItemService()
    {
        return this.cmsAdminItemService;
    }


    @Required
    public void setCmsAdminItemService(CMSAdminItemService cmsAdminItemService)
    {
        this.cmsAdminItemService = cmsAdminItemService;
    }


    protected CMSVersionDao getCmsVersionDao()
    {
        return this.cmsVersionDao;
    }


    @Required
    public void setCmsVersionDao(CMSVersionDao cmsVersionDao)
    {
        this.cmsVersionDao = cmsVersionDao;
    }


    public CMSAdminContentSlotService getCmsAdminContentSlotService()
    {
        return this.cmsAdminContentSlotService;
    }


    @Required
    public void setCmsAdminContentSlotService(CMSAdminContentSlotService cmsAdminContentSlotService)
    {
        this.cmsAdminContentSlotService = cmsAdminContentSlotService;
    }


    protected CMSAdminSiteService getCmsAdminSiteService()
    {
        return this.cmsAdminSiteService;
    }


    @Required
    public void setCmsAdminSiteService(CMSAdminSiteService cmsAdminSiteService)
    {
        this.cmsAdminSiteService = cmsAdminSiteService;
    }


    protected Converter<ItemModel, String> getCmsVersionToDataConverter()
    {
        return this.cmsVersionToDataConverter;
    }


    @Required
    public void setCmsVersionToDataConverter(Converter<ItemModel, String> cmsVersionToDataConverter)
    {
        this.cmsVersionToDataConverter = cmsVersionToDataConverter;
    }


    protected Converter<CMSVersionModel, ItemModel> getCmsVersionToModelPreviewConverter()
    {
        return this.cmsVersionToModelPreviewConverter;
    }


    @Required
    public void setCmsVersionToModelPreviewConverter(Converter<CMSVersionModel, ItemModel> cmsVersionToModelPreviewConverter)
    {
        this.cmsVersionToModelPreviewConverter = cmsVersionToModelPreviewConverter;
    }


    protected CMSVersionSessionContextProvider getCmsVersionSessionContextProvider()
    {
        return this.cmsVersionSessionContextProvider;
    }


    @Required
    public void setCmsVersionSessionContextProvider(CMSVersionSessionContextProvider cmsVersionSessionContextProvider)
    {
        this.cmsVersionSessionContextProvider = cmsVersionSessionContextProvider;
    }


    public SessionSearchRestrictionsDisabler getCmsSessionSearchRestrictionsDisabler()
    {
        return this.cmsSessionSearchRestrictionsDisabler;
    }


    @Required
    public void setCmsSessionSearchRestrictionsDisabler(SessionSearchRestrictionsDisabler cmsSessionSearchRestrictionsDisabler)
    {
        this.cmsSessionSearchRestrictionsDisabler = cmsSessionSearchRestrictionsDisabler;
    }


    public Converter<CMSVersionModel, ItemModel> getCmsVersionToModelRollbackConverter()
    {
        return this.cmsVersionToModelRollbackConverter;
    }


    @Required
    public void setCmsVersionToModelRollbackConverter(Converter<CMSVersionModel, ItemModel> cmsVersionToModelRollbackConverter)
    {
        this.cmsVersionToModelRollbackConverter = cmsVersionToModelRollbackConverter;
    }


    protected PermissionCRUDService getPermissionCRUDService()
    {
        return this.permissionCRUDService;
    }


    @Required
    public void setPermissionCRUDService(PermissionCRUDService permissionCRUDService)
    {
        this.permissionCRUDService = permissionCRUDService;
    }


    protected Predicate<ItemModel> getPageTypePredicate()
    {
        return this.pageTypePredicate;
    }


    @Required
    public void setPageTypePredicate(Predicate<ItemModel> pageTypePredicate)
    {
        this.pageTypePredicate = pageTypePredicate;
    }


    protected Predicate<ItemModel> getContentPageTypePredicate()
    {
        return this.contentPageTypePredicate;
    }


    @Required
    public void setContentPageTypePredicate(Predicate<ItemModel> contentPageTypePredicate)
    {
        this.contentPageTypePredicate = contentPageTypePredicate;
    }
}
