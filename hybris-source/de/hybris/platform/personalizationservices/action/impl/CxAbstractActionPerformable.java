package de.hybris.platform.personalizationservices.action.impl;

import de.hybris.platform.personalizationservices.CxContext;
import de.hybris.platform.personalizationservices.data.CxAbstractActionResult;
import de.hybris.platform.personalizationservices.model.CxAbstractActionModel;
import de.hybris.platform.personalizationservices.model.CxCustomizationModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.servicelayer.action.impl.ActionPerformable;
import de.hybris.platform.servicelayer.model.action.AbstractActionModel;
import java.util.Objects;
import java.util.Optional;

public abstract class CxAbstractActionPerformable<T extends CxAbstractActionModel> implements ActionPerformable<CxContext>
{
    protected abstract CxAbstractActionResult executeAction(T paramT, CxContext paramCxContext);


    public void performAction(AbstractActionModel action, CxContext context)
    {
        CxAbstractActionResult actionResult = executeAction((T)action, context);
        actionResult.setActionCode(action.getCode());
        if(action instanceof CxAbstractActionModel)
        {
            CxAbstractActionModel cxaction = (CxAbstractActionModel)action;
            Optional<CxVariationModel> variation = Optional.<CxAbstractActionModel>of(cxaction).map(CxAbstractActionModel::getVariation);
            Optional<CxCustomizationModel> customization = variation.map(CxVariationModel::getCustomization);
            Objects.requireNonNull(actionResult);
            variation.map(CxVariationModel::getCode).ifPresent(actionResult::setVariationCode);
            Objects.requireNonNull(actionResult);
            variation.map(CxVariationModel::getName).ifPresent(actionResult::setVariationName);
            Objects.requireNonNull(actionResult);
            customization.map(CxCustomizationModel::getCode).ifPresent(actionResult::setCustomizationCode);
            Objects.requireNonNull(actionResult);
            customization.map(CxCustomizationModel::getName).ifPresent(actionResult::setCustomizationName);
        }
        context.setActionResult(actionResult);
    }
}
