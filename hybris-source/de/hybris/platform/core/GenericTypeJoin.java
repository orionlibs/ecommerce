package de.hybris.platform.core;

import java.util.Map;

public class GenericTypeJoin extends FlexibleSearchTranslatable
{
    private GenericCondition joinCondition;
    private boolean typeExclusive = false;
    private boolean outerJoin;
    private final String typeCode;
    private final String alias;


    protected GenericTypeJoin(String typeCode, String alias, boolean outerJoin)
    {
        if(typeCode == null)
        {
            throw new NullPointerException("typecode was null");
        }
        this.typeCode = typeCode;
        this.alias = alias;
        setOuterJoin(outerJoin);
    }


    public void setTypeExclusive(boolean exclusive)
    {
        this.typeExclusive = exclusive;
    }


    public boolean isTypeExclusive()
    {
        return this.typeExclusive;
    }


    public GenericCondition getJoinCondition()
    {
        return this.joinCondition;
    }


    public void setJoinCondition(GenericCondition joinCondition)
    {
        this.joinCondition = joinCondition;
    }


    public void addCondition(GenericCondition condition)
    {
        if(getJoinCondition() == null)
        {
            setJoinCondition(condition);
        }
        else
        {
            if(!(getJoinCondition() instanceof GenericConditionList))
            {
                setJoinCondition((GenericCondition)new GenericConditionList(new GenericCondition[] {getJoinCondition()}));
            }
            ((GenericConditionList)getJoinCondition()).addToConditionList(condition);
        }
    }


    public boolean isOuterJoin()
    {
        return this.outerJoin;
    }


    public void setOuterJoin(boolean outerJoin)
    {
        this.outerJoin = outerJoin;
    }


    public String getJoinedTypeCode()
    {
        return this.typeCode;
    }


    public String getJoinedTypeAlias()
    {
        return this.alias;
    }


    public void toFlexibleSearch(StringBuilder queryBuffer, Map<String, String> aliasTypeMap, Map<String, Object> valueMap)
    {
        GenericQuery.TypeIdentifierMap idMap = (GenericQuery.TypeIdentifierMap)aliasTypeMap;
        String myAlias = idMap.registerJoinedTypeIdentifier((getJoinedTypeAlias() != null) ? getJoinedTypeAlias() :
                        getJoinedTypeCode());
        if(!isOuterJoin())
        {
            queryBuffer.append(" JOIN ");
        }
        else
        {
            String LEFT_JOIN = " LEFT JOIN ";
            queryBuffer.append(" LEFT JOIN ");
        }
        queryBuffer.append(getJoinedTypeCode());
        if(isTypeExclusive())
        {
            queryBuffer.append("!");
        }
        queryBuffer.append(" AS ").append(myAlias).append(" ");
        if(getJoinCondition() != null)
        {
            queryBuffer.append(" ON ");
            getJoinCondition().toFlexibleSearch(queryBuffer, aliasTypeMap, valueMap);
        }
    }
}
