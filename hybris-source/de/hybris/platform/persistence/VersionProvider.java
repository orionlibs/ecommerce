package de.hybris.platform.persistence;

import de.hybris.platform.util.Config;

public class VersionProvider
{
    public static String getBuildVersion()
    {
        return Config.getParameter("build.version");
    }


    public static String getBuildRelease()
    {
        return Config.getParameter("build.release");
    }


    public static String getBuildDescription()
    {
        return Config.getParameter("build.description");
    }


    public static String getSpecificationVersion(String packageName)
    {
        return getBuildVersion();
    }


    public static String getImplementationVersion(String packageName)
    {
        return getBuildVersion();
    }
}
