package de.hybris.platform.servicelayer.i18n.interceptors;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.session.SessionService;
import org.springframework.beans.factory.annotation.Required;

public class RemoveSessionLanguageInterceptor implements RemoveInterceptor
{
    private static final String LANGUAGE_SESSION_KEY = "language";
    private SessionService sessionService;


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    public void onRemove(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof LanguageModel)
        {
            if(this.sessionService.getAttribute("language") instanceof LanguageModel)
            {
                LanguageModel languageForSession = (LanguageModel)this.sessionService.getAttribute("language");
                if(languageForSession.equals(model))
                {
                    throw new InterceptorException("Cannot remove the language " + model + " which is current for session.");
                }
            }
        }
    }
}
