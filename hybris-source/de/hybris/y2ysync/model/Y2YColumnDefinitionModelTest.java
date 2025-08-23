package de.hybris.y2ysync.model;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.deltadetection.model.StreamConfigurationContainerModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Collection;
import java.util.UUID;
import javax.annotation.Resource;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

@IntegrationTest
public class Y2YColumnDefinitionModelTest extends ServicelayerBaseTest
{
    @Resource
    private ModelService modelService;
    @Resource
    private TypeService typeService;


    @Test
    public void shouldSaveLongY2YColumnDefinitionAttributes()
    {
        ComposedTypeModel productComposedType = this.typeService.getComposedTypeForClass(ProductModel.class);
        Y2YStreamConfigurationContainerModel container = createContainer();
        Y2YStreamConfigurationModel streamConfiguration = createStreamConfiguration(productComposedType, container);
        Y2YColumnDefinitionModel y2yColumnDefinition = (Y2YColumnDefinitionModel)this.modelService.create(Y2YColumnDefinitionModel.class);
        y2yColumnDefinition.setColumnName(UUID.randomUUID().toString());
        y2yColumnDefinition.setAttributeDescriptor(getAttributeDescriptor(productComposedType));
        y2yColumnDefinition.setStreamConfiguration(streamConfiguration);
        y2yColumnDefinition.setImpexHeader(RandomStringUtils.randomAlphanumeric(10000));
        this.modelService.saveAll(new Object[] {y2yColumnDefinition});
    }


    private AttributeDescriptorModel getAttributeDescriptor(ComposedTypeModel productComposedType)
    {
        Collection<AttributeDescriptorModel> attributes = productComposedType.getDeclaredattributedescriptors();
        return attributes.iterator().next();
    }


    private Y2YStreamConfigurationModel createStreamConfiguration(ComposedTypeModel productComposedType, Y2YStreamConfigurationContainerModel container)
    {
        Y2YStreamConfigurationModel streamConfiguration = (Y2YStreamConfigurationModel)this.modelService.create(Y2YStreamConfigurationModel.class);
        streamConfiguration.setStreamId(UUID.randomUUID().toString());
        streamConfiguration.setItemTypeForStream(productComposedType);
        streamConfiguration.setContainer((StreamConfigurationContainerModel)container);
        this.modelService.save(streamConfiguration);
        return streamConfiguration;
    }


    private Y2YStreamConfigurationContainerModel createContainer()
    {
        Y2YStreamConfigurationContainerModel container = (Y2YStreamConfigurationContainerModel)this.modelService.create(Y2YStreamConfigurationContainerModel.class);
        container.setId(UUID.randomUUID().toString());
        this.modelService.save(container);
        return container;
    }
}
