package de.hybris.platform.platformbackoffice.validation.interceptors;

import de.hybris.platform.platformbackoffice.validation.model.constraints.HybrisEnumValueCodeConstraintModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import org.apache.commons.lang3.StringUtils;

public class HybrisEnumValueCodeConstraintValidator implements ValidateInterceptor
{
    public void onValidate(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof HybrisEnumValueCodeConstraintModel)
        {
            HybrisEnumValueCodeConstraintModel constraint = (HybrisEnumValueCodeConstraintModel)model;
            if(StringUtils.isEmpty(constraint.getValue()))
            {
                throw new InterceptorException("The value for a HybrisEnumValue code constraint is empty!", this);
            }
        }
    }
}
