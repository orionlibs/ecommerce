package de.hybris.platform.oauth2;

import com.google.common.collect.ImmutableMap;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.user.BruteForceLoginAttemptsModel;
import de.hybris.platform.core.model.user.BruteForceOAuthDisabledAuditModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel;
import de.hybris.platform.webservicescommons.oauth2.client.ClientDetailsDao;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@IntegrationTest
public class AuthFailureListstenerIT extends ServicelayerTransactionalBaseTest
{
    public static final String testId = "testclient";
    private static final String PWD = "pwd";
    @Resource(name = "authFailureListener")
    private ApplicationListener listener;
    @Resource(name = "configurationService")
    private ConfigurationService config;
    @Resource(name = "flexibleSearchService")
    private FlexibleSearchService search;
    @Resource(name = "modelService")
    private ModelService model;
    @Resource(name = "oauthClientDetailsDao")
    private ClientDetailsDao clientDao;


    @Before
    public void setUp() throws Exception
    {
        this.config.getConfiguration().setProperty("oauth2.maxAuthenticationAttempts", "2");
        OAuthClientDetailsModel client = (OAuthClientDetailsModel)this.model.create(OAuthClientDetailsModel.class);
        client.setClientId("testclient");
        client.setClientSecret("pwd");
        this.model.save(client);
    }


    @Test
    public void createNewAttemptsRecord() throws Exception
    {
        this.listener.onApplicationEvent((ApplicationEvent)new AuthenticationFailureBadCredentialsEvent((Authentication)new UsernamePasswordAuthenticationToken("testclient", "pwd"), (AuthenticationException)new BadCredentialsException("")));
        Assertions.assertThat(findAttempts(this.search, "testclient").getAttempts()).isEqualTo(1);
        Assertions.assertThat(auditRecords().getResult()).isEmpty();
    }


    @Test
    public void createNewAuditRecord() throws Exception
    {
        createNewAttemptsRecord();
        this.listener.onApplicationEvent((ApplicationEvent)new AuthenticationFailureBadCredentialsEvent((Authentication)new UsernamePasswordAuthenticationToken("testclient", "pwd"), (AuthenticationException)new BadCredentialsException("")));
        Assertions.assertThat(findAttempts(this.search, "testclient").getAttempts()).isEqualTo(0);
        List<BruteForceOAuthDisabledAuditModel> result = auditRecords().getResult();
        Assertions.assertThat(result).hasSize(1);
        Assertions.assertThat(((BruteForceOAuthDisabledAuditModel)result.get(0)).getFailedOAuthAuthorizations()).isEqualTo(2);
        Assertions.assertThat(this.clientDao.findClientById("testclient").getDisabled()).isTrue();
    }


    public static BruteForceLoginAttemptsModel findAttempts(FlexibleSearchService search, String uid)
    {
        return (BruteForceLoginAttemptsModel)search.searchUnique(new FlexibleSearchQuery(String.format("select {pk} from {%s} where {%s}= ?uid", new Object[] {"BruteForceLoginAttempts", "uid"}), (Map)ImmutableMap.of("uid", uid)));
    }


    private SearchResult<BruteForceOAuthDisabledAuditModel> auditRecords()
    {
        return this.search.search(
                        String.format("select {a.pk} from {%s as a}  where {a.%s}=?uid", new Object[] {"BruteForceOAuthDisabledAudit", "uid"}), (Map)ImmutableMap.of("uid", "testclient"));
    }
}
