package de.hybris.platform.mediaconversion.os.config;

import java.io.File;

final class FileNameExtractor implements NameExtractor<File>
{
    static final NameExtractor<File> INSTANCE = new FileNameExtractor();


    public String extract(File item)
    {
        return item.getName().toLowerCase();
    }
}
