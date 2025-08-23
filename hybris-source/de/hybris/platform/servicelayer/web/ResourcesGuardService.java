package de.hybris.platform.servicelayer.web;

import com.google.common.base.Splitter;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.UrlPathHelper;

public class ResourcesGuardService
{
    private static final Splitter SPLITTER = Splitter.on('.').limit(2);
    private static final String RESPECT_PARENT_SETTINGS_KEY = "endpoints.guardservice.respect.parents";
    private static final String PARAMS_LOOKOUT_PATTERN = "endpoint.(.*?).disabled";
    private final Map<String, Map<String, Boolean>> extEndpoints = new HashMap<>();
    private final boolean respectParentSettings;


    public ResourcesGuardService()
    {
        this(Registry.getCurrentTenantNoFallback().getConfig().getParametersMatching("endpoint.(.*?).disabled", true),
                        Registry.getCurrentTenantNoFallback().getConfig().getBoolean("endpoints.guardservice.respect.parents", true));
    }


    ResourcesGuardService(Map<String, String> params, boolean respectParentSettings)
    {
        this.respectParentSettings = respectParentSettings;
        prepareEndpointMap(params);
        updateEndpoints();
    }


    private void prepareEndpointMap(Map<String, String> filteredConfig)
    {
        filteredConfig.forEach((key, value) -> insertValues(key, Boolean.parseBoolean(value)));
    }


    private void updateEndpoints()
    {
        if(this.respectParentSettings)
        {
            this.extEndpoints.forEach((extName, configuration) -> configuration.keySet().forEach(()));
        }
    }


    private void insertValues(String endPoint, boolean disabled)
    {
        List<String> splittedEndpoint = SPLITTER.splitToList(endPoint.toLowerCase(Locale.ROOT));
        String extName = splittedEndpoint.get(0);
        if(splittedEndpoint.size() == 2)
        {
            String resourcePath = "/" + ((String)splittedEndpoint.get(1)).replace(".", "/");
            if(this.extEndpoints.containsKey(extName))
            {
                ((Map<String, Boolean>)this.extEndpoints.get(extName)).put(resourcePath, Boolean.valueOf(disabled));
            }
            else
            {
                Map<String, Boolean> input = new HashMap<>();
                input.put(resourcePath, Boolean.valueOf(disabled));
                this.extEndpoints.put(extName, input);
            }
        }
        else if(this.extEndpoints.containsKey(extName))
        {
            ((Map<String, Boolean>)this.extEndpoints.get(extName)).put("/", Boolean.valueOf(disabled));
        }
        else
        {
            Map<String, Boolean> input = new HashMap<>();
            input.put("/", Boolean.valueOf(disabled));
            this.extEndpoints.put(extName, input);
        }
    }


    private static void reconfigureAccordingToParents(Map<String, Boolean> configuration, String currentEndpoint)
    {
        configuration.forEach((path, value) -> {
            if(isSubpathOf(currentEndpoint, path))
            {
                if(value.booleanValue())
                {
                    configuration.put(currentEndpoint, Boolean.TRUE);
                }
                else
                {
                    configuration.put(currentEndpoint, Boolean.FALSE);
                }
            }
        });
    }


    private static boolean isSubpathOf(String child, String parent)
    {
        Path childPath = Paths.get(child, new String[0]);
        Path parentPath = Paths.get(parent, new String[0]);
        return (childPath.compareTo(parentPath) > 0 && childPath.startsWith(parentPath));
    }


    private static String normalizeResourcePath(String resourcePath)
    {
        try
        {
            String path = normalize(resourcePath);
            if(pathIsNotRootAndEndsWithSlash(path))
            {
                return StringUtils.removeEnd(path, "/");
            }
            return path;
        }
        catch(URISyntaxException e)
        {
            throw new SystemException(e.getMessage(), e);
        }
    }


    private static String normalize(String resourcePath) throws URISyntaxException
    {
        String normalizedURL = removeSemicolonContent(resourcePath);
        normalizedURL = replaceMultipleSlashesWithOne(normalizedURL);
        normalizedURL = (new URI(normalizedURL)).normalize().getPath();
        return normalizedURL;
    }


    private static String replaceMultipleSlashesWithOne(String url)
    {
        return url.replaceAll("//*", "/");
    }


    private static String removeSemicolonContent(String uri)
    {
        return (new UrlPathHelper()).removeSemicolonContent(uri);
    }


    private static boolean pathIsNotRootAndEndsWithSlash(String path)
    {
        return (path.length() > 1 && path.endsWith("/"));
    }


    private static String pathToValidUrl(Path path)
    {
        return (path == null) ? "" : path.toString().replace("\\", "/");
    }


    public boolean isResourceEnabled(String extensionName, String resourcePath)
    {
        return !isResourceDisabled(extensionName, resourcePath);
    }


    public boolean isResourceDisabled(String extensionName, String resourcePath)
    {
        Objects.requireNonNull(extensionName);
        Objects.requireNonNull(resourcePath);
        String extNameLowerCase = extensionName.toLowerCase(Locale.ROOT);
        String resourcePathLowerCase = resourcePath.toLowerCase(Locale.ROOT);
        if(!this.extEndpoints.containsKey(extNameLowerCase))
        {
            return false;
        }
        Map<String, Boolean> extResources = this.extEndpoints.get(extNameLowerCase);
        if(MapUtils.isEmpty(extResources))
        {
            return false;
        }
        String path = normalizeResourcePath(resourcePathLowerCase);
        if(extResources.containsKey(path))
        {
            return ((Boolean)extResources.get(path)).booleanValue();
        }
        Path parentPath = Paths.get(path, new String[0]).getParent();
        if(parentPath == null)
        {
            return false;
        }
        return isResourceDisabled(extNameLowerCase, pathToValidUrl(parentPath));
    }
}
