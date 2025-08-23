package de.hybris.platform.warehousing.labels.service;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;

public interface PrintMediaService
{
    MediaModel getMediaForTemplate(String paramString, BusinessProcessModel paramBusinessProcessModel);


    String generatePopupScriptForMedia(MediaModel paramMediaModel, String paramString1, String paramString2, String paramString3);


    String generateHtmlMediaTemplate(MediaModel paramMediaModel);
}
