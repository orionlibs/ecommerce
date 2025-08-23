package de.hybris.bootstrap.ddl.tools.persistenceinfo;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Objects;

public class Deployment
{
    private final String tableName;
    private final String propsTableName;
    private final String name;
    private final String packageName;
    private final Collection<Long> typePKs;


    public Deployment(String tableName, String propsTableName, String name, String packageName)
    {
        this(tableName, propsTableName, name, packageName, (Iterable<Long>)ImmutableSet.of());
    }


    public Deployment(String tableName, String propsTableName, String name, String packageName, Iterable<Long> typePKs)
    {
        this.tableName = Objects.<String>requireNonNull(tableName);
        this.propsTableName = Objects.<String>requireNonNull(propsTableName);
        this.name = Objects.<String>requireNonNull(name);
        this.packageName = Objects.<String>requireNonNull(packageName);
        this.typePKs = Lists.newArrayList(typePKs);
    }


    public Deployment with(Iterable<Long> typePKs)
    {
        return new Deployment(this.tableName, this.propsTableName, this.name, this.packageName, Objects.<Iterable<Long>>requireNonNull(typePKs));
    }


    public String getTableName()
    {
        return this.tableName;
    }


    public String getLPTableName()
    {
        return this.tableName + "lp";
    }


    public String getPropsTableName()
    {
        return this.propsTableName;
    }


    public String getName()
    {
        return this.name;
    }


    public String getJndiName()
    {
        return this.packageName + "." + this.packageName;
    }


    public Collection<Long> getTypePKs()
    {
        return this.typePKs;
    }
}
