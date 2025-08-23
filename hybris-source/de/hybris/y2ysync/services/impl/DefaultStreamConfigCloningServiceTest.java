package de.hybris.y2ysync.services.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.deltadetection.model.StreamConfigurationModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.TestImportCsvUtil;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.y2ysync.model.Y2YColumnDefinitionModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationModel;
import de.hybris.y2ysync.services.StreamConfigCloningService;
import de.hybris.y2ysync.services.SyncConfigService;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class DefaultStreamConfigCloningServiceTest extends ServicelayerTransactionalBaseTest
{
    @Resource
    private StreamConfigCloningService streamConfigCloningService;
    @Resource
    private ModelService modelService;
    @Resource
    private CatalogVersionService catalogVersionService;
    @Resource
    private SyncConfigService syncConfigService;
    @Resource
    private TypeService typeService;
    @Resource(name = "testImportCsvUtil")
    private TestImportCsvUtil importCsvUtil;
    private Y2YStreamConfigurationContainerModel sourceContainer;
    private CatalogVersionModel catVerA1;


    @Before
    public void setUp() throws Exception
    {
        this.importCsvUtil.importCsv("/test/source_test_catalog.csv", "UTF-8");
        this.catVerA1 = this.catalogVersionService.getCatalogVersion("CatalogA", "CatalogVersionA1");
        this.sourceContainer = (Y2YStreamConfigurationContainerModel)this.modelService.create(Y2YStreamConfigurationContainerModel.class);
        this.sourceContainer.setId("testContainer");
        this.sourceContainer.setCatalogVersion(this.catVerA1);
        this.modelService.save(this.sourceContainer);
    }


    @Test
    public void testCloneStreamContainer() throws Exception
    {
        this.modelService.save(this.syncConfigService
                        .createStreamConfiguration(this.sourceContainer, "Product", prepareProductAttributeDescriptors(),
                                        Collections.emptySet()));
        this.modelService.save(this.syncConfigService
                        .createStreamConfiguration(this.sourceContainer, "Category", prepareCategoryAttributeDescriptors(),
                                        Collections.emptySet()));
        String targetContainerId = "targetContainerId";
        Y2YStreamConfigurationContainerModel targetContainer = this.streamConfigCloningService.cloneStreamContainer(this.sourceContainer, "targetContainerId");
        Assertions.assertThat(targetContainer).isNotSameAs(this.sourceContainer);
        Assertions.assertThat(targetContainer.getConfigurations()).hasSize(this.sourceContainer.getConfigurations().size()).isNotSameAs(this.sourceContainer
                        .getConfigurations());
        assureContainerFullyClonedAndNotPersisted(targetContainer);
        Assertions.assertThat(targetContainer.getCatalogVersion()).isSameAs(this.sourceContainer.getCatalogVersion());
        Assertions.assertThat(targetContainer.getId()).isEqualTo("targetContainerId");
        checkStreamIdUniqueness(this.sourceContainer.getConfigurations(), targetContainer.getConfigurations());
    }


    @Test
    public void testCloneStreamConfigurationsSubset() throws Exception
    {
        Y2YStreamConfigurationModel sourceProductStream = this.syncConfigService.createStreamConfiguration(this.sourceContainer, "Product",
                        prepareProductAttributeDescriptors(),
                        Collections.emptySet());
        Y2YStreamConfigurationModel sourceCategoryStream = this.syncConfigService.createStreamConfiguration(this.sourceContainer, "Category",
                        prepareCategoryAttributeDescriptors(),
                        Collections.emptySet());
        this.modelService.saveAll(new Object[] {sourceProductStream, sourceCategoryStream});
        Set<Y2YStreamConfigurationModel> streamConfigurationsCloned = this.streamConfigCloningService.cloneStreamConfigurations(new Y2YStreamConfigurationModel[] {sourceProductStream});
        Assertions.assertThat(streamConfigurationsCloned).hasSize(1);
        streamConfigurationsCloned.stream().forEach(e -> Assertions.assertThat(e.getContainer()).isNull());
        assureStreamsClonedAndNotPersisted((Set)streamConfigurationsCloned);
        checkStreamIdUniqueness(this.sourceContainer.getConfigurations(), (Set)streamConfigurationsCloned);
    }


    private void checkStreamIdUniqueness(Set<? extends StreamConfigurationModel> sourceStreamConfigs, Set<? extends StreamConfigurationModel> clonedStreamConfigs)
    {
        List<String> sourceStreamIds = (List<String>)sourceStreamConfigs.stream().map(e -> e.getStreamId()).collect(Collectors.toList());
        clonedStreamConfigs.stream().forEach(e -> Assertions.assertThat(e.getStreamId()).isNotIn(sourceStreamIds));
    }


    private void assureContainerFullyClonedAndNotPersisted(Y2YStreamConfigurationContainerModel targetContainer)
    {
        Assertions.assertThat(targetContainer).isNotNull();
        Assertions.assertThat((Comparable)targetContainer.getPk()).isNull();
        assureStreamsClonedAndNotPersisted(targetContainer.getConfigurations());
    }


    private void assureStreamsClonedAndNotPersisted(Set<? extends StreamConfigurationModel> streamConfigs)
    {
        Assertions.assertThat(streamConfigs).isNotEmpty();
        streamConfigs.stream().forEach(e -> Assertions.assertThat((Comparable)e.getPk()).isNull());
        for(StreamConfigurationModel conf : streamConfigs)
        {
            Set<Y2YColumnDefinitionModel> columnDefinitions = ((Y2YStreamConfigurationModel)conf).getColumnDefinitions();
            Assertions.assertThat(columnDefinitions).isNotEmpty();
            columnDefinitions.stream().forEach(e -> Assertions.assertThat((Comparable)e.getPk()).isNull());
        }
    }


    private Set<AttributeDescriptorModel> prepareProductAttributeDescriptors()
    {
        Set<AttributeDescriptorModel> res = new HashSet<>();
        res.add(this.typeService.getAttributeDescriptor("Product", "code"));
        res.add(this.typeService.getAttributeDescriptor("Product", "catalogVersion"));
        res.add(this.typeService.getAttributeDescriptor("Product", "name"));
        res.add(this.typeService.getAttributeDescriptor("Product", "unit"));
        res.add(this.typeService.getAttributeDescriptor("Product", "supercategories"));
        res.add(this.typeService.getAttributeDescriptor("Product", "approvalStatus"));
        return res;
    }


    private Set<AttributeDescriptorModel> prepareCategoryAttributeDescriptors()
    {
        Set<AttributeDescriptorModel> res = new HashSet<>();
        res.add(this.typeService.getAttributeDescriptor("Category", "code"));
        res.add(this.typeService.getAttributeDescriptor("Category", "name"));
        res.add(this.typeService.getAttributeDescriptor("Category", "catalogVersion"));
        return res;
    }
}
