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
public class ExcelMediaCollectionImportTranslatorTest
{
    private static final String GENERATED_CODE = "generatedCode";
    @InjectMocks
    @Spy
    private ExcelMediaCollectionImportTranslator translator;
    @Mock
    private TypeService typeService;
    @Mock
    private CatalogTypeService catalogTypeService;
    @Mock
    private KeyGenerator mediaCodeGenerator;
    @Mock
    private ExcelFilter<AttributeDescriptorModel> mandatoryFilter;
    @Mock
    private MediaFolderProvider mediaFolderProvider;


    @Before
    public void setUp()
    {
        Mockito.when(this.catalogTypeService.getCatalogVersionContainerAttribute("Media")).thenReturn("catalogVersion");
        Mockito.when(Boolean.valueOf(this.typeService.isAssignableFrom("Media", "Media"))).thenReturn(Boolean.valueOf(true));
        ((ExcelMediaCollectionImportTranslator)Mockito.doAnswer(inv -> {
            Map params = (Map)inv.getArguments()[1];
            return generateRefId(params);
        }).when(this.translator)).generateMediaRefId((AttributeDescriptorModel)Matchers.any(), (Map)Matchers.any());
        Mockito.when(this.mediaCodeGenerator.generate()).thenReturn("generatedCode");
    }


    protected String generateRefId(Map params)
    {
        String path = (String)params.getOrDefault("filePath", "");
        String code = (String)params.getOrDefault("code", "");
        return String.format("generatedFrom(%s,%s)", new Object[] {path, code});
    }


    @Test
    public void shouldImportMediaWithGeneratedCode()
    {
        Map<String, String> params = new HashMap<>();
        params.put("filePath", "path");
        params.put("code", "theCode");
        params.put("catalog", "default");
        params.put("version", "staged");
        Map<String, String> params2 = new HashMap<>();
        params2.put("filePath", "path2");
        params2.put("catalog", "default");
        params2.put("version", "staged");
        AttributeDescriptorModel attrDesc = BackofficeTestUtil.mockCollectionTypeAttributeDescriptor("Media");
        ImportParameters importParameters = new ImportParameters("Product", "b", "c", "d", Lists.newArrayList((Object[])new Map[] {params, params2}));
        Impex impex = this.translator.importData(attrDesc, importParameters);
        ImpexForType mediaImpex = impex.findUpdates("Media");
        Assertions.assertThat(mediaImpex).isNotNull();
        Assertions.assertThat(mediaImpex.getImpexTable().row(Integer.valueOf(0))).isNotNull();
        Assertions.assertThat(mediaImpex.getImpexTable().row(Integer.valueOf(0)).keySet()).hasSize(4);
        Assertions.assertThat(mediaImpex.getImpexTable().row(Integer.valueOf(0)).keySet()).containsOnly((Object[])new ImpexHeaderValue[] {this.translator
                        .createMediaReferenceIdHeader(attrDesc, params), this.translator.createMediaCodeHeader(attrDesc, params), this.translator
                        .createMediaCatalogVersionHeader(attrDesc, params), this.translator.createMediaContentHeader(attrDesc, params)});
        Assertions.assertThat(mediaImpex.getImpexTable().row(Integer.valueOf(0)).get(this.translator.createMediaCodeHeader(attrDesc, params))).isEqualTo("theCode");
        Assertions.assertThat(mediaImpex).isNotNull();
        Assertions.assertThat(mediaImpex.getImpexTable().row(Integer.valueOf(1))).isNotNull();
        Assertions.assertThat(mediaImpex.getImpexTable().row(Integer.valueOf(1)).keySet()).hasSize(4);
        Assertions.assertThat(mediaImpex.getImpexTable().row(Integer.valueOf(1)).keySet()).containsOnly((Object[])new ImpexHeaderValue[] {this.translator
                        .createMediaReferenceIdHeader(attrDesc, params2), this.translator.createMediaCodeHeader(attrDesc, params2), this.translator
                        .createMediaCatalogVersionHeader(attrDesc, params2), this.translator
                        .createMediaContentHeader(attrDesc, params2)});
        Assertions.assertThat(mediaImpex.getImpexTable().row(Integer.valueOf(1)).get(this.translator.createMediaCodeHeader(attrDesc, params2)))
                        .isEqualTo("generatedCode");
        ImpexForType productImpex = impex.findUpdates("Product");
        Assertions.assertThat(productImpex).isNotNull();
        Assertions.assertThat(productImpex.getImpexTable().get(Integer.valueOf(0), this.translator.createReferenceHeader(attrDesc)))
                        .isEqualTo(generateRefId(params) + "," + generateRefId(params));
    }


    @Test
    public void shouldNotImportContentIfFilePathIsEmpty()
    {
        Map<String, String> params1 = new HashMap<>();
        params1.put("code", "theCode1");
        params1.put("catalog", "default");
        params1.put("version", "staged");
        params1.put("folder", "folder");
        Mockito.when(this.mediaFolderProvider.provide(params1)).thenReturn("folder");
        Map<String, String> params2 = new HashMap<>();
        params2.put("code", "theCode2");
        params2.put("catalog", "default");
        params2.put("version", "staged");
        params2.put("folder", "folder");
        Mockito.when(this.mediaFolderProvider.provide(params2)).thenReturn("folder");
        AttributeDescriptorModel attrDesc = BackofficeTestUtil.mockCollectionTypeAttributeDescriptor("Media");
        ImportParameters importParameters = new ImportParameters("a", "b", "c", "d", Lists.newArrayList((Object[])new Map[] {params1, params2}));
        Impex impex = this.translator.importData(attrDesc, importParameters);
        ImpexForType mediaImpex = impex.findUpdates("Media");
        Assertions.assertThat(mediaImpex).isNotNull();
        Assertions.assertThat(mediaImpex.getImpexTable().row(Integer.valueOf(0))).isNotNull();
        Assertions.assertThat(mediaImpex.getImpexTable().row(Integer.valueOf(0)).keySet()).hasSize(4);
        Assertions.assertThat(mediaImpex.getImpexTable().row(Integer.valueOf(0)).keySet()).containsOnly((Object[])new ImpexHeaderValue[] {this.translator
                        .createMediaReferenceIdHeader(attrDesc, params1), this.translator.createMediaCodeHeader(attrDesc, params1), this.translator
                        .createMediaCatalogVersionHeader(attrDesc, params1), this.translator.createMediaFolderHeader(attrDesc, params1)});
        Assertions.assertThat(mediaImpex.getImpexTable().get(Integer.valueOf(0), this.translator.createMediaCodeHeader(attrDesc, params1))).isEqualTo("theCode1");
        Assertions.assertThat(mediaImpex.getImpexTable().row(Integer.valueOf(1))).isNotNull();
        Assertions.assertThat(mediaImpex.getImpexTable().row(Integer.valueOf(1)).keySet()).hasSize(4);
        Assertions.assertThat(mediaImpex.getImpexTable().row(Integer.valueOf(1)).keySet()).containsOnly((Object[])new ImpexHeaderValue[] {this.translator
                        .createMediaReferenceIdHeader(attrDesc, params2), this.translator.createMediaCodeHeader(attrDesc, params2), this.translator
                        .createMediaCatalogVersionHeader(attrDesc, params2), this.translator.createMediaFolderHeader(attrDesc, params2)});
        Assertions.assertThat(mediaImpex.getImpexTable().get(Integer.valueOf(1), this.translator.createMediaCodeHeader(attrDesc, params2))).isEqualTo("theCode2");
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
        MediaModel media1 = (MediaModel)Mockito.mock(MediaModel.class);
        MediaFolderModel mediaFolder1 = (MediaFolderModel)Mockito.mock(MediaFolderModel.class);
        MediaFolderModel mediaFolder2 = (MediaFolderModel)Mockito.mock(MediaFolderModel.class);
        Mockito.when(media1.getCode()).thenReturn("theCode1");
        Mockito.when(media1.getCatalogVersion()).thenReturn(cv);
        Mockito.when(media1.getFolder()).thenReturn(mediaFolder1);
        Mockito.when(mediaFolder1.getQualifier()).thenReturn("folder1");
        MediaModel media2 = (MediaModel)Mockito.mock(MediaModel.class);
        Mockito.when(media2.getCode()).thenReturn("theCode2");
        Mockito.when(media2.getCatalogVersion()).thenReturn(cv);
        Mockito.when(media2.getFolder()).thenReturn(mediaFolder2);
        Mockito.when(mediaFolder2.getQualifier()).thenReturn("folder2");
        Assertions.assertThat(this.translator.exportData(Lists.newArrayList((Object[])new MediaModel[] {media1, media2})).isPresent()).isTrue();
        Assertions.assertThat(this.translator.exportData(Lists.newArrayList((Object[])new MediaModel[] {media1, media2})).get())
                        .isEqualTo(":theCode1:default:staged:folder1,:theCode2:default:staged:folder2");
    }


    @Test
    public void shouldGivenTypeBeHandled()
    {
        AttributeDescriptorModel attributeDescriptor = BackofficeTestUtil.mockCollectionTypeAttributeDescriptor("Media");
        Assertions.assertThat(this.translator.canHandle(attributeDescriptor)).isTrue();
    }


    @Test
    public void shouldNotHandleOtherTypes()
    {
        AttributeDescriptorModel attributeDescriptor = BackofficeTestUtil.mockCollectionTypeAttributeDescriptor("Product");
        Assertions.assertThat(this.translator.canHandle(attributeDescriptor)).isFalse();
    }


    @Test
    public void shouldGenerateTheSameRefId()
    {
        ((ExcelMediaCollectionImportTranslator)Mockito.doCallRealMethod().when(this.translator)).generateMediaRefId((AttributeDescriptorModel)Matchers.any(), (Map)Matchers.any());
        Map<String, String> params1 = new HashMap<>();
        params1.put("code", "theCode1");
        params1.put("catalog", "default");
        params1.put("version", "staged");
        Map<String, String> params2 = new HashMap<>();
        params2.put("code", "theCode1");
        params2.put("catalog", "default");
        params2.put("version", "staged");
        Assertions.assertThat(this.translator.generateMediaRefId(null, params1)).isEqualTo(this.translator.generateMediaRefId(null, params2));
    }


    @Test
    public void shouldGenerateDifferentRefId()
    {
        ((ExcelMediaCollectionImportTranslator)Mockito.doCallRealMethod().when(this.translator)).generateMediaRefId((AttributeDescriptorModel)Matchers.any(), (Map)Matchers.any());
        Map<String, String> params1 = new HashMap<>();
        params1.put("code", "theCode1");
        params1.put("catalog", "Default");
        params1.put("version", "Staged");
        Map<String, String> params2 = new HashMap<>(params1);
        params2.put("code", "differentCode");
        Assertions.assertThat(this.translator.generateMediaRefId(null, params1)).isNotEqualTo(this.translator.generateMediaRefId(null, params2));
        Map<String, String> params3 = new HashMap<>(params1);
        params3.put("catalog", "notDefault");
        Assertions.assertThat(this.translator.generateMediaRefId(null, params1)).isNotEqualTo(this.translator.generateMediaRefId(null, params3));
        Map<String, String> params4 = new HashMap<>(params1);
        params4.put("version", "notStaged");
        Assertions.assertThat(this.translator.generateMediaRefId(null, params1)).isNotEqualTo(this.translator.generateMediaRefId(null, params4));
    }
}
