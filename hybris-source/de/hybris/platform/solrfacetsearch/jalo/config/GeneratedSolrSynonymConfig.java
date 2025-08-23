package de.hybris.platform.solrfacetsearch.jalo.config;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.solrfacetsearch.constants.GeneratedSolrfacetsearchConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSolrSynonymConfig extends GenericItem
{
    public static final String SYNONYMFROM = "synonymFrom";
    public static final String SYNONYMTO = "synonymTo";
    public static final String LANGUAGEPOS = "languagePOS";
    public static final String LANGUAGE = "language";
    public static final String FACETSEARCHCONFIGPOS = "facetSearchConfigPOS";
    public static final String FACETSEARCHCONFIG = "facetSearchConfig";
    protected static final BidirectionalOneToManyHandler<GeneratedSolrSynonymConfig> LANGUAGEHANDLER = new BidirectionalOneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRSYNONYMCONFIG, false, "language", "languagePOS", true, true, 2);
    protected static final BidirectionalOneToManyHandler<GeneratedSolrSynonymConfig> FACETSEARCHCONFIGHANDLER = new BidirectionalOneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRSYNONYMCONFIG, false, "facetSearchConfig", "facetSearchConfigPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("synonymFrom", Item.AttributeMode.INITIAL);
        tmp.put("synonymTo", Item.AttributeMode.INITIAL);
        tmp.put("languagePOS", Item.AttributeMode.INITIAL);
        tmp.put("language", Item.AttributeMode.INITIAL);
        tmp.put("facetSearchConfigPOS", Item.AttributeMode.INITIAL);
        tmp.put("facetSearchConfig", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        LANGUAGEHANDLER.newInstance(ctx, allAttributes);
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


    Integer getFacetSearchConfigPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "facetSearchConfigPOS");
    }


    Integer getFacetSearchConfigPOS()
    {
        return getFacetSearchConfigPOS(getSession().getSessionContext());
    }


    int getFacetSearchConfigPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getFacetSearchConfigPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getFacetSearchConfigPOSAsPrimitive()
    {
        return getFacetSearchConfigPOSAsPrimitive(getSession().getSessionContext());
    }


    void setFacetSearchConfigPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "facetSearchConfigPOS", value);
    }


    void setFacetSearchConfigPOS(Integer value)
    {
        setFacetSearchConfigPOS(getSession().getSessionContext(), value);
    }


    void setFacetSearchConfigPOS(SessionContext ctx, int value)
    {
        setFacetSearchConfigPOS(ctx, Integer.valueOf(value));
    }


    void setFacetSearchConfigPOS(int value)
    {
        setFacetSearchConfigPOS(getSession().getSessionContext(), value);
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
        LANGUAGEHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setLanguage(Language value)
    {
        setLanguage(getSession().getSessionContext(), value);
    }


    Integer getLanguagePOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "languagePOS");
    }


    Integer getLanguagePOS()
    {
        return getLanguagePOS(getSession().getSessionContext());
    }


    int getLanguagePOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getLanguagePOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getLanguagePOSAsPrimitive()
    {
        return getLanguagePOSAsPrimitive(getSession().getSessionContext());
    }


    void setLanguagePOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "languagePOS", value);
    }


    void setLanguagePOS(Integer value)
    {
        setLanguagePOS(getSession().getSessionContext(), value);
    }


    void setLanguagePOS(SessionContext ctx, int value)
    {
        setLanguagePOS(ctx, Integer.valueOf(value));
    }


    void setLanguagePOS(int value)
    {
        setLanguagePOS(getSession().getSessionContext(), value);
    }


    public String getSynonymFrom(SessionContext ctx)
    {
        return (String)getProperty(ctx, "synonymFrom");
    }


    public String getSynonymFrom()
    {
        return getSynonymFrom(getSession().getSessionContext());
    }


    public void setSynonymFrom(SessionContext ctx, String value)
    {
        setProperty(ctx, "synonymFrom", value);
    }


    public void setSynonymFrom(String value)
    {
        setSynonymFrom(getSession().getSessionContext(), value);
    }


    public String getSynonymTo(SessionContext ctx)
    {
        return (String)getProperty(ctx, "synonymTo");
    }


    public String getSynonymTo()
    {
        return getSynonymTo(getSession().getSessionContext());
    }


    public void setSynonymTo(SessionContext ctx, String value)
    {
        setProperty(ctx, "synonymTo", value);
    }


    public void setSynonymTo(String value)
    {
        setSynonymTo(getSession().getSessionContext(), value);
    }
}
