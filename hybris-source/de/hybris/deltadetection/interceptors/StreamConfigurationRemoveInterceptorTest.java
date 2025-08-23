package de.hybris.deltadetection.interceptors;

import de.hybris.deltadetection.enums.ItemVersionMarkerStatus;
import de.hybris.deltadetection.model.ItemVersionMarkerModel;
import de.hybris.deltadetection.model.StreamConfigurationContainerModel;
import de.hybris.deltadetection.model.StreamConfigurationModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Date;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

public class StreamConfigurationRemoveInterceptorTest extends ServicelayerBaseTest
{
    @Resource
    private ModelService modelService;
    @Resource
    private TypeService typeService;
    private StreamConfigurationContainerModel container;
    private TitleModel title;


    @Before
    public void setUp() throws Exception
    {
        this.container = createContainer();
        this.title = createTitle();
    }


    private StreamConfigurationContainerModel createContainer()
    {
        StreamConfigurationContainerModel container = (StreamConfigurationContainerModel)this.modelService.create(StreamConfigurationContainerModel.class);
        container.setId("TEST_CONTAINER");
        this.modelService.save(container);
        return container;
    }


    private TitleModel createTitle()
    {
        TitleModel title = (TitleModel)this.modelService.create(TitleModel.class);
        title.setCode("test1");
        this.modelService.save(title);
        return title;
    }


    @Test
    public void shouldRemoveCorrespondingItemVersionMarkesUponStreamConfigRemoval() throws Exception
    {
        StreamConfigurationModel configuration = createStreamConfiguration(this.container);
        ItemVersionMarkerModel ivm = createVersionMarker(configuration);
        this.modelService.remove(configuration);
        Thread.sleep(6000L);
        Assertions.assertThat(this.modelService.isRemoved(configuration)).isTrue();
        Assertions.assertThat(this.modelService.isRemoved(ivm)).isTrue();
    }


    private StreamConfigurationModel createStreamConfiguration(StreamConfigurationContainerModel container)
    {
        StreamConfigurationModel config = (StreamConfigurationModel)this.modelService.create(StreamConfigurationModel.class);
        config.setStreamId("TEST_CONFIG");
        config.setItemTypeForStream(this.typeService.getComposedTypeForCode("Title"));
        config.setContainer(container);
        this.modelService.save(config);
        return config;
    }


    private ItemVersionMarkerModel createVersionMarker(StreamConfigurationModel config)
    {
        ItemVersionMarkerModel ivm = (ItemVersionMarkerModel)this.modelService.create(ItemVersionMarkerModel.class);
        ivm.setStreamId(config.getStreamId());
        ivm.setItemComposedType(config.getItemTypeForStream());
        ivm.setStatus(ItemVersionMarkerStatus.ACTIVE);
        ivm.setVersionTS(new Date());
        ivm.setItemPK(this.title.getPk().getLong());
        this.modelService.save(ivm);
        return ivm;
    }
}
