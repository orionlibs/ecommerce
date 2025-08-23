package de.hybris.platform.persistence.hjmpgen;

import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.bootstrap.config.PlatformConfig;
import de.hybris.bootstrap.config.SystemConfig;
import de.hybris.bootstrap.typesystem.YDeployment;
import de.hybris.bootstrap.typesystem.YExtension;
import de.hybris.bootstrap.typesystem.YTypeSystem;
import de.hybris.bootstrap.typesystem.YTypeSystemHandler;
import de.hybris.bootstrap.typesystem.YTypeSystemLoader;
import de.hybris.bootstrap.typesystem.xml.HybrisTypeSystemParser;
import de.hybris.platform.core.DeploymentImpl;
import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.util.typesystem.ParsedDeployments;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HJMPGenerator
{
    private final String extName;
    private final PlatformConfig cfg;
    private final YTypeSystem system;
    private final DeploymentImpl deploymentinfo;
    private final Map<ExtensionInfo, File> targetDirectories;


    public static void main(String[] args)
    {
        try
        {
            SystemConfig systemConfig = ConfigUtil.getSystemConfig(args[0]);
            (new HJMPGenerator(PlatformConfig.getInstance(systemConfig), args[1])).generate();
        }
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
    }


    public HJMPGenerator(PlatformConfig cfg, String extensionName)
    {
        this(cfg, parseDeployments(cfg, extensionName), extensionName);
    }


    public HJMPGenerator(PlatformConfig cfg, YTypeSystem system)
    {
        this(cfg, system, null);
    }


    private HJMPGenerator(PlatformConfig cfg, YTypeSystem system, String extensionName)
    {
        this.cfg = cfg;
        this.extName = extensionName;
        this.system = system;
        this
                        .deploymentinfo = !"core".equalsIgnoreCase(extensionName) ? (DeploymentImpl)new ParsedDeployments(this.system, Collections.singleton(this.system.getExtension("core"))) : (DeploymentImpl)new ParsedDeployments(this.system);
        this.targetDirectories = createTargetDirectories(cfg);
    }


    public void generate() throws IOException, ClassNotFoundException
    {
        List<ExtensionInfo> extensionsToGo = (this.extName != null) ? Collections.<ExtensionInfo>singletonList(this.cfg.getExtensionInfo(this.extName)) : this.cfg.getExtensionInfosInBuildOrder();
        for(ExtensionInfo extInfo : extensionsToGo)
        {
            if(extInfo.getCoreModule() == null)
            {
                continue;
            }
            File targetDirectory = this.targetDirectories.get(extInfo);
            for(YDeployment yDepl : this.system.getExtension(extInfo.getName()).getOwnDeployments())
            {
                if(yDepl.isAbstract() || yDepl.isNonItemDeployment())
                {
                    continue;
                }
                ItemDeployment itemDeployment = this.deploymentinfo.getItemDeployment(yDepl.getFullName());
                if(itemDeployment == null)
                {
                    System.err.println("missing deployment " + extInfo.getName() + "::" + yDepl.getFullName() + " - got " + this.deploymentinfo
                                    .getBeanIDs() + " in " + this.deploymentinfo);
                    continue;
                }
                generate(itemDeployment, targetDirectory);
            }
            (new File(targetDirectory, "touch.tmp")).delete();
            try
            {
                (new File(targetDirectory, "touch.tmp")).createNewFile();
            }
            catch(IOException iOException)
            {
            }
        }
    }


    protected Map<ExtensionInfo, File> createTargetDirectories(PlatformConfig cfg)
    {
        Map<ExtensionInfo, File> ret = new HashMap<>();
        for(ExtensionInfo extInf : cfg.getExtensionInfosInBuildOrder())
        {
            if(extInf.getCoreModule() != null)
            {
                File f = new File(extInf.getExtensionDirectory(), "gensrc");
                if(!f.exists())
                {
                    f.mkdir();
                }
                ret.put(extInf, f);
            }
        }
        return ret;
    }


    protected void generate(ItemDeployment itemDeployment, File targetDir) throws IOException, ClassNotFoundException
    {
        if(itemDeployment.isAbstract())
        {
            return;
        }
        if(itemDeployment.isGeneric() && "de.hybris.platform.persistence.GenericItemHome"
                        .equalsIgnoreCase(itemDeployment.getHomeInterface().getName()))
        {
            return;
        }
        if(itemDeployment.getName().equals("de.hybris.platform.persistence.GenericItem"))
        {
            return;
        }
        if(itemDeployment.isGeneric() && "de.hybris.platform.persistence.link.LinkHome"
                        .equalsIgnoreCase(itemDeployment.getHomeInterface().getName()))
        {
            return;
        }
        if(itemDeployment.getName().equals("de.hybris.platform.persistence.link.Link"))
        {
            return;
        }
        HJMPEntityBean bean = new HJMPEntityBean(itemDeployment);
        bean.writeToFile(targetDir);
    }


    protected static YTypeSystem parseDeployments(PlatformConfig cfg, String extensionName)
    {
        YTypeSystemLoader loader = null;
        HybrisTypeSystemParser parser = null;
        try
        {
            loader = new YTypeSystemLoader(false);
            parser = new HybrisTypeSystemParser((YTypeSystemHandler)loader, false);
            for(ExtensionInfo extInfo : "core".equals(extensionName) ?
                            Collections.<T>singletonList((T)cfg.getExtensionInfo("core")) : Arrays.<T>asList((T[])new ExtensionInfo[] {cfg.getExtensionInfo("core"), cfg
                            .getExtensionInfo(extensionName)}))
            {
                YExtension ext = null;
                File itemsXml = (extInfo.getCoreModule() != null) ? new File(extInfo.getExtensionDirectory(), "resources/" + extInfo.getName() + "-items.xml") : null;
                if(itemsXml == null || !itemsXml.exists())
                {
                    itemsXml = (extInfo.getCoreModule() != null) ? new File(extInfo.getExtensionDirectory(), "resources/items.xml") : null;
                }
                if(itemsXml != null && itemsXml.exists())
                {
                    parser.parseExtensionDeploymentsFromSystem(ext = loader.addExtension(extInfo.getName(), null), new FileInputStream(itemsXml));
                }
                File advDepl = (extInfo.getCoreModule() != null) ? new File(extInfo.getExtensionDirectory(), "resources/" + extInfo.getName() + "-advanced-deployment.xml") : null;
                if(advDepl == null || !advDepl.exists())
                {
                    advDepl = (extInfo.getCoreModule() != null) ? new File(extInfo.getExtensionDirectory(), "resources/jar/" + extInfo.getName() + ".advanced-deployment.xml") : null;
                }
                if(advDepl != null && advDepl.exists())
                {
                    parser.parseExtensionDeployments((ext != null) ? ext : loader.addExtension(extInfo.getName(), null), new FileInputStream(advDepl));
                }
            }
            loader.finish();
            loader.validate();
            System.out.println("parsed " + loader.getSystem().getDeployments().size() + " deployments.");
            return loader.getSystem();
        }
        catch(Exception e)
        {
            e.printStackTrace(System.err);
            throw new IllegalArgumentException("unexpected parse error : " + e.getMessage());
        }
    }
}
