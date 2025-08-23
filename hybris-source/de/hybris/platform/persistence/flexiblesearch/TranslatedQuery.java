package de.hybris.platform.persistence.flexiblesearch;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.util.Config;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.apache.log4j.Logger;

public class TranslatedQuery implements Serializable
{
    private static final Logger LOG = Logger.getLogger(TranslatedQuery.class.getName());
    private static final String DB_PARAMS_LIMIT = "db.supported.inline.in.params.";
    private final String sqlTemplate;
    private final List valueMappings;
    private final List valuePositions;
    private final boolean needLanguageFlag;
    private final boolean hasLanguageMapping;
    private final Set<PK> typePKs;
    private final Map<Integer, Object> fixedParameters;
    private final boolean cacheStatement;


    TranslatedQuery(String sql, List<Object> valueMappings, List<Integer> valuePositions, boolean needLanguage, Set<PK> typePKs, Map<Integer, Object> fixed)
    {
        this.sqlTemplate = sql.trim();
        this.valueMappings = (valueMappings != null) ? valueMappings : Collections.emptyList();
        Objects.requireNonNull(ZERO);
        this.hasLanguageMapping = this.valueMappings.stream().anyMatch(ZERO::equals);
        this.valuePositions = (valuePositions != null) ? valuePositions : Collections.emptyList();
        this.typePKs = typePKs;
        this.needLanguageFlag = needLanguage;
        this.fixedParameters = fixed;
        this.cacheStatement = true;
    }


    public List getValuePositions()
    {
        return (this.valuePositions != null) ? new ArrayList(this.valuePositions) : Collections.EMPTY_LIST;
    }


    private TranslatedQuery(String sql, List<Object> valueMappings, List<Integer> valuePositions, boolean needLanguage, Set<PK> typePKs, Map<Integer, Object> fixed, boolean cloned)
    {
        this.sqlTemplate = sql;
        this.valueMappings = (valueMappings != null) ? valueMappings : Collections.emptyList();
        Objects.requireNonNull(ZERO);
        this.hasLanguageMapping = this.valueMappings.stream().anyMatch(ZERO::equals);
        this.valuePositions = (valuePositions != null) ? valuePositions : Collections.emptyList();
        this.typePKs = typePKs;
        this.needLanguageFlag = needLanguage;
        this.fixedParameters = fixed;
        this.cacheStatement = true;
    }


    public TranslatedQuery(String sql, List<Object> valueMappings, List<Integer> valuePositions, boolean needLanguage, Set<PK> typePKs, Map<Integer, Object> fixed, boolean cloned, boolean cacheStatement)
    {
        this.sqlTemplate = sql;
        this.valueMappings = (valueMappings != null) ? valueMappings : Collections.emptyList();
        Objects.requireNonNull(ZERO);
        this.hasLanguageMapping = this.valueMappings.stream().anyMatch(ZERO::equals);
        this.valuePositions = (valuePositions != null) ? valuePositions : Collections.emptyList();
        this.typePKs = typePKs;
        this.needLanguageFlag = needLanguage;
        this.fixedParameters = fixed;
        this.cacheStatement = cacheStatement;
    }


    public Object clone()
    {
        return new TranslatedQuery(this.sqlTemplate, this.valueMappings, this.valuePositions, this.needLanguageFlag, this.typePKs, this.fixedParameters, true, this.cacheStatement);
    }


    public boolean cacheStatement()
    {
        return this.cacheStatement;
    }


    public String getSQLTemplate()
    {
        return this.sqlTemplate;
    }


    public Set<PK> getTypePKs()
    {
        return this.typePKs;
    }


    public List getValueKeys()
    {
        return this.valueMappings;
    }


    public boolean needLanguage()
    {
        return this.needLanguageFlag;
    }


    public boolean hasLanguageMapping()
    {
        return this.hasLanguageMapping;
    }


    private static final Integer ZERO = Integer.valueOf(0);


    public final Map removeUnusedValues(Map _values) throws FlexibleSearchException
    {
        Map values = getAllValues(_values);
        Map<Object, Object> ret = Collections.EMPTY_MAP;
        if(!this.valueMappings.isEmpty())
        {
            int size = this.valueMappings.size();
            if(size == 1)
            {
                Object valueKey = this.valueMappings.get(0);
                if(ZERO.equals(valueKey))
                {
                    ret = Collections.EMPTY_MAP;
                }
                else
                {
                    Object value = values.get(valueKey);
                    if(value == null)
                    {
                        throw new FlexibleSearchException(null, "missing value for " + valueKey + ", got " + values, 0);
                    }
                    ret = Collections.singletonMap(valueKey, value);
                }
            }
            else
            {
                ret = new HashMap<>(size);
                Set<Object> missing = null;
                for(Object valueKey : this.valueMappings)
                {
                    if(!ZERO.equals(valueKey))
                    {
                        Object value = values.get(valueKey);
                        if(value == null)
                        {
                            if(missing == null)
                            {
                                missing = new HashSet(5);
                            }
                            missing.add(valueKey);
                            continue;
                        }
                        ret.put(valueKey, value);
                    }
                }
                if(missing != null)
                {
                    throw new FlexibleSearchException(null, "missing values for " + missing + ", got " + values, 0);
                }
            }
        }
        return ret;
    }


    public final Map getPositionMap(Map values)
    {
        Map<Object, Object> ret = new HashMap<>();
        for(Iterator it = this.valueMappings.iterator(), it2 = this.valuePositions.iterator(); it.hasNext(); )
        {
            Object valueKey = it.next();
            if(ZERO.equals(valueKey))
            {
                continue;
            }
            List list = (List)ret.get(valueKey);
            if(list == null)
            {
                ret.put(valueKey, list = new ArrayList());
            }
            list.add(it2.next());
        }
        return ret;
    }


    protected Map getAllValues(Map callerValues)
    {
        Map ret;
        if(this.fixedParameters != null && !this.fixedParameters.isEmpty())
        {
            CaseInsensitiveParameterMap<Integer, Object> caseInsensitiveParameterMap = new CaseInsensitiveParameterMap(callerValues);
            caseInsensitiveParameterMap.putAll(this.fixedParameters);
        }
        else
        {
            ret = callerValues;
        }
        return ret;
    }


    public final ExecutableQuery expandValues(Map _values, PK defaultLanguagePK)
    {
        if(this.needLanguageFlag && this.hasLanguageMapping && defaultLanguagePK == null)
        {
            throw new FlexibleSearchException(null, "you must provide a language for this query " + this, 0);
        }
        String query = this.sqlTemplate;
        List<Object> expandedValues = Collections.EMPTY_LIST;
        if(!this.valueMappings.isEmpty() || this.needLanguageFlag)
        {
            List<ValueMapper> mappings = createValueMappers(_values, defaultLanguagePK);
            expandedValues = new ArrayList(mappings.size());
            int offset = 0;
            StringBuilder execQuery = new StringBuilder(this.sqlTemplate);
            for(ValueMapper mapping : mappings)
            {
                int pos = mapping.getPosition();
                if(mapping.isInlined())
                {
                    String inlinedValue = mapping.getInlinedValue();
                    execQuery.replace(pos + offset, pos + offset + 1, inlinedValue);
                    offset += inlinedValue.length() - 1;
                    continue;
                }
                if(mapping.mustExpand())
                {
                    StringBuilder stingBuilder = new StringBuilder();
                    int index = 0;
                    for(Object v : mapping.getRealValues())
                    {
                        if(index > 0)
                        {
                            stingBuilder.append(',');
                        }
                        index++;
                        expandedValues.add(v);
                        stingBuilder.append('?');
                    }
                    execQuery.replace(pos + offset, pos + offset + 1, stingBuilder.toString());
                    offset += stingBuilder.length() - 1;
                    continue;
                }
                expandedValues.add(mapping.getRealValue());
            }
            query = execQuery.toString();
        }
        return new ExecutableQuery(query, expandedValues);
    }


    protected Collection<String> getMissingParameters(Collection<ValueMapper> mappings)
    {
        Collection<String> ret = null;
        for(ValueMapper vm : mappings)
        {
            if(vm.isMissing())
            {
                if(ret == null)
                {
                    ret = new LinkedHashSet<>();
                }
                ret.add(String.valueOf(vm.getValueKey()));
            }
        }
        return (ret != null) ? ret : Collections.EMPTY_SET;
    }


    protected List<ValueMapper> createValueMappers(Map userValues, PK defaultLanguagePK) throws FlexibleSearchException
    {
        Map values = getAllValues(userValues);
        List<ValueMapper> ret = new ArrayList<>(this.needLanguageFlag ? (this.valueMappings.size() + 1) : this.valueMappings.size());
        SqlParamsInliner inliner = getSqlParamsInliner(Config.getDatabaseName());
        int valueCount = 0;
        Iterator it;
        Iterator<Integer> it2;
        for(it = this.valueMappings.iterator(), it2 = this.valuePositions.iterator(); it.hasNext(); )
        {
            ValueMapper valueMapper = new ValueMapper(it.next(), ((Integer)it2.next()).intValue(), inliner);
            valueMapper.loadValue(values, defaultLanguagePK);
            ret.add(valueMapper);
            valueCount += valueMapper.getValueCount();
        }
        Collection<String> missing = getMissingParameters(ret);
        if(!missing.isEmpty())
        {
            throw new FlexibleSearchException(null, "missing values for " + missing + " ( values provided " + userValues + " )", 0);
        }
        inlineParamsIfNecessary(ret, valueCount);
        return ret;
    }


    private void inlineParamsIfNecessary(List<ValueMapper> ret, int valueCount)
    {
        Config.DatabaseName databaseName = Config.getDatabaseName();
        int paramLimitCount = getParamsLimit(databaseName);
        if(paramLimitCount <= 0)
        {
            return;
        }
        if(valueCount > paramLimitCount)
        {
            int remaining = valueCount;
            for(ValueMapper vm : ret)
            {
                if(vm.setInline())
                {
                    remaining -= vm.getValueCount();
                }
            }
            if(remaining > paramLimitCount)
            {
                LOG.warn("got query " + this + " with > " + paramLimitCount + " parameters on " + databaseName
                                .getName() + " - this will most likely crash!");
            }
        }
    }


    private int getParamsLimit(Config.DatabaseName databaseName)
    {
        return Config.getInt("db.supported.inline.in.params." + databaseName.getName(), 0);
    }


    private SqlParamsInliner getSqlParamsInliner(Config.DatabaseName databaseName)
    {
        if(Config.DatabaseName.SQLSERVER == databaseName)
        {
            return (SqlParamsInliner)new MSSQLSqlParamsInliner();
        }
        return (SqlParamsInliner)new GenericSqlParamsInliner();
    }


    protected int countParameters(Map values)
    {
        int ret = 0;
        for(Object valueKey : this.valueMappings)
        {
            if(!ZERO.equals(valueKey))
            {
                Object value = values.get(valueKey);
                if(value != null)
                {
                    if(value instanceof Collection)
                    {
                        ret += ((Collection)value).size();
                        continue;
                    }
                    ret++;
                }
            }
        }
        return ret;
    }
}
