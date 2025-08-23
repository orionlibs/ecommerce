package com.hybris.backoffice.excel.integration;

import com.google.common.collect.Lists;
import com.hybris.backoffice.excel.ExcelIntegrationTest;
import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ExcelExportParams;
import com.hybris.backoffice.excel.data.ExcelExportResult;
import com.hybris.backoffice.excel.data.Impex;
import com.hybris.backoffice.excel.exporting.DefaultExcelExportPreProcessor;
import com.hybris.backoffice.excel.exporting.DefaultExcelExportService;
import com.hybris.backoffice.excel.exporting.DefaultExcelExportWorkbookPostProcessor;
import com.hybris.backoffice.excel.importing.DefaultExcelImportService;
import com.hybris.backoffice.excel.importing.DefaultExcelImportWorkbookPostProcessor;
import com.hybris.backoffice.excel.importing.ImpexConverter;
import com.hybris.backoffice.excel.importing.data.ExcelImportResult;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.classification.features.LocalizedFeature;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.testframework.Transactional;
import de.hybris.platform.testframework.seed.ClassificationSystemTestDataCreator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

@Transactional
@IntegrationTest
public class ExcelClassificationIntegrationTest extends ExcelIntegrationTest
{
    @Rule
    public JUnitSoftAssertions soft = new JUnitSoftAssertions();
    @Resource
    private ClassificationService classificationService;
    private ClassificationClassModel classificationClass;
    private ClassificationSystemVersionModel systemVersion;
    @Resource
    private DefaultExcelExportService excelExportService;
    @Resource
    private DefaultExcelExportWorkbookPostProcessor excelExportWorkbookPostProcessor;
    @Resource
    private DefaultExcelExportPreProcessor excelExportPreProcessor;
    @Resource
    private DefaultExcelImportService excelImportService;
    @Resource
    private DefaultExcelImportWorkbookPostProcessor excelImportWorkbookPostProcessor;
    @Resource
    private ImpexConverter excelImpexConverter;
    @Resource
    private I18NService i18NService;
    private TimeZone defaultTimeZone;
    private TimeZone defaultSessionTimeZone;


    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        createCoreData();
        createDefaultCatalog();
        createHardwareCatalog();
        ClassificationSystemTestDataCreator creator = new ClassificationSystemTestDataCreator(getModelService());
        ClassificationSystemModel system = creator.createClassificationSystem("testClassificationSystem");
        this.systemVersion = creator.createClassificationSystemVersion("testVersion", system);
        this.classificationClass = creator.createClassificationClass("testClass", this.systemVersion);
        this.defaultTimeZone = TimeZone.getDefault();
        this.defaultSessionTimeZone = this.i18NService.getCurrentTimeZone();
    }


    @After
    public void tearDown()
    {
        TimeZone.setDefault(this.defaultTimeZone);
        this.i18NService.setCurrentTimeZone(this.defaultSessionTimeZone);
    }


    @Test
    public void testExportIncludingClassification() throws IOException
    {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        this.i18NService.setCurrentTimeZone(TimeZone.getTimeZone("UTC"));
        CatalogVersionModel catalogVersion = createCatalogVersionModel("catalog", "1.0");
        ProductModel product = prepareProduct("product", catalogVersion);
        FeatureList featureList = FeatureListFactory.create(getModelService(), getTypeService(), this.classificationClass, catalogVersion, this.systemVersion);
        product.setSupercategories(Lists.newArrayList((Object[])new CategoryModel[] {(CategoryModel)this.classificationClass}));
        getModelService().save(product);
        this.classificationService.setFeatures(product, featureList);
        ExcelExportParams excelExportParams = this.excelExportPreProcessor.process(new ExcelExportParams(
                        Lists.newArrayList((Object[])new ItemModel[] {(ItemModel)product}, ), new ArrayList(), convertFeatureToExcelAttribute(featureList)));
        Workbook workbook = this.excelExportService.exportData(Collections.singletonList(product), new ArrayList());
        try
        {
            ExcelExportResult excelExportResult = new ExcelExportResult(workbook, excelExportParams.getItemsToExport(), excelExportParams.getSelectedAttributes(), excelExportParams.getAdditionalAttributes(), excelExportParams.getAdditionalAttributes());
            this.excelExportWorkbookPostProcessor.process(excelExportResult);
            Assertions.assertThat((Iterable)workbook.getSheet("Product")).isNotNull();
            this.soft.assertThat(getCellAt(workbook, 2, 3)).isEqualTo("true");
            this.soft.assertThat(getCellAt(workbook, 3, 3)).isEqualTo("true,false,true");
            this.soft.assertThat(getCellAt(workbook, 4, 3)).isEqualTo("3.53");
            this.soft.assertThat(getCellAt(workbook, 5, 3)).isEqualTo("4.53:kg");
            this.soft.assertThat(getCellAt(workbook, 6, 3)).isEqualTo("4.53:g,3.276:g,3.21:g");
            this.soft.assertThat(getCellAt(workbook, 7, 3)).isEqualTo("RANGE[2.53;3.77]");
            this.soft.assertThat(getCellAt(workbook, 8, 3)).isEqualTo("RANGE[1.53:m;1.58:m],RANGE[2.01:m;2.53:m]");
            this.soft.assertThat(getCellAt(workbook, 9, 3)).isEqualTo("03.03.2018 10:00:00");
            this.soft.assertThat(getCellAt(workbook, 10, 3)).isEqualTo("RANGE[03.03.2018 10:00:00;03.03.2019 12:00:00]");
            this.soft.assertThat(getCellAt(workbook, 11, 3)).isEqualTo("some string");
            this.soft.assertThat(getCellAt(workbook, 12, 3)).isEqualTo("x1,x2,x3");
            this.soft.assertThat(getCellAt(workbook, 13, 3)).isEqualTo("check");
            this.soft.assertThat(getCellAt(workbook, 14, 3)).isEqualTo("productRef:1.0:catalog");
            this.soft.assertThat(getCellAt(workbook, 15, 3)).isEqualTo("danke");
            this.soft.assertThat(getCellAt(workbook, 16, 3)).isEqualTo("thanks");
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


    private String getCellAt(Workbook workbook, int column, int row)
    {
        return workbook.getSheet("Product").getRow(row).getCell(column).getStringCellValue();
    }


    @Test
    public void testImportIncludingClassification() throws IOException
    {
        setAttributeDescriptorNamesForProductCodeAndCatalogVersion();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        this.i18NService.setCurrentTimeZone(TimeZone.getTimeZone("UTC"));
        CatalogVersionModel catalogVersion = createCatalogVersionModel("catalog", "1.0");
        ProductModel product = prepareProduct("product", catalogVersion);
        FeatureList featureList = FeatureListFactory.create(getModelService(), getTypeService(), this.classificationClass, catalogVersion, this.systemVersion);
        product.setSupercategories(Lists.newArrayList((Object[])new CategoryModel[] {(CategoryModel)this.classificationClass}));
        getModelService().save(product);
        this.classificationService.setFeatures(product, featureList);
        XSSFWorkbook xSSFWorkbook = new XSSFWorkbook(getClass().getResourceAsStream("/test/excel/importClassification.xlsx"));
        try
        {
            Impex impex = this.excelImportService.convertToImpex((Workbook)xSSFWorkbook);
            this.excelImportWorkbookPostProcessor.process(new ExcelImportResult((Workbook)xSSFWorkbook, impex));
            ImportResult importResult = this.importService.importData(
                            createImportConfig(this.excelImpexConverter
                                            .convert(impex)));
            Assertions.assertThat(importResult).isNotNull();
            this.soft.assertThat(importResult.isFinished()).isTrue();
            this.soft.assertThat(importResult.isError()).isFalse();
            this.soft.assertThat(importResult.isSuccessful()).isTrue();
            FeatureList features = this.classificationService.getFeatures(product);
            this.soft.assertThat(getFeatureValue(features, "singleBoolean")).isEqualToIgnoringCase("false");
            this.soft.assertThat(getFeatureValue(features, "multipleBoolean")).isEqualToIgnoringCase("true,false,true");
            this.soft.assertThat(getFeatureValue(features, "singleNumberWithoutUnit")).isEqualToIgnoringCase("5.38");
            this.soft.assertThat(getFeatureValue(features, "singleNumberWithUnit")).isEqualToIgnoringCase("4.23:kg");
            this.soft.assertThat(getFeatureValue(features, "multipleNumberWithUnit")).isEqualToIgnoringCase("4.53:g,3.276:kg,3.21:g");
            this.soft.assertThat(getFeatureValue(features, "rangeSingleNumberWithoutUnit")).isEqualToIgnoringCase("2.07,3.77");
            this.soft.assertThat(getFeatureValue(features, "rangeMultipleNumberWithUnit"))
                            .isEqualToIgnoringCase("1.53:m,1.58:m,2.01:m,2.53:m");
            this.soft.assertThat(getFeatureValue(features, "singleDate")).isEqualToIgnoringCase("03.03.2018 10:00:00");
            this.soft.assertThat(getFeatureValue(features, "singleRangeDate"))
                            .isEqualToIgnoringCase("03.03.2018 10:00:00,05.03.2019 12:00:00");
            this.soft.assertThat(getFeatureValue(features, "singleString")).isEqualToIgnoringCase("some other string");
            this.soft.assertThat(getFeatureValue(features, "multiString")).isEqualToIgnoringCase("x1,x2,x3");
            this.soft.assertThat(getFeatureValue(features, "singleReference")).isEqualToIgnoringCase("productRef:1.0:catalog");
            this.soft.assertThat(getFeatureValue(features, "singleEnum")).isEqualToIgnoringCase("check");
            xSSFWorkbook.close();
        }
        catch(Throwable throwable)
        {
            try
            {
                xSSFWorkbook.close();
            }
            catch(Throwable throwable1)
            {
                throwable.addSuppressed(throwable1);
            }
            throw throwable;
        }
    }


    private static String getFeatureValue(FeatureList features, String code)
    {
        return features.getFeatureByCode("testClassificationSystem/testVersion/testClass." + code.toLowerCase()).getValue()
                        .getValue().toString();
    }


    private static List<ExcelAttribute> convertFeatureToExcelAttribute(FeatureList features)
    {
        Objects.requireNonNull(LocalizedFeature.class);
        Objects.requireNonNull(LocalizedFeature.class);
        List<LocalizedFeature> localizedFeatures = (List<LocalizedFeature>)features.getFeatures().stream().filter(LocalizedFeature.class::isInstance).map(LocalizedFeature.class::cast).collect(Collectors.toList());
        List<Feature> unlocalizedFeatures = (List<Feature>)features.getFeatures().stream().filter(feature -> !(feature instanceof LocalizedFeature)).collect(Collectors.toList());
        List<ExcelAttribute> attributes = new ArrayList<>();
        List<ExcelAttribute> localizedAttributes = new ArrayList<>();
        for(LocalizedFeature f : localizedFeatures)
        {
            for(Map.Entry<Locale, List<FeatureValue>> entry : (Iterable<Map.Entry<Locale, List<FeatureValue>>>)f.getValuesForAllLocales().entrySet())
            {
                ExcelClassificationAttribute excelClassificationAttribute = new ExcelClassificationAttribute();
                excelClassificationAttribute.setIsoCode(((Locale)entry.getKey()).toLanguageTag());
                excelClassificationAttribute.setAttributeAssignment(f.getClassAttributeAssignment());
                localizedAttributes.add(excelClassificationAttribute);
            }
        }
        List<ExcelAttribute> unlocalizedAttributes = (List<ExcelAttribute>)unlocalizedFeatures.stream().map(feature -> {
            ExcelClassificationAttribute excelClassificationAttribute = new ExcelClassificationAttribute();
            excelClassificationAttribute.setAttributeAssignment(feature.getClassAttributeAssignment());
            return excelClassificationAttribute;
        }).collect(Collectors.toList());
        attributes.addAll(unlocalizedAttributes);
        attributes.addAll(localizedAttributes);
        return attributes;
    }
}
