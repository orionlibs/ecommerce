package com.hybris.backoffice.searchservices.providers.impl;

import com.hybris.backoffice.proxy.LabelServiceProxy;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.searchservices.core.SnException;
import de.hybris.platform.searchservices.core.service.SnExpressionEvaluator;
import de.hybris.platform.searchservices.core.service.SnQualifier;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ItemModelLabelSnIndexerValueProviderTest
{
    @Mock
    private SnIndexerContext indexerContext;
    @Mock
    private SnIndexerFieldWrapper fieldWrapper;
    @Mock
    private ItemModel source;
    @Mock
    private LabelServiceProxy labelServiceProxy;
    @InjectMocks
    private ItemModelLabelSnIndexerValueProvider provider;
    @Mock
    private SnExpressionEvaluator snExpressionEvaluator;


    @Test
    public void shouldGetEmptyWhenQualifiersNotExist() throws SnIndexerException, SnException
    {
        Object object = new Object();
        Mockito.when(this.snExpressionEvaluator.evaluate(this.source, "")).thenReturn(object);
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(Collections.emptyMap());
        Mockito.when(this.fieldWrapper.getQualifiers()).thenReturn(Collections.emptyList());
        Mockito.when(Boolean.valueOf(this.fieldWrapper.isLocalized())).thenReturn(Boolean.valueOf(true));
        Map<Locale, Object> localizedValue = (Map<Locale, Object>)this.provider.getFieldValue(this.indexerContext, this.fieldWrapper, this.source, null);
        Assertions.assertThat(localizedValue.isEmpty()).isEqualTo(true);
    }


    @Test
    public void shouldGetProductLabelWhenProviderParametersNotExist() throws SnIndexerException, SnException
    {
        Object object = new Object();
        String label = "productLabel";
        SnQualifier qualifier = (SnQualifier)Mockito.mock(SnQualifier.class);
        Locale locale = new Locale("en");
        List<SnQualifier> qualifiers = new ArrayList<>(Arrays.asList(new SnQualifier[] {qualifier}));
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(Collections.emptyMap());
        Mockito.when(this.snExpressionEvaluator.evaluate(this.source, "")).thenReturn(object);
        Mockito.when(this.fieldWrapper.getQualifiers()).thenReturn(qualifiers);
        Mockito.when(Boolean.valueOf(this.fieldWrapper.isLocalized())).thenReturn(Boolean.valueOf(true));
        Mockito.when(qualifier.getAs(Locale.class)).thenReturn(locale);
        Mockito.when(this.labelServiceProxy.getObjectLabel(object, locale)).thenReturn("productLabel");
        Map<Locale, Object> localizedValue = (Map<Locale, Object>)this.provider.getFieldValue(this.indexerContext, this.fieldWrapper, this.source, null);
        Assertions.assertThat(localizedValue.isEmpty()).isEqualTo(false);
        Assertions.assertThat(localizedValue.size()).isEqualTo(1);
        Assertions.assertThat(localizedValue.get(locale)).isEqualTo("productLabel");
    }


    @Test(expected = SnIndexerException.class)
    public void shouldCatchExceptionWhenGetFieldValueAndNotLocalized() throws SnException
    {
        Object object = new Object();
        String label = "productLabel";
        SnQualifier qualifier = (SnQualifier)Mockito.mock(SnQualifier.class);
        Locale locale = new Locale("en");
        List<SnQualifier> qualifiers = new ArrayList<>(Arrays.asList(new SnQualifier[] {qualifier}));
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(Collections.emptyMap());
        Mockito.when(this.snExpressionEvaluator.evaluate(this.source, "")).thenReturn(object);
        Mockito.lenient().when(this.fieldWrapper.getQualifiers()).thenReturn(qualifiers);
        Mockito.when(Boolean.valueOf(this.fieldWrapper.isLocalized())).thenReturn(Boolean.valueOf(false));
        Mockito.lenient().when(qualifier.getAs(Locale.class)).thenReturn(locale);
        Mockito.lenient().when(this.labelServiceProxy.getObjectLabel(object, locale)).thenReturn("productLabel");
        this.provider.getFieldValue(this.indexerContext, this.fieldWrapper, this.source, null);
    }


    @Test(expected = SnIndexerException.class)
    public void shouldCatchExceptionWhenGetFieldValueCatchSnException() throws SnException
    {
        Object object = new Object();
        String label = "productLabel";
        SnQualifier qualifier = (SnQualifier)Mockito.mock(SnQualifier.class);
        Locale locale = new Locale("en");
        List<SnQualifier> qualifiers = new ArrayList<>(Arrays.asList(new SnQualifier[] {qualifier}));
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(Collections.emptyMap());
        BDDMockito.given(this.snExpressionEvaluator.evaluate(this.source, "")).willThrow(new Throwable[] {(Throwable)new SnException()});
        Mockito.lenient().when(this.fieldWrapper.getQualifiers()).thenReturn(qualifiers);
        Mockito.lenient().when(Boolean.valueOf(this.fieldWrapper.isLocalized())).thenReturn(Boolean.valueOf(false));
        Mockito.lenient().when(qualifier.getAs(Locale.class)).thenReturn(locale);
        Mockito.lenient().when(this.labelServiceProxy.getObjectLabel(object, locale)).thenReturn("productLabel");
        this.provider.getFieldValue(this.indexerContext, this.fieldWrapper, this.source, null);
    }


    @Test
    public void shouldGetItemLabelWhenProviderParametersExist() throws SnIndexerException, SnException
    {
        Object object = new Object();
        Map<String, String> valueProviderParameters = new HashMap<>();
        String parameter = "catalogVersion";
        String label = "catalogVersionLabel";
        SnQualifier qualifier = (SnQualifier)Mockito.mock(SnQualifier.class);
        Locale locale = new Locale("en");
        List<SnQualifier> qualifiers = new ArrayList<>(Arrays.asList(new SnQualifier[] {qualifier}));
        valueProviderParameters.put("expression", "catalogVersion");
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.when(this.snExpressionEvaluator.evaluate(this.source, "catalogVersion")).thenReturn(object);
        Mockito.when(this.fieldWrapper.getQualifiers()).thenReturn(qualifiers);
        Mockito.when(Boolean.valueOf(this.fieldWrapper.isLocalized())).thenReturn(Boolean.valueOf(true));
        Mockito.when(qualifier.getAs(Locale.class)).thenReturn(locale);
        Mockito.when(this.labelServiceProxy.getObjectLabel(object, locale)).thenReturn("catalogVersionLabel");
        Map<Locale, Object> localizedValue = (Map<Locale, Object>)this.provider.getFieldValue(this.indexerContext, this.fieldWrapper, this.source, null);
        Assertions.assertThat(localizedValue.isEmpty()).isEqualTo(false);
        Assertions.assertThat(localizedValue.size()).isEqualTo(1);
        Assertions.assertThat(localizedValue.get(locale)).isEqualTo("catalogVersionLabel");
    }


    @Test
    public void shouldNotGetItemLabelWhenLabelEmpty() throws SnIndexerException, SnException
    {
        Object object = new Object();
        Map<String, String> valueProviderParameters = new HashMap<>();
        String parameter = "catalogVersion";
        String label = "";
        SnQualifier qualifier = (SnQualifier)Mockito.mock(SnQualifier.class);
        Locale locale = new Locale("en");
        List<SnQualifier> qualifiers = new ArrayList<>(Arrays.asList(new SnQualifier[] {qualifier}));
        valueProviderParameters.put("expression", "catalogVersion");
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.when(this.snExpressionEvaluator.evaluate(this.source, "catalogVersion")).thenReturn(object);
        Mockito.when(this.fieldWrapper.getQualifiers()).thenReturn(qualifiers);
        Mockito.when(Boolean.valueOf(this.fieldWrapper.isLocalized())).thenReturn(Boolean.valueOf(true));
        Mockito.when(qualifier.getAs(Locale.class)).thenReturn(locale);
        Mockito.when(this.labelServiceProxy.getObjectLabel(object, locale)).thenReturn("");
        Map<Locale, Object> localizedValue = (Map<Locale, Object>)this.provider.getFieldValue(this.indexerContext, this.fieldWrapper, this.source, null);
        Assertions.assertThat(localizedValue.isEmpty()).isEqualTo(true);
    }
}
