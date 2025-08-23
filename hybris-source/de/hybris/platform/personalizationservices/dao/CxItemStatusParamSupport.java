package de.hybris.platform.personalizationservices.dao;

import de.hybris.platform.personalizationservices.enums.CxItemStatus;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class CxItemStatusParamSupport
{
    private static final List<CxItemStatus> ALL_STATUSES = Arrays.asList(CxItemStatus.values());
    private static final String STATUS_ERROR_MESSAGE = "Invalid status [{0}], available values {1}";
    private static final String COMMA_SEPARATOR = ",";


    public static Collection<CxItemStatus> buildStatuses(String statuses)
    {
        return (Collection<CxItemStatus>)Arrays.<String>stream(statuses.split(",")).map(CxItemStatusParamSupport::valueOf).collect(Collectors.toSet());
    }


    private static CxItemStatus valueOf(String s)
    {
        try
        {
            return CxItemStatus.valueOf(s.trim());
        }
        catch(IllegalArgumentException e)
        {
            throw new IllegalArgumentException(MessageFormat.format("Invalid status [{0}], available values {1}", new Object[] {s, ALL_STATUSES}), e);
        }
    }
}
