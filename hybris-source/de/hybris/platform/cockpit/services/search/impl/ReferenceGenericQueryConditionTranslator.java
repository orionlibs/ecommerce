package de.hybris.platform.cockpit.services.search.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.search.SearchParameterValue;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.search.ConditionTranslatorContext;
import de.hybris.platform.cockpit.services.search.GenericQueryConditionTranslator;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.Operator;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReferenceGenericQueryConditionTranslator implements GenericQueryConditionTranslator
{
    private static final String IN = "in";
    private static final Logger LOG = LoggerFactory.getLogger(ReferenceGenericQueryConditionTranslator.class);
    private TypeService typeService;
    private ModelService modelService;


    public GenericCondition translate(SearchParameterValue paramValue, ConditionTranslatorContext ctx)
    {
        GenericCondition ret = null;
        if("in".equals(paramValue.getOperator().getQualifier()) && paramValue.getValue() instanceof java.util.Collection)
        {
            List<Object> items = new ArrayList();
            for(Object item : paramValue.getValue())
            {
                if(item instanceof de.hybris.platform.core.model.ItemModel)
                {
                    items.add(getModelService().getSource(item));
                    continue;
                }
                if(item instanceof TypedObject)
                {
                    Object object = ((TypedObject)item).getObject();
                    if(object instanceof de.hybris.platform.core.model.ItemModel)
                    {
                        items.add(getModelService().getSource(object));
                    }
                    continue;
                }
                LOG.error("The item '" + item + "' is not a ItemModel or TypedObject. Condition will be ignored.");
                return null;
            }
            ret = GenericCondition.createConditionForValueComparison(new GenericSearchField(getTypeService()
                            .getAttributeCodeFromPropertyQualifier(paramValue.getParameterDescriptor().getQualifier())), Operator.IN, items);
        }
        return ret;
    }


    protected String getAttributeQuali(GenericSearchParameterDescriptor descriptor)
    {
        if(descriptor instanceof ItemAttributeSearchDescriptor)
        {
            return ((ItemAttributeSearchDescriptor)descriptor).getLastAttributeDescriptor().getQualifier();
        }
        return descriptor.getAttributeQualifier();
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
