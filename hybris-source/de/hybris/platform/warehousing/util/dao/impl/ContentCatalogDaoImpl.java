package de.hybris.platform.warehousing.util.dao.impl;

import de.hybris.platform.cms2.model.contents.ContentCatalogModel;

public class ContentCatalogDaoImpl extends AbstractWarehousingDao<ContentCatalogModel>
{
    protected String getQuery()
    {
        return "SELECT {pk} FROM {Catalog} WHERE {id}=?" + getCode();
    }
}
