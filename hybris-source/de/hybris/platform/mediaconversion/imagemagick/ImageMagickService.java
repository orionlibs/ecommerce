package de.hybris.platform.mediaconversion.imagemagick;

import java.io.IOException;
import java.util.List;

public interface ImageMagickService
{
    void convert(List<String> paramList) throws IOException;


    String identify(List<String> paramList) throws IOException;
}
