package com.hybris.backoffice.excel.translators;

import com.hybris.backoffice.BackofficeTestUtil;
import com.hybris.backoffice.excel.data.ImpexHeaderValue;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbstractExcelMediaImportTranslatorTest
{
    @InjectMocks
    @Spy
    private ExcelMediaImportTranslator translator;


    @Test
    public void shouldCreateMediaFolderHeader()
    {
        AttributeDescriptorModel descriptor = BackofficeTestUtil.mockAttributeDescriptor("Media");
        Map<String, String> params = new HashMap<>();
        ImpexHeaderValue header = this.translator.createMediaFolderHeader(descriptor, params);
        Assertions.assertThat(header.getName()).isEqualTo("folder(qualifier)");
    }


    @Test
    public void shouldHaveImportData()
    {
        Map<String, String> params = new HashMap<>();
        params.put("code", "code");
        params.put("filePath", "filePath");
        params.put("catalog", "catalog");
        params.put("version", "version");
        params.put("folder", "folder");
        boolean hasImportData = this.translator.hasImportData(params);
        Assertions.assertThat(hasImportData).isTrue();
    }


    @Test
    public void shouldNotHaveImportDataWhenFolderIsNotDefined()
    {
        Map<String, String> params = new HashMap<>();
        params.put("code", "code");
        params.put("filePath", "filePath");
        params.put("catalog", "catalog");
        params.put("version", "version");
        boolean hasImportData = this.translator.hasImportData(params);
        Assertions.assertThat(hasImportData).isTrue();
    }


    @Test
    public void shouldNotHaveImportData()
    {
        Map<String, String> params = new HashMap<>();
        params.put("code", "code");
        params.put("filePath", "filePath");
        params.put("folder", "folder");
        boolean hasImportData = this.translator.hasImportData(params);
        Assertions.assertThat(hasImportData).isFalse();
    }


    @Test
    public void shouldCreateMediaRow()
    {
        AttributeDescriptorModel attributeDescriptor = BackofficeTestUtil.mockAttributeDescriptor("desc");
        Map<String, String> params = (Map<String, String>)Mockito.mock(Map.class);
        String mediaRefId = "mediaRefId";
        ImpexHeaderValue mediaReferenceIdHeader = (ImpexHeaderValue)Mockito.mock(ImpexHeaderValue.class);
        ((ExcelMediaImportTranslator)Mockito.doReturn(mediaReferenceIdHeader).when(this.translator)).createMediaReferenceIdHeader(attributeDescriptor, params);
        ImpexHeaderValue mediaCodeHeader = (ImpexHeaderValue)Mockito.mock(ImpexHeaderValue.class);
        ((ExcelMediaImportTranslator)Mockito.doReturn(mediaCodeHeader).when(this.translator)).createMediaCodeHeader(attributeDescriptor, params);
        ((ExcelMediaImportTranslator)Mockito.doReturn("Code").when(this.translator)).getCode(attributeDescriptor, params);
        ImpexHeaderValue mediaCatalogVersionHeader = (ImpexHeaderValue)Mockito.mock(ImpexHeaderValue.class);
        ((ExcelMediaImportTranslator)Mockito.doReturn(mediaCatalogVersionHeader).when(this.translator)).createMediaCatalogVersionHeader(attributeDescriptor, params);
        ((ExcelMediaImportTranslator)Mockito.doReturn("catalogVersionData").when(this.translator)).catalogVersionData(params);
        ((ExcelMediaImportTranslator)Mockito.doReturn(null).when(this.translator)).getFolder(attributeDescriptor, params);
        ((ExcelMediaImportTranslator)Mockito.doReturn("").when(this.translator)).getFilePath(attributeDescriptor, params);
        Map<ImpexHeaderValue, Object> mediaRow = this.translator.createMediaRow(attributeDescriptor, "mediaRefId", params);
        ((ExcelMediaImportTranslator)Mockito.verify(this.translator, Mockito.times(0))).createMediaFolderHeader(attributeDescriptor, params);
        ((ExcelMediaImportTranslator)Mockito.verify(this.translator, Mockito.times(0))).createMediaContentHeader(attributeDescriptor, params);
        Assertions.assertThat(mediaRow).containsEntry(mediaReferenceIdHeader, "mediaRefId");
        Assertions.assertThat(mediaRow).containsEntry(mediaCodeHeader, "Code");
    }
}
