package de.hybris.platform.ruledefinitions.conditions;

import de.hybris.platform.ruleengineservices.compiler.RuleConditionTranslator;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.fest.util.Collections;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractRuleConditionTranslator implements RuleConditionTranslator
{
    protected static final String OPERATOR_PARAM = "operator";
    protected static final String VALUE_PARAM = "value";
    protected static final String CART_RAO_CURRENCY_ATTRIBUTE = "currencyIsoCode";
    protected static final String CART_RAO_ENTRIES_ATTRIBUTE = "entries";
    protected static final String PRODUCT_CONSUMED_RAO_ENTRY_ATTRIBUTE = "orderEntry";
    protected static final String ORDER_ENTRY_RAO_BASE_PRICE_ATTRIBUTE = "basePrice";
    protected static final String QUANTITY_PARAM = "quantity";
    protected static final String AVAILABLE_QUANTITY_PARAM = "availableQuantity";
    protected static final String CATEGORIES_OPERATOR_PARAM = "categories_operator";
    protected static final String CATEGORIES_PARAM = "categories";
    protected static final String ORDER_ENTRY_RAO_PRODUCT_CODE_ATTRIBUTE = "productCode";
    protected static final String ORDER_ENTRY_RAO_CATEGORY_CODES_ATTRIBUTE = "categoryCodes";
    protected static final String PRODUCTS_PARAM = "products";
    protected static final String BASE_PRODUCT_CODES_ATTRIBUTE = "baseProductCodes";
    private RuleConditionConsumptionSupport consumptionSupport;


    protected boolean verifyAllPresent(Object... objects)
    {
        boolean isPresent = true;
        if(ArrayUtils.isNotEmpty(objects))
        {
            isPresent = Arrays.<Object>stream(objects).map(this::covertToNullIfEmptyCollection).map(this::covertToNullIfEmptyMap).noneMatch(Objects::isNull);
        }
        return isPresent;
    }


    protected boolean verifyAnyPresent(Object... objects)
    {
        boolean anyPresent = true;
        if(ArrayUtils.isNotEmpty(objects))
        {
            anyPresent = Arrays.<Object>stream(objects).map(this::covertToNullIfEmptyCollection).map(this::covertToNullIfEmptyMap).anyMatch(Objects::nonNull);
        }
        return anyPresent;
    }


    protected Object covertToNullIfEmptyCollection(Object seedObject)
    {
        return (seedObject instanceof Collection && Collections.isEmpty((Collection)seedObject)) ? null : seedObject;
    }


    protected Object covertToNullIfEmptyMap(Object seedObject)
    {
        return (seedObject instanceof Map && MapUtils.isEmpty((Map)seedObject)) ? null : seedObject;
    }


    protected RuleConditionConsumptionSupport getConsumptionSupport()
    {
        return this.consumptionSupport;
    }


    @Required
    public void setConsumptionSupport(RuleConditionConsumptionSupport consumptionSupport)
    {
        this.consumptionSupport = consumptionSupport;
    }
}
