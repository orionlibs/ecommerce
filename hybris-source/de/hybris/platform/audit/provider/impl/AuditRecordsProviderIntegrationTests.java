package de.hybris.platform.audit.provider.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.audit.AuditTestHelper;
import de.hybris.platform.audit.AuditableTest;
import de.hybris.platform.audit.TypeAuditReportConfig;
import de.hybris.platform.audit.internal.config.AuditReportConfig;
import de.hybris.platform.audit.provider.AuditRecordsProvider;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.persistence.audit.AuditType;
import de.hybris.platform.persistence.audit.gateway.AuditRecord;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ListAssert;
import org.assertj.core.groups.Tuple;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class AuditRecordsProviderIntegrationTests extends ServicelayerBaseTest implements AuditableTest
{
    private final PropertyConfigSwitcher persistenceLegacyModeSwitch = new PropertyConfigSwitcher("persistence.legacy.mode");
    @Resource
    protected ModelService modelService;
    @Resource
    protected UserService userService;
    @Resource
    private AuditRecordsProvider auditRecordsProvider;
    private AuditTestHelper helper;


    @Before
    public void setUp() throws Exception
    {
        assumeAuditEnabled();
        this.helper = new AuditTestHelper();
        this.persistenceLegacyModeSwitch.switchToValue("true");
    }


    @After
    public void cleanup()
    {
        this.persistenceLegacyModeSwitch.switchBackToDefault();
        this.helper.removeCreatedItems();
        this.helper.clearAuditDataForTypes(new String[] {"Title", "Address", "User", "Media"});
    }


    @Test
    public void shouldReturnAuditRecordsForConfig() throws Exception
    {
        AuditReportConfig testAuditReportConfig = this.helper.createTestConfigForIntegrationTest();
        UserModel user = this.helper.prepareTestDataForIntegrationTest();
        PK userPk = user.getPk();
        TypeAuditReportConfig config = TypeAuditReportConfig.builder().withConfig(testAuditReportConfig).withFullReport().withRootTypePk(userPk).build();
        List<AuditRecord> records = (List<AuditRecord>)this.auditRecordsProvider.getRecords(config).collect(Collectors.toList());
        ((ListAssert)Assertions.assertThat(records).isNotNull().extracting(new Function[] {AuditRecord::getType, AuditRecord::getAuditType}).containsSubsequence(
                        (Object[])new Tuple[] {Assertions.tuple(new Object[] {"User", AuditType.MODIFICATION}), Assertions.tuple(new Object[] {"Address", AuditType.MODIFICATION}), Assertions.tuple(new Object[] {"Media", AuditType.MODIFICATION}),
                                        Assertions.tuple(new Object[] {"Address", AuditType.DELETION})})).containsOnlyOnce(
                        (Object[])new Tuple[] {Assertions.tuple(new Object[] {"User", AuditType.CURRENT}), Assertions.tuple(new Object[] {"Address", AuditType.CURRENT}), Assertions.tuple(new Object[] {"Title", AuditType.CURRENT}), Assertions.tuple(new Object[] {"Media", AuditType.CURRENT})});
        ((AbstractListAssert)Assertions.assertThat(records)
                        .filteredOn(ar -> ar.getType().equals("Title")))
                        .extracting(new Function[] {ar -> AuditTestHelper.getAuditRecordsAttributes(ar).get("code"), AuditRecord::getAuditType}).containsSubsequence((Object[])new Tuple[] {Assertions.tuple(new Object[] {"Mr", AuditType.CURRENT})});
        ((AbstractListAssert)Assertions.assertThat(records)
                        .filteredOn(ar -> ar.getType().equals("Address")))
                        .extracting(new Function[] {ar -> AuditTestHelper.getAuditRecordsAttributes(ar).get("town"), ar -> AuditTestHelper.getAuditRecordsAttributes(ar).get("streetname"), AuditRecord::getAuditType}).containsSubsequence(
                                        (Object[])new Tuple[] {Assertions.tuple(new Object[] {"Sosnowiec", "Chopina", AuditType.MODIFICATION}), Assertions.tuple(new Object[] {"New York", "55th St.", AuditType.DELETION}), Assertions.tuple(new Object[] {"Sosnowiec", "Chopina", AuditType.CURRENT})});
        ((AbstractListAssert)Assertions.assertThat(records)
                        .filteredOn(ar -> ar.getType().equals("Media")))
                        .extracting(new Function[] {ar -> AuditTestHelper.getAuditRecordsAttributes(ar).get("code"), AuditRecord::getAuditType})
                        .containsSubsequence((Object[])new Tuple[] {Assertions.tuple(new Object[] {"ugly picture of me", AuditType.MODIFICATION}), Assertions.tuple(new Object[] {"ugly picture of me", AuditType.CURRENT})});
    }


    @Test
    public void shouldReturnAuditRecordsForConfigWithVirtualAttribute() throws Exception
    {
        AuditReportConfig testAuditReportConfig = this.helper.createTestConfigWithVirtualAttributeForIntegrationTest();
        UserModel user = this.helper.prepareTestDataForIntegrationTest();
        PK userPk = user.getPk();
        TypeAuditReportConfig config = TypeAuditReportConfig.builder().withConfig(testAuditReportConfig).withRootTypePk(userPk).build();
        List<AuditRecord> records = (List<AuditRecord>)this.auditRecordsProvider.getRecords(config).collect(Collectors.toList());
        ((ListAssert)Assertions.assertThat(records).isNotNull().extracting(new Function[] {AuditRecord::getType, AuditRecord::getAuditType}).containsSubsequence(
                        (Object[])new Tuple[] {Assertions.tuple(new Object[] {"User", AuditType.MODIFICATION}), Assertions.tuple(new Object[] {"Address", AuditType.MODIFICATION}), Assertions.tuple(new Object[] {"Media", AuditType.MODIFICATION}),
                                        Assertions.tuple(new Object[] {"Title", AuditType.CREATION}), Assertions.tuple(new Object[] {"Address", AuditType.CREATION}), Assertions.tuple(new Object[] {"Title", AuditType.DELETION}), Assertions.tuple(new Object[] {"Address", AuditType.DELETION}),
                                        Assertions.tuple(new Object[] {"Address", AuditType.DELETION})})).contains(
                        (Object[])new Tuple[] {Assertions.tuple(new Object[] {"User", AuditType.CURRENT}), Assertions.tuple(new Object[] {"Address", AuditType.CURRENT}), Assertions.tuple(new Object[] {"Address", AuditType.CURRENT}), Assertions.tuple(new Object[] {"Title", AuditType.CURRENT}),
                                        Assertions.tuple(new Object[] {"Title", AuditType.CURRENT}), Assertions.tuple(new Object[] {"Media", AuditType.CURRENT})});
        ((ListAssert)((AbstractListAssert)Assertions.assertThat(records)
                        .filteredOn(ar -> ar.getType().equals("Title")))
                        .extracting(new Function[] {ar -> AuditTestHelper.getAuditRecordsAttributes(ar).get("code"), AuditRecord::getAuditType})
                        .containsSubsequence((Object[])new Tuple[] {Assertions.tuple(new Object[] {"Sir", AuditType.CREATION}), Assertions.tuple(new Object[] {"Sir", AuditType.DELETION})})).contains(
                        (Object[])new Tuple[] {Assertions.tuple(new Object[] {"Engr.", AuditType.CURRENT}), Assertions.tuple(new Object[] {"Mr", AuditType.CURRENT})});
        ((ListAssert)((AbstractListAssert)Assertions.assertThat(records)
                        .filteredOn(ar -> ar.getType().equals("Address")))
                        .extracting(new Function[] {ar -> AuditTestHelper.getAuditRecordsAttributes(ar).get("town"), ar -> AuditTestHelper.getAuditRecordsAttributes(ar).get("streetname"), AuditRecord::getAuditType}).containsSubsequence(
                                        (Object[])new Tuple[] {Assertions.tuple(new Object[] {"Sosnowiec", "Chopina", AuditType.MODIFICATION}), Assertions.tuple(new Object[] {"Krakow", "Rynek", AuditType.CREATION}), Assertions.tuple(new Object[] {"Krakow", "Rynek", AuditType.DELETION}),
                                                        Assertions.tuple(new Object[] {"New York", "55th St.", AuditType.DELETION})})).contains((Object[])new Tuple[] {Assertions.tuple(new Object[] {"Sosnowiec", "Chopina", AuditType.CURRENT})});
        ((AbstractListAssert)Assertions.assertThat(records)
                        .filteredOn(ar -> ar.getType().equals("Media")))
                        .extracting(new Function[] {ar -> AuditTestHelper.getAuditRecordsAttributes(ar).get("code"), AuditRecord::getAuditType})
                        .containsSubsequence((Object[])new Tuple[] {Assertions.tuple(new Object[] {"ugly picture of me", AuditType.MODIFICATION}), Assertions.tuple(new Object[] {"ugly picture of me", AuditType.CURRENT})});
    }


    public void assertContainsAddress(ArrayList addresses, String streetname)
    {
        boolean contains = false;
        for(Object address : addresses)
        {
            if(((Map)address).get("streetname").equals(streetname))
            {
                contains = true;
                break;
            }
        }
        Assertions.assertThat(contains).isTrue();
    }
}
