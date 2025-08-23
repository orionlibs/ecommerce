package de.hybris.bootstrap.ddl.dbtypesystem;

import java.util.Collection;

public interface Type extends DbTypeSystemItem
{
    long getPK();


    String getInternalCodeLowerCase();


    Long getSuperTypePk();


    int getItemTypeCode();


    Type getSuperType();


    Deployment getDeployment();


    Collection<Attribute> getAttributes();


    Attribute getAttribute(String paramString);


    Collection<EnumerationValue> getEnumerationValues();


    EnumerationValue getEnumerationValue(String paramString);


    String getItemJndiName();
}
