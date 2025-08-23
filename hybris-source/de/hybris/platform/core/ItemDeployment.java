package de.hybris.platform.core;

import java.io.Serializable;
import java.util.Collection;

public interface ItemDeployment extends Serializable
{
    boolean isFinal();


    String getName();


    boolean isAbstract();


    boolean isGeneric();


    boolean isNonItemDeployment();


    int getTypeCode();


    String getSuperDeploymentName();


    String getDatabaseTableName();


    String getDumpPropertyTableName();


    String getAuditTableName();


    Class getHomeInterface();


    Class getRemoteInterface();


    Class getImplClass();


    Collection getAttributes();


    Attribute getAttribute(String paramString);


    Collection getFinderMethods();


    FinderMethod getFinderMethod(String paramString1, String paramString2);


    Collection getIndexes();


    Index getIndex(String paramString);


    Class getConcreteEJBImplementationClass();


    String getColumnName(String paramString1, String paramString2);
}
