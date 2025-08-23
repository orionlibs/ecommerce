package de.hybris.deltadetection.model;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StreamConfigurationContainerIntegrationTest extends ServicelayerTransactionalBaseTest
{
    @Resource
    private ModelService modelService;
    @Resource
    private TypeService typeService;
    private StreamConfigurationContainerModel container1;
    private StreamConfigurationContainerModel container2;


    @Before
    public void setUp() throws Exception
    {
        this.container1 = (StreamConfigurationContainerModel)this.modelService.create(StreamConfigurationContainerModel.class);
        this.container1.setId("testStreams1");
        this.container2 = (StreamConfigurationContainerModel)this.modelService.create(StreamConfigurationContainerModel.class);
        this.container2.setId("testStreams2");
        this.modelService.saveAll(new Object[] {this.container1, this.container2});
    }


    @Test
    public void shouldSaveStreamConfigurationCorrectlyWithProperWhereClause() throws Exception
    {
        StreamConfigurationModel config = prepareStreamConfigurationFor("testStream", ProductModel.class, "{code}=?");
        config.setContainer(this.container1);
        this.modelService.save(config);
        Assertions.assertThat(this.modelService.isNew(config)).isFalse();
        Assertions.assertThat(config.getStreamId()).isEqualTo("testStream");
        Assertions.assertThat(config.getWhereClause()).isEqualTo("{code}=?");
    }


    @Test
    public void shouldThrowModelSavingExceptionWhenTryingToSaveStreamConfigurationWithTheSameStreamIDWithinSameContainer() throws Exception
    {
        StreamConfigurationModel config1 = prepareStreamConfigurationFor("testStream", ProductModel.class, "{code}=?");
        StreamConfigurationModel config2 = prepareStreamConfigurationFor("testStream", ProductModel.class, "{code}=?");
        config1.setContainer(this.container1);
        config2.setContainer(this.container1);
        try
        {
            this.modelService.saveAll(new Object[] {config1, config2});
            Assert.fail("should throw ModelSavingException");
        }
        catch(ModelSavingException modelSavingException)
        {
        }
    }


    @Test
    public void shouldFailValidationOfStreamConfigurationWhenWhereClauseContains_WHERE_keywordAtTheBeginning() throws Exception
    {
        shouldFailValidationOfStreamConfigurationWhenWhereClauseContains_WHERE_keywordAtTheBeginning("WHERE {code} IS TRUE");
    }


    @Test
    public void shouldFailValidationOfStreamConfigurationWhenWhereClauseContains_where_keywordAtTheBeginning() throws Exception
    {
        shouldFailValidationOfStreamConfigurationWhenWhereClauseContains_WHERE_keywordAtTheBeginning("where {code} IS TRUE");
    }


    @Test
    public void shouldFailValidationOfStreamConfigurationWhenWhereClauseContains__where_keywordAtTheBeginning() throws Exception
    {
        shouldFailValidationOfStreamConfigurationWhenWhereClauseContains_WHERE_keywordAtTheBeginning(" WHERE {code} IS TRUE");
    }


    private void shouldFailValidationOfStreamConfigurationWhenWhereClauseContains_WHERE_keywordAtTheBeginning(String whereClause)
    {
        StreamConfigurationModel config = prepareStreamConfigurationFor("testStream", ProductModel.class, whereClause);
        config.setWhereClause(whereClause);
        try
        {
            this.modelService.save(config);
            Assert.fail("Should throw ModelSavingException");
        }
        catch(ModelSavingException e)
        {
            Assertions.assertThat(e.getCause()).isInstanceOf(InterceptorException.class);
        }
    }


    private StreamConfigurationModel prepareStreamConfigurationFor(String streamId, Class itemType, String whereClause)
    {
        StreamConfigurationModel config = (StreamConfigurationModel)this.modelService.create(StreamConfigurationModel.class);
        config.setStreamId(streamId);
        config.setItemTypeForStream(this.typeService.getComposedTypeForClass(itemType));
        config.setWhereClause(whereClause);
        return config;
    }
}
