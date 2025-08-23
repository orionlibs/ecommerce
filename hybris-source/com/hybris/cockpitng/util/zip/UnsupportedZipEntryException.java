/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.zip;

/**
 * Thrown in case zip entries with names that target outside the directory appears (so called Zip Slip vulnerability)
 */
public class UnsupportedZipEntryException extends RuntimeException
{
    public UnsupportedZipEntryException(final String entryName)
    {
        super("ZipEntry with name: '" + entryName + "' is not supported!");
    }


    public UnsupportedZipEntryException(final Throwable cause)
    {
        super(cause);
    }
}
