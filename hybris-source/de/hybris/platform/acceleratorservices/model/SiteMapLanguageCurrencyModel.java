package de.hybris.platform.acceleratorservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SiteMapLanguageCurrencyModel extends ItemModel
{
    public static final String _TYPECODE = "SiteMapLanguageCurrency";
    public static final String LANGUAGE = "language";
    public static final String CURRENCY = "currency";


    public SiteMapLanguageCurrencyModel()
    {
    }


    public SiteMapLanguageCurrencyModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SiteMapLanguageCurrencyModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
    public CurrencyModel getCurrency()
    {
        return (CurrencyModel)getPersistenceContext().getPropertyValue("currency");
    }


    @Accessor(qualifier = "language", type = Accessor.Type.GETTER)
    public LanguageModel getLanguage()
    {
        return (LanguageModel)getPersistenceContext().getPropertyValue("language");
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
    public void setCurrency(CurrencyModel value)
    {
        getPersistenceContext().setPropertyValue("currency", value);
    }


    @Accessor(qualifier = "language", type = Accessor.Type.SETTER)
    public void setLanguage(LanguageModel value)
    {
        getPersistenceContext().setPropertyValue("language", value);
    }
}
