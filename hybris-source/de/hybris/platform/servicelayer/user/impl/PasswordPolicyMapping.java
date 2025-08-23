package de.hybris.platform.servicelayer.user.impl;

import com.google.common.base.Predicates;
import com.google.common.base.Splitter;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.user.PasswordPolicy;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class PasswordPolicyMapping
{
    private static final Splitter splitter = Splitter.on(',').omitEmptyStrings().trimResults();
    private static final String POLICY_PREFIX = "password.policy.";
    private static final String POLICY_INCLUDED_POSTFIX = ".groups.included";
    private static final String POLICY_EXCLUDED_POSTFIX = ".groups.excluded";
    final PasswordPolicy passwordPolicy;
    final Set<String> includedGroups;
    final Set<String> excludedGroups;
    final boolean matchAllExceptExcluded;


    public PasswordPolicyMapping(PasswordPolicy passwordPolicy, Set<String> includedGroups, Set<String> excludedGroups, boolean matchAllExceptExcluded)
    {
        this.passwordPolicy = passwordPolicy;
        this.includedGroups = includedGroups;
        this.excludedGroups = excludedGroups;
        this.matchAllExceptExcluded = matchAllExceptExcluded;
    }


    private static String includedGroups(PasswordPolicy passwordPolicy)
    {
        return "password.policy." + passwordPolicy.getPolicyName() + ".groups.included";
    }


    private static String excludedGroups(PasswordPolicy passwordPolicy)
    {
        return "password.policy." + passwordPolicy.getPolicyName() + ".groups.excluded";
    }


    public static PasswordPolicyMapping forPolicy(PasswordPolicy passwordPolicy)
    {
        List<String> excludedGroups;
        String includedGroupsProperty = Registry.getCurrentTenant().getConfig().getParameter(includedGroups(passwordPolicy));
        String excludedGroupsProperty = Registry.getCurrentTenant().getConfig().getParameter(excludedGroups(passwordPolicy));
        if(StringUtils.isBlank(includedGroupsProperty))
        {
            return null;
        }
        List<String> includedGroups = splitter.splitToList(includedGroupsProperty);
        if(StringUtils.isBlank(excludedGroupsProperty))
        {
            excludedGroups = Collections.emptyList();
        }
        else
        {
            excludedGroups = splitter.splitToList(excludedGroupsProperty);
        }
        if(includedGroups.contains("*"))
        {
            return new PasswordPolicyMapping(passwordPolicy, withoutWildcard(includedGroups), new HashSet<>(excludedGroups), true);
        }
        return new PasswordPolicyMapping(passwordPolicy, new HashSet<>(includedGroups), new HashSet<>(excludedGroups), false);
    }


    private static Set<String> withoutWildcard(List<String> includedGroups)
    {
        ImmutableList<String> withoutWildcard = ImmutableList.copyOf(Collections2.filter(includedGroups, Predicates.not(Predicates.equalTo("*"))));
        return new HashSet<>((Collection<? extends String>)withoutWildcard);
    }


    public boolean isActiveForGroups(Set<String> userGroups)
    {
        if(this.matchAllExceptExcluded && !allUserGroupsAreExcluded(userGroups))
        {
            return true;
        }
        Set<String> userGroupsCopy = new HashSet<>(userGroups);
        userGroupsCopy.removeAll(this.excludedGroups);
        Objects.requireNonNull(this.includedGroups);
        return userGroupsCopy.stream().anyMatch(this.includedGroups::contains);
    }


    private boolean allUserGroupsAreExcluded(Set<String> userGroups)
    {
        if(userGroups.isEmpty())
        {
            return false;
        }
        Set<String> userGroupsCopy = new HashSet<>(userGroups);
        userGroupsCopy.removeAll(this.excludedGroups);
        return userGroupsCopy.isEmpty();
    }


    public PasswordPolicy getPasswordPolicy()
    {
        return this.passwordPolicy;
    }
}
