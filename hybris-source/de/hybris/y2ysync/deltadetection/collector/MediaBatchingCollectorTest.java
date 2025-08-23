package de.hybris.y2ysync.deltadetection.collector;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.deltadetection.enums.ChangeType;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.util.SerializationUtils;

@IntegrationTest
public class MediaBatchingCollectorTest extends ServicelayerBaseTest
{
    @Resource
    private ModelService modelService;
    @Resource
    private MediaService mediaService;


    @Test
    public void shouldCreateSeparateMediaForEachBatchOfChangesOfGivenSize()
    {
        MediaBatchingCollector collector = new MediaBatchingCollector("testDeltaMedia", 3, this.modelService, this.mediaService);
        collector.setId("1");
        for(int i = 0; i < 10; i++)
        {
            collector.collect(dto(i));
        }
        collector.finish();
        List<PK> mediaPks = collector.getPksOfBatches();
        Assertions.assertThat(mediaPks).hasSize(4);
        CatalogUnawareMediaModel media0 = (CatalogUnawareMediaModel)this.mediaService.getMedia("testDeltaMedia-1-0");
        CatalogUnawareMediaModel media1 = (CatalogUnawareMediaModel)this.mediaService.getMedia("testDeltaMedia-1-1");
        CatalogUnawareMediaModel media2 = (CatalogUnawareMediaModel)this.mediaService.getMedia("testDeltaMedia-1-2");
        CatalogUnawareMediaModel media3 = (CatalogUnawareMediaModel)this.mediaService.getMedia("testDeltaMedia-1-3");
        Assertions.assertThat((Comparable)media0.getPk()).isEqualTo(mediaPks.get(0));
        Assertions.assertThat((Comparable)media1.getPk()).isEqualTo(mediaPks.get(1));
        Assertions.assertThat((Comparable)media2.getPk()).isEqualTo(mediaPks.get(2));
        Assertions.assertThat((Comparable)media3.getPk()).isEqualTo(mediaPks.get(3));
        byte[] data0 = this.mediaService.getDataFromMedia((MediaModel)media0);
        List<ItemChangeDTO> deserialized0 = (List<ItemChangeDTO>)SerializationUtils.deserialize(data0);
        Assertions.assertThat(deserialized0).hasSize(3);
        Assertions.assertThat(((ItemChangeDTO)deserialized0.get(0)).getItemPK()).isEqualTo(0L);
        Assertions.assertThat(((ItemChangeDTO)deserialized0.get(1)).getItemPK()).isEqualTo(1L);
        Assertions.assertThat(((ItemChangeDTO)deserialized0.get(2)).getItemPK()).isEqualTo(2L);
        byte[] data3 = this.mediaService.getDataFromMedia((MediaModel)media3);
        List<ItemChangeDTO> deserialized3 = (List<ItemChangeDTO>)SerializationUtils.deserialize(data3);
        Assertions.assertThat(deserialized3).hasSize(1);
        Assertions.assertThat(((ItemChangeDTO)deserialized3.get(0)).getItemPK()).isEqualTo(9L);
    }


    private ItemChangeDTO dto(long pk)
    {
        return new ItemChangeDTO(Long.valueOf(pk), new Date(), ChangeType.MODIFIED, "", "Product", "ProductStream");
    }
}
