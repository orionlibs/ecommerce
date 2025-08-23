package com.hybris.backoffice.excel.template;

import com.hybris.backoffice.excel.ExcelIntegrationTest;
import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelAttributeDescriptorAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ExcelExportResult;
import com.hybris.backoffice.excel.template.workbook.ExcelWorkbookService;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AtomicTypeModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.variants.model.VariantTypeModel;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.assertj.core.api.JUnitSoftAssertions;
import org.assertj.core.util.Lists;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;

public class ClassificationIncludedHeaderPromptPopulatorIntegrationTest extends ExcelIntegrationTest
{
    @Rule
    public JUnitSoftAssertions soft = new JUnitSoftAssertions();
    @Resource
    ExcelWorkbookService excelWorkbookService;
    @Resource
    FlexibleSearchService flexibleSearchService;
    @Spy
    @InjectMocks
    @Resource
    ClassificationIncludedHeaderPromptPopulator classificationIncludedHeaderPromptPopulator;
    private static String TEST_LONG_TYPE_CODE = "ElectronicsCapacityVariantProduct";
    private static String TEST_SHORT_SHEET_NAME = "ElectronicsCapacityVariantPro_1";


    @Test
    public void testPopulateWithVariant() throws Exception
    {
        loadClassificationImpex();
        VariantTypeModel variantTypeModel = (VariantTypeModel)saveItem((ItemModel)prepareVariant());
        ProductModel product = (ProductModel)saveItem((ItemModel)prepareProductWithVariant(
                        prepareProduct("product", createCatalogVersionModel("catalog", "1.0")), variantTypeModel));
        ItemModel variant = saveItem((ItemModel)prepareVariantProductModel(product, variantTypeModel));
        List<ExcelAttribute> assignments = prepareExcelClassificationAttributes();
        List<ExcelAttribute> excelAttributes = prepareExcelAttributeDescriptorAttributes(variant);
        Workbook workbook = this.excelWorkbookService.createWorkbook(loadResource("/excel/excelImpExMasterTemplate.xlsx"));
        try
        {
            ExcelExportResult excelExportResult = new ExcelExportResult(workbook, null, null, assignments, excelAttributes);
            this.excelWorkbookService.addProperty(workbook, TEST_SHORT_SHEET_NAME, TEST_LONG_TYPE_CODE);
            this.classificationIncludedHeaderPromptPopulator.populate(excelExportResult);
            String column1Variant = "VariantProduct";
            String column1Product = "Product";
            String column2Localized = "description[en]";
            String column2Unlocalized = "order";
            String column2Classification = "software.manufacturerURL[en] - SampleClassification/1.0";
            Sheet headerPrompt = workbook.getSheet(ExcelTemplateConstants.UtilitySheet.HEADER_PROMPT.getSheetName());
            this.soft.assertThat(headerPrompt.getRow(1).getCell(ExcelTemplateConstants.HeaderPrompt.HEADER_TYPE_CODE.getIndex()).getStringCellValue())
                            .isEqualTo("VariantProduct");
            this.soft.assertThat(headerPrompt.getRow(2).getCell(ExcelTemplateConstants.HeaderPrompt.HEADER_TYPE_CODE.getIndex()).getStringCellValue())
                            .isEqualTo("VariantProduct");
            this.soft.assertThat(headerPrompt.getRow(3).getCell(ExcelTemplateConstants.HeaderPrompt.HEADER_TYPE_CODE.getIndex()).getStringCellValue())
                            .isEqualTo("VariantProduct");
            this.soft.assertThat(headerPrompt.getRow(4).getCell(ExcelTemplateConstants.HeaderPrompt.HEADER_TYPE_CODE.getIndex()).getStringCellValue())
                            .isEqualTo("Product");
            this.soft.assertThat(headerPrompt.getRow(5).getCell(ExcelTemplateConstants.HeaderPrompt.HEADER_TYPE_CODE.getIndex()).getStringCellValue())
                            .isEqualTo("Product");
            this.soft.assertThat(headerPrompt.getRow(6).getCell(ExcelTemplateConstants.HeaderPrompt.HEADER_TYPE_CODE.getIndex()).getStringCellValue())
                            .isEqualTo("Product");
            this.soft.assertThat(headerPrompt.getRow(7).getCell(ExcelTemplateConstants.HeaderPrompt.HEADER_TYPE_CODE.getIndex()).getStringCellValue())
                            .isEqualTo(TEST_SHORT_SHEET_NAME);
            this.soft.assertThat(headerPrompt.getRow(1).getCell(ExcelTemplateConstants.HeaderPrompt.HEADER_ATTR_DISPLAYED_NAME.getIndex()).getStringCellValue())
                            .isEqualTo("description[en]");
            this.soft.assertThat(headerPrompt.getRow(2).getCell(ExcelTemplateConstants.HeaderPrompt.HEADER_ATTR_DISPLAYED_NAME.getIndex()).getStringCellValue())
                            .isEqualTo("order");
            this.soft.assertThat(headerPrompt.getRow(3).getCell(ExcelTemplateConstants.HeaderPrompt.HEADER_ATTR_DISPLAYED_NAME.getIndex()).getStringCellValue())
                            .isEqualTo("software.manufacturerURL[en] - SampleClassification/1.0");
            this.soft.assertThat(headerPrompt.getRow(4).getCell(ExcelTemplateConstants.HeaderPrompt.HEADER_ATTR_DISPLAYED_NAME.getIndex()).getStringCellValue())
                            .isEqualTo("description[en]");
            this.soft.assertThat(headerPrompt.getRow(5).getCell(ExcelTemplateConstants.HeaderPrompt.HEADER_ATTR_DISPLAYED_NAME.getIndex()).getStringCellValue())
                            .isEqualTo("order");
            this.soft.assertThat(headerPrompt.getRow(6).getCell(ExcelTemplateConstants.HeaderPrompt.HEADER_ATTR_DISPLAYED_NAME.getIndex()).getStringCellValue())
                            .isEqualTo("software.manufacturerURL[en] - SampleClassification/1.0");
            if(workbook != null)
            {
                workbook.close();
            }
        }
        catch(Throwable throwable)
        {
            if(workbook != null)
            {
                try
                {
                    workbook.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
            }
            throw throwable;
        }
    }


    protected List<ExcelAttribute> prepareExcelAttributeDescriptorAttributes(ItemModel variant)
    {
        String localizedAttribute = "Description";
        String unlocalizedAttribute = "order";
        AttributeDescriptorModel localizedProductAttributeDescriptor = getAttributeDescriptorOf(ProductModel.class, "Description");
        AttributeDescriptorModel unlocalizedProductAttributeDescriptor = getAttributeDescriptorOf(ProductModel.class, "order");
        AttributeDescriptorModel localizedVariantAttributeDescriptor = getAttributeDescriptorOf(variant, "Description");
        AttributeDescriptorModel unlocalizedVariantAttributeDescriptor = getAttributeDescriptorOf(variant, "order");
        AttributeDescriptorModel longCodeAttributeDescriptor = prepareLongTypeCodeAttributeDescriptor();
        return Lists.newArrayList((Object[])new ExcelAttribute[] {(ExcelAttribute)new ExcelAttributeDescriptorAttribute(longCodeAttributeDescriptor), (ExcelAttribute)new ExcelAttributeDescriptorAttribute(localizedProductAttributeDescriptor, "en"),
                        (ExcelAttribute)new ExcelAttributeDescriptorAttribute(unlocalizedProductAttributeDescriptor), (ExcelAttribute)new ExcelAttributeDescriptorAttribute(localizedVariantAttributeDescriptor, "en"),
                        (ExcelAttribute)new ExcelAttributeDescriptorAttribute(unlocalizedVariantAttributeDescriptor)});
    }


    protected AttributeDescriptorModel prepareLongTypeCodeAttributeDescriptor()
    {
        AttributeDescriptorModel longCodeAttributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        ComposedTypeModel composedTypeModel = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        AtomicTypeModel atomicTypeModel = (AtomicTypeModel)Mockito.mock(AtomicTypeModel.class);
        ((AttributeDescriptorModel)Mockito.doReturn("testName").when(longCodeAttributeDescriptor)).getName();
        ((AttributeDescriptorModel)Mockito.doReturn(composedTypeModel).when(longCodeAttributeDescriptor)).getEnclosingType();
        ((AttributeDescriptorModel)Mockito.doReturn(atomicTypeModel).when(longCodeAttributeDescriptor)).getAttributeType();
        ((ComposedTypeModel)Mockito.doReturn(TEST_LONG_TYPE_CODE).when(composedTypeModel)).getCode();
        return longCodeAttributeDescriptor;
    }


    protected List<ExcelAttribute> prepareExcelClassificationAttributes()
    {
        String queryForAllClassificationAssignments = "SELECT {ClassAttributeAssignment:PK} FROM {ClassAttributeAssignment}";
        List<ClassAttributeAssignmentModel> assignments = this.flexibleSearchService.search("SELECT {ClassAttributeAssignment:PK} FROM {ClassAttributeAssignment}").getResult();
        return (List<ExcelAttribute>)assignments.stream().map(assignment -> {
            ExcelClassificationAttribute attribute = new ExcelClassificationAttribute();
            attribute.setAttributeAssignment(assignment);
            attribute.setIsoCode("en");
            return attribute;
        }).collect(Collectors.toList());
    }


    private void loadClassificationImpex() throws ImpExException
    {
        importStream(loadResource("/test/excel/classificationSystem.csv"), StandardCharsets.UTF_8.name(), "classificationSystem.csv");
    }


    private InputStream loadResource(String path)
    {
        return (InputStream)Optional.<InputStream>ofNullable(getClass().getResourceAsStream(path))
                        .orElseThrow(() -> new AssertionError("Could not load resource: " + path));
    }
}
