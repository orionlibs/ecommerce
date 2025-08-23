package de.hybris.platform.servicelayer.user.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.user.PasswordPolicy;
import de.hybris.platform.servicelayer.user.PasswordPolicyViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Required;

public class RegexPasswordPolicy implements PasswordPolicy
{
    private L10NService l10NService;
    private Map<String, String> requiredPatterns;
    private Map<String, String> disallowedPatterns;
    private static final String REQUIRED_PATTERN_LOCALIZATION = "password.policy.violation.regex.required.";
    private static final String DISALLOWED_PATTERN_LOCALIZATION = "password.policy.violation.regex.disallowed.";
    private final String policyName;


    public RegexPasswordPolicy(String policyName)
    {
        this.policyName = policyName;
    }


    @PostConstruct
    void initPatterns()
    {
        this
                        .requiredPatterns = Registry.getCurrentTenantNoFallback().getConfig().getParametersMatching("password\\.policy\\.regex\\.required\\.(.*)", true);
        this
                        .disallowedPatterns = Registry.getCurrentTenantNoFallback().getConfig().getParametersMatching("password\\.policy\\.regex\\.disallowed\\.(.*)", true);
    }


    public List<PasswordPolicyViolation> verifyPassword(UserModel user, String plainPassword, String encoding)
    {
        List<PasswordPolicyViolation> violations = new ArrayList<>();
        for(Map.Entry<String, String> requiredPattern : this.requiredPatterns.entrySet())
        {
            if(!plainPassword.matches(requiredPattern.getValue()))
            {
                String localizedMsg = this.l10NService.getLocalizedString("password.policy.violation.regex.required." + (String)requiredPattern
                                .getKey());
                violations.add(new DefaultPasswordPolicyViolation(requiredPattern.getKey(), localizedMsg));
            }
        }
        for(Map.Entry<String, String> disallowedPattern : this.disallowedPatterns.entrySet())
        {
            if(plainPassword.matches(disallowedPattern.getValue()))
            {
                String localizedMsg = this.l10NService.getLocalizedString("password.policy.violation.regex.disallowed." + (String)disallowedPattern.getKey());
                violations.add(new DefaultPasswordPolicyViolation(disallowedPattern.getKey(), localizedMsg));
            }
        }
        return violations;
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
