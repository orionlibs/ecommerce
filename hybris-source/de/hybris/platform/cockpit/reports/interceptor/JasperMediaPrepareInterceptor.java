package de.hybris.platform.cockpit.reports.interceptor;

import de.hybris.platform.cockpit.reports.JasperMediaService;
import de.hybris.platform.cockpit.reports.model.JasperMediaModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import org.springframework.beans.factory.annotation.Required;

public class JasperMediaPrepareInterceptor implements PrepareInterceptor
{
    private transient JasperMediaService jasperMediaService;


    public void onPrepare(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(!(model instanceof JasperMediaModel))
        {
            throw new InterceptorException("Wrong type, should be JasperMediaModel, but was: " + model.getClass().getName());
        }
        JasperMediaModel jasperMedia = (JasperMediaModel)model;
        if(jasperMedia.getFolder() == null)
        {
            try
            {
                jasperMedia.setFolder(this.jasperMediaService.getJasperReportsMediaFolder());
            }
            catch(UnknownIdentifierException uie)
            {
                jasperMedia.setFolder(null);
            }
        }
    }


    @Required
    public void setJasperMediaService(JasperMediaService jasperMediaService)
    {
        this.jasperMediaService = jasperMediaService;
    }
}
