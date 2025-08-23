package de.hybris.platform.impex.distributed.batch.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.distributed.batch.ImportDataDumpStrategy;
import de.hybris.platform.impex.jalo.AbstractCodeLine;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.impex.jalo.imp.DefaultValueLineTranslator;
import de.hybris.platform.impex.jalo.imp.InsufficientDataException;
import de.hybris.platform.impex.jalo.imp.ValueLine;
import de.hybris.platform.impex.jalo.imp.ValueLineTranslator;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.impex.ProcessMode;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BatchData
{
    private static final Logger LOG = LoggerFactory.getLogger(BatchData.class);
    private final ImportDataDumpStrategy dumpStrategy;
    private final AbstractCodeLine beforeEachExpr;
    private final AbstractCodeLine afterEachExpr;
    private final ImportBatchParser.BatchingImpexReader reader;
    private final HeaderDescriptor headerDescriptor;
    private final ProcessMode processMode;
    private final List<ValueLine> valueLines;
    private final List<ImportData> importData;
    private List<ImportData> unresolved;
    private Set<StandardColumnDescriptor> descriptorsForCurrentMode;
    private final ValueLineTranslator valueLineTranslator;
    private Map<Integer, Map<String, Object>> translationCache;
    private boolean skipAfterEachProcessing = false;


    public BatchData(ImportDataDumpStrategy dumpStrategy, ImportBatchParser.BatchingImpexReader reader, HeaderDescriptor headerDescriptor, List<ValueLine> valueLines)
    {
        Preconditions.checkNotNull(dumpStrategy, "dumpStrategy must not be null");
        Preconditions.checkNotNull(headerDescriptor, "headerDescruotir must not be null");
        Preconditions.checkState(!headerDescriptor.getDefaultComposedType().isSingleton(), "Batching ImpEx does not support singleton types");
        this.dumpStrategy = dumpStrategy;
        this.headerDescriptor = headerDescriptor;
        this.reader = reader;
        this.beforeEachExpr = reader.getBeforeEachCode();
        this.afterEachExpr = reader.getAfterEachExpr();
        this.processMode = determineProcessMode(headerDescriptor);
        this.valueLines = valueLines;
        this.importData = new ArrayList<>();
        this.valueLineTranslator = (ValueLineTranslator)new DefaultValueLineTranslator();
        resolveValues();
        LOG.info("BatchData created. Current mode: {}; Type: {}; Effective batch size: {}", new Object[] {getProcessMode(), headerDescriptor
                        .getTypeCode(), Integer.valueOf(this.importData.size())});
    }


    private ProcessMode determineProcessMode(HeaderDescriptor descriptor)
    {
        if(descriptor.isRemoveMode())
        {
            this.descriptorsForCurrentMode = Collections.emptySet();
            return ProcessMode.REMOVE;
        }
        if(descriptor.isUpdateMode())
        {
            this.descriptorsForCurrentMode = descriptor.getColumnsForUpdate(descriptor.getTypeCode());
            return ProcessMode.UPDATE;
        }
        if(descriptor.isInsertMode())
        {
            this.descriptorsForCurrentMode = descriptor.getColumnsForCreation(descriptor.getTypeCode());
            return ProcessMode.INSERT;
        }
        if(descriptor.isInsertUpdateMode())
        {
            Set<StandardColumnDescriptor> columnsForCreation = descriptor.getColumnsForCreation(descriptor.getTypeCode());
            Set<StandardColumnDescriptor> columnsForUpdate = descriptor.getColumnsForUpdate(descriptor.getTypeCode());
            this.descriptorsForCurrentMode = new HashSet<>();
            this.descriptorsForCurrentMode.addAll(columnsForCreation);
            this.descriptorsForCurrentMode.addAll(columnsForUpdate);
            return ProcessMode.INSERT_UPDATE;
        }
        throw new IllegalStateException("Import mode for current Batch not known [headerDecriptor: " + this.headerDescriptor + "]");
    }


    public List<ImportData> getResolvedLines()
    {
        return (List<ImportData>)this.importData.stream().filter(d -> (d.getExistingItemPk() != null && d.isNotUnrecoverable())).collect(Collectors.toList());
    }


    public void processAfterEachExpressions()
    {
        if(this.afterEachExpr == null || isSkipAfterEachProcessing())
        {
            return;
        }
        getResolvedLines().stream().filter(this::isAllowedForAfterEachExecution)
                        .forEach(data -> this.reader.execute(this.afterEachExpr, data.getValueLine().dump()));
    }


    private boolean isAllowedForAfterEachExecution(ImportData data)
    {
        return (data.getExistingItemPk() != null && data.isNotUnrecoverable() && !data.isUnresolved());
    }


    public void setSkipAfterEachProcessing()
    {
        this.skipAfterEachProcessing = true;
    }


    public boolean isSkipAfterEachProcessing()
    {
        return this.skipAfterEachProcessing;
    }


    public String dumpUnresolvedLines()
    {
        if(this.valueLines.isEmpty())
        {
            return "";
        }
        StringBuilder sb = new StringBuilder(this.dumpStrategy.dump(this.headerDescriptor));
        if(this.beforeEachExpr != null)
        {
            sb.append(this.dumpStrategy.dump(this.beforeEachExpr));
        }
        if(this.afterEachExpr != null)
        {
            sb.append(this.dumpStrategy.dump(this.afterEachExpr));
        }
        sb.append(this.dumpStrategy.dump((List)this.unresolved.stream().map(ImportData::getValueLine).collect(Collectors.toList())));
        return sb.toString();
    }


    public boolean hasUnresolvedLines()
    {
        return CollectionUtils.isNotEmpty(this.unresolved);
    }


    public long getRemainingWorkLoad()
    {
        return this.unresolved.stream().mapToLong(ImportData::getRemainingWorkLoad).sum();
    }


    public Integer getNumUnresolvedLines()
    {
        return (this.unresolved == null) ? Integer.valueOf(0) : Integer.valueOf(this.unresolved.size());
    }


    public void addUnresolved(List<ImportData> unresolved)
    {
        if(CollectionUtils.isEmpty(unresolved))
        {
            return;
        }
        if(this.unresolved == null)
        {
            this.unresolved = new ArrayList<>();
        }
        this.unresolved.addAll(unresolved);
    }


    public HeaderDescriptor getHeaderDescriptor()
    {
        return this.headerDescriptor;
    }


    public Set<StandardColumnDescriptor> getUniqueColumns()
    {
        String composedTypeCode = this.headerDescriptor.getConfiguredComposedTypeCode();
        return this.headerDescriptor.getUniqueAttributeColumns(composedTypeCode);
    }


    public List<ImportData> getImportData()
    {
        return (List<ImportData>)ImmutableList.builder().addAll(this.importData).build();
    }


    public ProcessMode getProcessMode()
    {
        return this.processMode;
    }


    private void resolveValues()
    {
        Set<StandardColumnDescriptor> uniqueColumnDescriptors = this.headerDescriptor.getUniqueAttributeColumns(this.headerDescriptor.getConfiguredComposedType());
        this.translationCache = new HashMap<>();
        this.valueLines.stream().filter(this::isValidValueLine).forEach(valueLine -> {
            SortedMap<StandardColumnDescriptor, Object> uniqueTranslations = new TreeMap<>();
            Map<StandardColumnDescriptor, Object> currentModeTranlations = new HashMap<>();
            boolean gotTranslationException = false;
            String translationExceptionMessage = null;
            try
            {
                translateValues(valueLine, uniqueColumnDescriptors, uniqueTranslations);
                adjustUniqueTranslations(uniqueTranslations);
                translateValues(valueLine, this.descriptorsForCurrentMode, currentModeTranlations);
            }
            catch(SystemException e)
            {
                gotTranslationException = true;
                translationExceptionMessage = getExceptionMessage(e);
            }
            ImportData importData = new ImportData(this, valueLine, this.descriptorsForCurrentMode, uniqueTranslations, currentModeTranlations);
            if(gotTranslationException)
            {
                importData.markUnrecoverable(translationExceptionMessage);
            }
            this.importData.add(importData);
        });
    }


    private static String getExceptionMessage(SystemException e)
    {
        return (e.getCause() == null) ? e.getMessage() : e.getCause().getMessage();
    }


    private void adjustUniqueTranslations(SortedMap<StandardColumnDescriptor, Object> uniqueTranslations)
    {
        uniqueTranslations.entrySet().stream().filter(entry -> (((StandardColumnDescriptor)entry.getKey()).isLocalized() && entry.getValue() instanceof Map))
                        .forEach(entry -> {
                            Map locValue = (Map)entry.getValue();
                            Preconditions.checkState((locValue.size() <= 1), "Expected map containing no more than one element");
                            Object value = locValue.isEmpty() ? null : locValue.values().iterator().next();
                            entry.setValue(value);
                        });
    }


    private void translateValues(ValueLine valueLine, Set<StandardColumnDescriptor> columnDescriptors, Map<StandardColumnDescriptor, Object> translations)
    {
        for(StandardColumnDescriptor descr : columnDescriptors)
        {
            ValueLine.ValueEntry valueEntry = valueLine.getValueEntry(descr.getValuePosition());
            if(valueEntry == null || valueEntry.isIgnore())
            {
                continue;
            }
            Optional<Object> result = translate(valueLine, descr);
            if(result.isPresent())
            {
                translations.put(descr, toModelIfRequired(result.get()));
                continue;
            }
            if(!valueEntry.isUnresolved())
            {
                translations.put(descr, null);
            }
        }
    }


    private static Object toModelIfRequired(Object value)
    {
        if(value instanceof de.hybris.platform.jalo.Item)
        {
            return getModelService().toModelLayer(value);
        }
        return value;
    }


    private static ModelService getModelService()
    {
        return (ModelService)Registry.getApplicationContext().getBean("modelService", ModelService.class);
    }


    private boolean isValidValueLine(ValueLine valueLine)
    {
        try
        {
            valueLine.getComposedType();
            return true;
        }
        catch(InsufficientDataException e)
        {
            LOG.error("Invalid value line {} [reason: {}]", valueLine, e.getMessage());
            return false;
        }
    }


    private Optional<Object> translate(ValueLine valueLine, StandardColumnDescriptor descr)
    {
        int valuePosition = descr.getValuePosition();
        ValueLine.ValueEntry valueEntry = valueLine.getValueEntry(valuePosition);
        Optional<Object> translatedValue = tryGetTranslatedValue(descr.getValuePosition(), valueEntry.getCellValue());
        if(translatedValue.isPresent())
        {
            valueEntry.resolve(translatedValue.get());
            return translatedValue;
        }
        Optional<Object> translated = translateValue(valueLine, descr);
        if(translated.isPresent())
        {
            putValuesIntoTranslationCache(valuePosition, valueEntry.getCellValue(), translated.get());
        }
        return translated;
    }


    private Optional<Object> tryGetTranslatedValue(int valuePosition, String cellValue)
    {
        Map<String, Object> translatedValues = this.translationCache.get(Integer.valueOf(valuePosition));
        if(translatedValues == null)
        {
            return Optional.empty();
        }
        Object value = translatedValues.get(cellValue);
        return (value == null) ? Optional.<Object>empty() : Optional.<Object>of(value);
    }


    private Optional<Object> translateValue(ValueLine valueLine, StandardColumnDescriptor descriptor)
    {
        try
        {
            Object value;
            Map<StandardColumnDescriptor, Object> result = this.valueLineTranslator.translateColumnValues(Sets.newHashSet((Object[])new StandardColumnDescriptor[] {descriptor}, ), valueLine, null);
            Collection<StandardColumnDescriptor> unresolved = valueLine.getUnresolved(Sets.newHashSet((Object[])new StandardColumnDescriptor[] {descriptor}));
            if(CollectionUtils.isNotEmpty(unresolved))
            {
                HeaderDescriptor header = valueLine.getHeader();
                if(header.isRemoveMode())
                {
                    ValueLine.ValueEntry valueEntry = valueLine.getValueEntry(descriptor.getValuePosition());
                    if(valueEntry != null && valueEntry.isUnresolved())
                    {
                        valueEntry.resolve("<unresolved - ignored for removal>");
                    }
                }
                return Optional.empty();
            }
            if(descriptor.isLocalized())
            {
                HashMap<Locale, Object> localizedValue = new HashMap<>();
                localizedValue.put(getLocaleForIsoCode(descriptor.getLanguageIsoCode()), result.get(descriptor));
                value = localizedValue;
            }
            else
            {
                value = result.get(descriptor);
            }
            return (value == null) ? Optional.<Object>empty() : Optional.<Object>of(value);
        }
        catch(Exception e)
        {
            throw new SystemException(e);
        }
    }


    private Locale getLocaleForIsoCode(String isoCode)
    {
        return StringUtils.isEmpty(isoCode) ? getI18NService().getCurrentLocale() :
                        getCommonI18NService().getLocaleForIsoCode(isoCode);
    }


    CommonI18NService getCommonI18NService()
    {
        return (CommonI18NService)Registry.getApplicationContext().getBean("commonI18NService", CommonI18NService.class);
    }


    I18NService getI18NService()
    {
        return (I18NService)Registry.getApplicationContext().getBean("i18NService", I18NService.class);
    }


    private void putValuesIntoTranslationCache(int valuePosition, String cellValue, Object translatedValue)
    {
        Map<String, Object> translatedValues = this.translationCache.get(Integer.valueOf(valuePosition));
        if(translatedValues == null)
        {
            translatedValues = new HashMap<>();
            this.translationCache.put(Integer.valueOf(valuePosition), translatedValues);
        }
        translatedValues.putIfAbsent(cellValue, translatedValue);
    }
}
