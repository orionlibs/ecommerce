package de.hybris.y2ysync.services.impl;

import com.google.common.collect.Sets;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.DescriptorModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.y2ysync.XMLContentAssert;
import de.hybris.y2ysync.model.Y2YColumnDefinitionModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationModel;
import de.hybris.y2ysync.services.DataHubConfigService;
import de.hybris.y2ysync.services.DataHubExtGenerationConfig;
import de.hybris.y2ysync.services.SyncConfigService;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class DefaultDataHubConfigServiceTest extends ServicelayerTransactionalBaseTest
{
    @Resource
    private ModelService modelService;
    @Resource
    private DataHubConfigService dataHubConfigService;
    @Resource
    private SyncConfigService syncConfigService;
    @Resource
    private TypeService typeService;
    private Y2YStreamConfigurationContainerModel container;


    @Before
    public void setUp()
    {
        this.container = this.syncConfigService.createStreamConfigurationContainer("testContainer");
        this.modelService.save(this.container);
    }


    @Test
    public void shouldThrowIllegalStateExceptionWhenForUnsavedStreamConfiguration()
    {
        Set<AttributeDescriptorModel> attributeDescriptors = getAttributeDescriptorsFor("Product", new String[] {"code", "catalogVersion", "name", "thumbnail"});
        Y2YStreamConfigurationModel configuration = this.syncConfigService.createStreamConfiguration(this.container, "Product", attributeDescriptors,
                        Collections.emptySet());
        try
        {
            DataHubExtGenerationConfig dataHubExtGenerationConfig = getDataHubExtGenerationConfig();
            this.dataHubConfigService.createModelDefinitions(configuration, dataHubExtGenerationConfig);
            Assert.fail("Should throw IllegalStateException");
        }
        catch(IllegalStateException illegalStateException)
        {
        }
    }


    @Test
    public void shouldThrowIllegalStateExceptionWhenForUnsavedStreamConfigurationContainer()
    {
        Y2YStreamConfigurationContainerModel container = this.syncConfigService.createStreamConfigurationContainer("unsaved");
        try
        {
            this.dataHubConfigService.createModelDefinitions(container);
            Assert.fail("Should throw IllegalStateException");
        }
        catch(IllegalStateException illegalStateException)
        {
        }
    }


    @Test
    public void shouldGenerateDataHubModelForStreamConfiguration() throws Exception
    {
        Set<AttributeDescriptorModel> attributeDescriptors = getAttributeDescriptorsFor("Product", new String[] {"code", "catalogVersion", "name", "thumbnail"});
        Y2YStreamConfigurationModel configuration = this.syncConfigService.createStreamConfiguration(this.container, "Product", attributeDescriptors,
                        Collections.emptySet());
        this.modelService.save(configuration);
        DataHubExtGenerationConfig dataHubExtGenerationConfig = getDataHubExtGenerationConfig();
        String modelDefinitions = this.dataHubConfigService.createModelDefinitions(configuration, dataHubExtGenerationConfig);
        ((XMLContentAssert)XMLContentAssert.assertThat(modelDefinitions).isNotNull())
                        .ignoreWhitespaces()
                        .ignoreNodeOrder()
                        .ignoreComments()
                        .isIdenticalToResource("/test/datahubxml/dataHubModelFromStreamConfig.xml");
    }


    private DataHubExtGenerationConfig getDataHubExtGenerationConfig()
    {
        DataHubExtGenerationConfig dataHubExtGenerationConfig = new DataHubExtGenerationConfig();
        dataHubExtGenerationConfig.setGenerateRawItems(true);
        dataHubExtGenerationConfig.setGenerateCanonicalItems(true);
        dataHubExtGenerationConfig.setGenerateTargetItems(true);
        dataHubExtGenerationConfig.setPrettyFormat(false);
        dataHubExtGenerationConfig.setTargetExportCodes("");
        dataHubExtGenerationConfig.setTargetExportURL("http://www.xsd2xml.com");
        dataHubExtGenerationConfig.setTargetPassword("str1234");
        dataHubExtGenerationConfig.setTargetType("str1234");
        dataHubExtGenerationConfig.setTargetUserName("str1234");
        return dataHubExtGenerationConfig;
    }


    @Test
    public void shouldGenerateDataHubModelForStreamConfigurationContainer() throws Exception
    {
        Set<AttributeDescriptorModel> productDescriptors = getAttributeDescriptorsFor("Product", new String[] {"code", "catalogVersion", "name", "thumbnail"});
        Set<AttributeDescriptorModel> titleDescriptors = getAttributeDescriptorsFor("Title", new String[] {"code", "name"});
        Y2YStreamConfigurationModel productConfig = this.syncConfigService.createStreamConfiguration(this.container, "Product", productDescriptors,
                        Collections.emptySet());
        Y2YStreamConfigurationModel titleConfig = this.syncConfigService.createStreamConfiguration(this.container, "Title", titleDescriptors,
                        Collections.emptySet());
        productConfig.setStreamId("a-" + UUID.randomUUID().toString());
        titleConfig.setStreamId("b-" + UUID.randomUUID().toString());
        this.modelService.saveAll(new Object[] {productConfig, titleConfig});
        DataHubExtGenerationConfig dataHubExtGenerationConfig = getDataHubExtGenerationConfig();
        String modelDefinitions = this.dataHubConfigService.createDataHubExtension(this.container, dataHubExtGenerationConfig);
        ((XMLContentAssert)XMLContentAssert.assertThat(modelDefinitions).isNotNull())
                        .ignoreComments()
                        .ignoreNodeOrder()
                        .ignoreWhitespaces()
                        .isIdenticalToResource("/test/datahubxml/dataHubModelFromStreamConfigContainer.xml");
    }


    @Test
    public void shouldGenerateDataHubModelForStreamConfigurationWithUntypedColumnDefintion() throws Exception
    {
        Y2YColumnDefinitionModel mediaColDef = this.syncConfigService.createUntypedColumnDefinition("@media(Translator)", "pullURL");
        Set<AttributeDescriptorModel> mediaDesriptors = getAttributeDescriptorsFor("Media", new String[] {"code", "mime"});
        Y2YStreamConfigurationModel configuration = this.syncConfigService.createStreamConfiguration(this.container, "Media", mediaDesriptors,
                        Sets.newHashSet((Object[])new Y2YColumnDefinitionModel[] {mediaColDef}));
        this.modelService.save(configuration);
        DataHubExtGenerationConfig dataHubExtGenerationConfig = getDataHubExtGenerationConfig();
        String modelDefinitions = this.dataHubConfigService.createModelDefinitions(configuration, dataHubExtGenerationConfig);
        ((XMLContentAssert)XMLContentAssert.assertThat(modelDefinitions)
                        .isNotNull())
                        .ignoreWhitespaces()
                        .ignoreNodeOrder()
                        .ignoreComments()
                        .isIdenticalToResource("/test/datahubxml/dataHubModelForStreamConfigurationWithUntypedColumnDefintion.xml");
    }


    private Set<AttributeDescriptorModel> getAttributeDescriptorsFor(String typeCode, String... qualifiers)
    {
        return (Set<AttributeDescriptorModel>)Arrays.<String>stream(qualifiers).map(q -> this.typeService.getAttributeDescriptor(typeCode, q))
                        .sorted(Comparator.comparing(DescriptorModel::getQualifier))
                        .collect(Collectors.toCollection(java.util.LinkedHashSet::new));
    }
}
