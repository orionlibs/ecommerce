package de.hybris.y2ysync.model;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.y2ysync.services.SyncConfigService;
import java.util.Collections;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class Y2YStreamConfigurationPrepareInterceptorIntegrationTest extends ServicelayerBaseTest
{
    @Resource
    private ModelService modelService;
    @Resource
    private SyncConfigService syncConfigService;
    private Y2YStreamConfigurationContainerModel testContainer;


    @Before
    public void setUp() throws Exception
    {
        this.testContainer = this.syncConfigService.createStreamConfigurationContainer("testContainer");
    }


    @Test
    public void shouldGenerateDataHubTypeWithNamespacePrefix()
    {
        String typeCode = "Product";
        Y2YStreamConfigurationModel streamConfig = this.syncConfigService.createStreamConfiguration(this.testContainer, "Product",
                        Collections.emptySet());
        this.modelService.save(streamConfig);
        Assertions.assertThat(streamConfig).isNotNull();
        Assertions.assertThat(streamConfig.getDataHubType()).isEqualTo("testContainer_Product");
    }


    @Test
    public void shouldNotGenerateDataHubTypeForSavedObject()
    {
        Y2YStreamConfigurationModel streamConfig = this.syncConfigService.createStreamConfiguration(this.testContainer, "Product",
                        Collections.emptySet());
        this.modelService.save(streamConfig);
        streamConfig.setDataHubType("");
        this.modelService.save(streamConfig);
        Assertions.assertThat(streamConfig.getDataHubType()).isEqualTo("");
    }


    @Test
    public void shouldSkipDataHubTypeGenerationIfSet()
    {
        String typeCode = "Product";
        Y2YStreamConfigurationModel streamConfig = this.syncConfigService.createStreamConfiguration(this.testContainer, "Product",
                        Collections.emptySet());
        streamConfig.setDataHubType("explicitType");
        this.modelService.save(streamConfig);
        Assertions.assertThat(streamConfig).isNotNull();
        Assertions.assertThat(streamConfig.getDataHubType()).isEqualTo("explicitType");
    }


    @Test
    public void shouldSkipGenerationOfWhereClauseWhenAutoGenerateWhereClauseOptionIsSetToFalse() throws Exception
    {
        String typeCode = "Product";
        Y2YStreamConfigurationModel streamConfig = createStreamConfigNoAutoGenerationOfWhereClause("Product");
        Assertions.assertThat(streamConfig).isNotNull();
        Assertions.assertThat(streamConfig.getWhereClause()).isNull();
    }


    @Test
    public void shouldSkipGenerationOfInfoExpressionWhenAutoGenerateInfoExpressionOptionIsSetToFalse() throws Exception
    {
        String typeCode = "Product";
        Y2YStreamConfigurationModel streamConfig = createStreamConfigNoAutoGenerationOfInfoExpression("Product");
        Assertions.assertThat(streamConfig).isNotNull();
        Assertions.assertThat(streamConfig.getInfoExpression()).isNull();
    }


    @Test
    public void shouldSkipGenerationOfWhereClauseForCompletelyCatalogVersionUnawareModel() throws Exception
    {
        String typeCode = "Title";
        Y2YStreamConfigurationModel streamConfig = createStreamConfig("Title");
        Assertions.assertThat(streamConfig).isNotNull();
        Assertions.assertThat(streamConfig.getWhereClause()).isNull();
    }


    @Test
    public void shouldSkipGenerationOfWhereClauseForItemTypeStream() throws Exception
    {
        String typeCode = "Item";
        Y2YStreamConfigurationModel streamConfig = createStreamConfig("Item");
        Assertions.assertThat(streamConfig).isNotNull();
        Assertions.assertThat(streamConfig.getWhereClause()).isNull();
    }


    @Test
    public void shouldAutoGenerateWhereClauseForCatalogVersionAwareModel() throws Exception
    {
        String typeCode = "Product";
        Y2YStreamConfigurationModel streamConfig = createStreamConfig("Product");
        Assertions.assertThat(streamConfig).isNotNull();
        Assertions.assertThat(streamConfig.getWhereClause()).isEqualTo("{catalogVersion}=?catalogVersion");
    }


    @Test
    public void shouldAutoGenerateInfoExpressionForCatalogVersionAwareModel() throws Exception
    {
        String typeCode = "Product";
        Y2YStreamConfigurationModel streamConfig = createStreamConfig("Product");
        Assertions.assertThat(streamConfig).isNotNull();
        Assertions.assertThat(streamConfig.getInfoExpression())
                        .isEqualTo("#{getCatalogVersion().getCatalog().getId()}:#{getCatalogVersion().getVersion()}|#{getCode()}");
    }


    @Test
    public void shouldAutoGenerateWhereClauseForCatalogVersionUnawareModelUsingPartOfRelation() throws Exception
    {
        String typeCode = "ProductReference";
        Y2YStreamConfigurationModel streamConfig = createStreamConfig("ProductReference");
        Assertions.assertThat(streamConfig).isNotNull();
        Assertions.assertThat(streamConfig.getWhereClause())
                        .isEqualTo("{item.source} IN ({{ SELECT {p.PK} FROM {Product AS p} WHERE {p.catalogVersion}=?catalogVersion }})");
    }


    private Y2YStreamConfigurationModel createStreamConfig(String typeCode)
    {
        Y2YStreamConfigurationModel configuration = this.syncConfigService.createStreamConfiguration(this.testContainer, typeCode,
                        Collections.emptySet());
        this.modelService.save(configuration);
        return configuration;
    }


    private Y2YStreamConfigurationModel createStreamConfigNoAutoGenerationOfWhereClause(String typeCode)
    {
        Y2YStreamConfigurationModel configuration = this.syncConfigService.createStreamConfiguration(this.testContainer, typeCode,
                        Collections.emptySet());
        configuration.setAutoGenerateWhereClause(Boolean.FALSE);
        this.modelService.save(configuration);
        return configuration;
    }


    private Y2YStreamConfigurationModel createStreamConfigNoAutoGenerationOfInfoExpression(String typeCode)
    {
        Y2YStreamConfigurationModel configuration = this.syncConfigService.createStreamConfiguration(this.testContainer, typeCode,
                        Collections.emptySet());
        configuration.setAutoGenerateInfoExpression(Boolean.FALSE);
        this.modelService.save(configuration);
        return configuration;
    }
}
