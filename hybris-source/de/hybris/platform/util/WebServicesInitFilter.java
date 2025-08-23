package de.hybris.platform.util;

import de.hybris.platform.jalo.JaloConnectException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

public class WebServicesInitFilter extends HybrisInitFilter
{
    private static final Logger log = Logger.getLogger(WebServicesInitFilter.class.getName());


    public boolean doPreRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException
    {
        try
        {
            JaloSession session = getJaloSession(request, response, true);
            String languagesString = request.getHeader("Accept-Language");
            setLanguage(session, languagesString);
            notifyExtensions(request, response, true);
            return true;
        }
        catch(JaloConnectException e)
        {
            throw new ServletException("error getting session ", e);
        }
    }


    private void setLanguage(JaloSession session, String languagesString)
    {
        if(languagesString != null && !languagesString.isEmpty())
        {
            List<Lang> languages = getLanguages(languagesString);
            SessionContext context = session.getSessionContext();
            Set<Language> activeLanguages = session.getC2LManager().getActiveLanguages();
            for(Lang lang : languages)
            {
                for(Language language : activeLanguages)
                {
                    if(language.getIsoCode().equals(lang.getIsoCode()))
                    {
                        log.debug("language from Accept-Language header: " + lang.getIsoCode());
                        context.setLanguage(language);
                        // Byte code: goto -> 141
                    }
                }
            }
        }
    }


    private List<Lang> getLanguages(String languagesString)
    {
        if(languagesString.contains(","))
        {
            return getSortedLanguages(languagesString.split(","));
        }
        List<Lang> languages = new ArrayList<>();
        languages.add(getLanguage(languagesString));
        return languages;
    }


    private Lang getLanguage(String languagesString)
    {
        String languageString = languagesString.trim();
        if(languageString.contains(";q="))
        {
            String[] lang = languageString.split(";q=");
            try
            {
                return new Lang(lang[0], Double.parseDouble(lang[1]));
            }
            catch(NumberFormatException e)
            {
                return new Lang(lang[0]);
            }
        }
        return new Lang(languageString);
    }


    private List<Lang> getSortedLanguages(String[] languagesString)
    {
        List<Lang> languages = new ArrayList<>();
        for(String languageString : languagesString)
        {
            languages.add(getLanguage(languageString));
        }
        Collections.sort(languages);
        return languages;
    }
}
