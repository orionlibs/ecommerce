package de.hybris.platform.solr.controller.util;

public final class OsUtils
{
    private static final String OS_NAME_PROPERTY = "os.name";
    private static final String WINDOWS = "Windows";
    private static final String OS2 = "OS/2";
    private static final String NETWARE = "NetWare";


    public static final boolean isUnix()
    {
        String operatingSystem = System.getProperty("os.name");
        return (!operatingSystem.startsWith("Windows") && !operatingSystem.startsWith("OS/2") && !operatingSystem.startsWith("NetWare"));
    }


    public static final boolean isWindows()
    {
        String operatingSystem = System.getProperty("os.name");
        return operatingSystem.startsWith("Windows");
    }
}
