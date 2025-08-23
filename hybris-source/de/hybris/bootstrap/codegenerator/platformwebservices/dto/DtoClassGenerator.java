package de.hybris.bootstrap.codegenerator.platformwebservices.dto;

import de.hybris.bootstrap.codegenerator.ClassWriter;
import de.hybris.bootstrap.codegenerator.CodeGenerator;
import de.hybris.bootstrap.codegenerator.platformwebservices.AbstractWebservicesClassGenerator;
import de.hybris.bootstrap.codegenerator.platformwebservices.DtoConfig;
import de.hybris.bootstrap.codegenerator.platformwebservices.WebservicesConfig;
import de.hybris.bootstrap.typesystem.YComposedType;
import de.hybris.bootstrap.typesystem.YExtension;
import java.io.File;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

@Deprecated(since = "1818", forRemoval = true)
public class DtoClassGenerator extends AbstractWebservicesClassGenerator
{
    private static final Logger LOG = Logger.getLogger(DtoClassGenerator.class);
    private final WebservicesConfig wsConfig;
    private final Map<YComposedType, Set<YComposedType>> uniqueInfoMap;


    public DtoClassGenerator(CodeGenerator gen, WebservicesConfig wsConfig, Map<YComposedType, Set<YComposedType>> uniqueInfoMap)
    {
        super(gen);
        this.wsConfig = wsConfig;
        this.uniqueInfoMap = uniqueInfoMap;
    }


    protected void doGenerate(File wslayerExtGensrcDir, CodeGenerator gen, YExtension ext, YComposedType type)
    {
        if(isRootResourceType(type))
        {
            CollectionDtoConfig collectionDtoConfig = this.wsConfig.getCollectionDtoConfig(type);
            CollectionDtoWriter collectionDtoWriter = new CollectionDtoWriter(this.wsConfig, (DtoConfig)collectionDtoConfig, gen, ext);
            CodeGenerator.writeToFile((ClassWriter)collectionDtoWriter, wslayerExtGensrcDir, true, true);
        }
        SingleDtoConfig singleDtoConfig = this.wsConfig.getSingleDtoConfig(type);
        SingleDtoWriter singleDtoWriter = new SingleDtoWriter(this.wsConfig, (DtoConfig)singleDtoConfig, gen, ext, this.uniqueInfoMap);
        CodeGenerator.writeToFile((ClassWriter)singleDtoWriter, wslayerExtGensrcDir, true, true);
    }


    protected boolean isGenerable(YComposedType type)
    {
        return true;
    }


    protected void logStarted()
    {
        LOG.info("   Generating dtos ..");
    }
}
