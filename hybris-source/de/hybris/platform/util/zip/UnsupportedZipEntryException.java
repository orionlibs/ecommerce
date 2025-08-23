package de.hybris.platform.util.zip;

public class UnsupportedZipEntryException extends RuntimeException
{
    public UnsupportedZipEntryException(String entryName)
    {
        super("ZipEntry with name: '" + entryName + "' is not supported!");
    }


    public UnsupportedZipEntryException(Throwable cause)
    {
        super(cause);
    }
}
