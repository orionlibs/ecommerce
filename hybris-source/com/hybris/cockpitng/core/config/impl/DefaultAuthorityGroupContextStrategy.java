/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl;

import com.hybris.cockpitng.core.config.CockpitConfigurationContextStrategy;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.user.AuthorityGroupService;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.core.user.impl.AuthorityGroup;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

/**
 * Gets the current selected authority group as parent context for a user. You can use comma separated list of
 * principals such as "admin,inventorymanager,inventoryuser". This strategy will match any of them.
 */
public class DefaultAuthorityGroupContextStrategy implements CockpitConfigurationContextStrategy
{
    private static final String REGEXP_CONTEXT_VALUE = "^(.*)\u2400\\{([ug])}$";
    private static final Pattern PATTERN_CONTEXT_VALUE = Pattern.compile(REGEXP_CONTEXT_VALUE);
    private static final String FORMAT_CONTEXT_VALUE = "%s\u2400{%s}";
    private static final String CONTEXT_VALUE_TYPE_USER = "u";
    private static final String CONTEXT_VALUE_TYPE_GROUP = "g";
    private static final int MATCHER_GROUP = 2;
    private AuthorityGroupService authorityGroupService;
    private CockpitUserService cockpitUserService;


    @Override
    public boolean isResettable()
    {
        return true;
    }


    @Override
    public List<String> getParentContexts(final String context)
    {
        if(getAuthorityGroupService() != null && context != null)
        {
            final String currentUser = getCockpitUserService().getCurrentUser();
            final AuthorityGroup activeAuthorityGroupForUser = getAuthorityGroupService()
                            .getActiveAuthorityGroupForUser(currentUser);
            if(activeAuthorityGroupForUser != null)
            {
                final Optional<List<String>> parentContexts = getPrincipals(context).stream()
                                .filter(principal -> hasParentContexts(activeAuthorityGroupForUser, currentUser, principal))
                                .map(principal -> getParentContexts(activeAuthorityGroupForUser, currentUser, principal)).findFirst();
                if(parentContexts.isPresent())
                {
                    return parentContexts.get();
                }
            }
        }
        if(StringUtils.isBlank(context))
        {
            return Collections.emptyList();
        }
        else
        {
            // fallback to config without role
            return EMPTY_PARENT_CONTEXT;
        }
    }


    protected boolean hasParentContexts(final AuthorityGroup authorityGroup, final String currentUser, final String principal)
    {
        return StringUtils.equals(currentUser, extractUser(principal));
    }


    protected boolean isStamped(final String context)
    {
        return context != null && getStampedValuePattern().matcher(context).matches();
    }


    protected String extractAuthorityGroup(final String context)
    {
        return removeStamp(context, CONTEXT_VALUE_TYPE_GROUP);
    }


    protected String extractUser(final String context)
    {
        return removeStamp(context, CONTEXT_VALUE_TYPE_USER);
    }


    protected String removeStamp(final String context)
    {
        return removeStamp(context, (String)null);
    }


    protected String removeStamp(final String context, final String type)
    {
        return removeStamp(context, stamp -> type == null || StringUtils.equals(stamp, type));
    }


    protected String removeStamp(final String context, final Predicate<String> test)
    {
        if(StringUtils.isBlank(context))
        {
            return null;
        }
        final Matcher matcher = getStampedValuePattern().matcher(context);
        return isStamped(context) && matcher.matches() && test.test(matcher.group(MATCHER_GROUP)) ? matcher.group(1) : context;
    }


    protected String getStamp(final String context)
    {
        return getStamp(context, (String)null);
    }


    protected String getStamp(final String context, final String type)
    {
        return getStamp(context, stamp -> type == null || StringUtils.equals(stamp, type));
    }


    protected String getStamp(final String context, final Predicate<String> test)
    {
        final Matcher matcher = getStampedValuePattern().matcher(context);
        return isStamped(context) && matcher.matches() && test.test(matcher.group(MATCHER_GROUP)) ? matcher.group(MATCHER_GROUP) : null;
    }


    protected Pattern getStampedValuePattern()
    {
        return PATTERN_CONTEXT_VALUE;
    }


    protected List<String> getParentContexts(final AuthorityGroup authorityGroup, final String currentUser, final String principal)
    {
        if(StringUtils.equals(currentUser, extractUser(principal)))
        {
            return Collections.singletonList(stampParentValue(authorityGroup.getCode(), CONTEXT_VALUE_TYPE_GROUP));
        }
        return EMPTY_PARENT_CONTEXT;
    }


    protected String stampParentValue(final String value, final String stamp)
    {
        return String.format(FORMAT_CONTEXT_VALUE, value, stamp);
    }


    @Override
    public ConfigContext getConfigurationCacheKey(final ConfigContext configContext)
    {
        if(getAuthorityGroupService() != null)
        {
            final AuthorityGroup authorityGroup = getAuthorityGroupService()
                            .getActiveAuthorityGroupForUser(getCockpitUserService().getCurrentUser());
            if(authorityGroup != null && authorityGroup.getCode() != null)
            {
                return new RoleAwareConfigContext(configContext, authorityGroup.getCode());
            }
        }
        return configContext;
    }


    @Override
    public boolean valueMatches(final String contextValue, final String value)
    {
        if(StringUtils.isNotBlank(contextValue))
        {
            final List<String> contextPrincipals = getPrincipals(contextValue);
            final List<String> valuePrincipals = getPrincipals(value);
            if(valuePrincipals.isEmpty())
            {
                return contextPrincipals.isEmpty();
            }
            else
            {
                return valuePrincipals.stream().allMatch(
                                valuePrincipal -> contextPrincipals.stream().anyMatch(principal -> singleValueMatches(principal, valuePrincipal)));
            }
        }
        return singleValueMatches(contextValue, value);
    }


    protected List<String> getPrincipals(final String contextValue)
    {
        if(StringUtils.isNotBlank(contextValue))
        {
            return Stream.of(StringUtils.split(contextValue, ',')).map(String::trim).collect(Collectors.toList());
        }
        else
        {
            return Collections.emptyList();
        }
    }


    protected boolean singleValueMatches(final String contextValue, final String value)
    {
        return (contextValue == null && StringUtils.isBlank(value))
                        || (contextValue != null && contextValue.equalsIgnoreCase(removeStamp(value)));
    }


    protected AuthorityGroupService getAuthorityGroupService()
    {
        return authorityGroupService;
    }


    public void setAuthorityGroupService(final AuthorityGroupService authorityGroupService)
    {
        this.authorityGroupService = authorityGroupService;
    }


    protected CockpitUserService getCockpitUserService()
    {
        return cockpitUserService;
    }


    public void setCockpitUserService(final CockpitUserService cockpitUserService)
    {
        this.cockpitUserService = cockpitUserService;
    }


    static final class RoleAwareConfigContext implements ConfigContext
    {
        static final String ROLE_ATTRIBUTE = "__role";
        private final ConfigContext context;
        private final String role;


        private RoleAwareConfigContext(final ConfigContext configContext, final String role)
        {
            this.context = configContext;
            this.role = role;
        }


        @Override
        public String getAttribute(final String name)
        {
            if(ROLE_ATTRIBUTE.equals(name))
            {
                return role;
            }
            else
            {
                return context.getAttribute(name);
            }
        }


        @Override
        public Set<String> getAttributeNames()
        {
            final Set<String> attributeNames = new HashSet<>(context.getAttributeNames());
            attributeNames.add(ROLE_ATTRIBUTE);
            return Collections.unmodifiableSet(attributeNames);
        }


        @Override
        public boolean equals(final Object o)
        {
            if(this == o)
            {
                return true;
            }
            if(o == null || getClass() != o.getClass())
            {
                return false;
            }
            final RoleAwareConfigContext roleAwareConfigContext = (RoleAwareConfigContext)o;
            if(!context.equals(roleAwareConfigContext.context))
            {
                return false;
            }
            return role.equals(roleAwareConfigContext.role);
        }


        @Override
        public int hashCode()
        {
            int result = context.hashCode();
            result = 31 * result + role.hashCode();
            return result;
        }
    }
}
