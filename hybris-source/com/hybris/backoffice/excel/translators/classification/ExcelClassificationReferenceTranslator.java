package com.hybris.backoffice.excel.translators.classification;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ImpexHeaderValue;
import com.hybris.backoffice.excel.data.ImpexValue;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.importing.ExcelImportContext;
import com.hybris.backoffice.excel.template.filter.ExcelFilter;
import com.hybris.backoffice.excel.translators.generic.RequiredAttribute;
import com.hybris.backoffice.excel.translators.generic.factory.ExportDataFactory;
import com.hybris.backoffice.excel.translators.generic.factory.ReferenceFormatFactory;
import com.hybris.backoffice.excel.translators.generic.factory.RequiredAttributesFactory;
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.springframework.beans.factory.annotation.Required;

public class ExcelClassificationReferenceTranslator extends AbstractClassificationAttributeTranslator
{
    private static final int TRANSLATOR_PRIORITY = 100;
    private int order = 2147483547;
    private ExportDataFactory exportDataFactory;
    private ReferenceFormatFactory referenceFormatFactory;
    private RequiredAttributesFactory requiredAttributesFactory;
    private ClassificationAttributeHeaderValueCreator headerValueCreator;
    private ExcelFilter<AttributeDescriptorModel> filter;


    public Optional<String> exportSingle(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull FeatureValue featureToExport)
    {
        RequiredAttribute requiredAttribute = getRequiredAttribute(excelAttribute);
        return this.exportDataFactory.create(requiredAttribute, featureToExport.getValue());
    }


    @Nonnull
    public String singleReferenceFormat(@Nonnull ExcelClassificationAttribute excelAttribute)
    {
        RequiredAttribute requiredAttribute = getRequiredAttribute(excelAttribute);
        return this.referenceFormatFactory.create(requiredAttribute);
    }


    private RequiredAttribute getRequiredAttribute(ExcelClassificationAttribute excelAttribute)
    {
        ComposedTypeModel referenceType = excelAttribute.getAttributeAssignment().getReferenceType();
        return this.requiredAttributesFactory.create(referenceType);
    }


    @Nullable
    protected ImpexValue importSingle(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull ImportParameters importParameters, @Nonnull ExcelImportContext excelImportContext)
    {
        String headerValueName = this.headerValueCreator.create(excelAttribute, excelImportContext);
        String value = (String)importParameters.getSingleValueParameters().get("rawValue");
        return new ImpexValue(value, (new ImpexHeaderValue.Builder(headerValueName)).withLang(importParameters.getIsoCode())
                        .withQualifier(excelAttribute.getQualifier()).build());
    }


    public boolean canHandleAttribute(@Nonnull ExcelClassificationAttribute excelAttribute)
    {
        return (ClassificationAttributeTypeEnum.REFERENCE == excelAttribute.getAttributeAssignment().getAttributeType() &&
                        hasAtLeastOneUniqueAttribute(excelAttribute.getAttributeAssignment().getReferenceType()));
    }


    protected boolean hasAtLeastOneUniqueAttribute(ComposedTypeModel composedTypeModel)
    {
        if(composedTypeModel == null)
        {
            return false;
        }
        List<AttributeDescriptorModel> allAttributes = new ArrayList<>();
        allAttributes.addAll(composedTypeModel.getDeclaredattributedescriptors());
        allAttributes.addAll(composedTypeModel.getInheritedattributedescriptors());
        return allAttributes.stream().anyMatch((Predicate<? super AttributeDescriptorModel>)this.filter);
    }


    public int getOrder()
    {
        return this.order;
    }


    public void setOrder(int order)
    {
        this.order = order;
    }


    @Required
    public void setExportDataFactory(ExportDataFactory exportDataFactory)
    {
        this.exportDataFactory = exportDataFactory;
    }


    @Required
    public void setReferenceFormatFactory(ReferenceFormatFactory referenceFormatFactory)
    {
        this.referenceFormatFactory = referenceFormatFactory;
    }


    @Required
    public void setRequiredAttributesFactory(RequiredAttributesFactory requiredAttributesFactory)
    {
        this.requiredAttributesFactory = requiredAttributesFactory;
    }


    @Required
    public void setHeaderValueCreator(ClassificationAttributeHeaderValueCreator headerValueCreator)
    {
        this.headerValueCreator = headerValueCreator;
    }


    public ExcelFilter<AttributeDescriptorModel> getFilter()
    {
        return this.filter;
    }


    @Required
    public void setFilter(ExcelFilter<AttributeDescriptorModel> filter)
    {
        this.filter = filter;
    }
}
