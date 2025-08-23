/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions.export.csv;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListView;
import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.dataaccess.services.PropertyValueService;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import com.hybris.cockpitng.widgets.collectionbrowser.CollectionBrowserController;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.common.AbstractMoldStrategy;
import com.hybris.cockpitng.widgets.common.csv.CsvAwareListViewRenderer;
import com.hybris.cockpitng.widgets.util.UILabelUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zul.Filedownload;

public class ListViewExportCSVAction implements CockpitAction<Map, Object>
{
    public static final String MODEL_PAGEABLE = CollectionBrowserController.MODEL_PAGEABLE;
    public static final String MODEL_COLUMNS_CONFIG = AbstractMoldStrategy.MODEL_COLUMNS_CONFIG;
    public static final String CONFIRMATION_THRESHOLD = "confirmation.threshold";
    public static final String EXPORT_CSV_CONFIRMATION = "export.csv.confirmation";
    public static final String DELIMITER = "delimiter";
    /**
     * @deprecated since 6.5
     * @see #CSV_DELIMITER
     */
    @Deprecated(since = "6.5", forRemoval = true)
    public static final String BEGIN_SPREADSHEET_FORMULA_CHARACTER = "=";
    public static final String CSV_DELIMITER = ";";
    /**
     * @deprecated since 6.5
     * @see #CSV_DELIMITER
     */
    @Deprecated(since = "6.5", forRemoval = true)
    public static final String CSV_DELIMETER = CSV_DELIMITER;
    public static final String COCKPITNG_EXPORT_CSV_ACTION_DELIMITER = "cockpitng.export.csv.action.delimiter";
    public static final String COCKPITNG_EXPORT_CSV_ACTION_INPUT_SANITIZATION_ENABLED = "cockpitng.export.csv.action.input.sanitization.enabled";
    public static final String COCKPITNG_EXPORT_CSV_ACTION_INPUT_SANITIZATION_MODE = "cockpitng.export.csv.action.input.sanitization.mode";
    public static final String COCKPITNG_EXPORT_CSV_ACTION_INPUT_SANITIZATION_PREFIX = "cockpitng.export.csv.action.input.sanitization.prefix";
    public static final String SANITIZATION_MODE_TRIM = "trim";
    public static final String SANITIZATION_MODE_PREFIX = "prefix";
    public static final String DEFAULT_SANITIZATION_PREFIX = "'";
    protected static final char[] FORMULA_CHARACTERS =
                    {'@', '+', '-', '=', '|', '%'};
    private static final Logger LOG = LoggerFactory.getLogger(ListViewExportCSVAction.class);
    @Resource
    private LabelService labelService;
    @Resource
    private PermissionFacade permissionFacade;
    @Resource
    private TypeFacade typeFacade;
    @Resource
    private PropertyValueService propertyValueService;
    @Resource
    private CockpitLocaleService cockpitLocaleService;
    @Resource
    private CockpitProperties cockpitProperties;


    @Override
    public ActionResult<Object> perform(final ActionContext<Map> ctx)
    {
        final Pageable pageable = (Pageable)(ctx.getData()).get(MODEL_PAGEABLE);
        final ListView listView = (ListView)(ctx.getData()).get(MODEL_COLUMNS_CONFIG);
        final String csvContent = createCsv(pageable, listView, getCsvDelimiter(ctx));
        writeBinaryResponse(csvContent);
        return new ActionResult<>(ActionResult.SUCCESS);
    }


    protected String getCsvDelimiter(final ActionContext<Map> ctx)
    {
        final String delimiter = StringUtils.defaultIfBlank((String)ctx.getParameter(DELIMITER),
                        getCockpitProperties().getProperty(COCKPITNG_EXPORT_CSV_ACTION_DELIMITER));
        return StringUtils.defaultIfBlank(delimiter, CSV_DELIMITER);
    }


    protected void writeBinaryResponse(final String csvContent)
    {
        Filedownload.save(csvContent, "text/comma-separated-values;charset=UTF-8", "list.csv");
    }


    private String createCsv(final Pageable pageable, final ListView listView, final String csvDelimiter)
    {
        final StringBuilder builder = new StringBuilder();
        final List<ListColumn> columnsToRender = findColumnsPrintableInCSV(listView.getColumn());
        createCsvHeader(builder, pageable, columnsToRender, csvDelimiter);
        createCsvContent(builder, pageable, columnsToRender, csvDelimiter);
        return builder.toString();
    }


    List<ListColumn> findColumnsPrintableInCSV(final List<ListColumn> columns)
    {
        return columns.stream().filter(listColumn -> {
            if(StringUtils.isNotBlank(listColumn.getSpringBean()))
            {
                return getSpringBean(listColumn.getSpringBean()) instanceof CsvAwareListViewRenderer;
            }
            return StringUtils.isBlank(listColumn.getSpringBean()) && StringUtils.isBlank(listColumn.getClazz());
        }).collect(Collectors.toList());
    }


    protected Object getSpringBean(final String springBeanName)
    {
        return BackofficeSpringUtil.getBean(springBeanName);
    }


    private void createCsvHeader(final StringBuilder builder, final Pageable pageable, final List<ListColumn> columns,
                    final String csvDelimiter)
    {
        for(final ListColumn listColumn : columns)
        {
            final String columnHeaderLabel = UILabelUtil.getColumnHeaderLabel(listColumn, pageable.getTypeCode(), labelService);
            builder.append(wrapHeaderForCSV(escapeForCSV(columnHeaderLabel, csvDelimiter))).append(csvDelimiter);
        }
        builder.append('\n');
    }


    void createCsvContent(final StringBuilder builder, final Pageable pageable, final List<ListColumn> columns,
                    final String csvDelimiter)
    {
        for(final Object object : pageable.getAllResults())
        {
            try
            {
                final String dataTypeCode = getTypeFacade().getType(object);
                final DataType dataType = StringUtils.isBlank(dataTypeCode) ? null : getTypeFacade().load(dataTypeCode);
                for(final ListColumn listColumn : columns)
                {
                    final String stringValue = getLabel(object, dataType, listColumn);
                    builder.append(escapeForCSV(stringValue, csvDelimiter)).append(csvDelimiter);
                }
            }
            catch(final TypeNotFoundException tnf)
            {
                LOG.warn("Could not find type", tnf);
            }
            builder.append('\n');
        }
    }


    private String getLabel(final Object object, final DataType dataType, final ListColumn listColumn)
    {
        String stringValue = StringUtils.EMPTY;
        final String qualifier = listColumn.getQualifier();
        if(!canReadProperty(object, qualifier))
        {
            return getLabelService().getAccessDeniedLabel(object);
        }
        if(StringUtils.isNotBlank(listColumn.getSpringBean()))
        {
            final Object springBean = getSpringBean(listColumn.getSpringBean());
            if(springBean instanceof CsvAwareListViewRenderer)
            {
                return ((CsvAwareListViewRenderer)springBean).getCsvValue(object, listColumn);
            }
        }
        final Object value = getPropertyValueService().readValue(object, qualifier);
        final DataAttribute attribute = dataType != null ? dataType.getAttribute(qualifier) : null;
        if(attribute != null && attribute.isEncrypted())
        {
            return getLabelService().getAccessDeniedLabel(object);
        }
        if(isAttributeReference(attribute))
        {
            stringValue = getLabel(qualifier, value);
        }
        if(value instanceof HashMap)
        {
            final Locale currentLocale = cockpitLocaleService.getCurrentLocale();
            final String localizedValue = (String)((HashMap)value).get(currentLocale);
            stringValue = StringUtils.defaultIfBlank(localizedValue, StringUtils.EMPTY);
        }
        else if(StringUtils.isBlank(stringValue))
        {
            stringValue = value == null ? StringUtils.EMPTY : value.toString();
        }
        return stringValue;
    }


    private boolean isAttributeReference(final DataAttribute attribute)
    {
        return attribute == null || attribute.getValueType() == null || !attribute.getValueType().isAtomic();
    }


    private String getLabel(final String qualifier, final Object value)
    {
        String stringValue = StringUtils.EMPTY;
        try
        {
            stringValue = getLabelService().getObjectLabel(value);
        }
        catch(final Exception e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Could not get value for field '" + qualifier + "'. Using string representation instead.", e);
            }
        }
        return stringValue;
    }


    private boolean canReadProperty(final Object type, final String qualifier)
    {
        try
        {
            return getPermissionFacade().canReadInstanceProperty(type, qualifier);
        }
        catch(final Exception exc)
        {
            LOG.warn("Could not check assigned permissions", exc);
        }
        return false;
    }


    private String wrapHeaderForCSV(final String header)
    {
        return String.format("\"%s\"", header);
    }


    /**
     * @deprecated since 2011
     */
    @Deprecated(since = "2011", forRemoval = true)
    protected String escapeForCSV(final String value)
    {
        return escapeForCSV(value, CSV_DELIMITER);
    }


    protected String escapeForCSV(final String value, final String csvDelimiter)
    {
        if(StringUtils.isBlank(value))
        {
            return value;
        }
        String ret = sanitizeInput(value);
        ret = ret.replace('\n', ' ');
        if(ret.contains("\""))
        {
            ret = ret.replace("\"", "\"\"");
        }
        if(ret.contains(csvDelimiter) || ret.contains("\""))
        {
            ret = "\"" + ret + "\"";
        }
        return ret;
    }


    /**
     * The main purpose of this method is to sanitize the given payload in a way that will allow CSV processing tools - like
     * Excel - to open it without the risk of malicious script being activated.
     *
     * @param payload
     *           text to be placed in the final SCV file
     * @return sanitized version of payload
     */
    protected String sanitizeInput(final String payload)
    {
        final String result;
        if(Boolean.parseBoolean(getCockpitProperties().getProperty(COCKPITNG_EXPORT_CSV_ACTION_INPUT_SANITIZATION_ENABLED))
                        && payloadStartsWithFormulaCharacter(payload))
        {
            final String mode = StringUtils.defaultIfBlank(
                            getCockpitProperties().getProperty(COCKPITNG_EXPORT_CSV_ACTION_INPUT_SANITIZATION_MODE), SANITIZATION_MODE_TRIM);
            switch(mode)
            {
                case SANITIZATION_MODE_TRIM:
                    result = sanitizationTrim(payload);
                    break;
                case SANITIZATION_MODE_PREFIX:
                    result = sanitizationPrefix(payload);
                    break;
                default:
                    LOG.warn("Non-matching input sanitization mode specified: {}. Using default: \"{}\".", mode,
                                    SANITIZATION_MODE_TRIM);
                    result = sanitizationTrim(payload);
                    break;
            }
        }
        else
        {
            result = payload;
        }
        return result;
    }


    private String sanitizationPrefix(final String payload)
    {
        final String prefix = StringUtils.defaultIfBlank(
                        getCockpitProperties().getProperty(COCKPITNG_EXPORT_CSV_ACTION_INPUT_SANITIZATION_PREFIX),
                        DEFAULT_SANITIZATION_PREFIX);
        return prefix + payload;
    }


    private String sanitizationTrim(final String payload)
    {
        String result;
        result = payload;
        do
        {
            result = result.substring(1);
        }
        while(payloadStartsWithFormulaCharacter(result));
        return result;
    }


    protected boolean payloadStartsWithFormulaCharacter(final String result)
    {
        return StringUtils.isNotEmpty(result) && ArrayUtils.contains(FORMULA_CHARACTERS, result.charAt(0));
    }


    @Override
    public boolean canPerform(final ActionContext<Map> ctx)
    {
        final Map data = ctx.getData();
        if(data == null)
        {
            return false;
        }
        final Object pageable = data.get(MODEL_PAGEABLE);
        final Object listView = data.get(MODEL_COLUMNS_CONFIG);
        return pageable instanceof Pageable && listView instanceof ListView;
    }


    @Override
    public boolean needsConfirmation(final ActionContext<Map> ctx)
    {
        final Map data = ctx.getData();
        if(data == null)
        {
            return false;
        }
        final Pageable pageable = (Pageable)data.get(MODEL_PAGEABLE);
        final int confirmationThreshold = getConfirmationThreshold(ctx);
        return confirmationThreshold > 0 && pageable.getTotalCount() > confirmationThreshold;
    }


    @Override
    public String getConfirmationMessage(final ActionContext<Map> ctx)
    {
        final Pageable pageable = (Pageable)(ctx.getData()).get(MODEL_PAGEABLE);
        return ctx.getLabel(EXPORT_CSV_CONFIRMATION, new Object[]
                        {pageable.getTotalCount(), getConfirmationThreshold(ctx)});
    }


    private int getConfirmationThreshold(final ActionContext<Map> ctx)
    {
        final Object parameter = ctx.getParameter(CONFIRMATION_THRESHOLD);
        if(parameter instanceof Integer)
        {
            return ((Integer)parameter).intValue();
        }
        if(parameter instanceof String)
        {
            try
            {
                return Integer.parseInt((String)parameter);
            }
            catch(final NumberFormatException nfe)
            {
                LOG.warn(String.format("Invalid integer [%s]", parameter), nfe);
            }
        }
        return -1;
    }


    public LabelService getLabelService()
    {
        return labelService;
    }


    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }


    public PermissionFacade getPermissionFacade()
    {
        return permissionFacade;
    }


    public void setPermissionFacade(final PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }


    public PropertyValueService getPropertyValueService()
    {
        return propertyValueService;
    }


    public void setPropertyValueService(final PropertyValueService propertyValueService)
    {
        this.propertyValueService = propertyValueService;
    }


    public TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    public CockpitLocaleService getCockpitLocaleService()
    {
        return cockpitLocaleService;
    }


    public void setCockpitLocaleService(final CockpitLocaleService cockpitLocaleService)
    {
        this.cockpitLocaleService = cockpitLocaleService;
    }


    public CockpitProperties getCockpitProperties()
    {
        return cockpitProperties;
    }


    public void setCockpitProperties(final CockpitProperties cockpitProperties)
    {
        this.cockpitProperties = cockpitProperties;
    }
}
