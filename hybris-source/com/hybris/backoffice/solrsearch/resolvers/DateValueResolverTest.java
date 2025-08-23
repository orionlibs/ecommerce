package com.hybris.backoffice.solrsearch.resolvers;

import com.hybris.backoffice.solrsearch.populators.conditionvalueconverters.DateConditionValueConverter;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.QualifierProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.NoOpQualifierProvider;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mockito;

@IntegrationTest
public class DateValueResolverTest extends ServicelayerTransactionalTest
{
    private static final String SOLR_PROPERTY_NAME_CREATION_TIME_DATE = "creationTime_date";
    private static final String SOLR_PROPERTY_NAME_ONLINE_DATE_DATE = "onlineDate_date";
    private static final String INDEXED_PROPERTY_NAME_CREATION_TIME = "creationTime";
    private static final String INDEXED_PROPERTY_NAME_ONLINE_DATE = "onlineDate";
    private static final String INDEXED_PROPERTY_TYPE_NAME_DATE = "date";
    private final DateValueResolver dateValueResolver = new DateValueResolver();
    private final DateConditionValueConverter dateConditionValueConverter = new DateConditionValueConverter();
    private QualifierProvider qualifierProvider = (QualifierProvider)new NoOpQualifierProvider();
    @Resource
    private SessionService sessionService;
    @Resource
    private ModelService modelService;


    @Before
    public void setUp()
    {
        this.dateValueResolver.setSessionService(this.sessionService);
        this.dateValueResolver.setModelService(this.modelService);
        this.dateValueResolver.setQualifierProvider(this.qualifierProvider);
    }


    @Test
    public void shouldDateConditionValueConverterBeConsistentWithDateValueResolver() throws FieldValueProviderException
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, 2, 2, 10, 11, 12);
        for(String timeZoneID : TimeZone.getAvailableIDs())
        {
            calendar.setTimeZone(TimeZone.getTimeZone(timeZoneID));
            Date dateToTest = calendar.getTime();
            InputDocument document = (InputDocument)Mockito.mock(InputDocument.class);
            IndexerBatchContext batchContext = (IndexerBatchContext)Mockito.mock(IndexerBatchContext.class);
            IndexedProperty creationTimeIndexedProperty = new IndexedProperty();
            creationTimeIndexedProperty.setName("creationTime");
            creationTimeIndexedProperty.setType("date");
            creationTimeIndexedProperty.setCurrency(false);
            IndexedProperty onlineDateIndexedProperty = new IndexedProperty();
            onlineDateIndexedProperty.setName("onlineDate");
            onlineDateIndexedProperty.setType("date");
            onlineDateIndexedProperty.setLocalized(false);
            onlineDateIndexedProperty.setCurrency(false);
            ProductModel productModel = new ProductModel();
            productModel.setCreationtime(dateToTest);
            productModel.setOnlineDate(dateToTest);
            this.dateValueResolver.resolve(document, batchContext, Arrays.asList(new IndexedProperty[] {creationTimeIndexedProperty, onlineDateIndexedProperty}, ), (ItemModel)productModel);
            ArgumentCaptor<String> captorCreationTime = ArgumentCaptor.forClass(String.class);
            ((InputDocument)Mockito.verify(document, Mockito.times(1))).addField((String)Matchers.eq("creationTime_date"), captorCreationTime.capture());
            Assertions.assertThat((String)captorCreationTime.getValue()).isEqualTo(this.dateConditionValueConverter.apply(dateToTest));
            ArgumentCaptor<String> captorOnlineDate = ArgumentCaptor.forClass(String.class);
            ((InputDocument)Mockito.verify(document, Mockito.times(1))).addField((String)Matchers.eq("onlineDate_date"), captorOnlineDate.capture());
            Assertions.assertThat((String)captorOnlineDate.getValue()).isEqualTo(this.dateConditionValueConverter.apply(dateToTest));
        }
    }
}
