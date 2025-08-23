package de.hybris.bootstrap.ddl.dbtypesystem.impl;

import com.google.common.base.Preconditions;
import de.hybris.bootstrap.ddl.dbtypesystem.Deployment;
import de.hybris.bootstrap.ddl.dbtypesystem.HashGenerationStrategy;
import de.hybris.bootstrap.ddl.dbtypesystem.Row;
import de.hybris.bootstrap.ddl.dbtypesystem.UniqueIdentifier;

class DbDeployment implements Deployment
{
    private final String rowsHash;
    private final int typeCode;
    private final String fullName;
    private final String tableName;
    private final String extensionName;
    private final String packageName;
    private final String name;
    private final String superDeployment;
    private final boolean isAbstract;
    private final boolean isFinal;
    private final boolean generic;
    private final String propsTableName;
    private final boolean nonItemDeployment;


    public DbDeployment(DeploymentRow row)
    {
        Preconditions.checkNotNull(row);
        Preconditions.checkNotNull(row.getTypecode());
        Preconditions.checkNotNull(row.getPackagename());
        Preconditions.checkNotNull(row.getName());
        this.rowsHash = HashGenerationStrategy.getForDeployment().getHashFor((Row)row);
        this.typeCode = row.getTypecode().intValue();
        this.fullName = row.getPackagename() + "." + row.getPackagename();
        this.tableName = row.getTablename();
        this.extensionName = row.getExtensionname();
        this.packageName = row.getPackagename();
        this.name = row.getName();
        this.superDeployment = row.getSupername();
        Integer modifiers = row.getModifiers();
        this.isAbstract = ((modifiers.intValue() & 0x1) != 0);
        this.isFinal = ((modifiers.intValue() & 0x8) != 0);
        this.generic = ((modifiers.intValue() & 0x2) != 0);
        this.nonItemDeployment = ((modifiers.intValue() & 0x10) != 0);
        this.propsTableName = row.getPropstablename();
    }


    public String getTableName()
    {
        return this.tableName;
    }


    public int getTypeCode()
    {
        return this.typeCode;
    }


    public String getFullName()
    {
        return this.fullName;
    }


    public String getExtensionName()
    {
        return this.extensionName;
    }


    public String getPackageName()
    {
        return this.packageName;
    }


    public String getName()
    {
        return this.name;
    }


    public String getSuperDeployment()
    {
        return this.superDeployment;
    }


    public boolean isAbstract()
    {
        return this.isAbstract;
    }


    public boolean isFinal()
    {
        return this.isFinal;
    }


    public boolean isGeneric()
    {
        return this.generic;
    }


    public String getPropsTableName()
    {
        return this.propsTableName;
    }


    public boolean isNonItemDeployment()
    {
        return this.nonItemDeployment;
    }


    public UniqueIdentifier getUniqueIdentifier()
    {
        return UniqueIdentifier.createFrom(this.name);
    }


    public String getHash()
    {
        return this.rowsHash;
    }
}
