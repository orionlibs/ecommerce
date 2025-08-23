package de.hybris.platform.mediaconversion.os.process;

import de.hybris.platform.mediaconversion.os.Drain;

public class StringDrain implements Drain
{
    private final StringBuffer string = new StringBuffer();
    private final boolean appendNewlines;


    public StringDrain()
    {
        this(true);
    }


    public StringDrain(boolean appendNewlines)
    {
        this.appendNewlines = appendNewlines;
    }


    public void drain(String line)
    {
        if(line != null)
        {
            this.string.append(line);
            if(isAppendingNewlines())
            {
                this.string.append('\n');
            }
        }
    }


    public boolean isAppendingNewlines()
    {
        return this.appendNewlines;
    }


    public String toString()
    {
        return this.string.toString();
    }
}
