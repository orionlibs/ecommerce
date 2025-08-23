package de.hybris.bootstrap.beangenerator;

import de.hybris.bootstrap.beangenerator.definitions.model.BeanPrototype;
import de.hybris.bootstrap.beangenerator.definitions.model.ClassNameAware;
import de.hybris.bootstrap.beangenerator.definitions.model.Extension;
import de.hybris.bootstrap.beangenerator.definitions.model.PojoFactory;
import de.hybris.bootstrap.beangenerator.validators.EnumValueNameValidator;
import de.hybris.bootstrap.beangenerator.validators.JavaKeyWordValidator;
import de.hybris.bootstrap.beangenerator.validators.PojoCreationValidator;
import de.hybris.bootstrap.beangenerator.validators.PropertyNameValidator;
import de.hybris.bootstrap.beangenerator.validators.PropertyReferenceValidateListener;
import de.hybris.bootstrap.beangenerator.validators.TypeConsistencyValidator;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class BeanGenerator
{
    private static final String GLOBAL_ENUMTEMPLATE_VM = "global-enumtemplate.vm";
    private static final String GLOBAL_BEANTEMPLATE_VM = "global-beantemplate.vm";
    private static final String GLOBAL_WILDCARD_TEMPLATE_VM = "global-$template.vm";
    protected static final Logger LOG = Logger.getLogger(BeanGenerator.class);
    private static final String BOOTSTRAP_RESOURCE_DIR = "/bootstrap/resources/pojo/";
    private static final String SCHEMAS_RESOURCE_DIR = "/resources/schemas/";
    private final BeansDefinitionParser beansDefinitionParser;
    private final BeanSourceFileProcessor beanSourceFileGenerator;
    private final Map<String, String> extensionNamePathMapping;
    private final PojoFactory pojoFactory = new PojoFactory();
    private final String platformHome;
    private BeansPostProcessor beansPostProcessor = BeansPostProcessor.DEFAULT;


    public BeanGenerator(String platformHome, String targetDir, Map<String, String> extensionNamePathMapping)
    {
        this.platformHome = platformHome;
        this.extensionNamePathMapping = extensionNamePathMapping;
        this.beansDefinitionParser = new BeansDefinitionParser(calculateSchemaPath());
        this.beanSourceFileGenerator = new BeanSourceFileProcessor(targetDir);
        this.pojoFactory.setValidators(getListeners());
    }


    public void setBeansPostProcessor(BeansPostProcessor beansPostProcessor)
    {
        this.beansPostProcessor = (beansPostProcessor == null) ? BeansPostProcessor.DEFAULT : beansPostProcessor;
    }


    protected String calculateSchemaPath()
    {
        return (new File(this.platformHome, "/resources/schemas/beans.xsd")).getPath();
    }


    protected Collection<Extension> getConfiguredExtensions(Map<String, String> extensionNamePathMapping)
    {
        List<Extension> configuredExtensions = new ArrayList<>();
        for(Map.Entry<String, String> entry : extensionNamePathMapping.entrySet())
        {
            String extName = entry.getKey();
            String path = entry.getValue();
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Extension name: " + extName);
            }
            File pathFile = new File(path);
            if(!pathFile.exists())
            {
                throw new BeanGenerationException("Given extension dir " + path + " is not existing");
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Extension path: " + (String)extensionNamePathMapping.get(extName));
            }
            configuredExtensions.add(new Extension(extName, pathFile));
        }
        return configuredExtensions;
    }


    protected List<? extends PojoCreationValidator> getListeners()
    {
        return (List)Arrays.asList((Object[])new PojoCreationValidator.PojoCreationValidationAdapter[] {(PojoCreationValidator.PojoCreationValidationAdapter)new PropertyNameValidator(), (PojoCreationValidator.PojoCreationValidationAdapter)new EnumValueNameValidator(),
                        (PojoCreationValidator.PojoCreationValidationAdapter)new JavaKeyWordValidator(), (PojoCreationValidator.PojoCreationValidationAdapter)new TypeConsistencyValidator(), (PojoCreationValidator.PojoCreationValidationAdapter)new PropertyReferenceValidateListener(),
                        (PojoCreationValidator.PojoCreationValidationAdapter)new PojoFactory.BeanTemplateValidator()});
    }


    public boolean generate()
    {
        for(Extension extension : getConfiguredExtensions(this.extensionNamePathMapping))
        {
            File file = new File("" + extension.getHome() + "/resources/" + extension.getHome() + "-beans.xml");
            if(file.exists())
            {
                try
                {
                    this.pojoFactory.mergePojos(extension, this.beansDefinitionParser.parseBeansDefinition(file.getAbsolutePath()));
                }
                catch(BeanGenerationException bge)
                {
                    LOG.error(bge.getMessage() + ". Problem found during processing the file " + bge.getMessage() + " for extension <" + file + ">");
                    throw bge;
                }
            }
            this.pojoFactory.notifyExtensionProcessed();
        }
        Collection<? extends ClassNameAware> allBeans = this.beansPostProcessor.postProcess(this.pojoFactory.getBeans());
        boolean didGenerate = false;
        for(ClassNameAware beanPrototype : allBeans)
        {
            this.beanSourceFileGenerator.generateBeanSourceFile(beanPrototype, getVelocityTemplateForBean(beanPrototype));
            didGenerate = true;
        }
        return didGenerate;
    }


    protected File getVelocityTemplateForBean(ClassNameAware bean)
    {
        if(StringUtils.isBlank(bean.getTemplatePath()))
        {
            return getDefaultTemplate(bean);
        }
        return getCustomTemplate(bean);
    }


    private File getCustomTemplate(ClassNameAware bean)
    {
        String templatePath = bean.getTemplatePath();
        File customTemplateFile = new File(templatePath);
        if(customTemplateFile.canRead())
        {
            return customTemplateFile;
        }
        LOG.warn("Given template '" + templatePath + "' defined for bean (" + bean.getClassName() + ") at <" + bean
                        .getExtensionName() + "> is not accessible - falling back to default one '" +
                        getDefaultTemplateFileName(bean) + "'");
        return getDefaultTemplate(bean);
    }


    private String getDefaultTemplateFileName(ClassNameAware bean)
    {
        if(bean instanceof BeanPrototype)
        {
            String type = ((BeanPrototype)bean).getType();
            return StringUtils.isNotBlank(type) ? "global-$template.vm".replace("$", type) : "global-beantemplate.vm";
        }
        if(bean instanceof de.hybris.bootstrap.beangenerator.definitions.model.EnumPrototype)
        {
            return "global-enumtemplate.vm";
        }
        throw new IllegalArgumentException("Not supported type " + bean);
    }


    private File getDefaultTemplate(ClassNameAware bean)
    {
        return new File(this.platformHome, "/bootstrap/resources/pojo/" + getDefaultTemplateFileName(bean));
    }
}
