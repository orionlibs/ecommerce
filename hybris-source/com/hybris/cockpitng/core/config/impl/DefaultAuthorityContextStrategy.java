/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl;

import com.hybris.cockpitng.core.config.CockpitConfigurationContextStrategy;
import com.hybris.cockpitng.core.user.impl.AuthorityGroup;
import com.hybris.cockpitng.core.util.Validate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Gets the current selected authority group as parent context for a user. You can use comma separated list of
 * principals such as "admin,inventorymanager,inventoryuser". This strategy will match any of them.
 */
public class DefaultAuthorityContextStrategy extends DefaultAuthorityGroupContextStrategy
{
    private static final String REGEXP_CONTEXT_VALUE = "^(.*)\u2400\\{((a[0-9]+)|u|g)}$";
    private static final Pattern PATTERN_CONTEXT_VALUE = Pattern.compile(REGEXP_CONTEXT_VALUE);
    private static final char INDEX_STAMP_PREFIX_CHAR = 'a';
    private static final String INDEX_STAMP_PREFIX = "" + INDEX_STAMP_PREFIX_CHAR;
    private static final String OPERATOR_LOGICAL_AND = "&";


    @Override
    protected Pattern getStampedValuePattern()
    {
        return PATTERN_CONTEXT_VALUE;
    }


    @Override
    protected boolean hasParentContexts(final AuthorityGroup authorityGroup, final String currentUser, final String principal)
    {
        Validate.notNull("Authority group may not be null", authorityGroup);
        return super.hasParentContexts(authorityGroup, currentUser, principal) || isAuthorityGroup(authorityGroup, principal)
                        || (isAuthority(authorityGroup, principal) && hasMoreAuthorities(authorityGroup, principal));
    }


    protected boolean isAuthorityGroup(final AuthorityGroup authorityGroup, final String principal)
    {
        return Objects.equals(authorityGroup.getCode(), extractAuthorityGroup(principal));
    }


    protected boolean isAuthority(final AuthorityGroup authorityGroup, final String authority)
    {
        return CollectionUtils.isNotEmpty(authorityGroup.getAuthorities())
                        && authorityGroup.getAuthorities().contains(removeStamp(authority, this::isAuthorityIndexStamp));
    }


    @Override
    protected List<String> getParentContexts(final AuthorityGroup authorityGroup, final String currentUser, final String principal)
    {
        final List<String> superParentContexts = super.getParentContexts(authorityGroup, currentUser, principal);
        if(CockpitConfigurationContextStrategy.EMPTY_PARENT_CONTEXT.equals(superParentContexts)
                        && hasMoreAuthorities(authorityGroup, principal))
        {
            final int index = isAuthorityGroup(authorityGroup, principal) ? 0 : (getAuthorityIndex(authorityGroup, principal) + 1);
            return Collections
                            .singletonList(stampParentValue(authorityGroup.getAuthorities().get(index), "" + INDEX_STAMP_PREFIX + index));
        }
        return superParentContexts;
    }


    protected boolean hasMoreAuthorities(final AuthorityGroup authorityGroup, final String authority)
    {
        return (isAuthorityGroup(authorityGroup, authority) && CollectionUtils.isNotEmpty(authorityGroup.getAuthorities()))
                        || getAuthorityIndex(authorityGroup, authority) < (authorityGroup.getAuthorities().size() - 1);
    }


    protected int getAuthorityIndex(final AuthorityGroup authorityGroup, final String authority)
    {
        return (Integer)ObjectUtils.defaultIfNull(extractAuthorityIndex(authority),
                        authorityGroup.getAuthorities().indexOf(authority));
    }


    protected Integer extractAuthorityIndex(final String context)
    {
        final String stamp = getStamp(context, this::isAuthorityIndexStamp);
        return stamp != null ? Integer.valueOf(stamp.substring(1)) : null;
    }


    protected boolean isAuthorityIndexStamp(final String stamp)
    {
        try
        {
            return INDEX_STAMP_PREFIX_CHAR == stamp.charAt(0) && Integer.valueOf(stamp.substring(1)) > -1;
        }
        catch(final NumberFormatException ex)
        {
            return false;
        }
    }


    @Override
    protected boolean singleValueMatches(final String contextValue, final String value)
    {
        final String currentUser = getCockpitUserService().getCurrentUser();
        final AuthorityGroup authorityGroup = getAuthorityGroupService().getActiveAuthorityGroupForUser(currentUser);
        if(authorityGroup != null && contextValue != null && contextValue.contains(OPERATOR_LOGICAL_AND)
                        && StringUtils.equals(authorityGroup.getCode(), extractAuthorityGroup(value)))
        {
            return Stream.of(StringUtils.split(contextValue, OPERATOR_LOGICAL_AND)).map(String::trim)
                            .allMatch(authorityGroup.getAuthorities()::contains);
        }
        else
        {
            return super.singleValueMatches(contextValue, value);
        }
    }
}
