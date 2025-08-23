package de.hybris.platform.persistence.polyglot.search.criteria;

import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractToStringVisitor implements ConditionVisitor
{
    private final Deque<String> fragments = new ArrayDeque<>();
    private final String andDelimeter;
    private final String orDelimeter;
    private final String compositePrefix;
    private final String compositeSuffix;


    protected AbstractToStringVisitor(String andDelimeter, String orDelimeter, String compositePrefix, String compositeSuffix)
    {
        this.andDelimeter = Objects.<String>requireNonNull(andDelimeter, "andDelimeter mustn't be null.");
        this.orDelimeter = Objects.<String>requireNonNull(orDelimeter, "orDelimeter mustn't be null.");
        this.compositePrefix = Objects.<String>requireNonNull(compositePrefix, "compositePrefix mustn't be null.");
        this.compositeSuffix = Objects.<String>requireNonNull(compositeSuffix, "compositeSuffix mustn't be null.");
    }


    protected AbstractToStringVisitor()
    {
        this(" and ", " or ", "(", ")");
    }


    public void accept(Conditions.LogicalOrCondition condition)
    {
        compose((Conditions.CompositeCondition)condition, this.orDelimeter);
    }


    public String getString()
    {
        Preconditions.checkState((this.fragments.size() < 2), "Expected at most one frgment but was: %s", this.fragments.size());
        return this.fragments.isEmpty() ? "" : this.fragments.getFirst();
    }


    public void accept(Conditions.LogicalAndCondition condition)
    {
        compose((Conditions.CompositeCondition)condition, this.andDelimeter);
    }


    public void accept(Conditions.ComparisonCondition condition)
    {
        this.fragments.push(asString(condition));
    }


    protected void compose(Conditions.CompositeCondition condition, String delimiter)
    {
        int childCount = condition.getChildCount();
        String[] childFragments = new String[childCount];
        for(int i = childCount - 1; i >= 0; i--)
        {
            childFragments[i] = this.fragments.pop();
        }
        String newFragment = Stream.<CharSequence>of((CharSequence[])childFragments).collect(Collectors.joining(delimiter, this.compositePrefix, this.compositeSuffix));
        this.fragments.push(newFragment);
    }


    protected abstract String asString(Conditions.ComparisonCondition paramComparisonCondition);
}
