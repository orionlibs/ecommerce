package com.hybris.backoffice.user;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.impex.impl.ClasspathImpExResource;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;
import java.nio.charset.StandardCharsets;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class BackofficeUserServiceIntegrationTest extends ServicelayerTest
{
    private static final String CONFIG_KEY_LEGACY_ADMIN_CHECK_ENABLED = "backoffice.admincheck.legacy.enabled";
    @Resource(name = "backofficeUserService")
    private BackofficeUserService testSubject;
    @Resource
    private SessionService sessionService;
    @Resource
    private BackofficeRoleService backofficeRoleService;


    @Before
    public void setUp() throws Exception
    {
        importEssentialData();
        importTestData();
    }


    @Test
    public void isAdminShouldReturnTrueForAdmingroupUser()
    {
        UserModel user = this.testSubject.getUserForUID("directadmin");
        this.testSubject.setCurrentUser(user);
        Assertions.assertThat(this.testSubject.isAdmin(user)).isTrue();
    }


    @Test
    public void isAdminShouldReturnTrueForBackofficeadmingroupUser()
    {
        UserModel user = this.testSubject.getUserForUID("directbackofficeadmin");
        this.testSubject.setCurrentUser(user);
        Assertions.assertThat(this.testSubject.isAdmin(user)).isTrue();
    }


    @Test
    public void isAdminShouldReturnTrueForAdminSubgroupUser()
    {
        UserModel user = this.testSubject.getUserForUID("indirectadmin");
        this.testSubject.setCurrentUser(user);
        Assertions.assertThat(this.testSubject.isAdmin(user)).isTrue();
    }


    @Test
    public void isAdminShouldReturnTrueForBackofficeadminSubgroupUser()
    {
        UserModel user = this.testSubject.getUserForUID("indirectbackofficeadmin");
        this.testSubject.setCurrentUser(user);
        Assertions.assertThat(this.testSubject.isAdmin(user)).isTrue();
    }


    @Test
    public void isAdminShouldReturnTrueForActiveAdminrole()
    {
        UserModel user = this.testSubject.getUserForUID("roleadmin");
        this.testSubject.setCurrentUser(user);
        this.backofficeRoleService.setActiveRole("backofficeadministratorrole");
        Assertions.assertThat(this.testSubject.isAdmin(user)).isTrue();
    }


    @Test
    public void isAdminShouldReturnFalseForActiveUserrole()
    {
        UserModel user = this.testSubject.getUserForUID("roleadmin");
        this.testSubject.setCurrentUser(user);
        this.backofficeRoleService.setActiveRole("backofficeuserrole");
        Assertions.assertThat(this.testSubject.isAdmin(user)).isFalse();
    }


    @Test
    public void isAdminLegacyModeShouldReturnTrueForActiveUserrole()
    {
        UserModel user = this.testSubject.getUserForUID("roleadmin");
        this.testSubject.setCurrentUser(user);
        this.backofficeRoleService.setActiveRole("backofficeuserrole");
        Config.setParameter("backoffice.admincheck.legacy.enabled", "true");
        Assertions.assertThat(this.testSubject.isAdmin(user)).isTrue();
    }


    @Test
    public void isAdminShouldUseLegacyModeForNonCurrentUser()
    {
        UserModel user = this.testSubject.getUserForUID("roleadmin");
        this.testSubject.setCurrentUser(this.testSubject.getUserForUID("indirectadmin"));
        this.backofficeRoleService.setActiveRole("backofficeuserrole");
        Assertions.assertThat(this.testSubject.isAdmin(user)).isTrue();
    }


    private void importEssentialData() throws ImpExException
    {
        ClasspathImpExResource classpathImpExResource1 = new ClasspathImpExResource("/impex/essentialdataGroups.impex", StandardCharsets.UTF_8.name());
        ClasspathImpExResource classpathImpExResource2 = new ClasspathImpExResource("/impex/essentialdataCreateAuditWorkflowTemplate.impex", StandardCharsets.UTF_8.name());
        ClasspathImpExResource classpathImpExResource3 = new ClasspathImpExResource("/impex/essentialdataUsersAndGroups.impex", StandardCharsets.UTF_8.name());
        ClasspathImpExResource classpathImpExResource4 = new ClasspathImpExResource("/impex/projectdataUsersAndGroups.impex", StandardCharsets.UTF_8.name());
        importData((ImpExResource)classpathImpExResource1);
        importData((ImpExResource)classpathImpExResource2);
        importData((ImpExResource)classpathImpExResource3);
        importData((ImpExResource)classpathImpExResource4);
    }


    private void importTestData() throws ImpExException
    {
        ClasspathImpExResource classpathImpExResource = new ClasspathImpExResource("/test/userservice/usersAndGroups.impex", StandardCharsets.UTF_8.name());
        importData((ImpExResource)classpathImpExResource);
    }
}
