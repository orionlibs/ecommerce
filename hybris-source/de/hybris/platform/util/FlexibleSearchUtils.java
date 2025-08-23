package de.hybris.platform.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class FlexibleSearchUtils
{
    private static final int ORACLE_EXPRESSION_MAX = 1000;


    public static String buildOracleCompatibleCollectionStatement(String expression, String parameter, String expressionOperator, Collection<?> originalParameters, Map<String, Object> paramsMap)
    {
        if(!Config.isOracleUsed() || originalParameters == null || originalParameters.size() <= 1000)
        {
            paramsMap.put(parameter, originalParameters);
            return expression;
        }
        StringBuilder sb = new StringBuilder();
        int s = originalParameters.size();
        List copyList = new ArrayList(originalParameters);
        sb.append('(');
        for(int i = 0; i < s; i += 1000)
        {
            if(i > 0)
            {
                sb.append(' ').append(expressionOperator).append(' ');
            }
            int end = Math.min(i + 1000, s);
            int paramIndex = i / 1000;
            sb.append(" ( ");
            sb.append(expression.replaceAll("(\\?" + parameter + ")", "$1" + paramIndex));
            sb.append(" ) ");
            paramsMap.put(parameter + parameter, copyList.subList(i, end));
        }
        sb.append(')');
        return sb.toString();
    }
}
