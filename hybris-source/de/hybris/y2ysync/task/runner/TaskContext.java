package de.hybris.y2ysync.task.runner;

import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.y2ysync.enums.Y2YSyncType;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.util.SerializationUtils;

public class TaskContext
{
    private static final String MEDIA_PK_KEY = "mediaPK";
    private static final String SYNC_EXECUTION_ID_KEY = "syncExecutionID";
    private static final String CONDITION_NAME_KEY = "conditionName";
    private static final String IMPEX_HEADER_KEY = "impexHeader";
    private static final String TYPE_CODE_KEY = "typeCode";
    private static final String DATAHUB_COLUMNS_KEY = "dataHubColumns";
    private static final String SYNC_TYPE_KEY = "syncType";
    private static final String DATAHUB_TYPE_KEY = "dataHubType";
    private final Map<String, Object> ctx;
    private final ModelService modelService;
    private final MediaService mediaService;


    public TaskContext(ModelService modelService, MediaService mediaService, Map<String, Object> ctx)
    {
        this.ctx = Objects.<Map<String, Object>>requireNonNull(ctx, "ctx can't be null");
        this.modelService = Objects.<ModelService>requireNonNull(modelService, "modelService can't be null");
        this.mediaService = Objects.<MediaService>requireNonNull(mediaService, "mediaService can't be null");
    }


    public String getSyncExecutionId()
    {
        return getRequiredValue(String.class, "syncExecutionID");
    }


    public String getConditionToTrigger()
    {
        return getRequiredValue(String.class, "conditionName");
    }


    public String getImpExHeader()
    {
        return getRequiredValue(String.class, "impexHeader");
    }


    public String getDataHubColumns()
    {
        Optional<String> value = getValue(String.class, "dataHubColumns");
        return value.isPresent() ? value.get() : "";
    }


    public String getTypeCode()
    {
        return getRequiredValue(String.class, "typeCode");
    }


    public List<ItemChangeDTO> getChanges()
    {
        MediaModel media = (MediaModel)this.modelService.get(getMediaPK());
        byte[] data = this.mediaService.getDataFromMedia(media);
        return (List<ItemChangeDTO>)SerializationUtils.deserialize(data);
    }


    public PK getMediaPK()
    {
        return getRequiredValue(PK.class, "mediaPK");
    }


    public Y2YSyncType getSyncType()
    {
        return getRequiredValue(Y2YSyncType.class, "syncType");
    }


    public String getDataHubType()
    {
        Optional<String> value = getValue(String.class, "dataHubType");
        return value.orElse("");
    }


    private <T> T getRequiredValue(Class<T> clazz, String key)
    {
        Optional<T> value = getValue(clazz, key);
        if(!value.isPresent())
        {
            throw new IllegalStateException("Context doesn't contain value for '" + key + "'");
        }
        return value.get();
    }


    private <T> Optional<T> getValue(Class<T> clazz, String key)
    {
        Objects.requireNonNull(key, "key can't be null");
        Object valueObj = this.ctx.get(key);
        if(valueObj == null)
        {
            return Optional.empty();
        }
        try
        {
            T value = (T)valueObj;
            return Optional.of(value);
        }
        catch(ClassCastException e)
        {
            throw new IllegalStateException("Couldn't cast " + valueObj + " to " + clazz, e);
        }
    }
}
