package de.hybris.platform.impex.jalo.imp;

import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.jalo.header.AbstractColumnDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.impex.jalo.header.UnresolvedValueException;
import de.hybris.platform.impex.model.ExternalImportKeyModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.MapType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.CSVCellDecorator;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.StandardSearchResult;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public class DefaultExistingItemResolver implements ExistingItemResolver
{
    private static final Logger LOG = Logger.getLogger(DefaultExistingItemResolver.class);
    @Deprecated(since = "ages", forRemoval = false)
    protected static final boolean MYSQL_CASE_SENSITIVE_WORKAROUND = false;
    protected final boolean isMySQl = Config.isMySQLUsed();
    private FlexibleSearch flexibleSearch;


    public void notifyItemCreatedOrRemoved(ValueLineTranslator valueLineTranslator, ValueLine line)
    {
        createLookupTableEntryIfNeeded(line);
    }


    protected void createLookupTableEntryIfNeeded(ValueLine valueLine)
    {
        for(AbstractColumnDescriptor acd : valueLine.getHeader().getColumns())
        {
            if(shouldCreateLookupTableEntry(acd))
            {
                createLookupTableEntry(valueLine, acd);
                break;
            }
        }
    }


    protected boolean shouldCreateLookupTableEntry(AbstractColumnDescriptor acd)
    {
        if(acd == null)
        {
            return false;
        }
        CSVCellDecorator csvCellDecorator = acd.getCSVCellDecorator();
        return (csvCellDecorator instanceof de.hybris.platform.impex.jalo.header.ExternalImportKeyCellDecorator && acd.getDescriptorData() != null && acd
                        .getDescriptorData().getModifier("sourceSystemId") != null);
    }


    protected void createLookupTableEntry(ValueLine valueLine, AbstractColumnDescriptor acd)
    {
        ModelService modelService = (ModelService)Registry.getApplicationContext().getBean("modelService", ModelService.class);
        ExternalImportKeyModel externalImportKey = (ExternalImportKeyModel)modelService.create(ExternalImportKeyModel.class);
        externalImportKey.setSourceKey(valueLine.getValueEntry(acd.getValuePosition()).getCellValue()
                        .substring("<ignore>".length()));
        externalImportKey.setTargetPK(valueLine.getProcessedItemPK());
        externalImportKey.setSourceSystemID(
                        String.valueOf(acd.getDescriptorData().getModifier("sourceSystemId")));
        modelService.save(externalImportKey);
    }


    protected FlexibleSearch getFlexibleSearch()
    {
        if(this.flexibleSearch == null)
        {
            this.flexibleSearch = FlexibleSearch.getInstance();
        }
        return this.flexibleSearch;
    }


    public Collection<Item> findExisting(ValueLineTranslator valueLineTranslator, ValueLine line) throws InsufficientDataException, UnresolvedValueException, AmbiguousItemException
    {
        Collection<Item> ret = Collections.EMPTY_LIST;
        if(line.getProcessedItemPK() != null)
        {
            ret = resolveByPK(line);
        }
        else
        {
            ret = resolveByQuery(line, createQueryParameters(valueLineTranslator, line));
        }
        return ret;
    }


    protected Collection<Item> resolveByPK(ValueLine line)
    {
        return Collections.singletonList(JaloSession.getCurrentSession().getItem(line.getProcessedItemPK()));
    }


    protected QueryParameters createQueryParameters(ValueLineTranslator valueLineTranslator, ValueLine line) throws InsufficientDataException, UnresolvedValueException
    {
        HeaderDescriptor header = line.getHeader();
        ComposedType headerType = line.getHeader().getDefaultComposedType();
        Set<StandardColumnDescriptor> uniqueColumns = header.getUniqueAttributeColumns(line.getComposedType());
        if(!uniqueColumns.isEmpty() && headerType.isSingleton())
        {
            StringBuilder attributes = new StringBuilder();
            attributes.append("[");
            for(StandardColumnDescriptor attribute : uniqueColumns)
            {
                attributes.append(attribute.getQualifier()).append(",");
            }
            attributes.delete(attributes.length() - 1, attributes.length());
            attributes.append("]");
            LOG.warn("\"unique\" modifier/s of attribute/s " + attributes.toString() + " for type \"" + headerType.getCode() + "\" will be ignored because type is a singleton. Please do not use the \"unique\" modifier when processing singleton types, even in update mode.");
            uniqueColumns = Collections.EMPTY_SET;
        }
        ComposedType composedType = line.getHeader().getConfiguredComposedType();
        return new QueryParameters(composedType
                        .getCode(), composedType
                        .isSingleton(), uniqueColumns,
                        uniqueColumns.isEmpty() ? Collections.EMPTY_MAP : translateUniqueKeys(valueLineTranslator, line, uniqueColumns));
    }


    protected Collection<Item> resolveByQuery(ValueLine line, QueryParameters queryParameters) throws InsufficientDataException, UnresolvedValueException, AmbiguousItemException
    {
        Collection<Item> ret = Collections.EMPTY_LIST;
        Set<StandardColumnDescriptor> uniqueColumns = queryParameters.getUniqueColumns();
        if(!uniqueColumns.isEmpty() || queryParameters.isSingleton())
        {
            HeaderDescriptor header = line.getHeader();
            StringBuilder stringBuilder = new StringBuilder();
            Map<Object, Object> searchValues = new HashMap<>();
            ret = filterResultByNonSearchableColumns(
                            searchItems(queryParameters.getTypeCode(), queryParameters, stringBuilder, (Map)searchValues, false, "true"
                                            .equalsIgnoreCase(header
                                                            .getDescriptorData()
                                                            .getModifier("ignoreKeyCase"))), queryParameters);
            if(ret.size() > 1 && !header.isBatchMode())
            {
                throw new AmbiguousItemException(TypeManager.getInstance().getComposedType(queryParameters.getTypeCode()), ret, stringBuilder
                                .toString(), searchValues);
            }
        }
        return ret;
    }


    protected List<Item> filterResultByNonSearchableColumns(List<Item> result, QueryParameters queryParameters)
    {
        List<Item> ret = result;
        if(!result.isEmpty())
        {
            Set<StandardColumnDescriptor> nonSearchableColumns = queryParameters.getNonSearchableColumns();
            if(!nonSearchableColumns.isEmpty())
            {
                Object object = new Object(this, nonSearchableColumns);
                ret = new ArrayList<>(result.size());
                try
                {
                    for(Item i : result)
                    {
                        Map currentValues = i.getAllAttributes(JaloSession.getCurrentSession().getSessionContext(), (Item.AttributeFilter)object);
                        Map<StandardColumnDescriptor, Object> valueMappings = queryParameters.getUniqueValues();
                        for(StandardColumnDescriptor cd : nonSearchableColumns)
                        {
                            Object searchValue = valueMappings.get(cd);
                            Object currentValue = currentValues.get(cd.getQualifier());
                            if(searchValue != currentValue)
                            {
                                if(searchValue != null)
                                {
                                    if(!searchValue.equals(currentValue))
                                    {
                                        continue;
                                    }
                                    continue;
                                }
                                if(LOG.isDebugEnabled())
                                {
                                    LOG.debug("filtered item " + i.getPK()
                                                    .toString() + " due to non-searchable attribute mismatch: '" + cd
                                                    .getQualifier() + "' expected " + searchValue + " but got " + currentValue);
                                }
                            }
                        }
                        ret.add(i);
                    }
                }
                catch(JaloSecurityException e)
                {
                    throw new JaloSystemException(e);
                }
            }
        }
        return ret;
    }


    protected List<Item> searchItems(String typeCode, QueryParameters queryParameters, StringBuilder stringBuilder, Map<String, Object> searchValues, boolean exactType, boolean caseInsensitive) throws InsufficientDataException, UnresolvedValueException
    {
        Set<StandardColumnDescriptor> searchableColumns = queryParameters.getSearchableColumns();
        stringBuilder.append("SELECT {").append(Item.PK).append("} FROM {").append(typeCode).append(exactType ? "!" : "")
                        .append("}");
        if(!searchableColumns.isEmpty())
        {
            stringBuilder.append(" WHERE ");
            int index = 0;
            Map<StandardColumnDescriptor, Object> valueMappings = queryParameters.getUniqueValues();
            for(StandardColumnDescriptor cd : searchableColumns)
            {
                String qualifier = cd.getQualifier();
                Object searchValue = valueMappings.get(cd);
                if(index > 0)
                {
                    stringBuilder.append(" AND ");
                }
                index++;
                if(searchValue == null)
                {
                    stringBuilder.append("{").append(qualifier).append("} IS NULL");
                    continue;
                }
                boolean isString = false;
                String langQualifier = null;
                if(cd.isLocalized())
                {
                    isString = String.class.getName().equalsIgnoreCase(((MapType)cd
                                    .getAttributeDescriptor().getRealAttributeType()).getReturnType().getCode());
                    if(cd.getLanguageIso() != null)
                    {
                        try
                        {
                            Language language = cd.getLanguage();
                            langQualifier = language.getIsoCode();
                        }
                        catch(HeaderValidationException e)
                        {
                            throw new UnresolvedValueException(e.getMessage());
                        }
                    }
                }
                else
                {
                    isString = "java.lang.String".equalsIgnoreCase(cd
                                    .getAttributeDescriptor().getRealAttributeType().getCode());
                }
                stringBuilder.append((caseInsensitive && isString) ? " LOWER(" : "").append("{").append(qualifier);
                if(langQualifier != null)
                {
                    stringBuilder.append("[" + langQualifier + "]");
                }
                stringBuilder.append("}").append((caseInsensitive && isString) ? ")" : "");
                stringBuilder.append(this.isMySQl ? " = BINARY " : " = ");
                stringBuilder.append((caseInsensitive && isString) ? " LOWER( ?" : " ?")
                                .append(qualifier + qualifier)
                                .append((caseInsensitive && isString) ? " ) " : " ");
                searchValues.put(qualifier + qualifier, searchValue);
            }
        }
        StandardSearchResult<Item> searchResult = (StandardSearchResult<Item>)getFlexibleSearch().search(stringBuilder.toString(), searchValues, Item.class);
        List<Item> ret = searchResult.getResult();
        if(LOG.isDebugEnabled())
        {
            LOG.debug("existing query = " + stringBuilder.toString() + ", values = " + searchValues + ", found " + ret.size() + " items");
        }
        return ret;
    }


    protected Map<StandardColumnDescriptor, Object> translateUniqueKeys(ValueLineTranslator valueLineTranslator, ValueLine line, Set<StandardColumnDescriptor> uniqueColumns) throws UnresolvedValueException, InsufficientDataException
    {
        Map<StandardColumnDescriptor, Object> valueMappings = valueLineTranslator.translateColumnValues(uniqueColumns, line, null);
        Collection<StandardColumnDescriptor> unresolved = line.getUnresolved(uniqueColumns);
        if(!unresolved.isEmpty())
        {
            StringBuffer stringBuffer = new StringBuffer();
            for(StandardColumnDescriptor column : unresolved)
            {
                stringBuffer.append("column='").append(column.getQualifier()).append("' ");
                stringBuffer.append("value='").append(line.getValueEntry(column.getValuePosition()).getCellValue()).append("', ");
            }
            throw new UnresolvedValueException(stringBuffer.toString());
        }
        return valueMappings;
    }
}
