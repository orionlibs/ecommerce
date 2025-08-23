package de.hybris.platform.core;

import de.hybris.platform.util.ItemPropertyValue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GenericQuery extends FlexibleSearchTranslatable
{
    private static final String ERROR_CONDITION_EMPTY = "Condition cannot be NULL.";
    private static final String ERROR_TYPECODE_EMPTY = "StartTypeCode cannot be cannot be NULL or EMPTY.";
    private static final String ERROR_ORDERTYPE_NOTSET = "Field in OrderBy references Type which is neighter the startType nor a joined Type";
    private static final String ERROR_SELECTFIELD_EMPTY = "SelectField cannot be cannot be NULL.";
    private String initialTypeCode;
    private String initialTypeAlias;
    private GenericCondition genericCondition;
    private List<GenericSelectField> selectFields;
    private Collection<GenericSearchOrderBy> orderByList;
    private Collection<GenericTypeJoin> typeJoins;
    private boolean typeExclusive = false;


    public GenericQuery(String typeCode, GenericCondition condition, boolean typeExclusive)
    {
        if(condition != null)
        {
            validateAndSetCondition(condition);
        }
        validateAndSetInitialTypeCode(typeCode);
        this.typeExclusive = typeExclusive;
    }


    public GenericQuery(String typeCode, GenericCondition condition)
    {
        this(typeCode, condition, false);
    }


    public GenericQuery(String typeCode)
    {
        this(typeCode, null);
    }


    public GenericQuery(String typeCode, boolean typeExclusive)
    {
        this(typeCode, null, typeExclusive);
    }


    public String getInitialTypeCode()
    {
        return this.initialTypeCode;
    }


    protected void setInitialTypeCode(String typeCode)
    {
        validateAndSetInitialTypeCode(typeCode);
    }


    private void validateAndSetInitialTypeCode(String typeCode)
    {
        if(typeCode == null || "".equals(typeCode))
        {
            throw new IllegalArgumentException("StartTypeCode cannot be cannot be NULL or EMPTY.");
        }
        this.initialTypeCode = typeCode;
    }


    public void setInitialTypeAlias(String alias)
    {
        this.initialTypeAlias = alias;
    }


    public boolean isTypeExclusive()
    {
        return this.typeExclusive;
    }


    public void setTypeExclusive(boolean exclusive)
    {
        this.typeExclusive = exclusive;
    }


    public GenericQuery addCondition(GenericCondition condition)
    {
        if(getCondition() == null)
        {
            setCondition(condition);
        }
        else
        {
            if(!(getCondition() instanceof GenericConditionList))
            {
                setCondition((GenericCondition)new GenericConditionList(new GenericCondition[] {getCondition()}));
            }
            ((GenericConditionList)getCondition()).addToConditionList(condition);
        }
        return this;
    }


    public GenericQuery addConditions(GenericCondition... conditions)
    {
        for(GenericCondition condition2 : conditions)
        {
            addCondition(condition2);
        }
        return this;
    }


    public GenericCondition getCondition()
    {
        return this.genericCondition;
    }


    protected void setCondition(GenericCondition genericCondition)
    {
        validateAndSetCondition(genericCondition);
    }


    private void validateAndSetCondition(GenericCondition genericCondition)
    {
        if(genericCondition == null)
        {
            throw new IllegalArgumentException("Condition cannot be NULL.");
        }
        this.genericCondition = genericCondition;
    }


    public Collection<GenericSearchOrderBy> getOrderByList()
    {
        return (this.orderByList != null) ? Collections.<GenericSearchOrderBy>unmodifiableCollection(this.orderByList) : Collections.<GenericSearchOrderBy>emptyList();
    }


    public GenericQuery addOrderBy(GenericSearchOrderBy orderBy)
    {
        if(orderBy != null && !getOrderByList().contains(orderBy))
        {
            if(this.orderByList == null)
            {
                this.orderByList = new ArrayList<>();
            }
            this.orderByList.add(orderBy);
        }
        return this;
    }


    private boolean hasOrderBy()
    {
        return (this.orderByList != null && !this.orderByList.isEmpty());
    }


    private boolean hasConditions()
    {
        return ((getCondition() != null && !(getCondition() instanceof GenericConditionList)) || (
                        getCondition() instanceof GenericConditionList && !((GenericConditionList)getCondition()).isEmpty()));
    }


    private GenericTypeJoin createAndAddJoin(String typeCode, String alias, boolean outerJoin, boolean typeExclusive)
    {
        GenericTypeJoin join = new GenericTypeJoin(typeCode, alias, outerJoin);
        join.setTypeExclusive(typeExclusive);
        if(this.typeJoins == null)
        {
            this.typeJoins = new ArrayList<>();
        }
        this.typeJoins.add(join);
        return join;
    }


    public GenericTypeJoin addOuterJoin(String typeCode, String alias, GenericCondition joinCondition)
    {
        return addOuterJoin(typeCode, alias, joinCondition, false);
    }


    public GenericTypeJoin addOuterJoin(String typeCode, GenericCondition joinCondition)
    {
        return addOuterJoin(typeCode, null, joinCondition, false);
    }


    public GenericTypeJoin addOuterJoin(String typeCode, String alias, GenericCondition joinCondition, boolean isTypeExclusive)
    {
        GenericTypeJoin ret = createAndAddJoin(typeCode, alias, true, isTypeExclusive);
        ret.setJoinCondition(joinCondition);
        return ret;
    }


    public GenericTypeJoin addOuterJoin(String typeCode, GenericCondition joinCondition, boolean isTypeExclusive)
    {
        GenericTypeJoin ret = createAndAddJoin(typeCode, null, true, isTypeExclusive);
        ret.setJoinCondition(joinCondition);
        return ret;
    }


    public GenericTypeJoin addInnerJoin(String typeCode, String alias, GenericCondition joinCondition)
    {
        return addInnerJoin(typeCode, alias, joinCondition, false);
    }


    public GenericTypeJoin addInnerJoin(String typeCode, GenericCondition joinCondition)
    {
        return addInnerJoin(typeCode, null, joinCondition, false);
    }


    public GenericTypeJoin addInnerJoin(String typeCode, String alias, GenericCondition joinCondition, boolean isTypeExclusive)
    {
        GenericTypeJoin ret = createAndAddJoin(typeCode, alias, false, isTypeExclusive);
        ret.setJoinCondition(joinCondition);
        return ret;
    }


    public GenericTypeJoin addInnerJoin(String typeCode, GenericCondition joinCondition, boolean isTypeExclusive)
    {
        GenericTypeJoin ret = createAndAddJoin(typeCode, null, false, isTypeExclusive);
        ret.setJoinCondition(joinCondition);
        return ret;
    }


    public Collection<GenericTypeJoin> getTypeJoinList()
    {
        return (this.typeJoins != null) ? Collections.<GenericTypeJoin>unmodifiableCollection(this.typeJoins) : Collections.<GenericTypeJoin>emptyList();
    }


    public GenericQuery addSubQuery(GenericSearchField field, Operator operator, String subqueryTypeCode)
    {
        GenericQuery sub = new GenericQuery(subqueryTypeCode);
        addCondition((GenericCondition)new GenericSubQueryCondition(field, operator, sub));
        return sub;
    }


    public GenericQuery addSubQuery(String fieldQualifier, Operator operator, String subqueryTypeCode)
    {
        GenericQuery sub = new GenericQuery(subqueryTypeCode);
        addCondition((GenericCondition)new GenericSubQueryCondition(new GenericSearchField(fieldQualifier), operator, sub));
        return sub;
    }


    public GenericQuery addSubQuery(Operator operator, String subqueryTypeCode)
    {
        GenericQuery sub = new GenericQuery(subqueryTypeCode);
        addCondition((GenericCondition)new GenericSubQueryCondition(null, operator, sub));
        return sub;
    }


    public GenericQuery addSelectField(GenericSelectField field)
    {
        if(field == null)
        {
            throw new IllegalArgumentException("SelectField cannot be cannot be NULL.");
        }
        if(this.selectFields == null)
        {
            this.selectFields = new ArrayList<>();
        }
        this.selectFields.add(field);
        return this;
    }


    public List<Class> getResultClasses()
    {
        List<Class<?>> resultClasses = new ArrayList<>();
        for(GenericSelectField select : getSelectFields())
        {
            if(select.getReturnClass() == null)
            {
                throw new IllegalArgumentException("select field " + select + " without return class - may only be omitted in subqueries");
            }
            resultClasses.add(select.getReturnClass());
        }
        return resultClasses;
    }


    public List<GenericSelectField> getSelectFields()
    {
        if(this.selectFields == null || this.selectFields.isEmpty())
        {
            return Collections.singletonList(new GenericSelectField("PK", ItemPropertyValue.class));
        }
        return this.selectFields;
    }


    public String toFlexibleSearch(Map<String, Object> valueMap)
    {
        StringBuilder stringBuilder = new StringBuilder();
        toFlexibleSearch(stringBuilder, null, valueMap);
        return stringBuilder.toString();
    }


    public String toPolyglotSearch(Map<String, Object> valueMap)
    {
        StringBuilder stringBuilder = new StringBuilder();
        toPolyglotSearch(stringBuilder, null, valueMap);
        return stringBuilder.toString();
    }


    public void toFlexibleSearch(StringBuilder queryBuffer, Map<String, String> aliasTypeMap, Map<String, Object> valueMap)
    {
        TypeIdentifierMap idMap = new TypeIdentifierMap(aliasTypeMap);
        String myAlias = idMap.registerInitialTypeIdentifier((this.initialTypeAlias != null) ? this.initialTypeAlias : this.initialTypeCode);
        queryBuffer.append("SELECT ");
        for(Iterator<GenericSelectField> it = getSelectFields().iterator(); it.hasNext(); )
        {
            ((GenericSelectField)it.next()).toFlexibleSearch(queryBuffer, (Map)idMap, valueMap);
            if(it.hasNext())
            {
                queryBuffer.append(",").append(" ");
            }
        }
        queryBuffer.append(" FROM ");
        queryBuffer.append("{");
        queryBuffer.append(getInitialTypeCode());
        if(isTypeExclusive())
        {
            queryBuffer.append("!");
        }
        queryBuffer.append(" AS ").append(myAlias).append(" ");
        for(Iterator<GenericTypeJoin> joinIt = getTypeJoinList().iterator(); joinIt.hasNext(); )
        {
            ((GenericTypeJoin)joinIt.next()).toFlexibleSearch(queryBuffer, (Map)idMap, valueMap);
            queryBuffer.append(" ");
        }
        queryBuffer.append("}");
        if(hasConditions())
        {
            queryBuffer.append(" WHERE ");
            this.genericCondition.toFlexibleSearch(queryBuffer, (Map)idMap, valueMap);
        }
        if(hasOrderBy())
        {
            createOrderBy(queryBuffer, valueMap, idMap);
        }
    }


    private void createOrderBy(StringBuilder queryBuffer, Map<String, Object> valueMap, TypeIdentifierMap idMap)
    {
        queryBuffer.append(" ORDER BY ");
        for(Iterator<GenericSearchOrderBy> it = getOrderByList().iterator(); it.hasNext(); )
        {
            GenericSearchOrderBy searchOrderBy = it.next();
            if(!idMap.containsKey(searchOrderBy.getField().getTypeIdentifier()))
            {
                throw new IllegalArgumentException("Field in OrderBy references Type which is neighter the startType nor a joined Type:" + searchOrderBy.getField());
            }
            searchOrderBy.toFlexibleSearch(queryBuffer, (Map)idMap, valueMap);
            if(it.hasNext())
            {
                queryBuffer.append(",").append(" ");
            }
        }
    }


    public void toPolyglotSearch(StringBuilder queryBuffer, Map<String, String> aliasTypeMap, Map<String, Object> valueMap)
    {
        TypeIdentifierMap idMap = new TypeIdentifierMap(aliasTypeMap);
        idMap.registerInitialTypeIdentifier((this.initialTypeAlias != null) ? this.initialTypeAlias : this.initialTypeCode);
        queryBuffer.append("GET ");
        queryBuffer.append("{");
        queryBuffer.append(getInitialTypeCode());
        queryBuffer.append("}");
        if(hasConditions())
        {
            queryBuffer.append(" WHERE ");
            this.genericCondition.toPolyglotSearch(queryBuffer, (Map)idMap, valueMap);
        }
        if(hasOrderBy())
        {
            queryBuffer.append(" ORDER BY ");
            Iterator<GenericSearchOrderBy> iterator = getOrderByList().iterator();
            while(iterator.hasNext())
            {
                GenericSearchOrderBy searchOrderBy = iterator.next();
                if(!idMap.containsKey(searchOrderBy.getField().getTypeIdentifier()))
                {
                    throw new IllegalArgumentException("Field in OrderBy references Type which is neighter the startType nor a joined Type:" + searchOrderBy.getField());
                }
                searchOrderBy.toPolyglotSearch(queryBuffer, (Map)idMap, valueMap);
                if(iterator.hasNext())
                {
                    queryBuffer.append(", ");
                }
            }
        }
    }


    public boolean isTranslatableToPolyglotDialect()
    {
        return (!hasJoinList() && !hasPolyglotUnsupportedSelectFields() && !hasPolyglotUntranslatableCondition() &&
                        !hasPolyglotUnsupportedOrderBy());
    }


    private boolean hasPolyglotUnsupportedOrderBy()
    {
        for(GenericSearchOrderBy order : getOrderByList())
        {
            String qualifier = order.getField().getQualifier();
            if(qualifier != null && qualifier.contains("/"))
            {
                return true;
            }
        }
        return false;
    }


    private boolean hasPolyglotUntranslatableCondition()
    {
        GenericCondition condition = getCondition();
        return (condition != null && !condition.isTranslatableToPolyglotDialect());
    }


    private boolean hasPolyglotUnsupportedSelectFields()
    {
        return (getSelectFields().size() > 1 || (
                        getSelectFields().size() == 1 && !"PK".equals(((GenericSelectField)getSelectFields().get(0)).getQualifier())));
    }


    private boolean hasJoinList()
    {
        Collection<GenericTypeJoin> joinList = getTypeJoinList();
        return (joinList != null && !joinList.isEmpty());
    }
}
