package de.hybris.platform.configurablebundlecockpits.services.search.impl;

import de.hybris.platform.cockpit.model.search.SearchParameterValue;
import de.hybris.platform.cockpit.services.search.ConditionTranslatorContext;
import de.hybris.platform.cockpit.services.search.impl.ReferenceGenericQueryConditionTranslator;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericSearchField;
import org.apache.log4j.Logger;

public class BundlesReferenceGenericQueryConditionTranslator extends ReferenceGenericQueryConditionTranslator
{
    private static final String IS_EMPTY = "isEmpty";
    private static final Logger LOG = Logger.getLogger(BundlesReferenceGenericQueryConditionTranslator.class);


    public GenericCondition translate(SearchParameterValue paramValue, ConditionTranslatorContext ctx)
    {
        if("isEmpty".equals(paramValue.getOperator().getQualifier()))
        {
            GenericSearchField field = new GenericSearchField(getTypeService().getAttributeCodeFromPropertyQualifier(paramValue
                            .getParameterDescriptor().getQualifier()));
            return GenericCondition.createIsNullCondition(field);
        }
        LOG.error(String.format("Operator '%s' is not supported by %s. Condition will be ignored.", new Object[] {paramValue.getOperator()
                        .getQualifier(), getClass().getName()}));
        return null;
    }
}
