package de.hybris.platform.cms2.jalo.restrictions;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRestriction extends GeneratedAbstractRestriction
{
    @Deprecated(since = "4.3")
    public abstract String getDescription(SessionContext paramSessionContext);


    @Deprecated(since = "4.3")
    public String getDescription()
    {
        return getDescription(getSession().getSessionContext());
    }


    @Deprecated(since = "4.3")
    public String getType(SessionContext ctx)
    {
        return getComposedType().getName(ctx);
    }


    @Deprecated(since = "4.3")
    public String getTypeCode(SessionContext ctx)
    {
        return getComposedType().getCode();
    }


    @Deprecated(since = "4.3")
    public Map<Language, String> getAllType(SessionContext ctx)
    {
        return getComposedType().getAllNames(ctx);
    }


    @Deprecated(since = "4.3")
    public Map<Language, String> getAllDescription(SessionContext ctx)
    {
        Map<Language, String> allDescription = new HashMap<>();
        Collection<Language> languages = C2LManager.getInstance().getAllLanguages();
        for(Language language : languages)
        {
            SessionContext localContext = null;
            try
            {
                localContext = JaloSession.getCurrentSession().createLocalSessionContext();
                localContext.setLanguage(language);
                allDescription.put(language, getDescription(localContext));
            }
            finally
            {
                if(localContext != null)
                {
                    JaloSession.getCurrentSession().removeLocalSessionContext();
                }
            }
        }
        return allDescription;
    }
}
