package de.hybris.platform.persistence.flexiblesearch;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import java.util.ArrayList;
import java.util.List;

class JoinExpression extends ParsedText
{
    static final String JOIN = "JOIN";
    static final String LEFT = "LEFT";
    private final List<TypeJoin> typeJoins;
    private ParsedType startType;


    JoinExpression(FromClause from, String source)
    {
        super((ParsedText)from, source);
        this.typeJoins = new ArrayList<>(4);
    }


    FromClause getFrom()
    {
        return (FromClause)getEnclosingText();
    }


    protected void translate() throws FlexibleSearchException
    {
        if(this.startType == null)
        {
            String typeOrTypeJoin = getSource();
            String typeOrTypeJoinUPPER = typeOrTypeJoin.toUpperCase(LocaleHelper.getPersistenceLocale());
            int joinIdx = getWholeWordTokenPosition(typeOrTypeJoinUPPER, "JOIN");
            int leftIdx = getWholeWordTokenPosition(typeOrTypeJoinUPPER, "LEFT");
            boolean optional = (leftIdx >= 0 && leftIdx < joinIdx);
            String firstType = typeOrTypeJoin.substring(0, optional ? leftIdx : joinIdx).trim();
            String[] typeData = ParsedType.splitTypeExpression(firstType);
            setStartType(typeData[0], "exact"
                            .equals(typeData[1]), "alltypes"
                            .equals(typeData[1]), typeData[2], "deploymenttypes"
                            .equals(typeData[1]));
            do
            {
                int nextJoinIdx = getWholeWordTokenPosition(typeOrTypeJoinUPPER, "JOIN", joinIdx + "JOIN".length());
                leftIdx = getWholeWordTokenPosition(typeOrTypeJoinUPPER, "LEFT", joinIdx + "JOIN".length());
                String LEFT_JOIN = "LEFT JOIN";
                createJoinedType((nextJoinIdx >= 0) ?
                                typeOrTypeJoin.substring(joinIdx + "JOIN".length(), (
                                                leftIdx >= 0 && leftIdx < nextJoinIdx) ? leftIdx : nextJoinIdx) :
                                typeOrTypeJoin.substring(joinIdx + "JOIN".length()), optional ? "LEFT JOIN" : "JOIN", optional);
                if(nextJoinIdx >= 0)
                {
                    optional = (leftIdx >= 0 && leftIdx < nextJoinIdx);
                }
                joinIdx = nextJoinIdx;
            }
            while(joinIdx >= 0);
        }
        else
        {
            StringBuilder stringBuilder = new StringBuilder();
            getStartType().translate();
            stringBuilder.append(getStartType().getTranslated());
            for(TypeJoin tj : this.typeJoins)
            {
                tj.translate();
                stringBuilder.append(tj.getTranslated());
            }
            setBuffer(stringBuilder);
        }
    }


    private void createJoinedType(String joinedTypeClause, String operator, boolean optional) throws FlexibleSearchException
    {
        TypeJoin typeJoin = new TypeJoin(this, joinedTypeClause, operator, optional);
        this.typeJoins.add(typeJoin);
        typeJoin.translate();
    }


    private ParsedType getStartType()
    {
        return this.startType;
    }


    private void setStartType(String typeCode, boolean noSubtypes, boolean disableTypeCheck, String alias, boolean excludeSubtypesWithOwnDeployment) throws FlexibleSearchException
    {
        if(this.startType != null)
        {
            throw new IllegalStateException("start type already set");
        }
        this.startType = new ParsedType(getFrom(), typeCode, alias, noSubtypes, disableTypeCheck, excludeSubtypesWithOwnDeployment);
        getFrom().registerType(this.startType);
        this.startType.translate();
    }


    protected ParsedText translateNested(int resultInsertPos, String selectedText) throws FlexibleSearchException
    {
        throw new IllegalStateException("TypeJoin desnt have nested texts");
    }
}
