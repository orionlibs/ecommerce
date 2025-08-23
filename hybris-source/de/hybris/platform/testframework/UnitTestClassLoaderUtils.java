package de.hybris.platform.testframework;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class UnitTestClassLoaderUtils
{
    private static final Class[] parameters = new Class[] {URL.class};
    public static final String BIN_PLATFORM_PATH = "bin" + File.separator + "platform";
    public static final String TEMP_PLATFORM_PATH = "temp" + File.separator + "hybris";
    public static final String DEFAULT_CLASSPATH_FILE = TEMP_PLATFORM_PATH + TEMP_PLATFORM_PATH + "classpath_junit.txt";


    public static String createDefaultClassPathFilePath(String platformHome)
    {
        return platformHome.replace(BIN_PLATFORM_PATH, DEFAULT_CLASSPATH_FILE);
    }


    public static void addJunitClasspathUrls(ClassLoader classLoader, String classpathJunitFile)
    {
        if(!(classLoader instanceof URLClassLoader))
        {
            throw new RuntimeException("Parent classloader has to be of type: " + URLClassLoader.class);
        }
        Path file = Paths.get(classpathJunitFile, new String[0]);
        if(!file.toFile().exists())
        {
            throw new RuntimeException("Classpath junit file doesn't exists. Please create one in ../temp/hybris/classpath_junit.txt or pass location with CLASSPATHFILE_JUNIT proeprty");
        }
        Method method = getAddURLMethod();
        for(String url : loadClasspathUrls(file))
        {
            try
            {
                method.invoke(classLoader, new Object[] {new URL(url)});
            }
            catch(Exception t)
            {
                throw new RuntimeException(t);
            }
        }
    }


    private static List<String> loadClasspathUrls(Path file)
    {
        try
        {
            return Files.readAllLines(file, Charset.forName("UTF-8"));
        }
        catch(IOException e)
        {
            throw new RuntimeException("Cannot update parent class loader", e);
        }
    }


    private static Method getAddURLMethod()
    {
        Method method = null;
        try
        {
            method = URLClassLoader.class.getDeclaredMethod("addURL", parameters);
        }
        catch(NoSuchMethodException e)
        {
            throw new RuntimeException(e);
        }
        method.setAccessible(true);
        return method;
    }
}
