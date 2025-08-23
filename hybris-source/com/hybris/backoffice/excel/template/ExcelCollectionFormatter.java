package com.hybris.backoffice.excel.template;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

public class ExcelCollectionFormatter implements CollectionFormatter
{
    private static final Pattern BETWEEN_CURLY_BRACES_PATTERN = Pattern.compile("\\{(.+)},*");
    private static final int SPLIT_LIMIT = 1000;
    private static final String SPLIT_REGEX = "(?<=}),(?=\\{)";
    private static final String JOIN_CHARACTER = ",";
    private static final UnaryOperator<String> MAP_TO_CURLY_BRACES_FORMAT;

    static
    {
        MAP_TO_CURLY_BRACES_FORMAT = (text -> "{" + text + "}");
    }

    public String formatToString(@Nonnull Collection<String> collection)
    {
        return collection.stream()
                        .map(MAP_TO_CURLY_BRACES_FORMAT)
                        .distinct()
                        .collect(Collectors.joining(","));
    }


    public Set<String> formatToCollection(@Nonnull String string)
    {
        Set<String> objects = new LinkedHashSet<>();
        for(String element : string.split("(?<=}),(?=\\{)", 1000))
        {
            Matcher matcher = BETWEEN_CURLY_BRACES_PATTERN.matcher(element);
            if(matcher.find())
            {
                objects.add(matcher.group(1));
            }
        }
        return objects;
    }
}
