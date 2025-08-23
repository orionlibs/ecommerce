package de.hybris.bootstrap.config;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class DirectoryConfigLoader
{
    public static Properties loadFromDir(File targetDir, String fileSuffix)
    {
        DirectoryConfigLoader loader = new DirectoryConfigLoader();
        return loader.load(targetDir, fileSuffix);
    }


    public static Properties loadFromDir(String targetDir, String fileSuffix)
    {
        return loadFromDir(new File(targetDir), fileSuffix);
    }


    public Properties load(File targetDir, String fileSuffix)
    {
        Properties props = new Properties();
        if(targetDir == null || !Files.isDirectory(targetDir.toPath(), new java.nio.file.LinkOption[0]))
        {
            return props;
        }
        Object object = new Object(this, fileSuffix);
        List<File> matchingFiles = Arrays.asList(targetDir.listFiles((FileFilter)object));
        sortInPlaceByPriority(matchingFiles);
        for(File file : matchingFiles)
        {
            appendPropsFromFile(props, file);
        }
        return props;
    }


    private void appendPropsFromFile(Properties props, File resource)
    {
        if(!resource.exists())
        {
            throw new BootstrapConfigException("Can not load properties from " + resource.getAbsolutePath());
        }
        try
        {
            InputStream inputStream = new FileInputStream(resource);
            try
            {
                props.load(inputStream);
                inputStream.close();
            }
            catch(Throwable throwable)
            {
                try
                {
                    inputStream.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
        }
        catch(IOException e)
        {
            throw new BootstrapConfigException("Can not load properties from " + resource.getAbsolutePath(), e);
        }
    }


    private int extractFilePriority(File file)
    {
        return Integer.parseInt(file.getName().substring(0, 2));
    }


    private void sortInPlaceByPriority(List<File> files)
    {
        Collections.sort(files, (leftFile, rightFile) -> {
            int leftPriority = extractFilePriority(leftFile);
            int rightPriority = extractFilePriority(rightFile);
            return (leftPriority < rightPriority) ? -1 : ((leftPriority == rightPriority) ? 0 : 1);
        });
    }
}
