package de.hybris.platform.acceleratorservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class SiteMapConfigModel extends ItemModel
{
    public static final String _TYPECODE = "SiteMapConfig";
    public static final String CONFIGID = "configId";
    public static final String SITEMAPLANGUAGECURRENCIES = "siteMapLanguageCurrencies";
    public static final String SITEMAPPAGES = "siteMapPages";
    public static final String SITEMAPTEMPLATE = "siteMapTemplate";
    public static final String CUSTOMURLS = "customUrls";


    public SiteMapConfigModel()
    {
    }


    public SiteMapConfigModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SiteMapConfigModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "configId", type = Accessor.Type.GETTER)
    public String getConfigId()
    {
        return (String)getPersistenceContext().getPropertyValue("configId");
    }


    @Accessor(qualifier = "customUrls", type = Accessor.Type.GETTER)
    public Collection<String> getCustomUrls()
    {
        return (Collection<String>)getPersistenceContext().getPropertyValue("customUrls");
    }


    @Accessor(qualifier = "siteMapLanguageCurrencies", type = Accessor.Type.GETTER)
    public Collection<SiteMapLanguageCurrencyModel> getSiteMapLanguageCurrencies()
    {
        return (Collection<SiteMapLanguageCurrencyModel>)getPersistenceContext().getPropertyValue("siteMapLanguageCurrencies");
    }


    @Accessor(qualifier = "siteMapPages", type = Accessor.Type.GETTER)
    public Collection<SiteMapPageModel> getSiteMapPages()
    {
        return (Collection<SiteMapPageModel>)getPersistenceContext().getPropertyValue("siteMapPages");
    }


    @Accessor(qualifier = "siteMapTemplate", type = Accessor.Type.GETTER)
    public RendererTemplateModel getSiteMapTemplate()
    {
        return (RendererTemplateModel)getPersistenceContext().getPropertyValue("siteMapTemplate");
    }


    @Accessor(qualifier = "configId", type = Accessor.Type.SETTER)
    public void setConfigId(String value)
    {
        getPersistenceContext().setPropertyValue("configId", value);
    }


    @Accessor(qualifier = "customUrls", type = Accessor.Type.SETTER)
    public void setCustomUrls(Collection<String> value)
    {
        getPersistenceContext().setPropertyValue("customUrls", value);
    }


    @Accessor(qualifier = "siteMapLanguageCurrencies", type = Accessor.Type.SETTER)
    public void setSiteMapLanguageCurrencies(Collection<SiteMapLanguageCurrencyModel> value)
    {
        getPersistenceContext().setPropertyValue("siteMapLanguageCurrencies", value);
    }


    @Accessor(qualifier = "siteMapPages", type = Accessor.Type.SETTER)
    public void setSiteMapPages(Collection<SiteMapPageModel> value)
    {
        getPersistenceContext().setPropertyValue("siteMapPages", value);
    }


    @Accessor(qualifier = "siteMapTemplate", type = Accessor.Type.SETTER)
    public void setSiteMapTemplate(RendererTemplateModel value)
    {
        getPersistenceContext().setPropertyValue("siteMapTemplate", value);
    }
}
