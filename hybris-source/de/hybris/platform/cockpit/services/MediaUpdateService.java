package de.hybris.platform.cockpit.services;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.impl.MediaEditorSectionConfiguration;
import org.zkoss.util.media.Media;

public interface MediaUpdateService
{
    TypedObject updateMediaBinaryStream(TypedObject paramTypedObject, Media paramMedia);


    TypedObject updateMediaBinaryStream(TypedObject paramTypedObject, byte[] paramArrayOfbyte, String paramString1, String paramString2);


    MediaEditorSectionConfiguration.MediaContent retrieveMediaBinaryStream(TypedObject paramTypedObject);


    TypedObject removeCurrentImageStream(TypedObject paramTypedObject);
}
