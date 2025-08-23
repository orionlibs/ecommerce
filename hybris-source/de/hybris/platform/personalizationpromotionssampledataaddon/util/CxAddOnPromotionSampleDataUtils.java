package de.hybris.platform.personalizationpromotionssampledataaddon.util;

import de.hybris.platform.core.Registry;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;

public final class CxAddOnPromotionSampleDataUtils
{
    private CxAddOnPromotionSampleDataUtils()
    {
        throw new IllegalStateException("CxAddOnPromotionSampleDataUtils is an utility class");
    }


    public static final boolean isPromotionSourceRuleUnmodifiable(String code)
    {
        FlexibleSearchService flexibleSearchService = (FlexibleSearchService)Registry.getApplicationContext().getBean("flexibleSearchService", FlexibleSearchService.class);
        String query = "SELECT {pk}  FROM {PromotionSourceRule}  WHERE {code} = ?promotionCode  AND {status} <> ?status ";
        Map<String, Object> params = new HashMap<>();
        params.put("promotionCode", code);
        params.put("status", RuleStatus.UNPUBLISHED);
        SearchResult<PromotionSourceRuleModel> searchResult = flexibleSearchService.search("SELECT {pk}  FROM {PromotionSourceRule}  WHERE {code} = ?promotionCode  AND {status} <> ?status ", params);
        List<PromotionSourceRuleModel> publishedRules = searchResult.getResult();
        return CollectionUtils.isNotEmpty(publishedRules);
    }
}
