package de.hybris.platform.servicelayer.user.impl;

import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.PasswordPolicy;
import de.hybris.platform.servicelayer.user.PasswordPolicyService;
import de.hybris.platform.servicelayer.user.PasswordPolicyViolation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultPasswordPolicyService implements PasswordPolicyService
{
    @Autowired
    private List<PasswordPolicy> passwordPolicies;
    private List<PasswordPolicyMapping> passwordPolicyMappings;


    @PostConstruct
    void mapGroupsToPolicies()
    {
        this.passwordPolicyMappings = new ArrayList<>();
        for(PasswordPolicy passwordPolicy : this.passwordPolicies)
        {
            PasswordPolicyMapping passwordPolicyMapping = PasswordPolicyMapping.forPolicy(passwordPolicy);
            if(passwordPolicyMapping != null)
            {
                this.passwordPolicyMappings.add(passwordPolicyMapping);
            }
        }
    }


    public List<PasswordPolicyViolation> verifyPassword(UserModel user, String plainPassword, String encoding)
    {
        List<PasswordPolicyViolation> passwordPolicyViolations = new ArrayList<>();
        Set<String> userGroups = getUserGroupsUIDs(user);
        for(PasswordPolicyMapping mapping : this.passwordPolicyMappings)
        {
            if(mapping.isActiveForGroups(userGroups))
            {
                passwordPolicyViolations.addAll(mapping.getPasswordPolicy().verifyPassword(user, plainPassword, encoding));
            }
        }
        return passwordPolicyViolations;
    }


    protected Set<String> getUserGroupsUIDs(UserModel user)
    {
        Set<PrincipalGroupModel> allGroups = user.getAllGroups();
        if(allGroups != null)
        {
            return (Set<String>)allGroups.stream().map(i -> i.getUid()).collect(Collectors.toSet());
        }
        Set<PrincipalGroupModel> groups = user.getGroups();
        return (groups != null) ? (Set<String>)groups.stream().map(i -> i.getUid()).collect(Collectors.toSet()) : Collections.<String>emptySet();
    }
}
