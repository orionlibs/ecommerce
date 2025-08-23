package de.hybris.platform.mediaconversion.imagemagick;

import de.hybris.platform.mediaconversion.os.Drain;

class TeeDrain implements Drain
{
    private final Drain drainObject;
    private final StringBuilder output;


    TeeDrain(Drain drain)
    {
        this.drainObject = drain;
        if(this.drainObject == null)
        {
            throw new IllegalArgumentException("Drain to delegate to must not be null.");
        }
        this.output = new StringBuilder();
    }


    public void drain(String line)
    {
        if(line != null)
        {
            this.output.append(line);
            this.output.append('\n');
        }
        this.drainObject.drain(line);
    }


    public String toString()
    {
        return this.output.toString();
    }
}
