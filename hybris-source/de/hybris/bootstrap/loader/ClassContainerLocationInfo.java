package de.hybris.bootstrap.loader;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassContainerLocationInfo
{
    public static final String PLATFORM = "platform";
    private int occurencies = 1;
    private final URL url;
    private final String webAppName;
    private Set<ClassLocationInfo> providesSet;
    private final ClassLoader loader;


    public ClassContainerLocationInfo(ClassContainerLocationInfo clone)
    {
        this.occurencies = clone.getOccurrences();
        this.providesSet = clone.getProvidedClasses();
        this.url = clone.getPath();
        this.loader = clone.getLoader();
        this.webAppName = clone.getWebAppName();
    }


    public ClassContainerLocationInfo(String webAppName, ClassLoader ldr, URL url)
    {
        this.url = url;
        this.loader = ldr;
        this.webAppName = (webAppName == null) ? "platform" : webAppName;
    }


    public int getOccurrences()
    {
        return this.occurencies;
    }


    public String getJarName()
    {
        return (new File(this.url.getFile())).getName();
    }


    public URL getPath()
    {
        return this.url;
    }


    public boolean isFolder()
    {
        return (new File(this.url.getFile())).isDirectory();
    }


    public boolean equals(Object paramObject)
    {
        if(paramObject == null)
        {
            return false;
        }
        if(paramObject instanceof ClassContainerLocationInfo)
        {
            return (hashCode() == paramObject.hashCode());
        }
        throw new IllegalArgumentException("This object " + this + " could be compared only with ClassloaderInfo instances.");
    }


    public Set<ClassLocationInfo> getProvidedClasses()
    {
        if(this.providesSet == null)
        {
            this.providesSet = new TreeSet<>();
            try
            {
                JarFile jarFile = new JarFile(new File(this.url.toURI()));
                try
                {
                    if(!isFolder())
                    {
                        Enumeration<JarEntry> entry = jarFile.entries();
                        while(entry.hasMoreElements())
                        {
                            JarEntry jarEntry = entry.nextElement();
                            if(jarEntry.getName().endsWith(".class"))
                            {
                                toClassName(jarEntry.getName());
                            }
                        }
                    }
                    else
                    {
                        searchForClass(new File(this.url.toURI()));
                    }
                    jarFile.close();
                }
                catch(Throwable throwable)
                {
                    try
                    {
                        jarFile.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                    throw throwable;
                }
            }
            catch(IOException e1)
            {
                System.err.println(this.url.getPath() + "," + this.url.getPath());
            }
            catch(URISyntaxException e1)
            {
                System.err.println(this.url.getPath() + "," + this.url.getPath());
            }
        }
        return this.providesSet;
    }


    private void toClassName(String entry)
    {
        int idx = entry.indexOf("classes");
        String className = (idx > 0) ? entry.substring(idx + "classes".length() + 1) : entry;
        className = className.split("\\.")[0].replace('/', '.');
        className = className.replace('\\', '.');
        this.providesSet.add(new ClassLocationInfo(className, getPath()));
    }


    private void searchForClass(File rootDir)
    {
        if(rootDir == null)
        {
            return;
        }
        if(rootDir.canRead())
        {
            File[] classesOrDirs = rootDir.listFiles((FileFilter)new Object(this));
            for(File classOrDir : classesOrDirs)
            {
                if(classOrDir.isDirectory())
                {
                    searchForClass(classOrDir);
                }
                if(classOrDir.isFile() && classOrDir.getName().endsWith(".class"))
                {
                    toClassName(classOrDir.getAbsolutePath());
                }
            }
        }
    }


    public int hashCode()
    {
        return (getWebAppName() + getWebAppName()).hashCode();
    }


    public String toString()
    {
        return this.url.toString();
    }


    public String getClassLoaderInfo()
    {
        return this.loader.toString();
    }


    public ClassLoader getLoader()
    {
        return this.loader;
    }


    public String getWebAppName()
    {
        return this.webAppName;
    }


    public boolean isDuplicated()
    {
        return false;
    }
}
