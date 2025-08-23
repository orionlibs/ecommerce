package de.hybris.platform.cms2.servicelayer.services.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.cms2.common.functions.ChainFunction;
import de.hybris.platform.cms2.common.functions.ThrowableSupplier;
import de.hybris.platform.cms2.common.functions.impl.FunctionExceptionHandler;
import de.hybris.platform.cms2.common.functions.impl.Functions;
import de.hybris.platform.cms2.common.service.SessionSearchRestrictionsDisabler;
import de.hybris.platform.cms2.data.PagePreviewCriteriaData;
import de.hybris.platform.cms2.enums.CmsPageStatus;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.exceptions.CMSVersionNotFoundException;
import de.hybris.platform.cms2.model.CMSVersionModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSPageDao;
import de.hybris.platform.cms2.servicelayer.daos.CMSVersionDao;
import de.hybris.platform.cms2.servicelayer.services.CMSRestrictionService;
import de.hybris.platform.cms2.version.service.CMSVersionService;
import de.hybris.platform.cms2.version.service.CMSVersionSessionContextProvider;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractCMSPageService extends AbstractCMSService
{
    private CatalogVersionService catalogVersionService;
    private CMSPageDao cmsPageDao;
    private CMSRestrictionService cmsRestrictionService;
    private CMSVersionDao cmsVersionDao;
    private CMSVersionService cmsVersionService;
    private CMSVersionSessionContextProvider cmsVersionSessionContextProvider;
    private SessionSearchRestrictionsDisabler sessionSearchRestrictionsDisabler;
    private Comparator<AbstractPageModel> cmsItemCatalogLevelComparator;


    protected <T> T getItemByCriteria(PagePreviewCriteriaData pagePreviewCriteria, ThrowableSupplier<T> defaultSupplier, ThrowableSupplier<T> versionSupplier)
    {
        Predicate<PagePreviewCriteriaData> isVersioning = previewCriteriaData -> (previewCriteriaData != null && previewCriteriaData.getVersionUid() != null);
        Predicate<PagePreviewCriteriaData> doesVersionModelExists = previewCriteriaData -> getCmsVersionDao().findByUid(previewCriteriaData.getVersionUid()).isPresent();
        ChainFunction<PagePreviewCriteriaData, Optional<T>> chainFunction = Functions.ofSupplierConstrainedBy(FunctionExceptionHandler.supplier(versionSupplier), isVersioning.and(doesVersionModelExists))
                        .orElse(Functions.ofSupplierConstrainedBy(FunctionExceptionHandler.supplier(defaultSupplier), value -> true));
        return ((Optional<T>)chainFunction.apply(pagePreviewCriteria)).orElse(null);
    }


    protected AbstractPageModel getPageForId(String uid) throws CMSItemNotFoundException
    {
        AbstractPageModel page = (AbstractPageModel)getSessionSearchRestrictionsDisabler().execute(() -> {
            List<AbstractPageModel> pages = getCmsPageDao().findPagesByIdAndPageStatuses(uid, getCatalogVersionService().getSessionCatalogVersions(), Arrays.asList(new CmsPageStatus[] {CmsPageStatus.ACTIVE}));
            return pages.isEmpty() ? null : Collections.<AbstractPageModel>max(pages, getCmsItemCatalogLevelComparator());
        });
        if(Objects.isNull(page))
        {
            throw new CMSItemNotFoundException("No page with id [" + uid + "] found.");
        }
        return page;
    }


    protected AbstractPageModel getPageForVersionUid(String versionUid) throws CMSVersionNotFoundException
    {
        AbstractPageModel page = (AbstractPageModel)getSessionSearchRestrictionsDisabler().execute(() -> {
            Optional<CMSVersionModel> cmsVersionModel = getCmsVersionDao().findByUid(versionUid);
            if(cmsVersionModel.isPresent())
            {
                getCmsVersionSessionContextProvider().removeAllContentSlotsForPageFromCache();
                getCmsVersionSessionContextProvider().removeAllGeneratedItemsFromCache();
                return (AbstractPageModel)getCmsVersionService().createItemFromVersion(cmsVersionModel.get());
            }
            return null;
        });
        if(Objects.isNull(page))
        {
            throw new CMSVersionNotFoundException("The version with uid [" + versionUid + "] does not exist.");
        }
        return page;
    }


    protected CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    protected Comparator<AbstractPageModel> getCmsItemCatalogLevelComparator()
    {
        return this.cmsItemCatalogLevelComparator;
    }


    @Required
    public void setCmsItemCatalogLevelComparator(Comparator<AbstractPageModel> cmsItemCatalogLevelComparator)
    {
        this.cmsItemCatalogLevelComparator = cmsItemCatalogLevelComparator;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    protected CMSPageDao getCmsPageDao()
    {
        return this.cmsPageDao;
    }


    @Required
    public void setCmsPageDao(CMSPageDao cmsPageDao)
    {
        this.cmsPageDao = cmsPageDao;
    }


    protected CMSRestrictionService getCmsRestrictionService()
    {
        return this.cmsRestrictionService;
    }


    @Required
    public void setCmsRestrictionService(CMSRestrictionService cmsRestrictionService)
    {
        this.cmsRestrictionService = cmsRestrictionService;
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


    protected CMSVersionService getCmsVersionService()
    {
        return this.cmsVersionService;
    }


    @Required
    public void setCmsVersionService(CMSVersionService cmsVersionService)
    {
        this.cmsVersionService = cmsVersionService;
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


    protected SessionSearchRestrictionsDisabler getSessionSearchRestrictionsDisabler()
    {
        return this.sessionSearchRestrictionsDisabler;
    }


    @Required
    public void setSessionSearchRestrictionsDisabler(SessionSearchRestrictionsDisabler sessionSearchRestrictionsDisabler)
    {
        this.sessionSearchRestrictionsDisabler = sessionSearchRestrictionsDisabler;
    }
}
