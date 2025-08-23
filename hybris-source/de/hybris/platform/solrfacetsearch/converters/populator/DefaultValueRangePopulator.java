package de.hybris.platform.solrfacetsearch.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.config.ValueRange;
import de.hybris.platform.solrfacetsearch.config.ValueRangeType;
import de.hybris.platform.solrfacetsearch.config.ValueRanges;
import de.hybris.platform.solrfacetsearch.model.config.SolrValueRangeModel;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class DefaultValueRangePopulator implements Populator<SolrValueRangeModel, ValueRange>
{
    private static final Logger LOG = Logger.getLogger(DefaultValueRangePopulator.class);


    public void populate(SolrValueRangeModel source, ValueRange target)
    {
        target.setFrom(populateComparableFromString(source.getSolrValueRangeSet().getType(), source.getFrom()));
        target.setName(source.getName());
        target.setTo(populateComparableFromString(source.getSolrValueRangeSet().getType(), source.getTo()));
    }


    protected Comparable populateComparableFromString(String type, String value)
    {
        if(StringUtils.isBlank(value))
        {
            return value;
        }
        switch(null.$SwitchMap$de$hybris$platform$solrfacetsearch$config$ValueRangeType[ValueRangeType.valueOf(type.toUpperCase(Locale.ROOT)).ordinal()])
        {
            case 1:
                return value.toLowerCase(Locale.ROOT).intern();
            case 2:
                return Double.valueOf(value);
            case 3:
                return Integer.valueOf(value);
            case 4:
                return parseDate(value);
        }
        return value;
    }


    protected Comparable parseDate(String value)
    {
        try
        {
            return ValueRanges.parseDate(value);
        }
        catch(ParseException e)
        {
            LOG.error(String.format("'%s' is not a proper date value. Accepted format : %s", new Object[] {value, "yyyy-MM-dd [HH:mm]"}));
            return new Date(0L);
        }
    }
}
