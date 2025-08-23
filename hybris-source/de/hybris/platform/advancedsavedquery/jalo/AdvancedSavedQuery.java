package de.hybris.platform.advancedsavedquery.jalo;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class AdvancedSavedQuery extends GeneratedAdvancedSavedQuery
{
    private static final Logger LOG = Logger.getLogger(AdvancedSavedQuery.class.getName());
    private static final String WHERE = "where".intern();


    @ForceJALO(reason = "abstract method implementation")
    public String getGeneratedFlexibleSearch(SessionContext ctx)
    {
        return getGeneratedFlexibleSearch(ctx, null);
    }


    public String getGeneratedFlexibleSearch(List<SearchParameterContainer> queryParameterList)
    {
        return getGeneratedFlexibleSearch(getSession().getSessionContext(), queryParameterList);
    }


    public String getGeneratedFlexibleSearch(List<SearchParameterContainer> queryParameterList, String orderBy, String sortDirection)
    {
        return getGeneratedFlexibleSearch(getSession().getSessionContext(), queryParameterList, orderBy, sortDirection);
    }


    public String getGeneratedFlexibleSearch(SessionContext ctx, List<SearchParameterContainer> queryParameterList)
    {
        return getGeneratedFlexibleSearch(ctx, queryParameterList, null, null);
    }


    public String getGeneratedFlexibleSearch(SessionContext ctx, List<SearchParameterContainer> queryParameterList, String orderBy, String sortDirection)
    {
        String query = getQuery();
        StringBuffer toReturn = new StringBuffer(query);
        Collection whereParts = getWhereparts(ctx);
        if(!whereParts.isEmpty())
        {
            for(Iterator<WherePart> it = whereParts.iterator(); it.hasNext(); )
            {
                WherePart wherePart = it.next();
                String replacePattern = wherePart.getReplacePattern();
                String where = wherePart.toFlexibleSearchForm(ctx, queryParameterList);
                int OFFSET = replacePattern.length();
                if(where != null && where.length() > 0)
                {
                    int j = -1;
                    for(j = toReturn.indexOf(replacePattern); j >= 0; j = toReturn.indexOf(replacePattern, j + OFFSET))
                    {
                        toReturn.replace(j, j + OFFSET, where);
                    }
                    LOG.debug("toReturn: " + toReturn);
                    continue;
                }
                int i = -1;
                for(i = toReturn.indexOf(replacePattern); i >= 0; i = toReturn.indexOf(replacePattern, i + OFFSET))
                {
                    String str = query.toLowerCase(LocaleHelper.getPersistenceLocale());
                    int pos2 = str.indexOf(WHERE);
                    if(pos2 != -1 && str.substring(pos2, str.length()).trim().equals(WHERE + " " + WHERE))
                    {
                        toReturn.delete(pos2, toReturn.length());
                    }
                    else
                    {
                        toReturn.replace(i, i + OFFSET, "1=1");
                    }
                }
            }
        }
        String temp = query.toLowerCase(LocaleHelper.getPersistenceLocale());
        int pos = temp.indexOf(WHERE);
        if(pos != -1 && temp.substring(pos, temp.length()).trim().equals(WHERE))
        {
            toReturn.delete(pos, toReturn.length());
        }
        if(orderBy != null)
        {
            String direction = sortDirection;
            if("genericsearch.orderby.asc".equals(sortDirection))
            {
                direction = "ASC";
            }
            else if("genericsearch.orderby.desc".equals(direction))
            {
                direction = "DESC";
            }
            toReturn.append(" ORDER BY {" + orderBy + "} " + direction);
        }
        return toReturn.toString();
    }


    @ForceJALO(reason = "something else")
    public Collection getWhereparts(SessionContext ctx)
    {
        FlexibleSearch flexibleSearch = FlexibleSearch.getInstance();
        TypeManager typeManager = TypeManager.getInstance();
        String sql = "SELECT {" + Item.PK + "} FROM {" + typeManager.getComposedType(WherePart.class).getCode() + "} WHERE {savedQuery} = ?query";
        SearchResult searchResult = flexibleSearch.search(sql, Collections.singletonMap("query", this),
                        Collections.singletonList(WherePart.class), true, true, 0, -1);
        return searchResult.getResult();
    }


    private void removeWhereParts(SessionContext ctx, Collection coll) throws ConsistencyCheckException
    {
        for(Iterator<Item> it = coll.iterator(); it.hasNext(); )
        {
            ((Item)it.next()).remove();
        }
    }


    @ForceJALO(reason = "something else")
    public void setWhereparts(SessionContext ctx, Collection<?> value)
    {
        Collection<WherePart> coll = new ArrayList<>(getWhereparts(ctx));
        coll.removeAll(value);
        try
        {
            removeWhereParts(ctx, coll);
        }
        catch(ConsistencyCheckException cce)
        {
            LOG.debug("Failed to remove dynamic parameters", (Throwable)cce);
        }
        for(Iterator<?> it = value.iterator(); it.hasNext(); )
        {
            ((WherePart)it.next()).setSavedQuery(this);
        }
    }


    public WherePart getWherePart(String pattern)
    {
        return getWherePart(getSession().getSessionContext(), pattern);
    }


    public WherePart getWherePart(SessionContext ctx, String pattern)
    {
        FlexibleSearch flexibleSearch = FlexibleSearch.getInstance();
        TypeManager typeManager = TypeManager.getInstance();
        String sql = "SELECT {" + Item.PK + "} FROM {" + typeManager.getComposedType(WherePart.class).getCode() + "} WHERE {savedQuery} = ?query AND {replacePattern} = ?pattern";
        Map<String, Object> values = new HashMap<>();
        values.put("query", this);
        values.put("pattern", pattern);
        SearchResult searchResult = flexibleSearch.search(sql, values, Collections.singletonList(WherePart.class), true, true, 0, -1);
        if(searchResult.getCount() == 0)
        {
            return null;
        }
        return searchResult.getResult().iterator().next();
    }


    @Deprecated(since = "ages", forRemoval = false)
    @ForceJALO(reason = "something else")
    public Map getParams()
    {
        Map<String, Type> map = new HashMap<>();
        Collection wps = getWhereparts();
        for(Iterator<WherePart> it = wps.iterator(); it.hasNext(); )
        {
            WherePart wherePart = it.next();
            for(Iterator<AbstractAdvancedSavedQuerySearchParameter> params = wherePart.getDynamicParams().iterator(); params.hasNext(); )
            {
                AbstractAdvancedSavedQuerySearchParameter asq = params.next();
                Type type = asq.getValueType();
                try
                {
                    map.put((String)asq.getAttribute("name"), type);
                }
                catch(JaloInvalidParameterException e)
                {
                    e.printStackTrace();
                }
                catch(JaloSecurityException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }
}
