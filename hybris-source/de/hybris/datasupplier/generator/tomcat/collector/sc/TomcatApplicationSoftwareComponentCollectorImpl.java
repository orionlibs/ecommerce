package de.hybris.datasupplier.generator.tomcat.collector.sc;

import com.sap.sup.admin.sldsupplier.error.InfrastructureException;
import de.hybris.datasupplier.generator.tomcat.data.TomcatConfigObjectUtils;
import de.hybris.datasupplier.generator.tomcat.data.TomcatConfiguration;
import de.hybris.datasupplier.generator.tomcat.data.TomcatEngine;
import de.hybris.datasupplier.generator.tomcat.data.TomcatProductSoftwareComponentDeployment;
import de.hybris.datasupplier.generator.tomcat.data.TomcatServer;
import de.hybris.datasupplier.generator.tomcat.data.TomcatService;
import de.hybris.datasupplier.generator.tomcat.data.TomcatWebModule;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

public class TomcatApplicationSoftwareComponentCollectorImpl extends BaseSoftwareComponentCollectorImpl
{
    private static final String MBEAN_WEBMODULE_QUERY = "*:j2eeType=WebModule,*";
    private static final Logger LOG = Logger.getLogger(TomcatServerSoftwareComponentCollectorImpl.class);


    public String toString()
    {
        return "TomcatApplicationSoftwareComponentCollectorImpl";
    }


    public void collect()
    {
        try
        {
            startCollect();
            endCollect();
        }
        catch(MalformedObjectNameException e)
        {
            throw new InfrastructureException(e.getMessage(), e);
        }
        catch(AttributeNotFoundException e)
        {
            throw new InfrastructureException(e.getMessage(), e);
        }
        catch(InstanceNotFoundException e)
        {
            throw new InfrastructureException(e.getMessage(), e);
        }
        catch(ReflectionException e)
        {
            throw new InfrastructureException(e.getMessage(), e);
        }
        catch(MBeanException e)
        {
            throw new InfrastructureException(e.getMessage(), e);
        }
        catch(IOException e)
        {
            throw new InfrastructureException(e.getMessage(), e);
        }
    }


    protected void endCollect() throws MalformedObjectNameException, ReflectionException, AttributeNotFoundException, MBeanException, InstanceNotFoundException, IOException
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("END collecting product-final software deployments");
        }
    }


    protected void startCollect() throws MalformedObjectNameException, ReflectionException, AttributeNotFoundException, MBeanException, InstanceNotFoundException, IOException
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("START collecting product-final software deployments");
        }
        List webModules = collectWebModules();
        if(webModules.isEmpty())
        {
            setProductSoftwareComponentDeployments(Collections.emptyList());
            if(LOG.isDebugEnabled())
            {
                LOG.debug(" No application software components collected. No deployed Web modules ");
            }
            return;
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Inspecting [" + webModules.size() + "] deployed applications");
        }
        List productSoftwareComponentDeployments = buildProductSoftwareComponentDeployments(webModules);
        setProductSoftwareComponentDeployments(productSoftwareComponentDeployments);
        if(LOG.isDebugEnabled())
        {
            StringBuilder sb = new StringBuilder("(ATLDDS01495). [ApplicationSoftwareComponentCollector]");
            sb.append(" Found [");
            sb.append(productSoftwareComponentDeployments.size());
            sb.append("] application software component objects");
            LOG.debug(sb.toString());
        }
    }


    protected List buildProductSoftwareComponentDeployments(List webModules) throws IOException
    {
        List productSoftwareComponentDeployments = new ArrayList();
        TomcatConfiguration tomcatConfiguration = getTomcatConfiguration();
        Iterator<TomcatWebModule> webModuleIter = webModules.iterator();
        while(webModuleIter.hasNext())
        {
            TomcatWebModule webModule = webModuleIter.next();
            String engineName = webModule.getEngineName();
            if(engineName == null)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(" Warning: Unexpected Tomcat configuration. TomcatModule [" + webModule + "] has no engine name");
                }
                continue;
            }
            TomcatEngine engine = TomcatConfigObjectUtils.findEngine(tomcatConfiguration, engineName);
            if(engine == null)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(" Warning: Unexpected Tomcat configuration. Could not fine engine for TomcatModule [" + webModule + "] and engine name [" + engineName + "]");
                }
                continue;
            }
            TomcatService service = engine.getParentService();
            TomcatServer server = service.getParentServer();
            List deployments = null;
            try
            {
                deployments = buildProductSoftwareComponentDeployments(server, engine, webModule);
            }
            catch(Exception e)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Exception failed while collecting software components for TomcatModule [" + webModule + "] and engine name [" + engineName + "]:" + e
                                    .getMessage());
                    LOG.debug(e);
                }
            }
            if(deployments != null)
            {
                productSoftwareComponentDeployments.addAll(deployments);
            }
        }
        return productSoftwareComponentDeployments;
    }


    protected List buildProductSoftwareComponentDeployments(TomcatServer server, TomcatEngine engine, TomcatWebModule webModule) throws IOException
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(" Start building product information for WebModule [" + webModule + "]");
        }
        List productSoftwareDeployments = buildApplicationSoftwareComponentDeployments(server, engine, webModule);
        if(LOG.isDebugEnabled())
        {
            if(productSoftwareDeployments == null || productSoftwareDeployments.isEmpty())
            {
                LOG.debug("> Did final not find any final application software final component deployments for the WebModule [" + webModule + "]");
            }
            else
            {
                LOG.debug("> Found  [" + productSoftwareDeployments.size() + "] application software component deployments for the WebModule [" + webModule + "]");
            }
        }
        if(productSoftwareDeployments != null && productSoftwareDeployments.isEmpty())
        {
            return Collections.emptyList();
        }
        return productSoftwareDeployments;
    }


    protected List buildApplicationSoftwareComponentDeployments(TomcatServer server, TomcatEngine engine, TomcatWebModule webModule) throws IOException
    {
        File appDeploymentDir = getApplicationDeploymentDir(webModule, engine);
        if(appDeploymentDir == null)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("  Warning: Unexpected Tomcat configuration. Could not find application deployment dir for WebModule [" + webModule + "]");
            }
            return Collections.emptyList();
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("  ... Searching for application software components for [" + webModule + "] in [" + appDeploymentDir + "]");
        }
        TomcatProductSoftwareComponentDeployment template = new TomcatProductSoftwareComponentDeployment();
        template.setServer(server);
        template.setWebModule(webModule);
        List productSoftwareDeployments = null;
        if(appDeploymentDir.isDirectory())
        {
            productSoftwareDeployments = loadApplicationSoftwareComponentDeploymentsFromDir(appDeploymentDir, template);
        }
        else
        {
            String path = appDeploymentDir.getPath();
            if(!path.toLowerCase().endsWith(".war"))
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(" Warning: Unexpected Tomcat configuration. Cannot search for application component descriptors in file  [" + path + "] ");
                }
                return Collections.emptyList();
            }
            productSoftwareDeployments = loadApplicationSoftwareComponentDeploymentsFromWar(appDeploymentDir, template);
        }
        return productSoftwareDeployments;
    }


    protected List collectWebModules() throws MalformedObjectNameException, ReflectionException, AttributeNotFoundException, MBeanException, InstanceNotFoundException
    {
        MBeanServer mbeanServer = getServiceInitializer().getMBeanServer();
        ObjectName objnameForWebModules = new ObjectName("*:j2eeType=WebModule,*");
        Set<ObjectName> webModuleNames = mbeanServer.queryNames(objnameForWebModules, null);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("   ... Building [" + webModuleNames.size() + "] Tomcat WebModule objects");
        }
        Iterator<ObjectName> webModuleIter = webModuleNames.iterator();
        List<TomcatWebModule> webModules = new ArrayList();
        while(webModuleIter.hasNext())
        {
            ObjectName webModuleName = webModuleIter.next();
            if(LOG.isDebugEnabled())
            {
                LOG.debug("  Completing now Tomcat WebModule [" + webModuleName + "]");
            }
            TomcatWebModule webModule = buildWebModule(webModuleName);
            webModules.add(webModule);
        }
        return webModules;
    }
}
