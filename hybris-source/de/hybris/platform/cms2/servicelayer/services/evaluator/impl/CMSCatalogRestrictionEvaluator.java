package de.hybris.platform.cms2.servicelayer.services.evaluator.impl;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.cms2.model.restrictions.CMSCatalogRestrictionModel;
import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.cms2.servicelayer.services.evaluator.CMSRestrictionEvaluator;
import org.apache.log4j.Logger;

public class CMSCatalogRestrictionEvaluator implements CMSRestrictionEvaluator<CMSCatalogRestrictionModel>
{
    private static final Logger LOG = Logger.getLogger(CMSCatalogRestrictionEvaluator.class);


    public boolean evaluate(CMSCatalogRestrictionModel catalogaRestrictionModel, RestrictionData context)
    {
        CatalogModel catalogModel = null;
        if(context == null)
        {
            return true;
        }
        if(context.hasProduct())
        {
            catalogModel = context.getProduct().getCatalogVersion().getCatalog();
        }
        else if(context.hasCategory())
        {
            catalogModel = context.getCategory().getCatalogVersion().getCatalog();
        }
        else if(context.hasCatalog())
        {
            catalogModel = context.getCatalog();
        }
        else
        {
            LOG.warn("Could not evaluate CMSCatalogRestriction. RestrictionData contains neither a catalog, a category or a product. Returning false.");
            return false;
        }
        return catalogaRestrictionModel.getCatalogs().contains(catalogModel);
    }
}
