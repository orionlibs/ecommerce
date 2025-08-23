package de.hybris.bootstrap.ddl.dbtypesystem;

import de.hybris.bootstrap.typesystem.YAttributeDescriptor;

public interface Attribute extends DbTypeSystemItem
{
    long getPk();


    long getEnclosingTypePk();


    Long getPersistenceTypePk();


    String getQualifierLowerCaseInternal();


    Type getEnclosingType();


    boolean isUnique();


    Integer getModifiers();


    String getColumnName();


    String getAttributeHandler();


    boolean isProperty();


    YAttributeDescriptor.PersistenceType calculatePersistenceType();
}
