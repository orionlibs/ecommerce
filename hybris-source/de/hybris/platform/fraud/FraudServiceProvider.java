package de.hybris.platform.fraud;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.fraud.impl.FraudServiceResponse;

public interface FraudServiceProvider
{
    String getProviderName();


    FraudServiceResponse recognizeOrderFraudSymptoms(AbstractOrderModel paramAbstractOrderModel);


    FraudServiceResponse recognizeUserActivitySymptoms(UserModel paramUserModel);
}
