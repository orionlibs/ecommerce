package de.hybris.platform.personalizationservices.service;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.RecalculateAction;
import java.util.List;

public interface CxRecalculationService
{
    void recalculate(List<RecalculateAction> paramList);


    void recalculate(UserModel paramUserModel, List<RecalculateAction> paramList);
}
