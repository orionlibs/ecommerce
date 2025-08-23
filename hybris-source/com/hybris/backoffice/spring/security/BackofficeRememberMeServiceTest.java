package com.hybris.backoffice.spring.security;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.jalo.user.LoginToken;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BackofficeRememberMeServiceTest
{
    private static final String ISO_CODE = "fr";
    @Mock
    private LanguageModel languageModel;
    @Mock
    private LoginToken loginToken;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private CommonI18NService commonI18NService;
    @InjectMocks
    private BackofficeRememberMeService backofficeRememberMeService;


    @Test
    public void shouldSetLocaleFromCookie()
    {
        Mockito.when(this.request.getSession()).thenReturn(this.session);
        Mockito.when(this.commonI18NService.getCurrentLanguage()).thenReturn(this.languageModel);
        Mockito.when(this.languageModel.getIsocode()).thenReturn("fr");
        this.backofficeRememberMeService.processAutoLoginCookie(this.loginToken, this.request, this.response);
        assertZkLocaleSet();
    }


    protected void assertZkLocaleSet()
    {
        ArgumentCaptor<String> attributeNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Locale> localeCaptor = ArgumentCaptor.forClass(Locale.class);
        ((HttpSession)Mockito.verify(this.session)).setAttribute((String)attributeNameCaptor.capture(), localeCaptor.capture());
        Assertions.assertThat((String)attributeNameCaptor.getValue()).isEqualTo("org.zkoss.web.preferred.locale");
        Assertions.assertThat(((Locale)localeCaptor.getValue()).toLanguageTag()).isEqualTo("fr");
    }
}
