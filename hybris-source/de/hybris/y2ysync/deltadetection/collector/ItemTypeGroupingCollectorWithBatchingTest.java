package de.hybris.y2ysync.deltadetection.collector;

import com.google.common.collect.Lists;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.deltadetection.ChangesCollectorFactory;
import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.deltadetection.enums.ChangeType;
import java.util.Date;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Test;

@UnitTest
public class ItemTypeGroupingCollectorWithBatchingTest
{
    private static final String PRODUCT = "Product";
    private static final String CATEGORY = "Category";
    private static final String VARIANT_PRODUCT = "VariantProduct";


    @Test
    public void shouldGroupChangesByItemType()
    {
        TestBatchingCollector productsCollector = new TestBatchingCollector(this, "Product");
        TestBatchingCollector categoriesCollector = new TestBatchingCollector(this, "Category");
        TestBatchingCollector productVariantsCollector = new TestBatchingCollector(this, "VariantProduct");
        List<TestBatchingCollector> collectorsPool = Lists.newArrayList((Object[])new TestBatchingCollector[] {productsCollector, categoriesCollector, productVariantsCollector});
        ItemTypeGroupingCollectorWithBatching itemTypeGroupingCollectorWithBatching = new ItemTypeGroupingCollectorWithBatching((ChangesCollectorFactory)new TestCollectorFactory(this, collectorsPool));
        itemTypeGroupingCollectorWithBatching.collect(dto(1L, "Product"));
        itemTypeGroupingCollectorWithBatching.collect(dto(2L, "Product"));
        itemTypeGroupingCollectorWithBatching.collect(dto(11L, "Category"));
        itemTypeGroupingCollectorWithBatching.collect(dto(112L, "VariantProduct"));
        itemTypeGroupingCollectorWithBatching.collect(dto(12L, "Category"));
        itemTypeGroupingCollectorWithBatching.collect(dto(3L, "Product"));
        Assertions.assertThat(productsCollector.getCollectedChanges()).hasSize(3);
        Assertions.assertThat(categoriesCollector.getCollectedChanges()).hasSize(2);
        Assertions.assertThat(productVariantsCollector.getCollectedChanges()).hasSize(1);
    }


    private ItemChangeDTO dto(long pk, String typeCode)
    {
        return new ItemChangeDTO(Long.valueOf(pk), new Date(), ChangeType.MODIFIED, "", typeCode, "ProductStream");
    }
}
