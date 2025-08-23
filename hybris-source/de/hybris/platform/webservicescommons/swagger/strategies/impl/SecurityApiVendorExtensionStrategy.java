package de.hybris.platform.webservicescommons.swagger.strategies.impl;

import de.hybris.platform.webservicescommons.swagger.strategies.ConfigApiVendorExtensionStrategy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import springfox.documentation.service.ListVendorExtension;
import springfox.documentation.service.VendorExtension;

public class SecurityApiVendorExtensionStrategy extends ConfigApiVendorExtensionStrategy
{
    protected static final String CONFIG_SECURITY_NAMES = "sap.securityNames";
    protected static final String CONFIG_SECURITY = "sap.security";
    protected static final String CONFIG_SCOPES = "scopes";
    protected static final String SECURITY = "security";


    public List<VendorExtension> getVendorExtensions(String configPrefix)
    {
        ListVendorExtension<Map<String, List<String>>> listVendorExtension = new ListVendorExtension("security", getSecurity(configPrefix));
        return (List)List.of(listVendorExtension);
    }


    protected String[] getSecurityNames(String configPrefix)
    {
        return getConfigArray(configPrefix, new String[] {"sap.securityNames"});
    }


    protected List<Map<String, List<String>>> getSecurity(String configPrefix)
    {
        return (List<Map<String, List<String>>>)Arrays.<String>stream(getSecurityNames(configPrefix)).map(securityName -> {
            Map<String, List<String>> securityMap = new HashMap<>();
            securityMap.put(securityName, getSecurityScopes(configPrefix, securityName));
            return securityMap;
        }).collect(Collectors.toList());
    }


    protected List<String> getSecurityScopes(String configPrefix, String securityName)
    {
        return Arrays.asList(getConfigArray(configPrefix, new String[] {"sap.security", securityName, "scopes"}));
    }
}
