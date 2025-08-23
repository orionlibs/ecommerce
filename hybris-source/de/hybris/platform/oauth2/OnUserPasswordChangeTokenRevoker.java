package de.hybris.platform.oauth2;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.user.listener.PasswordChangeEvent;
import de.hybris.platform.servicelayer.user.listener.PasswordChangeListener;
import de.hybris.platform.webservicescommons.oauth2.token.OAuthRevokeTokenService;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

public class OnUserPasswordChangeTokenRevoker implements PasswordChangeListener
{
    static final String ENABLED_CONFIG_KEY = "oauth2.revoke.tokens.on.user.password.change";
    private static final Logger LOG = LoggerFactory.getLogger(OnUserPasswordChangeTokenRevoker.class);
    private final OAuthRevokeTokenService oauthRevokeTokenService;
    private final ConfigurationService configurationService;


    public OnUserPasswordChangeTokenRevoker(OAuthRevokeTokenService oauthRevokeTokenService, ConfigurationService configurationService)
    {
        this.oauthRevokeTokenService = Objects.<OAuthRevokeTokenService>requireNonNull(oauthRevokeTokenService, "oauthRevokeTokenService cannot be null.");
        this.configurationService = Objects.<ConfigurationService>requireNonNull(configurationService, "configurationService cannot be null.");
    }


    public void passwordChanged(PasswordChangeEvent event)
    {
        if(event == null || isDisabled())
        {
            return;
        }
        LOG.info("User password changed. Removing all their tokens.");
        this.oauthRevokeTokenService.revokeUserAccessTokens(event.getUserId(), getCurrentOAuthToken());
    }


    private Collection<String> getCurrentOAuthToken()
    {
        return Optional.<SecurityContext>ofNullable(SecurityContextHolder.getContext())
                        .map(SecurityContext::getAuthentication)
                        .map(Authentication::getDetails)
                        .map(d -> (d instanceof OAuth2AuthenticationDetails) ? (OAuth2AuthenticationDetails)d : null)
                        .map(OAuth2AuthenticationDetails::getTokenValue)
                        .map(Collections::singleton)
                        .orElse(Collections.emptySet());
    }


    private boolean isDisabled()
    {
        return !this.configurationService.getConfiguration().getBoolean("oauth2.revoke.tokens.on.user.password.change", true);
    }
}
