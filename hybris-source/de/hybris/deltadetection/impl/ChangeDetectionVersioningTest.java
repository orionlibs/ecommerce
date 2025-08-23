package de.hybris.deltadetection.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.deltadetection.ChangeDetectionService;
import de.hybris.deltadetection.ChangesCollector;
import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.deltadetection.StreamConfiguration;
import de.hybris.deltadetection.enums.ChangeType;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class ChangeDetectionVersioningTest extends ServicelayerBaseTest
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
    public void shouldDetectNewItemsWithForcedScalarVersionValue()
    {
        StreamConfiguration config = givenConfiguration(uniqueId(), "'GIVEN_VERSION_VALUE'");
        List<Change> changes = detectChanges(this.catalogVersionType, config);
        Assertions.assertThat(changes).containsOnly((Object[])new Change[] {added((ItemModel)this.catalogVersionOne), added((ItemModel)this.catalogVersionTwo)});
        Assertions.assertThat(((Change)changes.get(0)).getChangeDTO().getVersionValue()).isEqualTo("GIVEN_VERSION_VALUE");
        Assertions.assertThat(((Change)changes.get(1)).getChangeDTO().getVersionValue()).isEqualTo("GIVEN_VERSION_VALUE");
    }


    @Test
    public void shouldDetectNewItemsWithForcedAttributeVersionValue()
    {
        StreamConfiguration config = givenConfiguration(uniqueId(), "{item.version}");
        List<Change> changes = detectChanges(this.catalogVersionType, config);
        Assertions.assertThat(changes).containsOnly((Object[])new Change[] {added((ItemModel)this.catalogVersionOne), added((ItemModel)this.catalogVersionTwo)});
        Set<String> allVersions = (Set<String>)changes.stream().map(cv -> cv.getChangeDTO().getVersionValue()).collect(Collectors.toSet());
        Assertions.assertThat(allVersions).containsOnly((Object[])new String[] {"CATALOG_VERSION_ONE", "CATALOG_VERSION_TWO"});
    }


    @Test
    public void shouldDetectNewItemsWithForcedComputedVersionValue()
    {
        StreamConfiguration config = givenConfiguration(uniqueId(), "{{select CONCAT(count(*), '') from {CatalogVersion as cv} where {cv.catalog}=?catalog}}").withParameters(
                        (Map)ImmutableMap.of("catalog", this.catalog));
        List<Change> changes = detectChanges(this.catalogVersionType, config);
        Assertions.assertThat(changes).containsOnly((Object[])new Change[] {added((ItemModel)this.catalogVersionOne), added((ItemModel)this.catalogVersionTwo)});
        Assertions.assertThat(((Change)changes.get(0)).getChangeDTO().getVersionValue()).isEqualTo("2");
        Assertions.assertThat(((Change)changes.get(1)).getChangeDTO().getVersionValue()).isEqualTo("2");
    }


    @Test
    public void shouldNotDetectModifiedItemsWithForcedScalarVersionValue()
    {
        StreamConfiguration config = givenConfiguration(uniqueId(), "'GIVEN_VERSION_VALUE'");
        List<Change> changesToConsume = detectChanges(this.catalogVersionType, config);
        consumeChanges(changesToConsume);
        this.catalogVersionOne.setCategorySystemID("CHANGED");
        this.modelService.save(this.catalogVersionOne);
        List<Change> changes = detectChanges(this.catalogVersionType, config);
        Assertions.assertThat(changes).isEmpty();
    }


    @Test
    public void shouldNotDetectModifiedItemsWithForcedAttributeVersionValue()
    {
        StreamConfiguration config = givenConfiguration(uniqueId(), "{item.version}");
        List<Change> changesToConsume = detectChanges(this.catalogVersionType, config);
        consumeChanges(changesToConsume);
        this.catalogVersionOne.setCategorySystemID("CHANGED");
        this.modelService.save(this.catalogVersionOne);
        List<Change> changes = detectChanges(this.catalogVersionType, config);
        Assertions.assertThat(changes).isEmpty();
    }


    @Test
    public void shouldNotDetectModifiedItemsWithForcedComputedVersionValue()
    {
        StreamConfiguration config = givenConfiguration(uniqueId(), "{{select count(*) || '' from {CatalogVersion as cv} where {cv.catalog}=?catalog}}").withParameters(
                        (Map)ImmutableMap.of("catalog", this.catalog));
        List<Change> changesToConsume = detectChanges(this.catalogVersionType, config);
        consumeChanges(changesToConsume);
        this.catalogVersionOne.setCategorySystemID("CHANGED");
        this.modelService.save(this.catalogVersionOne);
        List<Change> changes = detectChanges(this.catalogVersionType, config);
        Assertions.assertThat(changes).isEmpty();
    }


    @Test
    public void shouldDetectModifiedItemsWithForcedAttributeVersionValue()
    {
        StreamConfiguration config = givenConfiguration(uniqueId(), "{item.categorySystemID}");
        List<Change> changesToConsume = detectChanges(this.catalogVersionType, config);
        consumeChanges(changesToConsume);
        this.catalogVersionTwo.setCategorySystemID("CHANGED");
        this.modelService.save(this.catalogVersionTwo);
        List<Change> changes = detectChanges(this.catalogVersionType, config);
        Assertions.assertThat(changes).containsOnly((Object[])new Change[] {modified((ItemModel)this.catalogVersionTwo)});
    }


    @Test
    public void shouldDetectModifiedItemsWithForcedComputedVersionValue()
    {
        StreamConfiguration config = givenConfiguration(uniqueId(), "{{select CONCAT(count(*), '') from {CatalogVersion as cv} where {cv.categorySystemID} is null}}").withParameters(
                        (Map)ImmutableMap.of("catalog", this.catalog));
        List<Change> changesToConsume = detectChanges(this.catalogVersionType, config);
        consumeChanges(changesToConsume);
        this.catalogVersionOne.setCategorySystemID("CHANGED");
        this.modelService.save(this.catalogVersionOne);
        List<Change> changes = detectChanges(this.catalogVersionType, config);
        Assertions.assertThat(changes).containsOnly((Object[])new Change[] {modified((ItemModel)this.catalogVersionOne), modified((ItemModel)this.catalogVersionTwo)});
        Assertions.assertThat(((Change)changes.get(0)).getChangeDTO().getVersionValue()).isEqualTo("1");
        Assertions.assertThat(((Change)changes.get(1)).getChangeDTO().getVersionValue()).isEqualTo("1");
    }


    @Test
    public void shouldDetectOnlyModifiedItemsByForcedVersion()
    {
        StreamConfiguration config = givenConfiguration(uniqueId(), "{item.categorySystemID}");
        List<Change> changesToConsume = detectChanges(this.catalogVersionType, config);
        consumeChanges(changesToConsume);
        this.catalogVersionOne.setActive(Boolean.valueOf(!this.catalogVersionOne.getActive().booleanValue()));
        this.catalogVersionTwo.setCategorySystemID("CHANGED");
        this.modelService.save(this.catalogVersionTwo);
        List<Change> changes = detectChanges(this.catalogVersionType, config);
        Assertions.assertThat(changes).containsOnly((Object[])new Change[] {modified((ItemModel)this.catalogVersionTwo)});
    }


    @Test
    public void shouldDetectRemovalOfItemWithForcedVersion()
    {
        StreamConfiguration config = givenConfiguration(uniqueId(), "{item.categorySystemID}");
        List<Change> changesToConsume = detectChanges(this.catalogVersionType, config);
        consumeChanges(changesToConsume);
        this.modelService.remove(this.catalogVersionTwo);
        List<Change> changes = detectChanges(this.catalogVersionType, config);
        Assertions.assertThat(changes).containsOnly((Object[])new Change[] {removed((ItemModel)this.catalogVersionTwo)});
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


    private StreamConfiguration givenConfiguration(String streamId, String versionValue)
    {
        return StreamConfiguration.buildFor(streamId).withVersionValue(versionValue);
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
