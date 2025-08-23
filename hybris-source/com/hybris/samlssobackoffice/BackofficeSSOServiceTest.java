package com.hybris.samlssobackoffice;

import com.google.common.collect.Sets;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.samlsinglesignon.model.SamlUserGroupModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import java.util.Collection;
import java.util.Set;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class BackofficeSSOServiceTest extends ServicelayerTransactionalTest
{
    private static final String SSO_DATABASE_USERGROUP_MAPPING = "sso.database.usergroup.mapping";
    @Resource
    private ModelService modelService;
    @Resource
    private UserService userService;
    @Resource
    private TypeService typeService;
    @Resource
    private BackofficeSSOService ssoUserService;
    private TypeModel employeeType;
    private UserGroupModel employeeGroup;
    private UserGroupModel adminGroup;


    @Before
    public void setup()
    {
        this.employeeType = this.typeService.getTypeForCode("Employee");
        this.employeeGroup = this.userService.getUserGroupForUID("employeegroup");
        this.adminGroup = this.userService.getUserGroupForUID("admingroup");
    }


    @Test
    public void shouldEnableBackofficeLoginWhenPropertyMappingEnablesIt()
    {
        Config.setParameter("sso.database.usergroup.mapping", "false");
        createMappingInProperties("ssoUserGroup", "Employee", this.employeeGroup.getUid(), "true");
        UserModel ssoUser = this.ssoUserService.getOrCreateSSOUser("ssoUserId", "ssoUserName", Sets.newHashSet((Object[])new String[] {"ssoUserGroup"}));
        assertSsoUser(ssoUser, "ssoUserId", "ssoUserName", Sets.newHashSet((Object[])new UserGroupModel[] {this.employeeGroup}, ), Boolean.valueOf(false), "Employee");
    }


    @Test
    public void shouldEnableBackofficeLoginIfAnyOfThePropertyMappingsEnableIt()
    {
        Config.setParameter("sso.database.usergroup.mapping", "false");
        createMappingInProperties("ssoUserGroup", "Employee", this.employeeGroup.getUid(), "false");
        createMappingInProperties("ssoUserGroup2", "Employee", this.adminGroup.getUid(), "true");
        UserModel ssoUser = this.ssoUserService.getOrCreateSSOUser("ssoUserId", "ssoUserName", Sets.newHashSet((Object[])new String[] {"ssoUserGroup", "ssoUserGroup2"}));
        Assertions.assertThat(ssoUser).isNotNull();
        Assertions.assertThat(ssoUser.getBackOfficeLoginDisabled()).isFalse();
    }


    @Test
    public void shouldDisallowAccessWhenPropertyMappingIsNotSet()
    {
        Config.setParameter("sso.database.usergroup.mapping", "false");
        Config.setParameter("sso.mapping.ssoUserGroup.usertype", "Employee");
        Config.setParameter("sso.mapping.ssoUserGroup.groups", this.employeeGroup.getUid());
        UserModel ssoUser = this.ssoUserService.getOrCreateSSOUser("ssoUserId", "ssoUserName", Sets.newHashSet((Object[])new String[] {"ssoUserGroup"}));
        Assertions.assertThat(ssoUser).isNotNull();
        Assertions.assertThat(ssoUser.getBackOfficeLoginDisabled()).isTrue();
    }


    @Test
    public void shouldDisallowAccessWhenPropertyMappingDisablesIt()
    {
        Config.setParameter("sso.database.usergroup.mapping", "false");
        createMappingInProperties("ssoUserGroup", "Employee", this.employeeGroup.getUid(), "false");
        UserModel ssoUser = this.ssoUserService.getOrCreateSSOUser("ssoUserId", "ssoUserName", Sets.newHashSet((Object[])new String[] {"ssoUserGroup"}));
        assertSsoUser(ssoUser, "ssoUserId", "ssoUserName", Sets.newHashSet((Object[])new UserGroupModel[] {this.employeeGroup}, ), Boolean.valueOf(true), "Employee");
    }


    @Test
    public void shouldEnableBackofficeLoginIfMappingInDatabaseEnablesIt()
    {
        Config.setParameter("sso.database.usergroup.mapping", "true");
        createMappingInDatabase("ssoUserGroup", this.employeeType, Sets.newHashSet((Object[])new UserGroupModel[] {this.employeeGroup}, ), true);
        UserModel ssoUser = this.ssoUserService.getOrCreateSSOUser("ssoUserId", "ssoUserName", Sets.newHashSet((Object[])new String[] {"ssoUserGroup"}));
        Assertions.assertThat(ssoUser).isNotNull();
        Assertions.assertThat(ssoUser.getBackOfficeLoginDisabled()).isFalse();
    }


    @Test
    public void shouldEnableBackofficeLoginIfAnyOfTheMappingsInDatabaseEnableIt()
    {
        Config.setParameter("sso.database.usergroup.mapping", "true");
        createMappingInDatabase("ssoUserGroup", this.employeeType, Sets.newHashSet((Object[])new UserGroupModel[] {this.employeeGroup}, ), true);
        createMappingInDatabase("ssoUserGroup2", this.employeeType, Sets.newHashSet((Object[])new UserGroupModel[] {this.employeeGroup}, ), false);
        UserModel ssoUser = this.ssoUserService.getOrCreateSSOUser("ssoUserId", "ssoUserName",
                        Sets.newHashSet((Object[])new String[] {"ssoUserGroup", "ssoUserGroup2"}));
        Assertions.assertThat(ssoUser).isNotNull();
        Assertions.assertThat(ssoUser.getBackOfficeLoginDisabled()).isFalse();
    }


    @Test
    public void shouldDisallowAccessWhenMappingInDatabaseDoesntEnableIt()
    {
        Config.setParameter("sso.database.usergroup.mapping", "true");
        createMappingInDatabase("ssoUserGroup", this.employeeType, Sets.newHashSet((Object[])new UserGroupModel[] {this.employeeGroup}, ), false);
        createMappingInDatabase("ssoUserGroup2", this.employeeType, Sets.newHashSet((Object[])new UserGroupModel[] {this.employeeGroup}, ), false);
        UserModel ssoUser = this.ssoUserService.getOrCreateSSOUser("ssoUserId", "ssoUserName", Sets.newHashSet((Object[])new String[] {"ssoUserGroup", "ssoUserGroup2"}));
        Assertions.assertThat(ssoUser).isNotNull();
        Assertions.assertThat(ssoUser.getBackOfficeLoginDisabled()).isTrue();
    }


    @Test
    public void shouldDisallowAccessWhenNoneOfTheMappingsInDatabaseEnableIt()
    {
        Config.setParameter("sso.database.usergroup.mapping", "true");
        createMappingInDatabase("ssoUserGroup", this.employeeType, Sets.newHashSet((Object[])new UserGroupModel[] {this.employeeGroup}, ), false);
        createMappingInDatabase("ssoUserGroup2", this.employeeType, Sets.newHashSet((Object[])new UserGroupModel[] {this.employeeGroup}, ), false);
        UserModel ssoUser = this.ssoUserService.getOrCreateSSOUser("ssoUserId", "ssoUserName", Sets.newHashSet((Object[])new String[] {"ssoUserGroup", "ssoUserGroup2"}));
        Assertions.assertThat(ssoUser).isNotNull();
        Assertions.assertThat(ssoUser.getBackOfficeLoginDisabled()).isTrue();
    }


    @Test
    public void shouldNotChangeBackofficeLoginDisabledIfUserHasItAlreadySet()
    {
        UserModel user = (UserModel)this.modelService.create("Employee");
        user.setUid("ssoUserId");
        user.setName("ssoUserName");
        user.setBackOfficeLoginDisabled(Boolean.valueOf(true));
        this.modelService.save(user);
        Config.setParameter("sso.database.usergroup.mapping", "false");
        createMappingInProperties("ssoUserGroup", "Employee", this.employeeGroup.getUid(), "true");
        UserModel ssoUser = this.ssoUserService.getOrCreateSSOUser("ssoUserId", "ssoUserName", Sets.newHashSet((Object[])new String[] {"ssoUserGroup"}));
        assertSsoUser(ssoUser, "ssoUserId", "ssoUserName", Sets.newHashSet((Object[])new UserGroupModel[] {this.employeeGroup}, ), Boolean.valueOf(true), "Employee");
    }


    @Test
    public void shouldChangeBackofficeLoginDisabledIfUserDoesntHaveItSet()
    {
        UserModel user = (UserModel)this.modelService.create("Employee");
        user.setUid("ssoUserId");
        user.setName("ssoUserName");
        user.setBackOfficeLoginDisabled(null);
        this.modelService.save(user);
        Config.setParameter("sso.database.usergroup.mapping", "false");
        createMappingInProperties("ssoUserGroup", "Employee", this.employeeGroup.getUid(), "true");
        UserModel ssoUser = this.ssoUserService.getOrCreateSSOUser("ssoUserId", "ssoUserName", Sets.newHashSet((Object[])new String[] {"ssoUserGroup"}));
        assertSsoUser(ssoUser, "ssoUserId", "ssoUserName", Sets.newHashSet((Object[])new UserGroupModel[] {this.employeeGroup}, ), Boolean.valueOf(false), "Employee");
    }


    private void createMappingInProperties(String ssoUserGroup, String userType, String groups, String enableBackofficeLogin)
    {
        Config.setParameter("sso.mapping." + ssoUserGroup + ".usertype", userType);
        Config.setParameter("sso.mapping." + ssoUserGroup + ".groups", groups);
        Config.setParameter("sso.mapping." + ssoUserGroup + ".enableBackofficeLogin", enableBackofficeLogin);
    }


    private void createMappingInDatabase(String ssoUserGroup, TypeModel userType, Set<UserGroupModel> userGroups, boolean enableBackofficeLogin)
    {
        SamlUserGroupModel samlUserGroupModel = new SamlUserGroupModel();
        samlUserGroupModel.setSamlUserGroup(ssoUserGroup);
        samlUserGroupModel.setUserType(userType);
        samlUserGroupModel.setUserGroups(userGroups);
        samlUserGroupModel.setEnableBackofficeLogin(Boolean.valueOf(enableBackofficeLogin));
        this.modelService.save(samlUserGroupModel);
    }


    private void assertSsoUser(UserModel ssoUser, String ssoUserId, String ssoUserName, Collection<UserGroupModel> userGroups, Boolean backofficeLoginDisabled, String typecode)
    {
        Assertions.assertThat(ssoUser).isNotNull();
        Assertions.assertThat(ssoUser.getUid()).isEqualTo(ssoUserId);
        Assertions.assertThat(ssoUser.getName()).isEqualTo(ssoUserName);
        Assertions.assertThat(ssoUser.getGroups()).containsOnlyElementsOf(userGroups);
        Assertions.assertThat(ssoUser.getBackOfficeLoginDisabled()).isEqualTo(backofficeLoginDisabled);
        Assertions.assertThat(ssoUser.getItemtype()).isEqualTo(typecode);
    }
}
