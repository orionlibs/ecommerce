package de.hybris.platform.jalo.type;

import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.persistence.type.TypeManagerEJB;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.znerd.xmlenc.XMLOutputter;

public class SearchRestriction extends TypeManagerManaged
{
    public static final String NAME = "name";
    public static final String CODE = "code";
    public static final String ACTIVE = "active";
    public static final String QUERY = "query";
    public static final String PRINCIPAL = "principal";
    public static final String RESTRICTEDTYPE = "restrictedType";
    public static final String RESTRICTION_TYPE_ALIAS = "item";


    protected SearchRestrictionImpl impl()
    {
        return (SearchRestrictionImpl)getImplementation();
    }


    protected SearchRestriction createEntity(Principal principal, ComposedType restrictedType, String query, String code, Boolean active) throws ConsistencyCheckException
    {
        return (SearchRestriction)TypeManager.getInstance().wrap((
                        (TypeManagerEJB)TypeManager.getInstance().getRemote()).createRestriction(principal.getPK(), restrictedType
                        .getPK(), query, code, active));
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap atts) throws JaloBusinessException, JaloInvalidParameterException
    {
        Set missing = new HashSet();
        if(((!checkMandatoryAttribute("principal", atts, missing) ? 1 : 0) | (!checkMandatoryAttribute("restrictedType", atts, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute("query", atts, missing) ? 1 : 0)) != 0)
        {
            throw new JaloInvalidParameterException("missing params " + missing + " , got " + atts, 0);
        }
        checkUniqueCodeTypePrincipal((Principal)atts.get("principal"), (ComposedType)atts.get("restrictedType"), (String)atts
                        .get("code"));
        checkRestrictionQuery((ComposedType)atts.get("restrictedType"), (String)atts.get("query"));
        return (Item)createEntity((Principal)atts.get("principal"), (ComposedType)atts.get("restrictedType"), (String)atts.get("query"), (String)atts
                        .get("code"), (Boolean)atts.get("active"));
    }


    protected Item.ItemAttributeMap getNonInitialAttributes(SessionContext ctx, Item.ItemAttributeMap allAttributes)
    {
        Item.ItemAttributeMap copyMap = super.getNonInitialAttributes(ctx, allAttributes);
        copyMap.remove("principal");
        copyMap.remove("restrictedType");
        copyMap.remove("query");
        copyMap.remove("code");
        return copyMap;
    }


    static
    {
        registerAccessFor(SearchRestriction.class, "restrictedType", (AttributeAccess)new Object());
    }

    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    public void setCode(SessionContext ctx, String code)
    {
        setProperty(ctx, "code", code);
    }


    public void setCode(String code)
    {
        setCode(getSession().getSessionContext(), code);
    }


    public boolean isActive()
    {
        return Boolean.TRUE.equals(getProperty("active"));
    }


    public void setActive(boolean active)
    {
        setProperty("active", active ? Boolean.TRUE : Boolean.FALSE);
    }


    public String getName(SessionContext ctx)
    {
        if(ctx.getLanguage() == null)
        {
            throw new JaloSystemException(null, "context requires a language when getting localized property 'name' ", -1);
        }
        return (String)getLocalizedProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public Map getAllName(SessionContext ctx)
    {
        return (Map)getLocalizedProperty(getAllValuesSessionContext(ctx), "name");
    }


    public Map getAllName()
    {
        return getAllName(getSession().getSessionContext());
    }


    public void setName(SessionContext ctx, String name)
    {
        if(ctx.getLanguage() == null)
        {
            throw new JaloSystemException(null, "context requires a language when setting localized property 'name' ", -1);
        }
        setLocalizedProperty(ctx, "name", name);
    }


    public void setName(String name)
    {
        setName(getSession().getSessionContext(), name);
    }


    public void setAllName(SessionContext ctx, Map names)
    {
        setLocalizedProperty(getAllValuesSessionContext(ctx), "name", names);
    }


    public void setAllName(Map names) throws JaloInvalidParameterException
    {
        setAllName(getSession().getSessionContext(), names);
    }


    public String getQuery(SessionContext ctx)
    {
        return (String)(new Object(this, "query"))
                        .get(ctx);
    }


    public String getQuery()
    {
        return getQuery(getSession().getSessionContext());
    }


    public void setQuery(SessionContext ctx, String query) throws JaloInvalidParameterException
    {
        checkRestrictionQuery(getRestrictionType(ctx), query);
        (new Object(this, "query", query))
                        .set(ctx);
    }


    public void setQuery(String query) throws JaloInvalidParameterException
    {
        setQuery(getSession().getSessionContext(), query);
    }


    public ComposedType getRestrictionType(SessionContext ctx)
    {
        return (ComposedType)(new Object(this, "restrictedType"))
                        .get(null);
    }


    public ComposedType getRestrictionType()
    {
        return getRestrictionType(getSession().getSessionContext());
    }


    public void setRestrictionType(SessionContext ctx, ComposedType type)
    {
        (new Object(this, "restrictedType", type))
                        .set(null);
    }


    public void setRestrictionType(ComposedType type)
    {
        setRestrictionType(getSession().getSessionContext(), type);
    }


    public Principal getPrincipal(SessionContext ctx)
    {
        return impl().getPrincipal();
    }


    public Principal getPrincipal()
    {
        return getPrincipal(getSession().getSessionContext());
    }


    public void setPrincipal(SessionContext ctx, Principal p)
    {
        impl().setPrincipal(p);
    }


    public void setPrincipal(Principal p)
    {
        setPrincipal(getSession().getSessionContext(), p);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Set<ComposedType> getExcludedSubtypes()
    {
        ComposedType myType = getRestrictionType();
        Set<ComposedType> allSubtypes = myType.getAllSubTypes();
        if(allSubtypes.isEmpty())
        {
            return Collections.EMPTY_SET;
        }
        Map<Object, Object> params = new HashMap<>();
        params.put("code", getCode());
        params.put("subtypes", allSubtypes);
        params.put("active", Boolean.TRUE);
        List<ComposedType> matches = FlexibleSearch.getInstance().search("SELECT {restrictedType} FROM {SearchRestriction*} WHERE {code}=?code AND {restrictedType} IN ( ?subtypes ) AND {active} = ?active ", params, ComposedType.class).getResult();
        return matches.isEmpty() ? Collections.EMPTY_SET : new HashSet<>(matches);
    }


    public String toString()
    {
        if(getImplementation() == null)
        {
            return super.toString();
        }
        return "SearchRestriction( '" + getCode() + "' : " + getPrincipal().getUID() + " + " + getRestrictionType().getCode() + " = " +
                        getQuery() + " )";
    }


    public String exportXMLDefinition(XMLOutputter xout)
    {
        return "";
    }


    protected void checkRestrictionQuery(ComposedType restrictedType, String query) throws JaloInvalidParameterException
    {
        if(restrictedType == null)
        {
            throw new JaloInvalidParameterException("restricted type cannot be NULL", 0);
        }
        if(query == null)
        {
            throw new JaloInvalidParameterException("query cannot be NULL", 0);
        }
        if("".equals(query.trim()))
        {
            throw new JaloInvalidParameterException("query cannot be empty", 0);
        }
        try
        {
            getSession().getFlexibleSearch().checkQuery("SELECT {item:" + Item.PK + "} FROM {" + restrictedType
                            .getCode() + " AS item} WHERE " + query, true);
        }
        catch(FlexibleSearchException e)
        {
            throw new JaloInvalidParameterException(e, e.getErrorCode());
        }
    }


    public static void checkUniqueCodeTypePrincipal(Principal principal, ComposedType type, String code)
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("type", type);
        params.put("code", code.toLowerCase());
        params.put("principal", principal);
        String searchRestrictionCode = TypeManager.getInstance().getComposedType(SearchRestriction.class).getCode();
        List rows = FlexibleSearch.getInstance().search("SELECT {" + Item.PK + "} FROM {" + searchRestrictionCode + "} WHERE {restrictedType}=?type AND LOWER( {code} ) = ?code AND {principal}=?principal ", params, Collections.singletonList(SearchRestriction.class), true, true, 0, -1).getResult();
        if(!rows.isEmpty())
        {
            throw new JaloInvalidParameterException("SearchRestriction not unique: code '" + code + "' type '" + type.getCode() + "' principal '" + principal
                            .getPK() + "' is not unique", 0);
        }
    }
}
