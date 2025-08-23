package de.hybris.platform.couponservices.dao.impl;

import de.hybris.platform.couponservices.dao.CouponDao;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.couponservices.model.CodeGenerationConfigurationModel;
import de.hybris.platform.couponservices.model.CustomerCouponForPromotionSourceRuleModel;
import de.hybris.platform.couponservices.model.MultiCodeCouponModel;
import de.hybris.platform.couponservices.model.SingleCodeCouponModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultCouponDao extends AbstractItemDao implements CouponDao
{
    private static final String COUPONID = "couponId";
    private static final String GET_COUPON_BY_ID_QUERY = "SELECT {" + Item.PK + "} FROM   {AbstractCoupon} WHERE  {couponId} = ?couponId";
    private static final String GET_SINGLECODE_COUPON_QUERY = "SELECT {" + Item.PK + "} FROM   {SingleCodeCoupon} WHERE  {couponId} = ?couponId";
    private static final String GET_MULTICODE_COUPON_QUERY = "SELECT {" + Item.PK + "} FROM   {MultiCodeCoupon} WHERE  {couponId} = ?couponId";
    private static final String GET_MULTICODE_COUPON_BY_CONFIG_QUERY = "SELECT {" + Item.PK + "} FROM   {MultiCodeCoupon} WHERE  {codeGenerationConfiguration} = ?config";
    private static final String FIND_ALL_COUPON_FORPROMOTIONSOURCERULE_QUERY = "SELECT {pk} FROM {CustomerCouponForPromotionSourceRule} WHERE {rule} = ?rule";
    private static final String FIND_ALL_COUPON_FORPROMOTIONSOURCERULE_FQL = "SELECT {pk} FROM {CustomerCouponForPromotionSourceRule} WHERE {rule} = ?rule AND EXISTS ({{SELECT {dr.promotion} FROM {RuleBasedPromotion as rbp JOIN DroolsRule as dr ON {dr.pk} = {rbp.rule} JOIN DroolsKIEBase as kb ON {kb.pk} = {dr.kieBase} JOIN DroolsKIEModule as km ON {kb.kieModule} = {km.pk} JOIN AbstractRule as rule ON {rule.pk} = {dr.sourceRule}} WHERE {km.name} = ?name AND {rule.pk} = ?rule }})";
    private static final String FIND_PROMOTION_RULE_FOR_COUPONCODE = "SELECT {r.pk} as pr FROM {PromotionSourceRule as r}, {CustomerCouponForPromotionSourceRule as rel}, {RuleStatus as rs} WHERE {r.status} = {rs.pk} AND {rs.code} = '" + RuleStatus.PUBLISHED
                    .getCode() + "' AND {rel.rule} = {r.pk} AND {rel.customerCouponCode} = ?couponCode";


    public AbstractCouponModel findCouponById(String couponId)
    {
        ServicesUtil.validateParameterNotNull(couponId, "String couponId cannot be null");
        Map<String, String> params = Collections.singletonMap("couponId", couponId);
        return (AbstractCouponModel)getFlexibleSearchService().searchUnique(new FlexibleSearchQuery(GET_COUPON_BY_ID_QUERY, params));
    }


    public SingleCodeCouponModel findSingleCodeCouponById(String couponId)
    {
        ServicesUtil.validateParameterNotNull(couponId, "String couponId must not be null");
        Map<String, String> params = Collections.singletonMap("couponId", couponId);
        return (SingleCodeCouponModel)getFlexibleSearchService().searchUnique(new FlexibleSearchQuery(GET_SINGLECODE_COUPON_QUERY, params));
    }


    public MultiCodeCouponModel findMultiCodeCouponById(String couponId)
    {
        ServicesUtil.validateParameterNotNull(couponId, "String couponId must not be null");
        Map<String, String> params = Collections.singletonMap("couponId", couponId);
        return (MultiCodeCouponModel)getFlexibleSearchService().searchUnique(new FlexibleSearchQuery(GET_MULTICODE_COUPON_QUERY, params));
    }


    public List<MultiCodeCouponModel> findMultiCodeCouponsByCodeConfiguration(CodeGenerationConfigurationModel config)
    {
        ServicesUtil.validateParameterNotNull(config, "CouponCodeGenerationConfiguration config must not be null");
        Map<String, Object> params = Collections.singletonMap("config", config);
        return getFlexibleSearchService().search(GET_MULTICODE_COUPON_BY_CONFIG_QUERY, params).getResult();
    }


    public List<CustomerCouponForPromotionSourceRuleModel> findAllCouponForSourceRules(PromotionSourceRuleModel rule)
    {
        return getFlexibleSearchService().search("SELECT {pk} FROM {CustomerCouponForPromotionSourceRule} WHERE {rule} = ?rule",
                        Collections.singletonMap("rule", rule)).getResult();
    }


    public List<CustomerCouponForPromotionSourceRuleModel> findAllCouponForSourceRules(PromotionSourceRuleModel rule, String moduleName)
    {
        Map<String, Object> params = new HashMap<>(0);
        params.put("rule", rule);
        params.put("name", moduleName);
        return getFlexibleSearchService()
                        .search("SELECT {pk} FROM {CustomerCouponForPromotionSourceRule} WHERE {rule} = ?rule AND EXISTS ({{SELECT {dr.promotion} FROM {RuleBasedPromotion as rbp JOIN DroolsRule as dr ON {dr.pk} = {rbp.rule} JOIN DroolsKIEBase as kb ON {kb.pk} = {dr.kieBase} JOIN DroolsKIEModule as km ON {kb.kieModule} = {km.pk} JOIN AbstractRule as rule ON {rule.pk} = {dr.sourceRule}} WHERE {km.name} = ?name AND {rule.pk} = ?rule }})",
                                        params)
                        .getResult();
    }


    public List<PromotionSourceRuleModel> findPromotionSourceRuleByCouponCode(String code)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_PROMOTION_RULE_FOR_COUPONCODE);
        query.addQueryParameter("couponCode", code);
        return getFlexibleSearchService().search(query).getResult();
    }
}
