package de.hybris.datasupplier.services.genericdatasupplier.impl;

import com.sap.sup.admin.sldsupplier.error.SLDDataSupplierApplicationException;
import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.datasupplier.services.HybrisCollectorService;
import de.hybris.datasupplier.services.genericdatasupplier.DataSupplierGenerationService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class DefaultDataSupplierGenerationService implements DataSupplierGenerationService
{
    private static final Logger LOG = Logger.getLogger(DefaultDataSupplierGenerationService.class);
    private static final String CLUSTER_ID_PROPERTY = "generic.datasupplier.clusterId";
    private static final String GENERICDATASUPPLIER_WAR_PROPERTY = "genericdatasupplier.war";
    private static final String GENERICDATASUPPLIER_WAR_NAME = "genericdatasupplier";
    private static final String GENERICDATASUPPLIER_WAR_EXTENSION = ".war";
    private static final String INSERT_COLLECTORS_KEYWORD = "INSERT_COLLECTORS";
    private static final String INSERT_DATABASE_COLLECTORS_KEYWORD = "INSERT_DATABASE_COLLECTORS";
    private static final String WEB_INF = "WEB-INF";
    private Properties genericDataSupplierProperties;
    private HybrisCollectorService hybrisCollectorService;
    private static final String FILE_SEPARATOR = "/";


    public void initialize()
    {
        try
        {
            InputStream is = DefaultDataSupplierGenerationService.class.getClassLoader().getResourceAsStream("genericdatasupplier.properties");
            try
            {
                this.genericDataSupplierProperties = new Properties();
                this.genericDataSupplierProperties.load(is);
                if(is != null)
                {
                    is.close();
                }
            }
            catch(Throwable throwable)
            {
                if(is != null)
                {
                    try
                    {
                        is.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(IOException e)
        {
            LOG.error("Failed to initialize genericdatasupplier properties.", e);
        }
    }


    public File generateDataSupplier(List<String> applications, Properties additionalProperties)
    {
        try
        {
            String location = explodeWar();
            editApplicationContext(applications, location);
            editPropertiesFile(additionalProperties, location);
            File generatedWar = generateWar(location);
            deleteExplodedWar(location);
            return generatedWar;
        }
        catch(IOException e)
        {
            LOG.error(e, e);
            return null;
        }
        catch(SLDDataSupplierApplicationException e)
        {
            LOG.error(e, (Throwable)e);
            return null;
        }
    }


    protected String explodeWar() throws IOException
    {
        InputStream warInput = DefaultDataSupplierGenerationService.class.getClassLoader().getResourceAsStream(this.genericDataSupplierProperties
                        .getProperty("genericdatasupplier.war"));
        try
        {
            File tempWar = File.createTempFile("genericdatasupplier", ".war");
            tempWar.deleteOnExit();
            FileUtils.copyInputStreamToFile(warInput, tempWar);
            String destdir = ConfigUtil.getPlatformConfig(DefaultDataSupplierGenerationService.class).getSystemConfig().getTempDir().getAbsolutePath() + "/genericdatasupplier/";
            JarFile jarFile = new JarFile(tempWar);
            try
            {
                Enumeration<JarEntry> entries = jarFile.entries();
                while(entries.hasMoreElements())
                {
                    JarEntry entry = entries.nextElement();
                    File file = new File(destdir, entry.getName());
                    if(!file.exists())
                    {
                        file.getParentFile().mkdirs();
                        file = new File(destdir, entry.getName());
                    }
                    if(entry.isDirectory())
                    {
                        continue;
                    }
                    InputStream is = jarFile.getInputStream(entry);
                    try
                    {
                        FileOutputStream fo = new FileOutputStream(file);
                        try
                        {
                            IOUtils.copy(is, fo);
                            fo.close();
                        }
                        catch(Throwable throwable)
                        {
                            try
                            {
                                fo.close();
                            }
                            catch(Throwable throwable1)
                            {
                                throwable.addSuppressed(throwable1);
                            }
                            throw throwable;
                        }
                        if(is != null)
                        {
                            is.close();
                        }
                    }
                    catch(Throwable throwable)
                    {
                        if(is != null)
                        {
                            try
                            {
                                is.close();
                            }
                            catch(Throwable throwable1)
                            {
                                throwable.addSuppressed(throwable1);
                            }
                        }
                        throw throwable;
                    }
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
            tempWar.delete();
            String str1 = destdir;
            if(warInput != null)
            {
                warInput.close();
            }
            return str1;
        }
        catch(Throwable throwable)
        {
            if(warInput != null)
            {
                try
                {
                    warInput.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
            }
            throw throwable;
        }
    }


    protected void editApplicationContext(List<String> applications, String explodedWarLocation) throws IOException
    {
        File appContext = new File(explodedWarLocation + "WEB-INF/applicationContext.xml");
        File tempAppContext = File.createTempFile("applicationContext", "xml");
        LineIterator lineIterator = FileUtils.lineIterator(appContext);
        while(lineIterator.hasNext())
        {
            String line = lineIterator.nextLine();
            FileUtils.writeStringToFile(tempAppContext, line + line, true);
            if(line.contains("INSERT_COLLECTORS"))
            {
                for(String application : applications)
                {
                    FileUtils.writeStringToFile(tempAppContext, this.genericDataSupplierProperties
                                    .getProperty(application) + this.genericDataSupplierProperties.getProperty(application), true);
                }
                continue;
            }
            if(line.contains("INSERT_DATABASE_COLLECTORS"))
            {
                for(String application : applications)
                {
                    String databaseProperty = this.genericDataSupplierProperties.getProperty(application + ".database");
                    if(!StringUtils.isEmpty(databaseProperty))
                    {
                        FileUtils.writeStringToFile(tempAppContext, databaseProperty + databaseProperty, true);
                    }
                }
            }
        }
        lineIterator.close();
        FileUtils.copyFile(tempAppContext, appContext);
        tempAppContext.delete();
    }


    protected void editPropertiesFile(Properties properties, String explodedWarLocation) throws IOException, SLDDataSupplierApplicationException
    {
        Properties existingProperties = new Properties();
        FileInputStream fis = new FileInputStream(explodedWarLocation + "WEB-INF" + explodedWarLocation + "classes" + File.separator + "project.properties");
        try
        {
            existingProperties.load(fis);
            fis.close();
        }
        catch(Throwable throwable)
        {
            try
            {
                fis.close();
            }
            catch(Throwable throwable1)
            {
                throwable.addSuppressed(throwable1);
            }
            throw throwable;
        }
        existingProperties.putAll(properties);
        existingProperties.put("generic.datasupplier.clusterId", this.hybrisCollectorService.getClusterId());
        FileOutputStream fos = new FileOutputStream(explodedWarLocation + "WEB-INF" + explodedWarLocation + "classes" + File.separator + "project.properties");
        try
        {
            existingProperties
                            .store(fos, "File generated dynamically. For comments about the properties please refer to the original genericdatasupplier.war in hybrisdatasupplier extension.");
            fos.close();
        }
        catch(Throwable throwable)
        {
            try
            {
                fos.close();
            }
            catch(Throwable throwable1)
            {
                throwable.addSuppressed(throwable1);
            }
            throw throwable;
        }
    }


    protected File generateWar(String explodedWarLocation) throws IOException
    {
        File newFile = File.createTempFile("genericdatasupplier", ".war");
        JarOutputStream jarOs = new JarOutputStream(new FileOutputStream(newFile));
        try
        {
            File rootDir = new File(explodedWarLocation);
            Collection<File> listDirectories = FileUtils.listFilesAndDirs(rootDir, DirectoryFileFilter.DIRECTORY, TrueFileFilter.INSTANCE);
            for(File dir : listDirectories)
            {
                if(!dir.getAbsolutePath().equals(rootDir.getAbsolutePath()))
                {
                    String path = dir.getPath().replace(rootDir.getPath() + rootDir.getPath(), "").replace(File.separator, "/");
                    if(!path.endsWith("/"))
                    {
                        path = path + "/";
                    }
                    JarEntry entry = new JarEntry(path);
                    jarOs.putNextEntry(entry);
                    jarOs.closeEntry();
                }
            }
            Collection<File> listFiles = FileUtils.listFiles(rootDir, FileFileFilter.FILE, TrueFileFilter.INSTANCE);
            for(File file : listFiles)
            {
                String name = file.getAbsolutePath().replace(rootDir.getAbsolutePath() + rootDir.getAbsolutePath(), "").replace(File.separator, "/");
                jarOs.putNextEntry(new JarEntry(name));
                FileInputStream fis = new FileInputStream(file);
                try
                {
                    IOUtils.copy(fis, jarOs);
                    fis.close();
                }
                catch(Throwable throwable)
                {
                    try
                    {
                        fis.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                    throw throwable;
                }
                jarOs.closeEntry();
            }
            jarOs.close();
        }
        catch(Throwable throwable)
        {
            try
            {
                jarOs.close();
            }
            catch(Throwable throwable1)
            {
                throwable.addSuppressed(throwable1);
            }
            throw throwable;
        }
        return newFile;
    }


    protected void deleteExplodedWar(String explodedWarLocation) throws IOException
    {
        FileUtils.deleteQuietly(new File(explodedWarLocation));
    }


    public void setHybrisCollectorService(HybrisCollectorService hybrisCollectorService)
    {
        this.hybrisCollectorService = hybrisCollectorService;
    }
}
