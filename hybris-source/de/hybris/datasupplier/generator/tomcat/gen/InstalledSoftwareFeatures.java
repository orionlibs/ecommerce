package de.hybris.datasupplier.generator.tomcat.gen;

import com.sap.sup.admin.sldsupplier.data.model.sc.meta.xml.ProductVersion;
import com.sap.sup.admin.sldsupplier.gen.BaseInstalledHelpObject;
import com.sap.sup.admin.sldsupplier.sc.ProductSoftwareComponentDeploymentIterator;
import de.hybris.datasupplier.generator.tomcat.data.TomcatServer;
import java.util.Iterator;
import java.util.List;

public class InstalledSoftwareFeatures extends BaseInstalledHelpObject
{
    public InstalledSoftwareFeatures(List deployments)
    {
        super(deployments);
    }


    public Iterator getDeployments()
    {
        InstanceIterator result = new InstanceIterator(this);
        this.currIterator = (ProductSoftwareComponentDeploymentIterator)result;
        return (Iterator)result;
    }


    public Iterator getDeployments(ProductVersion productVersion)
    {
        InstanceIterator result = new InstanceIterator(this, productVersion);
        this.currIterator = (ProductSoftwareComponentDeploymentIterator)result;
        return (Iterator)result;
    }


    public Iterator getDeployments(TomcatServer server)
    {
        InstanceIterator result = new InstanceIterator(this, server);
        this.currIterator = (ProductSoftwareComponentDeploymentIterator)result;
        return (Iterator)result;
    }


    public Iterator getDeployments(ProductVersion productVersion, TomcatServer server)
    {
        return (Iterator)new InstanceIterator(this, productVersion, server);
    }
}
