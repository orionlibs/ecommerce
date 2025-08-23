package de.hybris.platform.jalo.flexiblesearch;

import de.hybris.platform.jalo.flexiblesearch.hints.Hint;
import de.hybris.platform.persistence.flexiblesearch.polyglot.PolyglotPersistenceFlexibleSearchSupport;
import de.hybris.platform.persistence.polyglot.search.criteria.Criteria;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class QueryOptions
{
    static final int DEFAULT_START_VALUE = 0;
    static final int DEFAULT_COUNT_VALUE = -1;
    static final boolean DEFAULT_NEED_TOTAL_VALUE = false;
    private final String query;
    private final Map<String, Object> values;
    private final List<Attribute> resultAttributes;
    private final boolean failOnUnknownFields;
    private final boolean dontNeedTotal;
    private final boolean typeExclusive;
    private final int start;
    private final int count;
    private final List<Hint> hints;
    private final Optional<Criteria> polyglotCriteria;
    private final boolean startFromPolyglot;


    private QueryOptions(Builder builder)
    {
        this.query = builder.query;
        this.values = builder.values;
        this.failOnUnknownFields = builder.failOnUnknownFields;
        this.dontNeedTotal = builder.dontNeedTotal;
        this.start = builder.start;
        this.count = builder.count;
        this.hints = builder.hints;
        this.resultAttributes = builder.resultAttributes;
        this.typeExclusive = builder.typeExclusive;
        this.polyglotCriteria = PolyglotPersistenceFlexibleSearchSupport.tryToConvertToPolyglotCriteria(this);
        this.startFromPolyglot = builder.startFromPolyglot;
    }


    public boolean isTypeExclusive()
    {
        return this.typeExclusive;
    }


    public String getQuery()
    {
        return this.query;
    }


    public Map<String, Object> getValues()
    {
        return (this.values == null) ? Collections.<String, Object>emptyMap() : Collections.<String, Object>unmodifiableMap(this.values);
    }


    public int getValuesCount()
    {
        return (this.values == null) ? 0 : this.values.size();
    }


    public List<Class<?>> getResultClasses()
    {
        if(this.resultAttributes == null)
        {
            return Collections.emptyList();
        }
        List<Class<?>> resultClasses = (List<Class<?>>)this.resultAttributes.stream().map(Attribute::getClazz).collect(Collectors.toCollection(java.util.ArrayList::new));
        return Collections.unmodifiableList(resultClasses);
    }


    public List<Attribute> getResultAttributes()
    {
        return (this.resultAttributes == null) ? Collections.<Attribute>emptyList() : Collections.<Attribute>unmodifiableList(this.resultAttributes);
    }


    public boolean isFailOnUnknownFields()
    {
        return this.failOnUnknownFields;
    }


    public boolean isDontNeedTotal()
    {
        return this.dontNeedTotal;
    }


    boolean isStartFromPolyglot()
    {
        return this.startFromPolyglot;
    }


    public int getStart()
    {
        return this.start;
    }


    public int getCount()
    {
        return this.count;
    }


    public List<Hint> getHints()
    {
        return (this.hints == null) ? Collections.<Hint>emptyList() : Collections.<Hint>unmodifiableList(this.hints);
    }


    public static Builder newBuilder()
    {
        return new Builder();
    }


    public static Builder newBuilderFromTemplate(QueryOptions template)
    {
        return newBuilder()
                        .withQuery(template.query)
                        .withValues(template.values)
                        .withFailOnUnknownFields(template.failOnUnknownFields)
                        .withDontNeedTotal(template.dontNeedTotal)
                        .withStart(template.start)
                        .withCount(template.count)
                        .withHints(template.hints)
                        .withResultAttributes(template.resultAttributes)
                        .withTypeExclusive(template.typeExclusive);
    }


    public Criteria getPolyglotQueryCriteria()
    {
        requirePolyglotDialect();
        return this.polyglotCriteria.get();
    }


    public boolean isPolyglotDialectQuery()
    {
        return this.polyglotCriteria.isPresent();
    }


    public QueryOptions toFlexibleSearchDialect()
    {
        if(!isPolyglotDialectQuery())
        {
            return this;
        }
        String fsQuery = PolyglotPersistenceFlexibleSearchSupport.convertToFlexibleSearchQueryString(getPolyglotQueryCriteria());
        return newBuilderFromTemplate(this).withQuery(fsQuery).withStartFromPolyglot(true).build();
    }


    private void requirePolyglotDialect()
    {
        if(!isPolyglotDialectQuery())
        {
            new IllegalStateException("This query isn't a polyglot one.");
        }
    }
}
