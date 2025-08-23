/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.labels.labelproviders;

import com.hybris.backoffice.labels.LabelHandler;
import com.hybris.cockpitng.labels.LabelProvider;
import com.hybris.cockpitng.labels.LabelService;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import org.springframework.beans.factory.annotation.Required;

public class AbstractOrderLabelProvider implements LabelProvider<AbstractOrderModel>
{
    public static final String SEPARATOR = " - ";
    private LabelService labelService;
    private LabelHandler<Double, CurrencyModel> priceLabelHandler;


    @Override
    public String getLabel(final AbstractOrderModel order)
    {
        final StringBuilder builder = new StringBuilder(order.getCode());
        builder.append(SEPARATOR);
        builder.append(getLabelService().getObjectLabel(order.getUser()));
        builder.append(SEPARATOR);
        builder.append(getLabelService().getObjectLabel(order.getDate()));
        builder.append(SEPARATOR);
        builder.append(getPriceLabelHandler().getLabel(order.getTotalPrice(), order.getCurrency()));
        builder.append(SEPARATOR);
        builder.append(getLabelService().getObjectLabel(order.getStatus()));
        return builder.toString();
    }


    @Override
    public String getDescription(final AbstractOrderModel object)
    {
        return null;
    }


    @Override
    public String getIconPath(final AbstractOrderModel object)
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
