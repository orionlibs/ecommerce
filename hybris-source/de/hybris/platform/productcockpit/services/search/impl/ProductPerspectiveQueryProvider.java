package de.hybris.platform.productcockpit.services.search.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.services.search.impl.GenericQuerySearchProvider;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.GenericSelectField;
import de.hybris.platform.core.Operator;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.productcockpit.services.catalog.CatalogService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Executions;

public class ProductPerspectiveQueryProvider extends GenericQuerySearchProvider
{
    private static final Logger log = LoggerFactory.getLogger(ProductPerspectiveQueryProvider.class);
    public static final String SELECTED_CATEGORIES = "selectedCategories";
    public static final String SELECTED_CATALOG_VERSIONS = "selectedCatalogVersions";
    public static final String CATALOG_VERSION_FILTER = "catalogVersionFilter";
    private int maxCategoryCount = 900;
    private CatalogService productCockpitCatalogService;
    private Boolean restrDisabled;


    public CatalogService getProductCockpitCatalogService()
    {
        return this.productCockpitCatalogService;
    }


    @Required
    public void setProductCockpitCatalogService(CatalogService productCockpitCatalogService)
    {
        this.productCockpitCatalogService = productCockpitCatalogService;
    }


    public List<GenericCondition> createConditions(Query query, GenericQuery genQuery)
    {
        List<GenericCondition> conditions = new ArrayList<>();
        conditions.addAll(super.createConditions(query, genQuery));
        Collection<CatalogVersionModel> selectedVersions = (Collection<CatalogVersionModel>)query.getContextParameter("selectedCatalogVersions");
        Collection<CatalogVersionModel> filterVersions = (Collection<CatalogVersionModel>)query.getContextParameter("catalogVersionFilter");
        Collection<CategoryModel> selectedCategories = (Collection<CategoryModel>)query.getContextParameter("selectedCategories");
        if(getMaxCategoryCount() > 0 && selectedCategories != null && selectedCategories.size() > getMaxCategoryCount())
        {
            log.warn("The number of selected categories '" + selectedCategories.size() + "' exceeds the maximum allowed number of '" +
                            getMaxCategoryCount() + "'. Only the first '" + getMaxCategoryCount() + "' will be used when searching.");
            List<CategoryModel> categories = new LinkedList<>();
            Iterator<CategoryModel> iterator = selectedCategories.iterator();
            for(int i = 0; iterator.hasNext() && i < getMaxCategoryCount(); i++)
            {
                categories.add(iterator.next());
            }
            selectedCategories = categories;
        }
        if((selectedVersions != null && !selectedVersions.isEmpty()) || (selectedCategories != null &&
                        !selectedCategories.isEmpty()))
        {
            GenericCondition condition = createCatalogConditions(selectedVersions, selectedCategories);
            if(condition != null)
            {
                conditions.add(condition);
            }
        }
        if(filterVersions != null && !filterVersions.isEmpty())
        {
            GenericCondition condition = createCatalogConditions(filterVersions, Collections.EMPTY_LIST);
            if(condition != null)
            {
                conditions.add(condition);
            }
        }
        return conditions;
    }


    protected void beforeSearch()
    {
        super.beforeSearch();
        if(UITools.searchRestrictionsDisabledInCockpit())
        {
            JaloSession jaloSession = JaloSession.getCurrentSession();
            this.restrDisabled = (Boolean)jaloSession.getAttribute("disableRestrictions");
            jaloSession.setAttribute("disableRestrictions", Boolean.TRUE);
        }
    }


    protected void afterSearch()
    {
        super.afterSearch();
        if(UITools.searchRestrictionsDisabledInCockpit())
        {
            JaloSession jaloSession = JaloSession.getCurrentSession();
            jaloSession.setAttribute("disableRestrictions", this.restrDisabled);
        }
    }


    protected GenericCondition createCatalogConditions(Collection<CatalogVersionModel> selectedVersions, Collection<CategoryModel> selectedCategories)
    {
        List<GenericCondition> conditions = new ArrayList<>(3);
        Collection<CatalogVersionModel> versions = getVersionOnlyCatalogs(selectedVersions, selectedCategories);
        if(versions != null && !versions.isEmpty())
        {
            Collection<PK> args = new ArrayList<>(versions.size());
            for(CatalogVersionModel v : versions)
            {
                args.add(v.getPk());
            }
            conditions.add(
                            GenericCondition.createConditionForValueComparison(new GenericSearchField("catalogVersion"), Operator.IN, args));
            GenericQuery genericQuery = new GenericQuery("CategoryProductRelation");
            genericQuery.addInnerJoin("Category", "cat", GenericCondition.createConditionForFieldComparison(new GenericSearchField("source"), Operator.EQUAL, new GenericSearchField("cat", "PK")));
            genericQuery.addCondition(
                            GenericCondition.createConditionForValueComparison(new GenericSearchField("cat", "catalogVersion"), Operator.IN, args));
            genericQuery.addSelectField(new GenericSelectField("target", PK.class));
            if(Operator.EXISTS.equals(getCatalogSearchOperator()))
            {
                genericQuery.addCondition(GenericCondition.createConditionForFieldComparison(new GenericSearchField("target"), Operator.EQUAL, new GenericSearchField("item", "PK")));
                conditions.add(GenericCondition.createSubQueryCondition(null, Operator.EXISTS, genericQuery));
            }
            else
            {
                conditions
                                .add(GenericCondition.createSubQueryCondition(new GenericSearchField("item", "PK"), Operator.IN, genericQuery));
            }
        }
        if(selectedCategories != null && !selectedCategories.isEmpty())
        {
            Collection<PK> args = new ArrayList<>(selectedCategories.size() * 10);
            JaloSession jaloSession = JaloSession.getCurrentSession();
            for(CategoryModel c : selectedCategories)
            {
                args.add(c.getPk());
                Category cItem = (Category)jaloSession.getItem(c.getPk());
                for(Category subCat : cItem.getAllSubcategories())
                {
                    args.add(subCat.getPK());
                }
            }
            GenericQuery genericQuery = new GenericQuery("CategoryProductRelation");
            genericQuery.addCondition(
                            GenericCondition.createConditionForValueComparison(new GenericSearchField("source"), Operator.IN, args));
            genericQuery.addSelectField(new GenericSelectField("target", PK.class));
            if(Operator.EXISTS.equals(getCatalogSearchOperator()))
            {
                genericQuery.addCondition(GenericCondition.createConditionForFieldComparison(new GenericSearchField("target"), Operator.EQUAL, new GenericSearchField("item", "PK")));
                conditions.add(GenericCondition.createSubQueryCondition(null, Operator.EXISTS, genericQuery));
            }
            else
            {
                conditions
                                .add(GenericCondition.createSubQueryCondition(new GenericSearchField("item", "PK"), Operator.IN, genericQuery));
            }
        }
        return conditions.isEmpty() ? null : ((conditions.size() == 1) ? conditions.get(0) : (GenericCondition)GenericCondition.or(conditions));
    }


    protected Operator getCatalogSearchOperator()
    {
        String parameterName = "default.catalogSearchOperator";
        String parameterValue = UITools.getCockpitParameter("default.catalogSearchOperator", Executions.getCurrent());
        if(!StringUtils.isEmpty(parameterValue))
        {
            return Operator.getOperatorByStringCode(parameterValue);
        }
        return Operator.IN;
    }


    protected Collection<CatalogVersionModel> getVersionOnlyCatalogs(Collection<CatalogVersionModel> versions, Collection<CategoryModel> categories)
    {
        if(versions == null || versions.isEmpty() || categories == null || categories.isEmpty())
        {
            return versions;
        }
        Collection<CatalogVersionModel> ret = new LinkedHashSet<>(versions);
        CatalogService catalogService = getProductCockpitCatalogService();
        for(CategoryModel c : categories)
        {
            CatalogVersionModel catalogVersion = catalogService.getCatalogVersion(c);
            ret.remove(catalogVersion);
            if(ret.isEmpty())
            {
                break;
            }
        }
        return ret;
    }


    public void setMaxCategoryCount(int maxCategoryCount)
    {
        this.maxCategoryCount = maxCategoryCount;
    }


    public int getMaxCategoryCount()
    {
        return this.maxCategoryCount;
    }
}
