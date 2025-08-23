package de.hybris.tomcat;

import de.hybris.bootstrap.loader.ClassContainerLocationInfo;
import de.hybris.bootstrap.loader.LocationInfoAnalyzer;
import de.hybris.bootstrap.loader.impl.LocationInfoAnalyzerImpl;
import java.net.URL;
import java.net.URLClassLoader;

public class DiagnosticHybrisWebappLoader extends HybrisWebappLoader
{
    private String webAppName = null;


    public DiagnosticHybrisWebappLoader()
    {
    }


    public DiagnosticHybrisWebappLoader(ClassLoader parent)
    {
        super(parent);
    }


    public void setWebAppName(String appName)
    {
        this.webAppName = appName;
    }


    public String getWebAppName()
    {
        return this.webAppName;
    }


    protected void classLoaderHasBeenStarted(HybrisWebappLoader.HybrisWebappClassLoader classLoader)
    {
        super.classLoaderHasBeenStarted(classLoader);
        classLoader.setWebappName(this.webAppName);
        monitorClassLoadersChain(classLoader);
    }


    private void monitorClassLoadersChain(HybrisWebappLoader.HybrisWebappClassLoader classLoader)
    {
        monitorClassLoader((ClassLoader)classLoader, classLoader.getWebappName());
        ClassLoader loader = classLoader.getParent();
        while(loader != null)
        {
            monitorClassLoader(loader);
            loader = loader.getParent();
        }
    }


    private void monitorClassLoader(ClassLoader loader)
    {
        monitorClassLoader(loader, null);
    }


    private void monitorClassLoader(ClassLoader classLoader, String appName)
    {
        if(!(classLoader instanceof URLClassLoader))
        {
            return;
        }
        URLClassLoader urlClassLoader = (URLClassLoader)classLoader;
        for(URL url : urlClassLoader.getURLs())
        {
            monitorUrl(appName, urlClassLoader, url);
        }
    }


    private void monitorUrl(String appName, ClassLoader classLoader, URL url)
    {
        ClassContainerLocationInfo info = getLocationInfoAnalyzer().createClassLocationInfo(appName, classLoader, url);
        if(info != null)
        {
            getLocationInfoAnalyzer().addClassContainerLocationInfo(info);
        }
    }


    private LocationInfoAnalyzer getLocationInfoAnalyzer()
    {
        return LocationInfoAnalyzerImpl.getInstance();
    }
}
