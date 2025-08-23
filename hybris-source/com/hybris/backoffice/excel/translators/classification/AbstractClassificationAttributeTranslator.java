package com.hybris.backoffice.excel.translators.classification;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.Impex;
import com.hybris.backoffice.excel.data.ImpexForType;
import com.hybris.backoffice.excel.data.ImpexHeaderValue;
import com.hybris.backoffice.excel.data.ImpexValue;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.importing.ExcelImportContext;
import com.hybris.backoffice.excel.importing.parser.splitter.ExcelParserSplitter;
import com.hybris.backoffice.excel.translators.ExcelAttributeTranslator;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.ClassificationSystemService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.classification.features.LocalizedFeature;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractClassificationAttributeTranslator implements ExcelAttributeTranslator<ExcelClassificationAttribute>
{
    protected static final String SIMPLE_TYPE_WITH_UNITS_REFERENCE_FORMAT = "value:unit[%s]";
    protected static final String COMPLEX_TYPE_WITH_UNITS_REFERENCE_FORMAT = "%s:unit[%s]";
    protected static final String VALUE_WITH_UNITS_FORMAT = "%s:%s";
    private ClassificationService classificationService;
    private ClassificationSystemService classificationSystemService;
    private CommonI18NService commonI18NService;
    private ExcelParserSplitter excelParserSplitter;
    private ClassificationAttributeHeaderValueCreator classificationAttributeHeaderValueCreator;


    public boolean canHandleUnit(@Nonnull ExcelClassificationAttribute excelClassificationAttribute)
    {
        return false;
    }


    public boolean canHandleRange(@Nonnull ExcelClassificationAttribute excelClassificationAttribute)
    {
        return false;
    }


    public boolean canHandle(@Nonnull ExcelClassificationAttribute excelAttribute)
    {
        if(excelAttribute.getAttributeAssignment().getUnit() != null && !canHandleUnit(excelAttribute))
        {
            return false;
        }
        if(excelAttribute.getAttributeAssignment().getRange().booleanValue() && !canHandleRange(excelAttribute))
        {
            return false;
        }
        return canHandleAttribute(excelAttribute);
    }


    public Optional<String> exportData(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull Object objectToExport)
    {
        if(objectToExport instanceof ProductModel)
        {
            Feature feature = getClassificationService().getFeature((ProductModel)objectToExport, excelAttribute
                            .getAttributeAssignment());
            return Optional.ofNullable(
                            getStreamOfValuesToJoin(excelAttribute, feature)
                                            .collect(Collectors.joining(",")));
        }
        return Optional.empty();
    }


    @Nonnull
    protected Stream<String> getStreamOfValuesToJoin(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull Feature feature)
    {
        List<FeatureValue> featureValues = new ArrayList<>(getFeatureValues(excelAttribute, feature));
        return featureValues
                        .stream()
                        .map(featureValue -> exportWithUnit(excelAttribute, featureValue))
                        .filter(Optional::isPresent)
                        .map(Optional::get);
    }


    protected Optional<String> exportWithUnit(ExcelClassificationAttribute excelAttribute, FeatureValue featureValue)
    {
        ClassificationAttributeUnitModel unit = featureValue.getUnit();
        Optional<String> exportedValue = exportSingle(excelAttribute, featureValue);
        if(exportedValue.isPresent() && unit != null)
        {
            return Optional.of(String.format("%s:%s", new Object[] {exportedValue.get(), unit.getCode()}));
        }
        return exportedValue;
    }


    public Impex importData(ExcelAttribute excelAttribute, ImportParameters importParameters, ExcelImportContext excelImportContext)
    {
        Impex impex = new Impex();
        if(excelAttribute instanceof ExcelClassificationAttribute)
        {
            ExcelClassificationAttribute excelClassificationAttribute = (ExcelClassificationAttribute)excelAttribute;
            BiConsumer<ImpexHeaderValue, Object> insertValToImpex = (headerValue, impexValue) -> {
                ImpexForType impexForType = impex.findUpdates(importParameters.getTypeCode());
                impexForType.putValue(Integer.valueOf(0), headerValue, impexValue);
            };
            if(excelClassificationAttribute.getAttributeAssignment().getMultiValued().booleanValue())
            {
                Collection<ImportParameters> multiImportParameters = splitImportParametersOfMultivalue(importParameters);
                List<ImpexValue> impexValues = (List<ImpexValue>)multiImportParameters.stream().map(impParam -> importValue(excelClassificationAttribute, impParam, excelImportContext)).collect(Collectors.toList());
                String valueToInsert = impexValues.stream().filter(Objects::nonNull).map(ImpexValue::getValue).map(String::valueOf).collect(Collectors.joining(","));
                if(!impexValues.isEmpty() && impexValues.get(0) != null)
                {
                    insertValToImpex.accept(((ImpexValue)impexValues.get(0)).getHeaderValue(), valueToInsert);
                }
            }
            else
            {
                ImpexValue impexValue = importValue(excelClassificationAttribute, importParameters, excelImportContext);
                if(impexValue != null)
                {
                    insertValToImpex.accept(impexValue.getHeaderValue(), impexValue.getValue());
                }
            }
        }
        return impex;
    }


    private Collection<ImportParameters> splitImportParametersOfMultivalue(ImportParameters importParameters)
    {
        String cellValue = String.valueOf(importParameters.getCellValue());
        if(!StringUtils.contains(cellValue, ","))
        {
            return Lists.newArrayList((Object[])new ImportParameters[] {importParameters});
        }
        String[] multiCellValue = StringUtils.split(cellValue, ",");
        List<ImportParameters> splitImportParameters = new ArrayList<>();
        for(int i = 0; i < multiCellValue.length; i++)
        {
            String singleCellValue = multiCellValue[i];
            Map<String, String> singleParams = importParameters.getMultiValueParameters().get(i);
            ImportParameters singleImportParameters = new ImportParameters(importParameters.getTypeCode(), importParameters.getIsoCode(), singleCellValue, importParameters.getEntryRef(), Lists.newArrayList((Object[])new Map[] {singleParams}));
            splitImportParameters.add(singleImportParameters);
        }
        return splitImportParameters;
    }


    protected ImpexValue importValue(ExcelClassificationAttribute excelAttribute, ImportParameters importParameters, ExcelImportContext excelImportContext)
    {
        if(excelAttribute.getAttributeAssignment().getUnit() != null)
        {
            Pair<ImportParameters, String> pair = splitToImportParametersWithoutUnitAndUnit(importParameters);
            if(pair != null)
            {
                ImportParameters importParametersWithoutUnit = (ImportParameters)pair.getLeft();
                String unit = (String)pair.getRight();
                ImpexValue impexValueWithoutUnit = importSingle(excelAttribute, importParametersWithoutUnit, excelImportContext);
                if(impexValueWithoutUnit != null)
                {
                    String impexValueWithUnit = StringUtils.isNotBlank(unit) ? ("" + impexValueWithoutUnit.getValue() + ":" + impexValueWithoutUnit.getValue()) : String.valueOf(impexValueWithoutUnit.getValue());
                    return new ImpexValue(impexValueWithUnit, impexValueWithoutUnit.getHeaderValue());
                }
            }
        }
        return importSingle(excelAttribute, importParameters, excelImportContext);
    }


    private Pair<ImportParameters, String> splitToImportParametersWithoutUnitAndUnit(ImportParameters importParameters)
    {
        String[] cellValues = this.excelParserSplitter.apply(String.valueOf(importParameters.getCellValue()));
        if(cellValues.length != 2)
        {
            return null;
        }
        String[] rawValues = this.excelParserSplitter.apply((String)importParameters.getSingleValueParameters().get("rawValue"));
        Map<String, String> paramsWithoutUnit = new LinkedHashMap<>();
        paramsWithoutUnit.putAll(importParameters.getSingleValueParameters());
        paramsWithoutUnit.replace("rawValue", rawValues[0]);
        ImportParameters importParametersWithoutUnit = new ImportParameters(importParameters.getTypeCode(), importParameters.getIsoCode(), cellValues[0], importParameters.getEntryRef(), Lists.newArrayList((Object[])new Map[] {paramsWithoutUnit}));
        return (Pair<ImportParameters, String>)ImmutablePair.of(importParametersWithoutUnit, rawValues[1]);
    }


    @Nonnull
    protected Collection<FeatureValue> getFeatureValues(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull Feature feature)
    {
        Collection<FeatureValue> values = feature.getClassAttributeAssignment().getLocalized().booleanValue() ? getLocalizedFeatureValues(excelAttribute, (LocalizedFeature)feature) : getUnlocalizedFeatureValues(excelAttribute, feature);
        return (Collection<FeatureValue>)values.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }


    @Nonnull
    protected Collection<FeatureValue> getUnlocalizedFeatureValues(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull Feature feature)
    {
        ClassAttributeAssignmentModel classAttributeAssignment = feature.getClassAttributeAssignment();
        return CollectionUtils.emptyIfNull(
                        (classAttributeAssignment.getMultiValued().booleanValue() || classAttributeAssignment.getRange().booleanValue()) ?
                                        feature.getValues() : Lists.newArrayList((Object[])new FeatureValue[] {feature.getValue()}));
    }


    @Nonnull
    protected Collection<FeatureValue> getLocalizedFeatureValues(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull LocalizedFeature feature)
    {
        Locale locale = getCommonI18NService().getLocaleForIsoCode(excelAttribute.getIsoCode());
        ClassAttributeAssignmentModel attributeAssignment = excelAttribute.getAttributeAssignment();
        return CollectionUtils.emptyIfNull(
                        (attributeAssignment.getMultiValued().booleanValue() || attributeAssignment.getRange().booleanValue()) ?
                                        feature.getValues(locale) : Lists.newArrayList((Object[])new FeatureValue[] {feature.getValue(locale)}));
    }


    protected Collection<Pair<FeatureValue, FeatureValue>> getPartitionedData(List<FeatureValue> featureValues)
    {
        if(featureValues.isEmpty())
        {
            return Collections.emptyList();
        }
        Function<List<FeatureValue>, FeatureValue> getOrNull = list -> (list.size() > 1) ? list.get(1) : new FeatureValue("");
        int noOfPartitions = 2;
        return (Collection<Pair<FeatureValue, FeatureValue>>)ListUtils.partition(featureValues, 2)
                        .stream()
                        .map(list -> ImmutablePair.of(list.get(0), getOrNull.apply(list)))
                        .collect(Collectors.toList());
    }


    @Nonnull
    public String referenceFormat(@Nonnull ExcelClassificationAttribute excelAttribute)
    {
        ClassificationAttributeUnitModel unit = excelAttribute.getAttributeAssignment().getUnit();
        if(unit != null)
        {
            Collection<ClassificationAttributeUnitModel> availableUnits = StringUtils.isNotBlank(unit.getUnitType())
                            ? getClassificationSystemService().getUnitsOfTypeForSystemVersion(unit.getSystemVersion(), unit.getUnitType())
                            : Lists.newArrayList((Object[])new ClassificationAttributeUnitModel[] {unit});
            if(CollectionUtils.isNotEmpty(availableUnits))
            {
                String unitsAsString = availableUnits.stream().sorted(Comparator.comparing(ClassificationAttributeUnitModel::getConversionFactor).reversed()).map(ClassificationAttributeUnitModel::getCode).collect(Collectors.joining(","));
                String singleReferenceFormat = singleReferenceFormat(excelAttribute);
                return StringUtils.isEmpty(singleReferenceFormat) ?
                                String.format("value:unit[%s]", new Object[] {unitsAsString}) : String.format("%s:unit[%s]", new Object[] {singleReferenceFormat, unitsAsString});
            }
        }
        return singleReferenceFormat(excelAttribute);
    }


    public ClassificationService getClassificationService()
    {
        return this.classificationService;
    }


    @Required
    public void setClassificationService(ClassificationService classificationService)
    {
        this.classificationService = classificationService;
    }


    public ClassificationSystemService getClassificationSystemService()
    {
        return this.classificationSystemService;
    }


    @Required
    public void setClassificationSystemService(ClassificationSystemService classificationSystemService)
    {
        this.classificationSystemService = classificationSystemService;
    }


    public CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    public ExcelParserSplitter getExcelParserSplitter()
    {
        return this.excelParserSplitter;
    }


    @Required
    public void setExcelParserSplitter(ExcelParserSplitter excelParserSplitter)
    {
        this.excelParserSplitter = excelParserSplitter;
    }


    public ClassificationAttributeHeaderValueCreator getClassificationAttributeHeaderValueCreator()
    {
        return this.classificationAttributeHeaderValueCreator;
    }


    @Required
    public void setClassificationAttributeHeaderValueCreator(ClassificationAttributeHeaderValueCreator classificationAttributeHeaderValueCreator)
    {
        this.classificationAttributeHeaderValueCreator = classificationAttributeHeaderValueCreator;
    }


    public abstract Optional<String> exportSingle(@Nonnull ExcelClassificationAttribute paramExcelClassificationAttribute, @Nonnull FeatureValue paramFeatureValue);


    @Nonnull
    public abstract String singleReferenceFormat(@Nonnull ExcelClassificationAttribute paramExcelClassificationAttribute);


    public abstract boolean canHandleAttribute(@Nonnull ExcelClassificationAttribute paramExcelClassificationAttribute);


    @Nullable
    protected abstract ImpexValue importSingle(@Nonnull ExcelClassificationAttribute paramExcelClassificationAttribute, @Nonnull ImportParameters paramImportParameters, @Nonnull ExcelImportContext paramExcelImportContext);
}
