package de.hybris.datasupplier.generator.tomcat.collector.sc;

import com.sap.sup.admin.sldsupplier.data.model.sc.meta.xml.ProductInformation;
import com.sap.sup.admin.sldsupplier.data.model.sc.meta.xml.ProductSoftwareComponentDeployment;
import com.sap.sup.admin.sldsupplier.error.InfrastructureException;
import com.sap.sup.admin.sldsupplier.sc.ProductInformationCreator;
import com.sap.sup.admin.sldsupplier.sc.SoftwareComponentUtils;
import com.sap.sup.admin.sldsupplier.utils.Loader;
import de.hybris.datasupplier.generator.tomcat.data.Tomcat;
import de.hybris.datasupplier.generator.tomcat.data.TomcatConfigObjectUtils;
import de.hybris.datasupplier.generator.tomcat.data.TomcatConfiguration;
import de.hybris.datasupplier.generator.tomcat.data.TomcatEngine;
import de.hybris.datasupplier.generator.tomcat.data.TomcatProductSoftwareComponentDeployment;
import de.hybris.datasupplier.generator.tomcat.data.TomcatServer;
import de.hybris.datasupplier.generator.tomcat.data.TomcatService;
import de.hybris.datasupplier.generator.tomcat.data.TomcatWebModule;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import org.apache.log4j.Logger;

public class TomcatServerSoftwareComponentCollectorImpl extends BaseSoftwareComponentCollectorImpl
{
    private static final String MBEAN_WEBMODULE_QUERY = "*:j2eeType=WebModule,*";
    public static final String TOMCAT_SLD_DATASUPPLIER_WEB = "TomcatSLDDataSupplierWEB";
    private static final String CAN_NOT_LOAD_TOMCAT = "]. Cannot load Tomcat Server Software Component repository from SLDDS deployment.";
    private static final Logger LOG = Logger.getLogger(TomcatServerSoftwareComponentCollectorImpl.class);
    private ProductInformation tomcatPoductInformation;


    public ProductInformation getTomcatPoductInformation()
    {
        return this.tomcatPoductInformation;
    }


    public void setTomcatPoductInformation(ProductInformation tomcatPoductInformation)
    {
        this.tomcatPoductInformation = tomcatPoductInformation;
    }


    public String toString()
    {
        return "TomcatServerSoftwareComponentCollectorImpl";
    }


    protected ProductInformationCreator createProductInformationCreator()
    {
        TomcatSCRepositoryXMLParser xmlParser = new TomcatSCRepositoryXMLParser();
        xmlParser.setVerbose(getVerbose());
        return (ProductInformationCreator)xmlParser;
    }


    public void collect()
    {
        try
        {
            startCollect();
            debugLogging("END collecting server software component objects");
        }
        catch(MalformedObjectNameException | AttributeNotFoundException | InstanceNotFoundException | ReflectionException | MBeanException | IOException e)
        {
            throw new InfrastructureException(e.getMessage(), e);
        }
    }


    protected void startCollect() throws MalformedObjectNameException, ReflectionException, AttributeNotFoundException, MBeanException, InstanceNotFoundException, IOException
    {
        debugLogging("START collecting server software component objects");
        TomcatConfiguration tomcatConfiguration = getTomcatConfiguration();
        Tomcat tomcat = tomcatConfiguration.getTomcat();
        Collection servers = tomcat.getServers();
        if(servers.isEmpty())
        {
            setProductSoftwareComponentDeployments(Collections.emptyList());
            debugLogging("Unexpected Tomcat configuration. No Tomcat Server configuration objects collected. Will not assign server software components");
            return;
        }
        ProductInformation tomcatProductInformation = loadTomcatProductInformation();
        if(tomcatProductInformation == null)
        {
            tomcatProductInformation = loadTomcatServerProductInformationFromWar();
        }
        if(tomcatProductInformation == null)
        {
            debugLogging(" Could not load Tomcat Server software component/product repository. Cannot match Tomcat version and report Tomcat software components to SLD. Open CSS message on SV-SMG-DIA");
            setProductSoftwareComponentDeployments(Collections.emptyList());
            return;
        }
        setTomcatPoductInformation(tomcatProductInformation);
        debugLogging("Loaded Tomcat server software component/product repository: " + tomcatProductInformation);
        List productSoftwareComponentDeployments = new ArrayList();
        Iterator<TomcatServer> serverIter = servers.iterator();
        while(serverIter.hasNext())
        {
            TomcatServer server = serverIter.next();
            List deploymentes = buildProductSoftwareComponentDeployments(server);
            if(deploymentes != null)
            {
                productSoftwareComponentDeployments.addAll(deploymentes);
            }
        }
        StringBuilder sb = new StringBuilder("(ATLDDS02549). [ServerSoftwareComponentCollector]");
        sb.append(" Finished [");
        sb.append(productSoftwareComponentDeployments.size());
        sb.append("] server software component objects");
        debugLogging(sb.toString());
        setProductSoftwareComponentDeployments(productSoftwareComponentDeployments);
    }


    protected List buildProductSoftwareComponentDeployments(TomcatServer server)
    {
        String serverInfo = server.getServerInfo();
        if(serverInfo == null)
        {
            debugLogging("Warning: Unexpected Tomcat configuration.  Server [" + server + "] has no server info.");
            return Collections.emptyList();
        }
        ProductInformation productInformation = getTomcatPoductInformation();
        TomcatProductSoftwareComponentDeployment template = new TomcatProductSoftwareComponentDeployment();
        template.setServer(server);
        List allProductSoftwareDeployments = SoftwareComponentUtils.resolveBindings(productInformation, (ProductSoftwareComponentDeployment)template);
        debugLogging("       resolveBindings for ProductInformation returned [" + allProductSoftwareDeployments.size() + "] server software component objects");
        if(allProductSoftwareDeployments.isEmpty())
        {
            return Collections.emptyList();
        }
        String version = server.getVersion();
        if(version == null)
        {
            debugLogging("Unexpected Tomcat configuration. Server [" + server + "] has unknown Tomcat version [" + serverInfo + "]. Open CSS message on SV-SMG-DIA");
            return Collections.emptyList();
        }
        List tomcatServerDeployments = SoftwareComponentUtils.filterProductSoftwareDeploymentsByVersion(allProductSoftwareDeployments, version);
        debugLogging("   Collected [" + tomcatServerDeployments
                        .size() + "] Tomcat server software component deployments");
        return tomcatServerDeployments;
    }


    protected ProductInformation loadTomcatProductInformation()
    {
        try
        {
            InputStream in = Loader.getResourceAsStream("./repositories/tomcat_sc_repository.xml");
            try
            {
                if(in == null)
                {
                    ProductInformation productInformation1 = null;
                    if(in != null)
                    {
                        in.close();
                    }
                    return productInformation1;
                }
                ProductInformationCreator creator = getCreator();
                ProductInformation productInformation = creator.createProductInformation(in);
                if(in != null)
                {
                    in.close();
                }
                return productInformation;
            }
            catch(Throwable throwable)
            {
                if(in != null)
                {
                    try
                    {
                        in.close();
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
            LOG.error(e, e);
            return null;
        }
    }


    protected ProductInformation loadTomcatServerProductInformationFromWar() throws MalformedObjectNameException, ReflectionException, AttributeNotFoundException, MBeanException, InstanceNotFoundException, IOException
    {
        TomcatWebModule thisModule = findSldDataSupplierWebModule();
        if(thisModule == null)
        {
            debugLogging("Could not find SLDDS Web application [TomcatSLDDataSupplierWEB]. Cannot load Tomcat Server Software Component repository from SLDDS deployment.");
            return null;
        }
        String engineName = thisModule.getEngineName();
        if(engineName == null)
        {
            debugLogging("Unexpected Tomcat configuration. The Tomcat SLDDS WebModule [" + thisModule + "] has no engine name. Cannot load Tomcat Server Software Component repository from SLDDS deployment.");
            return null;
        }
        TomcatConfiguration tomcatConfiguration = getTomcatConfiguration();
        TomcatEngine engine = TomcatConfigObjectUtils.findEngine(tomcatConfiguration, engineName);
        if(engine == null)
        {
            debugLogging("Unexpected Tomcat configuration. No engine found for SLDDS WebModule [" + thisModule + "]. Cannot load  Tomcat Server Software Component repository from SLDDS deployment..");
            return null;
        }
        File appDeploymentDir = getApplicationDeploymentDir(thisModule, engine);
        if(appDeploymentDir == null)
        {
            debugLogging("Could not detect application deployment dir of the SLDDS WebModule. Cannot load Tomcat Server Software Component repository from SLDDS deployment.");
            return null;
        }
        debugLogging("Searching for Tomcat Server Software Component repository from SLDDS deployment in [" + appDeploymentDir + "]");
        TomcatProductSoftwareComponentDeployment template = new TomcatProductSoftwareComponentDeployment();
        TomcatService service = engine.getParentService();
        if(service == null)
        {
            debugLogging("Unexpected Tomcat configuration. No service object found for TomcatEngine  [" + engine + "]. Cannot load Tomcat Server Software Component repository from SLDDS deployment.");
            return null;
        }
        TomcatServer server = service.getParentServer();
        if(server == null)
        {
            debugLogging("Unexpected Tomcat configuration. No server object found for TomcatEngine  [" + engine + "]. Cannot load Tomcat Server Software Component repository from SLDDS deployment.");
            return null;
        }
        template.setServer(server);
        template.setWebModule(thisModule);
        List<ProductInformation> productSoftwareDeployments = null;
        if(appDeploymentDir.isDirectory())
        {
            productSoftwareDeployments = loadApplicationSoftwareComponentDeploymentsFromDir(appDeploymentDir, template);
        }
        else
        {
            String path = appDeploymentDir.getPath();
            if(!path.toLowerCase().endsWith(".war"))
            {
                debugLogging("Cannot search for application component descriptors in  [" + path + "]. Cannot load Tomcat Server Software Component repository from SLDDS deployment.");
                return null;
            }
            productSoftwareDeployments = loadApplicationSoftwareComponentDeploymentsFromWar(appDeploymentDir, template);
        }
        if(productSoftwareDeployments == null || productSoftwareDeployments.isEmpty())
        {
            debugLogging("Could not find Tomcat Server Component repository from SLDDS .WAR");
            return null;
        }
        return productSoftwareDeployments.get(0);
    }


    protected TomcatWebModule findSldDataSupplierWebModule() throws MalformedObjectNameException, ReflectionException, AttributeNotFoundException, MBeanException, InstanceNotFoundException
    {
        MBeanServer mbeanServer = getServiceInitializer().getMBeanServer();
        ObjectName objnameForWebModules = new ObjectName("*:j2eeType=WebModule,*");
        Set<ObjectName> webModuleNames = mbeanServer.queryNames(objnameForWebModules, null);
        debugLogging("  Searching for SLD-DS WebModule by name [TomcatSLDDataSupplierWEB] in [" + webModuleNames.size() + "] deployed applications");
        Iterator<ObjectName> webModuleIter = webModuleNames.iterator();
        while(webModuleIter.hasNext())
        {
            ObjectName webModuleName = webModuleIter.next();
            String name = webModuleName.getCanonicalName();
            if(name.indexOf("TomcatSLDDataSupplierWEB") < 0)
            {
                continue;
            }
            debugLogging("Found [" + webModuleName + "] TomcatSLDDataSupplierWEB WebModule");
            return buildWebModule(webModuleName);
        }
        return null;
    }


    private static void debugLogging(String message)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(message);
        }
    }
}
