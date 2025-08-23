package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.data.ExcelColumn;
import com.hybris.backoffice.excel.data.ExcelWorkbook;
import com.hybris.backoffice.excel.data.ExcelWorksheet;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.data.SelectedAttribute;
import com.hybris.backoffice.excel.importing.parser.DefaultImportParameterParser;
import com.hybris.backoffice.excel.importing.parser.ParsedValues;
import com.hybris.backoffice.excel.translators.generic.ExcelGenericReferenceTranslator;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.testframework.Transactional;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

@Transactional
@IntegrationTest
public class ExcelGenericReferenceValidatorIntegrationTest extends ServicelayerTest
{
    private static final String CATALOG_ID = "Default";
    private static final String CATALOG_VERSION_ID = "Online";
    private static final String FIRST_CATEGORY_ID = "First";
    private static final String SECOND_CATEGORY_ID = "Second";
    @Rule
    public JUnitSoftAssertions soft = new JUnitSoftAssertions();
    @Resource
    ExcelGenericReferenceValidator excelGenericReferenceValidator;
    @Resource
    ExcelGenericReferenceTranslator excelGenericReferenceTranslator;
    @Resource
    TypeService typeService;
    @Resource
    ModelService modelService;
    @Resource
    DefaultImportParameterParser defaultImportParameterParser;


    @Before
    public void setupData()
    {
        CatalogModel defaultCatalog = createCatalog("Default");
        CatalogVersionModel catalogVersion = createCatalogVersion(defaultCatalog, "Online");
        createCategory(catalogVersion, "First");
        createCategory(catalogVersion, "Second");
    }


    @Test
    public void shouldNotReportAnyValidationErrorsWhenCatalogVersionExist()
    {
        ParsedValues parsedValues = this.defaultImportParameterParser.parseValue("CatalogVersion.version:Catalog.id", "",
                        String.format("%s:%s", new Object[] {"Online", "Default"}));
        ImportParameters importParameters = new ImportParameters("Product", null, parsedValues.getCellValue(), null, parsedValues.getParameters());
        AttributeDescriptorModel attributeDescriptor = this.typeService.getAttributeDescriptor("Product", "catalogVersion");
        ExcelValidationResult validationResult = this.excelGenericReferenceValidator.validate(importParameters, attributeDescriptor, new HashMap<>());
        Assertions.assertThat(validationResult.hasErrors()).isFalse();
    }


    @Test
    public void shouldReportValidationErrorsWhenCatalogNotExist()
    {
        ParsedValues parsedValues = this.defaultImportParameterParser.parseValue("CatalogVersion.version:Catalog.id", "",
                        String.format("%s:%s", new Object[] {"NotExistingVersion", "NotExistingCatalog"}));
        ImportParameters importParameters = new ImportParameters("Product", null, parsedValues.getCellValue(), null, parsedValues.getParameters());
        AttributeDescriptorModel attributeDescriptor = this.typeService.getAttributeDescriptor("Product", "catalogVersion");
        ExcelValidationResult validationResult = this.excelGenericReferenceValidator.validate(importParameters, attributeDescriptor, new HashMap<>());
        this.soft.assertThat(validationResult.hasErrors()).isTrue();
        this.soft.assertThat(validationResult.getValidationErrors()).hasSize(1);
        this.soft.assertThat(((ValidationMessage)validationResult.getValidationErrors().get(0)).getMessageKey())
                        .isEqualTo("excel.import.validation.generic.translator.not.existing.item");
        this.soft.assertThat((Object[])((ValidationMessage)validationResult.getValidationErrors().get(0)).getParams()).contains((Object[])new Serializable[] {"Catalog", "{Catalog.id=NotExistingCatalog}"});
    }


    @Test
    public void shouldReportValidationErrorsWhenCatalogVersionNotExist()
    {
        ParsedValues parsedValues = this.defaultImportParameterParser.parseValue("CatalogVersion.version:Catalog.id", "",
                        String.format("%s:%s", new Object[] {"NotExistingVersion", "Default"}));
        ImportParameters importParameters = new ImportParameters("Product", null, parsedValues.getCellValue(), null, parsedValues.getParameters());
        AttributeDescriptorModel attributeDescriptor = this.typeService.getAttributeDescriptor("Product", "catalogVersion");
        ExcelValidationResult validationResult = this.excelGenericReferenceValidator.validate(importParameters, attributeDescriptor, new HashMap<>());
        this.soft.assertThat(validationResult.hasErrors()).isTrue();
        this.soft.assertThat(validationResult.getValidationErrors()).hasSize(1);
        this.soft.assertThat(((ValidationMessage)validationResult.getValidationErrors().get(0)).getMessageKey())
                        .isEqualTo("excel.import.validation.generic.translator.not.existing.item");
        this.soft.assertThat((Object[])((ValidationMessage)validationResult.getValidationErrors().get(0)).getParams()).contains((Object[])new Serializable[] {"CatalogVersion", "{CatalogVersion.version=NotExistingVersion, Catalog.id=Default}"});
    }


    @Test
    public void shouldNotReportAnyValidationErrorsWhenSupercategoriesExist()
    {
        ParsedValues parsedValues = this.defaultImportParameterParser.parseValue("Category.code:CatalogVersion.version:Catalog.id", "", "First:Online:Default,Second:Online:Default");
        ImportParameters importParameters = new ImportParameters(null, null, parsedValues.getCellValue(), null, parsedValues.getParameters());
        AttributeDescriptorModel attributeDescriptor = this.typeService.getAttributeDescriptor("Product", "supercategories");
        ExcelValidationResult validationResult = this.excelGenericReferenceValidator.validate(importParameters, attributeDescriptor, new HashMap<>());
        Assertions.assertThat(validationResult.hasErrors()).isFalse();
    }


    @Test
    public void shouldReportValidationErrorsWhenCatalogOfCategoryDoesNotExist()
    {
        ParsedValues parsedValues = this.defaultImportParameterParser.parseValue("Category.code:CatalogVersion.version:Catalog.id", "", "First:Online:NotExistingCatalog,Second:Online:NotExistingCatalog");
        ImportParameters importParameters = new ImportParameters(null, null, parsedValues.getCellValue(), null, parsedValues.getParameters());
        AttributeDescriptorModel attributeDescriptor = this.typeService.getAttributeDescriptor("Product", "supercategories");
        ExcelValidationResult validationResult = this.excelGenericReferenceValidator.validate(importParameters, attributeDescriptor, new HashMap<>());
        this.soft.assertThat(validationResult.hasErrors()).isTrue();
        this.soft.assertThat(validationResult.getValidationErrors()).hasSize(2);
        this.soft.assertThat(((ValidationMessage)validationResult.getValidationErrors().get(0)).getMessageKey())
                        .isEqualTo("excel.import.validation.generic.translator.not.existing.item");
        this.soft.assertThat(((ValidationMessage)validationResult.getValidationErrors().get(1)).getMessageKey())
                        .isEqualTo("excel.import.validation.generic.translator.not.existing.item");
        this.soft.assertThat((Object[])((ValidationMessage)validationResult.getValidationErrors().get(0)).getParams()).contains((Object[])new Serializable[] {"Catalog", "{Catalog.id=NotExistingCatalog}"});
        this.soft.assertThat((Object[])((ValidationMessage)validationResult.getValidationErrors().get(1)).getParams()).contains((Object[])new Serializable[] {"Catalog", "{Catalog.id=NotExistingCatalog}"});
    }


    @Test
    public void shouldReportValidationErrorsWhenCatalogVersionOfCategoryDoesNotExist()
    {
        ParsedValues parsedValues = this.defaultImportParameterParser.parseValue("Category.code:CatalogVersion.version:Catalog.id", "", "First:Online:Default,Second:NotExistingVersion:Default");
        ImportParameters importParameters = new ImportParameters(null, null, parsedValues.getCellValue(), null, parsedValues.getParameters());
        AttributeDescriptorModel attributeDescriptor = this.typeService.getAttributeDescriptor("Product", "supercategories");
        ExcelValidationResult validationResult = this.excelGenericReferenceValidator.validate(importParameters, attributeDescriptor, new HashMap<>());
        this.soft.assertThat(validationResult.hasErrors()).isTrue();
        this.soft.assertThat(validationResult.getValidationErrors()).hasSize(1);
        this.soft.assertThat(((ValidationMessage)validationResult.getValidationErrors().get(0)).getMessageKey())
                        .isEqualTo("excel.import.validation.generic.translator.not.existing.item");
        this.soft.assertThat((Object[])((ValidationMessage)validationResult.getValidationErrors().get(0)).getParams()).contains((Object[])new Serializable[] {"CatalogVersion", "{CatalogVersion.version=NotExistingVersion, Catalog.id=Default}"});
    }


    @Test
    public void shouldReportValidationErrorsWhenCodeOfCategoryDoesNotExist()
    {
        ParsedValues parsedValues = this.defaultImportParameterParser.parseValue("Category.code:CatalogVersion.version:Catalog.id", "", "FirstNotExisting:Online:Default,SecondNotExisting:Online:Default");
        ImportParameters importParameters = new ImportParameters(null, null, parsedValues.getCellValue(), null, parsedValues.getParameters());
        AttributeDescriptorModel attributeDescriptor = this.typeService.getAttributeDescriptor("Product", "supercategories");
        ExcelValidationResult validationResult = this.excelGenericReferenceValidator.validate(importParameters, attributeDescriptor, new HashMap<>());
        this.soft.assertThat(validationResult.hasErrors()).isTrue();
        this.soft.assertThat(validationResult.getValidationErrors()).hasSize(2);
        this.soft.assertThat(((ValidationMessage)validationResult.getValidationErrors().get(0)).getMessageKey())
                        .isEqualTo("excel.import.validation.generic.translator.not.existing.item");
        this.soft.assertThat(((ValidationMessage)validationResult.getValidationErrors().get(1)).getMessageKey())
                        .isEqualTo("excel.import.validation.generic.translator.not.existing.item");
        this.soft.assertThat((Object[])((ValidationMessage)validationResult.getValidationErrors().get(0)).getParams()).contains((Object[])new Serializable[] {"Category", "{Category.code=FirstNotExisting, CatalogVersion.version=Online, Catalog.id=Default}"});
        this.soft.assertThat((Object[])((ValidationMessage)validationResult.getValidationErrors().get(1)).getParams()).contains((Object[])new Serializable[] {"Category", "{Category.code=SecondNotExisting, CatalogVersion.version=Online, Catalog.id=Default}"});
    }


    @Test
    public void shouldNotReportAnyValidationErrorsWhenReferencedEntryExistInExcelSheet()
    {
        AttributeDescriptorModel attributeDescriptor = this.typeService.getAttributeDescriptor("Category", "supercategories");
        ParsedValues parsedValues = this.defaultImportParameterParser.parseValue(this.excelGenericReferenceTranslator.referenceFormat(attributeDescriptor), "", "firstCategory:Online:Default");
        ImportParameters importParameters = new ImportParameters(null, null, parsedValues.getCellValue(), null, parsedValues.getParameters());
        Map<String, Object> context = new HashMap<>();
        context.put(ExcelWorkbook.class.getCanonicalName(), prepareExcelWorkbook());
        ExcelValidationResult validationResult = this.excelGenericReferenceValidator.validate(importParameters, attributeDescriptor, context);
        Assertions.assertThat(validationResult.hasErrors()).isFalse();
    }


    @Test
    public void shouldReportValidationErrorsWhenReferencedEntryDoesNotExistInExcelSheet()
    {
        AttributeDescriptorModel attributeDescriptor = this.typeService.getAttributeDescriptor("Category", "supercategories");
        ParsedValues parsedValues = this.defaultImportParameterParser.parseValue(this.excelGenericReferenceTranslator
                        .referenceFormat(attributeDescriptor), "", "secondCategory:Online:Default");
        ImportParameters importParameters = new ImportParameters(null, null, parsedValues.getCellValue(), null, parsedValues.getParameters());
        Map<String, Object> context = new HashMap<>();
        context.put(ExcelWorkbook.class.getCanonicalName(), prepareExcelWorkbook());
        ExcelValidationResult validationResult = this.excelGenericReferenceValidator.validate(importParameters, attributeDescriptor, context);
        Assertions.assertThat(validationResult.hasErrors()).isTrue();
        Assertions.assertThat(validationResult.getValidationErrors()).extracting("messageKey")
                        .containsOnly(new Object[] {"excel.import.validation.generic.translator.not.existing.item"});
    }


    private ExcelWorkbook prepareExcelWorkbook()
    {
        ExcelWorkbook excelWorkbook = new ExcelWorkbook(new ExcelWorksheet[0]);
        ExcelWorksheet worksheet = new ExcelWorksheet("Category");
        AttributeDescriptorModel code = this.typeService.getAttributeDescriptor("Category", "code");
        AttributeDescriptorModel catalogVersion = this.typeService.getAttributeDescriptor("Category", "catalogVersion");
        worksheet.add(0, new ExcelColumn(new SelectedAttribute(code), Integer.valueOf(0)), prepareImportParameters(null, "firstCategory"));
        worksheet.add(0, new ExcelColumn(new SelectedAttribute(catalogVersion), Integer.valueOf(1)),
                        prepareImportParameters(catalogVersion, "Online:Default"));
        excelWorkbook.add(worksheet);
        return excelWorkbook;
    }


    private ImportParameters prepareImportParameters(AttributeDescriptorModel attributeDescriptor, String cellValue)
    {
        ParsedValues parsedValues = this.defaultImportParameterParser.parseValue(
                        (attributeDescriptor != null) ? this.excelGenericReferenceTranslator.referenceFormat(attributeDescriptor) : "", "", cellValue);
        return new ImportParameters(null, null, parsedValues.getCellValue(), null, parsedValues.getParameters());
    }


    private CatalogModel createCatalog(String catalogId)
    {
        CatalogModel defaultCatalog = (CatalogModel)this.modelService.create("Catalog");
        defaultCatalog.setId(catalogId);
        this.modelService.save(defaultCatalog);
        return defaultCatalog;
    }


    private CatalogVersionModel createCatalogVersion(CatalogModel defaultCatalog, String version)
    {
        CatalogVersionModel onlineVersion = (CatalogVersionModel)this.modelService.create("CatalogVersion");
        onlineVersion.setCatalog(defaultCatalog);
        onlineVersion.setVersion(version);
        this.modelService.save(onlineVersion);
        return onlineVersion;
    }


    private CategoryModel createCategory(CatalogVersionModel catalogVersion, String code)
    {
        CategoryModel category = (CategoryModel)this.modelService.create("Category");
        category.setCatalogVersion(catalogVersion);
        category.setCode(code);
        this.modelService.save(category);
        return category;
    }
}
