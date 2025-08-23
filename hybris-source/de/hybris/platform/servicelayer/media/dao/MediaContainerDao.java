package de.hybris.platform.servicelayer.media.dao;

import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaContextModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import java.util.List;

public interface MediaContainerDao extends Dao
{
    List<MediaContainerModel> findMediaContainersByQualifier(String paramString);


    List<MediaContextModel> findMediaContextByQualifier(String paramString);
}
