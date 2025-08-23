package de.hybris.bootstrap.ddl.dbtypesystem;

public interface Deployment extends DbTypeSystemItem
{
    String getTableName();


    String getFullName();


    int getTypeCode();


    String getExtensionName();


    String getPackageName();


    String getName();


    String getSuperDeployment();


    boolean isAbstract();


    boolean isGeneric();


    boolean isFinal();


    String getPropsTableName();


    boolean isNonItemDeployment();
}
