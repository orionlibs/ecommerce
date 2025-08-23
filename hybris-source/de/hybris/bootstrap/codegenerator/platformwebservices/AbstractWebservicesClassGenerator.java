package de.hybris.bootstrap.codegenerator.platformwebservices;

import de.hybris.bootstrap.codegenerator.CodeGenerator;
import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.bootstrap.typesystem.YComposedType;
import de.hybris.bootstrap.typesystem.YEnumType;
import de.hybris.bootstrap.typesystem.YExtension;
import de.hybris.bootstrap.typesystem.YRelation;
import java.io.File;
import java.util.Collection;
import java.util.Set;
import org.apache.log4j.Logger;

@Deprecated(since = "1818")
public abstract class AbstractWebservicesClassGenerator
{
    private static final String EXT_NAME = "platformwebservices";
    private static final Logger LOG = Logger.getLogger(AbstractWebservicesClassGenerator.class);
    private final CodeGenerator gen;
    private boolean regenerateResources = false;


    public AbstractWebservicesClassGenerator(CodeGenerator gen)
    {
        this.gen = gen;
    }


    public void generateClasses(Collection<ExtensionInfo> extensionCfgs)
    {
        ExtensionInfo wslayerExtInfo = null;
        for(ExtensionInfo searchInfo : extensionCfgs)
        {
            if(searchInfo.getName().equalsIgnoreCase("platformwebservices"))
            {
                wslayerExtInfo = searchInfo;
                break;
            }
        }
        if(wslayerExtInfo == null)
        {
            return;
        }
        File wslayerExtGensrcDir = new File(wslayerExtInfo.getExtensionDirectory(), "web/gensrc");
        if(!wslayerExtGensrcDir.exists())
        {
            wslayerExtGensrcDir.mkdirs();
        }
        boolean needsGeneration = false;
        for(ExtensionInfo info : extensionCfgs)
        {
            if(info.getName().equalsIgnoreCase("platformwebservices"))
            {
                if((wslayerExtGensrcDir.list()).length == 0)
                {
                    needsGeneration = true;
                    this.regenerateResources = true;
                    break;
                }
                if(this.regenerateResources)
                {
                    needsGeneration = true;
                    break;
                }
            }
            if(info.getCoreModule() == null || !info.getCoreModule().isGenerate())
            {
                continue;
            }
            if(info.isModifiedForCodeGeneration())
            {
                this.regenerateResources = true;
                needsGeneration = true;
            }
        }
        if(needsGeneration)
        {
            logStarted();
        }
        else
        {
            return;
        }
        for(ExtensionInfo info : extensionCfgs)
        {
            if(info.getCoreModule() == null)
            {
                continue;
            }
            if(info.getCoreModule() != null)
            {
                YExtension ext = this.gen.getTypeSystem().getExtension(info.getName());
                Set<YComposedType> types = ext.getOwnTypes(YComposedType.class, new Class[] {YEnumType.class, YRelation.class});
                for(YComposedType t : types)
                {
                    if(t.isGenerateModel() &&
                                    !t.getCode().equalsIgnoreCase("ExtensibleItem") && !t.getCode().equalsIgnoreCase("LocalizableItem") &&
                                    !t.getCode().equalsIgnoreCase("GenericItem") && (t.getSuperType() == null ||
                                    !t.getSuperType().getCode().equalsIgnoreCase("Link")))
                    {
                        try
                        {
                            if(isGenerable(t))
                            {
                                doGenerate(wslayerExtGensrcDir, this.gen, ext, t);
                            }
                        }
                        catch(Exception e)
                        {
                            LOG.error("Error while generating classes for type " + t.getCode(), e);
                        }
                    }
                }
            }
        }
    }


    protected abstract void logStarted();


    protected abstract void doGenerate(File paramFile, CodeGenerator paramCodeGenerator, YExtension paramYExtension, YComposedType paramYComposedType);


    protected abstract boolean isGenerable(YComposedType paramYComposedType);


    protected boolean isRootResourceType(YComposedType type)
    {
        return true;
    }


    public void setRegenerateResources(boolean regenerateResources)
    {
        this.regenerateResources = regenerateResources;
    }


    public boolean isRegenerateResources()
    {
        return this.regenerateResources;
    }
}
