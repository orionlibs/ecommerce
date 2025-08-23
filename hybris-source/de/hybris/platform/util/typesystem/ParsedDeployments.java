package de.hybris.platform.util.typesystem;

import de.hybris.bootstrap.typesystem.YDBTypeMapping;
import de.hybris.bootstrap.typesystem.YDeployment;
import de.hybris.bootstrap.typesystem.YExtension;
import de.hybris.bootstrap.typesystem.YTypeSystem;
import de.hybris.platform.core.DeploymentImpl;
import de.hybris.platform.core.ItemDeployment;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.apache.commons.collections.FastHashMap;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class ParsedDeployments implements DeploymentImpl
{
    private static final Logger log = Logger.getLogger(ParsedDeployments.class.getName());
    private final Map<String, ItemDeployment> deployments;
    private final Map<String, Map<String, String>> databaseTypeMappings;


    public ParsedDeployments(YTypeSystem system)
    {
        this(system, Collections.EMPTY_SET);
    }


    public ParsedDeployments(YTypeSystem system, Set<YExtension> hideExtensions)
    {
        FastHashMap<String, ItemDeployment> fastHashMap = new FastHashMap();
        fastHashMap.setFast(true);
        for(ItemDeployment depl : createDeployments(system, hideExtensions))
        {
            fastHashMap.put(depl.getName(), depl);
        }
        this.deployments = Collections.unmodifiableMap((Map<? extends String, ? extends ItemDeployment>)fastHashMap);
        CaseInsensitiveMap caseInsensitiveMap = new CaseInsensitiveMap();
        for(Map.Entry<String, YDBTypeMapping> e : (Iterable<Map.Entry<String, YDBTypeMapping>>)system.getDBTypeMappings().entrySet())
        {
            FastHashMap<String, String> fastHashMap1 = new FastHashMap();
            fastHashMap1.setFast(true);
            for(Map.Entry<String, String> e2 : (Iterable<Map.Entry<String, String>>)((YDBTypeMapping)e.getValue()).getTypeMappings().entrySet())
            {
                fastHashMap1.put(e2.getKey(), e2.getValue());
            }
            caseInsensitiveMap.put(e.getKey(), Collections.unmodifiableMap((Map<? extends String, ? extends String>)fastHashMap1));
        }
        this.databaseTypeMappings = Collections.unmodifiableMap((Map<? extends String, ? extends Map<String, String>>)caseInsensitiveMap);
    }


    protected Collection<ItemDeployment> createDeployments(YTypeSystem sys, Set<YExtension> hideExtensions)
    {
        Collection<ItemDeployment> ret = new CopyOnWriteArraySet<>();
        for(YDeployment depl : sys.getDeployments())
        {
            if(hideExtensions == null || !hideExtensions.contains(depl.getNamespace()))
            {
                ret.add(new YItemDeploymentWrapper(depl));
            }
        }
        return ret;
    }


    public Set<String> getBeanIDs()
    {
        return this.deployments.keySet();
    }


    public final String getColumnDefinition(String database, String attributeClassName)
    {
        String ret = getJavaTypeMapping(database).get(attributeClassName);
        if(ret == null)
        {
            if(log.isEnabledFor((Priority)Level.WARN))
            {
                log.warn("cannot find direct column type for class " + attributeClassName + " and database " + database + " - fallback to Serializable mapping");
            }
            ret = getJavaTypeMapping(database).get(Serializable.class.getName());
        }
        if(ret == null)
        {
            throw new IllegalStateException("no column type mapping available for class '" + attributeClassName + "' and database " + database);
        }
        return ret;
    }


    public ItemDeployment getItemDeployment(String beanID)
    {
        return this.deployments.get(beanID);
    }


    public Map<String, String> getJavaTypeMapping(String database)
    {
        Map<String, String> ret = this.databaseTypeMappings.get(database);
        return (ret != null) ? ret : Collections.EMPTY_MAP;
    }
}
