package de.hybris.platform.personalizationservices.trigger.impl;

import de.hybris.platform.personalizationservices.model.CxAbstractTriggerModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import java.util.Collection;
import java.util.Optional;
import org.apache.commons.collections4.CollectionUtils;

public class BaseTriggerInterceptor
{
    protected void isTriggerUnique(CxAbstractTriggerModel model, CxVariationModel variation, InterceptorContext context) throws InterceptorException
    {
        context.getModelService().refresh(variation);
        Collection<CxAbstractTriggerModel> triggers = variation.getTriggers();
        if(CollectionUtils.isEmpty(triggers))
        {
            return;
        }
        Optional<CxAbstractTriggerModel> otherTrigger = triggers.stream()
                        .filter(t -> (t instanceof de.hybris.platform.personalizationservices.model.CxSegmentTriggerModel || t instanceof de.hybris.platform.personalizationservices.model.CxExpressionTriggerModel || t instanceof de.hybris.platform.personalizationservices.model.CxDefaultTriggerModel))
                        .filter(t -> !model.getCode().equals(t.getCode())).findAny();
        if(otherTrigger.isPresent())
        {
            throw new InterceptorException("CxSegmentTrigger or CxExpressionTrigger already exists for this variation.");
        }
    }
}
