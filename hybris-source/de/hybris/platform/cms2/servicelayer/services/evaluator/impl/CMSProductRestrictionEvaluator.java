package de.hybris.platform.cms2.servicelayer.services.evaluator.impl;

import de.hybris.platform.cms2.model.restrictions.CMSProductRestrictionModel;
import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.cms2.servicelayer.services.evaluator.CMSRestrictionEvaluator;
import org.apache.log4j.Logger;

public class CMSProductRestrictionEvaluator implements CMSRestrictionEvaluator<CMSProductRestrictionModel>
{
    private static final Logger LOG = Logger.getLogger(CMSProductRestrictionEvaluator.class);


    public boolean evaluate(CMSProductRestrictionModel productRestrictionModel, RestrictionData context)
    {
        if(context == null)
        {
            return true;
        }
        if(context.hasProduct())
        {
            String code = context.getProduct().getCode();
            return productRestrictionModel.getProductCodes().contains(code);
        }
        LOG.warn("Could not evaluate CMSProductRestriction. RestrictionData contains no product. Returning false.");
        return false;
    }
}
