package com.hybris.backoffice.searchservices.providers.impl;

import com.hybris.backoffice.proxy.DataQualityCalculationServiceProxy;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.searchservices.admin.data.SnField;
import de.hybris.platform.searchservices.core.SnException;
import de.hybris.platform.searchservices.core.service.SnExpressionEvaluator;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DataQualitySnIndexerValueProviderTest
{
    @Mock
    private SnIndexerContext indexerContext;
    @Mock
    private SnIndexerFieldWrapper fieldWrapper;
    @Mock
    private ItemModel source;
    @InjectMocks
    private DataQualitySnIndexerValueProvider provider;
    @Mock
    private SnExpressionEvaluator snExpressionEvaluator;
    @Mock
    private SnField snField;
    @Mock
    private DataQualityCalculationServiceProxy dataQualityCalculationServiceProxy;


    @Test
    public void shouldGetFieldValue() throws SnIndexerException, SnException
    {
        String testDomainGroupId = "testDomainGroupId";
        Double testDouble = Double.valueOf(0.21035468D);
        Optional<Double> testOptionalDouble = Optional.of(testDouble);
        Double testFormatDouble = Double.valueOf(0.21D);
        this.provider.setDataQualityCalculationServiceProxy(this.dataQualityCalculationServiceProxy);
        this.provider.setDomainGroupId("testDomainGroupId");
        Mockito.when(this.dataQualityCalculationServiceProxy.calculate(this.source, "testDomainGroupId")).thenReturn(testOptionalDouble);
        Assertions.assertThat(this.provider.getFieldValue(this.indexerContext, this.fieldWrapper, this.source, null)).isEqualTo(testFormatDouble);
    }


    @Test(expected = SnIndexerException.class)
    public void shouldCatchExceptionWhenGetFieldValue() throws SnIndexerException
    {
        String testDomainGroupId = "testDomainGroupId";
        Optional<Double> testOptionalDouble = Optional.empty();
        this.provider.setDataQualityCalculationServiceProxy(this.dataQualityCalculationServiceProxy);
        this.provider.setDomainGroupId("testDomainGroupId");
        Mockito.when(this.dataQualityCalculationServiceProxy.calculate(this.source, "testDomainGroupId")).thenReturn(testOptionalDouble);
        this.provider.getFieldValue(this.indexerContext, this.fieldWrapper, this.source, null);
    }
}
