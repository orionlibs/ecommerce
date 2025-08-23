package de.hybris.y2ysync.model;

import com.google.common.collect.ImmutableSet;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.y2ysync.services.SyncConfigService;
import java.util.Collection;
import java.util.Set;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;

@IntegrationTest
public class Y2YStreamConfigurationValidateInterceptorTest extends ServicelayerTransactionalTest
{
    @Resource
    private ModelService modelService;
    @Resource
    private SyncConfigService syncConfigService;
    @Resource
    TypeService typeService;


    @Test
    public void shouldNotPermitColumnsWithAttributesNotFromType()
    {
        Y2YStreamConfigurationContainerModel testContainer = createContainer("testContainer");
        Y2YColumnDefinitionModel y2yColumnDefinition = createColumn("foo",
                        randomAttributeDescriptorForClass(MediaModel.class));
        Y2YStreamConfigurationModel streamConfiguration = this.syncConfigService.createStreamConfiguration(testContainer, "User",
                        (Set)ImmutableSet.of(y2yColumnDefinition));
        try
        {
            this.modelService.save(streamConfiguration);
            Assert.fail();
        }
        catch(ModelSavingException exception)
        {
            Assertions.assertThat(exception.getCause()).isExactlyInstanceOf(InterceptorException.class);
        }
    }


    private Y2YColumnDefinitionModel createColumn(String name, AttributeDescriptorModel attributeDescriptor)
    {
        Y2YColumnDefinitionModel y2yColumnDefinition = (Y2YColumnDefinitionModel)this.modelService.create(Y2YColumnDefinitionModel.class);
        y2yColumnDefinition.setColumnName(name);
        y2yColumnDefinition.setAttributeDescriptor(attributeDescriptor);
        return y2yColumnDefinition;
    }


    private Y2YStreamConfigurationContainerModel createContainer(String id)
    {
        return this.syncConfigService.createStreamConfigurationContainer(id);
    }


    private AttributeDescriptorModel randomAttributeDescriptorForClass(Class clazz)
    {
        ComposedTypeModel composedMedia = this.typeService.getComposedTypeForClass(clazz);
        Collection<AttributeDescriptorModel> attributes = composedMedia.getDeclaredattributedescriptors();
        return attributes.iterator().next();
    }
}
