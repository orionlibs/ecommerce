package de.hybris.platform.mediaconversion.os;

import java.io.File;

public class ProcessContext
{
    private final ProcessCommand context;
    private final Drain stdOutput;
    private final Drain stdError;


    public ProcessContext(String[] command, String[] environment, File directory, Drain stdOutput, Drain stdError)
    {
        this(new ProcessCommand(command, environment, directory), stdOutput, stdError);
    }


    public ProcessContext(ProcessCommand command, Drain stdOutput, Drain stdError)
    {
        this.context = command;
        if(this.context == null)
        {
            throw new IllegalArgumentException("ProcessContext must not be null.");
        }
        this.stdError = stdError;
        if(this.stdError == null)
        {
            throw new IllegalArgumentException("StdErr must not be null.");
        }
        this.stdOutput = stdOutput;
        if(this.stdOutput == null)
        {
            throw new IllegalArgumentException("StdOut must not be null.");
        }
    }


    public Drain getStdOutput()
    {
        return this.stdOutput;
    }


    public Drain getStdError()
    {
        return this.stdError;
    }


    public final ProcessCommand getContext()
    {
        return this.context;
    }


    public String[] getCommand()
    {
        return getContext().getCommand();
    }


    public String[] getEnvironment()
    {
        return getContext().getEnvironment();
    }


    public File getDirectory()
    {
        return getContext().getDirectory();
    }
}
