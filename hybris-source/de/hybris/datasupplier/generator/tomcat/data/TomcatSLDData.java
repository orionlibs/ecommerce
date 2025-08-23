package de.hybris.datasupplier.generator.tomcat.data;

import com.sap.sup.admin.sldsupplier.sc.ProductSoftwareComponentDeployments;
import java.io.Serializable;

public class TomcatSLDData implements Serializable
{
    private static final long serialVersionUID = -4948261960963088675L;
    private TomcatConfiguration tomcatConfiguration;
    private ProductSoftwareComponentDeployments productSoftwareDeployments;
    private DatabaseComponentDeployment databaseDeployments;


    public TomcatSLDData()
    {
    }


    public TomcatSLDData(TomcatConfiguration tomcatConfiguration, ProductSoftwareComponentDeployments productSoftwareDeployments, DatabaseComponentDeployment databaseDeployments)
    {
        this.tomcatConfiguration = tomcatConfiguration;
        this.productSoftwareDeployments = productSoftwareDeployments;
        this.databaseDeployments = databaseDeployments;
    }


    public TomcatConfiguration getTomcatConfiguration()
    {
        return this.tomcatConfiguration;
    }


    public void setTomcatConfiguration(TomcatConfiguration tomcatConfiguration)
    {
        this.tomcatConfiguration = tomcatConfiguration;
    }


    public ProductSoftwareComponentDeployments getProductSoftwareDeployments()
    {
        return this.productSoftwareDeployments;
    }


    public void setProductSoftwareDeployments(ProductSoftwareComponentDeployments productSoftwareDeployments)
    {
        this.productSoftwareDeployments = productSoftwareDeployments;
    }


    public DatabaseComponentDeployment getDatabaseDeployments()
    {
        return this.databaseDeployments;
    }


    public void setDatabaseDeployments(DatabaseComponentDeployment databaseDeployments)
    {
        this.databaseDeployments = databaseDeployments;
    }
}
