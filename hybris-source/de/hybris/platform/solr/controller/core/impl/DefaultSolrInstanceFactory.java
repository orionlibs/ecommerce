package de.hybris.platform.solr.controller.core.impl;

import de.hybris.platform.solr.controller.SolrControllerException;
import de.hybris.platform.solr.controller.core.SolrInstance;
import de.hybris.platform.solr.controller.core.SolrInstanceFactory;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultSolrInstanceFactory implements SolrInstanceFactory
{
    protected static final String SOLR_INSTANCES_PATH = "/solr/instances";


    public SolrInstance getInstanceForName(Map<String, String> configuration, String name) throws SolrControllerException
    {
        Map<String, SolrInstance> instances = createInstances(configuration);
        SolrInstance instance = instances.get(name);
        if(instance == null)
        {
            throw new SolrControllerException(MessageFormat.format("Solr instance not found for name ''{0}''", new Object[] {name}));
        }
        return instance;
    }


    public Collection<SolrInstance> getInstances(Map<String, String> configuration) throws SolrControllerException
    {
        Map<String, SolrInstance> instances = createInstances(configuration);
        return instances.values();
    }


    protected Map<String, SolrInstance> createInstances(Map<String, String> configuration)
    {
        Map<String, SolrInstance> instances = new HashMap<>();
        Map<String, String> instancesConfiguration = buildInstancesConfiguration(configuration);
        instancesConfiguration.forEach((key, value) -> {
            int instanceNameEndIndex = key.indexOf('.');
            String instanceName = key.substring(0, instanceNameEndIndex);
            String instanceConfigKey = key.substring(instanceNameEndIndex + 1);
            SolrInstance instance = (SolrInstance)instances.get(instanceName);
            if(instance == null)
            {
                instance = initializeInstance(instanceName, configuration);
                instances.put(instanceName, instance);
            }
            instance.getConfiguration().put(instanceConfigKey, value);
        });
        return instances;
    }


    protected Map<String, String> buildInstancesConfiguration(Map<String, String> configuration)
    {
        Map<String, String> instancesConfiguration = extractConfiguration(configuration, "solrserver.instances\\.(.*)");
        String instanceName = configuration.get("instance.name");
        if(instanceName != null)
        {
            Map<String, String> instanceConfiguration = extractConfiguration(configuration, "instance\\.(.*)");
            instanceConfiguration.forEach((key, value) -> instancesConfiguration.put(instanceName + "." + key, value));
        }
        return instancesConfiguration;
    }


    protected static Map<String, String> extractConfiguration(Map<String, String> configuration, String regex)
    {
        Map<String, String> newConfiguration = new HashMap<>();
        Pattern pattern = Pattern.compile(regex, 2);
        for(Map.Entry<String, String> entry : configuration.entrySet())
        {
            Matcher matcher = pattern.matcher(entry.getKey());
            if(matcher.matches())
            {
                newConfiguration.put(matcher.group(1), entry.getValue());
            }
        }
        return newConfiguration;
    }


    protected SolrInstance initializeInstance(String name, Map<String, String> configuration)
    {
        DefaultSolrInstance defaultSolrInstance = new DefaultSolrInstance(name);
        Map<String, String> instanceConfiguration = defaultSolrInstance.getConfiguration();
        Path configPath = Paths.get(configuration.get("HYBRIS_CONFIG_PATH"), new String[] {"/solr/instances", name});
        Path dataPath = Paths.get(configuration.get("HYBRIS_DATA_PATH"), new String[] {"/solr/instances", name});
        Path logPath = Paths.get(configuration.get("HYBRIS_LOG_PATH"), new String[] {"/solr/instances", name});
        Path solrSslStorePath = configPath.resolve("solr.p12");
        instanceConfiguration.put("config.dir", configPath.toString());
        instanceConfiguration.put("data.dir", dataPath.toString());
        instanceConfiguration.put("log.dir", logPath.toString());
        instanceConfiguration.put("ssl.keyStore", solrSslStorePath.toString());
        instanceConfiguration.put("ssl.trustStore", solrSslStorePath.toString());
        return (SolrInstance)defaultSolrInstance;
    }
}
