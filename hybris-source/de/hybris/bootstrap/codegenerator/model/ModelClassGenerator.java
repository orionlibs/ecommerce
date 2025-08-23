package de.hybris.bootstrap.codegenerator.model;

import de.hybris.bootstrap.beangenerator.BeanGenerationException;
import de.hybris.bootstrap.beangenerator.BeanGenerator;
import de.hybris.bootstrap.beangenerator.BeansPostProcessor;
import de.hybris.bootstrap.codegenerator.ClassWriter;
import de.hybris.bootstrap.codegenerator.CodeGenerator;
import de.hybris.bootstrap.codegenerator.PackageInfoWriter;
import de.hybris.bootstrap.codegenerator.model.enums.ClassEnumWriter;
import de.hybris.bootstrap.codegenerator.model.enums.EnumWriterValue;
import de.hybris.bootstrap.codegenerator.model.enums.JavaEnumWriter;
import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.bootstrap.typesystem.YComposedType;
import de.hybris.bootstrap.typesystem.YEnumType;
import de.hybris.bootstrap.typesystem.YEnumValue;
import de.hybris.bootstrap.typesystem.YExtension;
import de.hybris.bootstrap.typesystem.YRelation;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class ModelClassGenerator
{
    private static final Logger LOG = Logger.getLogger(ModelClassGenerator.class);
    private final CodeGenerator gen;


    public ModelClassGenerator(CodeGenerator gen)
    {
        this.gen = gen;
    }


    public boolean generateClasses(Collection<ExtensionInfo> extensionCfgs)
    {
        File modelsJarFile = getModelsJar();
        if(!modelsJarFile.exists() || isGensrcEmpty() || hasExtensionsWhichRequireCodeGeneration(extensionCfgs, modelsJarFile))
        {
            infoAndDeleteModelsJar(modelsJarFile);
            File genSrcDir = getOrCreateGensrcDir();
            generateModels(extensionCfgs, genSrcDir);
            generateBeanClasses(extensionCfgs, genSrcDir);
            return true;
        }
        return false;
    }


    private void generateBeanClasses(Collection<ExtensionInfo> extensionCfgs, File genSrcDir)
    {
        Map<String, String> extensionsMap = new LinkedHashMap<>();
        for(ExtensionInfo ext : extensionCfgs)
        {
            if(ext.isExternalExtension())
            {
                continue;
            }
            extensionsMap.put(ext.getName(), ext.getExtensionDirectory().getPath());
        }
        BeanGenerator beanGenerator = new BeanGenerator(this.gen.getPlatformConfig().getPlatformHome().getPath(), genSrcDir.getPath(), extensionsMap);
        beanGenerator.setBeansPostProcessor(getConfiguredBeansPostProcessor());
        beanGenerator.generate();
    }


    private BeansPostProcessor getConfiguredBeansPostProcessor()
    {
        Class<?> postProcessorClass;
        String className = this.gen.getPlatformProperties().getProperty("generator.beans.post.processor.class");
        if(StringUtils.isBlank(className))
        {
            return null;
        }
        try
        {
            postProcessorClass = Class.forName(className);
        }
        catch(ClassNotFoundException e)
        {
            throw new BeanGenerationException("Cannot load class '" + className + "'. Please make sure it's on the bootstrap classpath.");
        }
        if(!BeansPostProcessor.class.isAssignableFrom(postProcessorClass))
        {
            throw new BeanGenerationException("Class '" + postProcessorClass.getCanonicalName() + "' cannot be used as a BeansPostProcessor. Please make sure it implements '" + BeansPostProcessor.class
                            .getCanonicalName() + "' interface.");
        }
        try
        {
            return (BeansPostProcessor)postProcessorClass.newInstance();
        }
        catch(InstantiationException | IllegalAccessException e)
        {
            throw new BeanGenerationException("Cannot instantiate '" + postProcessorClass + "'. Please make sure it is not abstract and has public default constructor.");
        }
    }


    private void generateModels(Collection<ExtensionInfo> extensionCfgs, File gensrcDir)
    {
        Map<String, String> packages = new HashMap<>();
        for(ExtensionInfo info : extensionCfgs)
        {
            if(info.getCoreModule() != null && info.getCoreModule().isGenerate())
            {
                generateClasses(info, gensrcDir, packages);
            }
        }
        generatedPackageInfo(packages, gensrcDir);
    }


    private void infoAndDeleteModelsJar(File modelsJarFile)
    {
        if(modelsJarFile.exists())
        {
            LOG.info("   Generating models and beans (deleting " + modelsJarFile + " before)..");
            modelsJarFile.delete();
        }
        else
        {
            LOG.info("   Generating models and beans (no file " + modelsJarFile + " exists)..");
        }
    }


    private void generatedPackageInfo(Map<String, String> packages, File slayerExtGensrcDir)
    {
        for(Map.Entry<String, String> packageEntry : packages.entrySet())
        {
            PackageInfoWriter infoWriter = new PackageInfoWriter(this.gen, packageEntry.getKey());
            infoWriter.setJavadoc("Contains generated models for each type of " + (String)packageEntry.getValue() + " package.");
            try
            {
                CodeGenerator.writeToFile((ClassWriter)infoWriter, slayerExtGensrcDir, true, true);
            }
            catch(Exception e)
            {
                LOG.error("Error while generating package-info for package " + (String)packageEntry.getKey(), e);
            }
        }
    }


    private void generateClasses(ExtensionInfo info, File slayerExtGensrcDir, Map<String, String> packages)
    {
        YExtension ext = this.gen.getTypeSystem().getExtension(info.getName());
        Set<YComposedType> types = ext.getOwnTypes(YComposedType.class, new Class[] {YEnumType.class, YRelation.class});
        for(YComposedType t : types)
        {
            if(needsCodeGenerationForType(t))
            {
                generateClassesForType(slayerExtGensrcDir, packages, ext, t);
            }
        }
        Set<YEnumType> enums = ext.getOwnTypes(YEnumType.class);
        for(YEnumType curEnum : enums)
        {
            generateClassesForEnum(slayerExtGensrcDir, packages, ext, curEnum);
        }
    }


    private boolean needsCodeGenerationForType(YComposedType t)
    {
        return (t.isGenerateModel() &&
                        !"ExtensibleItem".equalsIgnoreCase(t.getCode()) && !"LocalizableItem".equalsIgnoreCase(t.getCode()) &&
                        !"GenericItem".equalsIgnoreCase(t.getCode()) && (t.getSuperType() == null || !"Link".equalsIgnoreCase(t.getSuperType().getCode())));
    }


    private void generateClassesForEnum(File slayerExtGensrcDir, Map<String, String> packages, YExtension ext, YEnumType curEnum)
    {
        JavaEnumWriter javaEnumWriter;
        if(curEnum.isDynamic() || !curEnum.isGenerate())
        {
            ClassEnumWriter classEnumWriter = new ClassEnumWriter(this.gen, ext, curEnum);
        }
        else
        {
            javaEnumWriter = new JavaEnumWriter(this.gen, ext, curEnum);
        }
        javaEnumWriter.setPackageName(ModelNameUtils.getModelPackage((YComposedType)curEnum, this.gen
                        .getExtensionPackage((YExtension)curEnum.getNamespace())));
        StringBuilder description = new StringBuilder("Generated enum " + curEnum.getCode() + " declared at extension " + ((YExtension)curEnum.getNamespace()).getExtensionName() + ".");
        if(curEnum.getTypeDescription() != null)
        {
            description.append("\n");
            description.append("<p/>");
            description.append("\n");
            description.append(curEnum.getTypeDescription().endsWith(".") ? curEnum.getTypeDescription() : (curEnum
                            .getTypeDescription() + "."));
        }
        javaEnumWriter.setJavadoc(description.toString());
        if(curEnum.isGenerate())
        {
            for(YEnumValue value : curEnum.getValues())
            {
                EnumWriterValue valueContainer = new EnumWriterValue(value.getCode());
                StringBuilder valueDescription = new StringBuilder("Generated enum value for " + curEnum.getCode() + "." + value.getCode() + " declared at extension " + ((YExtension)value.getNamespace()).getExtensionName() + ".");
                if(value.getDescription() != null)
                {
                    valueDescription.append("\n");
                    valueDescription.append("<p/>");
                    valueDescription.append("\n");
                    valueDescription.append(value.getDescription().endsWith(".") ? value.getDescription() : (value.getDescription() + "."));
                }
                valueContainer.setJavadoc(valueDescription.toString());
                javaEnumWriter.addEnumValue(valueContainer);
            }
        }
        try
        {
            CodeGenerator.writeToFile((ClassWriter)javaEnumWriter, slayerExtGensrcDir, true, true);
        }
        catch(Exception e)
        {
            LOG.error("Error while generating enum model for enum " + curEnum.getCode(), e);
        }
        if(!packages.containsKey(javaEnumWriter.getPackageName()))
        {
            String packageName = this.gen.getJaloClassName((YComposedType)curEnum);
            packageName = packageName.substring(0, packageName.lastIndexOf('.'));
            packages.put(javaEnumWriter.getPackageName(), packageName);
        }
    }


    private void generateClassesForType(File slayerExtGensrcDir, Map<String, String> packages, YExtension ext, YComposedType t)
    {
        ModelWriter typeWriter = new ModelWriter(this.gen, ext, t);
        try
        {
            CodeGenerator.writeToFile((ClassWriter)typeWriter, slayerExtGensrcDir, true, true);
        }
        catch(Exception e)
        {
            LOG.error("Error while generating model for type " + t.getCode(), e);
        }
        String packageName = this.gen.getJaloClassName(t);
        packageName = packageName.substring(0, packageName.lastIndexOf('.'));
        packages.put(typeWriter.getPackageName(), packageName);
    }


    private File getOrCreateGensrcDir()
    {
        File slayerExtGensrcDir = getGensrcDir();
        if(!slayerExtGensrcDir.exists())
        {
            slayerExtGensrcDir.mkdirs();
        }
        return slayerExtGensrcDir;
    }


    private File getModelsJar()
    {
        File bootstrapBinDir = this.gen.getPlatformConfig().getSystemConfig().getBootstrapBinDir();
        if(bootstrapBinDir != null && bootstrapBinDir.exists())
        {
            return new File(bootstrapBinDir, "models.jar");
        }
        return new File(this.gen.getPlatformConfig().getPlatformHome(), "bootstrap/bin/models.jar");
    }


    private File getGensrcDir()
    {
        return new File(this.gen.getPlatformConfig().getPlatformHome(), "bootstrap/gensrc");
    }


    private boolean hasExtensionsWhichRequireCodeGeneration(Collection<ExtensionInfo> extensionCfgs, File modelsJarFile)
    {
        long modelsJarFileLastModified = modelsJarFile.lastModified();
        for(ExtensionInfo info : extensionCfgs)
        {
            if(info.getCoreModule() != null && info.getCoreModule().isGenerate() && (info
                            .isModifiedForCodeGeneration() || info.isItemsXMLModifiedAfter(modelsJarFileLastModified)))
            {
                return true;
            }
        }
        return false;
    }


    private boolean isGensrcEmpty()
    {
        File gensrcDir = getGensrcDir();
        return (!gensrcDir.exists() || isEmpty(gensrcDir));
    }


    private boolean isEmpty(File dir)
    {
        String[] list = dir.list();
        return (list == null || list.length == 0);
    }


    private ExtensionInfo getCoreExtension(Collection<ExtensionInfo> extensionCfgs)
    {
        for(ExtensionInfo searchInfo : extensionCfgs)
        {
            if(searchInfo.getName().equalsIgnoreCase("core"))
            {
                return searchInfo;
            }
        }
        return null;
    }
}
