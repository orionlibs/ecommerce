package de.hybris.bootstrap.ddl.tools.persistenceinfo;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Objects;

public class TypeSystemRelatedDeployment
{
    private final String tableName;
    private final String propsTableName;
    private final String name;
    private final String packageName;
    private final boolean isColumnBased;
    private final Collection<Long> typePKs;


    public TypeSystemRelatedDeployment(String tableName, String propsTableName, String name, String packageName, boolean isColumnBased)
    {
        this(tableName, propsTableName, name, packageName, isColumnBased, (Iterable<Long>)ImmutableSet.of());
    }


    public TypeSystemRelatedDeployment(String tableName, String propsTableName, String name, String packageName, boolean isColumnBased, Iterable<Long> typePKs)
    {
        this.tableName = Objects.<String>requireNonNull(tableName);
        this.propsTableName = Objects.<String>requireNonNull(propsTableName);
        this.name = Objects.<String>requireNonNull(name);
        this.packageName = Objects.<String>requireNonNull(packageName);
        this.isColumnBased = isColumnBased;
        this.typePKs = Lists.newArrayList(typePKs);
    }


    public TypeSystemRelatedDeployment with(Iterable<Long> typePKs)
    {
        return new TypeSystemRelatedDeployment(this.tableName, this.propsTableName, this.name, this.packageName, this.isColumnBased,
                        Objects.<Iterable<Long>>requireNonNull(typePKs));
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


    public boolean isColumnBased()
    {
        return this.isColumnBased;
    }


    public Collection<Long> getTypePKs()
    {
        return this.typePKs;
    }
}
