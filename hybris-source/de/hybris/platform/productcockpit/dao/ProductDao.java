package de.hybris.platform.productcockpit.dao;

import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.productcockpit.model.macfinder.MacFinderTreeModelAbstract;
import de.hybris.platform.productcockpit.model.macfinder.node.LeafNode;
import de.hybris.platform.productcockpit.model.macfinder.node.MacFinderTreeNode;
import de.hybris.platform.variants.jalo.VariantProduct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDao extends AbstractDao
{
    private final CategoryDao categoryDao = new CategoryDao();


    public List<MacFinderTreeNode> getItems(Item category, CatalogVersion version, boolean withSubcategories)
    {
        return wrapIntoConnectedItem(getConnectedItems(category, version, withSubcategories));
    }


    public int getItemCount(Item category, CatalogVersion version, boolean withSubcategories)
    {
        Map<Object, Object> params = new HashMap<>();
        SessionContext ctx = null;
        try
        {
            int ret;
            if(UITools.searchRestrictionsDisabledInCockpit())
            {
                ctx = JaloSession.getCurrentSession().createLocalSessionContext();
                ctx.setAttribute("disableRestrictions", Boolean.TRUE);
            }
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
        finally
        {
            if(ctx != null)
            {
                JaloSession.getCurrentSession().removeLocalSessionContext();
            }
        }
    }


    public List<Item> getConnectedItems(Item category, CatalogVersion version, boolean withSubcategories)
    {
        return getSearchResult(category, version, withSubcategories, MacFinderTreeModelAbstract.getMaxItemCountProperty())
                        .getResult();
    }


    private SearchResult getSearchResult(Item category, CatalogVersion version, boolean withSubcategories, int count)
    {
        Map<Object, Object> params = new HashMap<>();
        SessionContext ctx = null;
        try
        {
            if(UITools.searchRestrictionsDisabledInCockpit())
            {
                ctx = JaloSession.getCurrentSession().createLocalSessionContext();
                ctx.setAttribute("disableRestrictions", Boolean.TRUE);
            }
            String query = prepareQuery(false, category, version, withSubcategories, params);
            FlexibleSearch flexibleSearch = JaloSession.getCurrentSession().getFlexibleSearch();
            SearchResult result = flexibleSearch.search(query, params, Collections.singletonList(Product.class), false, false, 0, count);
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


    private String prepareQuery(boolean queryCountProjection, Item category, CatalogVersion version, boolean withSubcategories, Map<String, CatalogVersion> params)
    {
        StringBuilder query = new StringBuilder();
        if(category == null)
        {
            List<Item> rootCategories = unwrapIntoConnectedItem(this.categoryDao.getItems(null, version, false));
            if(rootCategories.isEmpty())
            {
                if(queryCountProjection)
                {
                    query.append("SELECT COUNT(*) ");
                }
                else
                {
                    query.append("SELECT {p:").append(Item.PK).append("} ");
                }
                query.append("FROM {").append(TypeManager.getInstance().getComposedType(Product.class).getCode()).append(" AS p } ");
                params.put("cv", version);
                query.append("WHERE ").append("{p:").append(GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION).append("} = ?cv ");
            }
            else
            {
                if(queryCountProjection)
                {
                    query.append("SELECT COUNT(*) ");
                }
                else
                {
                    query.append("SELECT {p:").append(Item.PK).append("} ");
                }
                query.append("FROM {").append(TypeManager.getInstance().getComposedType(Product.class).getCode()).append(" AS p } ");
                params.put("cv", version);
                query.append("WHERE ").append("{p:").append(GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION).append("} = ?cv ");
                query.append("AND NOT EXISTS ({{ ");
                query.append("SELECT {p2:").append(Item.PK).append("} ");
                query.append("FROM {").append(TypeManager.getInstance().getComposedType(Product.class).getCode()).append(" AS p2 ");
                query.append("JOIN ").append(GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION).append(" AS l ");
                query.append("ON {l:").append("target").append("} = {p2:").append(Item.PK).append("} ");
                query.append("JOIN ").append(GeneratedCatalogConstants.TC.CATEGORY).append(" AS c ");
                query.append("ON {l:").append("source").append("} = {c:").append(Item.PK).append("} } ");
                query.append("WHERE ").append("{c:").append(CatalogConstants.Attributes.Category.CATALOGVERSION).append("} = ?cv ");
                query.append("AND ").append("{p:").append(Item.PK).append("} = ").append("{p2:").append(Item.PK).append("} ");
                query.append(" }}) ");
                query.append("AND NOT EXISTS ({{ ");
                query.append("SELECT {p3:").append(Item.PK).append("} ");
                query.append("FROM {").append(TypeManager.getInstance().getComposedType(VariantProduct.class).getCode())
                                .append(" AS p3 } ");
                query.append("WHERE ").append("{p3:").append("baseProduct").append("} IS NOT NULL ");
                query.append("AND ").append("{p:").append(Item.PK).append("} = ").append("{p3:").append(Item.PK).append("} ");
                query.append(" }}) ");
                if(!queryCountProjection)
                {
                    query.append(" ORDER BY {p:name:o}");
                }
            }
        }
        else
        {
            if(queryCountProjection)
            {
                query.append("SELECT COUNT(*) ");
            }
            else
            {
                query.append("SELECT {p:").append(Item.PK).append("} ");
            }
            query.append("FROM {").append(TypeManager.getInstance().getComposedType(Product.class).getCode()).append(" AS p ");
            query.append("JOIN ").append(GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION).append(" AS l ");
            query.append("ON {l:").append("target").append("} = {p:").append(Item.PK).append("} } ");
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
        }
        return query.toString();
    }
}
