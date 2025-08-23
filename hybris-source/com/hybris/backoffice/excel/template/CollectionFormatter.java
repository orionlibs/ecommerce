package com.hybris.backoffice.excel.template;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import javax.annotation.Nonnull;

public interface CollectionFormatter
{
    String formatToString(@Nonnull String... elements)
    {
        return formatToString(Arrays.asList(elements));
    }


    String formatToString(@Nonnull Collection<String> paramCollection);


    Set<String> formatToCollection(@Nonnull String paramString);
}
