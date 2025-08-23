/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.export.wizard.renderer;

import com.hybris.backoffice.excel.classification.ExcelClassificationAttributeFactory;
import com.hybris.backoffice.excel.translators.ExcelAttributeTranslatorRegistry;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import java.util.function.Predicate;
import org.springframework.beans.factory.annotation.Required;

/**
 * Predicate which checks whether classification attribute is supported by excel export.
 * {@link com.hybris.backoffice.renderer.attributeschooser.ClassificationAttributesExportRenderer#setSupportedAttributesPredicate(Predicate)}
 */
public class ExcelSupportedClassificationAttributesPredicate implements Predicate<ClassAttributeAssignmentModel>
{
    private ExcelAttributeTranslatorRegistry excelAttributeTranslatorRegistry;
    private ExcelClassificationAttributeFactory excelClassificationAttributeFactory;


    @Override
    public boolean test(final ClassAttributeAssignmentModel classAttributeAssignmentModel)
    {
        return getExcelAttributeTranslatorRegistry()
                        .canHandle(getExcelClassificationAttributeFactory().create(classAttributeAssignmentModel));
    }


    public ExcelAttributeTranslatorRegistry getExcelAttributeTranslatorRegistry()
    {
        return excelAttributeTranslatorRegistry;
    }


    @Required
    public void setExcelAttributeTranslatorRegistry(final ExcelAttributeTranslatorRegistry excelAttributeTranslatorRegistry)
    {
        this.excelAttributeTranslatorRegistry = excelAttributeTranslatorRegistry;
    }


    private ExcelClassificationAttributeFactory getExcelClassificationAttributeFactory()
    {
        return excelClassificationAttributeFactory;
    }


    @Required
    public void setExcelClassificationAttributeFactory(
                    final ExcelClassificationAttributeFactory excelClassificationAttributeFactory)
    {
        this.excelClassificationAttributeFactory = excelClassificationAttributeFactory;
    }
}
