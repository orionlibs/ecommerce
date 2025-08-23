package de.hybris.bootstrap.typesystem;

import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.bootstrap.config.PlatformConfig;
import de.hybris.bootstrap.typesystem.xml.HybrisTypeSystemParser;
import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.bootstrap.xml.XMLSchemaValidator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;

public class YTypeSystemSource
{
    private static final Logger LOG = Logger.getLogger(YTypeSystemSource.class.getName());
    private final List<ExtensionInfo> extensionCfgs;
    private final YTypeSystemHandler typeSystemLoader;
    private final OverridenItemsXml overridenItemsXml;
    private final PlatformConfig cfg;
    private YTypeSystem typeSystem;
    private Properties loadedPlatformProps = null;


    public YTypeSystemSource(PlatformConfig cfg, YTypeSystemHandler loader, OverridenItemsXml overridenItemsXml)
    {
        this.cfg = cfg;
        this.typeSystemLoader = loader;
        this.overridenItemsXml = overridenItemsXml;
        this.extensionCfgs = Collections.unmodifiableList(this.cfg.getExtensionInfosInBuildOrder());
    }


    public YTypeSystem getTypeSystem()
    {
        if(this.typeSystem == null)
        {
            this.typeSystem = readTypeSystem();
        }
        return this.typeSystem;
    }


    protected Properties getPlatformProperties()
    {
        if(this.loadedPlatformProps == null)
        {
            this.loadedPlatformProps = new Properties();
            ConfigUtil.loadRuntimeProperties(this.loadedPlatformProps, this.cfg);
        }
        return this.loadedPlatformProps;
    }


    protected PlatformConfig getPlatformConfig()
    {
        return this.cfg;
    }


    protected List<ExtensionInfo> getExtensionCfgs()
    {
        return this.extensionCfgs;
    }


    private YTypeSystem readTypeSystem()
    {
        HybrisTypeSystemParser parser = null;
        parser = new HybrisTypeSystemParser(this.typeSystemLoader, true);
        for(ExtensionInfo info : this.extensionCfgs)
        {
            if(info.getCoreModule() != null)
            {
                String extName = info.getName();
                this.typeSystemLoader.addExtension(extName, Collections.EMPTY_SET);
                File resDir = new File(info.getExtensionDirectory(), "resources");
                if(!resDir.exists())
                {
                    continue;
                }
                File items = findItemsXml(resDir, extName);
                if(!items.exists())
                {
                    continue;
                }
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("USING items.xml:" + items);
                }
                try
                {
                    validateXml(items);
                    parser.parseExtensionSystem(extName, new FileInputStream(items));
                }
                catch(ParseAbortException e)
                {
                    throw new IllegalArgumentException("unexpected parse error at " + extName + "-items.xml: " + e.getMessage(), e);
                }
                catch(FileNotFoundException e)
                {
                    throw new IllegalArgumentException("unexpected error at " + extName + "-items.xml: " + e.getMessage(), e);
                }
                parseDeploymentInformation(this.typeSystemLoader, parser, extName, resDir);
            }
        }
        this.typeSystemLoader.finish();
        this.typeSystemLoader.validate();
        return this.typeSystemLoader.getSystem();
    }


    private void parseDeploymentInformation(YTypeSystemHandler loader, HybrisTypeSystemParser parser, String extName, File resDir)
    {
        File deployments = new File(resDir, extName + "-advanced-deployment.xml");
        if(!deployments.exists())
        {
            deployments = new File(resDir, "jar/" + extName + ".advanced-deployment.xml");
        }
        if(deployments.exists())
        {
            try
            {
                parser.parseExtensionDeployments(loader.getSystem().getExtension(extName), new FileInputStream(deployments));
            }
            catch(ParseAbortException e)
            {
                throw new IllegalArgumentException("unexpected parse error at " + extName + "-advanced-deployment.xml: " + e
                                .getMessage(), e);
            }
            catch(FileNotFoundException e)
            {
                throw new IllegalArgumentException("unexpected error at " + extName + "-advanced-deployment.xml: " + e
                                .getMessage(), e);
            }
        }
    }


    private File findItemsXml(File resDir, String extName)
    {
        if(this.overridenItemsXml.isOverriden(extName))
        {
            return new File(resDir, this.overridenItemsXml.getOverridingFileName(extName));
        }
        File items = new File(resDir, "items.xml");
        if(!items.exists())
        {
            items = new File(resDir, extName + "-items.xml");
        }
        return items;
    }


    private void validateXml(File xml) throws IllegalArgumentException
    {
        XMLSchemaValidator vali = new XMLSchemaValidator();
        vali.setXmlFile(xml);
        vali.setXsdFile(new File("" + this.cfg.getPlatformHome() + "/resources/schemas/items.xsd"));
        vali.validate();
    }
}
