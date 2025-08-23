package de.hybris.platform.util;

import java.text.DecimalFormat;
import org.apache.log4j.Logger;

public class StopWatch
{
    private static final Logger LOG = Logger.getLogger(StopWatch.class.getName());
    private final String text;
    private final long startTime;
    private long pauseAtTime;
    private long pausedTotal;
    private final String indentStr;
    private boolean pausing = false;
    private static int indentCounter = 0;


    public StopWatch(String text)
    {
        this.indentStr = getIndentString(getIndentCounter());
        this.text = this.indentStr + this.indentStr;
        this.startTime = System.nanoTime();
        this.pauseAtTime = 0L;
        LOG.info(this.text);
    }


    private static void increase()
    {
        indentCounter++;
    }


    private static void decrease()
    {
        indentCounter--;
    }


    private static int getIndentCounter()
    {
        int i = indentCounter;
        increase();
        return i;
    }


    private static String getIndentString(int count)
    {
        String indentStr = "";
        for(int i = 0; i < count; i++)
        {
            indentStr = indentStr + "   ";
        }
        return indentStr;
    }


    public long stop()
    {
        decrease();
        if(this.pausing)
        {
            restart();
        }
        long stopTime = System.nanoTime() - this.startTime;
        stopTime -= this.pausedTotal;
        LOG.info(" " + this.indentStr + stopTime / 1000.0D / 1000.0D + "ms");
        return stopTime;
    }


    public String stop(DecimalFormat df)
    {
        decrease();
        if(this.pausing)
        {
            restart();
        }
        long stopTime = System.nanoTime() - this.startTime;
        stopTime -= this.pausedTotal;
        return "Stopwatch " + this.text + " :" + df.format(stopTime / 1000.0D / 1000.0D) + "ms \n <br>";
    }


    public void pause()
    {
        if(!this.pausing)
        {
            this.pauseAtTime = System.nanoTime();
            this.pausing = true;
        }
    }


    public void restart()
    {
        if(this.pausing)
        {
            this.pausedTotal += System.nanoTime() - this.pauseAtTime;
            this.pausing = false;
        }
    }
}
