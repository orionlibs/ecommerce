package de.hybris.platform.testframework;

import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.bootstrap.config.PlatformConfig;
import de.hybris.bootstrap.config.SystemConfig;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClasspathGenerator
{
    private static final Logger LOG = LoggerFactory.getLogger(ClasspathGenerator.class);


    public static void generate(String platformHome, String tempDir)
    {
        LOG.info("Junit hybris agent run with platform home: {}", platformHome);
        StringBuilder additionalClasspath = new StringBuilder();
        URL[] urls = getInPlaceURLs(platformHome, additionalClasspath.toString());
        List<String> lines = new ArrayList<>();
        for(URL url : urls)
        {
            lines.add(url.toString());
        }
        Path file = Paths.get(tempDir + "/classpath_junit.txt", new String[0]);
        try
        {
            Files.write(file, (Iterable)lines, Charset.forName("UTF-8"), new java.nio.file.OpenOption[0]);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }


    public static final URL[] getInPlaceURLs(String platformHome, String additionalClasspath)
    {
        return getInPlaceURLs(loadConfig(platformHome), additionalClasspath);
    }


    private static PlatformConfig loadConfig(String platformHome)
    {
        SystemConfig systemConfig = ConfigUtil.getSystemConfig(platformHome);
        return PlatformConfig.getInstance(systemConfig);
    }


    private static Iterable<URL> getWebModuleLibs(URI webModuleLibDir)
    {
        List<URL> result = new ArrayList<>();
        File libDir = new File(webModuleLibDir);
        if(libDir.exists() && libDir.isDirectory())
        {
            for(File file : libDir.listFiles())
            {
                if(file.getPath().endsWith("jar"))
                {
                    try
                    {
                        result.add(file.toURI().toURL());
                    }
                    catch(MalformedURLException e)
                    {
                        throw new IllegalStateException(e.getMessage(), e);
                    }
                }
            }
        }
        return result;
    }


    private static final URL[] getInPlaceURLs(PlatformConfig config, String additionalClasspath)
    {
        try
        {
            List<URL> urls = new ArrayList<>();
            addClassPathURLs(urls, additionalClasspath);
            File file = new File(new File(config.getPlatformHome(), "bootstrap/bin"), "ybootstrap.jar");
            urls.add(file.toURI().toURL());
            file = new File(config.getSystemConfig().getBootstrapBinDir(), "models.jar");
            if(!file.exists())
            {
                file = new File(new File(config.getPlatformHome(), "bootstrap/bin"), "models.jar");
            }
            urls.add(file.toURI().toURL());
            file = new File(new File(config.getSystemConfig().getConfigDir(), "licence"), "hybrislicence.jar");
            if(file.exists())
            {
                urls.add(file.toURI().toURL());
            }
            for(ExtensionInfo info : config.getExtensionInfosInBuildOrder())
            {
                addExtensionURLs(info, urls);
            }
            file = new File(new File(config.getPlatformHome(), "lib"), "dbdriver");
            if(file.listFiles() != null)
            {
                for(File lib : file.listFiles())
                {
                    urls.add(lib.toURI().toURL());
                }
            }
            file = new File(new File(config.getPlatformHome(), "tomcat"), "lib");
            if(file.listFiles() != null)
            {
                for(File lib : file.listFiles())
                {
                    urls.add(lib.toURI().toURL());
                }
            }
            return urls.<URL>toArray(new URL[urls.size()]);
        }
        catch(MalformedURLException e)
        {
            throw new IllegalArgumentException(e);
        }
    }


    private static void addClassPathURLs(List<URL> urls, String classpath) throws MalformedURLException
    {
        PathTokenizer st = new PathTokenizer(classpath);
        while(st.hasMoreTokens())
        {
            String s = st.nextToken();
            if(s.endsWith("*.jar"))
            {
                s = s.substring(0, s.length() - 5);
                for(File lib : Arrays.<File>asList((new File(s)).listFiles()))
                {
                    urls.add(lib.toURI().toURL());
                }
                continue;
            }
            File f = new File(s);
            urls.add(f.toURI().toURL());
        }
    }


    private static void addExtensionURLs(ExtensionInfo info, List<URL> urls) throws MalformedURLException
    {
        File file = new File(info.getExtensionDirectory(), "resources");
        if(file.exists())
        {
            urls.add(file.toURI().toURL());
        }
        file = new File(info.getExtensionDirectory(), "lib");
        if(file.exists())
        {
            for(File lib : file.listFiles())
            {
                urls.add(lib.toURI().toURL());
            }
        }
        file = new File(info.getExtensionDirectory(), "classes");
        if(file.exists())
        {
            urls.add(file.toURI().toURL());
        }
        file = new File(new File(info.getExtensionDirectory(), "bin"), info.getName() + "server.jar");
        if(file.exists())
        {
            urls.add(file.toURI().toURL());
        }
    }
}
