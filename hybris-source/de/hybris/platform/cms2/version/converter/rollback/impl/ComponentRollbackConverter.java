package de.hybris.platform.cms2.version.converter.rollback.impl;

import de.hybris.platform.cms2.cloning.service.CMSItemCloningService;
import de.hybris.platform.cms2.exceptions.ItemRollbackException;
import de.hybris.platform.cms2.model.CMSVersionModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.data.ContentSlotData;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cms2.version.converter.rollback.ItemRollbackConverter;
import de.hybris.platform.cms2.version.service.CMSVersionService;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.persistence.audit.payload.json.AuditPayload;
import de.hybris.platform.persistence.audit.payload.json.TypedValue;
import de.hybris.platform.persistence.audit.payload.json.ValueType;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class ComponentRollbackConverter implements ItemRollbackConverter
{
    private CMSVersionService cmsVersionService;
    private CMSComponentService cmsComponentService;
    private CMSItemCloningService cmsItemCloningService;
    private CMSPageService cmsPageService;
    private ModelService modelService;
    private TypeService typeService;
    private Predicate<ItemModel> constrainedBy;


    public AbstractCMSComponentModel rollbackItem(ItemModel itemModel, CMSVersionModel version, AuditPayload auditPayload) throws ItemRollbackException
    {
        AbstractCMSComponentModel componentModel = (AbstractCMSComponentModel)itemModel;
        AbstractPageModel page = getCmsVersionService().findPageVersionedByTransactionId(version.getTransactionId()).orElse(null);
        if(page != null)
        {
            if(this.cmsComponentService.isComponentUsedOutsidePage(componentModel, page))
            {
                componentModel = replaceComponentWithCloneInPage(componentModel, page, auditPayload);
            }
            else
            {
                rollbackComponentOnlyInPage(componentModel, page, auditPayload);
            }
        }
        return componentModel;
    }


    protected AbstractCMSComponentModel replaceComponentWithCloneInPage(AbstractCMSComponentModel originalComponent, AbstractPageModel page, AuditPayload componentPayload) throws ItemRollbackException
    {
        AbstractCMSComponentModel clonedComponent = (AbstractCMSComponentModel)getCmsItemCloningService().cloneComponent(originalComponent).orElseThrow(() -> new ItemRollbackException("Cannot clone shared component " + originalComponent.getUid()));
        Set<ContentSlotModel> slotsInPage = getPageSlots(page);
        Set<String> versionedSlots = getVersionedSlotsUids(componentPayload);
        clonedComponent.setSlots((Collection)slotsInPage.stream()
                        .filter(slot -> versionedSlots.contains(slot.getUid()))
                        .collect(Collectors.toList()));
        Set<String> slotsInPageIds = (Set<String>)slotsInPage.stream().map(CMSItemModel::getUid).collect(Collectors.toSet());
        originalComponent.setSlots((Collection)originalComponent.getSlots().stream()
                        .filter(slot -> !slotsInPageIds.contains(slot.getUid()))
                        .collect(Collectors.toList()));
        updatePayloadForClonedComponent(clonedComponent, componentPayload);
        removeSlotsOutsidePageFromPayload(slotsInPage, componentPayload);
        return clonedComponent;
    }


    protected void rollbackComponentOnlyInPage(AbstractCMSComponentModel component, AbstractPageModel page, AuditPayload componentPayload)
    {
        Set<ContentSlotModel> slotsInPage = getPageSlots(page);
        Set<String> versionedSlots = getVersionedSlotsUids(componentPayload);
        component.setSlots((Collection)slotsInPage.stream()
                        .filter(slot -> versionedSlots.contains(slot.getUid()))
                        .collect(Collectors.toList()));
        removeSlotsOutsidePageFromPayload(slotsInPage, componentPayload);
    }


    protected void updatePayloadForClonedComponent(AbstractCMSComponentModel clonedComponent, AuditPayload componentPayload)
    {
        ValueType valueType = ValueType.newType(String.class.getName());
        TypedValue uidValue = new TypedValue(valueType, Collections.singletonList(clonedComponent.getUid()));
        TypedValue nameValue = new TypedValue(valueType, Collections.singletonList(clonedComponent.getName()));
        componentPayload.getAttributes().put("uid", uidValue);
        componentPayload.getAttributes().put("name", nameValue);
    }


    protected Set<ContentSlotModel> getPageSlots(AbstractPageModel page)
    {
        return (Set<ContentSlotModel>)getCmsPageService().getContentSlotsForPage(page).stream()
                        .filter(contentSlotData -> !contentSlotData.isFromMaster())
                        .map(ContentSlotData::getContentSlot)
                        .collect(Collectors.toSet());
    }


    protected Set<String> getVersionedSlotsUids(AuditPayload componentPayload)
    {
        return (Set<String>)readSlotsFromComponentPayload(componentPayload).stream()
                        .map(this::getItemModelFromPk)
                        .filter(itemModel -> isAssignableFromVersion(itemModel.getItemtype()))
                        .map(versionModel -> ((CMSVersionModel)versionModel).getItemUid())
                        .collect(Collectors.toSet());
    }


    protected void removeSlotsOutsidePageFromPayload(Set<ContentSlotModel> slotsInPage, AuditPayload componentPayload)
    {
        Set<String> slotsInPageUids = (Set<String>)slotsInPage.stream().map(CMSItemModel::getUid).collect(Collectors.toSet());
        List<String> filteredPayloadSlots = (List<String>)readSlotsFromComponentPayload(componentPayload).stream().filter(slotPk -> isPkSlotInPage(slotPk, slotsInPageUids)).collect(Collectors.toList());
        ValueType valueType = ValueType.newType(String.class.getName());
        TypedValue slotsValue = new TypedValue(valueType, filteredPayloadSlots);
        componentPayload.getAttributes().put("slots", slotsValue);
    }


    protected boolean isPkSlotInPage(String slotPk, Set<String> slotsInPageUid)
    {
        ItemModel itemModel = getItemModelFromPk(slotPk);
        if(isAssignableFromVersion(itemModel.getItemtype()))
        {
            String slotUid = ((CMSVersionModel)itemModel).getItemUid();
            return slotsInPageUid.contains(slotUid);
        }
        return false;
    }


    protected List<String> readSlotsFromComponentPayload(AuditPayload componentPayload)
    {
        if(componentPayload.getAttributes().containsKey("slots"))
        {
            return ((TypedValue)componentPayload.getAttributes().get("slots")).getValue();
        }
        return Collections.emptyList();
    }


    protected ItemModel getItemModelFromPk(String pk)
    {
        return (ItemModel)this.modelService.get(PK.parse(pk));
    }


    protected boolean isAssignableFromVersion(String itemTypeCode)
    {
        return this.typeService.isAssignableFrom("CMSVersion", itemTypeCode);
    }


    protected CMSVersionService getCmsVersionService()
    {
        return this.cmsVersionService;
    }


    @Required
    public void setCmsVersionService(CMSVersionService cmsVersionService)
    {
        this.cmsVersionService = cmsVersionService;
    }


    protected CMSItemCloningService getCmsItemCloningService()
    {
        return this.cmsItemCloningService;
    }


    @Required
    public void setCmsItemCloningService(CMSItemCloningService cmsItemCloningService)
    {
        this.cmsItemCloningService = cmsItemCloningService;
    }


    protected CMSComponentService getCmsComponentService()
    {
        return this.cmsComponentService;
    }


    @Required
    public void setCmsComponentService(CMSComponentService cmsComponentService)
    {
        this.cmsComponentService = cmsComponentService;
    }


    protected CMSPageService getCmsPageService()
    {
        return this.cmsPageService;
    }


    @Required
    public void setCmsPageService(CMSPageService cmsPageService)
    {
        this.cmsPageService = cmsPageService;
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


    public Predicate<ItemModel> getConstrainedBy()
    {
        return this.constrainedBy;
    }


    @Required
    public void setConstrainedBy(Predicate<ItemModel> constrainedBy)
    {
        this.constrainedBy = constrainedBy;
    }
}
