package de.hybris.platform.commerceservices.setup.data;

import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ImpexMacroParameterData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String contentCatalog;
    private String productCatalog;
    private String siteUid;
    private String storeUid;
    private String configExtensionName;
    private String addonExtensionName;
    private String solrIndexedType;
    private String channel;
    private Map<String, String> additionalParameterMap;
    private List<UiExperienceLevel> supportedUiExperienceLevels;


    public void setContentCatalog(String contentCatalog)
    {
        this.contentCatalog = contentCatalog;
    }


    public String getContentCatalog()
    {
        return this.contentCatalog;
    }


    public void setProductCatalog(String productCatalog)
    {
        this.productCatalog = productCatalog;
    }


    public String getProductCatalog()
    {
        return this.productCatalog;
    }


    public void setSiteUid(String siteUid)
    {
        this.siteUid = siteUid;
    }


    public String getSiteUid()
    {
        return this.siteUid;
    }


    public void setStoreUid(String storeUid)
    {
        this.storeUid = storeUid;
    }


    public String getStoreUid()
    {
        return this.storeUid;
    }


    public void setConfigExtensionName(String configExtensionName)
    {
        this.configExtensionName = configExtensionName;
    }


    public String getConfigExtensionName()
    {
        return this.configExtensionName;
    }


    public void setAddonExtensionName(String addonExtensionName)
    {
        this.addonExtensionName = addonExtensionName;
    }


    public String getAddonExtensionName()
    {
        return this.addonExtensionName;
    }


    public void setSolrIndexedType(String solrIndexedType)
    {
        this.solrIndexedType = solrIndexedType;
    }


    public String getSolrIndexedType()
    {
        return this.solrIndexedType;
    }


    public void setChannel(String channel)
    {
        this.channel = channel;
    }


    public String getChannel()
    {
        return this.channel;
    }


    public void setAdditionalParameterMap(Map<String, String> additionalParameterMap)
    {
        this.additionalParameterMap = additionalParameterMap;
    }


    public Map<String, String> getAdditionalParameterMap()
    {
        return this.additionalParameterMap;
    }


    public void setSupportedUiExperienceLevels(List<UiExperienceLevel> supportedUiExperienceLevels)
    {
        this.supportedUiExperienceLevels = supportedUiExperienceLevels;
    }


    public List<UiExperienceLevel> getSupportedUiExperienceLevels()
    {
        return this.supportedUiExperienceLevels;
    }
}
