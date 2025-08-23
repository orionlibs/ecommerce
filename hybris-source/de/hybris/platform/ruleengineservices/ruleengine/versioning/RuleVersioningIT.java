package de.hybris.platform.ruleengineservices.ruleengine.versioning;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Resources;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengine.test.RuleEngineTestSupportService;
import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import java.io.IOException;
import java.net.URL;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Resource;
import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class RuleVersioningIT extends ServicelayerTest
{
    private static final String MODULE_NAME = "versioningTestModule";
    @Resource
    private ModelService modelService;
    @Resource
    private RuleEngineTestSupportService ruleEngineTestSupportService;
    private AbstractRuleEngineRuleModel ruleModel;
    private AbstractRuleEngineRuleModel ruleModel2;
    private AbstractRulesModuleModel module;
    private SourceRuleModel sourceRuleModel;


    @Before
    public void setUp() throws Exception
    {
        this.ruleModel = getRuleFromResource("ruleengine/test/versioning/rule1.drl", "ruleengine/test/versioning/rule.drl", "1d1c86c4-05c0-4fa1-a3b0-35dfaee8129a");
        this.module = this.ruleEngineTestSupportService.associateRulesToNewModule("versioningTestModule", (Set)ImmutableSet.of(this.ruleModel));
        this.modelService.save(this.ruleModel);
        this.ruleModel2 = createEmptyRule("ruleengine/test/versioning/rule4.drl", "1d1c86c4-05c0-4fa1-a3b0-35dfaee8129b");
        this.ruleEngineTestSupportService.associateRulesToNewModule("versioningTestModule", (Set)ImmutableSet.of(this.ruleModel2));
        this.ruleModel2.setActive(Boolean.TRUE);
        this.modelService.save(this.ruleModel2);
        this.sourceRuleModel = createSourceRule();
        this.modelService.save(this.sourceRuleModel);
    }


    @Test
    public void shouldRaiseExceptionWhenTryingToSaveSourceRuleBasedRuleVersionLessThenModuleVersion() throws Exception
    {
        this.ruleModel.setRuleContent(readFromResource("ruleengine/test/versioning/rule1_modified.drl"));
        this.modelService.save(this.ruleModel);
        AbstractRuleEngineRuleModel newRuleModel = createEmptyRule("ruleengine/test/versioning/rule3.drl", "ruleengine/test/versioning/rule3");
        newRuleModel.setVersion(Long.valueOf(0L));
        newRuleModel.setSourceRule((AbstractRuleModel)this.sourceRuleModel);
        this.ruleEngineTestSupportService.associateRulesModule(this.module, (Set)ImmutableSet.of(newRuleModel));
        Throwable throwable = ThrowableAssert.catchThrowable(() -> this.modelService.save(newRuleModel));
        ((AbstractThrowableAssert)Assertions.assertThat(throwable).isInstanceOf(ModelSavingException.class))
                        .hasMessageContaining("Non active rule version cannot increase overall knowledgebase snapshot version");
    }


    protected SourceRuleModel createSourceRule()
    {
        SourceRuleModel sourceRuleModel = new SourceRuleModel();
        sourceRuleModel.setVersion(Long.valueOf(1L));
        sourceRuleModel.setCode(UUID.randomUUID().toString());
        sourceRuleModel.setPriority(Integer.valueOf(100));
        sourceRuleModel.setStatus(RuleStatus.PUBLISHED);
        return sourceRuleModel;
    }


    protected String readFromResource(String resourceName) throws IOException
    {
        URL url = Resources.getResource(resourceName);
        return Resources.toString(url, Charsets.UTF_8);
    }


    protected AbstractRuleEngineRuleModel getRuleFromResource(String resourceName, String ruleCode, String ruleUUID) throws IOException
    {
        AbstractRuleEngineRuleModel rule = createEmptyRule(ruleCode, ruleUUID);
        rule.setActive(Boolean.TRUE);
        rule.setRuleContent(readFromResource(resourceName));
        return rule;
    }


    protected AbstractRuleEngineRuleModel createEmptyRule(String ruleCode, String ruleUUID) throws IOException
    {
        AbstractRuleEngineRuleModel rule = this.ruleEngineTestSupportService.createRuleModel();
        rule.setCode(ruleCode);
        rule.setUuid(ruleUUID);
        rule.setActive(Boolean.FALSE);
        return rule;
    }
}
