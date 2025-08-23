package com.hybris.backoffice.excel.translators;

import com.google.common.collect.Lists;
import com.hybris.backoffice.BackofficeTestUtil;
import com.hybris.backoffice.excel.data.Impex;
import com.hybris.backoffice.excel.data.ImpexForType;
import com.hybris.backoffice.excel.data.ImpexHeaderValue;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.template.filter.ExcelFilter;
import com.hybris.backoffice.excel.translators.media.MediaFolderProvider;
import de.hybris.platform.catalog.CatalogTypeService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExcelMediaImportTranslatorTest
{
    private static final String GENERATED_CODE = "generatedCode";
    @InjectMocks
    @Spy
    private ExcelMediaImportTranslator translator;
    @Mock
    private TypeService typeService;
    @Mock
    private CatalogTypeService catalogTypeService;
    @Mock
    private KeyGenerator mediaCodeGenerator;
    @Mock
    private MediaFolderModel mediaFolder;
    @Mock
    private ExcelFilter<AttributeDescriptorModel> mandatoryFilter;
    @Mock
    private MediaFolderProvider mediaFolderProvider;


    @Before
    public void setUp()
    {
        Mockito.when(this.catalogTypeService.getCatalogVersionContainerAttribute("Media")).thenReturn("catalogVersion");
        Mockito.when(Boolean.valueOf(this.typeService.isAssignableFrom("Media", "Media"))).thenReturn(Boolean.valueOf(true));
        Mockito.when(this.translator.generateMediaRefId((AttributeDescriptorModel)Matchers.any(), (Map)Matchers.any())).thenReturn("generatedCode");
        Mockito.when(this.mediaCodeGenerator.generate()).thenReturn("generatedCode");
    }


    @Test
    public void shouldImportMediaWithGeneratedCode()
    {
        Map<String, String> params = new HashMap<>();
        params.put("filePath", "path");
        params.put("catalog", "default");
        params.put("version", "staged");
        params.put("folder", "folder");
        Mockito.when(this.mediaFolderProvider.provide(params)).thenReturn("folder");
        AttributeDescriptorModel attrDesc = BackofficeTestUtil.mockAttributeDescriptor("Media");
        ImportParameters importParameters = new ImportParameters("Product", null, "c", "d", Lists.newArrayList((Object[])new Map[] {params}));
        Impex impex = this.translator.importData(attrDesc, importParameters);
        ImpexForType mediaImpex = impex.findUpdates("Media");
        Assertions.assertThat(mediaImpex).isNotNull();
        Assertions.assertThat(mediaImpex.getImpexTable().row(Integer.valueOf(0))).isNotNull();
        Assertions.assertThat(mediaImpex.getImpexTable().row(Integer.valueOf(0)).keySet()).hasSize(5);
        Assertions.assertThat(mediaImpex.getImpexTable().row(Integer.valueOf(0)).keySet()).containsOnly((Object[])new ImpexHeaderValue[] {this.translator
                        .createMediaReferenceIdHeader(attrDesc, params), this.translator.createMediaCodeHeader(attrDesc, params), this.translator
                        .createMediaCatalogVersionHeader(attrDesc, params), this.translator.createMediaContentHeader(attrDesc, params), this.translator
                        .createMediaFolderHeader(attrDesc, params)});
        Assertions.assertThat(mediaImpex.getImpexTable().row(Integer.valueOf(0)).get(this.translator.createMediaCodeHeader(attrDesc, params)))
                        .isEqualTo("generatedCode");
        ImpexForType productImpex = impex.findUpdates("Product");
        Assertions.assertThat(productImpex).isNotNull();
        Assertions.assertThat(productImpex.getImpexTable().get(Integer.valueOf(0), this.translator.createReferenceHeader(attrDesc))).isEqualTo("generatedCode");
    }


    @Test
    public void shouldNotImportContentIfFilePathIsEmpty()
    {
        Map<String, String> params = new HashMap<>();
        params.put("code", "theCode");
        params.put("catalog", "default");
        params.put("version", "staged");
        params.put("folder", "folder");
        Mockito.when(this.mediaFolderProvider.provide(params)).thenReturn("folder");
        AttributeDescriptorModel attrDesc = BackofficeTestUtil.mockAttributeDescriptor("Media");
        ImportParameters importParameters = new ImportParameters("a", "b", "c", "d", Lists.newArrayList((Object[])new Map[] {params}));
        Impex impex = this.translator.importData(attrDesc, importParameters);
        ImpexForType mediaImpex = impex.findUpdates("Media");
        Assertions.assertThat(mediaImpex).isNotNull();
        Assertions.assertThat(mediaImpex.getImpexTable().row(Integer.valueOf(0))).isNotNull();
        Assertions.assertThat(mediaImpex.getImpexTable().row(Integer.valueOf(0)).keySet()).hasSize(4);
        Assertions.assertThat(mediaImpex.getImpexTable().row(Integer.valueOf(0)).keySet()).containsOnly((Object[])new ImpexHeaderValue[] {this.translator
                        .createMediaReferenceIdHeader(attrDesc, params), this.translator.createMediaCodeHeader(attrDesc, params), this.translator
                        .createMediaCatalogVersionHeader(attrDesc, params), this.translator.createMediaFolderHeader(attrDesc, params)});
        Assertions.assertThat(mediaImpex.getImpexTable().get(Integer.valueOf(0), this.translator.createMediaCodeHeader(attrDesc, params))).isEqualTo("theCode");
    }


    @Test
    public void shouldExportDataBeNullSafe()
    {
        Assertions.assertThat(this.translator.exportData(null).isPresent()).isFalse();
    }


    @Test
    public void shouldExportedDataBeInProperFormat()
    {
        CatalogVersionModel cv = (CatalogVersionModel)Mockito.mock(CatalogVersionModel.class);
        CatalogModel catalog = (CatalogModel)Mockito.mock(CatalogModel.class);
        Mockito.when(catalog.getId()).thenReturn("default");
        Mockito.when(cv.getVersion()).thenReturn("staged");
        Mockito.when(cv.getCatalog()).thenReturn(catalog);
        MediaModel media = (MediaModel)Mockito.mock(MediaModel.class);
        Mockito.when(media.getCode()).thenReturn("theCode");
        Mockito.when(media.getCatalogVersion()).thenReturn(cv);
        Mockito.when(media.getFolder()).thenReturn(this.mediaFolder);
        Mockito.when(this.mediaFolder.getQualifier()).thenReturn("folder");
        Assertions.assertThat(this.translator.exportData(media).isPresent()).isTrue();
        Assertions.assertThat(this.translator.exportData(media).get()).isEqualTo(":theCode:default:staged:folder");
    }


    @Test
    public void shouldGivenTypeBeHandled()
    {
        AttributeDescriptorModel attributeDescriptor = BackofficeTestUtil.mockAttributeDescriptor("Media");
        Assertions.assertThat(this.translator.canHandle(attributeDescriptor)).isTrue();
    }


    @Test
    public void shouldNotHandleOtherTypes()
    {
        AttributeDescriptorModel attributeDescriptor = BackofficeTestUtil.mockAttributeDescriptor("Product");
        Assertions.assertThat(this.translator.canHandle(attributeDescriptor)).isFalse();
    }
}
