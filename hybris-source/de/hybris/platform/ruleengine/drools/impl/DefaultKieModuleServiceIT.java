package de.hybris.platform.ruleengine.drools.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.ruleengine.cronjob.DefaultKieModuleCleanupStrategy;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.internal.model.MaintenanceCleanupJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.runtime.KieContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

@IntegrationTest
public class DefaultKieModuleServiceIT extends ServicelayerTest
{
    private static final String KIE_MODULE_NAME = "kieModuleName";
    private static final String KIE_MODULE_NAME2 = "kieModuleName2";
    private static final String KIE_MODULE_NAME3 = "kieModuleName3";
    private static final String RELEASE_ID1 = "groupId:artifactId:version1";
    private static final String RELEASE_ID2 = "groupId:artifactId:version2";
    private static final String RELEASE_ID3 = "groupId:artifactId:version3";
    private static final String RELEASE_ID4 = "groupId:artifactId:version4";
    private static final String RELEASE_ID5 = "groupId:artifactId:version5";
    private static final String RELEASE_ID16 = "groupId:artifactId:version16";
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultKieModuleServiceIT.class);
    @Resource(name = "defaultKieModuleService")
    private DefaultKieModuleService defaultKieModuleService;
    @Resource(name = "modelService")
    private ModelService modelService;
    @Resource(name = "kieModuleCleanupStrategy")
    private DefaultKieModuleCleanupStrategy kieModuleCleanupStrategy;
    @Resource(name = "flexibleSearchService")
    private FlexibleSearchService flexibleSearchService;
    @Resource(name = "configurationService")
    private ConfigurationService configurationService;


    @Before
    public void setUp() throws ImpExException
    {
        importCsv("/ruleengine/test/ruleenginesetup.impex", "utf-8");
    }


    protected String getRuleContent(String ruleUuid, String ruleCode, String moduleName) throws IOException
    {
        String ruleTemplateContent = new String(Files.readAllBytes(Paths.get((new ClassPathResource("/ruleengine/test/impl/drools_rule_template.drl"))
                        .getURI())));
        String ruleContent = ruleTemplateContent.replaceAll("\\$\\{rule_uuid\\}", ruleUuid).replaceAll("\\$\\{rule_code\\}", ruleCode).replaceAll("\\$\\{module_name\\}", moduleName);
        return ruleContent;
    }


    @Test
    public void testWriteReadKieModule() throws IOException
    {
        if(checkCMCDisabled())
        {
            return;
        }
        KieModule kieModule = getKieModule();
        this.defaultKieModuleService.saveKieModule("kieModuleName", "groupId:artifactId:version1", kieModule);
        Optional<KieModule> kieModuleOptional = this.defaultKieModuleService.loadKieModule("kieModuleName", "groupId:artifactId:version1");
        Assertions.assertThat(kieModuleOptional.isPresent()).isTrue();
        Assertions.assertThat(kieModuleOptional.get() instanceof org.drools.compiler.kie.builder.impl.MemoryKieModule).isTrue();
        KieBase kieBase = getKieBase(kieModuleOptional.get());
        Assertions.assertThat(kieBase.getRule("de.hybris.platform.ruleengine.drools", "ruleUuid1")).isNotNull();
        Assertions.assertThat(kieBase.getRule("de.hybris.platform.ruleengine.drools", "ruleUuid2")).isNotNull();
        Assertions.assertThat(kieBase.getRule("de.hybris.platform.ruleengine.drools", "ruleUuid3")).isNull();
    }


    @Test
    public void testWriteRemoveKieModules() throws IOException
    {
        if(checkCMCDisabled())
        {
            return;
        }
        KieModule kieModule = getKieModule();
        this.defaultKieModuleService.saveKieModule("kieModuleName", "groupId:artifactId:version1", kieModule);
        this.defaultKieModuleService.saveKieModule("kieModuleName", "groupId:artifactId:version2", kieModule);
        this.defaultKieModuleService.saveKieModule("kieModuleName", "groupId:artifactId:version16", kieModule);
        this.defaultKieModuleService.saveKieModule("kieModuleName2", "groupId:artifactId:version3", kieModule);
        this.defaultKieModuleService.saveKieModule("kieModuleName2", "groupId:artifactId:version4", kieModule);
        this.defaultKieModuleService.saveKieModule("kieModuleName3", "groupId:artifactId:version5", kieModule);
        removeOldKieModules();
        Optional<KieModule> kieModuleOptional1 = this.defaultKieModuleService.loadKieModule("kieModuleName", "groupId:artifactId:version1");
        Assertions.assertThat(kieModuleOptional1.isPresent()).isFalse();
        Optional<KieModule> kieModuleOptional2 = this.defaultKieModuleService.loadKieModule("kieModuleName", "groupId:artifactId:version2");
        Assertions.assertThat(kieModuleOptional2.isPresent()).isFalse();
        Optional<KieModule> kieModuleOptional3 = this.defaultKieModuleService.loadKieModule("kieModuleName2", "groupId:artifactId:version3");
        Assertions.assertThat(kieModuleOptional3.isPresent()).isFalse();
        Optional<KieModule> kieModuleOptional6 = this.defaultKieModuleService.loadKieModule("kieModuleName", "groupId:artifactId:version16");
        Assertions.assertThat(kieModuleOptional6.isPresent()).isTrue();
        KieBase kieBase = getKieBase(kieModuleOptional6.get());
        Assertions.assertThat(kieBase.getRule("de.hybris.platform.ruleengine.drools", "ruleUuid1")).isNotNull();
        Assertions.assertThat(kieBase.getRule("de.hybris.platform.ruleengine.drools", "ruleUuid2")).isNotNull();
        Assertions.assertThat(kieBase.getRule("de.hybris.platform.ruleengine.drools", "ruleUuid3")).isNull();
        Optional<KieModule> kieModuleOptional4 = this.defaultKieModuleService.loadKieModule("kieModuleName2", "groupId:artifactId:version4");
        Assertions.assertThat(kieModuleOptional4.isPresent()).isTrue();
        Optional<KieModule> kieModuleOptional5 = this.defaultKieModuleService.loadKieModule("kieModuleName3", "groupId:artifactId:version5");
        Assertions.assertThat(kieModuleOptional5.isPresent()).isTrue();
    }


    protected void removeOldKieModules()
    {
        this.configurationService.getConfiguration().setProperty("kieModuleCleanupStrategy.lastVersionsOfKieModulesToKeep", "1");
        CronJobModel cjm = new CronJobModel();
        MaintenanceCleanupJobModel cleanupJob = new MaintenanceCleanupJobModel();
        cjm.setJob((JobModel)cleanupJob);
        SearchResult<DroolsKIEModuleModel> search = this.flexibleSearchService.search(this.kieModuleCleanupStrategy
                        .createFetchQuery(cjm));
        List<DroolsKIEModuleModel> kieModulesToRemoveOldMediaFor = search.getResult();
        this.kieModuleCleanupStrategy.process(kieModulesToRemoveOldMediaFor);
    }


    protected KieBase getKieBase(KieModule kieModule)
    {
        KieServices kieServices = KieServices.Factory.get();
        KieRepository kieRepository = kieServices.getRepository();
        kieRepository.addKieModule(kieModule);
        KieContainer kieContainer = kieServices.newKieContainer(kieModule.getReleaseId());
        return kieContainer.getKieBase();
    }


    protected KieModule getKieModule() throws IOException
    {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kfs = kieServices.newKieFileSystem();
        kfs.write("src/main/resources/rule1.drl", kieServices
                        .getResources().newByteArrayResource(getRuleContent("ruleUuid1", "ruleCode1", "moduleName1").getBytes()));
        kfs.write("src/main/resources/rule2.drl", kieServices
                        .getResources().newByteArrayResource(getRuleContent("ruleUuid2", "ruleCode2", "moduleName2").getBytes()));
        KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
        Results results = kieBuilder.getResults();
        if(results.hasMessages(new Message.Level[] {Message.Level.ERROR}))
        {
            throw new IllegalStateException((String)results.getMessages().stream().map(m -> m.getText()).collect(Collectors.joining(", ")));
        }
        KieModule kieModule = kieBuilder.getKieModule();
        return kieModule;
    }


    protected boolean checkCMCDisabled()
    {
        if(!this.configurationService.getConfiguration().getBoolean("ruleengine.centralized.module.compilation.enabled"))
        {
            LOGGER.info("skipping tests as centralized module compilation is disabled.");
            return true;
        }
        return false;
    }
}
