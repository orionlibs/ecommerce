package de.hybris.bootstrap.ddl.tools.persistenceinfo;

import com.google.common.collect.ImmutableSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class TypeSystemRelatedDeployments implements Iterable<TypeSystemRelatedDeployment>
{
    private final Map<String, TypeSystemRelatedDeployment> nameToDeployment = new HashMap<>();


    public Iterator<TypeSystemRelatedDeployment> iterator()
    {
        return this.nameToDeployment.values().iterator();
    }


    public void add(TypeSystemRelatedDeployment deployment)
    {
        Objects.requireNonNull(deployment);
        this.nameToDeployment.put(deployment.getName(), deployment);
    }


    public TypeSystemRelatedDeployment findByName(String name)
    {
        return this.nameToDeployment.get(Objects.requireNonNull(name));
    }


    public Set<String> getAllTables()
    {
        ImmutableSet.Builder<String> result = ImmutableSet.builder();
        for(TypeSystemRelatedDeployment deployment : this.nameToDeployment.values())
        {
            result.add(deployment.getTableName());
            result.add(deployment.getPropsTableName());
        }
        return (Set<String>)result.build();
    }


    public Set<String> getAllTablesWithLPTables()
    {
        ImmutableSet.Builder<String> result = ImmutableSet.builder();
        for(TypeSystemRelatedDeployment deployment : this.nameToDeployment.values())
        {
            result.add(deployment.getTableName());
            result.add(deployment.getPropsTableName());
            result.add(deployment.getLPTableName());
        }
        return (Set<String>)result.build();
    }
}
