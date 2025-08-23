package de.hybris.platform.solrfacetsearch.jalo.redirect;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.solrfacetsearch.constants.GeneratedSolrfacetsearchConstants;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrFacetSearchConfig;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSolrFacetSearchKeywordRedirect extends GenericItem
{
    public static final String LANGUAGE = "language";
    public static final String KEYWORD = "keyword";
    public static final String MATCHTYPE = "matchType";
    public static final String IGNORECASE = "ignoreCase";
    public static final String REDIRECT = "redirect";
    public static final String FACETSEARCHCONFIG = "facetSearchConfig";
    protected static final BidirectionalOneToManyHandler<GeneratedSolrFacetSearchKeywordRedirect> FACETSEARCHCONFIGHANDLER = new BidirectionalOneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRFACETSEARCHKEYWORDREDIRECT, false, "facetSearchConfig", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("language", Item.AttributeMode.INITIAL);
        tmp.put("keyword", Item.AttributeMode.INITIAL);
        tmp.put("matchType", Item.AttributeMode.INITIAL);
        tmp.put("ignoreCase", Item.AttributeMode.INITIAL);
        tmp.put("redirect", Item.AttributeMode.INITIAL);
        tmp.put("facetSearchConfig", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        FACETSEARCHCONFIGHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public SolrFacetSearchConfig getFacetSearchConfig(SessionContext ctx)
    {
        return (SolrFacetSearchConfig)getProperty(ctx, "facetSearchConfig");
    }


    public SolrFacetSearchConfig getFacetSearchConfig()
    {
        return getFacetSearchConfig(getSession().getSessionContext());
    }


    public void setFacetSearchConfig(SessionContext ctx, SolrFacetSearchConfig value)
    {
        FACETSEARCHCONFIGHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setFacetSearchConfig(SolrFacetSearchConfig value)
    {
        setFacetSearchConfig(getSession().getSessionContext(), value);
    }


    public Boolean isIgnoreCase(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "ignoreCase");
    }


    public Boolean isIgnoreCase()
    {
        return isIgnoreCase(getSession().getSessionContext());
    }


    public boolean isIgnoreCaseAsPrimitive(SessionContext ctx)
    {
        Boolean value = isIgnoreCase(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isIgnoreCaseAsPrimitive()
    {
        return isIgnoreCaseAsPrimitive(getSession().getSessionContext());
    }


    public void setIgnoreCase(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "ignoreCase", value);
    }


    public void setIgnoreCase(Boolean value)
    {
        setIgnoreCase(getSession().getSessionContext(), value);
    }


    public void setIgnoreCase(SessionContext ctx, boolean value)
    {
        setIgnoreCase(ctx, Boolean.valueOf(value));
    }


    public void setIgnoreCase(boolean value)
    {
        setIgnoreCase(getSession().getSessionContext(), value);
    }


    public String getKeyword(SessionContext ctx)
    {
        return (String)getProperty(ctx, "keyword");
    }


    public String getKeyword()
    {
        return getKeyword(getSession().getSessionContext());
    }


    public void setKeyword(SessionContext ctx, String value)
    {
        setProperty(ctx, "keyword", value);
    }


    public void setKeyword(String value)
    {
        setKeyword(getSession().getSessionContext(), value);
    }


    public Language getLanguage(SessionContext ctx)
    {
        return (Language)getProperty(ctx, "language");
    }


    public Language getLanguage()
    {
        return getLanguage(getSession().getSessionContext());
    }


    public void setLanguage(SessionContext ctx, Language value)
    {
        setProperty(ctx, "language", value);
    }


    public void setLanguage(Language value)
    {
        setLanguage(getSession().getSessionContext(), value);
    }


    public EnumerationValue getMatchType(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "matchType");
    }


    public EnumerationValue getMatchType()
    {
        return getMatchType(getSession().getSessionContext());
    }


    public void setMatchType(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "matchType", value);
    }


    public void setMatchType(EnumerationValue value)
    {
        setMatchType(getSession().getSessionContext(), value);
    }


    public SolrAbstractKeywordRedirect getRedirect(SessionContext ctx)
    {
        return (SolrAbstractKeywordRedirect)getProperty(ctx, "redirect");
    }


    public SolrAbstractKeywordRedirect getRedirect()
    {
        return getRedirect(getSession().getSessionContext());
    }


    public void setRedirect(SessionContext ctx, SolrAbstractKeywordRedirect value)
    {
        setProperty(ctx, "redirect", value);
    }


    public void setRedirect(SolrAbstractKeywordRedirect value)
    {
        setRedirect(getSession().getSessionContext(), value);
    }
}
