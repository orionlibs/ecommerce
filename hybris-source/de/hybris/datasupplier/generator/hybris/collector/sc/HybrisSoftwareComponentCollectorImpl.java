package de.hybris.datasupplier.generator.hybris.collector.sc;

import com.sap.sup.admin.sldsupplier.data.model.sc.meta.xml.ProductInformation;
import com.sap.sup.admin.sldsupplier.data.model.sc.meta.xml.ProductSoftwareComponentDeployment;
import com.sap.sup.admin.sldsupplier.data.model.sc.meta.xml.ProductVersion;
import com.sap.sup.admin.sldsupplier.error.InfrastructureException;
import com.sap.sup.admin.sldsupplier.sc.ProductInformationCreator;
import com.sap.sup.admin.sldsupplier.sc.SoftwareComponentUtils;
import com.sap.sup.admin.sldsupplier.utils.Loader;
import de.hybris.datasupplier.generator.hybris.data.HybrisProductSoftwareComponentDeployment;
import de.hybris.datasupplier.generator.tomcat.collector.sc.BaseSoftwareComponentCollectorImpl;
import de.hybris.datasupplier.generator.tomcat.data.Tomcat;
import de.hybris.datasupplier.generator.tomcat.data.TomcatConfiguration;
import de.hybris.datasupplier.generator.tomcat.data.TomcatServer;
import de.hybris.datasupplier.services.HybrisCollectorService;
import de.hybris.platform.core.Registry;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.management.InstanceNotFoundException;
import org.apache.log4j.Logger;

public class HybrisSoftwareComponentCollectorImpl extends BaseSoftwareComponentCollectorImpl
{
    private static final Logger LOG = Logger.getLogger(HybrisSoftwareComponentCollectorImpl.class);
    private static final String TECHNICAL_RELEASE_ATTRIBUTE = "TechnicalRelease";
    private static final String SCOPE_ATTRIBUTE = "attribute";
    private ProductInformation hybrisPoductInformation;
    private HybrisCollectorService hybrisCollectorService;


    public ProductInformation getHybrisPoductInformation()
    {
        return this.hybrisPoductInformation;
    }


    public void setHybrisPoductInformation(ProductInformation hybrisPoductInformation)
    {
        this.hybrisPoductInformation = hybrisPoductInformation;
    }


    public String toString()
    {
        return "HybrisSoftwareComponentCollectorImpl";
    }


    protected ProductInformationCreator createProductInformationCreator()
    {
        HybrisSCRepositoryXMLParser xmlParser = new HybrisSCRepositoryXMLParser();
        xmlParser.setVerbose(getVerbose());
        return (ProductInformationCreator)xmlParser;
    }


    public void collect()
    {
        try
        {
            startCollect();
            debugLogging("END collecting hybris software component objects");
        }
        catch(InstanceNotFoundException | IOException e)
        {
            throw new InfrastructureException(e.getMessage(), e);
        }
    }


    protected void startCollect() throws InstanceNotFoundException, IOException
    {
        debugLogging("START collecting hybris software component objects");
        ProductInformation hybrisProductInformation = loadHybrisProductInformation();
        setHybrisPoductInformation(hybrisProductInformation);
        debugLogging("Loaded hybris software component/product repository: " + hybrisProductInformation);
        TomcatConfiguration tomcatConfiguration = getTomcatConfiguration();
        Tomcat tomcat = tomcatConfiguration.getTomcat();
        Collection servers = tomcat.getServers();
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
        StringBuilder sb = new StringBuilder("(ATLDDS02549). [HybrisSoftwareComponentCollector]");
        sb.append(" Finished [");
        sb.append(productSoftwareComponentDeployments.size());
        sb.append("] hybris software component objects");
        debugLogging(sb.toString());
        setProductSoftwareComponentDeployments(productSoftwareComponentDeployments);
    }


    protected List buildProductSoftwareComponentDeployments(TomcatServer server) throws InstanceNotFoundException, IOException
    {
        String serverInfo = server.getServerInfo();
        if(serverInfo == null)
        {
            debugLogging("Warning: Unexpected Tomcat configuration.  Server [" + server + "] has no server info.");
            return Collections.emptyList();
        }
        ProductInformation productInformation = getHybrisPoductInformation();
        HybrisProductSoftwareComponentDeployment template = new HybrisProductSoftwareComponentDeployment();
        template.setServer(server);
        List<HybrisProductSoftwareComponentDeployment> allHybrisSoftwareDeployments = SoftwareComponentUtils.resolveBindings(productInformation, (ProductSoftwareComponentDeployment)template);
        for(HybrisProductSoftwareComponentDeployment deployment : allHybrisSoftwareDeployments)
        {
            ProductVersion pVersion = deployment.getProductVersion();
            if(pVersion != null)
            {
                String technicalRelease = pVersion.getTechnicalRelease();
                deployment.setHybrisVersion(technicalRelease);
            }
        }
        debugLogging("       resolveBindings for ProductInformation returned [" + allHybrisSoftwareDeployments.size() + "] server software component objects");
        if(allHybrisSoftwareDeployments.isEmpty())
        {
            return Collections.emptyList();
        }
        String version = server.getVersion();
        if(version == null)
        {
            LOG.error("Unexpected Tomcat configuration. Server [" + server + "] has unknown Tomcat version [" + serverInfo + "]. Open CSS message on SV-SMG-DIA");
            return Collections.emptyList();
        }
        List hybrisDeployments = filterProductSoftwareDeploymentsByVersion(allHybrisSoftwareDeployments,
                        getHybrisCollectorService().getHybrisVersion());
        debugLogging("   Collected [" + allHybrisSoftwareDeployments.size() + "] Hybris software component deployments");
        return hybrisDeployments;
    }


    protected ProductInformation loadHybrisProductInformation()
    {
        try
        {
            InputStream in = Loader.getResourceAsStream("./repositories/hybris_sc_repository.xml");
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


    public void setHybrisCollectorService(HybrisCollectorService hybrisCollectorService)
    {
        this.hybrisCollectorService = hybrisCollectorService;
    }


    public HybrisCollectorService getHybrisCollectorService()
    {
        if(this.hybrisCollectorService == null)
        {
            this.hybrisCollectorService = (HybrisCollectorService)Registry.getApplicationContext().getBean("hybrisCollectorService", HybrisCollectorService.class);
        }
        return this.hybrisCollectorService;
    }


    protected List filterProductSoftwareDeploymentsByVersion(List allHybrisProductSoftwareDeployments, String currentVersion)
    {
        List<HybrisProductSoftwareComponentDeployment> result = new ArrayList();
        debugLogging("Start filtering ProductSoftwareDeploymentsByVersion");
        Iterator<HybrisProductSoftwareComponentDeployment> iter = allHybrisProductSoftwareDeployments.iterator();
        while(iter.hasNext())
        {
            HybrisProductSoftwareComponentDeployment nextDeployment = iter.next();
            debugLogging("Checking running hybris version:" + currentVersion + " with ProductVersion HYBRIS_COM_SU from repo: " + nextDeployment
                            .getHybrisVersion());
            if(currentVersion.startsWith(nextDeployment.getHybrisVersion()))
            {
                debugLogging("Check has passed hybris running version: " + currentVersion + " with ProductVersion HYBRIS_COM_SU from repo: " + nextDeployment
                                .getHybrisVersion());
                if(nextDeployment.getSoftwareComponentVersion() != null)
                {
                    String technicalRelease = (String)nextDeployment.getSoftwareComponentVersion().getAttribute("attribute", "TechnicalRelease");
                    debugLogging("Checking SoftwareComponentVersion HY_COM TechnicalRelease: " + technicalRelease + " with running hybris version:" + currentVersion);
                    if(technicalRelease != null && currentVersion.startsWith(technicalRelease))
                    {
                        debugLogging("SoftwareComponentVersion HY_COM TechnicalRelease: " + technicalRelease + " fits running hybris version:" + currentVersion);
                        result.add(nextDeployment);
                    }
                }
            }
        }
        return result;
    }


    private static void debugLogging(String message)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(message);
        }
    }
}
