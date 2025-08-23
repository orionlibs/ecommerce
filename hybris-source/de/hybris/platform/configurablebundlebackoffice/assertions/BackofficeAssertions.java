package de.hybris.platform.configurablebundlebackoffice.assertions;

import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import org.assertj.core.api.Assertions;

public class BackofficeAssertions extends Assertions
{
    public static SearchConditionDataAssert assertThat(SearchConditionData actual)
    {
        return SearchConditionDataAssert.assertThat(actual);
    }
}
