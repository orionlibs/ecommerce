package com.hybris.backoffice.search.setup.impl;

import de.hybris.platform.servicelayer.impex.impl.FileBasedImpExResource;
import java.io.File;

public class FileBasedImpExResourceFactory
{
    public FileBasedImpExResource createFileBasedImpExResource(File file, String encoding)
    {
        return new FileBasedImpExResource(file, encoding);
    }
}
