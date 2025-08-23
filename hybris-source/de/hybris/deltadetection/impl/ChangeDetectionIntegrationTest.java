package de.hybris.deltadetection.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.deltadetection.ChangeDetectionService;
import de.hybris.deltadetection.ChangesCollector;
import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.deltadetection.enums.ChangeType;
import de.hybris.deltadetection.enums.ItemVersionMarkerStatus;
import de.hybris.deltadetection.model.ItemVersionMarkerModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.directpersistence.JaloAccessorsService;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.util.persistence.PersistenceUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@IntegrationTest
public class ChangeDetectionIntegrationTest extends ServicelayerBaseTest
{
    private static final Logger LOG = LoggerFactory.getLogger(ChangeDetectionIntegrationTest.class);
    @Resource
    private ChangeDetectionService changeDetectionService;
    @Resource
    private ModelService modelService;
    @Resource
    private TypeService typeService;
    @Resource
    private JaloAccessorsService jaloAccessorsService;
    private InMemoryChangesCollector inMemoryChangesCollector;
    private CustomerModel testCustomerJan;
    private CustomerModel testCustomerPiotr;
    private TitleModel testTitleFoo;
    private TitleModel testTitleBar;
    @Resource
    private FlexibleSearchService flexibleSearchService;
    private static final String STREAM_ID_XXX = "FeedXXX";
    private static final String STREAM_ID_YYY = "FeedYYY";


    @Before
    public void setUp() throws Exception
    {
        this.inMemoryChangesCollector = new InMemoryChangesCollector();
        this.testCustomerJan = prepareCustomer("Jan", "Jan C.");
        this.testCustomerPiotr = prepareCustomer("Piotr", "Piotr H.");
        this.testTitleFoo = (TitleModel)this.modelService.create(TitleModel.class);
        this.testTitleFoo.setCode("Foo");
        this.testTitleBar = (TitleModel)this.modelService.create(TitleModel.class);
        this.testTitleBar.setCode("Bar");
    }


    @Test
    public void shouldTreatItemVersionMarkerAsSLDSafeForWriting()
    {
        boolean isSLDSafe = ((Boolean)PersistenceUtils.doWithSLDPersistence(() -> Boolean.valueOf(this.jaloAccessorsService.isSLDSafeForWrite("ItemVersionMarker")))).booleanValue();
        Assertions.assertThat(isSLDSafe).isTrue();
    }


    @Test
    public void testFindChangesForNewItem() throws Exception
    {
        this.modelService.save(this.testCustomerJan);
        ItemChangeDTO change = this.changeDetectionService.getChangeForExistingItem((ItemModel)this.testCustomerJan, "FeedXXX");
        Assertions.assertThat(change).isNotNull();
        Assertions.assertThat(change.getItemPK()).isEqualTo(this.testCustomerJan.getPk().getLong());
        Assertions.assertThat((Comparable)change.getChangeType()).isEqualTo(ChangeType.NEW);
        Assertions.assertThat(change.getVersion()).isEqualTo(this.testCustomerJan.getModifiedtime());
    }


    @Test
    public void testFindChangesForModifiedItem() throws Exception
    {
        this.modelService.save(this.testCustomerJan);
        saveVersionMarker(this.testCustomerJan.getPk(), this.testCustomerJan.getModifiedtime(), "New item created(" + this.testCustomerJan
                        .toString() + ")", this.typeService
                        .getComposedTypeForCode(this.testCustomerJan.getItemtype()), "FeedXXX");
        Date markerVersion = this.testCustomerJan.getModifiedtime();
        Thread.sleep(2000L);
        this.testCustomerJan.setName("Jan is changed now");
        this.modelService.save(this.testCustomerJan);
        ItemChangeDTO change = this.changeDetectionService.getChangeForExistingItem((ItemModel)this.testCustomerJan, "FeedXXX");
        Assertions.assertThat(change).isNotNull();
        Assertions.assertThat(change.getItemPK()).isEqualTo(this.testCustomerJan.getPk().getLong());
        Assertions.assertThat((Comparable)change.getChangeType()).isEqualTo(ChangeType.MODIFIED);
        Assertions.assertThat(change.getVersion()).isEqualTo(this.testCustomerJan.getModifiedtime());
        Assertions.assertThat(markerVersion.before(change.getVersion())).isTrue();
    }


    @Test
    public void testFindChangesForNotChangedItem() throws Exception
    {
        this.modelService.save(this.testCustomerJan);
        saveVersionMarker(this.testCustomerJan.getPk(), this.testCustomerJan.getModifiedtime(), "New item created(" + this.testCustomerJan
                        .toString() + ")", this.typeService
                        .getComposedTypeForCode(this.testCustomerJan.getItemtype()), "FeedXXX");
        ItemChangeDTO change = this.changeDetectionService.getChangeForExistingItem((ItemModel)this.testCustomerJan, "FeedXXX");
        Assertions.assertThat(change).isNull();
    }


    @Test
    public void testFindChangeForRemovedItem() throws Exception
    {
        this.modelService.saveAll(new Object[] {this.testCustomerJan});
        saveVersionMarker(this.testCustomerJan.getPk(), this.testCustomerJan.getModifiedtime(), "New item created(" + this.testCustomerJan
                        .toString() + ")", this.typeService
                        .getComposedTypeForCode(this.testCustomerJan.getItemtype()), "FeedXXX");
        PK oldPkJan = this.testCustomerJan.getPk();
        Date oldModifiedTimeJan = this.testCustomerJan.getModifiedtime();
        this.modelService.removeAll(new Object[] {this.testCustomerJan});
        ItemChangeDTO change = this.changeDetectionService.getChangeForRemovedItem(oldPkJan, "FeedXXX");
        Assertions.assertThat(change).isNotNull();
        Assertions.assertThat(change.getItemPK()).isEqualTo(oldPkJan.getLong());
        Assertions.assertThat((Comparable)change.getChangeType()).isEqualTo(ChangeType.DELETED);
        Assertions.assertThat(change.getVersion()).isEqualTo(oldModifiedTimeJan);
    }


    @Test
    public void testFindChangesForRemovedItems() throws Exception
    {
        this.modelService.saveAll(new Object[] {this.testCustomerJan, this.testCustomerPiotr, this.testTitleFoo, this.testTitleBar});
        ComposedTypeModel composedTypeCustomer = this.typeService.getComposedTypeForClass(CustomerModel.class);
        ComposedTypeModel composedTypeTitle = this.typeService.getComposedTypeForClass(TitleModel.class);
        saveVersionMarker(this.testCustomerJan.getPk(), this.testCustomerJan.getModifiedtime(), "New item created(" + this.testCustomerJan
                        .toString() + ")", composedTypeCustomer, "FeedXXX");
        saveVersionMarker(this.testCustomerPiotr.getPk(), this.testCustomerPiotr.getModifiedtime(), "New item created(" + this.testCustomerPiotr
                        .toString() + ")", composedTypeCustomer, "FeedXXX");
        saveVersionMarker(this.testTitleFoo.getPk(), this.testTitleFoo.getModifiedtime(), "New item created(" + this.testTitleFoo
                        .toString() + ")", composedTypeTitle, "FeedXXX");
        saveVersionMarker(this.testTitleBar.getPk(), this.testTitleBar.getModifiedtime(), "New item created(" + this.testTitleBar
                        .toString() + ")", composedTypeTitle, "FeedXXX");
        PK oldPkJan = this.testCustomerJan.getPk();
        PK oldPkPiotr = this.testCustomerPiotr.getPk();
        PK oldPkTitleFoo = this.testTitleFoo.getPk();
        this.modelService.removeAll(new Object[] {this.testCustomerJan, this.testCustomerPiotr, this.testTitleFoo});
        List<ItemChangeDTO> changes = this.changeDetectionService.getChangesForRemovedItems("FeedXXX");
        Assertions.assertThat(changes).hasSize(3);
        Assertions.assertThat(changes).extracting("itemPK").containsOnly(new Object[] {oldPkJan.getLong(), oldPkPiotr.getLong(), oldPkTitleFoo.getLong()});
    }


    @Test
    public void testConsumeChangesForOneNewItem() throws Exception
    {
        this.modelService.saveAll(new Object[] {this.testCustomerJan});
        ItemChangeDTO change = this.changeDetectionService.getChangeForExistingItem((ItemModel)this.testCustomerJan, "FeedXXX");
        Assertions.assertThat(change).isNotNull();
        this.changeDetectionService.consumeChanges(Arrays.asList(new ItemChangeDTO[] {change}));
        change = this.changeDetectionService.getChangeForExistingItem((ItemModel)this.testCustomerJan, "FeedXXX");
        Assertions.assertThat(change).isNull();
    }


    @Test
    public void testConsumeChangesConcurrently() throws Exception
    {
        int numberOfChanges = 20;
        ComposedTypeModel composedTypeTitle = this.typeService.getComposedTypeForClass(TitleModel.class);
        List<ItemChangeDTO> changesA = new ArrayList<>(20);
        List<ItemChangeDTO> changesB = new ArrayList<>(20);
        for(int i = 0; i < 20; i++)
        {
            TitleModel testTitle = (TitleModel)this.modelService.create(TitleModel.class);
            testTitle.setCode("Mr " + Integer.toString(i));
            this.modelService.save(testTitle);
            saveVersionMarker(testTitle.getPk(), testTitle.getModifiedtime(), "item #" + Integer.toString(i), composedTypeTitle, "FeedXXX");
            testTitle.setName("test name " + Integer.toString(i));
            Thread.sleep(1000L);
            this.modelService.save(testTitle);
            ItemChangeDTO changesTitle = this.changeDetectionService.getChangeForExistingItem((ItemModel)testTitle, "FeedXXX");
            changesA.add(changesTitle);
            changesB.add(0, changesTitle);
        }
        Object object = new Object(this, 2, true, changesA, changesB);
        object.startAll();
        Assertions.assertThat(object.waitAndDestroy(60L)).isTrue();
        Assertions.assertThat(object.getErrors()).isEqualTo(Collections.emptyMap());
        for(ItemChangeDTO c : changesA)
        {
            List<ItemVersionMarkerModel> markers = lookupChangeFor(PK.fromLong(c.getItemPK().longValue()));
            Assertions.assertThat(markers.size()).isEqualTo(1);
        }
    }


    protected List<ItemVersionMarkerModel> lookupChangeFor(PK pk)
    {
        SearchResult<ItemVersionMarkerModel> sr = this.flexibleSearchService.search("SELECT {PK} FROM {ItemVersionMarker} WHERE {itemPK}=?pk", Collections.singletonMap("pk", pk));
        return sr.getResult();
    }


    @Test
    public void testConsumeChangesForDifferentChangeTypesAndDifferentItemTypes() throws Exception
    {
        ComposedTypeModel composedTypeCustomer = this.typeService.getComposedTypeForClass(CustomerModel.class);
        ComposedTypeModel composedTypeTitle = this.typeService.getComposedTypeForClass(TitleModel.class);
        this.modelService.saveAll(new Object[] {this.testCustomerJan, this.testCustomerPiotr, this.testTitleFoo, this.testTitleBar});
        saveVersionMarker(this.testCustomerPiotr.getPk(), this.testCustomerPiotr.getModifiedtime(), "New item created(" + this.testCustomerPiotr
                        .toString() + ")", composedTypeCustomer, "FeedXXX");
        Thread.sleep(2000L);
        this.testCustomerPiotr.setName("Piotr is changed now");
        this.modelService.save(this.testCustomerPiotr);
        saveVersionMarker(this.testTitleFoo.getPk(), this.testTitleFoo.getModifiedtime(), "New item created(" + this.testTitleFoo
                        .toString() + ")", composedTypeTitle, "FeedXXX");
        PK oldPkTitleFoo = this.testTitleFoo.getPk();
        this.modelService.remove(this.testTitleFoo);
        saveVersionMarker(this.testTitleBar.getPk(), this.testTitleBar.getModifiedtime(), "New item created(" + this.testTitleBar
                        .toString() + ")", composedTypeTitle, "FeedXXX");
        ItemChangeDTO changesJan;
        Assertions.assertThat(changesJan = this.changeDetectionService.getChangeForExistingItem((ItemModel)this.testCustomerJan, "FeedXXX")).isNotNull();
        ItemChangeDTO changesPiotr = this.changeDetectionService.getChangeForExistingItem((ItemModel)this.testCustomerPiotr, "FeedXXX");
        Assertions.assertThat(changesPiotr).isNotNull();
        ItemChangeDTO changesFoo;
        Assertions.assertThat(changesFoo = this.changeDetectionService.getChangeForRemovedItem(oldPkTitleFoo, "FeedXXX")).isNotNull();
        ItemChangeDTO changesBar;
        Assertions.assertThat(changesBar = this.changeDetectionService.getChangeForExistingItem((ItemModel)this.testTitleBar, "FeedXXX")).isNull();
        this.changeDetectionService.consumeChanges(Arrays.asList(new ItemChangeDTO[] {changesJan, changesPiotr, changesFoo, changesBar}));
        this.modelService.detachAll();
        this.testCustomerJan = (CustomerModel)this.modelService.get(this.testCustomerJan.getPk());
        this.testCustomerPiotr = (CustomerModel)this.modelService.get(this.testCustomerPiotr.getPk());
        this.testTitleBar = (TitleModel)this.modelService.get(this.testTitleBar.getPk());
        Assertions.assertThat(this.changeDetectionService.getChangeForExistingItem((ItemModel)this.testCustomerJan, "FeedXXX")).isNull();
        Assertions.assertThat(this.changeDetectionService.getChangeForExistingItem((ItemModel)this.testCustomerPiotr, "FeedXXX")).isNull();
        Assertions.assertThat(this.changeDetectionService.getChangeForRemovedItem(oldPkTitleFoo, "FeedXXX")).isNull();
        Assertions.assertThat(this.changeDetectionService.getChangeForExistingItem((ItemModel)this.testTitleBar, "FeedXXX")).isNull();
    }


    @Test
    public void testFindChangesByType() throws Exception
    {
        ComposedTypeModel composedTypeCustomer = this.typeService.getComposedTypeForClass(CustomerModel.class);
        ComposedTypeModel composedTypeTitle = this.typeService.getComposedTypeForClass(TitleModel.class);
        this.modelService.saveAll(new Object[] {this.testCustomerJan, this.testCustomerPiotr, this.testTitleFoo, this.testTitleBar});
        saveVersionMarker(this.testCustomerPiotr.getPk(), this.testCustomerPiotr.getModifiedtime(), "New item created(" + this.testCustomerPiotr
                        .toString() + ")", composedTypeCustomer, "FeedXXX");
        Thread.sleep(2000L);
        this.testCustomerPiotr.setName("Piotr is changed now");
        this.modelService.save(this.testCustomerPiotr);
        saveVersionMarker(this.testTitleFoo.getPk(), this.testTitleFoo.getModifiedtime(), "New item created(" + this.testTitleFoo
                        .toString() + ")", composedTypeTitle, "FeedXXX");
        PK oldPkTitleFoo = this.testTitleFoo.getPk();
        this.modelService.remove(this.testTitleFoo);
        this.changeDetectionService.collectChangesForType(composedTypeTitle, "FeedXXX", (ChangesCollector)this.inMemoryChangesCollector);
        List<ItemChangeDTO> changes = this.inMemoryChangesCollector.getChanges();
        Assertions.assertThat(changes).hasSize(2);
        Assertions.assertThat(changes).extracting("itemPK").containsOnly(new Object[] {oldPkTitleFoo.getLong(), this.testTitleBar.getPk().getLong()});
        Assertions.assertThat(changes).extracting("itemComposedType").containsOnly(new Object[] {"Title"});
    }


    @Test
    public void testFindChangesForNewItemStreamAware() throws Exception
    {
        this.modelService.save(this.testCustomerJan);
        ItemChangeDTO changeXXX = this.changeDetectionService.getChangeForExistingItem((ItemModel)this.testCustomerJan, "FeedXXX");
        ItemChangeDTO changeYYY = this.changeDetectionService.getChangeForExistingItem((ItemModel)this.testCustomerJan, "FeedYYY");
        for(ItemChangeDTO change : Arrays.<ItemChangeDTO>asList(new ItemChangeDTO[] {changeXXX, changeYYY}))
        {
            Assertions.assertThat(change).isNotNull();
            Assertions.assertThat(change.getItemPK()).isEqualTo(this.testCustomerJan.getPk().getLong());
            Assertions.assertThat((Comparable)change.getChangeType()).isEqualTo(ChangeType.NEW);
            Assertions.assertThat(change.getVersion()).isEqualTo(this.testCustomerJan.getModifiedtime());
        }
        Assertions.assertThat(changeXXX.getStreamId()).isEqualTo("FeedXXX");
        Assertions.assertThat(changeYYY.getStreamId()).isEqualTo("FeedYYY");
    }


    @Test
    public void testFindChangesForRemovedItemStreamAware() throws Exception
    {
        this.modelService.save(this.testCustomerJan);
        saveVersionMarker(this.testCustomerJan.getPk(), this.testCustomerJan.getModifiedtime(), "New item created(" + this.testCustomerJan
                        .toString() + ")", this.typeService
                        .getComposedTypeForCode(this.testCustomerJan.getItemtype()), "FeedXXX");
        PK oldPkJan = this.testCustomerJan.getPk();
        Date oldModifiedTimeJan = this.testCustomerJan.getModifiedtime();
        this.modelService.removeAll(new Object[] {this.testCustomerJan});
        ItemChangeDTO changeXXX = this.changeDetectionService.getChangeForRemovedItem(oldPkJan, "FeedXXX");
        ItemChangeDTO changeYYY = this.changeDetectionService.getChangeForRemovedItem(oldPkJan, "FeedYYY");
        Assertions.assertThat(changeXXX.getStreamId()).isEqualTo("FeedXXX");
        Assertions.assertThat(changeXXX.getItemPK()).isEqualTo(oldPkJan.getLong());
        Assertions.assertThat((Comparable)changeXXX.getChangeType()).isEqualTo(ChangeType.DELETED);
        Assertions.assertThat(changeXXX.getVersion()).isEqualTo(oldModifiedTimeJan);
        Assertions.assertThat(changeYYY).isNull();
    }


    @Test
    public void testFindChangesForModifiedItemStreamAware() throws Exception
    {
        this.modelService.save(this.testCustomerJan);
        saveVersionMarker(this.testCustomerJan.getPk(), this.testCustomerJan.getModifiedtime(), "New item created(" + this.testCustomerJan
                        .toString() + ")", this.typeService
                        .getComposedTypeForCode(this.testCustomerJan.getItemtype()), "FeedXXX");
        Date markerVersion = this.testCustomerJan.getModifiedtime();
        Thread.sleep(2000L);
        this.testCustomerJan.setName("Jan is changed now");
        this.modelService.save(this.testCustomerJan);
        ItemChangeDTO changeXXX = this.changeDetectionService.getChangeForExistingItem((ItemModel)this.testCustomerJan, "FeedXXX");
        ItemChangeDTO changeYYY = this.changeDetectionService.getChangeForExistingItem((ItemModel)this.testCustomerJan, "FeedYYY");
        for(ItemChangeDTO change : Arrays.<ItemChangeDTO>asList(new ItemChangeDTO[] {changeXXX, changeYYY}))
        {
            Assertions.assertThat(change).isNotNull();
            Assertions.assertThat(change.getItemPK()).isEqualTo(this.testCustomerJan.getPk().getLong());
            Assertions.assertThat(change.getVersion()).isEqualTo(this.testCustomerJan.getModifiedtime());
        }
        Assertions.assertThat(changeXXX.getStreamId()).isEqualTo("FeedXXX");
        Assertions.assertThat((Comparable)changeXXX.getChangeType()).isEqualTo(ChangeType.MODIFIED);
        Assertions.assertThat(markerVersion.before(changeXXX.getVersion())).isTrue();
        Assertions.assertThat(changeYYY.getStreamId()).isEqualTo("FeedYYY");
        Assertions.assertThat((Comparable)changeYYY.getChangeType()).isEqualTo(ChangeType.NEW);
    }


    @Test
    public void testFindChangesByTypeStreamAware() throws Exception
    {
        ComposedTypeModel composedTypeCustomer = this.typeService.getComposedTypeForClass(CustomerModel.class);
        ComposedTypeModel composedTypeTitle = this.typeService.getComposedTypeForClass(TitleModel.class);
        this.modelService.saveAll(new Object[] {this.testCustomerJan, this.testCustomerPiotr, this.testTitleFoo, this.testTitleBar});
        saveVersionMarker(this.testCustomerPiotr.getPk(), this.testCustomerPiotr.getModifiedtime(), "New item created(" + this.testCustomerPiotr
                        .toString() + ")", composedTypeCustomer, "FeedXXX");
        Thread.sleep(2000L);
        this.testCustomerPiotr.setName("Piotr is changed now");
        this.modelService.save(this.testCustomerPiotr);
        saveVersionMarker(this.testTitleFoo.getPk(), this.testTitleFoo.getModifiedtime(), "New item created(" + this.testTitleFoo
                        .toString() + ")", composedTypeTitle, "FeedXXX");
        PK oldPkTitleFoo = this.testTitleFoo.getPk();
        this.modelService.remove(this.testTitleFoo);
        this.changeDetectionService.collectChangesForType(composedTypeTitle, "FeedXXX", (ChangesCollector)this.inMemoryChangesCollector);
        List<ItemChangeDTO> changesXXX = this.inMemoryChangesCollector.getChanges();
        Assertions.assertThat(changesXXX).hasSize(2);
        Assertions.assertThat(changesXXX).extracting("itemPK").containsOnly(new Object[] {oldPkTitleFoo.getLong(), this.testTitleBar.getPk().getLong()});
        Assertions.assertThat(changesXXX).extracting("itemComposedType").containsOnly(new Object[] {"Title"});
        Assertions.assertThat(changesXXX).extracting("streamId").containsOnly(new Object[] {"FeedXXX"});
        this.inMemoryChangesCollector.clearChanges();
        this.changeDetectionService.collectChangesForType(composedTypeTitle, "FeedYYY", (ChangesCollector)this.inMemoryChangesCollector);
        List<ItemChangeDTO> changesYYY = this.inMemoryChangesCollector.getChanges();
        Assertions.assertThat(changesYYY).hasSize(1);
        Assertions.assertThat(changesYYY).extracting("itemPK").containsOnly(new Object[] {this.testTitleBar.getPk().getLong()});
        Assertions.assertThat(changesYYY).extracting("itemComposedType").containsOnly(new Object[] {"Title"});
        Assertions.assertThat(changesYYY).extracting("streamId").containsOnly(new Object[] {"FeedYYY"});
    }


    private ItemVersionMarkerModel saveVersionMarker(PK itemPK, Date version, String info, ComposedTypeModel itemComposedType, String streamID)
    {
        ItemVersionMarkerModel marker = (ItemVersionMarkerModel)this.modelService.create(ItemVersionMarkerModel.class);
        marker.setItemPK(itemPK.getLong());
        marker.setVersionTS(version);
        marker.setInfo(info);
        marker.setItemComposedType(itemComposedType);
        marker.setStreamId(streamID);
        marker.setStatus(ItemVersionMarkerStatus.ACTIVE);
        this.modelService.save(marker);
        return marker;
    }


    private CustomerModel prepareCustomer(String id, String name)
    {
        CustomerModel result = (CustomerModel)this.modelService.create(CustomerModel.class);
        result.setName(name);
        result.setUid(id);
        return result;
    }
}
