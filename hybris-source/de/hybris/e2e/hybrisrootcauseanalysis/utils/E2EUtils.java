package de.hybris.e2e.hybrisrootcauseanalysis.utils;

import java.io.File;
import java.util.Properties;
import org.apache.log4j.Logger;

public class E2EUtils
{
    private static final Logger LOG = Logger.getLogger(E2EUtils.class.getName());


    public static Object isNull(Object obj)
    {
        if(obj == null || "null".equals(obj))
        {
            return "";
        }
        return obj;
    }


    public static Properties getSortedProperties(Properties prop)
    {
        SortedProperties sortProp = new SortedProperties();
        sortProp.putAll(prop);
        return (Properties)sortProp;
    }


    public static File checkDirectory(String dir, String nameFolder)
    {
        File file = new File(dir + dir + File.separator);
        if(!file.exists())
        {
            if(file.mkdir())
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.info("Directory : " + nameFolder + " is created!");
                }
            }
            else
            {
                LOG.error("Failed to create directory : " + nameFolder);
                return null;
            }
        }
        return file;
    }
}
