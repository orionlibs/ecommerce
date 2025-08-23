package com.hybris.backoffice.daos.impl;

import com.google.common.collect.Sets;
import com.hybris.backoffice.model.user.BackofficeRoleModel;
import com.hybris.backoffice.user.BackofficeRoleService;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class BackofficeCatalogVersionDaoTest
{
    @InjectMocks
    private BackofficeCatalogVersionDao testSubject;
    @Mock
    private FlexibleSearchService flexibleSearchService;
    @Mock
    private BackofficeRoleService backofficeRoleService;
    @Mock
    private PrincipalModel user;
    @Captor
    private ArgumentCaptor<Map<String, Object>> queryParamsCaptor;


    @Test
    public void shouldIncludeOnlyDirectGroupsWhenThereIsNoActiveRole()
    {
        UserGroupModel groupModel = (UserGroupModel)Mockito.mock(UserGroupModel.class);
        BackofficeRoleModel inactiveRole = (BackofficeRoleModel)Mockito.mock(BackofficeRoleModel.class);
        UserGroupModel inactiveRoleGroupModel = (UserGroupModel)Mockito.mock(UserGroupModel.class);
        Mockito.when(this.user.getAllGroups()).thenReturn(Sets.newHashSet((Object[])new PrincipalGroupModel[] {(PrincipalGroupModel)groupModel, (PrincipalGroupModel)inactiveRole, (PrincipalGroupModel)inactiveRoleGroupModel}));
        Mockito.when(Boolean.valueOf(this.backofficeRoleService.shouldTreatRolesAsGroups())).thenReturn(Boolean.valueOf(false));
        Mockito.when(this.backofficeRoleService.getActiveRoleModel()).thenReturn(Optional.empty());
        Mockito.when(this.backofficeRoleService.getNonRolePrincipalsHierarchy(this.user)).thenReturn(Sets.newHashSet((Object[])new PrincipalModel[] {this.user, (PrincipalModel)groupModel}));
        Mockito.when(this.flexibleSearchService.search((String)Matchers.any(), (Map)Matchers.any())).thenReturn(new SearchResultImpl(new ArrayList(), 0, 0, 0));
        this.testSubject.findReadableCatalogVersions(this.user);
        ((FlexibleSearchService)Mockito.verify(this.flexibleSearchService)).search((String)Matchers.any(), (Map)this.queryParamsCaptor.capture());
        Object principalsQueryParam = ((Map)this.queryParamsCaptor.getValue()).get("principals");
        Assertions.assertThat(principalsQueryParam).isInstanceOf(Collection.class);
        Assertions.assertThat((Collection)principalsQueryParam).containsOnly(new Object[] {this.user, groupModel});
    }


    @Test
    public void shouldIncludeDirectGroupsAndActiveRole()
    {
        UserGroupModel groupModel = (UserGroupModel)Mockito.mock(UserGroupModel.class);
        BackofficeRoleModel activeRole = (BackofficeRoleModel)Mockito.mock(BackofficeRoleModel.class);
        UserGroupModel activeRoleGroupModel = (UserGroupModel)Mockito.mock(UserGroupModel.class);
        BackofficeRoleModel inactiveRole = (BackofficeRoleModel)Mockito.mock(BackofficeRoleModel.class);
        UserGroupModel inactiveRoleGroupModel = (UserGroupModel)Mockito.mock(UserGroupModel.class);
        Mockito.when(this.user.getAllGroups())
                        .thenReturn(Sets.newHashSet((Object[])new PrincipalGroupModel[] {(PrincipalGroupModel)groupModel, (PrincipalGroupModel)activeRole, (PrincipalGroupModel)activeRoleGroupModel, (PrincipalGroupModel)inactiveRole, (PrincipalGroupModel)inactiveRoleGroupModel}));
        Mockito.when(activeRole.getAllGroups()).thenReturn(Collections.singleton(activeRoleGroupModel));
        Mockito.when(Boolean.valueOf(this.backofficeRoleService.shouldTreatRolesAsGroups())).thenReturn(Boolean.valueOf(false));
        Mockito.when(this.backofficeRoleService.getActiveRoleModel()).thenReturn(Optional.of(activeRole));
        Mockito.when(this.backofficeRoleService.getNonRolePrincipalsHierarchy(this.user)).thenReturn(Sets.newHashSet((Object[])new PrincipalModel[] {this.user, (PrincipalModel)groupModel}));
        Mockito.when(this.flexibleSearchService.search((String)Matchers.any(), (Map)Matchers.any())).thenReturn(new SearchResultImpl(new ArrayList(), 0, 0, 0));
        this.testSubject.findReadableCatalogVersions(this.user);
        ((FlexibleSearchService)Mockito.verify(this.flexibleSearchService)).search((String)Matchers.any(), (Map)this.queryParamsCaptor.capture());
        Object principalsQueryParam = ((Map)this.queryParamsCaptor.getValue()).get("principals");
        Assertions.assertThat(principalsQueryParam).isInstanceOf(Collection.class);
        Assertions.assertThat((Collection)principalsQueryParam).containsOnly(new Object[] {this.user, groupModel, activeRole, activeRoleGroupModel});
    }


    @Test
    public void shouldConsiderEverythingInLegacyMode()
    {
        UserGroupModel activeRoleGroup = (UserGroupModel)Mockito.mock(UserGroupModel.class);
        BackofficeRoleModel activeRole = (BackofficeRoleModel)Mockito.mock(BackofficeRoleModel.class);
        UserGroupModel userGroup = (UserGroupModel)Mockito.mock(UserGroupModel.class);
        UserGroupModel inactiveRoleGroup = (UserGroupModel)Mockito.mock(UserGroupModel.class);
        UserGroupModel inactiveRoleSuperGroup = (UserGroupModel)Mockito.mock(UserGroupModel.class);
        BackofficeRoleModel inactiveRole = (BackofficeRoleModel)Mockito.mock(BackofficeRoleModel.class);
        Mockito.when(Boolean.valueOf(this.backofficeRoleService.shouldTreatRolesAsGroups())).thenReturn(Boolean.valueOf(true));
        Mockito.when(this.user.getAllGroups()).thenReturn(
                        Sets.newHashSet((Object[])new PrincipalGroupModel[] {(PrincipalGroupModel)userGroup, (PrincipalGroupModel)inactiveRole, (PrincipalGroupModel)activeRole, (PrincipalGroupModel)activeRoleGroup, (PrincipalGroupModel)inactiveRoleGroup,
                                        (PrincipalGroupModel)inactiveRoleSuperGroup}));
        Mockito.when(this.flexibleSearchService.search((String)Matchers.any(), (Map)Matchers.any())).thenReturn(new SearchResultImpl(new ArrayList(), 0, 0, 0));
        this.testSubject.findReadableCatalogVersions(this.user);
        ((FlexibleSearchService)Mockito.verify(this.flexibleSearchService)).search((String)Matchers.any(), (Map)this.queryParamsCaptor.capture());
        Object principalsQueryParam = ((Map)this.queryParamsCaptor.getValue()).get("principals");
        Assertions.assertThat(principalsQueryParam).isInstanceOf(Collection.class);
        Assertions.assertThat((Collection)principalsQueryParam).containsOnly(new Object[] {this.user, userGroup, activeRole, activeRoleGroup, inactiveRoleGroup, inactiveRole, inactiveRoleSuperGroup});
    }
}
