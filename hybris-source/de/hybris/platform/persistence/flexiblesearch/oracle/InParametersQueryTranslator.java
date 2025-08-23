package de.hybris.platform.persistence.flexiblesearch.oracle;

import com.google.common.base.Joiner;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.SessionParamTranslator;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.util.Config;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InParametersQueryTranslator
{
    private static final Logger LOG = LoggerFactory.getLogger(InParametersQueryTranslator.class);
    private static final String WITH_TABLE_PREFIX = "with_tbl_";
    private static final String DB_MAX_PARAMS = "db.supported.params.limit";
    private static final int PARAM_NAME_LIMIT = 17;
    private final int maxParametersLimit;
    final SessionParamTranslator sessionParamTranslator = new SessionParamTranslator();


    public InParametersQueryTranslator()
    {
        this.maxParametersLimit = Config.getInt("db.supported.params.limit", 1000);
    }


    public InParametersQueryTranslator(int maxParametersLimit)
    {
        this.maxParametersLimit = maxParametersLimit;
    }


    protected boolean isOracle()
    {
        return Config.isOracleUsed();
    }


    public boolean isAnyParamExceedingLimit(String query, Map queryParams)
    {
        return (isOracle() && findParamsExceedingInLimit(query, queryParams).size() > 0);
    }


    public List<ExceedingParameter> analyzeQuery(String qry, Map<?, ?> queryParams)
    {
        if(!isOracle())
        {
            return Collections.emptyList();
        }
        List<String> paramsExceedingInLimit = findParamsExceedingInLimit(qry, queryParams);
        Multimap<String, String> enclosingQueryFragments = findEnclosingQueryFragments(qry, paramsExceedingInLimit);
        Map<String, List<String>> qryFragmentToExtractedLiterals = extractLiterals(enclosingQueryFragments);
        Map<Object, Object> allQueryParamValues = new HashMap<>();
        if(queryParams != null)
        {
            allQueryParamValues.putAll(queryParams);
        }
        allQueryParamValues.putAll(extractSessionParameterValues(paramsExceedingInLimit));
        List<ExceedingParameter> result = new ArrayList<>();
        for(String paramExceeding : paramsExceedingInLimit)
        {
            int counter = 1;
            for(String enclosingQryFrag : enclosingQueryFragments.get(paramExceeding))
            {
                List<String> literals = qryFragmentToExtractedLiterals.get(enclosingQryFrag);
                result.add(new ExceedingParameter(paramExceeding, enclosingQryFrag, literals, allQueryParamValues
                                .get(paramExceeding),
                                getTableNameInCharacterLimit(paramExceeding, counter)));
                counter++;
            }
        }
        List<ExceedingParameter> deduplicatedResults = new ArrayList<>();
        for(ExceedingParameter param : result)
        {
            ExceedingParameter paramInList = findInList(param, deduplicatedResults);
            if(paramInList == null)
            {
                deduplicatedResults.add(param);
                continue;
            }
            paramInList.addQryFragments(param.getQryFragments());
        }
        return deduplicatedResults;
    }


    private String getTableNameInCharacterLimit(String paramName, int counter)
    {
        String columnName = (paramName.length() > 17) ? paramName.substring(0, 17) : paramName;
        return columnName + columnName;
    }


    private ExceedingParameter findInList(ExceedingParameter needle, List<ExceedingParameter> exceedingParams)
    {
        for(ExceedingParameter param : exceedingParams)
        {
            if(needle.getParam().equals(param.getParam()) &&
                            sameLiterals(param.getExtractedLiterals(), needle.getExtractedLiterals()))
            {
                return param;
            }
        }
        return null;
    }


    private boolean sameLiterals(List<String> coll1, List<String> coll2)
    {
        return (CollectionUtils.intersection(coll1, coll2).size() == coll1.size());
    }


    private Map<Object, Object> extractSessionParameterValues(List<String> paramsExceedingInLimit)
    {
        SessionContext sessionCtx = JaloSession.getCurrentSession().getSessionContext();
        HashedMap hashedMap = new HashedMap();
        this.sessionParamTranslator.translatePathValueKeys(sessionCtx, paramsExceedingInLimit, (Map)hashedMap);
        return (Map<Object, Object>)hashedMap;
    }


    private Map<String, List<String>> extractLiterals(Multimap<String, String> enclosingQueryFragments)
    {
        Map<String, List<String>> extractedLiterals = new HashMap<>();
        for(String parameter : enclosingQueryFragments.keySet())
        {
            for(String qryFragment : enclosingQueryFragments.get(parameter))
            {
                String qryInFragment = qryFragment.replaceAll("[()]", "");
                List<String> literals = new ArrayList<>();
                for(String token : qryInFragment.split(","))
                {
                    String trimmedToken = token.trim();
                    if(!trimmedToken.startsWith("?"))
                    {
                        literals.add(trimmedToken);
                    }
                }
                extractedLiterals.put(qryFragment, literals);
            }
        }
        return extractedLiterals;
    }


    private Multimap<String, String> findEnclosingQueryFragments(String qry, List<String> params)
    {
        ArrayListMultimap arrayListMultimap = ArrayListMultimap.create();
        for(String param : params)
        {
            if(countParameterOccurrences(qry, param) > 0)
            {
                List<Integer> paramIndexes = paramsStartIndexes(qry, param);
                for(Iterator<Integer> iterator = paramIndexes.iterator(); iterator.hasNext(); )
                {
                    int paramIndex = ((Integer)iterator.next()).intValue();
                    int fragmentStart = qry.lastIndexOf("(", paramIndex);
                    int fragmentEnd = qry.indexOf(")", paramIndex) + 1;
                    arrayListMultimap.put(param, qry.substring(fragmentStart, fragmentEnd));
                }
            }
        }
        return (Multimap<String, String>)arrayListMultimap;
    }


    private int countParameterOccurrences(String qry, String param)
    {
        Pattern pattern = matchSqlParamFollowedByComaWhitespaceOrBracketPattern(param);
        Matcher matcher = pattern.matcher(qry);
        int occurences = 0;
        while(matcher.find())
        {
            occurences++;
        }
        return occurences;
    }


    private List<Integer> paramsStartIndexes(String qry, String param)
    {
        Pattern pattern = matchSqlParamFollowedByComaWhitespaceOrBracketPattern(param);
        Matcher matcher = pattern.matcher(qry);
        List<Integer> indexes = new ArrayList<>();
        while(matcher.find())
        {
            indexes.add(Integer.valueOf(matcher.start()));
        }
        return indexes;
    }


    private Pattern matchSqlParamFollowedByComaWhitespaceOrBracketPattern(String param)
    {
        String reg = "\\?" + param + "(\\s|\\)|,)";
        return Pattern.compile(reg);
    }


    private List<String> findParamsExceedingInLimit(String query, Map queryParams)
    {
        List<String> exceedingParams = new ArrayList<>();
        if(queryParams != null)
        {
            for(Object param : queryParams.keySet())
            {
                Object value = queryParams.get(param);
                if(value instanceof Collection)
                {
                    Collection assignments = (Collection)value;
                    if(assignments.size() > this.maxParametersLimit)
                    {
                        exceedingParams.add((String)param);
                    }
                }
            }
        }
        List<String> sessionParams = this.sessionParamTranslator.extractSessionParamsFromQuery(query);
        if(!sessionParams.isEmpty())
        {
            Map<Object, Object> map = new HashMap<>();
            SessionContext session = JaloSession.getCurrentSession().getSessionContext();
            try
            {
                this.sessionParamTranslator.translatePathValueKeys(session, sessionParams, map);
            }
            catch(Exception e)
            {
                return Collections.emptyList();
            }
            for(Map.Entry<Object, Object> e : map.entrySet())
            {
                if(e.getValue() instanceof Collection)
                {
                    Collection col = (Collection)e.getValue();
                    if(col.size() > this.maxParametersLimit)
                    {
                        exceedingParams.add((String)e.getKey());
                    }
                }
            }
        }
        return exceedingParams;
    }


    public String generateWithClause(List<ExceedingParameter> exceedingParameters)
    {
        if(exceedingParameters.isEmpty())
        {
            return "";
        }
        List<String> withQueries = new ArrayList<>();
        for(ExceedingParameter param : exceedingParameters)
        {
            String withQry = generateWithClause(param);
            withQueries.add(withQry);
        }
        return "with " + Joiner.on(", ").join(withQueries);
    }


    private String generateWithClause(ExceedingParameter param)
    {
        String tableName = "with_tbl_" + param.getTableName();
        StringBuilder sb = new StringBuilder();
        if(param.getValue() != null && param.getValue() instanceof Collection)
        {
            Collection collection = (Collection)param.getValue();
            int counter = 1;
            for(Object value : collection)
            {
                String sqlValue;
                if(value instanceof AbstractItemModel)
                {
                    sqlValue = ((AbstractItemModel)value).getPk().getLongValueAsString();
                }
                else if(value instanceof Item)
                {
                    sqlValue = ((Item)value).getPK().getLongValueAsString();
                }
                else if(value instanceof PK)
                {
                    sqlValue = ((PK)value).getLongValueAsString();
                }
                else
                {
                    sqlValue = value.toString();
                }
                sb.append("select '").append(StringEscapeUtils.escapeSql(sqlValue)).append("' from dual");
                if(counter < collection.size() || !param.getExtractedLiterals().isEmpty())
                {
                    counter++;
                    sb.append(" UNION \n");
                }
            }
        }
        for(String literal : param.getExtractedLiterals())
        {
            sb.append("select ").append(literal).append(" from dual");
            if(param.getExtractedLiterals().get(param.getExtractedLiterals().size() - 1) != literal)
            {
                sb.append(" UNION \n");
            }
        }
        String values = sb.toString();
        if(StringUtils.isNotEmpty(values))
        {
            return tableName + " (id) as (" + tableName + ")";
        }
        return "";
    }
}
