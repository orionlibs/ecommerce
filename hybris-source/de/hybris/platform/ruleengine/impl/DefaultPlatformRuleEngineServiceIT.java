package de.hybris.platform.ruleengine.impl;

import com.google.common.collect.ImmutableMap;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.ruleengine.ExecutionContext;
import de.hybris.platform.ruleengine.RuleEngineActionResult;
import de.hybris.platform.ruleengine.RuleEngineService;
import de.hybris.platform.ruleengine.RuleEvaluationContext;
import de.hybris.platform.ruleengine.RuleEvaluationResult;
import de.hybris.platform.ruleengine.init.InitializationFuture;
import de.hybris.platform.ruleengine.init.RuleEngineKieModuleSwapper;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@IntegrationTest
public class DefaultPlatformRuleEngineServiceIT extends AbstractPlatformRuleEngineServiceIT
{
    private static final String TEST_MODULE_NAME = "ruleengine-test-module";
    private static final String TEST_BASE_NAME = "ruleengine-test-base";
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Resource
    private RuleEngineService platformRuleEngineService;
    @Resource
    private RuleEngineKieModuleSwapper ruleEngineKieModuleSwapper;


    @Before
    public void setUp() throws IOException, ImpExException
    {
        importCsv("/ruleengine/test/ruleenginesetup.impex", "utf-8");
        this
                        .ruleTemplateContent = new String(Files.readAllBytes(Paths.get((new ClassPathResource("/ruleengine/test/impl/drools_rule_template.drl")).getURI())));
        this
                        .ruleTemplateWrongContent = new String(Files.readAllBytes(
                        Paths.get((new ClassPathResource("/ruleengine/test/impl/drools_rule_template_wrong.drl")).getURI())));
    }


    @Test
    public void testInitializeOnStartupFail()
    {
        DroolsKIEModuleModel rulesModule = createRulesForModule("ruleengine-test-module", "ruleengine-test-base", 100);
        createNewDroolsRule(UUID.randomUUID().toString(), "ruleengine-test-module_rule_WRONG", "ruleengine-test-module", this.ruleTemplateWrongContent, rulesModule
                        .getKieBases().iterator().next());
        assertResultIsKo(this.platformRuleEngineService.initialize(Collections.singletonList(rulesModule), false, false), "NONE");
        Assertions.assertThat(rulesModule.getDeployedMvnVersion()).isNull();
    }


    @Test
    public void testInitializeOnStartupSingleModule()
    {
        DroolsKIEModuleModel rulesModule = createRulesForModule("ruleengine-test-module", "ruleengine-test-base", 10);
        assertResultIsOk(this.platformRuleEngineService.initialize(Collections.singletonList(rulesModule), false, false), rulesModule);
        assertModuleModelIsOk(rulesModule);
    }


    @Test
    public void testInitializeOnStartupMultipleModules()
    {
        DroolsKIEModuleModel rulesModule1 = createRulesForModule("ruleengine-test-module1", "ruleengine-test-base1", 100);
        DroolsKIEModuleModel rulesModule2 = createRulesForModule("ruleengine-test-module2", "ruleengine-test-base2", 100);
        DroolsKIEModuleModel rulesModule3 = createRulesForModule("ruleengine-test-module3", "ruleengine-test-base3", 100);
        RuleEngineActionResult result1 = createRuleEngineActionResult();
        this.platformRuleEngineService.initializeNonBlocking((AbstractRulesModuleModel)rulesModule1, null, true, false, result1);
        RuleEngineActionResult result2 = createRuleEngineActionResult();
        this.platformRuleEngineService.initializeNonBlocking((AbstractRulesModuleModel)rulesModule2, null, true, false, result2);
        RuleEngineActionResult result3 = createRuleEngineActionResult();
        this.platformRuleEngineService.initializeNonBlocking((AbstractRulesModuleModel)rulesModule3, null, true, false, result3);
        this.ruleEngineKieModuleSwapper.waitForSwappingToFinish();
        assertModuleModelIsOk(rulesModule1);
        assertModuleModelIsOk(rulesModule2);
        assertModuleModelIsOk(rulesModule3);
        assertResultIsOk(result1, rulesModule1);
        assertResultIsOk(result2, rulesModule2);
        assertResultIsOk(result3, rulesModule3);
    }


    @Test
    public void testInitializeSingleModuleUpdateWrongRule()
    {
        DroolsKIEModuleModel rulesModule = createRulesForModule("ruleengine-test-module", "ruleengine-test-base", 100);
        assertResultIsOk(this.platformRuleEngineService.initialize(Collections.singletonList(rulesModule), false, false), rulesModule);
        createNewDroolsRule(UUID.randomUUID().toString(), "ruleengine-test-module_rule_WRONG", "ruleengine-test-module", this.ruleTemplateWrongContent, rulesModule
                        .getKieBases().iterator().next());
        assertResultIsKo(this.platformRuleEngineService.initialize(Collections.singletonList(rulesModule), false, false), rulesModule
                        .getDeployedMvnVersion());
    }


    @Test
    public void testEvaluateRule()
    {
        DroolsKIEModuleModel rulesModule = createRulesForModule("ruleengine-test-module", "ruleengine-test-base", 10);
        assertResultIsOk(this.platformRuleEngineService.initialize(Collections.singletonList(rulesModule), false, false), rulesModule);
        RuleEvaluationContext ruleEvaluationContext = createRuleEvaluationContext(rulesModule);
        RuleEvaluationResult evaluationResult = this.platformRuleEngineService.evaluate(ruleEvaluationContext);
        Assertions.assertThat(evaluationResult).isNotNull();
    }


    @Test
    public void testEvaluateRuleRuleEngineNotInitialized()
    {
        DroolsKIEModuleModel rulesModule = createRulesForModule("ruleengine-test-modulenotinitialized", "ruleengine-test-base", 10);
        RuleEvaluationContext ruleEvaluationContext = createRuleEvaluationContext(rulesModule);
        RuleEvaluationResult result = this.platformRuleEngineService.evaluate(ruleEvaluationContext);
        Assertions.assertThat(result.isEvaluationFailed()).isTrue();
        Assertions.assertThat(result.getErrorMessage()).contains(new CharSequence[] {"Cannot complete the evaluation: rule engine was not initialized for releaseId"});
    }


    @Test
    public void testEvaluateRuleWhileSwitchingKieModule()
    {
        DroolsKIEModuleModel rulesModule = createRulesForModule("ruleengine-test-module", "ruleengine-test-base", 100);
        assertResultIsOk(this.platformRuleEngineService.initialize(Collections.singletonList(rulesModule), false, false), rulesModule);
        String deployedMvnversion = rulesModule.getDeployedMvnVersion();
        createNewDroolsRule(UUID.randomUUID().toString(), "ruleengine-test-module_rule_NEW", "ruleengine-test-module", this.ruleTemplateContent, rulesModule
                        .getKieBases().iterator().next());
        RuleEngineActionResult result = createRuleEngineActionResult();
        this.platformRuleEngineService.initializeNonBlocking((AbstractRulesModuleModel)rulesModule, null, false, false, result);
        RuleEvaluationContext ruleEvaluationContext = createRuleEvaluationContext(rulesModule);
        RuleEvaluationResult evaluationResult = this.platformRuleEngineService.evaluate(ruleEvaluationContext);
        this.ruleEngineKieModuleSwapper.waitForSwappingToFinish();
        assertResultIsOk(result, rulesModule, deployedMvnversion);
        Assertions.assertThat(evaluationResult).isNotNull();
    }


    @Test
    public void testInitializeRuleWhileSwitchingKieModule()
    {
        DroolsKIEModuleModel rulesModule = createRulesForModule("ruleengine-test-module", "ruleengine-test-base", 100);
        RuleEngineActionResult result = createRuleEngineActionResult();
        RetryTemplate retryTemplate = createPublishRetryTemplate();
        DefaultPlatformRuleEngineService platformRuleEngineService0 = (DefaultPlatformRuleEngineService)this.platformRuleEngineService;
        platformRuleEngineService0.setRuleEnginePublishRetryTemplate(retryTemplate);
        this.platformRuleEngineService.initializeNonBlocking((AbstractRulesModuleModel)rulesModule, null, false, false, result);
        createNewDroolsRule(UUID.randomUUID().toString(), "ruleengine-test-module_rule_NEW", "ruleengine-test-module", this.ruleTemplateContent, rulesModule
                        .getKieBases().iterator().next());
        RuleEngineActionResult result1 = createRuleEngineActionResult();
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.isActionFailed()).isFalse();
        this.platformRuleEngineService.initializeNonBlocking((AbstractRulesModuleModel)rulesModule, null, false, false, result1);
        Assertions.assertThat(result1).isNotNull();
        Assertions.assertThat(result1.isActionFailed()).isFalse();
    }


    private void assertResultIsOk(InitializationFuture initializationFuture, DroolsKIEModuleModel module)
    {
        List<RuleEngineActionResult> results = initializationFuture.waitForInitializationToFinish().getResults();
        Assertions.assertThat(results).isNotEmpty();
        for(RuleEngineActionResult result : results)
        {
            assertResultIsOk(result, module, "NONE");
        }
    }


    private void assertResultIsOk(RuleEngineActionResult result, DroolsKIEModuleModel module)
    {
        assertResultIsOk(result, module, "NONE");
    }


    private void assertResultIsOk(RuleEngineActionResult result, DroolsKIEModuleModel module, String oldDeployedMvnVersion)
    {
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.isActionFailed()).isFalse();
        Assertions.assertThat(result.getDeployedVersion()).matches(module.getDeployedMvnVersion());
        Assertions.assertThat(result.getOldVersion()).isEqualTo(oldDeployedMvnVersion);
        Assertions.assertThat(result.getResults()).isNullOrEmpty();
    }


    private void assertResultIsKo(InitializationFuture initializationFuture, String deployedMvnVersion)
    {
        List<RuleEngineActionResult> results = initializationFuture.waitForInitializationToFinish().getResults();
        Assertions.assertThat(results).isNotEmpty();
        for(RuleEngineActionResult result : results)
        {
            Assertions.assertThat(result).isNotNull();
            Assertions.assertThat(result.isActionFailed()).isTrue();
            Assertions.assertThat(result.getDeployedVersion()).isEqualTo(deployedMvnVersion);
            Assertions.assertThat(result.getOldVersion()).isEqualTo(deployedMvnVersion);
            Assertions.assertThat(result.getResults()).isNotEmpty();
        }
    }


    private void assertModuleModelIsOk(DroolsKIEModuleModel module)
    {
        Assertions.assertThat(module.getDeployedMvnVersion()).isEqualTo("1.0.0." + module.getVersion());
    }


    private RuleEngineActionResult createRuleEngineActionResult()
    {
        RuleEngineActionResult result = new RuleEngineActionResult();
        result.setExecutionContext(new ExecutionContext());
        return result;
    }


    private RetryTemplate createPublishRetryTemplate()
    {
        RetryTemplate retryTemplate = new RetryTemplate();
        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(3, (Map)ImmutableMap.of(RuntimeException.class, Boolean.valueOf(true)));
        retryTemplate.setRetryPolicy((RetryPolicy)simpleRetryPolicy);
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(2000L);
        retryTemplate.setBackOffPolicy((BackOffPolicy)backOffPolicy);
        return retryTemplate;
    }


    @After
    public void tearDown()
    {
        this.ruleEngineKieModuleSwapper.waitForSwappingToFinish();
    }
}
