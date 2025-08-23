package de.hybris.platform.servicelayer.web;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.config.ConfigIntf;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomMediaHeaderConfigurator
{
    private static final String HEADERS_LOOKOUT_PATTERN = "media.set.header.(.*?)";
    private static final String HEADERS_LOOKOUT_MIME_PATTERN = "media.set.mime.header.%s.(.*?)";
    private static final String HEADERS_LOOKOUT_FOLDER_PATTERN = "media.set.folder.header.%s.(.*?)";
    private static final String DISABLE = "disable";
    private static final Logger LOG = LoggerFactory.getLogger(CustomMediaHeaderConfigurator.class);
    private final Set<String> disabledHeaders = new HashSet<>();
    private final Map<String, Map<String, String>> mimeHeaders = new HashMap<>();
    private final Map<String, Map<String, String>> folderHeaders = new HashMap<>();
    private final HeadersLocalCache headersLocalCache = new HeadersLocalCache(this);
    private Map<String, String> globalHeaders;


    public void modifyResponseWithConfiguredHeaders(HttpServletResponse httpResponse)
    {
        Map<String, String> globalConfiguredHeaders = getGlobalConfiguredHeaders();
        Objects.requireNonNull(httpResponse);
        globalConfiguredHeaders.forEach(httpResponse::setHeader);
    }


    public void modifyResponseWithConfiguredHeaders(HttpServletResponse httpResponse, String mime, String folder)
    {
        try
        {
            Map<String, String> globalAndMimeAndFolderHeaders = this.headersLocalCache.obtainGlobalAndMimeAndFolderHeaders(mime, folder);
            Objects.requireNonNull(httpResponse);
            globalAndMimeAndFolderHeaders.forEach(httpResponse::setHeader);
        }
        catch(ExecutionException e)
        {
            LOG.warn("Failed to set media's headers basing on its mime type and folder", e);
        }
    }


    public void modifyResponseWithConfiguredHeaders(HttpServletResponse httpResponse, String mime)
    {
        try
        {
            Map<String, String> globalAndMimeHeaders = this.headersLocalCache.obtainGlobalAndMimeHeaders(mime);
            Objects.requireNonNull(httpResponse);
            globalAndMimeHeaders.forEach(httpResponse::setHeader);
        }
        catch(ExecutionException e)
        {
            LOG.warn("Failed to set media's headers basing on its mime type", e);
        }
    }


    private Map<String, String> getGlobalAndMimeHeaders(String mime)
    {
        Map<String, String> globalConfiguredHeaders = getGlobalConfiguredHeaders();
        Map<String, String> mimeConfiguredHeaders = getMimeConfiguredHeaders(mime);
        return mergeHeadersWithHigherPriorityToLower(mimeConfiguredHeaders, globalConfiguredHeaders);
    }


    private Map<String, String> getGlobalConfiguredHeaders()
    {
        if(Objects.isNull(this.globalHeaders))
        {
            this.globalHeaders = obtainHeaders();
        }
        return this.globalHeaders;
    }


    private Map<String, String> getGlobalAndMimeAndFolderHeaders(String mime, String folder)
    {
        Map<String, String> globalConfiguredHeaders = getGlobalConfiguredHeaders();
        Map<String, String> folderConfiguredHeaders = getFolderConfiguredHeaders(folder);
        Map<String, String> globalAndFolderConfig = mergeHeadersWithHigherPriorityToLower(folderConfiguredHeaders, globalConfiguredHeaders);
        Map<String, String> mimeConfiguredHeaders = getMimeConfiguredHeaders(mime);
        return mergeHeadersWithHigherPriorityToLower(mimeConfiguredHeaders, globalAndFolderConfig);
    }


    private Map<String, String> mergeHeadersWithHigherPriorityToLower(Map<String, String> higherPriorityHeaders, Map<String, String> headers)
    {
        Map<String, String> mergedHeaders = new HashMap<>(headers);
        higherPriorityHeaders.forEach((key, v) -> mergedHeaders.merge(key, v, ()));
        mergedHeaders.values().removeIf(value -> value.equals("disable"));
        return mergedHeaders;
    }


    private Map<String, String> getFolderConfiguredHeaders(String folder)
    {
        if(StringUtils.isBlank(folder))
        {
            return Collections.emptyMap();
        }
        return this.folderHeaders.computeIfAbsent(folder, h -> getAndValidateParametersMatchingHeaders(String.format("media.set.folder.header.%s.(.*?)", new Object[] {folder})));
    }


    private Map<String, String> getMimeConfiguredHeaders(String mime)
    {
        if(StringUtils.isBlank(mime))
        {
            return Collections.emptyMap();
        }
        return this.mimeHeaders.computeIfAbsent(mime, h -> getAndValidateParametersMatchingHeaders(String.format("media.set.mime.header.%s.(.*?)", new Object[] {mime})));
    }


    private Map<String, String> getAndValidateParametersMatchingHeaders(String pattern)
    {
        Map<String, String> parametersMatching = getTenantConfig().getParametersMatching(pattern, true);
        Map<String, String> headerValue = new HashMap<>();
        for(Map.Entry<String, String> entry : parametersMatching.entrySet())
        {
            if(StringUtils.isNotBlank(entry.getValue()) && StringUtils.isNotBlank(entry.getKey()))
            {
                headerValue.put(((String)entry.getKey()).toLowerCase(Locale.ENGLISH), entry.getValue());
            }
        }
        return Collections.unmodifiableMap(headerValue);
    }


    private Map<String, String> obtainHeaders()
    {
        Map<String, String> configuredHeaders = getConfiguredHeaders();
        Map<String, String> preConfiguredHeaders = getPreConfiguredHeaders();
        preConfiguredHeaders.putAll(configuredHeaders);
        Objects.requireNonNull(preConfiguredHeaders);
        getDisabledHeaders().forEach(preConfiguredHeaders::remove);
        return Collections.unmodifiableMap(preConfiguredHeaders);
    }


    public Map<String, String> getPreConfiguredHeaders()
    {
        return PreConfiguredHeaders.getPreConfiguredHeaders();
    }


    private Map<String, String> getConfiguredHeaders()
    {
        Map<String, String> matchingProperties = getTenantConfig().getParametersMatching("media.set.header.(.*?)", true);
        return adjustToHeaderOrBlacklist(matchingProperties);
    }


    public ConfigIntf getTenantConfig()
    {
        return Registry.getMasterTenant()
                        .getConfig();
    }


    private Map<String, String> adjustToHeaderOrBlacklist(Map<String, String> matchingProperties)
    {
        Map<String, String> headerValueMap = new HashMap<>();
        for(Map.Entry<String, String> entry : matchingProperties.entrySet())
        {
            String header = ((String)entry.getKey()).toLowerCase(Locale.ENGLISH);
            String value = entry.getValue();
            if(headerShouldNotBeDisabled(value))
            {
                if(propertyCanBeTakenIntoAccount(header, value))
                {
                    headerValueMap.put(header, value);
                }
                continue;
            }
            if(StringUtils.isNotEmpty(header))
            {
                this.disabledHeaders.add(header);
            }
        }
        return headerValueMap;
    }


    private boolean headerShouldNotBeDisabled(String value)
    {
        return !StringUtils.equalsAnyIgnoreCase(value, new CharSequence[] {"disable"});
    }


    private boolean propertyCanBeTakenIntoAccount(String property, String value)
    {
        return (!StringUtils.isEmpty(value) && !StringUtils.isEmpty(property));
    }


    public Set<String> getDisabledHeaders()
    {
        return this.disabledHeaders;
    }
}
