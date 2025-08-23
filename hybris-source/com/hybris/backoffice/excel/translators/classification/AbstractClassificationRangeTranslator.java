package com.hybris.backoffice.excel.translators.classification;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.Impex;
import com.hybris.backoffice.excel.data.ImpexForType;
import com.hybris.backoffice.excel.data.ImpexHeaderValue;
import com.hybris.backoffice.excel.data.ImpexValue;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.importing.ExcelImportContext;
import com.hybris.backoffice.excel.importing.parser.RangeParserUtils;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

public abstract class AbstractClassificationRangeTranslator extends AbstractClassificationAttributeTranslator
{
    @Nonnull
    protected Stream<String> getStreamOfValuesToJoin(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull Feature feature)
    {
        List<FeatureValue> featureValues = new ArrayList<>(getFeatureValues(excelAttribute, feature));
        return excelAttribute.getAttributeAssignment().getRange().booleanValue() ?
                        Stream.<String>of(exportRange(excelAttribute, getPartitionedData(featureValues)).orElse("")) :
                        featureValues.stream().map(featureValue -> exportWithUnit(excelAttribute, featureValue)).filter(Optional::isPresent).map(Optional::get);
    }


    public Optional<String> exportRange(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull Collection<Pair<FeatureValue, FeatureValue>> featureToExport)
    {
        return Optional.of(featureToExport
                        .stream()
                        .map(pair -> Arrays.asList(new String[] {getSingle(excelAttribute, (FeatureValue)pair.getLeft()), getSingle(excelAttribute, (FeatureValue)pair.getRight())})).map(list -> (String)list.stream().collect(Collectors.joining(";", "RANGE[", "]")))
                        .collect(Collectors.joining(",")));
    }


    public Impex importData(ExcelAttribute excelAttribute, ImportParameters importParameters, ExcelImportContext excelImportContext)
    {
        if(excelAttribute instanceof ExcelClassificationAttribute && ((ExcelClassificationAttribute)excelAttribute)
                        .getAttributeAssignment().getRange().booleanValue())
        {
            ExcelClassificationAttribute excelClassificationAttribute = (ExcelClassificationAttribute)excelAttribute;
            int multiSize = importParameters.getMultiValueParameters().size();
            if(multiSize == 0)
            {
                String headerValueName = getClassificationAttributeHeaderValueCreator().create(excelClassificationAttribute, excelImportContext);
                Impex impex1 = new Impex();
                ImpexForType impexForType = impex1.findUpdates(importParameters.getTypeCode());
                impexForType.putValue(Integer.valueOf(0), (new ImpexHeaderValue.Builder(headerValueName)).withLang(importParameters.getIsoCode())
                                .withQualifier(excelAttribute.getQualifier()).build(), "");
                return impex1;
            }
            List<Map<String, String>> paramsOfFrom = importParameters.getMultiValueParameters().subList(0, multiSize / 2);
            List<Map<String, String>> paramsOfTo = importParameters.getMultiValueParameters().subList(multiSize / 2, multiSize);
            List<String> impexRangeValues = new ArrayList<>();
            ImpexHeaderValue impexHeaderValue = null;
            for(int i = 0; i < paramsOfFrom.size(); i++)
            {
                Map<String, String> fromParams = paramsOfFrom.get(i);
                Map<String, String> toParams = paramsOfTo.get(i);
                ImportParameters fromImportParameters = getFromImportParameters(excelClassificationAttribute, importParameters, fromParams);
                ImportParameters toImportParameters = getToImportParameters(excelClassificationAttribute, importParameters, toParams);
                ImpexValue fromImpexValue = importValue((ExcelClassificationAttribute)excelAttribute, fromImportParameters, excelImportContext);
                ImpexValue toImpexValue = importValue((ExcelClassificationAttribute)excelAttribute, toImportParameters, excelImportContext);
                if(fromImpexValue != null && toImpexValue != null)
                {
                    impexHeaderValue = fromImpexValue.getHeaderValue();
                    String impexVal = String.format("%s,%s", new Object[] {fromImpexValue
                                    .getValue(), toImpexValue.getValue()});
                    impexRangeValues.add(impexVal);
                }
            }
            Impex impex = new Impex();
            if(!impexRangeValues.isEmpty())
            {
                ImpexForType impexForType = impex.findUpdates(importParameters.getTypeCode());
                impexForType.putValue(Integer.valueOf(0), impexHeaderValue,
                                StringUtils.join(impexRangeValues, ","));
            }
            return impex;
        }
        return super.importData(excelAttribute, importParameters, excelImportContext);
    }


    protected ImportParameters getFromImportParameters(ExcelClassificationAttribute excelClassificationAttribute, ImportParameters importParameters, Map<String, String> fromParams)
    {
        return RangeParserUtils.getSingleImportParameters(excelClassificationAttribute, importParameters, fromParams, RangeParserUtils.RangeBounds.FROM);
    }


    protected ImportParameters getToImportParameters(ExcelClassificationAttribute excelClassificationAttribute, ImportParameters importParameters, Map<String, String> toParams)
    {
        return RangeParserUtils.getSingleImportParameters(excelClassificationAttribute, importParameters, toParams, RangeParserUtils.RangeBounds.TO);
    }


    @Nonnull
    public String referenceFormat(@Nonnull ExcelClassificationAttribute excelAttribute)
    {
        if(excelAttribute.getAttributeAssignment().getRange().booleanValue())
        {
            String singleReferenceFormat = singleReferenceFormat(excelAttribute);
            ClassificationAttributeUnitModel unit = excelAttribute.getAttributeAssignment().getUnit();
            return (StringUtils.isEmpty(singleReferenceFormat) && unit == null) ?
                            "RANGE[value;value]" :
                            String.format("RANGE[%1$s;%1$s]", new Object[] {super.referenceFormat(excelAttribute)});
        }
        return super.referenceFormat(excelAttribute);
    }


    protected String getSingle(ExcelClassificationAttribute excelAttribute, FeatureValue featureValue)
    {
        return exportWithUnit(excelAttribute, featureValue).orElse("");
    }
}
