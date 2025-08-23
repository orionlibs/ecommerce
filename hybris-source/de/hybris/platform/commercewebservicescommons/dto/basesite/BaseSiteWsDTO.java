package de.hybris.platform.commercewebservicescommons.dto.basesite;

import de.hybris.platform.commercewebservicescommons.dto.basestore.BaseStoreWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.storesession.LanguageWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@ApiModel(value = "BaseSite", description = "Representation of a Base Site")
public class BaseSiteWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "uid", value = "Unique identifier of Basesite")
    private String uid;
    @ApiModelProperty(name = "name", value = "Name of Basesite")
    private String name;
    @ApiModelProperty(name = "stores", value = "List of Basestores")
    private List<BaseStoreWsDTO> stores;
    @ApiModelProperty(name = "theme", value = "Theme of Basesite")
    private String theme;
    @ApiModelProperty(name = "defaultLanguage", value = "Default language for Basesite")
    private LanguageWsDTO defaultLanguage;
    @ApiModelProperty(name = "locale", value = "Locale data for Basesite")
    private String locale;
    @ApiModelProperty(name = "channel", value = "Channel")
    private String channel;
    @ApiModelProperty(name = "urlEncodingAttributes", value = "List of url encoding attributes")
    private Collection<String> urlEncodingAttributes;
    @ApiModelProperty(name = "urlPatterns", value = "List of url patterns")
    private Collection<String> urlPatterns;
    @ApiModelProperty(name = "defaultPreviewCatalogId", value = "Default preview catalog id")
    private String defaultPreviewCatalogId;
    @ApiModelProperty(name = "defaultPreviewCategoryCode", value = "Default preview category code")
    private String defaultPreviewCategoryCode;
    @ApiModelProperty(name = "defaultPreviewProductCode", value = "Default preview product code")
    private String defaultPreviewProductCode;
    @ApiModelProperty(name = "registrationEnabled", value = "Indicates if the website supports registration", example = "true")
    private Boolean registrationEnabled;
    @ApiModelProperty(name = "requiresAuthentication", value = "Indicates if the BaseSite requires authentication prior to use it", example = "true")
    private Boolean requiresAuthentication;


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


    public void setStores(List<BaseStoreWsDTO> stores)
    {
        this.stores = stores;
    }


    public List<BaseStoreWsDTO> getStores()
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


    public void setDefaultLanguage(LanguageWsDTO defaultLanguage)
    {
        this.defaultLanguage = defaultLanguage;
    }


    public LanguageWsDTO getDefaultLanguage()
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


    public void setUrlEncodingAttributes(Collection<String> urlEncodingAttributes)
    {
        this.urlEncodingAttributes = urlEncodingAttributes;
    }


    public Collection<String> getUrlEncodingAttributes()
    {
        return this.urlEncodingAttributes;
    }


    public void setUrlPatterns(Collection<String> urlPatterns)
    {
        this.urlPatterns = urlPatterns;
    }


    public Collection<String> getUrlPatterns()
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


    public void setRegistrationEnabled(Boolean registrationEnabled)
    {
        this.registrationEnabled = registrationEnabled;
    }


    public Boolean getRegistrationEnabled()
    {
        return this.registrationEnabled;
    }


    public void setRequiresAuthentication(Boolean requiresAuthentication)
    {
        this.requiresAuthentication = requiresAuthentication;
    }


    public Boolean getRequiresAuthentication()
    {
        return this.requiresAuthentication;
    }
}
