package de.hybris.platform.servicelayer.user.impl;

import com.google.common.collect.Sets;
import com.google.common.io.Files;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.user.PasswordPolicy;
import de.hybris.platform.servicelayer.user.PasswordPolicyViolation;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class BlacklistFilePasswordPolicy implements PasswordPolicy
{
    private static final Logger LOG = LoggerFactory.getLogger(BlacklistFilePasswordPolicy.class);
    private static final String PASSWORD_BLACKLIST_FILE = "password.policy.blacklist.file";
    public static final String BLACKLISTED_PASSWORD = "blacklistedpassword";
    private L10NService l10NService;
    private Set<String> blacklistedPassword;
    private final String policyName;


    public BlacklistFilePasswordPolicy(String policyName)
    {
        this.policyName = policyName;
    }


    @PostConstruct
    protected void init()
    {
        String passwordBlacklist = Registry.getCurrentTenantNoFallback().getConfig().getParameter("password.policy.blacklist.file");
        URL resource = Registry.class.getClassLoader().getResource(passwordBlacklist);
        if(StringUtils.isNotBlank(passwordBlacklist))
        {
            try
            {
                File blacklistFile = new File(resource.toURI());
                this.blacklistedPassword = Sets.newHashSet(Files.readLines(blacklistFile, Charset.forName("UTF-8")));
            }
            catch(IOException | java.net.URISyntaxException e)
            {
                LOG.info("Failed to read blacklist password file: {}. BlacklistFilePasswordPolicy will not be active.", passwordBlacklist);
            }
        }
        else
        {
            LOG.info("Password blacklist file is not set. BlacklistFilePasswordPolicy will not be active.");
        }
    }


    public List<PasswordPolicyViolation> verifyPassword(UserModel user, String plainPassword, String encoding)
    {
        if(this.blacklistedPassword.contains(plainPassword))
        {
            return Arrays.asList(new PasswordPolicyViolation[] {(PasswordPolicyViolation)new DefaultPasswordPolicyViolation("blacklistedpassword", this.l10NService
                            .getLocalizedString("password.policy.violation.blacklist.blacklistedpassword"))});
        }
        return Collections.emptyList();
    }


    public String getPolicyName()
    {
        return this.policyName;
    }


    @Required
    public void setL10NService(L10NService l10NService)
    {
        this.l10NService = l10NService;
    }
}
