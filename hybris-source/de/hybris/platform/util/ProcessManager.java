package de.hybris.platform.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ProcessManager
{
    String[] cmdArray;
    File workingDir;
    Runtime runtime;
    Process process;
    private StringBuilder stdBuffer = null;
    private StringBuilder errBuffer = null;


    public ProcessManager(String[] cmdArray, String workingDir)
    {
        this.cmdArray = cmdArray;
        if(workingDir == null)
        {
            this.workingDir = null;
        }
        else
        {
            this.workingDir = new File(workingDir);
        }
        this.runtime = Runtime.getRuntime();
        this.process = null;
    }


    public void start() throws IOException, IllegalArgumentException
    {
        this.process = this.runtime.exec(this.cmdArray, (String[])null, this.workingDir);
    }


    public void waitFor() throws InterruptedException
    {
        if(this.process != null)
        {
            this.process.waitFor();
        }
    }


    public void destroy()
    {
        if(this.process != null)
        {
            this.process.destroy();
        }
    }


    public int exitValue()
    {
        if(this.process != null)
        {
            return this.process.exitValue();
        }
        return -1;
    }


    private static void flushStream(InputStream source, StringBuilder buffer)
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(source));
        try
        {
            String line;
            while((line = reader.readLine()) != null)
            {
                synchronized(buffer)
                {
                    buffer.append(line);
                    buffer.append('\n');
                }
            }
        }
        catch(IOException e)
        {
            buffer.append(e.getMessage());
        }
    }


    private void flushProcessStreams()
    {
        if(this.stdBuffer == null)
        {
            this.stdBuffer = new StringBuilder();
            this.errBuffer = new StringBuilder();
        }
        flushStream(this.process.getInputStream(), this.stdBuffer);
        flushStream(this.process.getErrorStream(), this.errBuffer);
    }


    public String toString()
    {
        StringBuilder buf = new StringBuilder();
        buf.append('[');
        for(int i = 0; i < this.cmdArray.length; i++)
        {
            buf.append(this.cmdArray[i]);
            buf.append(',');
        }
        buf.append("] in [");
        buf.append(this.workingDir);
        buf.append(']');
        if(this.process != null)
        {
            flushProcessStreams();
            buf.append(" std=[");
            buf.append(this.stdBuffer.toString());
            buf.append(']');
            buf.append(" err=[");
            buf.append(this.errBuffer.toString());
            buf.append(']');
        }
        return buf.toString();
    }
}
