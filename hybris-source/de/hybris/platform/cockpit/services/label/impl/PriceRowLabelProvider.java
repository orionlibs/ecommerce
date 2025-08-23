package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.cockpit.helpers.LocaleHelper;
import de.hybris.platform.cockpit.helpers.impl.DefaultLocaleHelper;
import de.hybris.platform.cockpit.services.label.CatalogAwareLabelProvider;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.jalo.Item;
import java.text.NumberFormat;

@Deprecated
public class PriceRowLabelProvider extends CatalogAwareLabelProvider<PriceRow>
{
    private final LocaleHelper localeHelper = (LocaleHelper)new DefaultLocaleHelper();


    protected String getItemLabel(PriceRow priceRow)
    {
        Double price = priceRow.getPrice();
        String currency = priceRow.getCurrency().getIsoCode();
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


    protected String getItemLabel(PriceRow priceRow, String languageIso)
    {
        return getItemLabel(priceRow);
    }


    protected CatalogVersion getCatalogVersion(PriceRow priceRow)
    {
        return CatalogManager.getInstance().getCatalogVersion((Item)priceRow);
    }


    protected String getIconPath(PriceRow item)
    {
        return null;
    }


    protected String getIconPath(PriceRow item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(PriceRow item)
    {
        return "";
    }


    protected String getItemDescription(PriceRow item, String languageIso)
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
