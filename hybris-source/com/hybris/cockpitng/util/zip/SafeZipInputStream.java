/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.zip;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.zip.ZipInputStream;

/**
 * SafeZipInputStream extends ZipInputStream and replaces ZipEntry with SafeZipEntry that mitigates Zip slip
 * vulnerability.
 */
public class SafeZipInputStream extends ZipInputStream
{
    public SafeZipInputStream(final InputStream in)
    {
        super(in);
    }


    public SafeZipInputStream(final InputStream in, final Charset charset)
    {
        super(in, charset);
    }


    /**
     * Reads the next ZIP safe file entry that mitigates Zip slip vulnerability
     * @return the next safe ZIP file entry, or null if there are no more entries
     * @throws IOException
     */
    @Override
    public SafeZipEntry getNextEntry() throws IOException
    {
        return (SafeZipEntry)super.getNextEntry();
    }


    /**
     * Creates a new <code>SafeZipEntry</code> object for the specified
     * entry name.
     *
     * @param name the ZIP file entry name
     * @return the SafeZipEntry just created
     */
    @Override
    protected SafeZipEntry createZipEntry(final String name)
    {
        return new SafeZipEntry(name);
    }
}
