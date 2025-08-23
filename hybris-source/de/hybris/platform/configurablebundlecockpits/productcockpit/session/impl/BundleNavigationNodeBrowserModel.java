package de.hybris.platform.configurablebundlecockpits.productcockpit.session.impl;

import com.google.common.collect.Lists;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.model.advancedsearch.ConditionValue;
import de.hybris.platform.cockpit.model.advancedsearch.ConditionValueContainer;
import de.hybris.platform.cockpit.model.advancedsearch.impl.AdvancedSearchHelper;
import de.hybris.platform.cockpit.model.advancedsearch.impl.SimpleConditionValue;
import de.hybris.platform.cockpit.model.browser.BrowserModelFactory;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.meta.impl.ItemAttributePropertyDescriptor;
import de.hybris.platform.cockpit.model.search.ExtendedSearchResult;
import de.hybris.platform.cockpit.model.search.Operator;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.model.search.SearchParameterDescriptor;
import de.hybris.platform.cockpit.model.search.SearchParameterValue;
import de.hybris.platform.cockpit.model.search.SearchType;
import de.hybris.platform.cockpit.services.label.LabelService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.search.SearchProvider;
import de.hybris.platform.cockpit.services.search.impl.ItemAttributeSearchDescriptor;
import de.hybris.platform.cockpit.session.BrowserFilter;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.DefaultSearchBrowserModel;
import de.hybris.platform.configurablebundlecockpits.productcockpit.navigationnode.browserarea.tree.BundleTemplateTreeModel;
import de.hybris.platform.configurablebundlecockpits.productcockpit.session.impl.type.BundleRuleType;
import de.hybris.platform.configurablebundlecockpits.servicelayer.services.BundleNavigationService;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.productcockpit.session.impl.QueryBrowserCatalogVersionFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;

public class BundleNavigationNodeBrowserModel extends DefaultSearchBrowserModel
{
    private static final String BUNDLE_TEMPLATE_PARENT_TEMPLATE = "BundleTemplate.parentTemplate";
    private static final String BUNDLE_TEMPLATE_CATALOG_VERSION = "BundleTemplate.catalogVersion";
    private static final Logger LOG = Logger.getLogger(BundleNavigationNodeBrowserModel.class);
    private static final String BUNDLE_NAVIGATION_SERVICE = "bundleNavigationService";
    private static final String BROWSER_MODEL = "BundleProductSearchBrowserModel";
    private Set<TypedObject> selectedNode;
    private List<List<Integer>> openedItems = Lists.newArrayList();
    private BundleTemplateTreeModel treeModel;
    private LabelService labelService;
    private TypeService typeService;
    private BundleNavigationService bundleNavigationService;


    public BundleNavigationNodeBrowserModel()
    {
        super(UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate("BundleTemplate"));
        UIBrowserArea browserArea = UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea();
        if(browserArea instanceof BundleNavigationNodeBrowserArea)
        {
            CatalogVersionModel catVer = ((BundleNavigationNodeBrowserArea)browserArea).getActiveCatalogVersion();
            if(catVer != null)
            {
                CatalogModel catalog = catVer.getCatalog();
                String catalogLabel = getLabelServiceInternal().getObjectTextLabelForTypedObject(
                                getTypeServiceInternal().wrapItem(catalog));
                String catVerLabel = getLabelServiceInternal().getObjectTextLabelForTypedObject(
                                getTypeServiceInternal().wrapItem(catVer));
                String arrows = " >> ";
                setLabel(catalogLabel + " >> " + catalogLabel);
            }
        }
    }


    public void removeItems(Collection<Integer> indexes)
    {
    }


    public void blacklistItems(Collection<Integer> indexes)
    {
    }


    public AbstractContentBrowser createViewComponent()
    {
        return (AbstractContentBrowser)new BundleNavigationNodeContentBrowser();
    }


    public TypedObject getItem(int index)
    {
        return null;
    }


    public List<TypedObject> getItems()
    {
        return Collections.emptyList();
    }


    public void clearPreservedState()
    {
        this.selectedNode = null;
        this.openedItems.clear();
    }


    public void removeSelectedNavigationNode(BundleNavigationNodeContentBrowser content)
    {
        content.removeSelectedNavigationNode();
    }


    public void fireAddNewNavigationNode(BundleNavigationNodeContentBrowser content)
    {
        content.fireAddRootNavigatioNode();
    }


    public int getTreeRootChildCount()
    {
        int ret = 0;
        UIBrowserArea browserArea = UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea();
        if(browserArea instanceof BundleNavigationNodeBrowserArea)
        {
            BundleNavigationNodeBrowserArea navigationBrowserArea = (BundleNavigationNodeBrowserArea)browserArea;
            CatalogVersionModel catalogVersionModel = navigationBrowserArea.getActiveCatalogVersion();
            if(catalogVersionModel != null)
            {
                List<BundleTemplateModel> rootNavigationNodes = getBundleNavigationNodeService().getRootNavigationNodes(navigationBrowserArea
                                .getActiveCatalogVersion());
                ret = CollectionUtils.isEmpty(rootNavigationNodes) ? 0 : CollectionUtils.size(rootNavigationNodes);
            }
        }
        return ret;
    }


    public void openRelatedQueryBrowser()
    {
        UIBrowserArea browserArea = UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea();
        if(browserArea instanceof BundleNavigationNodeBrowserArea)
        {
            BundleNavigationNodeBrowserArea bArea = (BundleNavigationNodeBrowserArea)browserArea;
            focus();
            bArea.setSplittable(true);
            bArea.setSplitModeActiveDirectly(true);
            BrowserModelFactory factory = (BrowserModelFactory)SpringUtil.getBean("BrowserModelFactory");
            BundleProductSearchBrowserModel browserModel = (BundleProductSearchBrowserModel)factory.createBrowserModel("BundleProductSearchBrowserModel");
            browserModel.setLastQuery(new Query(null, "*", 0, 0));
            browserModel.setBrowserFilterFixed((BrowserFilter)new QueryBrowserCatalogVersionFilter(bArea.getActiveCatalogVersion()));
            bArea.addVisibleBrowser(1, (BrowserModel)browserModel);
            browserModel.updateItems();
            browserModel.focus();
        }
    }


    public void openRelatedBundleQueryBrowser(BundleTemplateModel bundleTemplateModel, BrowserModel model, BundleRuleType bundleRuleType)
    {
        UIBrowserArea browserArea = UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea();
        if(browserArea instanceof BundleNavigationNodeBrowserArea)
        {
            BundleNavigationNodeBrowserArea bArea = (BundleNavigationNodeBrowserArea)browserArea;
            focus();
            bArea.setSplittable(true);
            bArea.setSplitModeActiveDirectly(true);
            BrowserModelFactory factory = (BrowserModelFactory)SpringUtil.getBean("BrowserModelFactory");
            BundleRulesSearchBrowserModel browserModel = (BundleRulesSearchBrowserModel)factory.createBrowserModel(bundleRuleType.getModelName());
            Query query = new Query(null, "*", 0, 0);
            PropertyDescriptor bundleTemplatePropertyDesc = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor(bundleRuleType.getTemplateBundleName());
            ConditionValueContainer condition = AdvancedSearchHelper.createSimpleConditionValue(bundleTemplateModel);
            ItemAttributeSearchDescriptor itemAttributeSearchDescriptor = new ItemAttributeSearchDescriptor((ItemAttributePropertyDescriptor)bundleTemplatePropertyDesc);
            List<SearchParameterValue> paramValues = new ArrayList<>();
            List<List<SearchParameterValue>> orValues = new ArrayList<>();
            for(ConditionValue conditionValue : condition.getConditionValues())
            {
                paramValues.add(new SearchParameterValue((SearchParameterDescriptor)itemAttributeSearchDescriptor, conditionValue, Operator.EQUALS));
            }
            query.setParameterValues(paramValues);
            query.setParameterOrValues(orValues);
            browserModel.setLastQuery(query);
            browserModel.setBrowserFilterFixed((BrowserFilter)new QueryBrowserCatalogVersionFilter(bArea.getActiveCatalogVersion()));
            bArea.addVisibleBrowser(1, (BrowserModel)browserModel);
            browserModel.updateItems();
        }
    }


    protected BundleNavigationService getBundleNavigationNodeService()
    {
        if(this.bundleNavigationService == null)
        {
            this.bundleNavigationService = (BundleNavigationService)SpringUtil.getBean("bundleNavigationService");
        }
        return this.bundleNavigationService;
    }


    public void updateLabels()
    {
        UIBrowserArea browserArea = UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea();
        if(browserArea instanceof BundleNavigationNodeBrowserArea)
        {
            CatalogVersionModel catVer = ((BundleNavigationNodeBrowserArea)browserArea).getActiveCatalogVersion();
            if(catVer != null)
            {
                CatalogModel catalog = catVer.getCatalog();
                String catalogLabel = getLabelService().getObjectTextLabelForTypedObject(getTypeService().wrapItem(catalog));
                String catVerLabel = getLabelService().getObjectTextLabelForTypedObject(getTypeService().wrapItem(catVer));
                String arrows = " >> ";
                setLabel(catalogLabel + " >> " + catalogLabel);
            }
        }
    }


    protected ExtendedSearchResult doSearchInternal(Query query)
    {
        if(query == null)
        {
            throw new IllegalArgumentException("Query can not be null.");
        }
        ExtendedSearchResult result = null;
        SearchProvider searchProvider = getSearchProvider();
        if(searchProvider != null)
        {
            Query searchQuery = null;
            int pageSize = (query.getCount() > 0) ? query.getCount() : getPageSize();
            SearchType selectedType = getSelectedType(query);
            searchQuery = new Query(Collections.singletonList(selectedType), query.getSimpleText(), query.getStart(), pageSize);
            searchQuery.setNeedTotalCount(!isSimplePaging());
            searchQuery.setParameterOrValues(query.getParameterOrValues());
            setParamValuesToSearchQuery(query, searchQuery);
            ObjectTemplate selTemplate = (ObjectTemplate)query.getContextParameter("objectTemplate");
            if(selTemplate != null)
            {
                searchQuery.setContextParameter("objectTemplate", selTemplate);
            }
            setSortPropertiesToSearchQuery(query, searchQuery);
            try
            {
                Query clonedQuery = (Query)searchQuery.clone();
                setLastQuery(clonedQuery);
            }
            catch(CloneNotSupportedException localCloneNotSupportedException)
            {
                LOG.error("Cloning the query is not supported");
                LOG.debug("Cloning exception", localCloneNotSupportedException);
            }
            if(getBrowserFilter() != null)
            {
                getBrowserFilter().filterQuery(searchQuery);
            }
            result = searchProvider.search(searchQuery);
            updateLabels();
        }
        return result;
    }


    protected SearchType getSelectedType(Query query)
    {
        SearchType selectedType = null;
        if(query.getSelectedTypes().size() == 1)
        {
            selectedType = query.getSelectedTypes().iterator().next();
        }
        else if(!query.getSelectedTypes().isEmpty())
        {
            selectedType = query.getSelectedTypes().iterator().next();
            LOG.warn("Query has ambigious search types. Using '" + selectedType.getCode() + "' for searching.");
        }
        if(selectedType == null)
        {
            selectedType = getSearchType();
        }
        return selectedType;
    }


    protected void setParamValuesToSearchQuery(Query originalQuery, Query searchQuery)
    {
        UIBrowserArea browserArea = UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea();
        List<SearchParameterValue> paramValues = new ArrayList<>();
        for(SearchParameterValue spv : originalQuery.getParameterValues())
        {
            if(!"BundleTemplate.catalogVersion".equals(spv.getParameterDescriptor().getQualifier()) &&
                            !"BundleTemplate.parentTemplate".equals(spv.getParameterDescriptor().getQualifier()))
            {
                paramValues.add(spv);
            }
        }
        TypeService currentTypeService = UISessionUtils.getCurrentSession().getTypeService();
        SimpleConditionValue simpleConditionValue = new SimpleConditionValue(((BundleNavigationNodeBrowserArea)browserArea).getActiveCatalogVersion());
        ItemAttributePropertyDescriptor catalogVersionPropertyDescriptor = (ItemAttributePropertyDescriptor)currentTypeService.getPropertyDescriptor("BundleTemplate.catalogVersion");
        paramValues.add(new SearchParameterValue((SearchParameterDescriptor)new ItemAttributeSearchDescriptor(catalogVersionPropertyDescriptor), simpleConditionValue, Operator.EQUALS));
        ItemAttributePropertyDescriptor parentTemplatePropertyDescriptor = (ItemAttributePropertyDescriptor)currentTypeService.getPropertyDescriptor("BundleTemplate.parentTemplate");
        paramValues.add(new SearchParameterValue((SearchParameterDescriptor)new ItemAttributeSearchDescriptor(parentTemplatePropertyDescriptor), new SimpleConditionValue("", new Operator("isEmpty")), new Operator("isEmpty")));
        searchQuery.setParameterValues(paramValues);
    }


    protected void setSortPropertiesToSearchQuery(Query originalQuery, Query searchQuery)
    {
        TypeService sessionTypeService = UISessionUtils.getCurrentSession().getTypeService();
        searchQuery.addSortCriterion(sessionTypeService.getPropertyDescriptor("BundleTemplate.name"), true);
    }


    public List<List<Integer>> getOpenedPath()
    {
        return this.openedItems;
    }


    public void setOpenedItems(List<List<Integer>> openedItems)
    {
        this.openedItems = openedItems;
    }


    public BundleTemplateTreeModel getTreeModel()
    {
        return this.treeModel;
    }


    public void setTreeModel(BundleTemplateTreeModel treeModel)
    {
        this.treeModel = treeModel;
    }


    public Set<TypedObject> getSelectedNode()
    {
        return this.selectedNode;
    }


    public void setSelectedNode(Set<TypedObject> selectedNode)
    {
        this.selectedNode = selectedNode;
    }


    protected LabelService getLabelService()
    {
        return getLabelServiceInternal();
    }


    protected TypeService getTypeService()
    {
        return getTypeServiceInternal();
    }


    protected final LabelService getLabelServiceInternal()
    {
        if(this.labelService == null)
        {
            this.labelService = UISessionUtils.getCurrentSession().getLabelService();
        }
        return this.labelService;
    }


    protected final TypeService getTypeServiceInternal()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }
}
