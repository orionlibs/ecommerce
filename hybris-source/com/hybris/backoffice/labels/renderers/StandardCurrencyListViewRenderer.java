/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.labels.renderers;

import com.hybris.backoffice.labels.LabelHandler;
import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.common.AbstractMoldStrategy;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.csv.CsvAwareListViewRenderer;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.price.TaxModel;
import de.hybris.platform.europe1.model.AbstractDiscountRowModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;

public class StandardCurrencyListViewRenderer extends AbstractWidgetComponentRenderer<Listcell, ListColumn, Object>
                implements CsvAwareListViewRenderer
{
    private static final String CSS_CELL_LABEL = "yw-listview-cell-label";
    private static final Logger LOG = LoggerFactory.getLogger(StandardCurrencyListViewRenderer.class);
    private LabelHandler<Double, CurrencyModel> priceLabelHandler;


    @Override
    public void render(final Listcell parent, final ListColumn configuration, final Object object, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        final StandardCurrencyData data = calculateStandardCurrencyData(object, configuration);
        renderComponents(parent, configuration, object, data.initialised, data.currency, data.totalPrice);
    }


    @Override
    public String getCsvValue(final Object object, final ListColumn listColumn)
    {
        final StandardCurrencyData data = calculateStandardCurrencyData(object, listColumn);
        return createLabel(data.initialised, data.totalPrice, data.currency);
    }


    protected void renderComponents(final Listcell parent, final ListColumn configuration, final Object object,
                    final boolean initialised, final CurrencyModel currency, final Double totalPrice)
    {
        final String labelText = createLabel(initialised, totalPrice, currency);
        final Label label = new Label(labelText);
        UITools.modifySClass(label, CSS_CELL_LABEL, true);
        label.setAttribute(AbstractMoldStrategy.ATTRIBUTE_HYPERLINK_CANDIDATE, Boolean.TRUE);
        parent.appendChild(label);
        fireComponentRendered(label, parent, configuration, object);
        fireComponentRendered(parent, configuration, object);
    }


    private StandardCurrencyData calculateStandardCurrencyData(final Object object, final ListColumn listColumn)
    {
        boolean initialised = false;
        CurrencyModel currency = null;
        Double totalPrice = null;
        if(object instanceof AbstractDiscountRowModel)
        {
            final AbstractDiscountRowModel discount = (AbstractDiscountRowModel)object;
            currency = discount.getCurrency();
            totalPrice = discount.getValue();
            initialised = true;
        }
        else if(object instanceof TaxModel)
        {
            final TaxModel tax = (TaxModel)object;
            currency = tax.getCurrency();
            totalPrice = tax.getValue();
            initialised = true;
        }
        else if(object instanceof PriceRowModel)
        {
            final PriceRowModel price = (PriceRowModel)object;
            currency = price.getCurrency();
            totalPrice = price.getPrice();
            initialised = true;
        }
        else if(object instanceof AbstractOrderModel)
        {
            final AbstractOrderModel orderModel = (AbstractOrderModel)object;
            currency = orderModel.getCurrency();
            totalPrice = orderModel.getTotalPrice();
            initialised = true;
        }
        else if(object instanceof AbstractOrderEntryModel)
        {
            final AbstractOrderEntryModel orderEntryModel = (AbstractOrderEntryModel)object;
            currency = orderEntryModel.getOrder().getCurrency();
            totalPrice = orderEntryModel.getProperty(listColumn.getQualifier());
            initialised = true;
        }
        else
        {
            LOG.warn("Passed object: [{}] is not of supported type", object);
        }
        return new StandardCurrencyData(initialised, currency, totalPrice);
    }


    private String createLabel(final boolean initialised, final Double totalPrice, final CurrencyModel currency)
    {
        return initialised ? getPriceLabelHandler().getLabel(totalPrice, currency) : StringUtils.EMPTY;
    }


    /**
     * Utility private class, just to contain values calculated in
     * {@link #calculateStandardCurrencyData(Object, ListColumn)} to pass to other methods in this class (reason for that
     * was to not break backward compatibility of protected methods).
     */
    private static class StandardCurrencyData
    {
        private final boolean initialised;
        private final CurrencyModel currency;
        private final Double totalPrice;


        StandardCurrencyData(final boolean initialised, final CurrencyModel currency, final Double totalPrice)
        {
            this.initialised = initialised;
            this.currency = currency;
            this.totalPrice = totalPrice;
        }
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
