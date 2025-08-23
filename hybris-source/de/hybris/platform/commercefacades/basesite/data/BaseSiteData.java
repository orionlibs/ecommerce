package de.hybris.platform.commercefacades.basesite.data;

import de.hybris.platform.commercefacades.basestore.data.BaseStoreData;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import java.io.Serializable;
import java.util.List;

public class BaseSiteData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String uid;
    private String name;
    private List<BaseStoreData> stores;
    private String theme;
    private LanguageData defaultLanguage;
    private String locale;
    private String channel;
    private boolean requiresAuthentication;
    private boolean enableRegistration;
    private List<String> urlPatterns;
    private String defaultPreviewCatalogId;
    private String defaultPreviewCategoryCode;
    private String defaultPreviewProductCode;
    private List<String> urlEncodingAttributes;


    public void setUid(String uid)
    {
        this.uid = uid;
    }


    public String getUid()
    {
        return this.uid;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setStores(List<BaseStoreData> stores)
    {
        this.stores = stores;
    }


    public List<BaseStoreData> getStores()
    {
        return this.stores;
    }


    public void setTheme(String theme)
    {
        this.theme = theme;
    }


    public String getTheme()
    {
        return this.theme;
    }


    public void setDefaultLanguage(LanguageData defaultLanguage)
    {
        this.defaultLanguage = defaultLanguage;
    }


    public LanguageData getDefaultLanguage()
    {
        return this.defaultLanguage;
    }


    public void setLocale(String locale)
    {
        this.locale = locale;
    }


    public String getLocale()
    {
        return this.locale;
    }


    public void setChannel(String channel)
    {
        this.channel = channel;
    }


    public String getChannel()
    {
        return this.channel;
    }


    public void setRequiresAuthentication(boolean requiresAuthentication)
    {
        this.requiresAuthentication = requiresAuthentication;
    }


    public boolean isRequiresAuthentication()
    {
        return this.requiresAuthentication;
    }


    public void setEnableRegistration(boolean enableRegistration)
    {
        this.enableRegistration = enableRegistration;
    }


    public boolean isEnableRegistration()
    {
        return this.enableRegistration;
    }


    public void setUrlPatterns(List<String> urlPatterns)
    {
        this.urlPatterns = urlPatterns;
    }


    public List<String> getUrlPatterns()
    {
        return this.urlPatterns;
    }


    public void setDefaultPreviewCatalogId(String defaultPreviewCatalogId)
    {
        this.defaultPreviewCatalogId = defaultPreviewCatalogId;
    }


    public String getDefaultPreviewCatalogId()
    {
        return this.defaultPreviewCatalogId;
    }


    public void setDefaultPreviewCategoryCode(String defaultPreviewCategoryCode)
    {
        this.defaultPreviewCategoryCode = defaultPreviewCategoryCode;
    }


    public String getDefaultPreviewCategoryCode()
    {
        return this.defaultPreviewCategoryCode;
    }


    public void setDefaultPreviewProductCode(String defaultPreviewProductCode)
    {
        this.defaultPreviewProductCode = defaultPreviewProductCode;
    }


    public String getDefaultPreviewProductCode()
    {
        return this.defaultPreviewProductCode;
    }


    public void setUrlEncodingAttributes(List<String> urlEncodingAttributes)
    {
        this.urlEncodingAttributes = urlEncodingAttributes;
    }


    public List<String> getUrlEncodingAttributes()
    {
        return this.urlEncodingAttributes;
    }
}
