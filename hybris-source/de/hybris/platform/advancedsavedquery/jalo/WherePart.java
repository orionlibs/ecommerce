package de.hybris.platform.advancedsavedquery.jalo;

import de.hybris.platform.advancedsavedquery.constants.GeneratedASQConstants;
import de.hybris.platform.core.Operator;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.TypeManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;

public class WherePart extends GeneratedWherePart
{
    private static final Logger LOG = Logger.getLogger(WherePart.class.getName());


    @ForceJALO(reason = "something else")
    public Collection<AbstractAdvancedSavedQuerySearchParameter> getDynamicParams(SessionContext ctx)
    {
        FlexibleSearch flexibleSearch = FlexibleSearch.getInstance();
        TypeManager typeManager = TypeManager.getInstance();
        String sql = "SELECT {" + Item.PK + "} FROM {" + typeManager.getComposedType(AbstractAdvancedSavedQuerySearchParameter.class).getCode() + "} WHERE {wherePart} = ?wherepart";
        SearchResult searchResult = flexibleSearch.search(ctx, sql, Collections.singletonMap("wherepart", this),
                        Collections.singletonList(AbstractAdvancedSavedQuerySearchParameter.class), true, true, 0, -1);
        return searchResult.getResult();
    }


    @ForceJALO(reason = "something else")
    public void setDynamicParams(SessionContext ctx, Collection<AbstractAdvancedSavedQuerySearchParameter> value)
    {
        Collection<AbstractAdvancedSavedQuerySearchParameter> coll = new ArrayList<>(getDynamicParams(ctx));
        coll.removeAll(value);
        try
        {
            removeDynamicParams(ctx, coll);
        }
        catch(ConsistencyCheckException cce)
        {
            LOG.debug("Failed to remove dynamic parameters", (Throwable)cce);
        }
        for(Iterator<AbstractAdvancedSavedQuerySearchParameter> it = value.iterator(); it.hasNext(); )
        {
            ((AbstractAdvancedSavedQuerySearchParameter)it.next()).setWherePart(ctx, this);
        }
    }


    public String toFlexibleSearchForm(SessionContext ctx)
    {
        Collection<AbstractAdvancedSavedQuerySearchParameter> coll = getDynamicParams(ctx);
        StringBuffer buffer = new StringBuffer();
        for(Iterator<AbstractAdvancedSavedQuerySearchParameter> it = coll.iterator(); it.hasNext(); )
        {
            AbstractAdvancedSavedQuerySearchParameter asq = it.next();
            fillBuffer(ctx, buffer, asq, null);
        }
        return buffer.toString();
    }


    private void fillBuffer(SessionContext ctx, StringBuffer buffer, AbstractAdvancedSavedQuerySearchParameter asq, Operator operator)
    {
        if(buffer.length() > 0)
        {
            buffer.append(isAndAsPrimitive() ? " AND " : " OR ");
        }
        buffer.append((operator == null) ? asq.toFlexibleSearchForm(ctx) : asq.toFlexibleSearchForm(ctx, operator));
        buffer.append(' ');
    }


    public String toFlexibleSearchForm(SessionContext ctx, List<AdvancedSavedQuery.SearchParameterContainer> parameters)
    {
        if(parameters == null)
        {
            return toFlexibleSearchForm(ctx);
        }
        StringBuffer buffer = new StringBuffer();
        for(AdvancedSavedQuery.SearchParameterContainer container : parameters)
        {
            if(isAddParameter(ctx, container))
            {
                fillBuffer(ctx, buffer, container.getQueryParameter(), container.getOperator());
            }
        }
        return buffer.toString();
    }


    private boolean isAddParameter(SessionContext ctx, AdvancedSavedQuery.SearchParameterContainer container)
    {
        if(container == null)
        {
            return true;
        }
        Operator operator = container.getOperator();
        if(operator.equals(Operator.IS_NOT_NULL) || operator.equals(Operator.IS_NULL))
        {
            return true;
        }
        Object value = container.getValue();
        String emptyHandling = container.getQueryParameter().getEmptyHandling(ctx).getCode();
        boolean toReturn = false;
        if(GeneratedASQConstants.Enumerations.EmptyParamEnum.ASITIS.equals(emptyHandling))
        {
            toReturn = true;
        }
        else if(GeneratedASQConstants.Enumerations.EmptyParamEnum.IGNORE.equals(emptyHandling))
        {
            if(value instanceof String)
            {
                toReturn = (((String)value).length() > 0);
            }
            else
            {
                toReturn = (value != null);
            }
        }
        else if(GeneratedASQConstants.Enumerations.EmptyParamEnum.TRIMANDIGNORE.equals(emptyHandling))
        {
            if(value instanceof String)
            {
                toReturn = (((String)value).trim().length() > 0);
            }
            else
            {
                toReturn = (value != null);
            }
        }
        return toReturn;
    }


    private void removeDynamicParams(SessionContext ctx, Collection<AbstractAdvancedSavedQuerySearchParameter> coll) throws ConsistencyCheckException
    {
        for(Iterator<AbstractAdvancedSavedQuerySearchParameter> it = coll.iterator(); it.hasNext(); )
        {
            ((Item)it.next()).remove();
        }
    }


    public String toString()
    {
        if(getImplementation() == null)
        {
            return super.toString();
        }
        return "(" + getSavedQuery().getCode() + ")" + getReplacePattern();
    }
}
