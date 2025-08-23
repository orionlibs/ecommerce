package de.hybris.platform.ruleengine.versioning;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Resources;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.ruleengine.dao.EngineRuleDao;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengine.test.RuleEngineTestSupportService;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Resource;
import org.assertj.core.api.ThrowableAssert;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class RuleVersioningIT extends ServicelayerTest
{
    private static final String MODULE_NAME = "versioningTestModule";
    @Resource
    private ModelService modelService;
    @Resource
    private EngineRuleDao engineRuleDao;
    @Resource
    private RuleEngineTestSupportService ruleEngineTestSupportService;
    private AbstractRuleEngineRuleModel ruleModel;
    private AbstractRuleEngineRuleModel ruleModel2;
    private AbstractRulesModuleModel module;


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
    }


    @Test
    public void testRuleWithoutContent() throws Exception
    {
        Assertions.assertThat(this.ruleModel2.getRuleContent()).isEqualTo(null);
        Assertions.assertThat(this.ruleModel2.getChecksum()).isEqualTo(null);
        this.ruleModel2.setRuleContent(readFromResource("ruleengine/test/versioning/rule4.drl"));
        this.modelService.save(this.ruleModel2);
        this.ruleModel2 = this.engineRuleDao.getRuleByCode("ruleengine/test/versioning/rule4.drl", "versioningTestModule");
        Assertions.assertThat(this.ruleModel2.getRuleContent()).isNotEqualTo(null);
        Assertions.assertThat(this.ruleModel2.getChecksum()).isNotEqualTo(null);
    }


    @Test
    public void testRuleAndModuleInitialVersion() throws Exception
    {
        Assertions.assertThat(this.ruleModel.getVersion()).isEqualTo(1L);
        Assertions.assertThat(this.ruleModel.getCurrentVersion()).isEqualTo(true);
        Assertions.assertThat(this.ruleModel.getChecksum()).isNotEqualTo(null);
        Assertions.assertThat(this.module.getVersion()).isEqualTo(2L);
    }


    @Test
    public void testRuleAndModuleChangeVersionSync() throws Exception
    {
        this.ruleModel.setRuleContent(readFromResource("ruleengine/test/versioning/rule1_modified.drl"));
        this.modelService.save(this.ruleModel);
        this.ruleModel = this.engineRuleDao.getRuleByCode("ruleengine/test/versioning/rule.drl", "versioningTestModule");
        Assertions.assertThat(this.ruleModel.getVersion()).isEqualTo(3L);
        Assertions.assertThat(this.ruleModel.getCurrentVersion()).isEqualTo(true);
        Optional<AbstractRulesModuleModel> associatedModuleOptional = this.ruleEngineTestSupportService.resolveAssociatedRuleModule(this.ruleModel);
        Assertions.assertThat(associatedModuleOptional.isPresent()).isTrue();
        Assertions.assertThat(((AbstractRulesModuleModel)associatedModuleOptional.get()).getVersion()).isEqualTo(3L);
    }


    @Test
    public void testRuleAndModuleVersionNotChangedIfContentIsSame() throws Exception
    {
        this.ruleModel.setRuleContent(readFromResource("ruleengine/test/versioning/rule1.drl"));
        this.ruleEngineTestSupportService.associateRulesToNewModule("versioningTestModule", (Set)ImmutableSet.of(this.ruleModel));
        this.modelService.save(this.ruleModel);
        this.ruleModel = this.engineRuleDao.getRuleByCode("ruleengine/test/versioning/rule.drl", "versioningTestModule");
        Assertions.assertThat(this.ruleModel.getVersion()).isEqualTo(1L);
        Assertions.assertThat(this.ruleModel.getCurrentVersion()).isEqualTo(true);
        Optional<AbstractRulesModuleModel> associatedModuleOptional = this.ruleEngineTestSupportService.resolveAssociatedRuleModule(this.ruleModel);
        Assertions.assertThat(associatedModuleOptional.isPresent()).isTrue();
        Assertions.assertThat(((AbstractRulesModuleModel)associatedModuleOptional.get()).getVersion()).isEqualTo(2L);
    }


    @Test
    public void testRuleVersionIfNewRuleIsAdded() throws Exception
    {
        this.ruleModel.setRuleContent(readFromResource("ruleengine/test/versioning/rule1_modified.drl"));
        this.modelService.save(this.ruleModel);
        AbstractRuleEngineRuleModel newRuleModel = getRuleFromResource("ruleengine/test/versioning/rule3.drl", "ruleengine/test/versioning/rule3.drl", "ruleengine/test/versioning/rule3");
        this.ruleEngineTestSupportService.associateRulesModule(this.module, (Set)ImmutableSet.of(newRuleModel));
        this.modelService.save(newRuleModel);
        newRuleModel = this.engineRuleDao.getRuleByCode("ruleengine/test/versioning/rule3.drl", "versioningTestModule");
        Assertions.assertThat(newRuleModel.getVersion()).isEqualTo(4L);
        Assertions.assertThat(newRuleModel.getCurrentVersion()).isEqualTo(true);
        Optional<AbstractRulesModuleModel> associatedModuleOptional = this.ruleEngineTestSupportService.resolveAssociatedRuleModule(newRuleModel);
        Assertions.assertThat(associatedModuleOptional.isPresent()).isTrue();
        Assertions.assertThat(((AbstractRulesModuleModel)associatedModuleOptional.get()).getVersion()).isEqualTo(4L);
    }


    @Test
    public void testRuleVersionEqualsToModuleVersion() throws Exception
    {
        this.ruleModel.setRuleContent(readFromResource("ruleengine/test/versioning/rule1_modified.drl"));
        this.ruleEngineTestSupportService.associateRulesModule(this.module, (Set)ImmutableSet.of(this.ruleModel));
        this.modelService.save(this.ruleModel);
        AbstractRuleEngineRuleModel newRuleModel = createEmptyRule("ruleengine/test/versioning/rule3.drl", "ruleengine/test/versioning/rule3");
        newRuleModel.setActive(Boolean.TRUE);
        newRuleModel.setVersion(Long.valueOf(1L));
        this.ruleEngineTestSupportService.associateRulesModule(this.module, (Set)ImmutableSet.of(newRuleModel));
        this.modelService.save(newRuleModel);
    }


    @Test
    public void shouldSaveManuallyCreatedRuleVersionWithoutContentValidation() throws Exception
    {
        this.ruleModel.setRuleContent(readFromResource("ruleengine/test/versioning/rule1_modified.drl"));
        this.modelService.save(this.ruleModel);
        AbstractRuleEngineRuleModel newRuleModel = createEmptyRule("ruleengine/test/versioning/rule3.drl", "ruleengine/test/versioning/rule3");
        newRuleModel.setVersion(Long.valueOf(0L));
        newRuleModel.setSourceRule(null);
        this.ruleEngineTestSupportService.associateRulesModule(this.module, (Set)ImmutableSet.of(newRuleModel));
        Throwable throwable = ThrowableAssert.catchThrowable(() -> this.modelService.save(newRuleModel));
        Assertions.assertThat(throwable).isNull();
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
