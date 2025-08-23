package de.hybris.platform.webservicescommons.swagger.strategies.impl;

import de.hybris.platform.webservicescommons.swagger.strategies.ConfigApiVendorExtensionStrategy;
import java.util.List;
import springfox.documentation.service.ObjectVendorExtension;
import springfox.documentation.service.StringVendorExtension;
import springfox.documentation.service.VendorExtension;

public class DefaultApiVendorExtensionStrategy extends ConfigApiVendorExtensionStrategy
{
    protected static final String EXT_API_TYPE = "x-sap-api-type";
    protected static final String EXT_SHORT_TEXT = "x-sap-shortText";
    protected static final String EXT_STATE_INFO = "x-sap-stateInfo";
    protected static final String EXT_STATE = "state";
    protected static final String CONFIG_API_TYPE = "sap.apiType";
    protected static final String CONFIG_SHORT_TEXT = "sap.shortText";
    protected static final String CONFIG_STATE = "sap.state";


    public List<VendorExtension> getVendorExtensions(String configPrefix)
    {
        StringVendorExtension apiType = new StringVendorExtension("x-sap-api-type", getApiType(configPrefix));
        StringVendorExtension shortText = new StringVendorExtension("x-sap-shortText", getShortText(configPrefix));
        ObjectVendorExtension stateInfo = new ObjectVendorExtension("x-sap-stateInfo");
        StringVendorExtension state = new StringVendorExtension("state", getState(configPrefix));
        stateInfo.addProperty((VendorExtension)state);
        return (List)List.of(apiType, shortText, stateInfo);
    }


    protected String getApiType(String configPrefix)
    {
        return getConfigValue(configPrefix, new String[] {"sap.apiType"});
    }


    protected String getShortText(String configPrefix)
    {
        return getConfigValue(configPrefix, new String[] {"sap.shortText"});
    }


    protected String getState(String configPrefix)
    {
        return getConfigValue(configPrefix, new String[] {"sap.state"});
    }
}
