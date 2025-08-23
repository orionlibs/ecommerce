package de.hybris.platform.spring;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public abstract class AbstractWebModuleResolver
{
    private static final Logger LOG = Logger.getLogger(AbstractWebModuleResolver.class.getName());
    protected static final String EMPTY_STRING = "";
    protected static final String ROOT_STRING = "/";
    protected static final String CONFIG_KEY_EXTENSIONWEBMODS = "extension.webmods";
    public static final String WEBROOT_PROPERTY = "webroot";
    protected static final String DEFAULT_TENANT = "master";
    protected final Map globalProps;


    protected AbstractWebModuleResolver(Map properties)
    {
        Preconditions.checkNotNull(properties);
        this.globalProps = properties;
    }


    protected BiMap<String, String> prepareReversedMap()
    {
        List<String> webExtensions = getInstalledWebModules();
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Found web modules :" + webExtensions);
        }
        Map<String, String> webModulesProps = Maps.filterEntries(this.globalProps, (Predicate)new WebModulePredicate(this, webExtensions));
        Map<String, String> mergedWebModulesProps = Maps.filterEntries(webModulesProps, (Predicate)new MergeRedundantPredicate(this, this.globalProps
                        .keySet()));
        return (BiMap<String, String>)ImmutableBiMap.copyOf(mergedWebModulesProps);
    }


    protected List<String> getInstalledWebModules()
    {
        String nameStr = (String)this.globalProps.get("extension.webmods");
        if(nameStr != null)
        {
            ImmutableList.Builder<String> result = ImmutableList.builder();
            for(String entries : Splitter.on(";").omitEmptyStrings().trimResults().split(nameStr))
            {
                result.add(Iterables.getFirst(Splitter.on(",").split(entries), null));
            }
            return (List<String>)result.build();
        }
        throw new IllegalStateException();
    }
}
