package de.hybris.platform.persistence.flexiblesearch;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

class TypeJoin extends ParsedText
{
    static final String ON = "ON";
    private final String operator;
    private final boolean optional;
    private List conditions;
    private ParsedType type;


    TypeJoin(JoinExpression join, String joinedTypeText, String operator, boolean optional)
    {
        super((ParsedText)join, joinedTypeText);
        this.operator = operator;
        this.optional = optional;
    }


    JoinExpression getJoin()
    {
        return (JoinExpression)getEnclosingText();
    }


    boolean isOptional()
    {
        return this.optional;
    }


    String getOperator()
    {
        return this.operator;
    }


    boolean hasConditions()
    {
        return (this.conditions != null && !this.conditions.isEmpty());
    }


    List getConditions()
    {
        return (this.conditions != null) ? this.conditions : Collections.EMPTY_LIST;
    }


    protected void translate() throws FlexibleSearchException
    {
        if(this.type == null)
        {
            String typeJoinText = getSource();
            int onIdx = getWholeWordTokenPosition(typeJoinText.toUpperCase(LocaleHelper.getPersistenceLocale()), "ON");
            String[] typeData = ParsedType.splitTypeExpression((onIdx >= 0) ? typeJoinText.substring(0, onIdx).trim() : typeJoinText.trim());
            setType(typeData[0], "exact"
                            .equals(typeData[1]), "alltypes"
                            .equals(typeData[1]), typeData[2], "deploymenttypes"
                            .equals(typeData[1]));
            if(onIdx >= 0)
            {
                setConditions(FlexibleSearchTools.split(typeJoinText.substring(onIdx + "ON".length()).trim(), "AND", true));
            }
        }
        else
        {
            String LEFT_JOIN = " LEFT JOIN ";
            StringBuilder stringBuilder = new StringBuilder();
            this.type.translate();
            stringBuilder.append(isOptional() ? " LEFT JOIN " : " JOIN ").append(this.type.getTranslated());
            setBuffer(stringBuilder);
        }
    }


    private void setConditions(List conditionStrings) throws FlexibleSearchException
    {
        if(this.conditions == null)
        {
            this.conditions = new LinkedList();
        }
        for(Iterator<String> it = conditionStrings.iterator(); it.hasNext(); )
        {
            String conditionString = it.next();
            JoinCondition cond = new JoinCondition(conditionString, this);
            cond.translate();
            this.conditions.add(cond);
        }
    }


    private void setType(String typeCode, boolean noSubtypes, boolean disableTypeCheck, String alias, boolean excludeSubtypesWithOwnDeployment) throws FlexibleSearchException
    {
        if(this.type != null)
        {
            throw new IllegalStateException("type was already set");
        }
        this.type = new ParsedType(this, typeCode, alias, noSubtypes, disableTypeCheck, excludeSubtypesWithOwnDeployment);
        getJoin().getFrom().registerType(this.type);
        this.type.translate();
    }


    protected ParsedText translateNested(int resultInsertPos, String selectedText) throws FlexibleSearchException
    {
        throw new IllegalStateException("ParsedType doesnt have nested texts");
    }
}
