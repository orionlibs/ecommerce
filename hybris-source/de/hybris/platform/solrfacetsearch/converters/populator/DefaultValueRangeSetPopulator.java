package de.hybris.platform.solrfacetsearch.converters.populator;

import de.hybris.platform.converters.Converters;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.solrfacetsearch.config.ValueRange;
import de.hybris.platform.solrfacetsearch.config.ValueRangeSet;
import de.hybris.platform.solrfacetsearch.config.ValueRangeType;
import de.hybris.platform.solrfacetsearch.model.config.SolrValueRangeModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrValueRangeSetModel;
import java.util.Locale;
import org.apache.commons.lang.StringUtils;

public class DefaultValueRangeSetPopulator implements Populator<SolrValueRangeSetModel, ValueRangeSet>
{
    private static final String DEFAULT_QUALIFIER = "default";
    private Converter<SolrValueRangeModel, ValueRange> valueRangeConverter;
    private String defaultQualifier;


    public void populate(SolrValueRangeSetModel source, ValueRangeSet target)
    {
        if(source.getQualifier() != null)
        {
            target.setQualifier(source.getQualifier());
        }
        else
        {
            target.setQualifier(getDefaultQualifier());
        }
        target.setValueRanges(Converters.convertAll(source.getSolrValueRanges(), this.valueRangeConverter));
        target.setType(ValueRangeType.valueOf(source.getType().toUpperCase(Locale.ROOT)));
    }


    public void setValueRangeConverter(Converter<SolrValueRangeModel, ValueRange> valueRangeConverter)
    {
        this.valueRangeConverter = valueRangeConverter;
    }


    protected String getDefaultQualifier()
    {
        if(StringUtils.isEmpty(this.defaultQualifier))
        {
            return "default";
        }
        return this.defaultQualifier;
    }


    public void setDefaultQualifier(String defaultQualifier)
    {
        this.defaultQualifier = defaultQualifier;
    }
}
