package de.hybris.platform.productcockpit.dao;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.productcockpit.model.macfinder.MacFinderTreeModelAbstract;
import de.hybris.platform.productcockpit.model.macfinder.node.LeafNode;
import de.hybris.platform.productcockpit.model.macfinder.node.MacFinderTreeNode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MediaDao extends AbstractDao
{
    public List<MacFinderTreeNode> getItems(Item category, CatalogVersion version, boolean withSubcategories)
    {
        return wrapIntoConnectedItem(getConnectedItems(category, version, withSubcategories));
    }


    public List<Item> getConnectedItems(Item category, CatalogVersion version, boolean withSubcategories)
    {
        SearchResult result = getResult(category, withSubcategories, MacFinderTreeModelAbstract.getMaxItemCountProperty());
        return (result == null) ? Collections.EMPTY_LIST : result.getResult();
    }


    private SearchResult getResult(Item category, boolean withSubcategories, int count)
    {
        if(category == null)
        {
            return null;
        }
        SessionContext ctx = null;
        try
        {
            if(UITools.searchRestrictionsDisabledInCockpit())
            {
                ctx = JaloSession.getCurrentSession().createLocalSessionContext();
                ctx.setAttribute("disableRestrictions", Boolean.TRUE);
            }
            Map<Object, Object> params = new HashMap<>();
            StringBuilder query = new StringBuilder();
            query.append("SELECT {m:").append(Item.PK).append("} ");
            query.append("FROM {").append(TypeManager.getInstance().getComposedType(Media.class).getCode()).append(" AS m ");
            query.append("JOIN ").append(GeneratedCatalogConstants.Relations.CATEGORYMEDIARELATION).append(" AS l ");
            query.append("ON {l:").append("target").append("}={m:").append(Item.PK).append("} } ");
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
            query.append(" ORDER BY {l:").append("sequenceNumber").append("}");
            FlexibleSearch flexibleSearch = JaloSession.getCurrentSession().getFlexibleSearch();
            SearchResult result = flexibleSearch.search(query.toString(), params, Collections.singletonList(Media.class), false, false, 0, count);
            return result;
        }
        finally
        {
            if(ctx != null)
            {
                JaloSession.getCurrentSession().removeLocalSessionContext();
            }
        }
    }


    public int getItemCount(Item category, CatalogVersion version, boolean withSubcategories)
    {
        SearchResult result = getResult(category, withSubcategories, 1);
        return (result == null) ? 0 : result.getTotalCount();
    }


    protected List<MacFinderTreeNode> wrapIntoConnectedItem(List<Item> sourceList)
    {
        List<MacFinderTreeNode> results = new ArrayList<>();
        for(Item item : sourceList)
        {
            LeafNode leafNode = new LeafNode();
            leafNode.setOriginalItem(getTypeService().wrapItem(item.getPK()));
            results.add(leafNode);
        }
        return results;
    }
}
