package de.hybris.bootstrap.codegenerator.jalo;

import de.hybris.bootstrap.codegenerator.ClassWriter;
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

public class JaloClassGenerator
{
    private static final Logger LOG = Logger.getLogger(JaloClassGenerator.class);
    private final CodeGenerator gen;


    public JaloClassGenerator(CodeGenerator gen)
    {
        this.gen = gen;
    }


    public boolean generateClasses(Collection<ExtensionInfo> extensionCfgs)
    {
        boolean infoPrinted = false;
        for(ExtensionInfo info : extensionCfgs)
        {
            if(info.getCoreModule() == null || !info.getCoreModule().isGenerate() || !info.getCoreModule().isSourceAvailable())
            {
                continue;
            }
            YExtension ext = this.gen.getTypeSystem().getExtension(info.getName());
            File srcDir = new File(info.getExtensionDirectory(), "src");
            File gensrcDir = new File(info.getExtensionDirectory(), "gensrc");
            if(!srcDir.exists())
            {
                srcDir.mkdirs();
            }
            if(!gensrcDir.exists())
            {
                gensrcDir.mkdirs();
            }
            if(info.isModifiedForCodeGeneration())
            {
                if(!infoPrinted)
                {
                    LOG.info("   Generating item classes ..");
                    infoPrinted = true;
                }
                ConstantsWriter constants = new ConstantsWriter(ext, this.gen);
                boolean touchNext = CodeGenerator.writeToFile((ClassWriter)constants, gensrcDir, true, false);
                CodeGenerator.writeToFile(constants.createNonAbstractClassWriter(), srcDir, false, touchNext);
                if(!"de.hybris.platform.jalo.extension.GenericManager".equals(info.getCoreModule().getManager()))
                {
                    ManagerWriter manager = new ManagerWriter(ext, this.gen, info.isJaloLogicFree());
                    touchNext = CodeGenerator.writeToFile((ClassWriter)manager, gensrcDir, true, false);
                    if(!info.isJaloLogicFree())
                    {
                        CodeGenerator.writeToFile(manager.createNonAbstractClassWriter(), srcDir, false, touchNext);
                    }
                }
                Set<YComposedType> types = ext.getOwnTypes(YComposedType.class, new Class[] {YEnumType.class, YRelation.class});
                for(YComposedType t : types)
                {
                    if(t.isGenerate())
                    {
                        ItemTypeWriter typeWriter = new ItemTypeWriter(this.gen, ext, t, false, info.isJaloLogicFree());
                        touchNext = CodeGenerator.writeToFile((ClassWriter)typeWriter, gensrcDir, true, false);
                        if(!info.isJaloLogicFree())
                        {
                            CodeGenerator.writeToFile(typeWriter.createNonAbstractClassWriter(), srcDir, false, touchNext);
                        }
                    }
                }
            }
        }
        return infoPrinted;
    }
}
