package de.hybris.platform.cronjob.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class MoveMediaCronJobModel extends CronJobModel
{
    public static final String _TYPECODE = "MoveMediaCronJob";
    public static final String MEDIAS = "medias";
    public static final String TARGETFOLDER = "targetFolder";
    public static final String MOVEDMEDIASCOUNT = "movedMediasCount";


    public MoveMediaCronJobModel()
    {
    }


    public MoveMediaCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public MoveMediaCronJobModel(JobModel _job)
    {
        setJob(_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public MoveMediaCronJobModel(JobModel _job, ItemModel _owner)
    {
        setJob(_job);
        setOwner(_owner);
    }


    @Accessor(qualifier = "medias", type = Accessor.Type.GETTER)
    public Collection<MediaModel> getMedias()
    {
        return (Collection<MediaModel>)getPersistenceContext().getPropertyValue("medias");
    }


    @Accessor(qualifier = "movedMediasCount", type = Accessor.Type.GETTER)
    public Integer getMovedMediasCount()
    {
        return (Integer)getPersistenceContext().getPropertyValue("movedMediasCount");
    }


    @Accessor(qualifier = "targetFolder", type = Accessor.Type.GETTER)
    public MediaFolderModel getTargetFolder()
    {
        return (MediaFolderModel)getPersistenceContext().getPropertyValue("targetFolder");
    }


    @Accessor(qualifier = "medias", type = Accessor.Type.SETTER)
    public void setMedias(Collection<MediaModel> value)
    {
        getPersistenceContext().setPropertyValue("medias", value);
    }


    @Accessor(qualifier = "movedMediasCount", type = Accessor.Type.SETTER)
    public void setMovedMediasCount(Integer value)
    {
        getPersistenceContext().setPropertyValue("movedMediasCount", value);
    }


    @Accessor(qualifier = "targetFolder", type = Accessor.Type.SETTER)
    public void setTargetFolder(MediaFolderModel value)
    {
        getPersistenceContext().setPropertyValue("targetFolder", value);
    }
}
