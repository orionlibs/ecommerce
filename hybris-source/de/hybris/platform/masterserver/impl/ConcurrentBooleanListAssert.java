package de.hybris.platform.masterserver.impl;

import java.util.List;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class ConcurrentBooleanListAssert extends AbstractAssert<ConcurrentBooleanListAssert, List<Boolean>>
{
    public ConcurrentBooleanListAssert(List<Boolean> actual)
    {
        super(actual, ConcurrentBooleanListAssert.class);
    }


    public static ConcurrentBooleanListAssert assertThat(List<Boolean> actual)
    {
        return new ConcurrentBooleanListAssert(actual);
    }


    public ConcurrentBooleanListAssert hasNumberOfTrueElements(int elementsCount)
    {
        int trueElementsCount = 0;
        for(int i = 0; i < ((List)this.actual).size(); i++)
        {
            if(Boolean.TRUE.equals(((List)this.actual).get(i)))
            {
                trueElementsCount++;
            }
        }
        Assertions.assertThat(trueElementsCount).isEqualTo(elementsCount);
        return this;
    }


    public ConcurrentBooleanListAssert hasNumberOfFalseElements(int elementsCount)
    {
        int falseElementsCount = 0;
        for(int i = 0; i < ((List)this.actual).size(); i++)
        {
            if(Boolean.FALSE.equals(((List)this.actual).get(i)))
            {
                falseElementsCount++;
            }
        }
        Assertions.assertThat(falseElementsCount).isEqualTo(elementsCount);
        return this;
    }
}
