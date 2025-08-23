package de.hybris.platform.util;

import de.hybris.platform.jalo.type.AttributeAccess;
import de.hybris.platform.jalo.type.AttributeDescriptor;

public class ConfigAttributeDescriptor extends AttributeDescriptor
{
    public static final String EXTERNAL_QUALIFIER = "externalQualifier";
    public static final String STORE_IN_DATABASE = "storeInDatabase";

    static
    {
        registerAccessFor(ConfigAttributeDescriptor.class, "storeInDatabase", (AttributeAccess)new Object());
    }

    public String getExternalQualifier()
    {
        return (String)getProperty("externalQualifier");
    }


    public void setExternalQualifier(String extQ)
    {
        setProperty("externalQualifier", extQ);
    }


    public boolean storeInDatabase()
    {
        return Boolean.TRUE.equals(getProperty("storeInDatabase"));
    }


    public void setStoreInDatabase(boolean storeInDB)
    {
        setProperty("storeInDatabase", storeInDB ? Boolean.TRUE : Boolean.FALSE);
    }
}
