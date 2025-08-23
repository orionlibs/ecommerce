package de.hybris.platform.impex.jalo.imp;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.ImpExMedia;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.CSVWriter;
import java.io.File;
import java.io.IOException;

public interface DumpHandler
{
    void init() throws IOException;


    void switchDump() throws ImpExException;


    void finish(boolean paramBoolean);


    CSVReader getReaderOfLastDump();


    CSVWriter getWriterOfCurrentDump();


    ImpExMedia getDumpAsMedia();


    File getDumpAsFile();


    String getDumpAsString();
}
