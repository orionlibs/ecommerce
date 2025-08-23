/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.constraints;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.configurablebundleservices.model.AbstractBundleRuleModel;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.collections.CollectionUtils;

/**
 * Validates that at least 1 target product is assigned to the given
 * {@link de.hybris.platform.configurablebundleservices.model.DisableProductBundleRuleModel}.
 */
public class AbstractBundleRuleTargetProductsAssignedValidator
                extends BasicBundleRuleValidator<AbstractBundleRuleTargetProductsAssigned>
{
    @Override
    public boolean isValid(final AbstractBundleRuleModel value, final ConstraintValidatorContext context)
    {
        validateParameterNotNull(value, "Validating object is null");
        if(CollectionUtils.isEmpty(value.getTargetProducts()))
        {
            buildErrorMessage(AbstractBundleRuleModel.TARGETPRODUCTS, context);
            return false;
        }
        return true;
    }
}
