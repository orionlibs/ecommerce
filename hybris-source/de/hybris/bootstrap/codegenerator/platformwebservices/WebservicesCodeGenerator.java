package de.hybris.bootstrap.codegenerator.platformwebservices;

import de.hybris.bootstrap.codegenerator.CodeGenerator;
import de.hybris.bootstrap.codegenerator.platformwebservices.dto.DtoClassGenerator;
import de.hybris.bootstrap.codegenerator.platformwebservices.resource.ResourceClassGenerator;
import de.hybris.bootstrap.codegenerator.platformwebservices.resource.SimpleUniqueIdentifierResolver;
import de.hybris.bootstrap.codegenerator.platformwebservices.resource.SubResourceResolver;
import de.hybris.bootstrap.codegenerator.platformwebservices.resource.UniqueIdentifierResolver;
import de.hybris.bootstrap.codegenerator.platformwebservices.resource.UniqueSubResourceResolver;
import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.bootstrap.typesystem.YComposedType;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

@Deprecated(since = "1818", forRemoval = true)
public class WebservicesCodeGenerator
{
    private static final Logger LOG = Logger.getLogger(WebservicesCodeGenerator.class);
    private static final String DEFAULT_EXT_NAME = "platformwebservices";
    private static final String WS_EXT_NAME_KEY = "webservice.module";
    private final CodeGenerator gen;
    private final List<ExtensionInfo> extensionCfgs;
    private ExtensionInfo wsExtInfo = null;
    private final WebservicesConfig wsConfig;


    public WebservicesCodeGenerator(CodeGenerator codeGen, List<ExtensionInfo> extensionInfo)
    {
        this.gen = codeGen;
        this.extensionCfgs = extensionInfo;
        UniqueSubResourceResolver uniqueSubResourceResolver = new UniqueSubResourceResolver();
        SimpleUniqueIdentifierResolver simpleUniqueIdentifierResolver = new SimpleUniqueIdentifierResolver();
        boolean platformWebservicesExtensionIncluded = false;
        for(ExtensionInfo searchInfo : this.extensionCfgs)
        {
            if(searchInfo.getName()
                            .equalsIgnoreCase(codeGen.getPlatformProperties().getProperty("webservice.module", "platformwebservices")))
            {
                this.wsExtInfo = searchInfo;
                break;
            }
            if(searchInfo.getName().equals("platformwebservices"))
            {
                platformWebservicesExtensionIncluded = true;
            }
        }
        if(platformWebservicesExtensionIncluded)
        {
            if(this.wsExtInfo == null)
            {
                LOG.warn("*********************************************************\nDesignated extension <<" + codeGen
                                .getPlatformProperties().getProperty("webservice.module", "platformwebservices")
                                + ">> as 'webservice extension' was not found in set of extensions\n or it is not correctly spelled in [local|project].properties property {webservice.module} \n.*********************************************************");
                throw new IllegalArgumentException("*********************************************************\nDesignated extension <<" + codeGen
                                .getPlatformProperties().getProperty("webservice.module", "platformwebservices")
                                + ">> as 'webservice extension' was not found in set of extensions\n or it is not correctly spelled in [local|project].properties property {webservice.module} \n.*********************************************************");
            }
            if(!"platformwebservices".equals(codeGen.getPlatformProperties().getProperty("webservice.module", "platformwebservices")))
            {
                LOG.info("   Designated webservice extension:" + this.wsExtInfo.getName());
            }
        }
        this.wsConfig = new WebservicesConfig(this.gen, (SubResourceResolver)uniqueSubResourceResolver, (UniqueIdentifierResolver)simpleUniqueIdentifierResolver);
    }


    public boolean generate()
    {
        Map<YComposedType, Set<YComposedType>> uniqueInfoMap = new HashMap<>();
        DtoClassGenerator dtoGenerator = new DtoClassGenerator(this.gen, this.wsConfig, uniqueInfoMap);
        dtoGenerator.generateClasses(this.extensionCfgs);
        boolean regenerateResources = dtoGenerator.isRegenerateResources();
        ((UniqueSubResourceResolver)this.wsConfig.getSubresourceResolver()).setUniqueInfoMap(uniqueInfoMap);
        ResourceClassGenerator resourceGenerator = new ResourceClassGenerator(this.gen, this.wsConfig);
        resourceGenerator.setRegenerateResources(regenerateResources);
        resourceGenerator.generateClasses(this.extensionCfgs);
        if(regenerateResources)
        {
            try
            {
                File file = new File("" + this.wsExtInfo.getExtensionDirectory() + "/resources/generated-" + this.wsExtInfo.getExtensionDirectory() + "-web-spring.xml");
                generateSpringConfig(new FileWriter(file));
            }
            catch(Exception e)
            {
                LOG.error("Error while regenerating webservices spring config", e);
            }
        }
        return regenerateResources;
    }


    public void generateSpringConfig(Writer writer)
    {
        PrintWriter printer = new PrintWriter(writer);
        printer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        printer.println("<beans xmlns=\"http://www.springframework.org/schema/beans\"");
        printer.print("xmlns:security=\"http://www.springframework.org/schema/security\" ");
        printer.print("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ");
        printer.println("xsi:schemaLocation=\" ");
        printer.println("http://www.springframework.org/schema/beans");
        printer.println("http://www.springframework.org/schema/beans/spring-beans-3.1.xsd");
        printer.println("http://www.springframework.org/schema/security");
        printer.println("http://www.springframework.org/schema/security/spring-security-3.1.xsd");
        printer.println("\">");
        printer.println("<!--");
        printer.println("\tDto class container.");
        printer.println("\tCollects all dtos (single + collection dtos)");
        printer.println("\tUsage: generic ObjectGraph + JAXBContext assembling");
        printer.println("-->");
        printer
                        .println("\t<bean id=\"dtoClassContainer\" class=\"de.hybris.platform.webservices.provider.DtoClassContainer\" scope=\"singleton\">");
        printer.println("\t\t<property name=\"singleDtoNodes\">");
        printer.println("\t\t\t<list>");
        for(ResourceConfig cfg : this.wsConfig.getAllSingleResources())
        {
            String dto = cfg.getDTOConfig().getDtoClassName();
            printer.println("\t\t\t<value type=\"java.lang.Class\">" + dto + "</value>");
        }
        printer.println("\t\t\t</list>");
        printer.println("\t\t</property>");
        printer.println("\t\t<property name=\"collectionDtoNodes\">");
        printer.println("\t\t\t<list>");
        for(ResourceConfig cfg : this.wsConfig.getAllRootResources())
        {
            String dto = cfg.getDTOConfig().getDtoClassName();
            printer.println("\t\t\t<value type=\"java.lang.Class\">" + dto + "</value>");
        }
        printer.println("\t\t\t</list>");
        printer.println("\t\t</property>");
        printer.println("\t</bean>");
        printer.println("<!--");
        printer.println("\tGeneric ObjectGraph.");
        printer.println("\tCollects information about DTO<->Model mapping.");
        printer.println("\tGets injected into Resources.");
        printer.println("-->");
        printer
                        .println("\t<bean id=\"genericGraph\" class=\"de.hybris.platform.webservices.objectgraphtransformer.YObjectGraphTransformer\" scope=\"singleton\" init-method=\"initialize\">");
        printer.println("\t\t<property name=\"graphNodes\" ref=\"dtoClassContainer\"/>");
        printer.println("\t</bean>");
        printer.println();
        printer.println("\t<!--");
        printer.println("\tTyped resources");
        printer.println("\t-->");
        for(ResourceConfig cfg : this.wsConfig.getAllRootResources())
        {
            String resName = cfg.getClassName();
            String resSimplename = cfg.getSimpleClassName();
            resSimplename = resSimplename.substring(0, 1).toLowerCase() + resSimplename.substring(0, 1).toLowerCase();
            printer.println("\t<bean id=\"" + resSimplename + "\" class=\"" + resName + "\" scope=\"prototype\" parent=\"abstractResource\"/>");
        }
        printer.println();
        for(ResourceConfig cfg : this.wsConfig.getAllSingleResources())
        {
            String resName = cfg.getClassName();
            String resSimplename = cfg.getSimpleClassName();
            resSimplename = resSimplename.substring(0, 1).toLowerCase() + resSimplename.substring(0, 1).toLowerCase();
            printer.println("\t<bean id=\"" + resSimplename + "\" class=\"" + resName + "\" scope=\"prototype\" parent=\"abstractResource\"/>");
        }
        printer.println("</beans>");
        printer.close();
    }
}
