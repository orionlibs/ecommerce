package de.hybris.platform.impex.jalo.imp;

import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.impex.jalo.header.UnresolvedValueException;
import de.hybris.platform.impex.jalo.translators.AbstractValueTranslator;
import de.hybris.platform.impex.jalo.translators.ItemExpressionTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class DefaultValueLineTranslator implements ValueLineTranslator
{
    private static final Logger LOG = Logger.getLogger(DefaultValueLineTranslator.class);
    private final EnumerationValue relaxedMode = ImpExManager.getImportRelaxedMode();


    public Map<StandardColumnDescriptor, Object> translateColumnValues(Collection<StandardColumnDescriptor> columnDescriptors, ValueLine valueLine, Item existing) throws InsufficientDataException
    {
        if(!columnDescriptors.isEmpty())
        {
            boolean combined = valueLine.getHeader().getReader().isCombinedSearchEnabled();
            Map<StandardColumnDescriptor, Object> ret = new HashMap<>(columnDescriptors.size());
            Map<StandardColumnDescriptor, ItemExpressionTranslator.CombinedSearchResult> preloadedResults = null;
            if(combined && columnDescriptors.size() > 1)
            {
                List<ItemExpressionTranslator> translators = null;
                List<String> expressions = null;
                List<StandardColumnDescriptor> columns2fetch = null;
                for(StandardColumnDescriptor cd : columnDescriptors)
                {
                    boolean virtual = cd.isVirtual();
                    ValueLine.ValueEntry valueEntry = virtual ? null : valueLine.getValueEntry(cd.getValuePosition());
                    if(valueEntry == null || !valueEntry.isIgnore())
                    {
                        AbstractValueTranslator trans = cd.getValueTranslator();
                        if(trans instanceof ItemExpressionTranslator && !(trans instanceof de.hybris.platform.impex.jalo.translators.AlternativeExpressionTranslator))
                        {
                            if(!virtual && (valueEntry == null || valueEntry.isUnresolved()))
                            {
                                if(translators == null)
                                {
                                    translators = new ArrayList<>(columnDescriptors.size());
                                    expressions = new ArrayList<>(columnDescriptors.size());
                                    columns2fetch = new ArrayList<>(columnDescriptors.size());
                                }
                                translators.add((ItemExpressionTranslator)trans);
                                expressions.add((valueEntry != null) ? valueEntry.getCellValue() : null);
                                columns2fetch.add(cd);
                            }
                        }
                    }
                }
                if(translators != null && translators.size() > 1)
                {
                    preloadedResults = new HashMap<>(translators.size());
                    int index = 0;
                    for(ItemExpressionTranslator.CombinedSearchResult res : ItemExpressionTranslator.convertAllToJalo(existing, translators, expressions))
                    {
                        preloadedResults.put(columns2fetch.get(index), res);
                        index++;
                    }
                }
            }
            for(StandardColumnDescriptor cd : columnDescriptors)
            {
                boolean virtual = cd.isVirtual();
                ValueLine.ValueEntry valueEntry = virtual ? null : valueLine.getValueEntry(cd.getValuePosition());
                if(valueEntry == null || !valueEntry.isIgnore())
                {
                    try
                    {
                        Object value;
                        if(valueEntry != null && !valueEntry.isUnresolved())
                        {
                            value = valueEntry.getTranslatedValue();
                        }
                        else if(virtual)
                        {
                            value = cd.getDefaultValue();
                        }
                        else
                        {
                            ItemExpressionTranslator.CombinedSearchResult res = (combined && preloadedResults != null) ? preloadedResults.get(cd) : null;
                            if(res != null)
                            {
                                if(res.isUnresolved())
                                {
                                    throw new UnresolvedValueException("could not resolve item for " + (
                                                    (valueEntry != null) ? valueEntry.getCellValue() : "empty cell"));
                                }
                                Object element = res.getElement();
                                if(element == null && res.isEmpty() && cd.hasDefaultValueDefinition())
                                {
                                    if(cd.isPartOf())
                                    {
                                        element = cd.calculateDefaultValue(existing);
                                    }
                                    else
                                    {
                                        element = cd.getDefaultValue();
                                    }
                                }
                                else
                                {
                                    element = res.getElement();
                                }
                                value = element;
                            }
                            else
                            {
                                value = cd.importValue((valueEntry != null) ? valueEntry.getCellValue() : null, existing);
                            }
                        }
                        if(valueEntry != null)
                        {
                            valueEntry.resolve(value);
                        }
                        else if(!virtual)
                        {
                            valueEntry = valueLine.resolveMissingEntry(cd.getValuePosition(), value);
                        }
                        if(ret.get(cd) != null)
                        {
                            throw new IllegalStateException("duplicate attribute value '" + cd
                                            .getQualifier() + "' detected - found " + ret
                                            .get(cd));
                        }
                        ret.put(cd, value);
                    }
                    catch(UnresolvedValueException e)
                    {
                        if(LOG.isDebugEnabled())
                        {
                            LOG.debug(e.getMessage() + " - marking cell unresolved for second pass", (Throwable)e);
                        }
                        if(virtual)
                        {
                            throw new IllegalStateException("virtual column did not provide value : " + e.getMessage(), e);
                        }
                        if(valueEntry != null)
                        {
                            valueEntry.markUnresolved(e.getMessage());
                        }
                        else
                        {
                            valueEntry = valueLine.unresolveMissingEntry(cd.getValuePosition());
                            valueEntry.markUnresolved(e.getMessage());
                        }
                    }
                }
                if(!valueLine.getHeader().getReader().getValidationMode().equals(this.relaxedMode) && ret.get(cd) == null && (valueEntry == null ||
                                !valueEntry.isUnresolved()) && (valueEntry == null ||
                                !valueEntry.isIgnore()) && cd
                                .isMandatory() && !cd.isAllowNull() && cd.getAttributeDescriptor()
                                .getDefaultValue() == null)
                {
                    throw new InsufficientDataException(valueLine, (
                                    valueEntry != null && valueEntry.isUnresolved()) ? ("could not resolve value for mandatory or initial-only attribute " +
                                    cd.getHeader().getTypeCode() + "." + cd.getQualifier() + " : value was '" + valueEntry.getCellValue() + "'") : ("value is NULL for mandatory attribute " +
                                    cd.getHeader().getTypeCode() + "." + cd.getQualifier()), 1);
                }
            }
            return ret;
        }
        return Collections.EMPTY_MAP;
    }
}
