package de.hybris.datasupplier.generator.tomcat.collector.sc;

import com.sap.sup.admin.sldsupplier.collector.CollectorContext;
import com.sap.sup.admin.sldsupplier.config.SLDSupplierConfiguration;
import com.sap.sup.admin.sldsupplier.data.model.sc.meta.xml.ProductInformation;
import com.sap.sup.admin.sldsupplier.data.model.sc.meta.xml.ProductSoftwareComponentDeployment;
import com.sap.sup.admin.sldsupplier.sc.DefaultProductInformationXMLParser;
import com.sap.sup.admin.sldsupplier.sc.ProductInformationCreator;
import com.sap.sup.admin.sldsupplier.sc.SoftwareComponentUtils;
import com.sap.sup.admin.sldsupplier.sldreg.TransportUtils;
import de.hybris.datasupplier.generator.tomcat.collector.BaseTomcatCollectorImpl;
import de.hybris.datasupplier.generator.tomcat.data.TomcatConfigObjectUtils;
import de.hybris.datasupplier.generator.tomcat.data.TomcatConfiguration;
import de.hybris.datasupplier.generator.tomcat.data.TomcatEngine;
import de.hybris.datasupplier.generator.tomcat.data.TomcatHost;
import de.hybris.datasupplier.generator.tomcat.data.TomcatProductSoftwareComponentDeployment;
import de.hybris.datasupplier.generator.tomcat.data.TomcatWebModule;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import org.apache.log4j.Logger;

public abstract class BaseSoftwareComponentCollectorImpl extends BaseTomcatCollectorImpl
{
    private static final Logger LOG = Logger.getLogger(BaseSoftwareComponentCollectorImpl.class);
    public static final String WAR = ".war";
    public static final String TMP = "tmp";
    public static final int MAX_SOFTWARE_COMPONENT_LEVEL = 2;
    private ProductInformationCreator creator;
    private TomcatConfiguration tomcatConfiguration;
    private int maxSoftwareComponentLookupLevel;
    private int currentSoftwareComponentLookupLevel;
    private List productSoftwareComponentDeployments;


    public List getProductSoftwareComponentDeployments()
    {
        return this.productSoftwareComponentDeployments;
    }


    public void setProductSoftwareComponentDeployments(List productSoftwareComponentDeployments)
    {
        this.productSoftwareComponentDeployments = productSoftwareComponentDeployments;
    }


    public ProductInformationCreator getCreator()
    {
        if(this.creator == null)
        {
            this.creator = createProductInformationCreator();
        }
        return this.creator;
    }


    protected ProductInformationCreator createProductInformationCreator()
    {
        DefaultProductInformationXMLParser xmlParser = new DefaultProductInformationXMLParser();
        xmlParser.setVerbose(getVerbose());
        return (ProductInformationCreator)xmlParser;
    }


    public void setCreator(ProductInformationCreator creator)
    {
        this.creator = creator;
    }


    public TomcatConfiguration getTomcatConfiguration()
    {
        return this.tomcatConfiguration;
    }


    public void setTomcatConfiguration(TomcatConfiguration tomcatConfiguration)
    {
        this.tomcatConfiguration = tomcatConfiguration;
    }


    public int getMaxSoftwareComponentLookupLevel()
    {
        return this.maxSoftwareComponentLookupLevel;
    }


    public void setMaxSoftwareComponentLookupLevel(int softwareComponentLookupLevel)
    {
        this.maxSoftwareComponentLookupLevel = softwareComponentLookupLevel;
    }


    public int getCurrentSoftwareComponentLookupLevel()
    {
        return this.currentSoftwareComponentLookupLevel;
    }


    public void setCurrentSoftwareComponentLookupLevel(int currentSoftwareComponentLookupLevel)
    {
        this.currentSoftwareComponentLookupLevel = currentSoftwareComponentLookupLevel;
    }


    protected List loadApplicationSoftwareComponentDeploymentsFromDir(File appDeploymentDir, TomcatProductSoftwareComponentDeployment template) throws IOException
    {
        List results = new ArrayList();
        debugLogging("    ... Searching for application component descriptor in directory [" + appDeploymentDir + "]");
        File[] files = appDeploymentDir.listFiles();
        for(int i = 0; i < files.length; i++)
        {
            File child = files[i];
            if(child.isDirectory())
            {
                List descriptors = loadApplicationSoftwareComponentDeploymentsFromDir(child, template);
                if(descriptors != null)
                {
                    results.addAll(descriptors);
                }
            }
            else
            {
                String path = child.getPath();
                if(path == null)
                {
                    debugLogging("Warning: Unexpected Tomcat configuration. NULL path file for file [" + child + "]");
                }
                if(getCreator().accepts(path))
                {
                    debugLogging("       >>> Found application component descriptor in file [" + path + "]");
                    InputStream in = new BufferedInputStream(new FileInputStream(child));
                    List currProductSoftwareDeployments = null;
                    try
                    {
                        currProductSoftwareDeployments = buildSoftwareComponentDeployments(in, template);
                    }
                    finally
                    {
                        in.close();
                    }
                    if(currProductSoftwareDeployments != null && !currProductSoftwareDeployments.isEmpty())
                    {
                        debugLogging("     >>> Finished [" + currProductSoftwareDeployments.size() + "] software component deployments in the directory [" + appDeploymentDir + "]");
                        results.addAll(currProductSoftwareDeployments);
                    }
                }
            }
        }
        return results;
    }


    protected List loadApplicationSoftwareComponentDeploymentsFromWar(File appDeploymentDir, TomcatProductSoftwareComponentDeployment template) throws IOException
    {
        debugLogging("  .. Searching for app deployment descriptors in .WAR file [" + appDeploymentDir + "]");
        CollectorContext collectorContext = getCollectorContext();
        SLDSupplierConfiguration configuration = collectorContext.getSLDSupplierConfiguration();
        String workingDirPath = configuration.getWorkingDir();
        workingDirPath = TransportUtils.makeWorkingDirSoft(workingDirPath);
        if(workingDirPath == null)
        {
            debugLogging("(ATLDDS00435). Warning: No working directory available. Cannot extract WAR [" + appDeploymentDir + "].");
            return Collections.emptyList();
        }
        StringBuilder tmp = (new StringBuilder(workingDirPath)).append(File.separator).append("tmp");
        String tmpDirPath = tmp.toString();
        File tmpDir = new File(tmpDirPath);
        if(!tmpDir.exists())
        {
            boolean success = tmpDir.mkdir();
            if(!success)
            {
                debugLogging("Warning:  Could not created temp directory [" + tmpDirPath + "]. Cannot extract WAR [" + appDeploymentDir + "].");
                return Collections.emptyList();
            }
        }
        String destDirPath = tmp.append(File.separator).append(appDeploymentDir.getName()).substring(0, tmp.length() - 4);
        File destDir = new File(destDirPath);
        if(!destDir.exists())
        {
            boolean success = destDir.mkdir();
            if(!success)
            {
                debugLogging("Warning:  Could not created temp directory [" + destDir + "]. Cannot extract WAR [" + appDeploymentDir + "].");
                return Collections.emptyList();
            }
        }
        ZipInputStream zis = new ZipInputStream(new FileInputStream(appDeploymentDir));
        List results = null;
        this.currentSoftwareComponentLookupLevel = 0;
        try
        {
            results = loadApplicationSoftwareComponentDeploymentsFromZip(zis, destDirPath, template);
        }
        catch(IOException ioe)
        {
            debugLogging("Warning: IOException reading .WAR file [" + appDeploymentDir + "]:" + ioe.getMessage());
            debugLogging(ioe);
        }
        finally
        {
            zis.close();
        }
        return results;
    }


    protected List loadApplicationSoftwareComponentDeploymentsFromZip(ZipInputStream zis, String destDirName, TomcatProductSoftwareComponentDeployment template) throws IOException
    {
        List results = new ArrayList();
        byte[] buffer = new byte[4096];
        StringBuilder sb = new StringBuilder();
        ZipEntry zipEntry = null;
        while((zipEntry = zis.getNextEntry()) != null)
        {
            String entryName = zipEntry.getName();
            sb.setLength(0);
            String destFilePath = sb.append(destDirName).append(File.separator).append(entryName).toString();
            if(zipEntry.isDirectory())
            {
                debugLogging("       .... Inspecting now dir in WAR file [" + destFilePath + "]");
                File destDir = new File(destFilePath);
                if(!destDir.exists())
                {
                    destDir.mkdir();
                }
                zis.closeEntry();
                continue;
            }
            if(!getCreator().accepts(entryName))
            {
                zis.closeEntry();
                continue;
            }
            debugLogging("        ... Extrating software component descriptor [" + entryName + "] from .WAR to file [" + destFilePath + "]");
            try
            {
                File destFile = new File(destFilePath);
                if(destFile.exists() && !destFile.canWrite())
                {
                    debugLogging("Warning: Cannot process software component descriptor [" + entryName + "]. Cannot write to the existing file [" + destFile + "]");
                    zis.closeEntry();
                    zis.closeEntry();
                    continue;
                }
                BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(destFile));
                try
                {
                    int numBytes;
                    while((numBytes = zis.read(buffer, 0, buffer.length)) != -1)
                    {
                        fos.write(buffer, 0, numBytes);
                    }
                }
                finally
                {
                    fos.close();
                }
            }
            catch(IOException ioe)
            {
                debugLogging("Warning: IOException extrating software component descriptor to file [" + destFilePath + "]:" + ioe.getMessage());
                debugLogging(ioe);
            }
            finally
            {
                zis.closeEntry();
            }
            List currProductSoftwareDeployments = null;
            try
            {
                InputStream fins = new BufferedInputStream(new FileInputStream(destFilePath));
                try
                {
                    currProductSoftwareDeployments = buildSoftwareComponentDeployments(fins, template);
                    fins.close();
                }
                catch(Throwable throwable)
                {
                    try
                    {
                        fins.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                    throw throwable;
                }
            }
            catch(IOException ioe)
            {
                debugLogging("Warning: Error reading software component descriptor from file [" + destFilePath + "]:" + ioe.getMessage());
                debugLogging(ioe);
            }
            if(currProductSoftwareDeployments != null && !currProductSoftwareDeployments.isEmpty())
            {
                debugLogging("  For [" + entryName + "] in WAR found [" + currProductSoftwareDeployments.size() + "] software component deployments");
                results.addAll(currProductSoftwareDeployments);
            }
        }
        return results;
    }


    protected List buildSoftwareComponentDeployments(InputStream appDescriptorStream, TomcatProductSoftwareComponentDeployment template)
    {
        ProductInformation productInformation = getCreator().createProductInformation(appDescriptorStream);
        debugLogging("   Finished with software component deployments (before resolving bindings): " + productInformation);
        List currProductSoftwareDeployments = SoftwareComponentUtils.resolveBindings(productInformation, (ProductSoftwareComponentDeployment)template);
        debugLogging("  Collected [" + currProductSoftwareDeployments.size() + "] software component deployments after resolve bindigs");
        return currProductSoftwareDeployments;
    }


    protected File getApplicationDeploymentDir(TomcatWebModule webModule, TomcatEngine engine)
    {
        String moduleDocBase = webModule.getDocBase();
        if(moduleDocBase == null)
        {
            debugLogging("Warning: Unexpected Tomcat configuration. TomcatModule [" + webModule + "] has no doc base!");
            return null;
        }
        File appDeploymentDir = new File(moduleDocBase);
        if(appDeploymentDir.exists())
        {
            debugLogging("        >>> Found application depoyment dir as moduleDocBase = [" + moduleDocBase + "]");
            return appDeploymentDir;
        }
        TomcatHost defaultHost = engine.getDefaultTomcatHost();
        if(defaultHost == null)
        {
            debugLogging("Warning: Unexpected Tomcat configuration. TomcatEngine [" + engine + "] has no default host!");
            return null;
        }
        String engineBaseDir = null;
        String hostBaseDir = defaultHost.getBaseDir();
        File tmp = new File(hostBaseDir);
        if(!tmp.exists())
        {
            engineBaseDir = engine.getBaseDir();
            if(engineBaseDir == null)
            {
                debugLogging("Warning: Unexpected Tomcat configuration. TomcatEngine [" + engine + "] has no engine base dir!");
                return null;
            }
        }
        StringBuilder sb = new StringBuilder();
        if(engineBaseDir != null)
        {
            sb.append(engineBaseDir);
            sb.append("/");
        }
        sb.append(hostBaseDir);
        sb.append("/");
        sb.append(moduleDocBase);
        appDeploymentDir = new File(sb.toString());
        if(!appDeploymentDir.exists())
        {
            debugLogging("Warning: Unexpected Tomcat configuration. Application depoyment dir does not exists [" + appDeploymentDir + "]");
            return null;
        }
        return appDeploymentDir;
    }


    protected TomcatWebModule buildWebModule(ObjectName webModuleName) throws ReflectionException, AttributeNotFoundException, MBeanException, InstanceNotFoundException
    {
        TomcatWebModule webModule = new TomcatWebModule();
        webModule.setObjectName(webModuleName);
        MBeanServer mbeanServer = getServiceInitializer().getMBeanServer();
        Object attrEngineName = mbeanServer.getAttribute(webModuleName, "engineName");
        if(attrEngineName != null)
        {
            webModule.setEngineName(attrEngineName.toString());
        }
        Object attrDocBase = mbeanServer.getAttribute(webModuleName, "docBase");
        if(attrDocBase != null)
        {
            webModule.setDocBase(attrDocBase.toString());
        }
        Object attrPath = mbeanServer.getAttribute(webModuleName, "path");
        if(attrPath != null)
        {
            webModule.setPath(attrPath.toString());
        }
        return webModule;
    }


    protected TomcatWebModule buildWebModule70(ObjectName webModuleName) throws ReflectionException, AttributeNotFoundException, MBeanException, InstanceNotFoundException
    {
        TomcatWebModule webModule = new TomcatWebModule();
        webModule.setObjectName(webModuleName);
        MBeanServer mbeanServer = getServiceInitializer().getMBeanServer();
        Object attrManagedResource = mbeanServer.getAttribute(webModuleName, "managedResource");
        if(attrManagedResource != null)
        {
            String strAttrManagedResource = attrManagedResource.toString();
            webModule.setManagedResource(strAttrManagedResource);
            String engineName = TomcatConfigObjectUtils.getEngineNameFromMappedResource70(strAttrManagedResource);
            webModule.setEngineName(engineName);
        }
        Object attrDocBase = mbeanServer.getAttribute(webModuleName, "docBase");
        if(attrDocBase != null)
        {
            webModule.setDocBase(attrDocBase.toString());
        }
        Object attrPath = mbeanServer.getAttribute(webModuleName, "path");
        if(attrPath != null)
        {
            webModule.setPath(attrPath.toString());
        }
        return webModule;
    }


    private static void debugLogging(String message)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(message);
        }
    }


    private static void debugLogging(Throwable exc)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(exc);
        }
    }
}
