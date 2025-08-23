package com.hybris.cis.api.subscription.mock.util;

import com.hybris.cis.api.exception.ServiceRequestException;
import com.hybris.cis.api.exception.codes.ServiceExceptionDetail;
import com.hybris.cis.api.subscription.exception.SubscriptionServiceExceptionCodes;
import com.hybris.cis.api.subscription.model.CisChangePaymentMethodRequest;
import com.hybris.cis.api.subscription.model.CisPaymentMethodUpdateRequest;
import java.util.Locale;

public final class CisChangePaymentMethodRequestConverter
{
    private CisChangePaymentMethodRequestConverter() throws IllegalAccessException
    {
        throw new IllegalAccessException(String.format("Utility class %s may not be instantiated.", new Object[] {CisChangePaymentMethodRequestConverter.class
                        .getSimpleName()}));
    }


    public static CisPaymentMethodUpdateRequest convert(CisChangePaymentMethodRequest source)
    {
        CisPaymentMethodUpdateRequest target = new CisPaymentMethodUpdateRequest();
        target.setMerchantPaymentMethodId(source.getMerchantPaymentMethodId());
        target.setParameters(source.getParameters());
        String action = source.getAction();
        switch(action.toLowerCase(Locale.ENGLISH))
        {
            case "enable":
                target.setEnabled(Boolean.valueOf(true));
                target.setPropagate(Boolean.valueOf(source.isPropagate()));
                return target;
            case "disable":
                target.setEnabled(Boolean.valueOf(false));
                target.setPropagate(Boolean.valueOf(source.isPropagate()));
                return target;
        }
        throw new ServiceRequestException(new ServiceExceptionDetail(SubscriptionServiceExceptionCodes.PARAMETERS_REF_INVALID, String.format("Unknown action '%s' for payment method update.", new Object[] {action})));
    }
}
