package de.hybris.platform.fraud;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.fraud.impl.FraudServiceResponse;
import java.util.Collection;

public interface FraudService
{
    FraudServiceResponse recognizeOrderSymptoms(String paramString, AbstractOrderModel paramAbstractOrderModel);


    FraudServiceResponse recognizeActivitySymptoms(String paramString, UserModel paramUserModel);


    FraudServiceProvider getProvider(String paramString);


    Collection<FraudServiceProvider> getProviders();
}
