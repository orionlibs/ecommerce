package de.hybris.platform.solrfacetsearch.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.solrfacetsearch.constants.GeneratedSolrfacetsearchConstants;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrIndexedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedSolrSort extends GenericItem
{
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String USEBOOST = "useBoost";
    public static final String INDEXEDTYPEPOS = "indexedTypePOS";
    public static final String INDEXEDTYPE = "indexedType";
    public static final String FIELDS = "fields";
    protected static final BidirectionalOneToManyHandler<GeneratedSolrSort> INDEXEDTYPEHANDLER = new BidirectionalOneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRSORT, false, "indexedType", "indexedTypePOS", true, true, 2);
    protected static final OneToManyHandler<SolrSortField> FIELDSHANDLER = new OneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRSORTFIELD, true, "sort", "sortPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("useBoost", Item.AttributeMode.INITIAL);
        tmp.put("indexedTypePOS", Item.AttributeMode.INITIAL);
        tmp.put("indexedType", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    public void setCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "code", value);
    }


    public void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        INDEXEDTYPEHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public List<SolrSortField> getFields(SessionContext ctx)
    {
        return (List<SolrSortField>)FIELDSHANDLER.getValues(ctx, (Item)this);
    }


    public List<SolrSortField> getFields()
    {
        return getFields(getSession().getSessionContext());
    }


    public void setFields(SessionContext ctx, List<SolrSortField> value)
    {
        FIELDSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setFields(List<SolrSortField> value)
    {
        setFields(getSession().getSessionContext(), value);
    }


    public void addToFields(SessionContext ctx, SolrSortField value)
    {
        FIELDSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToFields(SolrSortField value)
    {
        addToFields(getSession().getSessionContext(), value);
    }


    public void removeFromFields(SessionContext ctx, SolrSortField value)
    {
        FIELDSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromFields(SolrSortField value)
    {
        removeFromFields(getSession().getSessionContext(), value);
    }


    public SolrIndexedType getIndexedType(SessionContext ctx)
    {
        return (SolrIndexedType)getProperty(ctx, "indexedType");
    }


    public SolrIndexedType getIndexedType()
    {
        return getIndexedType(getSession().getSessionContext());
    }


    public void setIndexedType(SessionContext ctx, SolrIndexedType value)
    {
        INDEXEDTYPEHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setIndexedType(SolrIndexedType value)
    {
        setIndexedType(getSession().getSessionContext(), value);
    }


    Integer getIndexedTypePOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "indexedTypePOS");
    }


    Integer getIndexedTypePOS()
    {
        return getIndexedTypePOS(getSession().getSessionContext());
    }


    int getIndexedTypePOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getIndexedTypePOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getIndexedTypePOSAsPrimitive()
    {
        return getIndexedTypePOSAsPrimitive(getSession().getSessionContext());
    }


    void setIndexedTypePOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "indexedTypePOS", value);
    }


    void setIndexedTypePOS(Integer value)
    {
        setIndexedTypePOS(getSession().getSessionContext(), value);
    }


    void setIndexedTypePOS(SessionContext ctx, int value)
    {
        setIndexedTypePOS(ctx, Integer.valueOf(value));
    }


    void setIndexedTypePOS(int value)
    {
        setIndexedTypePOS(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedSolrSort.getName requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public Map<Language, String> getAllName(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "name", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllName()
    {
        return getAllName(getSession().getSessionContext());
    }


    public void setName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedSolrSort.setName requires a session language", 0);
        }
        setLocalizedProperty(ctx, "name", value);
    }


    public void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }


    public void setAllName(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "name", value);
    }


    public void setAllName(Map<Language, String> value)
    {
        setAllName(getSession().getSessionContext(), value);
    }


    public Boolean isUseBoost(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "useBoost");
    }


    public Boolean isUseBoost()
    {
        return isUseBoost(getSession().getSessionContext());
    }


    public boolean isUseBoostAsPrimitive(SessionContext ctx)
    {
        Boolean value = isUseBoost(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isUseBoostAsPrimitive()
    {
        return isUseBoostAsPrimitive(getSession().getSessionContext());
    }


    public void setUseBoost(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "useBoost", value);
    }


    public void setUseBoost(Boolean value)
    {
        setUseBoost(getSession().getSessionContext(), value);
    }


    public void setUseBoost(SessionContext ctx, boolean value)
    {
        setUseBoost(ctx, Boolean.valueOf(value));
    }


    public void setUseBoost(boolean value)
    {
        setUseBoost(getSession().getSessionContext(), value);
    }
}
