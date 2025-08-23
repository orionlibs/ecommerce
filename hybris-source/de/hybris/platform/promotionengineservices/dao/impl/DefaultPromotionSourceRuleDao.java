package de.hybris.platform.promotionengineservices.dao.impl;

import de.hybris.platform.promotionengineservices.dao.PromotionSourceRuleDao;
import de.hybris.platform.promotionengineservices.model.CatForPromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.CombinedCatsForRuleModel;
import de.hybris.platform.promotionengineservices.model.ExcludedCatForRuleModel;
import de.hybris.platform.promotionengineservices.model.ExcludedProductForRuleModel;
import de.hybris.platform.promotionengineservices.model.ProductForPromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;

public class DefaultPromotionSourceRuleDao extends AbstractItemDao implements PromotionSourceRuleDao
{
    protected static final String FILTER_BY_RULE_AND_MODULE_NAME = " AND EXISTS ({{ SELECT {dr.promotion} FROM {RuleBasedPromotion as rbp JOIN DroolsRule as dr ON {dr.pk} = {rbp.rule} JOIN DroolsKIEBase as kb ON {kb.pk} = {dr.kieBase} JOIN AbstractRule as rule ON {rule.pk} = {dr.sourceRule} } WHERE {kb.name} = ?name AND {rule.code} = ?ruleCode}})";
    protected static final String GET_ALL_PRODUCTS_FOR_RULE = "SELECT {pfp.pk} FROM {ProductForPromotionSourceRule as pfp JOIN AbstractRule as r ON {pfp.rule} = {r.pk} } WHERE {r.code} = ?ruleCode";
    protected static final String GET_ALL_PRODUCTS_FOR_RULE_AND_MODULE = "SELECT {pfp.pk} FROM {ProductForPromotionSourceRule as pfp JOIN AbstractRule as r ON {pfp.rule} = {r.pk} } WHERE {r.code} = ?ruleCode AND EXISTS ({{ SELECT {dr.promotion} FROM {RuleBasedPromotion as rbp JOIN DroolsRule as dr ON {dr.pk} = {rbp.rule} JOIN DroolsKIEBase as kb ON {kb.pk} = {dr.kieBase} JOIN AbstractRule as rule ON {rule.pk} = {dr.sourceRule} } WHERE {kb.name} = ?name AND {rule.code} = ?ruleCode}})";
    protected static final String GET_ALL_CATEGORIES_FOR_RULE = "SELECT {cfp.pk} FROM {CatForPromotionSourceRule as cfp JOIN AbstractRule as r ON {cfp.rule} = {r.pk} } WHERE {r.code} = ?ruleCode";
    protected static final String GET_ALL_CATEGORIES_FOR_RULE_AND_MODULE = "SELECT {cfp.pk} FROM {CatForPromotionSourceRule as cfp JOIN AbstractRule as r ON {cfp.rule} = {r.pk} } WHERE {r.code} = ?ruleCode AND EXISTS ({{ SELECT {dr.promotion} FROM {RuleBasedPromotion as rbp JOIN DroolsRule as dr ON {dr.pk} = {rbp.rule} JOIN DroolsKIEBase as kb ON {kb.pk} = {dr.kieBase} JOIN AbstractRule as rule ON {rule.pk} = {dr.sourceRule} } WHERE {kb.name} = ?name AND {rule.code} = ?ruleCode}})";
    protected static final String GET_ALL_EXCLUDED_CATEGORIES_FOR_RULE = "SELECT {cfp.pk} FROM {ExcludedCatForRule as cfp JOIN AbstractRule as r ON {cfp.rule} = {r.pk} } WHERE {r.code} = ?ruleCode";
    protected static final String GET_ALL_EXCLUDED_CATEGORIES_FOR_RULE_AND_MODULE = "SELECT {cfp.pk} FROM {ExcludedCatForRule as cfp JOIN AbstractRule as r ON {cfp.rule} = {r.pk} } WHERE {r.code} = ?ruleCode AND EXISTS ({{ SELECT {dr.promotion} FROM {RuleBasedPromotion as rbp JOIN DroolsRule as dr ON {dr.pk} = {rbp.rule} JOIN DroolsKIEBase as kb ON {kb.pk} = {dr.kieBase} JOIN AbstractRule as rule ON {rule.pk} = {dr.sourceRule} } WHERE {kb.name} = ?name AND {rule.code} = ?ruleCode}})";
    protected static final String GET_ALL_EXCLUDED_PRODUCTS_FOR_RULE = "SELECT {pfp.pk} FROM {ExcludedProductForRule as pfp JOIN AbstractRule as r ON {pfp.rule} = {r.pk} } WHERE {r.code} = ?ruleCode";
    protected static final String GET_ALL_EXCLUDED_PRODUCTS_FOR_RULE_AND_MODULE = "SELECT {pfp.pk} FROM {ExcludedProductForRule as pfp JOIN AbstractRule as r ON {pfp.rule} = {r.pk} } WHERE {r.code} = ?ruleCode AND EXISTS ({{ SELECT {dr.promotion} FROM {RuleBasedPromotion as rbp JOIN DroolsRule as dr ON {dr.pk} = {rbp.rule} JOIN DroolsKIEBase as kb ON {kb.pk} = {dr.kieBase} JOIN AbstractRule as rule ON {rule.pk} = {dr.sourceRule} } WHERE {kb.name} = ?name AND {rule.code} = ?ruleCode}})";
    protected static final String GET_ALL_COMBINED_CATS_FOR_RULE = "SELECT {cfp.pk} FROM {CombinedCatsForRule as cfp JOIN AbstractRule as r ON {cfp.rule} = {r.pk} } WHERE {r.code} = ?ruleCode";
    protected static final String GET_ALL_COMBINED_CATS_FOR_RULE_AND_MODULE = "SELECT {cfp.pk} FROM {CombinedCatsForRule as cfp JOIN AbstractRule as r ON {cfp.rule} = {r.pk} } WHERE {r.code} = ?ruleCode AND EXISTS ({{ SELECT {dr.promotion} FROM {RuleBasedPromotion as rbp JOIN DroolsRule as dr ON {dr.pk} = {rbp.rule} JOIN DroolsKIEBase as kb ON {kb.pk} = {dr.kieBase} JOIN AbstractRule as rule ON {rule.pk} = {dr.sourceRule} } WHERE {kb.name} = ?name AND {rule.code} = ?ruleCode}})";
    protected static final String GET_LAST_CONDITIONID_FOR_RULE = "SELECT max({conditionId}) FROM {CombinedCatsForRule} WHERE {rule} = ?rule";
    protected static final String FIND_PROMOTIONS_SELECT = "SELECT {rel.promotion} as pr FROM {PromotionSourceRule as r}, ";
    protected static final String FIND_PROMOTIONS_WHERE1 = "WHERE {rel.rule} = {r.pk} ";
    protected static final String FIND_PROMOTIONS_WHERE2 = "AND {r.status} =?statusPublished AND {r.excludeFromStorefrontDisplay} !=?excludeFromStorefrontDisplay AND {r.website} IN (?promotionGroups) ";
    protected static final String FIND_PROMOTIONS_WITHIN_START_END_DATES = "AND ( {r.startDate} <= ?nowDate OR {r.startDate} IS NULL) AND ( {r.endDate} >= ?nextMinuteDate OR {r.endDate} IS NULL) ";
    protected static final String FIND_PUBLISHED_PROMOTIONS = "AND {r.status} IN (?publishedPromotionStatuses) ";
    protected static final String FIND_PROMOTIONS_PROD_SELECT = "{ProductForPromotionSourceRule as rel} ";
    protected static final String FIND_PROMOTIONS_PROD_WHERE = "AND {rel.productCode} = ?productCode ";
    protected static final String FIND_PROMOTIONS_CAT_SELECT = "{CatForPromotionSourceRule as rel} ";
    protected static final String FIND_PROMOTIONS_CAT_WHERE = "AND {rel.categoryCode} IN (?categoryCodes) ";
    protected static final String FIND_PROMOTIONS_COMB_CAT_QUERY = "SELECT promotion as pr FROM ({{ SELECT {cc1.rule} as r1, {cc1.promotion} as promotion, {cc1.conditionId} as c1, count({cc1.categoryCode}) as count_cat1 FROM {PromotionSourceRule as r}, {CombinedCatsForRule as cc1} WHERE {cc1.categoryCode} in (?categoryCodes) AND {r.pk} = {cc1.rule} AND {r.status} =?statusPublished AND {r.excludeFromStorefrontDisplay} !=?excludeFromStorefrontDisplay AND {r.website} IN (?promotionGroups) AND ( {r.startDate} <= ?nowDate OR {r.startDate} IS NULL) AND ( {r.endDate} >= ?nextMinuteDate OR {r.endDate} IS NULL) GROUP BY {cc1.rule}, {cc1.promotion}, {cc1.conditionId} }}) a WHERE EXISTS ({{ SELECT {cc2.rule}, {cc2.promotion}, {cc2.conditionId}, count({cc2.categoryCode}) FROM {PromotionSourceRule as r}, {CombinedCatsForRule as cc2} WHERE {cc2.rule} = r1 AND {cc2.conditionId} = c1 AND {r.pk} = {cc2.rule} AND {r.status} =?statusPublished AND {r.excludeFromStorefrontDisplay} !=?excludeFromStorefrontDisplay AND {r.website} IN (?promotionGroups) AND ( {r.startDate} <= ?nowDate OR {r.startDate} IS NULL) AND ( {r.endDate} >= ?nextMinuteDate OR {r.endDate} IS NULL) GROUP BY {cc2.rule}, {cc2.promotion}, {cc2.conditionId} HAVING count({cc2.categoryCode}) = count_cat1 }}) AND NOT EXISTS ({{ SELECT 1 FROM {ExcludedProductForRule} WHERE {rule} = r1 AND {productCode} = ?productCode }}) AND NOT EXISTS ({{ SELECT 1 FROM {ExcludedCatForRule} WHERE {rule} = r1 AND {categoryCode} IN (?categoryCodes) }}) ";
    protected static final String EXCLUDE_PRODUCTS_WHERE = "AND NOT EXISTS ({{ SELECT 1 FROM {ExcludedProductForRule} WHERE {rule} = {rel.rule} AND {productCode} = ?productCode }}) ";
    protected static final String EXCLUDE_CAT_WHERE = "AND NOT EXISTS ({{ SELECT 1 FROM {ExcludedCatForRule} WHERE {rule} = {rel.rule} AND {categoryCode} IN (?categoryCodes) }}) ";
    protected static final String EXCLUDE_NULL_PRODUCT_PROMOTIONS = "AND {rel.promotion} IS NOT NULL ";
    protected static final String EXCLUDE_NULL_RESULTS_PROMOTIONS = "WHERE x.pr IS NOT NULL ";
    protected static final String FIND_PROMOTIONS_PROD_QUERY = "SELECT {rel.promotion} as pr FROM {PromotionSourceRule as r}, {ProductForPromotionSourceRule as rel} WHERE {rel.rule} = {r.pk} AND {r.status} =?statusPublished AND {r.excludeFromStorefrontDisplay} !=?excludeFromStorefrontDisplay AND {r.website} IN (?promotionGroups) AND {rel.productCode} = ?productCode AND ( {r.startDate} <= ?nowDate OR {r.startDate} IS NULL) AND ( {r.endDate} >= ?nextMinuteDate OR {r.endDate} IS NULL) AND {r.status} IN (?publishedPromotionStatuses) AND {rel.promotion} IS NOT NULL ";
    protected static final String FIND_PROMOTIONS_CAT_QUERY = "SELECT {rel.promotion} as pr FROM {PromotionSourceRule as r}, {CatForPromotionSourceRule as rel} WHERE {rel.rule} = {r.pk} AND {r.status} =?statusPublished AND {r.excludeFromStorefrontDisplay} !=?excludeFromStorefrontDisplay AND {r.website} IN (?promotionGroups) AND {rel.categoryCode} IN (?categoryCodes) AND ( {r.startDate} <= ?nowDate OR {r.startDate} IS NULL) AND ( {r.endDate} >= ?nextMinuteDate OR {r.endDate} IS NULL) AND NOT EXISTS ({{ SELECT 1 FROM {ExcludedProductForRule} WHERE {rule} = {rel.rule} AND {productCode} = ?productCode }}) AND NOT EXISTS ({{ SELECT 1 FROM {ExcludedCatForRule} WHERE {rule} = {rel.rule} AND {categoryCode} IN (?categoryCodes) }}) ";
    protected static final String FIND_PROMOTIONS_UNION1 = "SELECT x.pr FROM ( {{";
    protected static final String FIND_PROMOTIONS_UNION2 = "}} UNION {{";
    protected static final String FIND_PROMOTIONS_UNION3 = "}}) x WHERE x.pr IS NOT NULL ";
    protected static final String RULE_CODE_PARAM = "ruleCode";


    public List<ProductForPromotionSourceRuleModel> findAllProductForPromotionSourceRule(PromotionSourceRuleModel rule, String baseName)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("ruleCode", rule.getCode());
        params.put("name", baseName);
        SearchResult<ProductForPromotionSourceRuleModel> searchResult = getFlexibleSearchService().search(
                        "SELECT {pfp.pk} FROM {ProductForPromotionSourceRule as pfp JOIN AbstractRule as r ON {pfp.rule} = {r.pk} } WHERE {r.code} = ?ruleCode AND EXISTS ({{ SELECT {dr.promotion} FROM {RuleBasedPromotion as rbp JOIN DroolsRule as dr ON {dr.pk} = {rbp.rule} JOIN DroolsKIEBase as kb ON {kb.pk} = {dr.kieBase} JOIN AbstractRule as rule ON {rule.pk} = {dr.sourceRule} } WHERE {kb.name} = ?name AND {rule.code} = ?ruleCode}})",
                        params);
        return searchResult.getResult();
    }


    public List<CatForPromotionSourceRuleModel> findAllCatForPromotionSourceRule(PromotionSourceRuleModel rule, String baseName)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("ruleCode", rule.getCode());
        params.put("name", baseName);
        SearchResult<CatForPromotionSourceRuleModel> searchResult = getFlexibleSearchService().search(
                        "SELECT {cfp.pk} FROM {CatForPromotionSourceRule as cfp JOIN AbstractRule as r ON {cfp.rule} = {r.pk} } WHERE {r.code} = ?ruleCode AND EXISTS ({{ SELECT {dr.promotion} FROM {RuleBasedPromotion as rbp JOIN DroolsRule as dr ON {dr.pk} = {rbp.rule} JOIN DroolsKIEBase as kb ON {kb.pk} = {dr.kieBase} JOIN AbstractRule as rule ON {rule.pk} = {dr.sourceRule} } WHERE {kb.name} = ?name AND {rule.code} = ?ruleCode}})",
                        params);
        return searchResult.getResult();
    }


    public List<ExcludedCatForRuleModel> findAllExcludedCatForPromotionSourceRule(PromotionSourceRuleModel rule, String baseName)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("ruleCode", rule.getCode());
        params.put("name", baseName);
        SearchResult<ExcludedCatForRuleModel> searchResult = getFlexibleSearchService().search(
                        "SELECT {cfp.pk} FROM {ExcludedCatForRule as cfp JOIN AbstractRule as r ON {cfp.rule} = {r.pk} } WHERE {r.code} = ?ruleCode AND EXISTS ({{ SELECT {dr.promotion} FROM {RuleBasedPromotion as rbp JOIN DroolsRule as dr ON {dr.pk} = {rbp.rule} JOIN DroolsKIEBase as kb ON {kb.pk} = {dr.kieBase} JOIN AbstractRule as rule ON {rule.pk} = {dr.sourceRule} } WHERE {kb.name} = ?name AND {rule.code} = ?ruleCode}})",
                        params);
        return searchResult.getResult();
    }


    public List<CombinedCatsForRuleModel> findAllCombinedCatsForRule(PromotionSourceRuleModel rule, String baseName)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("ruleCode", rule.getCode());
        params.put("name", baseName);
        SearchResult<CombinedCatsForRuleModel> searchResult = getFlexibleSearchService().search(
                        "SELECT {cfp.pk} FROM {CombinedCatsForRule as cfp JOIN AbstractRule as r ON {cfp.rule} = {r.pk} } WHERE {r.code} = ?ruleCode AND EXISTS ({{ SELECT {dr.promotion} FROM {RuleBasedPromotion as rbp JOIN DroolsRule as dr ON {dr.pk} = {rbp.rule} JOIN DroolsKIEBase as kb ON {kb.pk} = {dr.kieBase} JOIN AbstractRule as rule ON {rule.pk} = {dr.sourceRule} } WHERE {kb.name} = ?name AND {rule.code} = ?ruleCode}})",
                        params);
        return searchResult.getResult();
    }


    public List<ExcludedProductForRuleModel> findAllExcludedProductForPromotionSourceRule(PromotionSourceRuleModel rule, String baseName)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("ruleCode", rule.getCode());
        params.put("name", baseName);
        SearchResult<ExcludedProductForRuleModel> searchResult = getFlexibleSearchService().search(
                        "SELECT {pfp.pk} FROM {ExcludedProductForRule as pfp JOIN AbstractRule as r ON {pfp.rule} = {r.pk} } WHERE {r.code} = ?ruleCode AND EXISTS ({{ SELECT {dr.promotion} FROM {RuleBasedPromotion as rbp JOIN DroolsRule as dr ON {dr.pk} = {rbp.rule} JOIN DroolsKIEBase as kb ON {kb.pk} = {dr.kieBase} JOIN AbstractRule as rule ON {rule.pk} = {dr.sourceRule} } WHERE {kb.name} = ?name AND {rule.code} = ?ruleCode}})",
                        params);
        return searchResult.getResult();
    }


    public List<RuleBasedPromotionModel> findPromotions(Collection<PromotionGroupModel> promotionGroups, String productCode, Set<String> categoryCodes)
    {
        return findPromotions(promotionGroups, productCode, categoryCodes, new Date());
    }


    public Integer findLastConditionIdForRule(PromotionSourceRuleModel rule)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("rule", rule);
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT max({conditionId}) FROM {CombinedCatsForRule} WHERE {rule} = ?rule", params);
        query.setResultClassList(Collections.singletonList(Integer.class));
        SearchResult<Integer> searchResult = getFlexibleSearchService().search(query);
        return searchResult.getResult().get(0);
    }


    protected List<RuleBasedPromotionModel> findPromotions(Collection<PromotionGroupModel> promotionGroups, String productCode, Set<String> categoryCodes, Date currentDate)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("productCode", productCode);
        params.put("statusPublished", RuleStatus.PUBLISHED);
        params.put("promotionGroups", promotionGroups);
        params.put("publishedPromotionStatuses", new HashSet(Collections.singletonList(RuleStatus.PUBLISHED)));
        params.put("nowDate", Date.from(currentDate.toInstant().truncatedTo(ChronoUnit.MINUTES)));
        params.put("nextMinuteDate",
                        Date.from(currentDate.toInstant().plus(1L, ChronoUnit.MINUTES).truncatedTo(ChronoUnit.MINUTES)));
        params.put("excludeFromStorefrontDisplay", Boolean.TRUE);
        StringBuilder stringBuilder = new StringBuilder(200);
        stringBuilder.append(
                        "SELECT {rel.promotion} as pr FROM {PromotionSourceRule as r}, {ProductForPromotionSourceRule as rel} WHERE {rel.rule} = {r.pk} AND {r.status} =?statusPublished AND {r.excludeFromStorefrontDisplay} !=?excludeFromStorefrontDisplay AND {r.website} IN (?promotionGroups) AND {rel.productCode} = ?productCode AND ( {r.startDate} <= ?nowDate OR {r.startDate} IS NULL) AND ( {r.endDate} >= ?nextMinuteDate OR {r.endDate} IS NULL) AND {r.status} IN (?publishedPromotionStatuses) AND {rel.promotion} IS NOT NULL ");
        if(CollectionUtils.isNotEmpty(categoryCodes))
        {
            params.put("categoryCodes", categoryCodes);
            stringBuilder.insert(0, "SELECT x.pr FROM ( {{");
            stringBuilder.append("}} UNION {{");
            stringBuilder.append(
                            "SELECT {rel.promotion} as pr FROM {PromotionSourceRule as r}, {CatForPromotionSourceRule as rel} WHERE {rel.rule} = {r.pk} AND {r.status} =?statusPublished AND {r.excludeFromStorefrontDisplay} !=?excludeFromStorefrontDisplay AND {r.website} IN (?promotionGroups) AND {rel.categoryCode} IN (?categoryCodes) AND ( {r.startDate} <= ?nowDate OR {r.startDate} IS NULL) AND ( {r.endDate} >= ?nextMinuteDate OR {r.endDate} IS NULL) AND NOT EXISTS ({{ SELECT 1 FROM {ExcludedProductForRule} WHERE {rule} = {rel.rule} AND {productCode} = ?productCode }}) AND NOT EXISTS ({{ SELECT 1 FROM {ExcludedCatForRule} WHERE {rule} = {rel.rule} AND {categoryCode} IN (?categoryCodes) }}) ");
            stringBuilder.append("}} UNION {{");
            stringBuilder.append(
                            "SELECT promotion as pr FROM ({{ SELECT {cc1.rule} as r1, {cc1.promotion} as promotion, {cc1.conditionId} as c1, count({cc1.categoryCode}) as count_cat1 FROM {PromotionSourceRule as r}, {CombinedCatsForRule as cc1} WHERE {cc1.categoryCode} in (?categoryCodes) AND {r.pk} = {cc1.rule} AND {r.status} =?statusPublished AND {r.excludeFromStorefrontDisplay} !=?excludeFromStorefrontDisplay AND {r.website} IN (?promotionGroups) AND ( {r.startDate} <= ?nowDate OR {r.startDate} IS NULL) AND ( {r.endDate} >= ?nextMinuteDate OR {r.endDate} IS NULL) GROUP BY {cc1.rule}, {cc1.promotion}, {cc1.conditionId} }}) a WHERE EXISTS ({{ SELECT {cc2.rule}, {cc2.promotion}, {cc2.conditionId}, count({cc2.categoryCode}) FROM {PromotionSourceRule as r}, {CombinedCatsForRule as cc2} WHERE {cc2.rule} = r1 AND {cc2.conditionId} = c1 AND {r.pk} = {cc2.rule} AND {r.status} =?statusPublished AND {r.excludeFromStorefrontDisplay} !=?excludeFromStorefrontDisplay AND {r.website} IN (?promotionGroups) AND ( {r.startDate} <= ?nowDate OR {r.startDate} IS NULL) AND ( {r.endDate} >= ?nextMinuteDate OR {r.endDate} IS NULL) GROUP BY {cc2.rule}, {cc2.promotion}, {cc2.conditionId} HAVING count({cc2.categoryCode}) = count_cat1 }}) AND NOT EXISTS ({{ SELECT 1 FROM {ExcludedProductForRule} WHERE {rule} = r1 AND {productCode} = ?productCode }}) AND NOT EXISTS ({{ SELECT 1 FROM {ExcludedCatForRule} WHERE {rule} = r1 AND {categoryCode} IN (?categoryCodes) }}) ");
            stringBuilder.append("}}) x WHERE x.pr IS NOT NULL ");
        }
        SearchResult<RuleBasedPromotionModel> searchResult = getFlexibleSearchService().search(stringBuilder.toString(), params);
        return searchResult.getResult();
    }
}
