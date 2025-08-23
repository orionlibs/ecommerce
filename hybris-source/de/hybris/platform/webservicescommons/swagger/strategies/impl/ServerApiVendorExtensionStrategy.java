package de.hybris.platform.webservicescommons.swagger.strategies.impl;

import de.hybris.platform.webservicescommons.swagger.strategies.ConfigApiVendorExtensionStrategy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import springfox.documentation.service.ListVendorExtension;
import springfox.documentation.service.VendorExtension;

public class ServerApiVendorExtensionStrategy extends ConfigApiVendorExtensionStrategy
{
    protected static final String EXT_SERVERS = "x-servers";
    protected static final String CONFIG_SERVER = "sap.server";
    protected static final String CONFIG_SERVER_URL = "url";
    protected static final String CONFIG_SERVER_DESCRIPTION = "description";
    protected static final String CONFIG_TEMPLATES = "templates";
    protected static final String CONFIG_DEFAULT = "default";
    protected static final String CONFIG_ENUM = "enum";
    protected static final String CONFIG_SERVERS = "sap.servers";
    protected static final Pattern SERVER_URL_PLACEHOLDER_PATTERN = Pattern.compile("\\{[^}]+}");


    public List<VendorExtension> getVendorExtensions(String configPrefix)
    {
        ListVendorExtension<Map<String, Object>> servers = new ListVendorExtension("x-servers", getServers(configPrefix));
        return (List)List.of(servers);
    }


    protected String getServerConfigValue(String configPrefix, String serverName, String relativeConfigKey)
    {
        return getConfigValue(configPrefix, new String[] {"sap.server", serverName, relativeConfigKey});
    }


    protected String getServerTemplatesConfigValue(String configPrefix, String serverName, String urlPlaceholder, String relativeConfigKey)
    {
        return getConfigValue(configPrefix, new String[] {"sap.server", serverName, "templates", urlPlaceholder, relativeConfigKey});
    }


    protected String[] getServersNames(String configPrefix)
    {
        return getConfigArray(configPrefix, new String[] {"sap.servers"});
    }


    protected List<Map<String, Object>> getServers(String configPrefix)
    {
        return (List<Map<String, Object>>)Arrays.<String>stream(getServersNames(configPrefix)).map(serverName -> {
            Map<String, Object> serverMap = new HashMap<>();
            String serverUrl = getServerConfigValue(configPrefix, serverName, "url");
            serverMap.put("url", serverUrl);
            serverMap.put("description", getServerConfigValue(configPrefix, serverName, "description"));
            serverMap.put("templates", getTemplates(configPrefix, serverName, serverUrl));
            return serverMap;
        }).collect(Collectors.toList());
    }


    protected Map<String, Object> getTemplates(String configPrefix, String serverName, String serverUrl)
    {
        Map<String, Object> templates = new HashMap<>();
        Matcher matcher = SERVER_URL_PLACEHOLDER_PATTERN.matcher(serverUrl);
        while(matcher.find())
        {
            Map<String, Object> templateEntries = new HashMap<>();
            String urlPlaceholder = matcher.group();
            String urlPlaceholderName = urlPlaceholder.substring(1, urlPlaceholder.length() - 1);
            addTemplateDescriptionToMap(templateEntries, configPrefix, serverName, urlPlaceholderName);
            addTemplateDefaultToMap(templateEntries, configPrefix, serverName, urlPlaceholderName);
            addTemplateEnumToMap(templateEntries, configPrefix, serverName, urlPlaceholderName);
            templates.put(urlPlaceholderName, templateEntries);
        }
        return templates;
    }


    protected void addTemplateDescriptionToMap(Map<String, Object> templateEntries, String configPrefix, String serverName, String urlPlaceholderName)
    {
        templateEntries.put("description",
                        getServerTemplatesConfigValue(configPrefix, serverName, urlPlaceholderName, "description"));
    }


    protected void addTemplateDefaultToMap(Map<String, Object> templateEntries, String configPrefix, String serverName, String urlPlaceholderName)
    {
        String templateDefault = getServerTemplatesConfigValue(configPrefix, serverName, urlPlaceholderName, "default");
        if(StringUtils.isNotBlank(templateDefault))
        {
            templateEntries.put("default", templateDefault);
        }
    }


    protected void addTemplateEnumToMap(Map<String, Object> templateEntries, String configPrefix, String serverName, String urlPlaceholderName)
    {
        String templateEnum = getServerTemplatesConfigValue(configPrefix, serverName, urlPlaceholderName, "enum");
        if(StringUtils.isNotBlank(templateEnum))
        {
            templateEntries.put("enum", List.of(templateEnum.split(",")));
        }
    }
}
