package com.hybris.backoffice.excel.template.populator;

import com.hybris.backoffice.excel.template.CollectionFormatter;
import com.hybris.backoffice.excel.template.ExcelTemplateConstants;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.fest.assertions.Assertions;
import org.fest.assertions.MapAssert;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClassificationTypeSystemSheetCompressorTest
{
    @Mock
    CollectionFormatter mockedCollectionFormatter;
    @InjectMocks
    ClassificationTypeSystemSheetCompressor compressor;


    @Test
    public void shouldCompressClassificationRowWhenAttributeIsSameButLangIsDifferent()
    {
        Collection<Map<ExcelTemplateConstants.ClassificationTypeSystemColumns, String>> rows = new ArrayList<>();
        rows.add(prepareRow("fullName1", "classificationClass", "en"));
        rows.add(prepareRow("fullName2", "classificationClass", "de"));
        BDDMockito.given(this.mockedCollectionFormatter.formatToString(new String[] {"en", "de"})).willReturn("{en},{de}");
        BDDMockito.given(this.mockedCollectionFormatter.formatToString(new String[] {"fullName1", "fullName2"})).willReturn("{fullName1},{fullName2}");
        Collection<Map<ExcelTemplateConstants.ClassificationTypeSystemColumns, String>> compressionResult = this.compressor.compress(rows);
        Assertions.assertThat(compressionResult).hasSize(1);
        Map<ExcelTemplateConstants.ClassificationTypeSystemColumns, String> firstRow = compressionResult.iterator().next();
        Assertions.assertThat(firstRow).includes(new MapAssert.Entry[] {MapAssert.entry(ExcelTemplateConstants.ClassificationTypeSystemColumns.FULL_NAME, "{fullName1},{fullName2}")});
        Assertions.assertThat(firstRow).includes(new MapAssert.Entry[] {MapAssert.entry(ExcelTemplateConstants.ClassificationTypeSystemColumns.CLASSIFICATION_SYSTEM, "classificationSystem")});
        Assertions.assertThat(firstRow).includes(new MapAssert.Entry[] {MapAssert.entry(ExcelTemplateConstants.ClassificationTypeSystemColumns.CLASSIFICATION_VERSION, "classificationVersion")});
        Assertions.assertThat(firstRow).includes(new MapAssert.Entry[] {MapAssert.entry(ExcelTemplateConstants.ClassificationTypeSystemColumns.CLASSIFICATION_CLASS, "classificationClass")});
        Assertions.assertThat(firstRow).includes(new MapAssert.Entry[] {MapAssert.entry(ExcelTemplateConstants.ClassificationTypeSystemColumns.CLASSIFICATION_ATTRIBUTE, "classificationAttribute")});
        Assertions.assertThat(firstRow).includes(new MapAssert.Entry[] {MapAssert.entry(ExcelTemplateConstants.ClassificationTypeSystemColumns.ATTRIBUTE_LOCALIZED, "true")});
        Assertions.assertThat(firstRow).includes(new MapAssert.Entry[] {MapAssert.entry(ExcelTemplateConstants.ClassificationTypeSystemColumns.ATTRIBUTE_LOC_LANG, "{en},{de}")});
        Assertions.assertThat(firstRow).includes(new MapAssert.Entry[] {MapAssert.entry(ExcelTemplateConstants.ClassificationTypeSystemColumns.MANDATORY, "true")});
    }


    @Test
    public void shouldCompressClassificationRowWhenAttributeIsSameButClassIsDifferent()
    {
        Collection<Map<ExcelTemplateConstants.ClassificationTypeSystemColumns, String>> rows = new ArrayList<>();
        rows.add(prepareRow("fullName", "classificationClass1", "en"));
        rows.add(prepareRow("fullName", "classificationClass2", "en"));
        Collection<Map<ExcelTemplateConstants.ClassificationTypeSystemColumns, String>> compressionResult = this.compressor.compress(rows);
        Map<ExcelTemplateConstants.ClassificationTypeSystemColumns, String> firstRow = compressionResult.stream().filter(row -> ((String)row.get(ExcelTemplateConstants.ClassificationTypeSystemColumns.CLASSIFICATION_CLASS)).equals("classificationClass1")).findFirst().orElse(null);
        Map<ExcelTemplateConstants.ClassificationTypeSystemColumns, String> secondRow = compressionResult.stream().filter(row -> ((String)row.get(ExcelTemplateConstants.ClassificationTypeSystemColumns.CLASSIFICATION_CLASS)).equals("classificationClass2")).findFirst().orElse(null);
        Assertions.assertThat(compressionResult).hasSize(2);
        Assert.assertNotNull(firstRow);
        Assert.assertNotNull(secondRow);
    }


    private Map<ExcelTemplateConstants.ClassificationTypeSystemColumns, String> prepareRow(String fullName, String classificationClass, String language)
    {
        return (Map<ExcelTemplateConstants.ClassificationTypeSystemColumns, String>)new Object(this, fullName, classificationClass, language);
    }
}
