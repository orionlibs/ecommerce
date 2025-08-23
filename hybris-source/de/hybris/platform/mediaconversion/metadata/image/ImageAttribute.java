package de.hybris.platform.mediaconversion.metadata.image;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.model.MediaMetaDataModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

enum ImageAttribute
{
    height("image", "%h"),
    width("image", "%w"),
    filesize("image", "%b"),
    fileformat("image", "%m"),
    numberOfImages("image", "%n"),
    xResolution("image", "%x"),
    yResolution("image", "%y"),
    colorspace("image", "%[colorspace]"),
    signature("image", "%#"),
    exif("exif", "%[EXIF:*]");
    private static final Logger LOG;
    private static final Pattern EXIF_LINE_PATTERN;
    private final String groupName;
    private final String identificationFormat;

    static
    {
        LOG = Logger.getLogger(ImageAttribute.class);
        EXIF_LINE_PATTERN = Pattern.compile("exif:([^=]+)[=](.+)", 2);
    }

    ImageAttribute(String groupName, String identificationFormat)
    {
        this.groupName = groupName;
        this.identificationFormat = identificationFormat;
    }


    String getIdentificationFormat()
    {
        return this.identificationFormat;
    }


    String getGroupName()
    {
        return this.groupName;
    }


    void createMetaData(ModelService modelservice, MediaModel media, String key, String value)
    {
        MediaMetaDataModel ret = (MediaMetaDataModel)modelservice.create(MediaMetaDataModel.class);
        ret.setCode(key);
        ret.setMedia(media);
        ret.setGroupName(getGroupName());
        ret.setValue(value);
        modelservice.save(ret);
    }


    void parse(ModelService modelService, MediaModel media, BufferedReader bufferedReader) throws IOException
    {
        String value = bufferedReader.readLine();
        if(value == null || value.trim().isEmpty())
        {
            return;
        }
        createMetaData(modelService, media, name(), value.trim());
    }
}
