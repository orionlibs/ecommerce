package de.hybris.datasupplier.generator.hybris.data;

import com.sap.sup.admin.sldsupplier.data.model.sc.meta.xml.Instance;
import com.sap.sup.admin.sldsupplier.data.model.sc.meta.xml.ProductSoftwareComponentDeployment;
import com.sap.sup.admin.sldsupplier.data.model.sc.meta.xml.ProductVersion;
import com.sap.sup.admin.sldsupplier.data.model.sc.meta.xml.SoftwareComponentVersion;
import de.hybris.datasupplier.generator.tomcat.data.TomcatServer;
import de.hybris.datasupplier.generator.tomcat.data.TomcatWebModule;

public class HybrisProductSoftwareComponentDeployment extends ProductSoftwareComponentDeployment
{
    private TomcatServer server;
    private TomcatWebModule webModule;
    private String hybrisVersion;


    public HybrisProductSoftwareComponentDeployment()
    {
    }


    public HybrisProductSoftwareComponentDeployment(ProductVersion productVersion, Instance instance, SoftwareComponentVersion softwareComponentVersion)
    {
        super(productVersion, instance, softwareComponentVersion);
    }


    public ProductSoftwareComponentDeployment copy()
    {
        return new HybrisProductSoftwareComponentDeployment(getProductVersion(), getInstance(), getSoftwareComponentVersion());
    }


    public ProductSoftwareComponentDeployment copy(ProductVersion productVersion, Instance instance, SoftwareComponentVersion softwareComponentVersion)
    {
        HybrisProductSoftwareComponentDeployment copy = new HybrisProductSoftwareComponentDeployment(productVersion, instance, softwareComponentVersion);
        copy.setServer(this.server);
        copy.setWebModule(this.webModule);
        copy.setHybrisVersion(this.hybrisVersion);
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


    public String getHybrisVersion()
    {
        return this.hybrisVersion;
    }


    public void setHybrisVersion(String hybrisVersion)
    {
        this.hybrisVersion = hybrisVersion;
    }
}
