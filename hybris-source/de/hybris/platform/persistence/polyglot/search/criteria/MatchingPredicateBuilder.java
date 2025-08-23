package de.hybris.platform.persistence.polyglot.search.criteria;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.persistence.polyglot.PolyglotPersistence;
import de.hybris.platform.persistence.polyglot.model.Identity;
import de.hybris.platform.persistence.polyglot.model.Key;
import de.hybris.platform.persistence.polyglot.model.Reference;
import de.hybris.platform.persistence.polyglot.model.SingleAttributeKey;
import de.hybris.platform.persistence.polyglot.view.ItemStateView;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import org.apache.commons.collections4.comparators.NullComparator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class MatchingPredicateBuilder implements ConditionVisitor
{
    private static final SingleAttributeKey typeKey = PolyglotPersistence.getNonlocalizedKey(Item.TYPE);
    private final Deque<Predicate<ItemStateView>> predicates = new ArrayDeque<>();
    private final Set<Identity> typeIds;
    private final Condition condition;
    private final Map<String, Object> params;


    public MatchingPredicateBuilder(Criteria criteria)
    {
        this(criteria.getTypeIdentitySet(), criteria.getCondition(), criteria.getParams());
    }


    public MatchingPredicateBuilder(Set<Identity> typeIds, Condition condition, Map<String, Object> params)
    {
        this.typeIds = typeIds;
        this.condition = condition;
        this.params = params;
    }


    public Predicate<ItemStateView> getPredicate()
    {
        this.predicates.clear();
        this.condition.visit(this);
        Predicate<ItemStateView> matchingType = s -> this.typeIds.contains(((Reference)s.get((Key)typeKey)).getIdentity());
        if(this.predicates.isEmpty())
        {
            return matchingType;
        }
        return matchingType.and(this.predicates.getFirst());
    }


    public void accept(Conditions.LogicalAndCondition condition)
    {
        int childCount = condition.getChildCount();
        Predicate<ItemStateView> composedPredicate = s -> true;
        for(int i = 0; i < childCount; i++)
        {
            composedPredicate = composedPredicate.and(this.predicates.pop());
        }
        this.predicates.push(composedPredicate);
    }


    public void accept(Conditions.LogicalOrCondition condition)
    {
        int childCount = condition.getChildCount();
        Predicate<ItemStateView> composedPredicate = s -> false;
        for(int i = 0; i < childCount; i++)
        {
            composedPredicate = composedPredicate.or(this.predicates.pop());
        }
        this.predicates.push(composedPredicate);
    }


    public void accept(Conditions.ComparisonCondition condition)
    {
        Objects.requireNonNull(this.params);
        Object valueToCompare = condition.getParamNameToCompare().map(this.params::get).orElse(null);
        Predicate<ItemStateView> p = getComparingPredicate(valueToCompare, condition);
        this.predicates.push(p);
    }


    private Predicate<ItemStateView> getComparingPredicate(Object valueToCompare, Conditions.ComparisonCondition condition)
    {
        if(valueToCompare == null)
        {
            return withNullComparison(condition);
        }
        if(valueToCompare instanceof Identity)
        {
            return withIdentityComparison(condition, (Identity)valueToCompare);
        }
        if(valueToCompare instanceof Reference)
        {
            return withIdentityComparison(condition, ((Reference)valueToCompare).getIdentity());
        }
        if(valueToCompare instanceof Comparable)
        {
            return withComparableComparison(condition, (Comparable)valueToCompare);
        }
        if(condition.getOperator() == Conditions.ComparisonCondition.CmpOperator.EQUAL && (valueToCompare instanceof de.hybris.platform.persistence.polyglot.model.SerializableValue || valueToCompare instanceof Map))
        {
            return withEquality(condition, valueToCompare);
        }
        throw new UnsupportedOperationException("Value: '" + valueToCompare + "' of type: " + valueToCompare
                        .getClass() + " is not supported.");
    }


    private Predicate<ItemStateView> withEquality(Conditions.ComparisonCondition condition, Object valueToCompare)
    {
        return s -> {
            Object value = s.get((Key)condition.getKey());
            return Objects.equals(value, valueToCompare);
        };
    }


    private Predicate<ItemStateView> withComparableComparison(Conditions.ComparisonCondition condition, Comparable valueToCompare)
    {
        return s -> {
            int cmpResult;
            Object value = s.get((Key)condition.getKey());
            if(value == null)
            {
                cmpResult = (new NullComparator(false)).compare(value, valueToCompare);
            }
            else if(value instanceof Reference)
            {
                Identity valueFromReference = ((Reference)value).getIdentity();
                cmpResult = safeCompare((Comparable)valueFromReference, valueToCompare);
            }
            else
            {
                cmpResult = safeCompare((Comparable)value, valueToCompare);
            }
            return condition.getOperator().matchesCompareResult(cmpResult);
        };
    }


    private <T> int safeCompare(Comparable<T> originalValue, Comparable valueToCompare)
    {
        try
        {
            return compare(originalValue, valueToCompare);
        }
        catch(ClassCastException e)
        {
            throw new PolyglotCannotCastSearchArgumentException(e);
        }
    }


    private <T> int compare(Comparable<T> originalValue, Comparable<?> valueToCompare)
    {
        if(originalValue instanceof Character && valueToCompare instanceof CharSequence && valueToCompare.toString()
                        .length() == 1)
        {
            return originalValue.toString().compareTo(valueToCompare.toString());
        }
        if(originalValue instanceof Number && valueToCompare instanceof Number)
        {
            if(originalValue.getClass().equals(valueToCompare.getClass()))
            {
                return originalValue.compareTo((T)valueToCompare);
            }
            return Double.compare(((Number)originalValue).doubleValue(), ((Number)valueToCompare).doubleValue());
        }
        if(originalValue instanceof Identity && ((Identity)originalValue).toLongValue() > 0L && ((valueToCompare instanceof String &&
                        StringUtils.isNumeric(valueToCompare.toString())) || valueToCompare instanceof Long))
        {
            return Long.compare(((Identity)originalValue).toLongValue(), toLongValue(valueToCompare));
        }
        return originalValue.compareTo((T)valueToCompare);
    }


    private long toLongValue(Comparable<?> value)
    {
        if(value instanceof Long)
        {
            return ((Long)value).longValue();
        }
        return NumberUtils.toLong(value.toString());
    }


    private Predicate<ItemStateView> withIdentityComparison(Conditions.ComparisonCondition condition, Identity valueToCompare)
    {
        return s -> {
            Identity value = asIdentity(s.get((Key)condition.getKey()));
            int cmpResult = Long.valueOf(value.toLongValue()).compareTo(Long.valueOf(valueToCompare.toLongValue()));
            return condition.getOperator().matchesCompareResult(cmpResult);
        };
    }


    private Identity asIdentity(Object object)
    {
        if(object instanceof Identity)
        {
            return (Identity)object;
        }
        if(object instanceof Reference)
        {
            return ((Reference)object).getIdentity();
        }
        return PolyglotPersistence.unknownIdentity();
    }


    private Predicate<ItemStateView> withNullComparison(Conditions.ComparisonCondition condition)
    {
        NullComparator cmp = new NullComparator(false);
        return s -> {
            Object value = s.get((Key)condition.getKey());
            return condition.getOperator().matchesCompareResult(cmp.compare(value, null));
        };
    }
}
