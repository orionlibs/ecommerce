package de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.services;

import de.hybris.platform.core.model.media.MediaModel;

public interface E2EXmlFileService
{
    void copyToE2Efolder(MediaModel paramMediaModel, String paramString);


    String getRootNameFolder();
}
