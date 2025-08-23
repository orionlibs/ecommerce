package de.hybris.platform.servicelayer.web.session.interceptor;

import de.hybris.platform.cache.Cache;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.web.StoredHttpSessionModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.web.session.internal.SessionUtils;

public class StoredHttpSessionRemoveInterceptor implements RemoveInterceptor<StoredHttpSessionModel>
{
    public void onRemove(StoredHttpSessionModel storedHttpSessionModel, InterceptorContext ctx) throws InterceptorException
    {
        Cache cache = Registry.getCurrentTenant().getCache();
        String sessionId = storedHttpSessionModel.getSessionId();
        cache.invalidate(SessionUtils.createSessionCacheKey(sessionId), 2);
    }
}
