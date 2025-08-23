package de.hybris.bootstrap.testclasses;

import groovy.lang.GroovyClassLoader;
import java.io.IOException;
import java.net.URL;
import org.codehaus.groovy.control.CompilationFailedException;

public class PatchedForTestGroovyClassLoader extends GroovyClassLoader
{
    public PatchedForTestGroovyClassLoader(ClassLoader parent)
    {
        super(parent);
    }


    protected Class loadClass(String name, boolean resolve) throws ClassNotFoundException
    {
        return loadClass(name, true, false, resolve);
    }


    public Class loadClass(String name, boolean lookupScriptFiles, boolean preferClassOverScript, boolean resolve) throws ClassNotFoundException, CompilationFailedException
    {
        Class<?> cls = getClassCacheEntry(name);
        boolean recompile = isRecompilable(cls);
        if(!recompile)
        {
            return cls;
        }
        ClassNotFoundException last = null;
        ClassLoader parent = getParent();
        if(parent != null)
        {
            try
            {
                Class<?> parentClassLoaderClass = parent.loadClass(name);
                if(cls != parentClassLoaderClass)
                {
                    return parentClassLoaderClass;
                }
            }
            catch(ClassNotFoundException cnfe)
            {
                last = cnfe;
            }
            catch(NoClassDefFoundError ncdfe)
            {
                if(ncdfe.getMessage().indexOf("wrong name") > 0)
                {
                    last = new ClassNotFoundException(name);
                }
                else
                {
                    throw ncdfe;
                }
            }
        }
        SecurityManager sm = System.getSecurityManager();
        if(sm != null)
        {
            String className = name.replace('/', '.');
            int i = className.lastIndexOf('.');
            if(i != -1 && !className.startsWith("sun.reflect."))
            {
                sm.checkPackageAccess(className.substring(0, i));
            }
        }
        if(cls != null && preferClassOverScript)
        {
            return cls;
        }
        if(lookupScriptFiles)
        {
            try
            {
                Class<?> classCacheEntry = getClassCacheEntry(name);
                if(classCacheEntry != cls)
                {
                    return classCacheEntry;
                }
                URL source = getResourceLoader().loadGroovySource(name);
                Class<?> oldClass = cls;
                cls = null;
                cls = recompile(source, name, oldClass);
            }
            catch(IOException ioe)
            {
                last = new ClassNotFoundException("IOException while opening groovy source: " + name, ioe);
            }
            finally
            {
                if(cls == null)
                {
                    removeClassCacheEntry(name);
                }
                else
                {
                    setClassCacheEntry(cls);
                }
            }
        }
        if(cls == null)
        {
            if(last == null)
            {
                throw new AssertionError(true);
            }
            throw last;
        }
        return cls;
    }
}
