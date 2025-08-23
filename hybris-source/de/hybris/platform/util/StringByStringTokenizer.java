package de.hybris.platform.util;

public class StringByStringTokenizer
{
    private final String theString;
    private final String theDelimiter;
    private final boolean isReturningDelimiter;
    private int position;


    public StringByStringTokenizer(String string, String delimiter, boolean returnDelimiter)
    {
        this.theString = string;
        this.theDelimiter = delimiter;
        this.isReturningDelimiter = returnDelimiter;
        this.position = 0;
        if(!this.isReturningDelimiter)
        {
            if(string.startsWith(delimiter))
            {
                this.position = delimiter.length();
            }
        }
    }


    public boolean hasMoreTokens()
    {
        if(this.theString.length() > this.position)
        {
            if(this.isReturningDelimiter)
            {
                return true;
            }
            int currentPosition = this.position;
            while(this.theString.startsWith(this.theDelimiter, currentPosition))
            {
                currentPosition += this.theDelimiter.length();
            }
            return (currentPosition != this.theString.length());
        }
        return false;
    }


    public String nextToken()
    {
        while(this.theString.startsWith(this.theDelimiter, this.position))
        {
            this.position += this.theDelimiter.length();
            if(this.isReturningDelimiter)
            {
                return this.theDelimiter;
            }
        }
        int newPosition = this.theString.indexOf(this.theDelimiter, this.position);
        if(newPosition == -1)
        {
            String str = this.theString.substring(this.position);
            this.position = this.theString.length();
            return str;
        }
        String result = this.theString.substring(this.position, newPosition);
        this.position = newPosition;
        return result;
    }
}
