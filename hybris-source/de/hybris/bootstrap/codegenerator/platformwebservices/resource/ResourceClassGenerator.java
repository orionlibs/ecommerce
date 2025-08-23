package de.hybris.bootstrap.codegenerator.platformwebservices.resource;

import de.hybris.bootstrap.codegenerator.ClassWriter;
import de.hybris.bootstrap.codegenerator.CodeGenerator;
import de.hybris.bootstrap.codegenerator.platformwebservices.AbstractWebservicesClassGenerator;
import de.hybris.bootstrap.codegenerator.platformwebservices.ResourceConfig;
import de.hybris.bootstrap.codegenerator.platformwebservices.WebservicesConfig;
import de.hybris.bootstrap.typesystem.YComposedType;
import de.hybris.bootstrap.typesystem.YExtension;
import java.io.File;
import org.apache.log4j.Logger;

@Deprecated(since = "1818", forRemoval = true)
public class ResourceClassGenerator extends AbstractWebservicesClassGenerator
{
    private static final Logger LOG = Logger.getLogger(ResourceClassGenerator.class);
    private final WebservicesConfig wsCfgProvider;


    public ResourceClassGenerator(CodeGenerator gen, WebservicesConfig wsCfgProvider)
    {
        super(gen);
        this.wsCfgProvider = wsCfgProvider;
    }


    protected void doGenerate(File wslayerExtGensrcDir, CodeGenerator gen, YExtension ext, YComposedType type)
    {
        if(isRootResourceType(type))
        {
            CollectionResourceConfig collectionResourceConfig = this.wsCfgProvider.getCollectionResourceConfig(type);
            CollectionResourceWriter collectionResourceWriter = new CollectionResourceWriter(gen, ext, (ResourceConfig)collectionResourceConfig);
            CodeGenerator.writeToFile((ClassWriter)collectionResourceWriter, wslayerExtGensrcDir, true, true);
        }
        SingleResourceConfig singleResourceConfig = this.wsCfgProvider.getSingleResourceConfig(type);
        SingleResourceWriter singleResourceWriter = new SingleResourceWriter(gen, ext, (ResourceConfig)singleResourceConfig);
        CodeGenerator.writeToFile((ClassWriter)singleResourceWriter, wslayerExtGensrcDir, true, true);
    }


    protected boolean isGenerable(YComposedType type)
    {
        return true;
    }


    protected void logStarted()
    {
        LOG.info("   Generating resources ..");
    }
}
