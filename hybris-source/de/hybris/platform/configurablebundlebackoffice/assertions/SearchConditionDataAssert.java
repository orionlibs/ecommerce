package de.hybris.platform.configurablebundlebackoffice.assertions;

import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import java.util.Objects;
import org.assertj.core.api.AbstractAssert;

public class SearchConditionDataAssert extends AbstractAssert<SearchConditionDataAssert, SearchConditionData>
{
    public SearchConditionDataAssert(SearchConditionData actual)
    {
        super(actual, SearchConditionDataAssert.class);
    }


    public static SearchConditionDataAssert assertThat(SearchConditionData actual)
    {
        return new SearchConditionDataAssert(actual);
    }


    public SearchConditionDataAssert hasOperator(ValueComparisonOperator operator)
    {
        isNotNull();
        if(!Objects.equals(((SearchConditionData)this.actual).getOperator(), operator))
        {
            failWithMessage("Expected operator to be <%s> but was <%s>", new Object[] {operator, ((SearchConditionData)this.actual).getOperator()});
        }
        return this;
    }


    public SearchConditionDataAssert hasField(String fieldName)
    {
        isNotNull();
        if(null == ((SearchConditionData)this.actual).getFieldType())
        {
            failWithMessage("Expected field name to be not null", new Object[0]);
        }
        else if(!Objects.equals(((SearchConditionData)this.actual).getFieldType().getName(), fieldName))
        {
            failWithMessage("Expected field name to be <%s> but was <%s>", new Object[] {fieldName, ((SearchConditionData)this.actual).getFieldType().getName()});
        }
        return this;
    }


    public SearchConditionDataAssert hasValue(Object value)
    {
        isNotNull();
        if(!Objects.equals(((SearchConditionData)this.actual).getValue(), value))
        {
            failWithMessage("Expected value to be <%s> but was <%s>", new Object[] {value, ((SearchConditionData)this.actual).getValue()});
        }
        return this;
    }


    public SearchConditionDataAssert hasEmptyValue()
    {
        isNotNull();
        if(Objects.nonNull(((SearchConditionData)this.actual).getValue()))
        {
            failWithMessage("Expected value to be null but was <%s>", new Object[] {((SearchConditionData)this.actual).getValue()});
        }
        return this;
    }
}
