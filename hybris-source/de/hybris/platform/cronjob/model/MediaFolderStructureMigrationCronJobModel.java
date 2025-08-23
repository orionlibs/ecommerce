package de.hybris.platform.cronjob.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class MediaFolderStructureMigrationCronJobModel extends CronJobModel
{
    public static final String _TYPECODE = "MediaFolderStructureMigrationCronJob";
    public static final String MEDIAFOLDER = "mediaFolder";


    public MediaFolderStructureMigrationCronJobModel()
    {
    }


    public MediaFolderStructureMigrationCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public MediaFolderStructureMigrationCronJobModel(JobModel _job, MediaFolderModel _mediaFolder)
    {
        setJob(_job);
        setMediaFolder(_mediaFolder);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public MediaFolderStructureMigrationCronJobModel(JobModel _job, MediaFolderModel _mediaFolder, ItemModel _owner)
    {
        setJob(_job);
        setMediaFolder(_mediaFolder);
        setOwner(_owner);
    }


    @Accessor(qualifier = "mediaFolder", type = Accessor.Type.GETTER)
    public MediaFolderModel getMediaFolder()
    {
        return (MediaFolderModel)getPersistenceContext().getPropertyValue("mediaFolder");
    }


    @Accessor(qualifier = "mediaFolder", type = Accessor.Type.SETTER)
    public void setMediaFolder(MediaFolderModel value)
    {
        getPersistenceContext().setPropertyValue("mediaFolder", value);
    }
}
