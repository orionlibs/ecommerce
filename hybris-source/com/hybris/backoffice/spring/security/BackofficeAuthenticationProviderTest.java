package com.hybris.backoffice.spring.security;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.user.impl.DefaultUserService;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@UnitTest
public class BackofficeAuthenticationProviderTest
{
    private BackofficeAuthenticationProvider backofficeAuthenticationProvider;
    @Mock
    private DefaultUserService userService;
    @Mock
    private Authentication mockedAuthentication;


    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        this.backofficeAuthenticationProvider = (BackofficeAuthenticationProvider)new Object(this);
        this.backofficeAuthenticationProvider.setUserService((UserService)this.userService);
    }


    @Test
    public void testAuthenticate()
    {
        Mockito.when(this.userService.getUserForUID("employee")).thenReturn(new EmployeeModel());
        Mockito.when(this.userService.getUserForUID("customer")).thenReturn(new CustomerModel());
        Mockito.when(this.userService.getUserForUID(Mockito.anyString(), (Class)Mockito.any()))
                        .thenCallRealMethod();
        Authentication employeeAuth = (Authentication)Mockito.mock(Authentication.class);
        Mockito.when(employeeAuth.getName()).thenReturn("employee");
        Authentication customerAuth = (Authentication)Mockito.mock(Authentication.class);
        Mockito.when(customerAuth.getName()).thenReturn("customer");
        boolean exceptionCaught = false;
        try
        {
            this.backofficeAuthenticationProvider.authenticate(customerAuth);
        }
        catch(AuthenticationException e)
        {
            exceptionCaught = true;
        }
        Assert.assertTrue(exceptionCaught);
    }


    @Test
    public void testEmployeeLoginDisabledExplicity()
    {
        EmployeeModel employee = (EmployeeModel)Mockito.mock(EmployeeModel.class);
        Mockito.when(this.userService.getUserForUID("employee")).thenReturn(employee);
        Mockito.when(this.userService.getUserForUID(Mockito.anyString(), (Class)Mockito.any()))
                        .thenCallRealMethod();
        Authentication employeeAuth = (Authentication)Mockito.mock(Authentication.class);
        Mockito.when(employeeAuth.getName()).thenReturn("employee");
        Mockito.when(employee.getBackOfficeLoginDisabled()).thenReturn(Boolean.FALSE);
        Authentication authenticate = this.backofficeAuthenticationProvider.authenticate(employeeAuth);
        Assert.assertNotNull(authenticate);
        Mockito.when(employee.getBackOfficeLoginDisabled()).thenReturn(Boolean.TRUE);
        boolean exceptionCaught = false;
        try
        {
            this.backofficeAuthenticationProvider.authenticate(employeeAuth);
        }
        catch(AuthenticationException e)
        {
            exceptionCaught = true;
        }
        Assert.assertTrue(exceptionCaught);
    }


    @Test
    public void testNewEmployeeDisabled()
    {
        Mockito.when(this.userService.getUserForUID("newone")).thenReturn(new EmployeeModel());
        Mockito.when(this.userService.getUserForUID(Mockito.anyString(), (Class)Mockito.any()))
                        .thenCallRealMethod();
        Authentication employeeAuth = (Authentication)Mockito.mock(Authentication.class);
        Mockito.when(employeeAuth.getName()).thenReturn("newone");
        boolean exceptionCaught = false;
        try
        {
            this.backofficeAuthenticationProvider.authenticate(employeeAuth);
        }
        catch(AuthenticationException e)
        {
            exceptionCaught = true;
        }
        Assert.assertTrue(exceptionCaught);
    }


    @Test
    public void testLoginDisabledAdmin()
    {
        String testLoginDisabledAdmin = "testLoginDisabledAdmin";
        EmployeeModel admin = createEmployee("testLoginDisabledAdmin");
        assignToAdminGroup(admin);
        Mockito.when(admin.getBackOfficeLoginDisabled()).thenReturn(Boolean.TRUE);
        Authentication authentication = getAuthentication("testLoginDisabledAdmin");
        Assert.assertNotNull(this.backofficeAuthenticationProvider.authenticate(authentication));
    }


    @Test
    public void testAdminLoginDisabledOnDirectGroup()
    {
        String testAdminLoginDisabledOnDirectGroup = "testAdminLoginDisabledOnDirectGroup";
        EmployeeModel admin = createEmployee("testAdminLoginDisabledOnDirectGroup");
        assignToAdminGroup(admin);
        createDirectDisabledGroup(admin);
        Authentication authentication = getAuthentication("testAdminLoginDisabledOnDirectGroup");
        Assert.assertNotNull(this.backofficeAuthenticationProvider.authenticate(authentication));
    }


    @Test
    public void testAdminLoginDisabledOnParentGroup()
    {
        String testAdminLoginDisabledOnParentGroup = "testAdminLoginDisabledOnParentGroup";
        EmployeeModel admin = createEmployee("testAdminLoginDisabledOnParentGroup");
        assignToAdminGroup(admin);
        UserGroupModel directGroup = createDirectGroup(admin);
        createParentDisabledGroup(directGroup);
        Authentication authentication = getAuthentication("testAdminLoginDisabledOnParentGroup");
        Assert.assertNotNull(this.backofficeAuthenticationProvider.authenticate(authentication));
    }


    @Test
    public void testEmployeeLoginActiveOnDirectGroup()
    {
        String employeeLoginActiveOnDirectGroup = "employeeLoginActiveOnDirectGroup";
        EmployeeModel employee = createEmployee("employeeLoginActiveOnDirectGroup");
        createDirectActiveGroup(employee);
        Authentication authentication = getAuthentication("employeeLoginActiveOnDirectGroup");
        Assert.assertNotNull(this.backofficeAuthenticationProvider.authenticate(authentication));
    }


    @Test
    public void testEmployeeLoginActiveOnParentGroup()
    {
        String employeeLoginActiveOnParentGroup = "employeeLoginActiveOnParentGroup";
        EmployeeModel employee = createEmployee("employeeLoginActiveOnParentGroup");
        UserGroupModel directGroup = createDirectGroup(employee);
        createParentActiveGroup(directGroup);
        Authentication authentication = getAuthentication("employeeLoginActiveOnParentGroup");
        Assert.assertNotNull(this.backofficeAuthenticationProvider.authenticate(authentication));
    }


    @Test
    public void testEmployeeLoginActiveOnDisabledGroup()
    {
        String employeeLoginActiveOnDisabledGroup = "employeeLoginActiveOnDisabledGroup";
        EmployeeModel employee = createEmployee("employeeLoginActiveOnDisabledGroup");
        createDirectDisabledGroup(employee);
        Authentication authentication = getAuthentication("employeeLoginActiveOnDisabledGroup");
        boolean exceptionCaught = false;
        try
        {
            this.backofficeAuthenticationProvider.authenticate(authentication);
        }
        catch(AuthenticationException e)
        {
            exceptionCaught = true;
        }
        Assert.assertTrue(exceptionCaught);
    }


    @Test
    public void testEmployeeLoginDisabledOnParentDisabledGroup()
    {
        String employeeLoginDisabledOnParentGroup = "employeeLoginDisabledOnParentGroup";
        EmployeeModel employee = createEmployee("employeeLoginDisabledOnParentGroup");
        UserGroupModel directGroup = createDirectGroup(employee);
        createParentDisabledGroup(directGroup);
        Authentication authentication = getAuthentication("employeeLoginDisabledOnParentGroup");
        boolean exceptionCaught = false;
        try
        {
            this.backofficeAuthenticationProvider.authenticate(authentication);
        }
        catch(AuthenticationException e)
        {
            exceptionCaught = true;
        }
        Assert.assertTrue(exceptionCaught);
    }


    @Test
    public void testEmployeeLoginDisabledOnNotSetGroups()
    {
        String employeeLoginDisabledOnParentGroup = "employeeLoginDisabledOnParentGroup";
        EmployeeModel employee = createEmployee("employeeLoginDisabledOnParentGroup");
        UserGroupModel directGroup = createDirectGroup(employee);
        createParentGroup(directGroup);
        Authentication authentication = getAuthentication("employeeLoginDisabledOnParentGroup");
        boolean exceptionCaught = false;
        try
        {
            this.backofficeAuthenticationProvider.authenticate(authentication);
        }
        catch(AuthenticationException e)
        {
            exceptionCaught = true;
        }
        Assert.assertTrue(exceptionCaught);
    }


    private void assignToAdminGroup(EmployeeModel employee)
    {
        Mockito.when(Boolean.valueOf(this.userService.isAdmin((UserModel)employee))).thenReturn(Boolean.TRUE);
    }


    private void createDirectDisabledGroup(EmployeeModel employee)
    {
        UserGroupModel directDisabledGroup = createDirectGroup(employee);
        Mockito.when(directDisabledGroup.getBackOfficeLoginDisabled()).thenReturn(Boolean.TRUE);
    }


    private void createDirectActiveGroup(EmployeeModel employee)
    {
        UserGroupModel directActiveGroup = createDirectGroup(employee);
        Mockito.when(directActiveGroup.getBackOfficeLoginDisabled()).thenReturn(Boolean.FALSE);
    }


    private UserGroupModel createDirectGroup(EmployeeModel employee)
    {
        Set<PrincipalGroupModel> list = new HashSet<>();
        UserGroupModel group = (UserGroupModel)Mockito.mock(UserGroupModel.class);
        Mockito.when(group.getBackOfficeLoginDisabled()).thenReturn(null);
        group.setUid("directtestgroup");
        list.add(group);
        Mockito.when(employee.getGroups()).thenReturn(list);
        return group;
    }


    private EmployeeModel createEmployee(String employeeLoginActiveOnDirectGroup)
    {
        EmployeeModel employee = (EmployeeModel)Mockito.mock(EmployeeModel.class);
        employee.setUid(employeeLoginActiveOnDirectGroup);
        employee.setName(employeeLoginActiveOnDirectGroup);
        Mockito.when(employee.getBackOfficeLoginDisabled()).thenReturn(null);
        Mockito.when(this.userService.getUserForUID(employeeLoginActiveOnDirectGroup, EmployeeModel.class)).thenReturn(employee);
        return employee;
    }


    private void createParentDisabledGroup(UserGroupModel childGroup)
    {
        UserGroupModel parentGroup = createParentGroup(childGroup);
        Mockito.when(parentGroup.getBackOfficeLoginDisabled()).thenReturn(Boolean.TRUE);
    }


    private void createParentActiveGroup(UserGroupModel childGroup)
    {
        UserGroupModel parentGroup = createParentGroup(childGroup);
        Mockito.when(parentGroup.getBackOfficeLoginDisabled()).thenReturn(Boolean.FALSE);
    }


    private UserGroupModel createParentGroup(UserGroupModel childGroup)
    {
        UserGroupModel parentGroup = (UserGroupModel)Mockito.mock(UserGroupModel.class);
        Mockito.when(parentGroup.getBackOfficeLoginDisabled()).thenReturn(null);
        parentGroup.setUid("parentestgroup");
        Mockito.when(childGroup.getGroups()).thenReturn(Collections.singleton(parentGroup));
        return parentGroup;
    }


    private Authentication getAuthentication(String name)
    {
        Authentication employeeAuth = (Authentication)Mockito.mock(Authentication.class);
        Mockito.when(employeeAuth.getName()).thenReturn(name);
        return employeeAuth;
    }
}
