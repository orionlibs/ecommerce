package de.hybris.platform.persistence.flexiblesearch.typecache;

import de.hybris.platform.core.PK;

public interface CachedTypeData
{
    PK getTypePk();


    boolean isAbstract();


    String getLocalizedTableName();


    String getUnlocalizedTableName();


    String getStandardTableName();


    String getPropertyTableName();


    int getPropertyTypeForName(String paramString);


    String getLocalizedPropertyColumnName(String paramString);


    String getUnlocalizedPropertyColumnName(String paramString);


    String getCorePropertyColumnName(String paramString);
}
