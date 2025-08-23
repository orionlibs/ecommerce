package de.hybris.platform.impex.jalo;

import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

public class PLA_12491_Test_Interceptor implements ValidateInterceptor<TitleModel>
{
    private volatile String codeToThrowErrorOn;
    private volatile boolean errorThrown;


    public void onValidate(TitleModel model, InterceptorContext ctx) throws InterceptorException
    {
        if(this.codeToThrowErrorOn != null && this.codeToThrowErrorOn.equalsIgnoreCase(model.getCode()))
        {
            this.errorThrown = true;
            throw new RuntimeException("intentional runtime error inside error - PLA-12491");
        }
    }


    public void setUpForTest(String codeToThrowErrorOn)
    {
        this.codeToThrowErrorOn = codeToThrowErrorOn;
    }


    public boolean errorWasThrown()
    {
        return this.errorThrown;
    }


    public void reset()
    {
        this.codeToThrowErrorOn = null;
        this.errorThrown = false;
    }
}
