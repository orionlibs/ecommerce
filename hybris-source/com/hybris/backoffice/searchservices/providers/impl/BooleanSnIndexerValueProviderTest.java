package com.hybris.backoffice.searchservices.providers.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.searchservices.admin.data.SnField;
import de.hybris.platform.searchservices.core.SnException;
import de.hybris.platform.searchservices.core.service.SnExpressionEvaluator;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BooleanSnIndexerValueProviderTest
{
    @Mock
    private SnIndexerContext indexerContext;
    @Mock
    private SnIndexerFieldWrapper fieldWrapper;
    @Mock
    private ItemModel source;
    @InjectMocks
    private BooleanSnIndexerValueProvider provider;
    @Mock
    private SnExpressionEvaluator snExpressionEvaluator;
    @Mock
    private SnField snField;
    static final String FIELD_ID = "field_id";


    @Test
    public void shouldGetTrueFieldValue() throws SnIndexerException, SnException
    {
        List<ItemModel> value = Collections.emptyList();
        this.provider.setSnExpressionEvaluator(this.snExpressionEvaluator);
        Mockito.when(this.snExpressionEvaluator.evaluate(ArgumentMatchers.any(), (String)ArgumentMatchers.any())).thenReturn(value);
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(new HashMap<>());
        Mockito.when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.when(this.snField.getId()).thenReturn("field_id");
        Assertions.assertThat(this.provider.getFieldValue(this.indexerContext, this.fieldWrapper, this.source, null)).isEqualTo(Boolean.valueOf(true));
    }


    @Test
    public void shouldGetFalseFieldValue() throws SnIndexerException, SnException
    {
        List<ItemModel> value = new ArrayList<>();
        ItemModel item = (ItemModel)Mockito.mock(ItemModel.class);
        value.add(item);
        this.provider.setSnExpressionEvaluator(this.snExpressionEvaluator);
        Mockito.when(this.snExpressionEvaluator.evaluate(ArgumentMatchers.any(), (String)ArgumentMatchers.any())).thenReturn(value);
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(new HashMap<>());
        Mockito.when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.when(this.snField.getId()).thenReturn("field_id");
        Assertions.assertThat(this.provider.getFieldValue(this.indexerContext, this.fieldWrapper, this.source, null)).isEqualTo(Boolean.valueOf(false));
    }


    @Test
    public void shouldGetNotBooleanFieldValue() throws SnIndexerException, SnException
    {
        ItemModel value = (ItemModel)Mockito.mock(ItemModel.class);
        this.provider.setSnExpressionEvaluator(this.snExpressionEvaluator);
        Mockito.when(this.snExpressionEvaluator.evaluate(ArgumentMatchers.any(), (String)ArgumentMatchers.any())).thenReturn(value);
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(new HashMap<>());
        Mockito.when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.when(this.snField.getId()).thenReturn("field_id");
        Assertions.assertThat(this.provider.getFieldValue(this.indexerContext, this.fieldWrapper, this.source, null)).isEqualTo(value);
    }
}
