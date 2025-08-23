package de.hybris.platform.impex.jalo;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public interface ImpExFile
{
    OutputStream getOutputStream();


    String getMimeType();


    File getFile();


    void close() throws IOException;


    String getFileExtension();
}
