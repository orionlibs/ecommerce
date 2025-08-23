package de.hybris.platform.productcockpit.dao;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.productcockpit.model.macfinder.MacFinderTreeModelAbstract;
import de.hybris.platform.productcockpit.model.macfinder.node.CategoryNode;
import de.hybris.platform.productcockpit.model.macfinder.node.MacFinderTreeNode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryDao extends AbstractDao
{
    public List<MacFinderTreeNode> getItems(Item category, CatalogVersion version, boolean withSubcategories)
    {
        return wrapIntoConnectedItem(getConnectedItems(category, version, withSubcategories));
    }


    public List<Item> getConnectedItems(Item category, CatalogVersion version, boolean withSubcategories)
    {
        return getSearchResult(category, version, withSubcategories, MacFinderTreeModelAbstract.getMaxItemCountProperty())
                        .getResult();
    }


    private SearchResult getSearchResult(Item category, CatalogVersion version, boolean withSubcategories, int count)
    {
        if(category == null)
        {
            List<Item> list = new ArrayList<>();
            list.addAll(version.getRootCategories());
            return (SearchResult)new Object(this, list);
        }
        Map<Object, Object> params = new HashMap<>();
        String query = prepareQuery(false, category, version, withSubcategories, params);
        FlexibleSearch flexibleSearch = JaloSession.getCurrentSession().getFlexibleSearch();
        SearchResult result = flexibleSearch.search(query, params, Collections.singletonList(Category.class), false, false, 0, count);
        return result;
    }


    public int getItemCount(Item category, CatalogVersion version, boolean withSubcategories)
    {
        int ret;
        if(category == null)
        {
            ret = version.getRootCategories().size();
            return ret;
        }
        Map<Object, Object> params = new HashMap<>();
        String query = prepareQuery(true, category, version, withSubcategories, params);
        FlexibleSearch flexibleSearch = JaloSession.getCurrentSession().getFlexibleSearch();
        SearchResult result = flexibleSearch.search(query, params, Integer.class);
        if(result.getResult().isEmpty())
        {
            ret = 0;
        }
        else
        {
            ret = ((Integer)result.getResult().get(0)).intValue();
        }
        return ret;
    }


    protected List<MacFinderTreeNode> wrapIntoConnectedItem(List<Item> sourceList)
    {
        List<MacFinderTreeNode> results = new ArrayList<>();
        for(Item item : sourceList)
        {
            CategoryNode categoryNode = new CategoryNode();
            categoryNode.setOriginalItem(getTypeService().wrapItem(item.getPK()));
            results.add(categoryNode);
        }
        return results;
    }


    private String prepareQuery(boolean queryCountProjection, Item category, CatalogVersion version, boolean withSubcategories, Map<String, Item> params)
    {
        StringBuilder query = new StringBuilder();
        if(queryCountProjection)
        {
            query.append("SELECT COUNT(*) ");
        }
        else
        {
            query.append("SELECT {c:").append(Item.PK).append("} ");
        }
        query.append("FROM {").append(TypeManager.getInstance().getComposedType(Category.class).getCode()).append(" AS c ");
        query.append("JOIN ").append(GeneratedCatalogConstants.Relations.CATEGORYCATEGORYRELATION).append(" AS l ");
        query.append("ON {l:").append("target").append("}={c:").append(Item.PK).append("} } ");
        query.append("WHERE ").append("{l:").append("source").append("}");
        params.put("cat", category);
        query.append(" IN ( ?cat ");
        if(withSubcategories)
        {
            Collection<Category> allSubCategories = ((Category)category).getAllSubcategories();
            if(!allSubCategories.isEmpty())
            {
                params.put("allSubCategories", allSubCategories);
                query.append(", ?allSubCategories ");
            }
        }
        query.append(") ");
        if(!queryCountProjection)
        {
            query.append("ORDER BY {l:").append("sequenceNumber").append("}");
        }
        return query.toString();
    }
}
