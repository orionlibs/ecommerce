package de.hybris.platform.servicelayer.i18n.interceptors;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import org.springframework.beans.factory.annotation.Required;

public class RemoveLastLanguageInterceptor implements RemoveInterceptor
{
    private SessionService sessionService;
    private FlexibleSearchService flexibleSearchService;


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    public void onRemove(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof de.hybris.platform.core.model.c2l.LanguageModel)
        {
            CheckLanguagesCountSessionExecutionBody executionBody = new CheckLanguagesCountSessionExecutionBody(this);
            this.sessionService.executeInLocalView((SessionExecutionBody)executionBody);
            if(executionBody.isLastLanguage())
            {
                throw new InterceptorException("There must be at least one language avilable.");
            }
        }
    }
}
