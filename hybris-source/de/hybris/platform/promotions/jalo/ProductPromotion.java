package de.hybris.platform.promotions.jalo;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.promotions.constants.GeneratedPromotionsConstants;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;
import de.hybris.platform.util.Config;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.map.Flat3Map;

public abstract class ProductPromotion extends GeneratedProductPromotion
{
    protected PromotionsManager.RestrictionSetResult findAllProducts(SessionContext ctx, PromotionEvaluationContext promoContext)
    {
        String query;
        Set<Category> promotionCategories = new HashSet<>();
        for(Category cat : getCategories(ctx))
        {
            promotionCategories.add(cat);
            promotionCategories.addAll(cat.getAllSubcategories(ctx));
        }
        List<Category> promotionCategoriesList = new ArrayList<>();
        promotionCategoriesList.addAll(promotionCategories);
        String queryProdPromo = "\tSELECT DISTINCT {product:" + Item.PK + "} \tFROM {" + TypeManager.getInstance().getComposedType(Product.class).getCode() + " as product} \tWHERE \t( \t\t{product:" + Item.PK + "} IN ( \t\t\t{{ \t\t\t\tSELECT {prod2promo:source} FROM {"
                        + GeneratedPromotionsConstants.Relations.PRODUCTPROMOTIONRELATION + " AS prod2promo} \t\t\t\tWHERE {prod2promo:target} = ?promo \t\t\t}} \t\t) ";
        String queryCategories = "\t\tOR \t\t{product:" + Item.PK + "} IN ( \t\t\t{{ \t\t\t\tSELECT {cat2prod:target} FROM { " + GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION + " AS cat2prod} \t\t\t\tWHERE";
        String categoriesQueryEnd = "\t\t\t}} \t\t) ";
        Flat3Map queryParams = new Flat3Map();
        queryParams.put("promo", this);
        if(!Config.isOracleUsed())
        {
            queryCategories = queryCategories + " {cat2prod:source} IN ( ?promotionCategories ) ";
            queryParams.put("promotionCategories", promotionCategories);
        }
        else
        {
            int pages = 0;
            int i;
            for(i = 0; i < promotionCategoriesList.size(); i += 1000)
            {
                queryParams.put("promotionCategories_" + pages, promotionCategoriesList
                                .subList(i, Math.min(i + 1000, promotionCategoriesList.size())));
                pages++;
            }
            for(i = 0; i < pages; i++)
            {
                if(i > 0)
                {
                    queryCategories = queryCategories + " OR ";
                }
                queryCategories = queryCategories + "{cat2prod:source} IN ( ?promotionCategories_" + queryCategories + " )";
            }
        }
        queryCategories = queryCategories + "\t\t\t}} \t\t) ";
        String queryEnd = "\t) ";
        if(promotionCategories.isEmpty())
        {
            query = queryProdPromo + "\t) ";
        }
        else
        {
            query = queryProdPromo + queryProdPromo + "\t) ";
        }
        List<Product> products = getSession().getFlexibleSearch().search(ctx, query, (Map)queryParams, Product.class).getResult();
        if(promoContext.getObserveRestrictions())
        {
            return PromotionsManager.getInstance().evaluateRestrictions(ctx, products, promoContext.getOrder(), (AbstractPromotion)this, promoContext
                            .getDate());
        }
        return new PromotionsManager.RestrictionSetResult(products);
    }


    protected PromotionsManager.RestrictionSetResult findEligibleProductsInBasket(SessionContext ctx, PromotionEvaluationContext promoContext)
    {
        Collection<Product> products = PromotionsManager.getBaseProductsForOrder(ctx, promoContext.getOrder());
        if(!products.isEmpty())
        {
            Set<Category> promotionCategories = new HashSet<>();
            for(Category cat : getCategories(ctx))
            {
                promotionCategories.add(cat);
                promotionCategories.addAll(cat.getAllSubcategories(ctx));
            }
            List<Category> promotionCategoriesList = new ArrayList<>(promotionCategories);
            Flat3Map params = new Flat3Map();
            params.put("promo", this);
            params.put("product", products);
            StringBuilder promQuery = new StringBuilder("SELECT DISTINCT pprom.pk FROM (");
            promQuery.append(" {{ SELECT {p2p:").append("source").append("} as pk ");
            promQuery.append(" FROM {").append(GeneratedPromotionsConstants.Relations.PRODUCTPROMOTIONRELATION).append(" AS p2p } ");
            promQuery.append(" WHERE ?promo = {p2p:").append("target").append("} ");
            promQuery.append(" AND {p2p:").append("source").append("} in (?product) }} ");
            if(!Config.isOracleUsed())
            {
                if(!promotionCategoriesList.isEmpty())
                {
                    promQuery.append(" UNION ");
                    promQuery.append("{{ SELECT {cat2prod:").append("target").append("} as pk ");
                    promQuery.append(" FROM { ").append(GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION).append(" AS cat2prod} ");
                    promQuery.append(" WHERE {cat2prod:").append("source").append("} in (?promotionCategories)  ");
                    promQuery.append("   AND {cat2prod:").append("target").append("} in (?product) }} ");
                    params.put("promotionCategories", promotionCategories);
                }
                promQuery.append(" ) AS pprom");
            }
            else
            {
                if(!promotionCategoriesList.isEmpty())
                {
                    int pages = 0;
                    int i;
                    for(i = 0; i < promotionCategoriesList.size(); i += 1000)
                    {
                        params.put("promotionCategories_" + pages, promotionCategoriesList
                                        .subList(i, Math.min(i + 1000, promotionCategoriesList.size())));
                        pages++;
                    }
                    for(i = 0; i < pages; i++)
                    {
                        promQuery.append(" UNION ");
                        promQuery.append("{{ SELECT {cat2prod:").append("target").append("} as pk ");
                        promQuery.append(" FROM { ").append(GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION)
                                        .append(" AS cat2prod} ");
                        promQuery.append(" WHERE {cat2prod:").append("source").append("} in (?promotionCategories_").append(i);
                        promQuery.append(")   AND {cat2prod:").append("target").append("} in (?product) }} ");
                    }
                }
                promQuery.append(" ) pprom");
            }
            List<Product> cartProducts = getSession().getFlexibleSearch().search(ctx, promQuery.toString(), (Map)params, Product.class).getResult();
            if(promoContext.getObserveRestrictions())
            {
                return PromotionsManager.getInstance().evaluateRestrictions(ctx, cartProducts, promoContext.getOrder(), (AbstractPromotion)this, promoContext
                                .getDate());
            }
            return new PromotionsManager.RestrictionSetResult(cartProducts);
        }
        return new PromotionsManager.RestrictionSetResult(new ArrayList(0));
    }


    protected void buildDataUniqueKey(SessionContext ctx, StringBuilder builder)
    {
        super.buildDataUniqueKey(ctx, builder);
        buildDataUniqueKeyForProducts(ctx, builder, getProducts(ctx));
        buildDataUniqueKeyForCategories(ctx, builder, getCategories(ctx));
    }
}
