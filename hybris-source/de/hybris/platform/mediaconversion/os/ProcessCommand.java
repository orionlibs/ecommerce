package de.hybris.platform.mediaconversion.os;

import java.io.File;
import java.io.Serializable;

public class ProcessCommand implements Serializable
{
    private static final long serialVersionUID = 368750936166627464L;
    private final String[] command;
    private final String[] environment;
    private final File directory;


    ProcessCommand(String[] command, String[] environment, File directory)
    {
        this.command = command;
        if(this.command == null || this.command.length == 0 || containsNull(this.command))
        {
            throw new IllegalArgumentException("Command array must not be null, empty or contain null entries.");
        }
        this.environment = environment;
        this.directory = directory;
    }


    private static boolean containsNull(String[] array)
    {
        for(String entry : array)
        {
            if(entry == null)
            {
                return true;
            }
        }
        return false;
    }


    public String[] getCommand()
    {
        return this.command;
    }


    public String[] getEnvironment()
    {
        return this.environment;
    }


    public File getDirectory()
    {
        return this.directory;
    }
}
