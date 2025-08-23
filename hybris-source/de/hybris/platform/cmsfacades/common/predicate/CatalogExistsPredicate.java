/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.predicate;

import de.hybris.platform.catalog.CatalogVersionService;
import java.util.function.Predicate;
import org.springframework.beans.factory.annotation.Required;

/**
 * Predicate to test if a given catalog exists.
 * <p>
 * Returns <tt>TRUE</tt> if the given catalog exists; <tt>FALSE</tt> otherwise.
 * </p>
 */
public class CatalogExistsPredicate implements Predicate<String>
{
    private CatalogVersionService catalogVersionService;


    @Override
    public boolean test(final String catalogId)
    {
        return getCatalogVersionService().getAllCatalogVersions().stream()
                        .anyMatch(cat -> cat.getCatalog().getId().equals(catalogId));
    }


    protected CatalogVersionService getCatalogVersionService()
    {
        return catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }
}
