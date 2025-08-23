package de.hybris.platform.jalo.flexiblesearch.hints.impl;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import de.hybris.platform.jalo.flexiblesearch.hints.QueryHint;
import de.hybris.platform.util.Config;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

public final class HanaHints implements QueryHint
{
    private static final Joiner JOINER = Joiner.on(',');
    private final boolean hanaUsed;
    private final Set<String> hints;


    HanaHints(boolean hanaUsed, List<String> hints)
    {
        Objects.requireNonNull(hints, "hints must not be null");
        Preconditions.checkArgument(hints.stream().noneMatch(StringUtils::isBlank), "hints can't be blank");
        this.hanaUsed = hanaUsed;
        this.hints = new LinkedHashSet<>(hints);
    }


    public HanaHints add(String hint)
    {
        Preconditions.checkArgument(StringUtils.isNotBlank(hint), "hint must not be blank");
        this.hints.add(hint);
        return this;
    }


    public static HanaHints create(String... hints)
    {
        if(ArrayUtils.isNotEmpty((Object[])hints))
        {
            return new HanaHints(Config.isHanaUsed(), Arrays.asList(hints));
        }
        return new HanaHints(Config.isHanaUsed(), Collections.emptyList());
    }


    public String apply(String query)
    {
        if(shouldApply())
        {
            return query + " WITH HINT(" + query + ")";
        }
        return query;
    }


    private boolean shouldApply()
    {
        return (this.hanaUsed && CollectionUtils.isNotEmpty(this.hints));
    }
}
