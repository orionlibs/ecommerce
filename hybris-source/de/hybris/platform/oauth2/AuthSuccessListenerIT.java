package de.hybris.platform.oauth2;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.user.BruteForceLoginAttemptsModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;

@IntegrationTest
public class AuthSuccessListenerIT extends ServicelayerTransactionalBaseTest
{
    public static final String testId = "testclient";
    @Resource(name = "authSuccessListener")
    private ApplicationListener listener;
    @Resource(name = "flexibleSearchService")
    private FlexibleSearchService search;
    @Resource(name = "modelService")
    private ModelService model;


    @Test(expected = ModelNotFoundException.class)
    public void ignoreSuccess() throws Exception
    {
        this.listener.onApplicationEvent((ApplicationEvent)new AuthenticationSuccessEvent((Authentication)new UsernamePasswordAuthenticationToken("testclient", "pwd")));
        AuthFailureListstenerIT.findAttempts(this.search, "testclient");
    }


    @Test
    public void resetAttempts() throws Exception
    {
        BruteForceLoginAttemptsModel attempts = (BruteForceLoginAttemptsModel)this.model.create(BruteForceLoginAttemptsModel.class);
        attempts.setUid("testclient");
        attempts.setAttempts(Integer.valueOf(1));
        this.model.save(attempts);
        this.listener.onApplicationEvent((ApplicationEvent)new AuthenticationSuccessEvent((Authentication)new UsernamePasswordAuthenticationToken("testclient", "pwd")));
        Assertions.assertThat(AuthFailureListstenerIT.findAttempts(this.search, "testclient").getAttempts()).isEqualTo(0);
    }
}
