package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.helpers.LocaleHelper;
import de.hybris.platform.cockpit.helpers.impl.DefaultLocaleHelper;
import de.hybris.platform.cockpit.services.label.CatalogAwareModelLabelProvider;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.europe1.model.PriceRowModel;
import java.text.NumberFormat;

public class PriceRowModelLabelProvider extends CatalogAwareModelLabelProvider<PriceRowModel>
{
    private LocaleHelper localeHelper = (LocaleHelper)new DefaultLocaleHelper();


    protected String getItemLabel(PriceRowModel priceRow)
    {
        Double price = priceRow.getPrice();
        String currency = priceRow.getCurrency().getIsocode();
        String unit = priceRow.getUnit().getName();
        StringBuffer buf = new StringBuffer();
        if(price == null)
        {
            buf.append('?');
        }
        else
        {
            buf.append(getFormattedPrice(price));
        }
        if(currency != null)
        {
            buf.append(' ');
            buf.append(currency);
        }
        if(unit != null)
        {
            buf.append(" / ");
            buf.append(unit);
        }
        return buf.toString();
    }


    protected String getItemLabel(PriceRowModel priceRow, String languageIso)
    {
        return getItemLabel(priceRow);
    }


    protected CatalogVersionModel getCatalogVersionModel(PriceRowModel itemModel)
    {
        return itemModel.getCatalogVersion();
    }


    protected String getIconPath(PriceRowModel item)
    {
        return null;
    }


    protected String getIconPath(PriceRowModel item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(PriceRowModel item)
    {
        return "";
    }


    protected String getItemDescription(PriceRowModel item, String languageIso)
    {
        return "";
    }


    private String getCurrentLangCode()
    {
        return UISessionUtils.getCurrentSession().getSystemService().getCurrentLanguage().getIsocode();
    }


    private String getFormattedPrice(Double val)
    {
        return NumberFormat.getInstance(this.localeHelper.getLocale(getCurrentLangCode())).format(val);
    }
}
