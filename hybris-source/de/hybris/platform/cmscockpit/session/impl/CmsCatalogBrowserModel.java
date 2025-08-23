package de.hybris.platform.cmscockpit.session.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cms2.enums.CmsPageStatus;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmscockpit.session.CmsCatalogBrowserModelFactory;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.GridMainAreaComponentFactory;
import de.hybris.platform.cockpit.components.contentbrowser.ListMainAreaComponentFactory;
import de.hybris.platform.cockpit.components.contentbrowser.MainAreaComponentFactory;
import de.hybris.platform.cockpit.model.gridview.GridItemRenderer;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.meta.impl.ItemAttributePropertyDescriptor;
import de.hybris.platform.cockpit.model.search.ExtendedSearchResult;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.model.search.SearchParameterDescriptor;
import de.hybris.platform.cockpit.model.search.SearchParameterValue;
import de.hybris.platform.cockpit.services.search.SearchProvider;
import de.hybris.platform.cockpit.services.search.impl.ItemAttributeSearchDescriptor;
import de.hybris.platform.cockpit.session.BrowserFilter;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.DefaultSearchBrowserModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.util.Clients;

public class CmsCatalogBrowserModel extends DefaultSearchBrowserModel
{
    private List<MainAreaComponentFactory> viewModes = null;
    private ModelService modelService;
    private CMSAdminPageService cmsPageService;
    private CMSAdminSiteService cmsAdminSiteService = null;


    @Deprecated(since = "4.5", forRemoval = true)
    @HybrisDeprecation(sinceVersion = "4.5")
    public CmsCatalogBrowserModel(ObjectTemplate rootType)
    {
        super(rootType);
    }


    protected ExtendedSearchResult doSearchInternal(Query query)
    {
        if(query.isSimpleSearch() && !isAdvancedSearchVisible())
        {
            PropertyDescriptor propertyDescriptor = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor("abstractPage.defaultPage");
            ItemAttributeSearchDescriptor searchDescriptor = new ItemAttributeSearchDescriptor((ItemAttributePropertyDescriptor)propertyDescriptor);
            if(!isDefaultPageConditionAdded(query, searchDescriptor, Boolean.TRUE))
            {
                query.addParameterValue(new SearchParameterValue((SearchParameterDescriptor)searchDescriptor, Boolean.TRUE, searchDescriptor
                                .getDefaultOperator()));
            }
            PropertyDescriptor propertyDescriptorPageStatus = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor("abstractpage.pageStatus");
            ItemAttributeSearchDescriptor searchDescriptorPageStatus = new ItemAttributeSearchDescriptor((ItemAttributePropertyDescriptor)propertyDescriptorPageStatus);
            query.addParameterValue(new SearchParameterValue((SearchParameterDescriptor)searchDescriptorPageStatus, CmsPageStatus.ACTIVE, searchDescriptor
                            .getDefaultOperator()));
        }
        return super.doSearchInternal(query);
    }


    protected boolean isDefaultPageConditionAdded(Query query, ItemAttributeSearchDescriptor searchDescriptor, Boolean value)
    {
        boolean added = false;
        if(CollectionUtils.isNotEmpty(query.getParameterValues()))
        {
            for(SearchParameterValue additionalSearchParam : query.getParameterValues())
            {
                if(searchDescriptor.equals(additionalSearchParam.getParameterDescriptor()) && additionalSearchParam
                                .getValue().equals(value))
                {
                    added = true;
                    break;
                }
            }
        }
        return added;
    }


    public Object clone() throws CloneNotSupportedException
    {
        CmsCatalogBrowserModelFactory factory = (CmsCatalogBrowserModelFactory)SpringUtil.getBean("cmsCatalogBrowserModelFactory");
        CmsCatalogBrowserModel browserModel = factory.getInstance(getRootType());
        browserModel.setSearchProvider(getSearchProvider());
        browserModel.setSimplePaging(isSimplePaging());
        browserModel.setResult(getResult());
        browserModel.setLastQuery((getLastQuery() == null) ? null : (Query)getLastQuery().clone());
        browserModel.setSortableProperties(getAdvancedSearchModel().getSortableProperties());
        browserModel.setSortAsc(getAdvancedSearchModel().isSortAscending());
        browserModel.setOffset(getOffset());
        browserModel.setPageSize(getPageSize());
        browserModel.setTotalCount(getTotalCount());
        browserModel.setBrowserFilter(getBrowserFilter());
        browserModel.updateLabels();
        return browserModel;
    }


    public List<MainAreaComponentFactory> getAvailableViewModes()
    {
        if(this.viewModes == null)
        {
            this.viewModes = new ArrayList<>();
            this.viewModes.add(new GridMainAreaComponentFactory((GridItemRenderer)new Object(this)));
            this.viewModes.add(new ListMainAreaComponentFactory());
        }
        return this.viewModes;
    }


    protected SearchProvider getSearchProvider()
    {
        if(this.searchProvider == null)
        {
            this.searchProvider = (SearchProvider)SpringUtil.getBean("cmsSearchProvider");
        }
        return this.searchProvider;
    }


    protected String getAllItemLabel()
    {
        CatalogVersionModel catalogVersionModel = getCmsAdminSiteService().getActiveCatalogVersion();
        if(catalogVersionModel != null)
        {
            TypedObject typedObject = UISessionUtils.getCurrentSession().getTypeService().wrapItem(catalogVersionModel);
            return Labels.getLabel("cms_general.all_pages_for") + " " + Labels.getLabel("cms_general.all_pages_for");
        }
        return Labels.getLabel("cms_general.all_pages");
    }


    public ModelService getModelService()
    {
        if(this.modelService == null)
        {
            this.modelService = (ModelService)SpringUtil.getBean("modelService");
        }
        return this.modelService;
    }


    public CMSAdminPageService getCmsAdminPageService()
    {
        if(this.cmsPageService == null)
        {
            this.cmsPageService = (CMSAdminPageService)SpringUtil.getBean("cmsAdminPageService");
        }
        return this.cmsPageService;
    }


    public AbstractContentBrowser createViewComponent()
    {
        return (AbstractContentBrowser)new CmsCatalogSearchContentBrowser();
    }


    protected CMSAdminSiteService getCmsAdminSiteService()
    {
        if(this.cmsAdminSiteService == null)
        {
            this.cmsAdminSiteService = (CMSAdminSiteService)SpringUtil.getBean("cmsAdminSiteService");
        }
        return this.cmsAdminSiteService;
    }


    public void deleteCurrentPage(TypedObject currenPage)
    {
        ItemModel itemModel = (ItemModel)currenPage.getObject();
        try
        {
            if(itemModel != null)
            {
                getModelService().remove(itemModel);
                BrowserModel browserModel = UISessionUtils.getCurrentSession().getPerspective("cmscockpit.perspective.catalog").getBrowserArea().getFocusedBrowser();
                if(browserModel instanceof DefaultSearchBrowserModel)
                {
                    ((CmsCatalogBrowserModel)browserModel).updateItems();
                }
                UISessionUtils.getCurrentSession().setCurrentPerspective(
                                UISessionUtils.getCurrentSession().getPerspective("cmscockpit.perspective.catalog"));
            }
        }
        finally
        {
            Clients.showBusy(null, false);
        }
    }


    public List<TypedObject> getTemplates()
    {
        List<TypedObject> templates = new ArrayList<>();
        if(getCmsAdminSiteService().getActiveCatalogVersion() != null)
        {
            templates.addAll(UISessionUtils.getCurrentSession().getTypeService()
                            .wrapItems(getCmsAdminPageService().getAllPageTemplates(true)));
        }
        return templates;
    }


    public Set<BrowserFilter> getAvailableBrowserFilters()
    {
        Set<BrowserFilter> availableBrowserFilters = new LinkedHashSet<>();
        if(getBrowserFilterFixed() != null)
        {
            availableBrowserFilters.add(getBrowserFilterFixed());
        }
        Set<BrowserFilter> configuredBrowserFilters = getConfiguredBrowserFilters();
        if(CollectionUtils.isNotEmpty(configuredBrowserFilters))
        {
            availableBrowserFilters.addAll(configuredBrowserFilters);
        }
        return availableBrowserFilters;
    }
}
