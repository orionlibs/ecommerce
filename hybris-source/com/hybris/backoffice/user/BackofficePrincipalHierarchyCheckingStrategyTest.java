package com.hybris.backoffice.user;

import com.google.common.collect.Sets;
import com.hybris.backoffice.model.user.BackofficeRoleModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.security.permissions.PermissionCheckValue;
import de.hybris.platform.servicelayer.security.permissions.PermissionChecker;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BackofficePrincipalHierarchyCheckingStrategyTest
{
    @InjectMocks
    private BackofficePrincipalHierarchyCheckingStrategy testSubject;
    @Mock
    private UserService userService;
    @Mock
    private BackofficeRoleService backofficeRoleService;
    @Mock
    private BackofficeUserService backofficeUserService;
    @Mock
    private PermissionChecker permissionChecker;
    @Mock
    private UserModel user;
    private static final String PERMISSION_NAME = "permission";
    private static final String ROLE_UID = "role";


    @Test
    public void shouldConflictOnDirectGroupAndIndirectActiveRole()
    {
        UserGroupModel directGroup1 = (UserGroupModel)Mockito.mock(UserGroupModel.class);
        UserGroupModel directGroup2 = (UserGroupModel)Mockito.mock(UserGroupModel.class);
        BackofficeRoleModel indirectRole = (BackofficeRoleModel)Mockito.mock(BackofficeRoleModel.class);
        Set<PrincipalGroupModel> directGroups = Sets.newHashSet((Object[])new PrincipalGroupModel[] {(PrincipalGroupModel)directGroup1, (PrincipalGroupModel)directGroup2});
        Mockito.when(indirectRole.getUid()).thenReturn("role");
        Mockito.when(this.user.getGroups()).thenReturn(directGroups);
        Mockito.lenient().when(this.user.getAllGroups()).thenReturn(Sets.newHashSet((Object[])new PrincipalGroupModel[] {(PrincipalGroupModel)directGroup1, (PrincipalGroupModel)directGroup2, (PrincipalGroupModel)indirectRole}));
        Mockito.when(directGroup2.getGroups()).thenReturn(Collections.singleton(indirectRole));
        Mockito.lenient().when(directGroup2.getAllGroups()).thenReturn(Collections.singleton(indirectRole));
        Mockito.when(Boolean.valueOf(this.backofficeRoleService.shouldTreatRolesAsGroups())).thenReturn(Boolean.valueOf(false));
        Mockito.lenient().when(Boolean.valueOf(this.backofficeRoleService.isActiveRole(indirectRole.getUid()))).thenReturn(Boolean.valueOf(true));
        Mockito.when(this.backofficeRoleService.filterOutRolePrincipals(directGroups)).thenReturn(directGroups);
        Mockito.when(this.backofficeRoleService.getActiveRoleModel()).thenReturn(Optional.of(indirectRole));
        Mockito.when(Boolean.valueOf(this.backofficeUserService.isCurrentUser(this.user))).thenReturn(Boolean.valueOf(true));
        Mockito.when(Boolean.valueOf(this.userService.isAdmin(this.user))).thenReturn(Boolean.valueOf(false));
        Mockito.when(this.permissionChecker.checkPermission((PrincipalModel)this.user, "permission")).thenReturn(PermissionCheckValue.NOT_DEFINED);
        Mockito.when(this.permissionChecker.checkPermission((PrincipalModel)directGroup1, "permission")).thenReturn(PermissionCheckValue.DENIED);
        Mockito.when(this.permissionChecker.checkPermission((PrincipalModel)directGroup2, "permission")).thenReturn(PermissionCheckValue.NOT_DEFINED);
        Mockito.when(this.permissionChecker.checkPermission((PrincipalModel)indirectRole, "permission")).thenReturn(PermissionCheckValue.ALLOWED);
        PermissionCheckValue result = this.testSubject.checkPermissionsForPrincipalHierarchy(this.permissionChecker, (PrincipalModel)this.user, "permission");
        Assertions.assertThat((Comparable)result).isEqualTo(PermissionCheckValue.CONFLICTING);
    }


    @Test
    public void shouldIgnoreInactiveRole()
    {
        UserGroupModel directGroup1 = (UserGroupModel)Mockito.mock(UserGroupModel.class);
        UserGroupModel directGroup2 = (UserGroupModel)Mockito.mock(UserGroupModel.class);
        BackofficeRoleModel directRole = (BackofficeRoleModel)Mockito.mock(BackofficeRoleModel.class);
        Set<PrincipalGroupModel> directGroups = Sets.newHashSet((Object[])new PrincipalGroupModel[] {(PrincipalGroupModel)directGroup1, (PrincipalGroupModel)directGroup2, (PrincipalGroupModel)directRole});
        Mockito.when(directRole.getUid()).thenReturn("role");
        Mockito.when(this.user.getGroups()).thenReturn(directGroups);
        Mockito.lenient().when(this.user.getAllGroups()).thenReturn(directGroups);
        Mockito.when(Boolean.valueOf(this.backofficeRoleService.shouldTreatRolesAsGroups())).thenReturn(Boolean.valueOf(false));
        Mockito.lenient().when(Boolean.valueOf(this.backofficeRoleService.isActiveRole(directRole.getUid()))).thenReturn(Boolean.valueOf(false));
        Mockito.when(this.backofficeRoleService.filterOutRolePrincipals(directGroups)).thenReturn(Sets.newHashSet((Object[])new PrincipalGroupModel[] {(PrincipalGroupModel)directGroup1, (PrincipalGroupModel)directGroup2}));
        Mockito.when(this.backofficeRoleService.getActiveRoleModel()).thenReturn(Optional.empty());
        Mockito.when(Boolean.valueOf(this.backofficeUserService.isCurrentUser(this.user))).thenReturn(Boolean.valueOf(true));
        Mockito.when(Boolean.valueOf(this.userService.isAdmin(this.user))).thenReturn(Boolean.valueOf(false));
        Mockito.when(this.permissionChecker.checkPermission((PrincipalModel)this.user, "permission")).thenReturn(PermissionCheckValue.NOT_DEFINED);
        Mockito.when(this.permissionChecker.checkPermission((PrincipalModel)directGroup1, "permission")).thenReturn(PermissionCheckValue.DENIED);
        Mockito.when(this.permissionChecker.checkPermission((PrincipalModel)directGroup2, "permission")).thenReturn(PermissionCheckValue.NOT_DEFINED);
        Mockito.lenient().when(this.permissionChecker.checkPermission((PrincipalModel)directRole, "permission")).thenReturn(PermissionCheckValue.ALLOWED);
        PermissionCheckValue result = this.testSubject.checkPermissionsForPrincipalHierarchy(this.permissionChecker, (PrincipalModel)this.user, "permission");
        Assertions.assertThat((Comparable)result).isEqualTo(PermissionCheckValue.DENIED);
    }


    @Test
    public void shouldConflictOnActiveRoleHierarchy()
    {
        UserGroupModel indirectGroup = (UserGroupModel)Mockito.mock(UserGroupModel.class);
        BackofficeRoleModel directRole = (BackofficeRoleModel)Mockito.mock(BackofficeRoleModel.class);
        BackofficeRoleModel indirectRole = (BackofficeRoleModel)Mockito.mock(BackofficeRoleModel.class);
        Set<PrincipalGroupModel> directGroups = (Set)Collections.singleton(directRole);
        Mockito.when(directRole.getUid()).thenReturn("role");
        Mockito.when(this.user.getGroups()).thenReturn(directGroups);
        Mockito.lenient().when(this.user.getAllGroups()).thenReturn(Sets.newHashSet((Object[])new PrincipalGroupModel[] {(PrincipalGroupModel)indirectGroup, (PrincipalGroupModel)directRole, (PrincipalGroupModel)indirectRole}));
        Mockito.when(directRole.getGroups()).thenReturn(Sets.newHashSet((Object[])new PrincipalGroupModel[] {(PrincipalGroupModel)indirectGroup, (PrincipalGroupModel)indirectRole}));
        Mockito.lenient().when(directRole.getAllGroups()).thenReturn(Sets.newHashSet((Object[])new PrincipalGroupModel[] {(PrincipalGroupModel)indirectGroup, (PrincipalGroupModel)indirectRole}));
        Mockito.when(Boolean.valueOf(this.backofficeRoleService.shouldTreatRolesAsGroups())).thenReturn(Boolean.valueOf(false));
        Mockito.when(Boolean.valueOf(this.backofficeRoleService.isActiveRole(directRole.getUid()))).thenReturn(Boolean.valueOf(true));
        Mockito.when(this.backofficeRoleService.filterOutRolePrincipals(directGroups)).thenReturn(new HashSet());
        Mockito.when(this.backofficeRoleService.getActiveRoleModel()).thenReturn(Optional.of(directRole));
        Mockito.when(Boolean.valueOf(this.backofficeUserService.isCurrentUser(this.user))).thenReturn(Boolean.valueOf(true));
        Mockito.when(Boolean.valueOf(this.userService.isAdmin(this.user))).thenReturn(Boolean.valueOf(false));
        Mockito.when(this.permissionChecker.checkPermission((PrincipalModel)this.user, "permission")).thenReturn(PermissionCheckValue.NOT_DEFINED);
        Mockito.when(this.permissionChecker.checkPermission((PrincipalModel)directRole, "permission")).thenReturn(PermissionCheckValue.NOT_DEFINED);
        Mockito.when(this.permissionChecker.checkPermission((PrincipalModel)indirectGroup, "permission")).thenReturn(PermissionCheckValue.DENIED);
        Mockito.when(this.permissionChecker.checkPermission((PrincipalModel)indirectRole, "permission")).thenReturn(PermissionCheckValue.ALLOWED);
        PermissionCheckValue result = this.testSubject.checkPermissionsForPrincipalHierarchy(this.permissionChecker, (PrincipalModel)this.user, "permission");
        Assertions.assertThat((Comparable)result).isEqualTo(PermissionCheckValue.CONFLICTING);
    }


    @Test
    public void shouldConflictOnInactiveRoleInLegacyMode()
    {
        UserGroupModel directGroup1 = (UserGroupModel)Mockito.mock(UserGroupModel.class);
        UserGroupModel directGroup2 = (UserGroupModel)Mockito.mock(UserGroupModel.class);
        BackofficeRoleModel directRole = (BackofficeRoleModel)Mockito.mock(BackofficeRoleModel.class);
        Set<PrincipalGroupModel> directGroups = Sets.newHashSet((Object[])new PrincipalGroupModel[] {(PrincipalGroupModel)directGroup1, (PrincipalGroupModel)directGroup2, (PrincipalGroupModel)directRole});
        Mockito.lenient().when(directRole.getUid()).thenReturn("role");
        Mockito.when(this.user.getGroups()).thenReturn(directGroups);
        Mockito.lenient().when(this.user.getAllGroups()).thenReturn(directGroups);
        Mockito.when(Boolean.valueOf(this.backofficeRoleService.shouldTreatRolesAsGroups())).thenReturn(Boolean.valueOf(true));
        Mockito.lenient().when(Boolean.valueOf(this.backofficeUserService.isCurrentUser(this.user))).thenReturn(Boolean.valueOf(true));
        Mockito.when(Boolean.valueOf(this.userService.isAdmin(this.user))).thenReturn(Boolean.valueOf(false));
        Mockito.when(this.permissionChecker.checkPermission((PrincipalModel)this.user, "permission")).thenReturn(PermissionCheckValue.NOT_DEFINED);
        Mockito.when(this.permissionChecker.checkPermission((PrincipalModel)directGroup1, "permission")).thenReturn(PermissionCheckValue.DENIED);
        Mockito.when(this.permissionChecker.checkPermission((PrincipalModel)directGroup2, "permission")).thenReturn(PermissionCheckValue.NOT_DEFINED);
        Mockito.when(this.permissionChecker.checkPermission((PrincipalModel)directRole, "permission")).thenReturn(PermissionCheckValue.ALLOWED);
        PermissionCheckValue result = this.testSubject.checkPermissionsForPrincipalHierarchy(this.permissionChecker, (PrincipalModel)this.user, "permission");
        Assertions.assertThat((Comparable)result).isEqualTo(PermissionCheckValue.CONFLICTING);
    }


    @Test
    public void shouldIgnoreAllRolesIfUserIsNotCurrentLoginUser()
    {
        UserGroupModel directGroup1 = (UserGroupModel)Mockito.mock(UserGroupModel.class);
        BackofficeRoleModel directRole1 = (BackofficeRoleModel)Mockito.mock(BackofficeRoleModel.class);
        BackofficeRoleModel directRole2 = (BackofficeRoleModel)Mockito.mock(BackofficeRoleModel.class);
        Set<PrincipalGroupModel> directGroups = Sets.newHashSet((Object[])new PrincipalGroupModel[] {(PrincipalGroupModel)directGroup1, (PrincipalGroupModel)directRole1, (PrincipalGroupModel)directRole2});
        Mockito.when(directRole1.getUid()).thenReturn("role1");
        Mockito.when(directRole2.getUid()).thenReturn("role2");
        Mockito.when(this.user.getGroups()).thenReturn(directGroups);
        Mockito.when(Boolean.valueOf(this.backofficeUserService.isCurrentUser(this.user))).thenReturn(Boolean.valueOf(false));
        Mockito.lenient().when(Boolean.valueOf(this.backofficeRoleService.isActiveRole(directRole1.getUid()))).thenReturn(Boolean.valueOf(false));
        Mockito.lenient().when(Boolean.valueOf(this.backofficeRoleService.isActiveRole(directRole2.getUid()))).thenReturn(Boolean.valueOf(true));
        Mockito.when(Boolean.valueOf(this.backofficeRoleService.shouldTreatRolesAsGroups())).thenReturn(Boolean.valueOf(false));
        Mockito.when(this.backofficeRoleService.filterOutRolePrincipals(directGroups)).thenReturn(Sets.newHashSet((Object[])new PrincipalGroupModel[] {(PrincipalGroupModel)directGroup1}));
        Mockito.when(Boolean.valueOf(this.userService.isAdmin(this.user))).thenReturn(Boolean.valueOf(false));
        Mockito.when(this.permissionChecker.checkPermission((PrincipalModel)this.user, "permission")).thenReturn(PermissionCheckValue.NOT_DEFINED);
        Mockito.when(this.permissionChecker.checkPermission((PrincipalModel)directGroup1, "permission")).thenReturn(PermissionCheckValue.DENIED);
        Mockito.lenient().when(this.permissionChecker.checkPermission((PrincipalModel)directRole1, "permission")).thenReturn(PermissionCheckValue.ALLOWED);
        Mockito.lenient().when(this.permissionChecker.checkPermission((PrincipalModel)directRole2, "permission")).thenReturn(PermissionCheckValue.ALLOWED);
        PermissionCheckValue result = this.testSubject.checkPermissionsForPrincipalHierarchy(this.permissionChecker, (PrincipalModel)this.user, "permission");
        Assertions.assertThat((Comparable)result).isEqualTo(PermissionCheckValue.DENIED);
    }
}
