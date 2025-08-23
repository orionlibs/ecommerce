package de.hybris.platform.jalo.flexiblesearch.limit.impl;

import de.hybris.platform.jalo.flexiblesearch.limit.LimitStatementBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class LimitStatementBuilderAssert extends AbstractAssert<LimitStatementBuilderAssert, LimitStatementBuilder>
{
    public LimitStatementBuilderAssert(LimitStatementBuilder actual)
    {
        super(actual, LimitStatementBuilderAssert.class);
    }


    public static LimitStatementBuilderAssert assertThat(LimitStatementBuilder actual)
    {
        return new LimitStatementBuilderAssert(actual);
    }


    public LimitStatementBuilderAssert hasOriginalStartAndCountValues(Integer val1, Integer val2)
    {
        Assertions.assertThat(this.actual).isNotNull();
        Assertions.assertThat(((LimitStatementBuilder)this.actual).getOriginalStart()).isEqualTo(val1);
        Assertions.assertThat(((LimitStatementBuilder)this.actual).getOriginalCount()).isEqualTo(val2);
        return this;
    }


    public LimitStatementBuilderAssert hasAdditionalStatementValues(Integer... values)
    {
        List<Object> expected = new ArrayList();
        expected.add("foo");
        expected.add("bar");
        expected.addAll(Arrays.asList((Object[])values));
        Assertions.assertThat(this.actual).isNotNull();
        Assertions.assertThat(((LimitStatementBuilder)this.actual).getModifiedStatementValues()).hasSize(2 + values.length);
        Assertions.assertThat(((LimitStatementBuilder)this.actual).getModifiedStatementValues()).isEqualTo(expected);
        return this;
    }


    public LimitStatementBuilderAssert hasNoAdditionalStatementValues()
    {
        Assertions.assertThat(this.actual).isNotNull();
        Assertions.assertThat(((LimitStatementBuilder)this.actual).getModifiedStatementValues()).hasSize(2);
        return this;
    }
}
