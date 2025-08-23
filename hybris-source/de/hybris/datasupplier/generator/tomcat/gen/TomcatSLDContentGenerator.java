package de.hybris.datasupplier.generator.tomcat.gen;

import com.sap.sup.admin.sldsupplier.error.ContentGenerationException;
import com.sap.sup.admin.sldsupplier.gen.velocity.BaseVelocitySLDContentGenerator;
import com.sap.sup.admin.sldsupplier.sc.ProductSoftwareComponentDeployments;
import de.hybris.datasupplier.generator.tomcat.SLDDSInfo;
import de.hybris.datasupplier.generator.tomcat.Version;
import de.hybris.datasupplier.generator.tomcat.data.TomcatConfiguration;
import de.hybris.datasupplier.generator.tomcat.data.TomcatSLDData;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TomcatSLDContentGenerator extends BaseVelocitySLDContentGenerator
{
    public static final String KEY_PRODUCT_SOFTWARE_COMPONENT_DEPLOYMENTS = "ProductSoftwareComponentDeployments";
    public static final String KEY_TOMCAT_CONFIGURATION = "TomcatConfiguration";
    public static final String KEY_DATABASE_COMPONENT = "DatabaseComponent";
    private String sldContent;


    public String getSldContent()
    {
        return this.sldContent;
    }


    public String toString()
    {
        return "TomcatSLDContentGenerator";
    }


    public String generateSLD(TomcatSLDData sldData) throws IOException, ContentGenerationException
    {
        TomcatConfiguration tomcatConfig = sldData.getTomcatConfiguration();
        ProductSoftwareComponentDeployments productSoftwareDeployments = sldData.getProductSoftwareDeployments();
        List deployments = productSoftwareDeployments.getDeployments();
        InstalledSoftwareFeatures softwareFeatures = new InstalledSoftwareFeatures(deployments);
        InstalledProducts products = new InstalledProducts(deployments);
        InstalledSoftwareComponents components = new InstalledSoftwareComponents(deployments);
        DatabaseComponent dbComponents = new DatabaseComponent(sldData.getDatabaseDeployments());
        String configInfo = tomcatConfig.toString();
        String productSoftwareInfo = productSoftwareDeployments.toShortString();
        SLDDSInfo sldInfo = new SLDDSInfo(configInfo, productSoftwareInfo);
        Map<Object, Object> paramMap = new HashMap<>();
        paramMap.put("TomcatConfiguration", tomcatConfig);
        paramMap.put("SLDInfo", sldInfo);
        paramMap.put("ProductSoftwareComponentDeployments", productSoftwareDeployments);
        paramMap.put("InstalledSoftwareFeatures", softwareFeatures);
        paramMap.put("InstalledSoftwareComponents", components);
        paramMap.put("InstalledProducts", products);
        paramMap.put("DatabaseComponent", dbComponents);
        paramMap.put("version", Version.VERSION);
        this.sldContent = generateSLD("/velocity/tomcat_sld_result.vm", paramMap);
        return this.sldContent;
    }
}
