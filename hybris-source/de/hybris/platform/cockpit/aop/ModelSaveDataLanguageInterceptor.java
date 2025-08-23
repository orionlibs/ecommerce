package de.hybris.platform.cockpit.aop;

import de.hybris.platform.cockpit.session.UISession;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import java.util.Locale;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;

public class ModelSaveDataLanguageInterceptor
{
    public Object intercept(ProceedingJoinPoint pjp) throws Throwable
    {
        SessionContext ctx = null;
        UISession uiSession = UISessionUtils.getCurrentSession();
        JaloSession jaloSession = JaloSession.getCurrentSession();
        try
        {
            if(uiSession != null && jaloSession != null)
            {
                Locale uiLocale = uiSession.getGlobalDataLocale();
                Locale jaloLocale = jaloSession.getSessionContext().getLocale();
                if(jaloLocale == null || !jaloLocale.equals(uiLocale))
                {
                    ctx = jaloSession.createLocalSessionContext();
                    ctx.setLocale(uiLocale);
                    ctx.setLanguage(getGlobalDataLanguage(uiSession));
                }
            }
            return pjp.proceed();
        }
        finally
        {
            if(jaloSession != null && ctx != null)
            {
                jaloSession.removeLocalSessionContext();
            }
        }
    }


    private Language getGlobalDataLanguage(UISession uiSession)
    {
        if(uiSession != null)
        {
            String iso = uiSession.getGlobalDataLanguageIso();
            if(!StringUtils.isEmpty(iso))
            {
                try
                {
                    return C2LManager.getInstance().getLanguageByIsoCode(iso);
                }
                catch(JaloItemNotFoundException jaloItemNotFoundException)
                {
                }
            }
        }
        return null;
    }
}
