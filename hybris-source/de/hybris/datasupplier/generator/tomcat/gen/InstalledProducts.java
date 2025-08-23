package de.hybris.datasupplier.generator.tomcat.gen;

import com.sap.sup.admin.sldsupplier.data.model.sc.meta.xml.SoftwareComponentVersion;
import com.sap.sup.admin.sldsupplier.gen.BaseInstalledHelpObject;
import com.sap.sup.admin.sldsupplier.sc.ProductSoftwareComponentDeploymentIterator;
import de.hybris.datasupplier.generator.tomcat.data.TomcatServer;
import java.util.Iterator;
import java.util.List;

public class InstalledProducts extends BaseInstalledHelpObject
{
    public InstalledProducts(List deployments)
    {
        super(deployments);
    }


    public Iterator getDeployments()
    {
        ProductVersionIterator result = new ProductVersionIterator(this);
        this.currIterator = (ProductSoftwareComponentDeploymentIterator)result;
        return (Iterator)result;
    }


    public Iterator getDeployments(SoftwareComponentVersion softwareComponentVersion)
    {
        ProductVersionIterator result = new ProductVersionIterator(this, softwareComponentVersion);
        this.currIterator = (ProductSoftwareComponentDeploymentIterator)result;
        return (Iterator)result;
    }


    public Iterator getDeployments(TomcatServer server)
    {
        ProductVersionIterator result = new ProductVersionIterator(this, server);
        this.currIterator = (ProductSoftwareComponentDeploymentIterator)result;
        return (Iterator)result;
    }


    public Iterator getDeployments(SoftwareComponentVersion softwareComponentVersion, TomcatServer server)
    {
        ProductVersionIterator result = new ProductVersionIterator(this, softwareComponentVersion, server);
        this.currIterator = (ProductSoftwareComponentDeploymentIterator)result;
        return (Iterator)result;
    }
}
