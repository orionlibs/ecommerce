/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.labels.labelproviders;

import com.hybris.backoffice.labels.LabelHandler;
import com.hybris.cockpitng.labels.LabelProvider;
import com.hybris.cockpitng.labels.LabelService;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import org.springframework.beans.factory.annotation.Required;

public class AbstractOrderEntryLabelProvider implements LabelProvider<AbstractOrderEntryModel>
{
    public static final String SEPARATOR = " - ";
    private LabelService labelService;
    private LabelHandler<Double, CurrencyModel> priceLabelHandler;


    @Override
    public String getLabel(final AbstractOrderEntryModel orderEntry)
    {
        final StringBuilder builder = new StringBuilder(orderEntry.getOrder().getCode());
        builder.append('.').append(getLabelService().getObjectLabel(orderEntry.getEntryNumber())).append(" : ")
                        .append(getLabelService()
                                        .getObjectLabel(orderEntry.getProduct().getName() != null ? orderEntry.getProduct().getName() : ""))
                        .append('[').append(getLabelService().getObjectLabel(orderEntry.getProduct().getCode())).append(']').append(' ')
                        .append(orderEntry.getQuantity()).append(' ').append(orderEntry.getUnit().getCode()).append(" x ")
                        .append(getPriceLabelHandler().getLabel(orderEntry.getBasePrice(), orderEntry.getOrder().getCurrency())).append(" = ")
                        .append(getPriceLabelHandler().getLabel(orderEntry.getTotalPrice(), orderEntry.getOrder().getCurrency()));
        return builder.toString();
    }


    @Override
    public String getDescription(final AbstractOrderEntryModel object)
    {
        return null;
    }


    @Override
    public String getIconPath(final AbstractOrderEntryModel object)
    {
        return null;
    }


    public LabelService getLabelService()
    {
        return labelService;
    }


    @Required
    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }


    public LabelHandler<Double, CurrencyModel> getPriceLabelHandler()
    {
        return priceLabelHandler;
    }


    @Required
    public void setPriceLabelHandler(final LabelHandler<Double, CurrencyModel> priceLabelHandler)
    {
        this.priceLabelHandler = priceLabelHandler;
    }
}
