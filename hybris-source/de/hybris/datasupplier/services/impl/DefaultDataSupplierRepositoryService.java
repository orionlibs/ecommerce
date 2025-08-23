package de.hybris.datasupplier.services.impl;

import com.sap.sup.admin.sldsupplier.data.model.sc.meta.xml.ProductInformation;
import com.sap.sup.admin.sldsupplier.data.model.sc.meta.xml.ProductSoftwareComponentDeployment;
import com.sap.sup.admin.sldsupplier.sc.DefaultProductInformationXMLParser;
import com.sap.sup.admin.sldsupplier.sc.ProductInformationCreator;
import com.sap.sup.admin.sldsupplier.sc.SoftwareComponentUtils;
import com.sap.sup.admin.sldsupplier.utils.Loader;
import de.hybris.datasupplier.generator.hybris.data.HybrisProductSoftwareComponentDeployment;
import de.hybris.datasupplier.services.DataSupplierRepositoryService;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.log4j.Logger;

public class DefaultDataSupplierRepositoryService implements DataSupplierRepositoryService
{
    private static final Logger LOG = Logger.getLogger(DefaultDataSupplierRepositoryService.class);
    private String hybrisProductTechnicalName;
    private String hybrisSoftwareComponentTechnicalName;


    public void initialize()
    {
        LOG.debug("Initializing DefaultDataSupplierRepositoryService repository service.");
        List<HybrisProductSoftwareComponentDeployment> allHybrisSoftwareDeployments = SoftwareComponentUtils.resolveBindings(
                        loadHybrisProductInformation(), (ProductSoftwareComponentDeployment)new HybrisProductSoftwareComponentDeployment());
        if(allHybrisSoftwareDeployments != null && !allHybrisSoftwareDeployments.isEmpty())
        {
            LOG.debug("Found HybrisProductSoftwareComponentDeployment");
            this.hybrisProductTechnicalName = ((HybrisProductSoftwareComponentDeployment)allHybrisSoftwareDeployments.get(0)).getProductVersion().getTechnicalName();
            this
                            .hybrisSoftwareComponentTechnicalName = ((HybrisProductSoftwareComponentDeployment)allHybrisSoftwareDeployments.get(0)).getSoftwareComponentVersion().getTechnicalName();
        }
        if(this.hybrisProductTechnicalName == null)
        {
            LOG.error("Did not find a product technical name for hybris.");
        }
        if(this.hybrisSoftwareComponentTechnicalName == null)
        {
            LOG.error("Did not find a software component technical name for hybris.");
        }
    }


    public String getHybrisProductTechnicalName()
    {
        return this.hybrisProductTechnicalName;
    }


    public String getHybrisSoftwareComponentTechnicalName()
    {
        return this.hybrisSoftwareComponentTechnicalName;
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
                ProductInformationCreator creator = createProductInformationCreator();
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


    protected ProductInformationCreator createProductInformationCreator()
    {
        return (ProductInformationCreator)new DefaultProductInformationXMLParser();
    }
}
