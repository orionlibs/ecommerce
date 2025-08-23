package de.hybris.platform.util;

import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.PlatformConfig;
import de.hybris.platform.util.zip.SafeZipEntry;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

public final class SecurityUtils
{
    public static boolean isPathNotEscapingDirectory(Path path)
    {
        PlatformConfig platformConfig = ConfigUtil.getPlatformConfig(SafeZipEntry.class);
        File platformTempDir = platformConfig.getSystemConfig().getTempDir();
        return isNotEscapingDirectory(path, platformTempDir);
    }


    public static boolean isPathEscapingDirectory(Path path)
    {
        return !isPathNotEscapingDirectory(path);
    }


    private static Path resolveCanonicalFromRoot(Path root, Path testPath)
    {
        try
        {
            return root.resolve(testPath).toFile().getCanonicalFile().toPath();
        }
        catch(IOException e)
        {
            throw new RuntimeException("Failed to resolve test path against root", e);
        }
    }


    public static boolean isPathNotEscapingDirectory(Path path, String platformHome)
    {
        File platformTempDir = ConfigUtil.getSystemConfig(platformHome).getTempDir();
        return isNotEscapingDirectory(path, platformTempDir);
    }


    private static boolean isNotEscapingDirectory(Path path, File platformTempDir)
    {
        Path testRootDirectory = platformTempDir.toPath().resolve(UUID.randomUUID().toString()).toAbsolutePath();
        Path canonicalPath = resolveCanonicalFromRoot(testRootDirectory, path);
        return canonicalPath.startsWith(testRootDirectory);
    }
}
