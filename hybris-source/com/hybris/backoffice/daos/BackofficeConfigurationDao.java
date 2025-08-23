package com.hybris.backoffice.daos;

import de.hybris.platform.core.model.media.MediaModel;
import java.util.Collection;

public interface BackofficeConfigurationDao
{
    Collection<MediaModel> findMedias(String paramString);
}
