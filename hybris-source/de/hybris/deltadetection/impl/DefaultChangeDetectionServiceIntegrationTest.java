package de.hybris.deltadetection.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.deltadetection.ChangeDetectionService;
import de.hybris.deltadetection.ChangesCollector;
import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.deltadetection.StreamConfiguration;
import de.hybris.deltadetection.enums.ChangeType;
import de.hybris.deltadetection.model.StreamConfigurationContainerModel;
import de.hybris.deltadetection.model.StreamConfigurationModel;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class DefaultChangeDetectionServiceIntegrationTest extends ServicelayerBaseTest
{
    private static final String CATALOG_VERSION_ONE = "CATALOG_VERSION_ONE";
    private static final String CATALOG_VERSION_TWO = "CATALOG_VERSION_TWO";
    @Resource
    private ModelService modelService;
    @Resource
    private TypeService typeService;
    @Resource
    private ChangeDetectionService changeDetectionService;
    private ComposedTypeModel catalogVersionType;
    private CatalogVersionModel catalogVersionOne;
    private CatalogVersionModel catalogVersionTwo;
    private CatalogModel catalog;


    @Before
    public void setUp()
    {
        this.catalog = (CatalogModel)this.modelService.create(CatalogModel.class);
        this.catalog.setId(uniqueId());
        this.catalogVersionOne = createCatalogVersion("CATALOG_VERSION_ONE");
        this.catalogVersionTwo = createCatalogVersion("CATALOG_VERSION_TWO");
        this.modelService.saveAll();
        this.catalogVersionType = this.typeService.getComposedTypeForClass(CatalogVersionModel.class);
    }


    @Test
    public void shouldDetectNewItemsWithoutItemSelector()
    {
        StreamConfiguration config = givenConfiguration(uniqueId());
        List<Change> changes = detectChanges(this.catalogVersionType, config);
        Assertions.assertThat(changes).containsOnly((Object[])new Change[] {added((ItemModel)this.catalogVersionOne), added((ItemModel)this.catalogVersionTwo)});
    }


    @Test
    public void shouldDetectNewItemsWithItemSelectorWithoutParameters()
    {
        String selector = "{item.version}='CATALOG_VERSION_ONE'";
        StreamConfiguration config = givenConfiguration(uniqueId()).withItemSelector("{item.version}='CATALOG_VERSION_ONE'");
        List<Change> changes = detectChanges(this.catalogVersionType, config);
        Assertions.assertThat(changes).containsOnly((Object[])new Change[] {added((ItemModel)this.catalogVersionOne)});
    }


    @Test
    public void shouldDetectNewItemsWithItemSelectorWithParameters()
    {
        String selector = "{item.version}=?cv";
        ImmutableMap immutableMap = ImmutableMap.of("cv", "CATALOG_VERSION_TWO");
        StreamConfiguration config = givenConfiguration(uniqueId()).withItemSelector("{item.version}=?cv").withParameters((Map)immutableMap);
        List<Change> changes = detectChanges(this.catalogVersionType, config);
        Assertions.assertThat(changes).containsOnly((Object[])new Change[] {added((ItemModel)this.catalogVersionTwo)});
    }


    @Test
    public void shouldDetectModifiedItemsWithoutItemSelector()
    {
        StreamConfiguration config = givenConfiguration(uniqueId());
        List<Change> changesToConsume = detectChanges(this.catalogVersionType, config);
        consumeChanges(changesToConsume);
        this.catalogVersionOne.setCategorySystemID("CHANGED");
        this.catalogVersionTwo.setCategorySystemID("CHANGED");
        this.modelService.saveAll();
        List<Change> changes = detectChanges(this.catalogVersionType, config);
        Assertions.assertThat(changes).containsOnly((Object[])new Change[] {modified((ItemModel)this.catalogVersionOne), modified((ItemModel)this.catalogVersionTwo)});
    }


    @Test
    public void shouldDetectModifiedItemsWithItemSelectorWithoutParameters()
    {
        String selector = "{item.version}='CATALOG_VERSION_ONE'";
        StreamConfiguration config = givenConfiguration(uniqueId()).withItemSelector("{item.version}='CATALOG_VERSION_ONE'");
        List<Change> changesToConsume = detectChanges(this.catalogVersionType, config);
        consumeChanges(changesToConsume);
        this.catalogVersionOne.setCategorySystemID("CHANGED");
        this.catalogVersionTwo.setCategorySystemID("CHANGED");
        this.modelService.saveAll();
        List<Change> changes = detectChanges(this.catalogVersionType, config);
        Assertions.assertThat(changes).containsOnly((Object[])new Change[] {modified((ItemModel)this.catalogVersionOne)});
    }


    @Test
    public void shouldDetectModifiedItemsWithItemSelectorWithParameters()
    {
        String selector = "{item.version}=?cv";
        ImmutableMap immutableMap = ImmutableMap.of("cv", "CATALOG_VERSION_TWO");
        StreamConfiguration config = givenConfiguration(uniqueId()).withItemSelector("{item.version}=?cv").withParameters((Map)immutableMap);
        List<Change> changesToConsume = detectChanges(this.catalogVersionType, config);
        consumeChanges(changesToConsume);
        this.catalogVersionOne.setCategorySystemID("CHANGED");
        this.catalogVersionTwo.setCategorySystemID("CHANGED");
        this.modelService.saveAll();
        List<Change> changes = detectChanges(this.catalogVersionType, config);
        Assertions.assertThat(changes).containsOnly((Object[])new Change[] {modified((ItemModel)this.catalogVersionTwo)});
    }


    @Test
    public void shouldDetectRemovedItemsWithoutItemSelector()
    {
        StreamConfiguration config = givenConfiguration(uniqueId());
        List<Change> changesToConsume = detectChanges(this.catalogVersionType, config);
        consumeChanges(changesToConsume);
        this.modelService.remove(this.catalogVersionOne);
        this.modelService.remove(this.catalogVersionTwo);
        List<Change> changes = detectChanges(this.catalogVersionType, config);
        Assertions.assertThat(changes).containsOnly((Object[])new Change[] {removed((ItemModel)this.catalogVersionOne), removed((ItemModel)this.catalogVersionTwo)});
    }


    @Test
    public void shouldDetectRemovedItemsWithItemSelectorWithoutParameters()
    {
        String selector = "{item.version}='CATALOG_VERSION_ONE'";
        StreamConfiguration config = givenConfiguration(uniqueId()).withItemSelector("{item.version}='CATALOG_VERSION_ONE'");
        List<Change> changesToConsume = detectChanges(this.catalogVersionType, config);
        consumeChanges(changesToConsume);
        this.modelService.remove(this.catalogVersionOne);
        this.modelService.remove(this.catalogVersionTwo);
        List<Change> changes = detectChanges(this.catalogVersionType, config);
        Assertions.assertThat(changes).containsOnly((Object[])new Change[] {removed((ItemModel)this.catalogVersionOne)});
    }


    @Test
    public void shouldDetectRemovedItemsWithItemSelectorWithParameters()
    {
        String selector = "{item.version}=?cv";
        ImmutableMap immutableMap = ImmutableMap.of("cv", "CATALOG_VERSION_TWO");
        StreamConfiguration config = givenConfiguration(uniqueId()).withItemSelector("{item.version}=?cv").withParameters((Map)immutableMap);
        List<Change> changesToConsume = detectChanges(this.catalogVersionType, config);
        consumeChanges(changesToConsume);
        this.modelService.remove(this.catalogVersionOne);
        this.modelService.remove(this.catalogVersionTwo);
        this.modelService.saveAll();
        List<Change> changes = detectChanges(this.catalogVersionType, config);
        Assertions.assertThat(changes).containsOnly((Object[])new Change[] {removed((ItemModel)this.catalogVersionTwo)});
    }


    @Test
    public void shouldDetectAllChangeTypesWithoutItemSelector()
    {
        StreamConfiguration config = givenConfiguration(uniqueId());
        List<Change> changesToConsume = detectChanges(this.catalogVersionType, config);
        consumeChanges(changesToConsume);
        this.modelService.remove(this.catalogVersionOne);
        this.catalogVersionTwo.setCategorySystemID("CHANGED");
        CatalogVersionModel newCatalogVersion = createCatalogVersion("TMP_VERSION");
        this.modelService.saveAll();
        List<Change> changes = detectChanges(this.catalogVersionType, config);
        Assertions.assertThat(changes).containsOnly((Object[])new Change[] {removed((ItemModel)this.catalogVersionOne), modified((ItemModel)this.catalogVersionTwo), added((ItemModel)newCatalogVersion)});
    }


    @Test
    public void shouldDetectAllChangeTypesWithItemSelectorWithoutParameters()
    {
        String selector = "{item.version} like '%VERSION%'";
        StreamConfiguration config = givenConfiguration(uniqueId()).withItemSelector("{item.version} like '%VERSION%'");
        List<Change> changesToConsume = detectChanges(this.catalogVersionType, config);
        consumeChanges(changesToConsume);
        this.modelService.remove(this.catalogVersionTwo);
        this.catalogVersionOne.setCategorySystemID("CHANGED");
        CatalogVersionModel newCatalogVersion = createCatalogVersion("TMP_VERSION");
        this.modelService.saveAll();
        List<Change> changes = detectChanges(this.catalogVersionType, config);
        Assertions.assertThat(changes).containsOnly((Object[])new Change[] {removed((ItemModel)this.catalogVersionTwo), added((ItemModel)newCatalogVersion), modified((ItemModel)this.catalogVersionOne)});
    }


    @Test
    public void shouldDetectAllChangeTypesWithItemSelectorWithParameters()
    {
        String selector = "{item.version} like ?cv";
        ImmutableMap immutableMap = ImmutableMap.of("cv", "%VERSION%");
        StreamConfiguration config = givenConfiguration(uniqueId()).withItemSelector("{item.version} like ?cv").withParameters((Map)immutableMap);
        List<Change> changesToConsume = detectChanges(this.catalogVersionType, config);
        consumeChanges(changesToConsume);
        this.modelService.remove(this.catalogVersionTwo);
        this.catalogVersionOne.setCategorySystemID("CHANGED");
        CatalogVersionModel newCatalogVersion = createCatalogVersion("TMP_VERSION");
        this.modelService.saveAll();
        List<Change> changes = detectChanges(this.catalogVersionType, config);
        Assertions.assertThat(changes).containsOnly((Object[])new Change[] {removed((ItemModel)this.catalogVersionTwo), added((ItemModel)newCatalogVersion), modified((ItemModel)this.catalogVersionOne)});
    }


    @Test
    public void shouldDetectAsRemovedItemsWhichNoLongerMatchSelector()
    {
        String selector = "{item.version} like ?cv";
        ImmutableMap immutableMap = ImmutableMap.of("cv", "%VERSION%");
        StreamConfiguration config = givenConfiguration(uniqueId()).withItemSelector("{item.version} like ?cv").withParameters((Map)immutableMap);
        List<Change> changesToConsume = detectChanges(this.catalogVersionType, config);
        consumeChanges(changesToConsume);
        this.catalogVersionOne.setVersion("CHANGED");
        this.modelService.saveAll();
        List<Change> changes = detectChanges(this.catalogVersionType, config);
        Assertions.assertThat(changes).containsOnly((Object[])new Change[] {removed((ItemModel)this.catalogVersionOne)});
    }


    @Test
    public void shouldDetectAsNewItemsWhichBecomeToMatchSelector()
    {
        String selector = "{item.version} like ?cv";
        ImmutableMap immutableMap = ImmutableMap.of("cv", "%ONE%");
        StreamConfiguration config = givenConfiguration(uniqueId()).withItemSelector("{item.version} like ?cv").withParameters((Map)immutableMap);
        List<Change> changesToConsume = detectChanges(this.catalogVersionType, config);
        consumeChanges(changesToConsume);
        this.catalogVersionTwo.setVersion(this.catalogVersionTwo.getVersion() + "ONE");
        this.modelService.saveAll();
        List<Change> changes = detectChanges(this.catalogVersionType, config);
        Assertions.assertThat(changes).containsOnly((Object[])new Change[] {added((ItemModel)this.catalogVersionTwo)});
    }


    private CatalogVersionModel createCatalogVersion(String version)
    {
        CatalogVersionModel result = (CatalogVersionModel)this.modelService.create(CatalogVersionModel.class);
        result.setVersion(version);
        result.setCatalog(this.catalog);
        return result;
    }


    private Change added(ItemModel item)
    {
        return new Change(ChangeType.NEW, item.getPk().getLong());
    }


    private Change modified(ItemModel item)
    {
        return new Change(ChangeType.MODIFIED, item.getPk().getLong());
    }


    private Change removed(ItemModel item)
    {
        return new Change(ChangeType.DELETED, item.getPk().getLong());
    }


    private StreamConfiguration givenConfiguration(String streamId)
    {
        StreamConfigurationContainerModel streamCfgContainer = (StreamConfigurationContainerModel)this.modelService.create(StreamConfigurationContainerModel.class);
        streamCfgContainer.setId("containerId");
        this.modelService.save(streamCfgContainer);
        StreamConfigurationModel streamCfg = (StreamConfigurationModel)this.modelService.create(StreamConfigurationModel.class);
        streamCfg.setStreamId(streamId);
        streamCfg.setContainer(streamCfgContainer);
        streamCfg.setItemTypeForStream(this.typeService.getComposedTypeForClass(ProductModel.class));
        streamCfg.setWhereClause("not used");
        streamCfg.setInfoExpression("#{getPk()}");
        this.modelService.save(streamCfg);
        return StreamConfiguration.buildFor(streamId);
    }


    private void consumeChanges(List<Change> changes)
    {
        List<ItemChangeDTO> changesToConsume = (List<ItemChangeDTO>)changes.stream().map(c -> c.getChangeDTO()).collect(Collectors.toList());
        this.changeDetectionService.consumeChanges(changesToConsume);
    }


    private List<Change> detectChanges(ComposedTypeModel composedType, StreamConfiguration configuration)
    {
        ImmutableList.Builder<Change> resultBuilder = ImmutableList.builder();
        this.changeDetectionService.collectChangesForType(composedType, configuration, (ChangesCollector)new Object(this, resultBuilder));
        return (List<Change>)resultBuilder.build();
    }


    private static String uniqueId()
    {
        return UUID.randomUUID().toString();
    }
}
