package com.hybris.backoffice.excel.translators.classification;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ImpexHeaderValue;
import com.hybris.backoffice.excel.data.ImpexValue;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.importing.ExcelImportContext;
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import javax.annotation.Nonnull;

public class ExcelClassificationEnumTypeTranslator extends AbstractClassificationAttributeTranslator
{
    private int order;


    public boolean canHandleAttribute(@Nonnull ExcelClassificationAttribute excelClassificationAttribute)
    {
        return (excelClassificationAttribute.getAttributeAssignment().getAttributeType() == ClassificationAttributeTypeEnum.ENUM);
    }


    public Optional<String> exportSingle(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull FeatureValue featureToExport)
    {
        Object featureValue = featureToExport.getValue();
        return Stream.<Optional>of(new Optional[] {extractHybrisEnumValueCode(featureValue), extractClassificationAttributeValueCode(featureValue)}).filter(Optional::isPresent)
                        .map(Optional::get)
                        .findFirst();
    }


    private static Optional<String> extractHybrisEnumValueCode(Object value)
    {
        Objects.requireNonNull(HybrisEnumValue.class);
        Objects.requireNonNull(HybrisEnumValue.class);
        return Optional.<Object>ofNullable(value).filter(HybrisEnumValue.class::isInstance).map(HybrisEnumValue.class::cast)
                        .map(HybrisEnumValue::getCode);
    }


    private static Optional<String> extractClassificationAttributeValueCode(Object value)
    {
        Objects.requireNonNull(ClassificationAttributeValueModel.class);
        Objects.requireNonNull(ClassificationAttributeValueModel.class);
        return Optional.<Object>ofNullable(value).filter(ClassificationAttributeValueModel.class::isInstance).map(ClassificationAttributeValueModel.class::cast)
                        .map(ClassificationAttributeValueModel::getCode);
    }


    public ImpexValue importSingle(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull ImportParameters importParameters, @Nonnull ExcelImportContext excelImportContext)
    {
        String headerName = getClassificationAttributeHeaderValueCreator().create(excelAttribute, excelImportContext);
        return new ImpexValue(importParameters.getCellValue(), (new ImpexHeaderValue.Builder(headerName))
                        .withLang(importParameters.getIsoCode()).withQualifier(excelAttribute.getQualifier()).build());
    }


    @Nonnull
    public String singleReferenceFormat(@Nonnull ExcelClassificationAttribute excelAttribute)
    {
        return "";
    }


    public int getOrder()
    {
        return this.order;
    }


    public void setOrder(int order)
    {
        this.order = order;
    }
}
