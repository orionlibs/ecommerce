package com.hybris.backoffice.user;

import com.google.common.collect.Sets;
import com.hybris.backoffice.model.user.BackofficeRoleModel;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultBackofficeRoleServiceTest
{
    private static final String SESSION_ATTRIBUTE_ACTIVE_AUTHORITY_GROUP_ID = "cockpitActiveAuthorityGroupId";
    @InjectMocks
    private final BackofficeRoleService testSubject = (BackofficeRoleService)new DefaultBackofficeRoleService();
    @Mock
    private PrincipalModel user;
    @Mock
    private SessionService sessionService;
    @Mock
    private UserService userService;


    @Test
    public void shouldTreatFullHierarchyOfActiveRoleAsRolePrincipalsHierarchy()
    {
        BackofficeRoleModel activeRole = (BackofficeRoleModel)Mockito.mock(BackofficeRoleModel.class);
        UserGroupModel activeRoleUserGroup = (UserGroupModel)Mockito.mock(UserGroupModel.class);
        BackofficeRoleModel inactiveRole = (BackofficeRoleModel)Mockito.mock(BackofficeRoleModel.class);
        UserGroupModel inactiveRoleUserGroup = (UserGroupModel)Mockito.mock(UserGroupModel.class);
        Map<PrincipalModel, Set<PrincipalGroupModel>> hierarchy = new HashMap<>();
        hierarchy.put(this.user, (Set)Collections.singleton(activeRole));
        hierarchy.put(activeRole, (Set)Collections.singleton(activeRoleUserGroup));
        hierarchy.put(activeRoleUserGroup, (Set)Collections.singleton(inactiveRole));
        hierarchy.put(inactiveRole, (Set)Collections.singleton(inactiveRoleUserGroup));
        mockHierarchy(hierarchy);
        String UID = "role";
        Mockito.when(this.sessionService.getAttribute("cockpitActiveAuthorityGroupId")).thenReturn("role");
        Mockito.when(this.userService.getUserGroupForUID("role")).thenReturn(activeRole);
        Collection<PrincipalGroupModel> activeRolePrincipalsHierarchy = this.testSubject.getActiveRolePrincipalsHierarchy();
        Assertions.assertThat(activeRolePrincipalsHierarchy).containsOnly((Object[])new PrincipalGroupModel[] {(PrincipalGroupModel)activeRole, (PrincipalGroupModel)activeRoleUserGroup, (PrincipalGroupModel)inactiveRole, (PrincipalGroupModel)inactiveRoleUserGroup});
    }


    @Test
    public void shouldTreatEmptyCollectionAsRolePrincipalsHierarchyWhenNoActiveRole()
    {
        BackofficeRoleModel inactiveRole = (BackofficeRoleModel)Mockito.mock(BackofficeRoleModel.class);
        UserGroupModel inactiveRoleUserGroup = (UserGroupModel)Mockito.mock(UserGroupModel.class);
        UserGroupModel userGroup = (UserGroupModel)Mockito.mock(UserGroupModel.class);
        Map<PrincipalModel, Set<PrincipalGroupModel>> hierarchy = new HashMap<>();
        hierarchy.put(this.user, Sets.newHashSet((Object[])new PrincipalGroupModel[] {(PrincipalGroupModel)userGroup, (PrincipalGroupModel)inactiveRole}));
        hierarchy.put(inactiveRole, (Set)Collections.singleton(inactiveRoleUserGroup));
        mockHierarchy(hierarchy);
        Collection<PrincipalGroupModel> activeRolePrincipalsHierarchy = this.testSubject.getActiveRolePrincipalsHierarchy();
        Assertions.assertThat(activeRolePrincipalsHierarchy).isEmpty();
    }


    @Test
    public void shouldIgnoreRolesWithTheirHierarchyInNonRoleHierarchy()
    {
        BackofficeRoleModel inactiveRole = (BackofficeRoleModel)Mockito.mock(BackofficeRoleModel.class);
        UserGroupModel inactiveRoleUserGroup = (UserGroupModel)Mockito.mock(UserGroupModel.class);
        UserGroupModel userGroup = (UserGroupModel)Mockito.mock(UserGroupModel.class);
        Map<PrincipalModel, Set<PrincipalGroupModel>> hierarchy = new HashMap<>();
        hierarchy.put(this.user, Sets.newHashSet((Object[])new PrincipalGroupModel[] {(PrincipalGroupModel)userGroup, (PrincipalGroupModel)inactiveRole}));
        hierarchy.put(inactiveRole, (Set)Collections.singleton(inactiveRoleUserGroup));
        mockHierarchy(hierarchy);
        Collection<PrincipalModel> nonRolePrincipalsHierarchy = this.testSubject.getNonRolePrincipalsHierarchy(this.user);
        Assertions.assertThat(nonRolePrincipalsHierarchy).containsOnly((Object[])new PrincipalModel[] {this.user, (PrincipalModel)userGroup});
    }


    @Test
    public void shouldFilterOutRoles()
    {
        BackofficeRoleModel role1 = (BackofficeRoleModel)Mockito.mock(BackofficeRoleModel.class);
        BackofficeRoleModel role2 = (BackofficeRoleModel)Mockito.mock(BackofficeRoleModel.class);
        UserGroupModel group1 = (UserGroupModel)Mockito.mock(UserGroupModel.class);
        UserGroupModel group2 = (UserGroupModel)Mockito.mock(UserGroupModel.class);
        Collection<PrincipalGroupModel> result = this.testSubject.filterOutRolePrincipals(Sets.newHashSet((Object[])new PrincipalGroupModel[] {(PrincipalGroupModel)role1, (PrincipalGroupModel)group1, (PrincipalGroupModel)role2, (PrincipalGroupModel)group2}));
        Assertions.assertThat(result).containsOnly((Object[])new PrincipalGroupModel[] {(PrincipalGroupModel)group1, (PrincipalGroupModel)group2});
    }


    protected void mockHierarchy(Map<PrincipalModel, Set<PrincipalGroupModel>> hierarchy)
    {
        mockHierarchy(this.user, hierarchy);
    }


    protected void mockHierarchy(PrincipalModel root, Map<PrincipalModel, Set<PrincipalGroupModel>> hierarchy)
    {
        Mockito.when(root.getGroups()).thenReturn(hierarchy.get(root));
        Mockito.when(root.getAllGroups()).thenReturn(hierarchy.values().stream().flatMap(Collection::stream).collect(Collectors.toSet()));
        Map<PrincipalModel, Set<PrincipalGroupModel>> subHierarchy = new HashMap<>(hierarchy);
        Set<PrincipalGroupModel> groups = subHierarchy.remove(root);
        if(CollectionUtils.isNotEmpty(groups))
        {
            groups.forEach(group -> mockHierarchy((PrincipalModel)group, subHierarchy));
        }
    }
}
