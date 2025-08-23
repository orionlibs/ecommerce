package de.hybris.datasupplier.generator.tomcat.collector.sc;

import com.sap.sup.admin.sldsupplier.collector.CollectorContext;
import com.sap.sup.admin.sldsupplier.config.SLDSupplierConfiguration;
import com.sap.sup.admin.sldsupplier.error.InfrastructureException;
import com.sap.sup.admin.sldsupplier.error.SLDDataSupplierTechnicalException;
import com.sap.sup.admin.sldsupplier.error.SoftwareComponentDataCollectException;
import com.sap.sup.admin.sldsupplier.sc.ProductSoftwareComponentDeployments;
import de.hybris.datasupplier.generator.hybris.collector.sc.HybrisSoftwareComponentCollectorImpl;
import de.hybris.datasupplier.generator.tomcat.collector.BaseTomcatCollectorServiceImpl;
import de.hybris.datasupplier.generator.tomcat.collector.TomcatSLDDataCollector;
import de.hybris.datasupplier.generator.tomcat.data.Tomcat;
import de.hybris.datasupplier.generator.tomcat.data.TomcatConfiguration;
import de.hybris.datasupplier.generator.tomcat.data.TomcatServer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;

public class TomcatSoftwareComponentCollectorServiceImpl extends BaseTomcatCollectorServiceImpl implements TomcatSoftwareComponentCollectorService
{
    private static final Logger LOG = Logger.getLogger(TomcatSoftwareComponentCollectorServiceImpl.class);


    public String toString()
    {
        return "TomcatSoftwareComponentCollectorServiceImpl";
    }


    public ProductSoftwareComponentDeployments collectSoftwareComponents(TomcatConfiguration tomcatConfiguration) throws SoftwareComponentDataCollectException
    {
        ProductSoftwareComponentDeployments result = null;
        try
        {
            result = startCollectSoftwareComponents(tomcatConfiguration);
        }
        catch(InfrastructureException e)
        {
            throw new SoftwareComponentDataCollectException(e);
        }
        catch(SecurityException e)
        {
            throw new SoftwareComponentDataCollectException(e);
        }
        catch(Exception e)
        {
            throw new SLDDataSupplierTechnicalException(e);
        }
        return result;
    }


    protected ProductSoftwareComponentDeployments startCollectSoftwareComponents(TomcatConfiguration tomcatConfiguration)
    {
        List appProductSoftwareDeployments = collectApplicationSoftwareComponents(tomcatConfiguration);
        List serverRepositoryDeployments = collectServerSoftwareComponents(tomcatConfiguration);
        if(LOG.isDebugEnabled())
        {
            StringBuilder sb = new StringBuilder("(ATLDDS01490)");
            sb.append("[SoftwareComponentCollectorService] Tomcat software component list contains : [");
            if(appProductSoftwareDeployments != null)
            {
                sb.append(appProductSoftwareDeployments.size());
            }
            else
            {
                sb.append("0");
            }
            sb.append("] application software components");
            sb.append("  and [");
            if(serverRepositoryDeployments != null)
            {
                sb.append(serverRepositoryDeployments.size());
            }
            else
            {
                sb.append("0");
            }
            sb.append("]  server software components");
            LOG.debug(sb.toString());
        }
        List productSoftwareDeployments = new ArrayList();
        if(appProductSoftwareDeployments != null && !appProductSoftwareDeployments.isEmpty())
        {
            productSoftwareDeployments.addAll(appProductSoftwareDeployments);
        }
        if(serverRepositoryDeployments != null && !serverRepositoryDeployments.isEmpty())
        {
            productSoftwareDeployments.addAll(serverRepositoryDeployments);
        }
        if(!productSoftwareDeployments.isEmpty())
        {
            productSoftwareDeployments = enhanceProductSoftwareDeployments(productSoftwareDeployments);
            if(LOG.isDebugEnabled())
            {
                LOG.debug(" [SoftwareComponentCollectorService] got [" + productSoftwareDeployments.size() + "] enhanced software component deployment objects ");
            }
        }
        return new ProductSoftwareComponentDeployments(productSoftwareDeployments);
    }


    protected List collectApplicationSoftwareComponents(TomcatConfiguration tomcatConfiguration)
    {
        CollectorContext context = getCollectorContext();
        SLDSupplierConfiguration sldConfiguration = context.getSLDSupplierConfiguration();
        boolean softwareComponentLookupDisabled = sldConfiguration.getApplicationComponentLookupDisabled();
        if(softwareComponentLookupDisabled)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(" [SoftwareComponentCollectorService] Application software component collection is DISABLED ");
            }
            return Collections.emptyList();
        }
        TomcatApplicationSoftwareComponentCollectorImpl applicationCollector = createApplicationSoftwareComponentCollector(tomcatConfiguration);
        applicationCollector.collect();
        return applicationCollector.getProductSoftwareComponentDeployments();
    }


    protected List collectServerSoftwareComponents(TomcatConfiguration tomcatConfiguration)
    {
        CollectorContext context = getCollectorContext();
        SLDSupplierConfiguration sldConfiguration = context.getSLDSupplierConfiguration();
        boolean softwareComponentLookupDisabled = sldConfiguration.getServerComponentLookupDisabled();
        if(softwareComponentLookupDisabled)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(" [SoftwareComponentCollectorService] Server software component collection is DISABLED ");
            }
            return Collections.emptyList();
        }
        List allSoftwareComponent = new ArrayList();
        TomcatServerSoftwareComponentCollectorImpl serverCollector = createServerSoftwareComponentCollector(tomcatConfiguration);
        serverCollector.collect();
        List serverProductSoftwareDeployments = serverCollector.getProductSoftwareComponentDeployments();
        allSoftwareComponent.addAll(serverProductSoftwareDeployments);
        HybrisSoftwareComponentCollectorImpl hybrisCollector = createHybrisSoftwareComponentCollector(tomcatConfiguration);
        hybrisCollector.collect();
        List hybrisProductSoftwareDeployments = hybrisCollector.getProductSoftwareComponentDeployments();
        allSoftwareComponent.addAll(hybrisProductSoftwareDeployments);
        return allSoftwareComponent;
    }


    protected TomcatApplicationSoftwareComponentCollectorImpl createApplicationSoftwareComponentCollector(TomcatConfiguration tomcatConfiguration)
    {
        Tomcat tomcat = tomcatConfiguration.getTomcat();
        Collection<TomcatServer> servers = tomcat.getServers();
        TomcatApplicationSoftwareComponentCollectorImpl collector = null;
        if(servers.isEmpty())
        {
            collector = new TomcatApplicationSoftwareComponentCollectorImpl();
        }
        else
        {
            TomcatServer server = servers.iterator().next();
            String version = server.getVersion();
            if(Double.parseDouble(version) >= Double.parseDouble("7.0"))
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(" Using Tomcat app software collector");
                }
                TomcatApplicationSoftwareComponentCollector70Impl tomcatApplicationSoftwareComponentCollector70Impl = new TomcatApplicationSoftwareComponentCollector70Impl();
            }
            else
            {
                collector = new TomcatApplicationSoftwareComponentCollectorImpl();
            }
        }
        collector.setTomcatConfiguration(tomcatConfiguration);
        initializeCollector((TomcatSLDDataCollector)collector);
        return collector;
    }


    protected HybrisSoftwareComponentCollectorImpl createHybrisSoftwareComponentCollector(TomcatConfiguration tomcatConfiguration)
    {
        HybrisSoftwareComponentCollectorImpl collector = new HybrisSoftwareComponentCollectorImpl();
        collector.setTomcatConfiguration(tomcatConfiguration);
        initializeCollector((TomcatSLDDataCollector)collector);
        return collector;
    }


    protected TomcatServerSoftwareComponentCollectorImpl createServerSoftwareComponentCollector(TomcatConfiguration tomcatConfiguration)
    {
        TomcatServerSoftwareComponentCollectorImpl collector = null;
        Tomcat tomcat = tomcatConfiguration.getTomcat();
        Collection<TomcatServer> servers = tomcat.getServers();
        if(servers.isEmpty())
        {
            collector = new TomcatServerSoftwareComponentCollectorImpl();
        }
        else
        {
            TomcatServer server = servers.iterator().next();
            String version = server.getVersion();
            if(Double.parseDouble(version) >= Double.parseDouble("7.0"))
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(" Using Tomcat server software collector");
                }
                TomcatServerSoftwareComponentCollector70Impl tomcatServerSoftwareComponentCollector70Impl = new TomcatServerSoftwareComponentCollector70Impl();
            }
            else
            {
                collector = new TomcatServerSoftwareComponentCollectorImpl();
            }
        }
        collector.setTomcatConfiguration(tomcatConfiguration);
        initializeCollector((TomcatSLDDataCollector)collector);
        return collector;
    }


    protected List enhanceProductSoftwareDeployments(List productSoftwareDeployments)
    {
        return productSoftwareDeployments;
    }
}
