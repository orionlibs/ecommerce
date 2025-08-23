/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.retail.sapppspricing.impl;

import com.sap.retail.sapppspricing.PPSConfigService;
import com.sap.retail.sapppspricing.PricingBackend;
import com.sap.retail.sapppspricing.SapPPSPricingRuntimeException;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.impl.DefaultCalculationService;
import de.hybris.platform.order.strategies.calculation.FindDiscountValuesStrategy;
import de.hybris.platform.order.strategies.calculation.OrderRequiresCalculationStrategy;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.PriceValue;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.client.RestClientException;

/**
 * Order calculation service using PPS if set to active. Otherwise the Default hybris Logic is used.
 */
public class DefaultPPSCalculationService extends DefaultCalculationService
{
    private static final long serialVersionUID = -8580425879363594073L;
    // The following attributes are introduced because in the super class they are private and no getter exists
    private transient OrderRequiresCalculationStrategy orderRequiresCalculationStrategy;
    private transient CommonI18NService commonI18NService;
    private transient PricingBackend pricingBackend;
    private transient PPSConfigService configService;
    private List<FindDiscountValuesStrategy> findPPSDiscountsStrategies;
    final List<FindDiscountValuesStrategy> discountStrategies = new ArrayList<>();


    @Override
    public void calculate(final AbstractOrderModel order) throws CalculationException
    {
        if(getConfigService().isPpsActive(order))
        {
            setDiscountStrategiesForPPSActive();
            if(orderRequiresCalculationStrategy.requiresCalculation(order))
            {
                // update prices from sap backend.
                updateOrderFromPPS(order);
                super.recalculate(order);
            }
        }
        else
        {
            setDiscountStrategiesForPPSNonActive();
            super.calculate(order);
        }
    }


    @Override
    public void recalculate(final AbstractOrderModel order) throws CalculationException
    {
        if(getConfigService().isPpsActive(order))
        {
            setDiscountStrategiesForPPSActive();
            // update prices from sap PPS backend.
            updateOrderFromPPS(order);
        }
        else
        {
            setDiscountStrategiesForPPSNonActive();
        }
        super.recalculate(order);
    }


    @Override
    public void calculateTotals(final AbstractOrderModel order, final boolean recalculate) throws CalculationException
    {
        if(getConfigService().isPpsActive(order))
        {
            setDiscountStrategiesForPPSActive();
            // update prices from sap backend.
            updateOrderFromPPS(order);
            super.calculateTotals(order, recalculate, calculateSubtotal(order, recalculate));
        }
        else
        {
            setDiscountStrategiesForPPSNonActive();
            super.calculateTotals(order, recalculate);
        }
    }


    protected void updateOrderFromPPS(final AbstractOrderModel order) throws CalculationException
    {
        // set order currency to session currency
        order.setCurrency(commonI18NService.getCurrentCurrency());
        try
        {
            getPricingBackend().readPricesForCart(order);
        }
        catch(final RestClientException | SapPPSPricingRuntimeException e)
        {
            throw new CalculationException("Could not calculate order " + order.getCode(), e);
        }
    }


    // super method uses a strategy for finding discount values on item level.
    // This is not used for PPS - here the PPS is the only strategy
    @Override
    protected List<DiscountValue> findDiscountValues(final AbstractOrderEntryModel entry) throws CalculationException
    {
        if(!getConfigService().isPpsActive(entry.getOrder()))
        {
            setDiscountStrategiesForPPSNonActive();
            return super.findDiscountValues(entry);
        }
        else
        {
            setDiscountStrategiesForPPSActive();
        }
        return entry.getDiscountValues();
    }


    // super method uses a strategy for finding base price.
    // This is not used for PPS - here the PPS is the only strategy
    @Override
    protected PriceValue findBasePrice(final AbstractOrderEntryModel entry) throws CalculationException
    {
        final AbstractOrderModel order = entry.getOrder();
        if(!getConfigService().isPpsActive(order))
        {
            setDiscountStrategiesForPPSNonActive();
            return super.findBasePrice(entry);
        }
        else
        {
            setDiscountStrategiesForPPSActive();
        }
        return new PriceValue(order.getCurrency().getIsocode(), entry.getBasePrice().doubleValue(), order.getNet().booleanValue());
    }


    private void setDiscountStrategiesForPPSActive()
    {
        setFindDiscountsStrategies(discountStrategies);
    }


    private void setDiscountStrategiesForPPSNonActive()
    {
        setFindDiscountsStrategies(getFindPPSDiscountsStrategies());
    }


    public CommonI18NService getCommonI18NService()
    {
        return commonI18NService;
    }


    @Override
    public void setCommonI18NService(final CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
        super.setCommonI18NService(commonI18NService);
    }


    public OrderRequiresCalculationStrategy getOrderRequiresCalculationStrategy()
    {
        return orderRequiresCalculationStrategy;
    }


    @Override
    public void setOrderRequiresCalculationStrategy(final OrderRequiresCalculationStrategy orderRequiresCalculationStrategy)
    {
        super.setOrderRequiresCalculationStrategy(orderRequiresCalculationStrategy);
        this.orderRequiresCalculationStrategy = orderRequiresCalculationStrategy;
    }


    public PricingBackend getPricingBackend()
    {
        return pricingBackend;
    }


    public void setPricingBackend(final PricingBackend pricingBackend)
    {
        this.pricingBackend = pricingBackend;
    }


    public PPSConfigService getConfigService()
    {
        return configService;
    }


    public void setConfigService(final PPSConfigService configService)
    {
        this.configService = configService;
    }


    /**
     * @return the findPPSDiscountsStrategies
     */
    public List<FindDiscountValuesStrategy> getFindPPSDiscountsStrategies()
    {
        return findPPSDiscountsStrategies;
    }


    /**
     * @param findPPSDiscountsStrategies the findPPSDiscountsStrategies to set
     */
    public void setFindPPSDiscountsStrategies(List<FindDiscountValuesStrategy> findPPSDiscountsStrategies)
    {
        this.findPPSDiscountsStrategies = findPPSDiscountsStrategies;
    }
}
