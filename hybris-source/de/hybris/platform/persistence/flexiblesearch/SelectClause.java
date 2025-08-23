package de.hybris.platform.persistence.flexiblesearch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class SelectClause extends FieldExpression
{
    private static final Pattern[] AGGREGATE_FKT = new Pattern[] {Pattern.compile("(^|\\s+)count\\s*\\("),
                    Pattern.compile("(^|\\s+)min\\s*\\("),
                    Pattern.compile("(^|\\s+)max\\s*\\("),
                    Pattern.compile("(^|\\s+)avg\\s*\\("),
                    Pattern.compile("(^|\\s+)sum\\s*\\(")};


    SelectClause(String select, FromClause from)
    {
        super(from, select);
    }


    String modifyForUnionSyntax(String unionTableAlias)
    {
        StringBuilder innerSelect = new StringBuilder();
        for(TableField field : getFields())
        {
            String alias = getAlias(field);
            if(alias == null)
            {
                alias = createUnionFieldAlias("selectArg", field);
                if(innerSelect.length() > 0)
                {
                    innerSelect.append(",");
                }
                innerSelect.append(field.getTranslated()).append(" AS ").append(alias);
            }
            replaceInTranslated(field, unionTableAlias + "." + unionTableAlias);
        }
        return innerSelect.toString();
    }


    public boolean containsAggregation()
    {
        String srcClean = getCleanSource().toLowerCase();
        for(Pattern p : AGGREGATE_FKT)
        {
            Matcher matcher = p.matcher(srcClean);
            while(matcher.find())
            {
                int start = matcher.start();
                if(start > 4)
                {
                    String preFix = srcClean.substring(0, start).trim();
                    if(!preFix.toLowerCase().endsWith(" as"))
                    {
                        return true;
                    }
                    continue;
                }
                return true;
            }
        }
        return false;
    }
}
