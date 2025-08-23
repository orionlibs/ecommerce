package de.hybris.platform.admincockpit.jalo.cronjob;

import java.io.File;
import java.util.Collection;

public interface OrphanedFilesHandler
{
    boolean process(Collection<File> paramCollection);
}
