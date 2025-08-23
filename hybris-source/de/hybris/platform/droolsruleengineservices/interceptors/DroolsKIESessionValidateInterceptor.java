package de.hybris.platform.droolsruleengineservices.interceptors;

import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIESessionModel;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import java.util.Optional;

public class DroolsKIESessionValidateInterceptor implements ValidateInterceptor<DroolsKIESessionModel>
{
    private static final String SESSION_BASE_ERROR_MSG = "exception.droolskiesessionvalidateinterceptor.session.base";
    private static final String BASE_MODULE_ERROR_MSG = "exception.droolskiesessionvalidateinterceptor.base";
    private static final String SESSION_DEFINED_ERROR_MSG = "exception.droolskiesessionvalidateinterceptor.session.defined";
    private L10NService l10NService;


    public void onValidate(DroolsKIESessionModel session, InterceptorContext context) throws InterceptorException
    {
        validateProperties(session);
        DroolsKIEModuleModel kmodule = session.getKieBase().getKieModule();
        if(kmodule.getKieBases() != null)
        {
            validateKieBases(session, kmodule);
        }
        else if(session.getKieBase().getKieSessions() != null)
        {
            validateKieSessions(session, kmodule);
        }
    }


    protected void validateProperties(DroolsKIESessionModel session) throws InterceptorException
    {
        if(session.getKieBase() == null)
        {
            throw new InterceptorException(getL10NService().getLocalizedString("exception.droolskiesessionvalidateinterceptor.session.base", new Object[] {session.getName()}));
        }
        if(session.getKieBase().getKieModule() == null)
        {
            throw new InterceptorException(getL10NService().getLocalizedString("exception.droolskiesessionvalidateinterceptor.base", new Object[] {session.getKieBase().getName()}));
        }
    }


    protected void validateKieBases(DroolsKIESessionModel session, DroolsKIEModuleModel kmodule) throws InterceptorException
    {
        String name = session.getName();
        Optional<DroolsKIESessionModel> existingSession = kmodule.getKieBases().stream().flatMap(b -> b.getKieSessions().stream().filter(())).findAny();
        if(existingSession.isPresent())
        {
            DroolsKIEBaseModel kieBase = ((DroolsKIESessionModel)existingSession.get()).getKieBase();
            throw new InterceptorException(getL10NService().getLocalizedString("exception.droolskiesessionvalidateinterceptor.session.defined", new Object[] {name, kieBase.getName(), kmodule.getName()}));
        }
    }


    protected void validateKieSessions(DroolsKIESessionModel session, DroolsKIEModuleModel kmodule) throws InterceptorException
    {
        String name = session.getName();
        for(DroolsKIESessionModel s : session.getKieBase().getKieSessions())
        {
            if(name.equals(s.getName()) && !s.equals(session))
            {
                throw new InterceptorException(getL10NService().getLocalizedString("exception.droolskiesessionvalidateinterceptor.session.defined", new Object[] {name, session.getKieBase().getName(), kmodule.getName()}));
            }
        }
    }


    protected L10NService getL10NService()
    {
        return this.l10NService;
    }


    public void setL10NService(L10NService l10NService)
    {
        this.l10NService = l10NService;
    }
}
