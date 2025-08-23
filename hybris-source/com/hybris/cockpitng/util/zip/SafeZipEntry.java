/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.zip;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.zip.ZipEntry;
import org.apache.commons.io.FileUtils;

/**
 * SafeZipEntry class that prevents Zip Slip vulnerability by disallowing ZipEntries with names that target outside
 * directory, eg. ../foo.txt. In such cases com.hybris.cockpitng.util.zip.UnsupportedZipEntryException is thrown.
 */
public class SafeZipEntry extends ZipEntry
{
    public SafeZipEntry(final String name)
    {
        super(name);
    }


    public SafeZipEntry(final ZipEntry e)
    {
        super(e);
    }


    @Override
    public String getName()
    {
        final String name = super.getName();
        if(isNotEscapingDirectory(Paths.get(name)))
        {
            return name;
        }
        throw new UnsupportedZipEntryException(name);
    }


    private boolean isNotEscapingDirectory(final Path path)
    {
        final File tmpDir = new File(FileUtils.getTempDirectoryPath());
        final Path testRootDirectory = tmpDir.toPath().resolve(UUID.randomUUID().toString()).toAbsolutePath();
        final Path canonicalPath = resolveCanonicalFromRoot(testRootDirectory, path);
        return canonicalPath.startsWith(testRootDirectory);
    }


    private Path resolveCanonicalFromRoot(final Path root, final Path testPath)
    {
        try
        {
            return root.resolve(testPath).toFile().getCanonicalFile().toPath();
        }
        catch(final IOException e)
        {
            throw new UnsupportedZipEntryException(e);
        }
    }
}