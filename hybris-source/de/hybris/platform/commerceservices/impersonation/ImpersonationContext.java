package de.hybris.platform.commerceservices.impersonation;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.enums.UserTaxGroup;
import java.io.Serializable;
import java.util.Collection;

public class ImpersonationContext implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Collection<CatalogVersionModel> catalogVersions;
    private UserModel user;
    private LanguageModel language;
    private CurrencyModel currency;
    private UserTaxGroup taxGroup;
    private BaseSiteModel site;
    private AbstractOrderModel order;


    public void setCatalogVersions(Collection<CatalogVersionModel> catalogVersions)
    {
        this.catalogVersions = catalogVersions;
    }


    public Collection<CatalogVersionModel> getCatalogVersions()
    {
        return this.catalogVersions;
    }


    public void setUser(UserModel user)
    {
        this.user = user;
    }


    public UserModel getUser()
    {
        return this.user;
    }


    public void setLanguage(LanguageModel language)
    {
        this.language = language;
    }


    public LanguageModel getLanguage()
    {
        return this.language;
    }


    public void setCurrency(CurrencyModel currency)
    {
        this.currency = currency;
    }


    public CurrencyModel getCurrency()
    {
        return this.currency;
    }


    public void setTaxGroup(UserTaxGroup taxGroup)
    {
        this.taxGroup = taxGroup;
    }


    public UserTaxGroup getTaxGroup()
    {
        return this.taxGroup;
    }


    public void setSite(BaseSiteModel site)
    {
        this.site = site;
    }


    public BaseSiteModel getSite()
    {
        return this.site;
    }


    public void setOrder(AbstractOrderModel order)
    {
        this.order = order;
    }


    public AbstractOrderModel getOrder()
    {
        return this.order;
    }
}
