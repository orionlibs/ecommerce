package com.hybris.backoffice.solrsearch.setup.impl;

import de.hybris.platform.servicelayer.impex.impl.FileBasedImpExResource;
import java.io.File;

@Deprecated(since = "2105")
public class FileBasedImpExResourceFactory
{
    protected FileBasedImpExResource createFileBasedImpExResource(File file, String encoding)
    {
        return new FileBasedImpExResource(file, encoding);
    }
}
