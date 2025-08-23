package de.hybris.platform.cms2.cloning.service;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.cloning.service.impl.CMSModelCloningContext;

public interface CMSModelCloningContextFactory
{
    CMSModelCloningContext createCloningContextWithCatalogVersionPredicates(CatalogVersionModel paramCatalogVersionModel);
}
