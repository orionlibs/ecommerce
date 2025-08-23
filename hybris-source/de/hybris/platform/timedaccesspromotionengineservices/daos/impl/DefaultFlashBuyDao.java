package de.hybris.platform.timedaccesspromotionengineservices.daos.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.promotionengineservices.model.ProductForPromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.timedaccesspromotionengineservices.daos.FlashBuyDao;
import de.hybris.platform.timedaccesspromotionengineservices.model.FlashBuyCouponModel;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.apache.log4j.Logger;

public class DefaultFlashBuyDao extends DefaultGenericDao<FlashBuyCouponModel> implements FlashBuyDao
{
    private static final String COUPON_ACTIVE = "active";
    private static final String CATALOG_VERSION_ACTIVE = "active";
    private static final String DROOLS_RULE_CURRENT_VERSION = "currentVersion";
    private static final Logger LOG = Logger.getLogger(DefaultFlashBuyDao.class);
    private static final String FIND_FLASHBUY_COUPON_BY_PROMOTIONCODE_QUERY = "SELECT {c:pk} FROM {FlashBuyCoupon AS c JOIN PromotionSourceRule AS p ON {p:pk} = {c:rule} JOIN RuleStatus AS rs ON {p:status} = {rs:pk}} WHERE {active} = ?active AND {rs:code} = '" + RuleStatus.PUBLISHED
                    .getCode() + "' AND {p:code} = ?promotionCode";
    private static final String FIND_PRODUCT_BY_PROMOTION_QUERY = "SELECT {p:pk} FROM {ProductForPromotionSourceRule AS pfr JOIN Product AS p ON {pfr:productCode} = {p:code} JOIN CatalogVersion AS cv ON {p:catalogVersion} = {cv:pk} AND {cv:active} = ?active} WHERE {pfr:promotion} = ?promotion";
    private static final String FIND_ALL_PRODUCTS_BY_PROMOTION_SOURCE_RULE =
                    "SELECT {p:pk} FROM {ProductForPromotionSourceRule AS pfr JOIN Product AS p ON {pfr:productCode} = {p:code} JOIN CatalogVersion AS cv ON {p:catalogVersion} = {cv:pk} AND {cv:active} = ?active JOIN PromotionSourceRule AS psr ON {psr:pk} = {pfr:rule} JOIN RuleStatus AS rs ON {psr:status} = {rs:pk} JOIN RuleBasedPromotion AS rbp ON {pfr:promotion} = {rbp:pk} JOIN DroolsRule AS dr ON {rbp:rule} = {dr:pk} AND {dr:currentVersion} = ?currentVersion} WHERE {rs:code} = '"
                                    + RuleStatus.PUBLISHED
                                    .getCode() + "' AND {psr:pk} = ?rule";
    private static final String FIND_PROMOTION_RULE_BY_PRODUCT = "SELECT {r.pk} as pr FROM {PromotionSourceRule as r}, {ProductForPromotionSourceRule as rel}, {RuleStatus as rs} WHERE {r.status} = {rs.pk} and {rs.code} = '" + RuleStatus.PUBLISHED
                    .getCode() + "' AND {rel.rule} = {r.pk} AND {rel.productCode} = ?productCode";
    private static final String FIND_PRODUCT_FOR_PROMOTION_RULE = "SELECT {rel.pk} as pr FROM {PromotionSourceRule as r}, {ProductForPromotionSourceRule as rel}, {RuleStatus as rs} WHERE {r.status} = {rs.pk} and {rs.code} = '" + RuleStatus.PUBLISHED
                    .getCode() + "' AND {rel.rule} = {r.pk} AND {r.code} = ?code";
    private static final String FIND_FLASHBUYCOUPON_BY_PRODUCTCODE = "SELECT {c:pk} as pr FROM {FlashBuyCoupon as c JOIN PromotionSourceRule AS p ON {p:pk} = {c:rule} JOIN RuleStatus AS rs ON {p:status} = {rs:pk}}  WHERE {c:active} = ?active AND {rs:code} = '" + RuleStatus.PUBLISHED
                    .getCode() + "' AND ({p:endDate} > ?now OR {p:endDate} is null) AND {c:product} = ?product ";


    public DefaultFlashBuyDao()
    {
        super("FlashBuyCoupon");
    }


    public Optional<FlashBuyCouponModel> findFlashBuyCouponByPromotionCode(String promotionCode)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_FLASHBUY_COUPON_BY_PROMOTIONCODE_QUERY);
        query.addQueryParameter("active", Boolean.TRUE);
        query.addQueryParameter("promotionCode", promotionCode);
        try
        {
            return Optional.of((FlashBuyCouponModel)getFlexibleSearchService().searchUnique(query));
        }
        catch(ModelNotFoundException e)
        {
            return Optional.empty();
        }
        catch(AmbiguousIdentifierException e)
        {
            LOG.warn("More than one FlashBuyCoupon Found, return empty.");
            return Optional.empty();
        }
    }


    public List<PromotionSourceRuleModel> findPromotionSourceRuleByProduct(String productCode)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_PROMOTION_RULE_BY_PRODUCT);
        query.addQueryParameter("productCode", productCode);
        return getFlexibleSearchService().search(query).getResult();
    }


    public Optional<ProductModel> findProductByPromotion(AbstractPromotionModel promotion)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {p:pk} FROM {ProductForPromotionSourceRule AS pfr JOIN Product AS p ON {pfr:productCode} = {p:code} JOIN CatalogVersion AS cv ON {p:catalogVersion} = {cv:pk} AND {cv:active} = ?active} WHERE {pfr:promotion} = ?promotion");
        query.addQueryParameter("active", Boolean.TRUE);
        query.addQueryParameter("promotion", promotion);
        try
        {
            return Optional.of((ProductModel)getFlexibleSearchService().searchUnique(query));
        }
        catch(ModelNotFoundException e)
        {
            return Optional.empty();
        }
    }


    public List<ProductForPromotionSourceRuleModel> findProductForPromotionSourceRules(PromotionSourceRuleModel rule)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_PRODUCT_FOR_PROMOTION_RULE);
        query.addQueryParameter("code", rule.getCode());
        return getFlexibleSearchService().search(query).getResult();
    }


    public List<ProductModel> findAllProductsByPromotionSourceRule(PromotionSourceRuleModel rule)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_ALL_PRODUCTS_BY_PROMOTION_SOURCE_RULE);
        query.addQueryParameter("active", Boolean.TRUE);
        query.addQueryParameter("currentVersion", Boolean.TRUE);
        query.addQueryParameter("rule", rule);
        return getFlexibleSearchService().search(query).getResult();
    }


    public List<FlashBuyCouponModel> findFlashBuyCouponByProduct(ProductModel product)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_FLASHBUYCOUPON_BY_PRODUCTCODE);
        query.addQueryParameter("active", Boolean.TRUE);
        query.addQueryParameter("now", new Date());
        query.addQueryParameter("product", product);
        return getFlexibleSearchService().search(query).getResult();
    }
}
