package de.hybris.platform.servicelayer.media.impl;

import java.util.Collection;
import java.util.Map;

public interface OrphanedMediaDao extends MediaDao
{
    Collection findOrphanedMedias(int paramInt1, int paramInt2, Map<String, Object> paramMap);


    int getMediasCount();
}
