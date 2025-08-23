package com.hybris.backoffice.solrsearch.resolvers;

import com.hybris.backoffice.proxy.DataQualityCalculationServiceProxy;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.QualifierProvider;
import java.util.Collections;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalMatchers;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DataQualityValueResolverTest
{
    @InjectMocks
    private DataQualityValueResolver testSubject;
    @Mock
    private DataQualityCalculationServiceProxy dataQualityCalculationServiceProxy;
    @Mock
    private QualifierProvider qualifierProvider;
    @Mock
    private SessionService sessionService;
    @Mock
    private JaloSession jaloSession;


    @Before
    public void setUp() throws Exception
    {
        Mockito.when(this.sessionService.getRawSession((Session)Matchers.any())).thenReturn(this.jaloSession);
    }


    @Test
    public void shouldNotAllowNegativeDataQuality() throws Exception
    {
        InputDocument document = (InputDocument)Mockito.mock(InputDocument.class);
        IndexedProperty property = (IndexedProperty)Mockito.mock(IndexedProperty.class);
        Mockito.when(this.dataQualityCalculationServiceProxy.calculate(Matchers.any(), (String)Matchers.any())).thenReturn(Optional.of(Double.valueOf(-1.0D)));
        this.testSubject.resolve(document, (IndexerBatchContext)Mockito.mock(IndexerBatchContext.class), Collections.singletonList(property), (ItemModel)Mockito.mock(ItemModel.class));
        ((InputDocument)Mockito.verify(document, Mockito.never())).addField((IndexedProperty)Matchers.any(), Double.valueOf(AdditionalMatchers.lt(0.0D)), (String)Matchers.any());
        ((InputDocument)Mockito.verify(document)).addField((IndexedProperty)Matchers.any(), Double.valueOf(Matchers.eq(0.0D)), (String)Matchers.any());
    }
}
