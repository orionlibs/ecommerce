package de.hybris.platform.personalizationcmsweb.queries;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.CMSVersionModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cms2.version.service.CMSVersionService;
import de.hybris.platform.cms2.version.service.CMSVersionSessionContextProvider;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.personalizationcms.model.CxCmsComponentContainerModel;
import de.hybris.platform.personalizationwebservices.data.CatalogVersionWsDTO;
import de.hybris.platform.personalizationwebservices.queries.impl.AbstractRestQueryExecutor;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;

public class CmsPageVersionSwitchQueryExecutor extends AbstractRestQueryExecutor
{
    private static final Logger LOG = LoggerFactory.getLogger(CmsPageVersionSwitchQueryExecutor.class);
    public static final String FIELD = "result";
    private static final Map<String, Boolean> TRUE = Collections.singletonMap("result", Boolean.TRUE);
    private static final Map<String, Boolean> FALSE = Collections.singletonMap("result", Boolean.FALSE);
    public static final String VERSION_ID = "versionId";
    private static final String ACTIONS_FOR_CONTAINERS = " SELECT {a.PK}  FROM {CxCmsAction as a} WHERE {a.containerId} IN (?containers)  AND {a.catalogVersion} = ?catalogVersion  ";
    private CMSVersionService cmsVersionService;
    private CMSVersionSessionContextProvider cmsVersionSessionContextProvider;
    private CMSPageService cmsPageService;
    private FlexibleSearchService flexibleSearchService;
    private CatalogVersionService catalogVersionService;
    private ModelService modelService;


    protected void validateInputParams(Map<String, String> params, Errors errors)
    {
        validateMissingField(errors, new String[] {"versionId", "catalog", "catalogVersion"});
    }


    protected Map<String, Boolean> executeAfterValidation(Map<String, String> params)
    {
        Set<String> targetContainers;
        Map<String, Boolean> result = TRUE;
        String versionId = params.get("versionId");
        String catalog = params.get("catalog");
        String catalogVersion = params.get("catalogVersion");
        CatalogVersionModel catalogVersionModel = this.catalogVersionService.getCatalogVersion(catalog, catalogVersion);
        CMSVersionModel pageVersion = getPageVersion(versionId, catalogVersionModel);
        Transaction tx = Transaction.current();
        try
        {
            tx.begin();
            ItemModel itemFromVersion = this.cmsVersionService.createItemFromVersion(pageVersion);
            if(!(itemFromVersion instanceof AbstractPageModel))
            {
                return result;
            }
            List<ContentSlotForPageModel> targetSlots = this.cmsVersionSessionContextProvider.getAllCachedContentSlotsForPage();
            targetContainers = getContainersForContentSlots(targetSlots);
        }
        finally
        {
            tx.rollback();
            this.modelService.detachAll();
        }
        Set<String> currentContainers = getContainersFromCurrentPage(pageVersion.getItemUid());
        currentContainers.removeAll(targetContainers);
        if(currentContainers.isEmpty())
        {
            return result;
        }
        int count = getNumberOfMissingActions(currentContainers, catalogVersionModel);
        return (count == 0) ? TRUE : FALSE;
    }


    protected CMSVersionModel getPageVersion(String versionId, CatalogVersionModel catalogVersionModel)
    {
        this.catalogVersionService.setSessionCatalogVersions(Collections.singleton(catalogVersionModel));
        Optional<CMSVersionModel> optPageVersion = this.cmsVersionService.getVersionByUid(versionId);
        CMSVersionModel pageVersion = optPageVersion.<Throwable>orElseThrow(() -> new WebserviceValidationException("No item with versionId " + versionId + " was found"));
        CatalogVersionModel targetCatalogVersionModel = pageVersion.getItemCatalogVersion();
        if(catalogVersionModel == null || !catalogVersionModel.equals(targetCatalogVersionModel))
        {
            throw new WebserviceValidationException("Declared catalog and item catalog do not match.");
        }
        return pageVersion;
    }


    protected Set<String> getContainersForContentSlots(Collection<ContentSlotForPageModel> contentSlotForPageModels)
    {
        return (Set<String>)contentSlotForPageModels.stream().map(ContentSlotForPageModel::getContentSlot)
                        .flatMap(cs -> cs.getCmsComponents().stream())
                        .filter(c -> c instanceof CxCmsComponentContainerModel)
                        .map(c -> (CxCmsComponentContainerModel)c)
                        .map(CxCmsComponentContainerModel::getSourceId)
                        .collect(Collectors.toSet());
    }


    protected Set<String> getContainersFromCurrentPage(String pageId)
    {
        try
        {
            AbstractPageModel pageModel = this.cmsPageService.getPageForId(pageId);
            Collection<ContentSlotForPageModel> contentSlotsForPage = this.cmsPageService.getOwnContentSlotsForPage(pageModel);
            return getContainersForContentSlots(contentSlotsForPage);
        }
        catch(CMSItemNotFoundException e)
        {
            LOG.warn("Can't find page with id " + pageId, (Throwable)e);
            return Collections.emptySet();
        }
    }


    protected int getNumberOfMissingActions(Set<String> containers, CatalogVersionModel catalogVersion)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(" SELECT {a.PK}  FROM {CxCmsAction as a} WHERE {a.containerId} IN (?containers)  AND {a.catalogVersion} = ?catalogVersion  ");
        query.addQueryParameter("containers", containers);
        query.addQueryParameter("catalogVersion", catalogVersion);
        query.setNeedTotal(true);
        query.setCount(1);
        SearchResult<Object> result = this.flexibleSearchService.search(query);
        return result.getTotalCount();
    }


    public List<CatalogVersionWsDTO> getCatalogsForWriteAccess(Map<String, String> params)
    {
        return Collections.emptyList();
    }


    public List<CatalogVersionWsDTO> getCatalogsForReadAccess(Map<String, String> params)
    {
        return getCatalogFromParams(params);
    }


    @Required
    public void setCmsVersionService(CMSVersionService cmsVersionService)
    {
        this.cmsVersionService = cmsVersionService;
    }


    protected CMSVersionService getCmsVersionService()
    {
        return this.cmsVersionService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    protected CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCmsVersionSessionContextProvider(CMSVersionSessionContextProvider cmsVersionSessionContextProvider)
    {
        this.cmsVersionSessionContextProvider = cmsVersionSessionContextProvider;
    }


    protected CMSVersionSessionContextProvider getCmsVersionSessionContextProvider()
    {
        return this.cmsVersionSessionContextProvider;
    }


    @Required
    public void setCmsPageService(CMSPageService cmsPageService)
    {
        this.cmsPageService = cmsPageService;
    }


    protected CMSPageService getCmsPageService()
    {
        return this.cmsPageService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }
}
