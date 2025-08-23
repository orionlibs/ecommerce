package de.hybris.bootstrap.ddl.tools.persistenceinfo;

import com.google.common.collect.ImmutableSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Deployments implements Iterable<Deployment>
{
    private final Map<String, Deployment> nameToDeployment = new HashMap<>();


    public Iterator<Deployment> iterator()
    {
        return this.nameToDeployment.values().iterator();
    }


    public void add(Deployment deployment)
    {
        Objects.requireNonNull(deployment);
        this.nameToDeployment.put(deployment.getName(), deployment);
    }


    public Deployment findByName(String name)
    {
        return this.nameToDeployment.get(Objects.requireNonNull(name));
    }


    public Set<String> getAllTables()
    {
        ImmutableSet.Builder<String> result = ImmutableSet.builder();
        for(Deployment deployment : this.nameToDeployment.values())
        {
            result.add(deployment.getTableName());
            result.add(deployment.getPropsTableName());
        }
        return (Set<String>)result.build();
    }


    public Set<String> getAllTablesWithLPTables()
    {
        ImmutableSet.Builder<String> result = ImmutableSet.builder();
        for(Deployment deployment : this.nameToDeployment.values())
        {
            result.add(deployment.getTableName());
            result.add(deployment.getPropsTableName());
            result.add(deployment.getLPTableName());
        }
        return (Set<String>)result.build();
    }
}
