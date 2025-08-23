package de.hybris.bootstrap.ddl;

import de.hybris.bootstrap.annotations.UnitTest;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

@UnitTest
public class InitUserPasswordServiceTest
{
    private InitUserPasswordService initUserPasswordService;


    @Before
    public void prepareTest()
    {
        this.initUserPasswordService = new InitUserPasswordService(getMockedPropertiesLoader());
    }


    @Test
    public void shouldReturnUserPassword()
    {
        String adminPassword = this.initUserPasswordService.readUserPassword("admin");
        String userPassword = this.initUserPasswordService.readUserPassword("user");
        String anonymousPassword = this.initUserPasswordService.readUserPassword("anonymous");
        AssertionsForInterfaceTypes.assertThat(adminPassword).isEqualTo("nimda");
        AssertionsForInterfaceTypes.assertThat(userPassword).isEqualTo("resu");
        AssertionsForInterfaceTypes.assertThat(anonymousPassword).isEqualTo("suomynona");
    }


    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionIfPasswordIsEmpty()
    {
        this.initUserPasswordService.readUserPassword("user1");
    }


    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionIfPasswordIsNotProvidedForUser()
    {
        this.initUserPasswordService.readUserPassword("user2");
    }


    private PropertiesLoader getMockedPropertiesLoader()
    {
        PropertiesLoader propertiesLoader = (PropertiesLoader)Mockito.mock(PropertiesLoader.class);
        Mockito.when(propertiesLoader.getProperty("initialpassword.admin")).thenReturn("nimda");
        Mockito.when(propertiesLoader.getProperty("initialpassword.user")).thenReturn("resu");
        Mockito.when(propertiesLoader.getProperty("initialpassword.anonymous")).thenReturn("suomynona");
        Mockito.when(propertiesLoader.getProperty("initialpassword.user1")).thenReturn("");
        return propertiesLoader;
    }
}
