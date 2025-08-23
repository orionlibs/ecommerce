package de.hybris.bootstrap.codegenerator;

import de.hybris.bootstrap.codegenerator.jalo.JaloClassGenerator;
import de.hybris.bootstrap.codegenerator.model.ModelClassGenerator;
import de.hybris.bootstrap.codegenerator.platformwebservices.WebservicesCodeGenerator;
import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.bootstrap.config.PlatformConfig;
import de.hybris.bootstrap.typesystem.OverridenItemsXml;
import de.hybris.bootstrap.typesystem.YAtomicType;
import de.hybris.bootstrap.typesystem.YCollectionType;
import de.hybris.bootstrap.typesystem.YComposedType;
import de.hybris.bootstrap.typesystem.YExtension;
import de.hybris.bootstrap.typesystem.YMapType;
import de.hybris.bootstrap.typesystem.YType;
import de.hybris.bootstrap.typesystem.YTypeSystemHandler;
import de.hybris.bootstrap.typesystem.YTypeSystemLoader;
import de.hybris.bootstrap.typesystem.YTypeSystemSource;
import de.hybris.bootstrap.util.DeploymentMigrationUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class CodeGenerator extends YTypeSystemSource
{
    private static final Logger LOG = Logger.getLogger(CodeGenerator.class.getName());
    private final Map<String, List<String>> customSettersSignatureInfoMap;


    public CodeGenerator(String platformHome)
    {
        this(PlatformConfig.getInstance(ConfigUtil.getSystemConfig(platformHome)), (YTypeSystemHandler)new YTypeSystemLoader(true));
    }


    public CodeGenerator(PlatformConfig cfg)
    {
        this(cfg, (YTypeSystemHandler)new YTypeSystemLoader(true));
    }


    public CodeGenerator(PlatformConfig cfg, YTypeSystemHandler loader)
    {
        super(cfg, loader, OverridenItemsXml.empty());
        this.customSettersSignatureInfoMap = DeploymentMigrationUtil.prepareMigratedCoreTypesInfoMap(getPlatformProperties()
                        .getProperty("migrated_core_type.info"));
    }


    public String getExtensionPackage(YExtension extension)
    {
        return getInfo(extension.getExtensionName()).getCoreModule().getPackageRoot();
    }


    public ExtensionInfo getInfo(YExtension ext)
    {
        return getInfo(ext.getExtensionName());
    }


    public PlatformConfig getPlatformConfig()
    {
        return super.getPlatformConfig();
    }


    public Properties getPlatformProperties()
    {
        return super.getPlatformProperties();
    }


    public String getJavaClassName(YType type)
    {
        if(type instanceof YComposedType)
        {
            return getJaloClassName((YComposedType)type);
        }
        if(type instanceof YAtomicType)
        {
            return ((YAtomicType)type).getJavaClassName();
        }
        if(type instanceof YCollectionType)
        {
            YCollectionType collType = (YCollectionType)type;
            StringBuilder builder = new StringBuilder();
            switch(null.$SwitchMap$de$hybris$bootstrap$typesystem$YCollectionType$TypeOfCollection[collType.getTypeOfCollection().ordinal()])
            {
                case 1:
                    builder.append(Set.class.getName());
                    break;
                case 2:
                    builder.append(List.class.getName());
                    break;
                case 3:
                    builder.append(Collection.class.getName());
                    break;
            }
            builder.append("<").append(getJavaClassName(collType.getElementType())).append(">");
            return builder.toString();
        }
        if(type instanceof YMapType)
        {
            return "java.util.Map<" + getJavaClassName(((YMapType)type).getArgumentType()) + "," +
                            getJavaClassName(((YMapType)type).getReturnType()) + ">";
        }
        throw new IllegalArgumentException("unknown type " + type);
    }


    public String getJaloClassName(YComposedType type)
    {
        return getJaloClassName(type, getExtensionPackage((YExtension)type.getNamespace()));
    }


    public static String getJaloClassName(YComposedType type, String packageRoot)
    {
        if(type.isGenerate())
        {
            String own = type.getOwnJaloClassName();
            return (own == null) ? (packageRoot + ".jalo." + packageRoot) : own;
        }
        return type.getJaloClassName();
    }


    public ExtensionInfo getInfo(String extensionName)
    {
        for(ExtensionInfo info : getExtensionCfgs())
        {
            if(info.getName().equalsIgnoreCase(extensionName))
            {
                return info;
            }
        }
        throw new IllegalArgumentException("no info for extension name '" + extensionName + "'");
    }


    protected void generate()
    {
        boolean markUnmodifedAfter = generateJaloClasses();
        markUnmodifedAfter |= generateModelsAndBeansClasses();
        markUnmodifedAfter |= generateWebserviceClasses();
        if(markUnmodifedAfter)
        {
            markExtensionsUnmodified();
        }
        else
        {
            LOG.info("   No changes found, skipping code generation");
        }
    }


    private void markExtensionsUnmodified()
    {
        for(ExtensionInfo info : getExtensionCfgs())
        {
            if(info.getCoreModule() != null && info.getCoreModule().isGenerate())
            {
                info.markUnmodifiedForCodeGeneration();
            }
        }
    }


    private boolean generateJaloClasses()
    {
        JaloClassGenerator generator = new JaloClassGenerator(this);
        return generator.generateClasses(getExtensionCfgs());
    }


    private boolean generateModelsAndBeansClasses()
    {
        ModelClassGenerator generator = new ModelClassGenerator(this);
        return generator.generateClasses(getExtensionCfgs());
    }


    @Deprecated(since = "ages", forRemoval = true)
    private boolean generateWebserviceClasses()
    {
        WebservicesCodeGenerator generator = new WebservicesCodeGenerator(this, getExtensionCfgs());
        return generator.generate();
    }


    public static String packageToDir(String packageName)
    {
        StringBuilder text = new StringBuilder();
        for(StringTokenizer st = new StringTokenizer(packageName, "."); st.hasMoreTokens(); )
        {
            text.append(st.nextToken());
            if(st.hasMoreTokens())
            {
                text.append("/");
            }
        }
        return text.toString();
    }


    public static boolean writeToFile(ClassWriter classWriter, File baseDir, boolean overwrite, boolean touch)
    {
        boolean touchNext;
        File targetDir = new File(baseDir, packageToDir(classWriter.getPackageName()));
        if(!targetDir.exists())
        {
            targetDir.mkdirs();
        }
        File targetFile = new File(targetDir, classWriter.getClassName() + ".java");
        if(targetFile.exists())
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("" + targetFile + " exists!");
            }
            touchNext = true;
        }
        else
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("" + targetFile + " DOES NOT exists!");
            }
            touchNext = false;
        }
        if(!overwrite && targetFile.exists())
        {
            if(touch)
            {
                try
                {
                    FileUtils.touch(targetFile);
                }
                catch(IOException e)
                {
                    LOG.error(e.getMessage(), e);
                }
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("skipped generating " + targetFile.getName() + " since file already exists. file is being touched instead.");
                }
            }
        }
        else
        {
            JavaFile src = new JavaFile();
            classWriter.write(src);
            writeToFile(src, targetFile);
        }
        return touchNext;
    }


    private static void writeToFile(JavaFile javaFile, File targetFile)
    {
        try
        {
            PrintWriter writer = new PrintWriter(targetFile, "utf-8");
            for(String line : javaFile.getLines())
            {
                writer.println(line);
            }
            writer.close();
        }
        catch(FileNotFoundException | java.io.UnsupportedEncodingException e)
        {
            throw new IllegalArgumentException("unexpected error : " + e.getMessage(), e);
        }
    }


    public Map<String, List<String>> getCustomSettersSignatureInfoMap()
    {
        return this.customSettersSignatureInfoMap;
    }


    public static void generate(PlatformConfig cfg)
    {
        if(LOG.isInfoEnabled())
        {
            LOG.info("Starting code generation ...");
        }
        long startTime = System.currentTimeMillis();
        CodeGenerator gen = new CodeGenerator(cfg);
        gen.generate();
        long endTime = System.currentTimeMillis();
        if(LOG.isInfoEnabled())
        {
            LOG.info("Code generation done in " + endTime - startTime + " ms.");
        }
    }


    public static void main(String[] args)
    {
        Logger root = Logger.getRootLogger();
        System.out.println("Basic configuration for Log4J active.");
        if(args.length < 1)
        {
            LOG.error("usage java ...CodeGenerator <pathToPlatformHome>");
        }
        else
        {
            generate(PlatformConfig.getInstance(ConfigUtil.getSystemConfig(args[0])));
        }
    }
}
