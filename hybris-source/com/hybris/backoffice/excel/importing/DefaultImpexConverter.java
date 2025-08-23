package com.hybris.backoffice.excel.importing;

import com.hybris.backoffice.excel.data.Impex;
import com.hybris.backoffice.excel.data.ImpexForType;
import com.hybris.backoffice.excel.data.ImpexHeaderValue;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultImpexConverter implements ImpexConverter
{
    public static final String IMPEX_OPERATION_TYPE = "INSERT_UPDATE ";
    public static final String DEFAULT_FIELD_SEPARATOR = String.valueOf(';');
    public static final String DEFAULT_LINE_SEPARATOR = String.valueOf("\n");
    private static final Logger LOG = LoggerFactory.getLogger(DefaultImpexConverter.class);


    private static boolean isEmpty(Map.Entry<ImpexHeaderValue, Object> entry)
    {
        return (entry.getValue() != null && StringUtils.isBlank(entry.getValue().toString()));
    }


    public String convert(Impex impex)
    {
        StringBuilder sb = new StringBuilder();
        for(ImpexForType impexForType : impex.getImpexes())
        {
            sb.append(prepareImpexHeader(impexForType));
            sb.append(prepareImpexRows(impexForType));
            sb.append(DEFAULT_LINE_SEPARATOR).append(DEFAULT_LINE_SEPARATOR);
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Created impex content: \n {}", sb);
        }
        return sb.toString();
    }


    protected String prepareImpexRows(ImpexForType impexForType)
    {
        Set<String> rows = new HashSet<>();
        for(Iterator<Integer> iterator = impexForType.getImpexTable().rowKeySet().iterator(); iterator.hasNext(); )
        {
            Integer index = iterator.next();
            StringBuilder sb = new StringBuilder(DEFAULT_FIELD_SEPARATOR);
            Map<ImpexHeaderValue, Object> row = impexForType.getImpexTable().row(index);
            if(areUniqueAttributesPopulated(row))
            {
                impexForType.getImpexTable().columnKeySet()
                                .forEach(header -> sb.append(getValue(row.get(header))).append(DEFAULT_FIELD_SEPARATOR));
                rows.add(sb.toString());
            }
        }
        return String.join(DEFAULT_LINE_SEPARATOR, (Iterable)rows);
    }


    protected String getValue(Object value)
    {
        if(value != null)
        {
            String valueAsString = value.toString();
            if(valueAsString.contains(DEFAULT_FIELD_SEPARATOR))
            {
                String escapedValue = StringEscapeUtils.escapeCsv(valueAsString);
                return !escapedValue.startsWith("\"") ? String.format("\"%s\"", new Object[] {escapedValue}) : escapedValue;
            }
            return StringEscapeUtils.escapeCsv(valueAsString);
        }
        return "";
    }


    protected boolean areUniqueAttributesPopulated(Map<ImpexHeaderValue, Object> row)
    {
        for(Map.Entry<ImpexHeaderValue, Object> entry : row.entrySet())
        {
            if(((ImpexHeaderValue)entry.getKey()).isUnique() && ((ImpexHeaderValue)entry.getKey()).isMandatory() && isEmpty(entry))
            {
                return false;
            }
        }
        return true;
    }


    protected String prepareImpexHeader(ImpexForType impexForType)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT_UPDATE ").append(impexForType.getTypeCode()).append(DEFAULT_FIELD_SEPARATOR);
        impexForType.getImpexTable().columnKeySet()
                        .forEach(attr -> sb.append(prepareHeaderAttribute(attr)).append(DEFAULT_FIELD_SEPARATOR));
        sb.append(DEFAULT_LINE_SEPARATOR);
        return sb.toString();
    }


    protected String prepareHeaderAttribute(ImpexHeaderValue headerAttribute)
    {
        List<String> headerAttributes = new ArrayList<>();
        if(headerAttribute.isUnique())
        {
            headerAttributes.add("unique=true");
        }
        if(StringUtils.isNotBlank(headerAttribute.getLang()))
        {
            headerAttributes.add(String.format("lang=%s", new Object[] {headerAttribute.getLang()}));
        }
        if(StringUtils.isNotBlank(headerAttribute.getDateFormat()))
        {
            headerAttributes.add(String.format("dateformat=%s", new Object[] {headerAttribute.getDateFormat()}));
        }
        if(StringUtils.isNotBlank(headerAttribute.getTranslator()))
        {
            headerAttributes.add(String.format("translator=%s", new Object[] {headerAttribute.getTranslator()}));
        }
        return headerAttributes.isEmpty() ? headerAttribute.getName() :
                        String.format("%s[%s]", new Object[] {headerAttribute.getName(), String.join(",", (Iterable)headerAttributes)});
    }
}
