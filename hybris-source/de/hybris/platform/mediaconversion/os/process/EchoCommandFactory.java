package de.hybris.platform.mediaconversion.os.process;

import java.util.LinkedList;
import java.util.List;

final class EchoCommandFactory
{
    static String[] buildCommand(String... message)
    {
        List<String> ret = new LinkedList<>();
        if(System.getProperty("os.name").toLowerCase().startsWith("win"))
        {
            ret.add("cmd");
            ret.add("/C");
        }
        ret.add("echo");
        if(message != null)
        {
            for(String msg : message)
            {
                ret.add(msg);
            }
        }
        return ret.<String>toArray(new String[ret.size()]);
    }
}
