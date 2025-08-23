package de.hybris.platform.catalog.jalo;

import de.hybris.platform.jalo.SessionContext;

public interface CatalogItem<T extends de.hybris.platform.jalo.Item>
{
    String getIDAttributeQualifier();


    String getCatalogVersionAttributeQualifier();


    String getCatalogItemID();


    CatalogVersion getCatalogVersion(SessionContext paramSessionContext);


    T getCounterpartItem(CatalogVersion paramCatalogVersion);
}
