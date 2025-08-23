package com.hybris.backoffice.searchservices.providers.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.searchservices.admin.data.SnField;
import de.hybris.platform.searchservices.core.SnException;
import de.hybris.platform.searchservices.core.service.SnExpressionEvaluator;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClassificationSnIndexerValueProviderTest
{
    @Mock
    private SnIndexerContext indexerContext;
    @Mock
    private SnIndexerFieldWrapper fieldWrapper;
    @Mock
    private ItemModel source;
    @InjectMocks
    private ClassificationSnIndexerValueProvider provider;
    @Mock
    private SnExpressionEvaluator snExpressionEvaluator;
    @Mock
    private SnField snField;
    @Mock
    private ItemModel item;


    @Test
    public void shouldGetFieldValue() throws SnIndexerException, SnException
    {
        List<ItemModel> value = new ArrayList<>();
        value.add(this.item);
        value.add(this.item);
        this.provider.setSnExpressionEvaluator(this.snExpressionEvaluator);
        Mockito.when(this.snExpressionEvaluator.evaluate(Matchers.any(), (String)Matchers.any())).thenReturn(value);
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(new HashMap<>());
        Mockito.when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.when(this.snField.getId()).thenReturn("field_id");
        Object fieldValue = this.provider.getFieldValue(this.indexerContext, this.fieldWrapper, this.source, null);
        Assertions.assertThat(fieldValue instanceof List).isEqualTo(true);
        List fieldValueList = (List)fieldValue;
        Assertions.assertThat(fieldValueList.size()).isEqualTo(1);
        Assertions.assertThat(fieldValueList.get(0)).isEqualTo(this.item);
    }


    @Test
    public void shouldGetNotCollectionFieldValue() throws SnIndexerException, SnException
    {
        this.provider.setSnExpressionEvaluator(this.snExpressionEvaluator);
        Mockito.when(this.snExpressionEvaluator.evaluate(Matchers.any(), (String)Matchers.any())).thenReturn(this.item);
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(new HashMap<>());
        Mockito.when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.when(this.snField.getId()).thenReturn("field_id");
        Object fieldValue = this.provider.getFieldValue(this.indexerContext, this.fieldWrapper, this.source, null);
        Assertions.assertThat(fieldValue).isEqualTo(this.item);
    }
}
