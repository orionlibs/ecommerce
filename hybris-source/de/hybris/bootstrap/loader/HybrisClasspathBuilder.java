package de.hybris.bootstrap.loader;

import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.bootstrap.config.PlatformConfig;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import org.apache.tools.ant.AntClassLoader;

public class HybrisClasspathBuilder
{
    private static List<File> classpath;


    public static String getClassPathAsString(PlatformConfig platformConfig)
    {
        StringBuilder out = new StringBuilder();
        for(File file : getClassPathAsList(platformConfig))
        {
            try
            {
                if(out.length() != 0)
                {
                    out.append(';');
                }
                out.append(file.getCanonicalPath());
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        return out.toString();
    }


    public static ClassLoader applyClasspathToClassloader(PlatformConfig platformConfig, ClassLoader parentClassLoader)
    {
        if(parentClassLoader == null)
        {
            parentClassLoader = ClassLoader.getSystemClassLoader();
        }
        for(File file : getClassPathAsList(platformConfig))
        {
            try
            {
                URL u = file.toURL();
                Class<URLClassLoader> urlClass = URLClassLoader.class;
                Method method = urlClass.getDeclaredMethod("addURL", new Class[] {URL.class});
                method.setAccessible(true);
                method.invoke(parentClassLoader, new Object[] {u});
            }
            catch(Exception e)
            {
                System.out.println("Could not add " + file + " to classpath!");
            }
        }
        return parentClassLoader;
    }


    public static List<File> getClassPathAsList(PlatformConfig platformConfig)
    {
        if(classpath != null)
        {
            return classpath;
        }
        List<ExtensionInfo> extensions = platformConfig.getExtensionInfosInBuildOrder();
        classpath = new ArrayList<>();
        for(ExtensionInfo inf : extensions)
        {
            File classes = new File(inf.getExtensionDirectory(), "classes");
            if(classes.exists())
            {
                classpath.add(classes);
            }
            File src = new File(inf.getExtensionDirectory(), "src");
            if(src.exists())
            {
                classpath.add(src);
            }
            File testsrc = new File(inf.getExtensionDirectory(), "testsrc");
            if(testsrc.exists())
            {
                classpath.add(testsrc);
            }
            File resources = new File(inf.getExtensionDirectory(), "resources");
            if(resources.exists())
            {
                classpath.add(resources);
            }
            File binFolder = new File(inf.getExtensionDirectory(), "bin");
            if(binFolder.exists())
            {
                for(File lib : binFolder.listFiles())
                {
                    if(lib.getAbsolutePath().endsWith(".jar"))
                    {
                        classpath.add(lib);
                    }
                }
                classpath.add(binFolder);
            }
            File libFolder = new File(inf.getExtensionDirectory(), "lib");
            if(libFolder.exists())
            {
                for(File lib : libFolder.listFiles())
                {
                    if(lib.getAbsolutePath().endsWith(".jar"))
                    {
                        classpath.add(lib);
                    }
                }
                classpath.add(libFolder);
            }
        }
        classpath.add(new File(platformConfig.getSystemConfig().getConfigDir(), "/licence/hybrislicence.jar"));
        File bootstrapFolder = new File(platformConfig.getPlatformHome(), "bootstrap/bin");
        if(bootstrapFolder.exists())
        {
            for(File lib : bootstrapFolder.listFiles())
            {
                if(lib.getAbsolutePath().endsWith(".jar"))
                {
                    classpath.add(lib);
                }
            }
        }
        File bootstrapFolderLib = new File(platformConfig.getPlatformHome(), "bootstrap/lib");
        if(bootstrapFolderLib.exists())
        {
            for(File lib : bootstrapFolderLib.listFiles())
            {
                if(lib.getAbsolutePath().endsWith(".jar"))
                {
                    classpath.add(lib);
                }
            }
        }
        for(File lib : (new File(platformConfig.getPlatformHome(), "tomcat/lib")).listFiles())
        {
            if(lib.getAbsolutePath().endsWith(".jar"))
            {
                classpath.add(lib);
            }
        }
        for(File lib : (new File(platformConfig.getPlatformHome(), "lib/dbdriver")).listFiles())
        {
            if(lib.getAbsolutePath().endsWith(".jar"))
            {
                classpath.add(lib);
            }
        }
        for(File lib : (new File(platformConfig.getPlatformHome(), "apache-ant/lib")).listFiles())
        {
            if(lib.getAbsolutePath().endsWith(".jar"))
            {
                classpath.add(lib);
            }
        }
        File antDirectory = new File(platformConfig.getPlatformHome(), "resources/ant/lib");
        if(antDirectory.exists())
        {
            for(File lib : antDirectory.listFiles())
            {
                if(lib.getAbsolutePath().endsWith(".jar"))
                {
                    classpath.add(lib);
                }
            }
        }
        return classpath;
    }


    public static AntClassLoader getAntClassLoader(PlatformConfig platformConfig)
    {
        AntClassLoader antClassLoader = new AntClassLoader();
        antClassLoader.addJavaLibraries();
        for(File file : getClassPathAsList(platformConfig))
        {
            antClassLoader.addPathComponent(file);
        }
        return antClassLoader;
    }
}
