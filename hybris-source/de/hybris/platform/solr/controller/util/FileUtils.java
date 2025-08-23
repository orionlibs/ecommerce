package de.hybris.platform.solr.controller.util;

import java.io.IOException;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;

public final class FileUtils
{
    public static void copyDirectory(Path srcPath, Path dstPath) throws IOException
    {
        if(srcPath == null)
        {
            throw new NullPointerException("Source must not be null");
        }
        if(dstPath == null)
        {
            throw new NullPointerException("Destination must not be null");
        }
        if(!srcPath.toFile().isDirectory())
        {
            throw new IllegalArgumentException(MessageFormat.format("Source path ''{0}'' is not a directory", new Object[] {srcPath}));
        }
        if(dstPath.toFile().exists() && !dstPath.toFile().isDirectory())
        {
            throw new IllegalArgumentException(MessageFormat.format("Destination path ''{0}'' is not a directory", new Object[] {dstPath}));
        }
        Files.walkFileTree(srcPath, (FileVisitor<? super Path>)new Object(dstPath, srcPath));
    }


    public static void deleteDirectory(Path path) throws IOException
    {
        if(path == null)
        {
            throw new NullPointerException("Source must not be null");
        }
        if(!path.toFile().exists())
        {
            return;
        }
        if(!path.toFile().isDirectory())
        {
            throw new IllegalArgumentException(MessageFormat.format("Path ''{0}'' is not a directory", new Object[] {path}));
        }
        Files.walkFileTree(path, (FileVisitor<? super Path>)new Object());
    }
}
