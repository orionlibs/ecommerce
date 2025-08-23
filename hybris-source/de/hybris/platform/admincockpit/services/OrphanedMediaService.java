package de.hybris.platform.admincockpit.services;

import java.io.File;
import java.util.Collection;

public interface OrphanedMediaService
{
    OrphanedMediaResult searchForOrphanedMediaFiles(File paramFile);


    OrphanedMediaResult searchForOrphanedMediaFiles(File paramFile, int paramInt);


    boolean processFiles(Collection<File> paramCollection);
}
