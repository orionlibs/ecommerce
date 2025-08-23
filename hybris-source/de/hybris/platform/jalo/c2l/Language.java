package de.hybris.platform.jalo.c2l;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloImplementationManager;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.Utilities;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Language extends GeneratedLanguage
{
    @Deprecated(since = "ages", forRemoval = false)
    public static final String FALLBACK_LANGUAGES = "fallbackLanguages";


    @Deprecated(since = "ages", forRemoval = false)
    public static void registerAsJaloObject()
    {
        JaloImplementationManager.clearJaloObjectMapping(Language.class);
    }


    @ForceJALO(reason = "consistency check")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        if(!allAttributes.containsKey("isocode"))
        {
            throw new JaloInvalidParameterException("Missing parameter( isocode ) to create a Language.", 0);
        }
        checkConsistencyIsocode((String)allAttributes.get("isocode"), null, type.getCode());
        allAttributes.setAttributeMode("isocode", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }


    protected String isoCodeCheck = null;
    protected Locale localeCache = null;


    public Locale getLocale()
    {
        String isoCode = getIsocode();
        if(!isoCode.equals(this.isoCodeCheck) || this.localeCache == null)
        {
            this.localeCache = Utilities.getLocale(this.isoCodeCheck = isoCode);
        }
        return this.localeCache;
    }


    @ForceJALO(reason = "consistency check")
    public void setFallbackLanguages(SessionContext ctx, List<Language> fallBackTo)
    {
        if(fallBackTo != null && fallBackTo.contains(this))
        {
            throw new JaloInvalidParameterException("fallback languages cycle detected for " + this + " and " + fallBackTo, 0);
        }
        setProperty("fallbackLanguages", (fallBackTo != null) ? new ArrayList<>(fallBackTo) : null);
    }


    public void setFallbackLanguages(Language... fallBackTo) throws JaloInvalidParameterException
    {
        setFallbackLanguages(Arrays.asList(fallBackTo));
    }


    @ForceJALO(reason = "something else")
    public List<Language> getFallbackLanguages(SessionContext ctx)
    {
        List<? extends Language> l = (List)getProperty(ctx, "fallbackLanguages");
        return (l != null) ? Collections.<Language>unmodifiableList(l) : Collections.EMPTY_LIST;
    }


    @ForceJALO(reason = "consistency check")
    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        if(equals(getSession().getSessionContext().getLanguage()))
        {
            throw new ConsistencyCheckException("Cannot remove current session language!", 555);
        }
        super.remove(ctx);
    }
}
