package de.hybris.datasupplier.generator.tomcat.gen;

import com.sap.sup.admin.sldsupplier.data.model.sc.meta.xml.ProductVersion;
import com.sap.sup.admin.sldsupplier.gen.BaseInstalledHelpObject;
import com.sap.sup.admin.sldsupplier.sc.ProductSoftwareComponentDeploymentIterator;
import de.hybris.datasupplier.generator.tomcat.data.TomcatServer;
import java.util.Iterator;
import java.util.List;

public class InstalledSoftwareComponents extends BaseInstalledHelpObject
{
    public InstalledSoftwareComponents(List deployments)
    {
        super(deployments);
    }


    public Iterator getDeployments()
    {
        SoftwareComponentVersionIterator result = new SoftwareComponentVersionIterator(this);
        this.currIterator = (ProductSoftwareComponentDeploymentIterator)result;
        return (Iterator)result;
    }


    public Iterator getDeployments(ProductVersion productVersion)
    {
        SoftwareComponentVersionIterator result = new SoftwareComponentVersionIterator(this, productVersion);
        this.currIterator = (ProductSoftwareComponentDeploymentIterator)result;
        return (Iterator)result;
    }


    public Iterator getDeployments(TomcatServer serverFilter)
    {
        SoftwareComponentVersionIterator result = new SoftwareComponentVersionIterator(this, serverFilter);
        this.currIterator = (ProductSoftwareComponentDeploymentIterator)result;
        return (Iterator)result;
    }


    public Iterator getDeployments(ProductVersion productVersion, TomcatServer serverFilter)
    {
        SoftwareComponentVersionIterator result = new SoftwareComponentVersionIterator(this, productVersion, serverFilter);
        this.currIterator = (ProductSoftwareComponentDeploymentIterator)result;
        return (Iterator)result;
    }
}
