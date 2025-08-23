package de.hybris.datasupplier.generator.tomcat.data;

import com.sap.sup.admin.sldsupplier.data.model.sc.meta.xml.Instance;
import com.sap.sup.admin.sldsupplier.data.model.sc.meta.xml.ProductSoftwareComponentDeployment;
import com.sap.sup.admin.sldsupplier.data.model.sc.meta.xml.ProductVersion;
import com.sap.sup.admin.sldsupplier.data.model.sc.meta.xml.SoftwareComponentVersion;

public class TomcatProductSoftwareComponentDeployment extends ProductSoftwareComponentDeployment
{
    private static final long serialVersionUID = 2306333307804069153L;
    private TomcatServer server;
    private TomcatWebModule webModule;


    public TomcatProductSoftwareComponentDeployment()
    {
    }


    public TomcatProductSoftwareComponentDeployment(ProductVersion productVersion, Instance instance, SoftwareComponentVersion softwareComponentVersion)
    {
        super(productVersion, instance, softwareComponentVersion);
    }


    public ProductSoftwareComponentDeployment copy()
    {
        return new TomcatProductSoftwareComponentDeployment(getProductVersion(), getInstance(), getSoftwareComponentVersion());
    }


    public ProductSoftwareComponentDeployment copy(ProductVersion productVersion, Instance instance, SoftwareComponentVersion softwareComponentVersion)
    {
        TomcatProductSoftwareComponentDeployment copy = new TomcatProductSoftwareComponentDeployment(productVersion, instance, softwareComponentVersion);
        copy.setServer(this.server);
        copy.setWebModule(this.webModule);
        return copy;
    }


    public String toString()
    {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(";");
        if(this.server != null)
        {
            sb.append(this.server);
        }
        else
        {
            sb.append("NULL");
        }
        sb.append(";");
        return sb.toString();
    }


    public TomcatWebModule getWebModule()
    {
        return this.webModule;
    }


    public void setWebModule(TomcatWebModule webModule)
    {
        this.webModule = webModule;
    }


    public TomcatServer getServer()
    {
        return this.server;
    }


    public void setServer(TomcatServer server)
    {
        this.server = server;
    }
}
