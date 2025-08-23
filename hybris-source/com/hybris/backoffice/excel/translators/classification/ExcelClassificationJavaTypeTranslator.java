package com.hybris.backoffice.excel.translators.classification;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ImpexHeaderValue;
import com.hybris.backoffice.excel.data.ImpexValue;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.importing.ExcelImportContext;
import com.hybris.backoffice.excel.util.ExcelDateUtils;
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.classification.features.FeatureValue;
import java.util.Date;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Required;

public class ExcelClassificationJavaTypeTranslator extends AbstractClassificationRangeTranslator
{
    private static final Set<ClassificationAttributeTypeEnum> SUPPORTED_TYPES = EnumSet.of(ClassificationAttributeTypeEnum.NUMBER, ClassificationAttributeTypeEnum.STRING, ClassificationAttributeTypeEnum.BOOLEAN, ClassificationAttributeTypeEnum.DATE);
    private ExcelDateUtils excelDateUtils;
    private int order;


    public boolean canHandleUnit(@Nonnull ExcelClassificationAttribute excelClassificationAttribute)
    {
        return (excelClassificationAttribute.getAttributeAssignment().getAttributeType() == ClassificationAttributeTypeEnum.NUMBER);
    }


    public boolean canHandleRange(@Nonnull ExcelClassificationAttribute excelClassificationAttribute)
    {
        return (excelClassificationAttribute.getAttributeAssignment().getAttributeType() == ClassificationAttributeTypeEnum.NUMBER || excelClassificationAttribute
                        .getAttributeAssignment().getAttributeType() == ClassificationAttributeTypeEnum.DATE);
    }


    public boolean canHandleAttribute(@Nonnull ExcelClassificationAttribute excelClassificationAttribute)
    {
        return SUPPORTED_TYPES.contains(excelClassificationAttribute.getAttributeAssignment().getAttributeType());
    }


    public Optional<String> exportSingle(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull FeatureValue featureToExport)
    {
        Object value = featureToExport.getValue();
        if(value instanceof Date)
        {
            return Optional.ofNullable(this.excelDateUtils.exportDate((Date)value));
        }
        return Optional.ofNullable(value.toString());
    }


    public ImpexValue importSingle(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull ImportParameters importParameters, @Nonnull ExcelImportContext excelImportContext)
    {
        if(ClassificationAttributeTypeEnum.DATE.equals(excelAttribute.getAttributeAssignment().getAttributeType()))
        {
            return importDate(excelAttribute, importParameters, excelImportContext);
        }
        return importSimple(excelAttribute, importParameters, excelImportContext);
    }


    protected ImpexValue importDate(ExcelClassificationAttribute excelAttribute, ImportParameters importParameters, ExcelImportContext excelImportContext)
    {
        String headerValueName = getClassificationAttributeHeaderValueCreator().create(excelAttribute, excelImportContext);
        String dateToImport = this.excelDateUtils.importDate(String.valueOf(importParameters.getCellValue()));
        return new ImpexValue(dateToImport, (new ImpexHeaderValue.Builder(headerValueName)).withLang(importParameters.getIsoCode())
                        .withDateFormat(this.excelDateUtils.getDateTimeFormat()).withQualifier(excelAttribute.getQualifier()).build());
    }


    protected ImpexValue importSimple(ExcelClassificationAttribute excelAttribute, ImportParameters importParameters, ExcelImportContext excelImportContext)
    {
        String headerValueName = getClassificationAttributeHeaderValueCreator().create(excelAttribute, excelImportContext);
        return new ImpexValue(importParameters.getCellValue(), (new ImpexHeaderValue.Builder(headerValueName))
                        .withLang(importParameters.getIsoCode()).withQualifier(excelAttribute.getQualifier()).build());
    }


    @Nonnull
    public String singleReferenceFormat(@Nonnull ExcelClassificationAttribute excelAttribute)
    {
        if(ClassificationAttributeTypeEnum.DATE.getCode()
                        .equals(excelAttribute.getAttributeAssignment().getAttributeType().getCode()))
        {
            return this.excelDateUtils.getDateTimeFormat();
        }
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


    public ExcelDateUtils getExcelDateUtils()
    {
        return this.excelDateUtils;
    }


    @Required
    public void setExcelDateUtils(ExcelDateUtils excelDateUtils)
    {
        this.excelDateUtils = excelDateUtils;
    }
}
