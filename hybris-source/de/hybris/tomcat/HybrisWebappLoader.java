package de.hybris.tomcat;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.loader.WebappLoader;

public class HybrisWebappLoader extends WebappLoader
{
    private static final String CLASS_LOADER_NAME = HybrisWebappClassLoader.class.getName();


    public HybrisWebappLoader()
    {
        this(null);
    }


    public HybrisWebappLoader(ClassLoader parentClassLoader)
    {
        super(parentClassLoader);
        setLoaderClass(CLASS_LOADER_NAME);
    }


    public void setPlatformHome(String platformHome) throws Exception
    {
        HybrisWebappClassLoader.setPlatformHome(platformHome);
    }


    public void setDeployName(String deployName) throws Exception
    {
        HybrisWebappClassLoader.setDeployName(deployName);
    }


    protected void startInternal() throws LifecycleException
    {
        super.startInternal();
        ClassLoader classLoader = getClassLoader();
        if(classLoader != null && classLoader instanceof HybrisWebappClassLoader)
        {
            classLoaderHasBeenStarted((HybrisWebappClassLoader)classLoader);
        }
    }


    protected void classLoaderHasBeenStarted(HybrisWebappClassLoader classLoader)
    {
    }
}
