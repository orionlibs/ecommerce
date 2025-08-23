package de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.services;

import de.hybris.platform.core.model.media.MediaModel;

public interface E2EChangesXmlService
{
    MediaModel getMedia(String paramString);


    String getCode();


    String getNameFile();
}
