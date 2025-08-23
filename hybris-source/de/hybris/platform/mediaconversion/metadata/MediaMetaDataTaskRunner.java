package de.hybris.platform.mediaconversion.metadata;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.MediaMetaDataService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;
import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class MediaMetaDataTaskRunner implements TaskRunner<TaskModel>
{
    private static final Logger LOG = Logger.getLogger(MediaMetaDataTaskRunner.class);
    public static final String BEAN_NAME = "mediaMetaDataTaskRunner";
    private MediaMetaDataService mediaMetaDataService;
    private ModelService modelService;
    private FlexibleSearchService flexibleSearchService;


    public static TaskModel create(MediaModel model)
    {
        TaskModel task = new TaskModel();
        task.setRunnerBean("mediaMetaDataTaskRunner");
        task.setContext(new MediaKey(model));
        return task;
    }


    public void handleError(TaskService service, TaskModel task, Throwable fault)
    {
        LOG.error("Failed to extract metadata from '" + task.getContextItem() + "'.", fault);
    }


    public void run(TaskService service, TaskModel task) throws RetryLaterException
    {
        getMediaMetaDataService().extractAllMetaData(toMediaModel(task.getContext()));
    }


    protected MediaModel toMediaModel(Object obj)
    {
        if(obj instanceof MediaKey)
        {
            return queryMedia((MediaKey)obj);
        }
        if(obj instanceof MediaModel)
        {
            return (MediaModel)obj;
        }
        if(obj instanceof Long)
        {
            return toMediaModel(PK.fromLong(((Long)obj).longValue()));
        }
        if(obj instanceof PK)
        {
            return toMediaModel(getModelService().get((PK)obj));
        }
        throw new IllegalArgumentException("Invalid task context '" + obj + "'. Cannot be converted to media model.");
    }


    private MediaModel queryMedia(MediaKey key)
    {
        try
        {
            Map<String, Object> params = new TreeMap<>();
            params.put("code", key.getCode());
            params.put("catVers", key.getCatalogVersion());
            FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {pk} FROM {Media} WHERE {code} = ?code AND {catalogVersion} = ?catVers", params);
            return (MediaModel)getFlexibleSearchService().searchUnique(query);
        }
        catch(ModelNotFoundException e)
        {
            throw new IllegalStateException("Media '" + key.getCode() + "' in catalog version '" + key.getCatalogVersion() + "' to extract metadata from disappeared.", e);
        }
        catch(AmbiguousIdentifierException e)
        {
            throw new IllegalStateException("Multiple medias for code  '" + key.getCode() + "' and catalog version '" + key
                            .getCatalogVersion() + "' found.", e);
        }
    }


    public MediaMetaDataService getMediaMetaDataService()
    {
        return this.mediaMetaDataService;
    }


    @Required
    public void setMediaMetaDataService(MediaMetaDataService mediaMetaDataService)
    {
        this.mediaMetaDataService = mediaMetaDataService;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
