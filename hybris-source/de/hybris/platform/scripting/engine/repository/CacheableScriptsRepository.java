package de.hybris.platform.scripting.engine.repository;

import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.scripting.engine.content.ScriptContent;

public interface CacheableScriptsRepository
{
    ScriptContent lookupScript(CacheKey paramCacheKey);


    CacheKey createCacheKey(String paramString1, String paramString2);
}
