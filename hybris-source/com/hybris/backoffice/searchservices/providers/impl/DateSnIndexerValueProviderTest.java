package com.hybris.backoffice.searchservices.providers.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.searchservices.admin.data.SnField;
import de.hybris.platform.searchservices.core.SnException;
import de.hybris.platform.searchservices.core.service.SnExpressionEvaluator;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DateSnIndexerValueProviderTest
{
    @Mock
    private SnIndexerContext indexerContext;
    @Mock
    private SnIndexerFieldWrapper fieldWrapper;
    @Mock
    private ItemModel source;
    @InjectMocks
    private DateSnIndexerValueProvider provider;
    @Mock
    private SnExpressionEvaluator snExpressionEvaluator;
    @Mock
    private SnField snField;
    @Mock
    private ItemModel item;


    @Test
    public void shouldGetFieldValue() throws SnIndexerException, SnException
    {
        String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date value = new Date();
        this.provider.setSnExpressionEvaluator(this.snExpressionEvaluator);
        Mockito.when(this.snExpressionEvaluator.evaluate(Matchers.any(), (String)Matchers.any())).thenReturn(value);
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(new HashMap<>());
        Mockito.when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.when(this.snField.getId()).thenReturn("field_id");
        Assertions.assertThat(this.provider.getFieldValue(this.indexerContext, this.fieldWrapper, this.source, null)).isEqualTo(dateFormat.format(value));
    }


    @Test
    public void shouldGetNotDateFieldValue() throws SnIndexerException, SnException
    {
        ItemModel value = (ItemModel)Mockito.mock(ItemModel.class);
        this.provider.setSnExpressionEvaluator(this.snExpressionEvaluator);
        Mockito.when(this.snExpressionEvaluator.evaluate(Matchers.any(), (String)Matchers.any())).thenReturn(value);
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(new HashMap<>());
        Mockito.when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.when(this.snField.getId()).thenReturn("field_id");
        Assertions.assertThat(this.provider.getFieldValue(this.indexerContext, this.fieldWrapper, this.source, null)).isEqualTo(value);
    }
}
