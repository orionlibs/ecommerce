package de.hybris.platform.platformbackoffice.data.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.json.ObjectMapperConfiguration;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.model.ModelService;
import org.springframework.beans.factory.annotation.Required;

public class ItemObjectMapperConfiguration implements ObjectMapperConfiguration
{
    private TypeFacade typeFacade;
    private ModelService modelService;


    @Required
    public void setTypeFacade(TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public ObjectMapper configureObjectMapper(Class<?> objectType, ObjectMapper mapper)
    {
        mapper.setVisibility(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                        .withFieldVisibility(JsonAutoDetect.Visibility.NONE).withGetterVisibility(JsonAutoDetect.Visibility.ANY)
                        .withSetterVisibility(JsonAutoDetect.Visibility.NONE).withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        Object object = new Object(this, "itemObjectModule", Version.unknownVersion());
        object.addKeyDeserializer(AttributeDescriptorModel.class, (KeyDeserializer)new Object(this));
        mapper.addMixIn(MediaModel.class, MediaModelMixin.class);
        mapper.registerModule((Module)object);
        return mapper;
    }
}
