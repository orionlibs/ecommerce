package com.hybris.backoffice.searchservices.providers.impl;

import com.hybris.backoffice.proxy.LabelServiceProxy;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.searchservices.admin.data.SnField;
import de.hybris.platform.searchservices.core.SnException;
import de.hybris.platform.searchservices.core.service.SnExpressionEvaluator;
import de.hybris.platform.searchservices.core.service.SnQualifier;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ItemtypeLabelSnIndexerValueProviderTest
{
    @Mock
    private SnIndexerContext indexerContext;
    @Mock
    private SnIndexerFieldWrapper fieldWrapper;
    @Mock
    private ItemModel source;
    @InjectMocks
    private ItemtypeLabelSnIndexerValueProvider provider;
    @Mock
    private SnExpressionEvaluator snExpressionEvaluator;
    @Mock
    private SnField snField;
    @Mock
    private TypeService typeService;
    @Mock
    private LabelServiceProxy labelServiceProxy;
    @Mock
    private ComposedTypeModel itemtypeModel;


    @Before
    public void setUp()
    {
        this.provider.setLabelServiceProxy(this.labelServiceProxy);
        this.provider.setTypeService(this.typeService);
        String itemtypeCode = "testItemtype";
        Mockito.when(this.source.getItemtype()).thenReturn("testItemtype");
        Mockito.when(this.typeService.getComposedTypeForCode("testItemtype")).thenReturn(this.itemtypeModel);
    }


    @Test
    public void shouldGetFieldValue() throws SnException
    {
        List<SnQualifier> qualifiers = new ArrayList<>();
        SnQualifier testQualifier1 = (SnQualifier)Mockito.mock(SnQualifier.class);
        SnQualifier testQualifier2 = (SnQualifier)Mockito.mock(SnQualifier.class);
        qualifiers.add(testQualifier1);
        qualifiers.add(testQualifier2);
        Mockito.when(Boolean.valueOf(this.fieldWrapper.isLocalized())).thenReturn(Boolean.valueOf(true));
        Mockito.when(testQualifier1.getAs(Locale.class)).thenReturn(Locale.ENGLISH);
        Mockito.when(testQualifier2.getAs(Locale.class)).thenReturn(Locale.GERMAN);
        Mockito.when(this.fieldWrapper.getQualifiers()).thenReturn(qualifiers);
        String testLabelEn = "testLabelEn";
        String testLabelDe = "testLabelDe";
        Mockito.when(this.labelServiceProxy.getObjectLabel(this.itemtypeModel, Locale.ENGLISH)).thenReturn("testLabelEn");
        Mockito.when(this.labelServiceProxy.getObjectLabel(this.itemtypeModel, Locale.GERMAN)).thenReturn("testLabelDe");
        Object fieldValue = this.provider.getFieldValue(this.indexerContext, this.fieldWrapper, this.source, null);
        Assertions.assertThat(fieldValue).isInstanceOf(Map.class);
        Map fieldValueMap = (Map)fieldValue;
        Assertions.assertThat(fieldValueMap).hasSize(2).containsEntry(Locale.ENGLISH, "testLabelEn").containsEntry(Locale.GERMAN, "testLabelDe");
    }


    @Test(expected = SnIndexerException.class)
    public void shouldCatchExceptionWhenGetFieldValue() throws SnIndexerException
    {
        Mockito.when(Boolean.valueOf(this.fieldWrapper.isLocalized())).thenReturn(Boolean.valueOf(false));
        this.provider.getFieldValue(this.indexerContext, this.fieldWrapper, this.source, null);
    }
}
