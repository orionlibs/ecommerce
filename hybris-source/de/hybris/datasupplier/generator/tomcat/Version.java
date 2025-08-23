package de.hybris.datasupplier.generator.tomcat;

public class Version
{
    public static final Version VERSION = new Version();
    private static final String FIXED_VERSION = "7.10.22.2";
    private int invocation = 0;


    public String getVersion()
    {
        return "7.10.22.2";
    }


    public String getInvocation()
    {
        synchronized(VERSION)
        {
            this.invocation++;
        }
        return Integer.toString(this.invocation);
    }


    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Version ");
        sb.append(getVersion());
        sb.append(" invocations [");
        sb.append(getInvocation());
        sb.append("]");
        return sb.toString();
    }
}
