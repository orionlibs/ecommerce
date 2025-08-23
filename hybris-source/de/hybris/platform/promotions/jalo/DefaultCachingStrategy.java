package de.hybris.platform.promotions.jalo;

import de.hybris.platform.servicelayer.session.SessionService;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCachingStrategy implements CachingStrategy
{
    public static final String SESSION_CACHE_ATTRIBUTE = "promotion.cache";
    private SessionService sessionService;


    public void put(String code, List<PromotionResult> results)
    {
        getCache().put(code, results);
    }


    public List<PromotionResult> get(String code)
    {
        List<PromotionResult> results = getCache().get(code);
        return (results == null) ? Collections.<PromotionResult>emptyList() : results;
    }


    public void remove(String code)
    {
        getCache().remove(code);
    }


    protected Map<String, List<PromotionResult>> getCache()
    {
        return ((SessionCacheContainer)getSessionService().getOrLoadAttribute("promotion.cache", () -> new SessionCacheContainer(this))).getCache();
    }


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }
}
