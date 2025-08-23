package com.hybris.backoffice.solrsearch.core.search;

import com.hybris.backoffice.widgets.fulltextsearch.FullTextSearchStrategy;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import java.util.Collections;
import java.util.Map;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SolrSearchFilterValidationStrategyTest
{
    private static final String TYPE_CODE = "typeCode";
    private static final String FILTER_NAME = "filterName";
    private static final String LOCALIZED_FILTER_NAME = "localizedFilterName";
    @Spy
    @InjectMocks
    private SolrSearchFilterValidationStrategy testSubject;
    @Mock
    private FullTextSearchStrategy solrSearchStrategy;
    @Mock
    private TypeFacade typeFacade;
    @Mock
    private Object value;


    @Before
    public void setUp()
    {
        Mockito.when(this.typeFacade.getType(this.value)).thenReturn(Object.class.getName());
        ((SolrSearchFilterValidationStrategy)Mockito.doReturn(Boolean.valueOf(true)).when(this.testSubject)).isLocalizedProperty("typeCode", "localizedFilterName");
    }


    @Test
    public void shouldReturnInvalidWhenThereIsNoIndexedProperties()
    {
        Mockito.when(this.solrSearchStrategy.getFieldType("typeCode", "filterName")).thenReturn(null);
        boolean isValid = this.testSubject.isValid("typeCode", "filterName", this.value);
        Assertions.assertThat(isValid).isFalse();
    }


    @Test
    public void shouldReturnInvalidWhenFilterTypeDoesNotMatchValueType()
    {
        Mockito.when(this.solrSearchStrategy.getFieldType("typeCode", "filterName")).thenReturn("SomeOtherType");
        boolean isValid = this.testSubject.isValid("typeCode", "filterName", this.value);
        Assertions.assertThat(isValid).isFalse();
    }


    @Test
    public void shouldReturnValidWhenFilterTypeMatchesValueType()
    {
        Mockito.when(this.solrSearchStrategy.getFieldType("typeCode", "filterName")).thenReturn(Object.class.getName());
        boolean isValid = this.testSubject.isValid("typeCode", "filterName", this.value);
        Assertions.assertThat(isValid).isTrue();
    }


    @Test
    public void shouldReturnInvalidWhenFilterTypeIsLocalizedAndFilterValueIsNotLocalized()
    {
        Mockito.when(Boolean.valueOf(this.solrSearchStrategy.isLocalized("typeCode", "filterName"))).thenReturn(Boolean.valueOf(true));
        boolean isValid = this.testSubject.isValid("typeCode", "filterName", this.value);
        Assertions.assertThat(isValid).isFalse();
    }


    @Test
    public void shouldReturnInvalidWhenFilterTypeIsNotLocalizedAndFilterValueIsLocalized()
    {
        Mockito.when(Boolean.valueOf(this.solrSearchStrategy.isLocalized("typeCode", "filterName"))).thenReturn(Boolean.valueOf(false));
        boolean isValid = this.testSubject.isValid("typeCode", "filterName", provideLocalizedValue());
        Assertions.assertThat(isValid).isFalse();
    }


    @Test
    public void shouldReturnTrueForNonLocalizedFieldWithNull()
    {
        boolean valid = this.testSubject.isValid("typeCode", "filterName", null, ValueComparisonOperator.IS_EMPTY);
        Assertions.assertThat(valid).isTrue();
    }


    @Test
    public void shouldReturnTrueForLocalizedFieldWithEmptyMap()
    {
        boolean valid = this.testSubject.isValid("typeCode", "localizedFilterName", Collections.emptyMap(), ValueComparisonOperator.IS_EMPTY);
        Assertions.assertThat(valid).isTrue();
    }


    private Object provideLocalizedValue()
    {
        return Mockito.mock(Map.class);
    }
}
