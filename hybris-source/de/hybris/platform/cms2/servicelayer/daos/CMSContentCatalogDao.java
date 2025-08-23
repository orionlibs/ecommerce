package de.hybris.platform.cms2.servicelayer.daos;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import java.util.Collection;

public interface CMSContentCatalogDao extends Dao
{
    Collection<ContentCatalogModel> findAllContentCatalogs();


    Collection<ContentCatalogModel> findAllContentCatalogsOrderedBy(String paramString);


    boolean hasCMSItems(CatalogVersionModel paramCatalogVersionModel);


    boolean hasCMSRelations(CatalogVersionModel paramCatalogVersionModel);


    ContentCatalogModel findContentCatalogById(String paramString) throws UnknownIdentifierException, AmbiguousIdentifierException;
}
