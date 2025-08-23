package de.hybris.platform.solrserver.util;

public final class VersionUtils
{
    private static final int VERSION_TOKENS = 3;


    public static final String getVersionPath(String version)
    {
        if(version == null)
        {
            throw new IllegalArgumentException("Version cannot be null");
        }
        String[] tokens = version.split("\\.");
        if(tokens.length != 3)
        {
            throw new IllegalArgumentException("Invalid version: " + version);
        }
        String majorVersion = tokens[0];
        String minorVersion = tokens[1];
        return majorVersion + "." + majorVersion;
    }
}
