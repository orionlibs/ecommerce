package de.hybris.platform.ruleengineservices.rao.providers.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ruleengineservices.calculation.RuleEngineCalculationService;
import de.hybris.platform.ruleengineservices.rao.AbstractOrderRAO;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.rao.PaymentModeRAO;
import de.hybris.platform.ruleengineservices.rao.UserGroupRAO;
import de.hybris.platform.ruleengineservices.rao.UserRAO;
import de.hybris.platform.ruleengineservices.rao.providers.RAOFactsExtractor;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCartRAOProvider extends AbstractExpandedRAOProvider<AbstractOrderModel, CartRAO>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCartRAOProvider.class);
    public static final String INCLUDE_CART = "INCLUDE_CART";
    public static final String EXPAND_ENTRIES = "EXPAND_ENTRIES";
    public static final String EXPAND_PRODUCTS = "EXPAND_PRODUCTS";
    public static final String EXPAND_DISCOUNTS = "EXPAND_DISCOUNTS";
    public static final String AVAILABLE_DELIVERY_MODES = "EXPAND_AVAILABLE_DELIVERY_MODES";
    public static final String EXPAND_USERS = "EXPAND_USERS";
    public static final String EXPAND_PAYMENT_MODE = "EXPAND_PAYMENT_MODE";
    public static final String EXPAND_CATEGORIES = "EXPAND_CATEGORIES";
    private Converter<AbstractOrderModel, CartRAO> cartRaoConverter;
    private RuleEngineCalculationService ruleEngineCalculationService;


    protected CartRAO createRAO(AbstractOrderModel cart)
    {
        CartRAO rao = (CartRAO)getCartRaoConverter().convert(cart);
        getRuleEngineCalculationService().calculateTotals((AbstractOrderRAO)rao);
        return rao;
    }


    protected Set<Object> expandRAO(CartRAO cart, Collection<String> options)
    {
        Set<Object> facts = new LinkedHashSet(super.expandRAO(cart, options));
        options.forEach(option -> expandRAOForOption(cart, facts, option));
        return facts;
    }


    protected void expandRAOForOption(CartRAO cart, Set<Object> facts, String option)
    {
        Set<OrderEntryRAO> entries = cart.getEntries();
        switch(option)
        {
            case "INCLUDE_CART":
                facts.add(cart);
            case "EXPAND_DISCOUNTS":
                facts.addAll(cart.getDiscountValues());
            case "EXPAND_ENTRIES":
                addEntries(facts, entries);
            case "EXPAND_PRODUCTS":
                return;
            case "EXPAND_USERS":
                addUserGroups(facts, cart.getUser());
            case "EXPAND_PAYMENT_MODE":
                addPaymentMode(facts, cart.getPaymentMode());
        }
        LOGGER.debug("Unknown option: '{}'. Skipping", option);
    }


    protected void addUserGroups(Set<Object> facts, UserRAO userRAO)
    {
        if(Objects.nonNull(userRAO))
        {
            facts.add(userRAO);
            Set<UserGroupRAO> groups = userRAO.getGroups();
            if(CollectionUtils.isNotEmpty(groups))
            {
                facts.addAll(groups);
            }
        }
    }


    protected void addPaymentMode(Set<Object> facts, PaymentModeRAO paymentModeRAO)
    {
        if(Objects.nonNull(paymentModeRAO))
        {
            facts.add(paymentModeRAO);
        }
    }


    protected void addEntries(Set<Object> facts, Set<OrderEntryRAO> entries)
    {
        if(CollectionUtils.isNotEmpty(entries))
        {
            facts.addAll(entries);
        }
    }


    protected Predicate<RAOFactsExtractor> isEnabled(Collection<String> options)
    {
        return e -> (StringUtils.isNotEmpty(e.getTriggeringOption()) && options.contains(e.getTriggeringOption()));
    }


    protected Converter<AbstractOrderModel, CartRAO> getCartRaoConverter()
    {
        return this.cartRaoConverter;
    }


    @Required
    public void setCartRaoConverter(Converter<AbstractOrderModel, CartRAO> cartRaoConverter)
    {
        this.cartRaoConverter = cartRaoConverter;
    }


    protected RuleEngineCalculationService getRuleEngineCalculationService()
    {
        return this.ruleEngineCalculationService;
    }


    @Required
    public void setRuleEngineCalculationService(RuleEngineCalculationService ruleEngineCalculationService)
    {
        this.ruleEngineCalculationService = ruleEngineCalculationService;
    }
}
