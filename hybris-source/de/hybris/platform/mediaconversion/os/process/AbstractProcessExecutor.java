package de.hybris.platform.mediaconversion.os.process;

import de.hybris.platform.mediaconversion.os.Drain;
import de.hybris.platform.mediaconversion.os.ProcessContext;
import de.hybris.platform.mediaconversion.os.ProcessExecutor;
import java.io.File;
import java.io.IOException;

public abstract class AbstractProcessExecutor implements ProcessExecutor
{
    public int execute(String[] command, String[] environment, File directory, Drain stdOutput, Drain stdError) throws IOException
    {
        return execute(new ProcessContext(command, environment, directory, stdOutput, stdError));
    }
}
