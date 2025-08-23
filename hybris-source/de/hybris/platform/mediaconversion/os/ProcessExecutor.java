package de.hybris.platform.mediaconversion.os;

import java.io.File;
import java.io.IOException;

public interface ProcessExecutor
{
    int execute(String[] paramArrayOfString1, String[] paramArrayOfString2, File paramFile, Drain paramDrain1, Drain paramDrain2) throws IOException;


    int execute(ProcessContext paramProcessContext) throws IOException;


    void quit() throws IOException;
}
