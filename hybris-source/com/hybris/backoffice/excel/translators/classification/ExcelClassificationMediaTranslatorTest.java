package com.hybris.backoffice.excel.translators.classification;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.Impex;
import com.hybris.backoffice.excel.data.ImpexForType;
import com.hybris.backoffice.excel.data.ImpexHeaderValue;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.importing.ExcelImportContext;
import com.hybris.backoffice.excel.translators.media.MediaFolderProvider;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExcelClassificationMediaTranslatorTest
{
    @Mock
    TypeService typeService;
    @Mock
    KeyGenerator mediaCodeGenerator;
    @Mock
    MediaFolderProvider mediaFolderProvider;
    @InjectMocks
    ExcelClassificationMediaTranslator excelClassificationMediaTranslator;


    @Test
    public void shouldHandleRequestWhenAttributeIsMedia()
    {
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ComposedTypeModel composedType = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        ExcelClassificationAttribute attribute = new ExcelClassificationAttribute();
        attribute.setAttributeAssignment(classAttributeAssignment);
        BDDMockito.given(classAttributeAssignment.getReferenceType()).willReturn(composedType);
        BDDMockito.given(composedType.getCode()).willReturn("Media");
        BDDMockito.given(Boolean.valueOf(this.typeService.isAssignableFrom("Media", "Media"))).willReturn(Boolean.valueOf(true));
        boolean canHandle = this.excelClassificationMediaTranslator.canHandleAttribute(attribute);
        Assertions.assertThat(canHandle).isTrue();
    }


    @Test
    public void shouldNotHandleRequestWhenAttributeIsNotMedia()
    {
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ComposedTypeModel composedType = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        ExcelClassificationAttribute attribute = new ExcelClassificationAttribute();
        attribute.setAttributeAssignment(classAttributeAssignment);
        BDDMockito.given(classAttributeAssignment.getReferenceType()).willReturn(composedType);
        BDDMockito.given(composedType.getCode()).willReturn("Product");
        BDDMockito.given(Boolean.valueOf(this.typeService.isAssignableFrom("Media", "Product"))).willReturn(Boolean.valueOf(false));
        boolean canHandle = this.excelClassificationMediaTranslator.canHandleAttribute(attribute);
        Assertions.assertThat(canHandle).isFalse();
    }


    @Test
    public void shouldGenerateReferenceFormatWithoutUrl()
    {
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ExcelClassificationAttribute attribute = new ExcelClassificationAttribute();
        attribute.setAttributeAssignment(classAttributeAssignment);
        this.excelClassificationMediaTranslator.setExportUrl(false);
        String referenceFormat = this.excelClassificationMediaTranslator.singleReferenceFormat(attribute);
        Assertions.assertThat(referenceFormat).isEqualTo("filePath:code:catalog:version:folder");
    }


    @Test
    public void shouldGenerateReferenceFormatWithUrl()
    {
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ExcelClassificationAttribute attribute = new ExcelClassificationAttribute();
        attribute.setAttributeAssignment(classAttributeAssignment);
        this.excelClassificationMediaTranslator.setExportUrl(true);
        String referenceFormat = this.excelClassificationMediaTranslator.singleReferenceFormat(attribute);
        Assertions.assertThat(referenceFormat).isEqualTo("filePath:code:catalog:version:folder:url");
    }


    @Test
    public void shouldExportEmptyResponseWhenValueIsNotMedia()
    {
        FeatureValue featureValue = new FeatureValue("not a media");
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ExcelClassificationAttribute attribute = new ExcelClassificationAttribute();
        attribute.setAttributeAssignment(classAttributeAssignment);
        Optional<String> exportedValue = this.excelClassificationMediaTranslator.exportSingle(attribute, featureValue);
        Assertions.assertThat(exportedValue).isEmpty();
    }


    @Test
    public void shouldExportResponseWithoutUrlWhenValueIsMedia()
    {
        MediaModel media = (MediaModel)Mockito.mock(MediaModel.class);
        MediaFolderModel mediaFolder = (MediaFolderModel)Mockito.mock(MediaFolderModel.class);
        CatalogVersionModel catalogVersion = (CatalogVersionModel)Mockito.mock(CatalogVersionModel.class);
        CatalogModel catalog = (CatalogModel)Mockito.mock(CatalogModel.class);
        FeatureValue featureValue = new FeatureValue(media);
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ExcelClassificationAttribute attribute = new ExcelClassificationAttribute();
        attribute.setAttributeAssignment(classAttributeAssignment);
        BDDMockito.given(media.getCatalogVersion()).willReturn(catalogVersion);
        BDDMockito.given(catalogVersion.getCatalog()).willReturn(catalog);
        BDDMockito.given(media.getFolder()).willReturn(mediaFolder);
        BDDMockito.given(mediaFolder.getQualifier()).willReturn("Public");
        BDDMockito.given(catalogVersion.getVersion()).willReturn("Online");
        BDDMockito.given(catalog.getId()).willReturn("Default");
        BDDMockito.given(media.getCode()).willReturn("img1");
        this.excelClassificationMediaTranslator.setExportUrl(false);
        Optional<String> exportedValue = this.excelClassificationMediaTranslator.exportSingle(attribute, featureValue);
        Assertions.assertThat(exportedValue).isPresent();
        Assertions.assertThat(exportedValue.get()).isEqualTo(":img1:Default:Online:Public");
    }


    @Test
    public void shouldExportResponseWithUrlWhenValueIsMedia()
    {
        MediaModel media = (MediaModel)Mockito.mock(MediaModel.class);
        MediaFolderModel mediaFolder = (MediaFolderModel)Mockito.mock(MediaFolderModel.class);
        CatalogVersionModel catalogVersion = (CatalogVersionModel)Mockito.mock(CatalogVersionModel.class);
        CatalogModel catalog = (CatalogModel)Mockito.mock(CatalogModel.class);
        FeatureValue featureValue = new FeatureValue(media);
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ExcelClassificationAttribute attribute = new ExcelClassificationAttribute();
        attribute.setAttributeAssignment(classAttributeAssignment);
        BDDMockito.given(media.getCatalogVersion()).willReturn(catalogVersion);
        BDDMockito.given(catalogVersion.getCatalog()).willReturn(catalog);
        BDDMockito.given(media.getFolder()).willReturn(mediaFolder);
        BDDMockito.given(mediaFolder.getQualifier()).willReturn("Public");
        BDDMockito.given(catalogVersion.getVersion()).willReturn("Online");
        BDDMockito.given(catalog.getId()).willReturn("Default");
        BDDMockito.given(media.getCode()).willReturn("img1");
        BDDMockito.given(media.getDownloadURL()).willReturn("https://localhost:9002/media/img1.png");
        this.excelClassificationMediaTranslator.setExportUrl(true);
        Optional<String> exportedValue = this.excelClassificationMediaTranslator.exportSingle(attribute, featureValue);
        Assertions.assertThat(exportedValue).isPresent();
        Assertions.assertThat(exportedValue.get()).isEqualTo(":img1:Default:Online:Public:\"https://localhost:9002/media/img1.png\"");
    }


    @Test
    public void shouldGenerateEmptyImpexWhenAttributeIsNotClassificationAttribute()
    {
        ExcelAttribute excelAttribute = (ExcelAttribute)Mockito.mock(ExcelAttribute.class);
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        ExcelImportContext excelImportContext = (ExcelImportContext)Mockito.mock(ExcelImportContext.class);
        Impex impex = this.excelClassificationMediaTranslator.importData(excelAttribute, importParameters, excelImportContext);
        Assertions.assertThat(impex.getImpexes()).isEmpty();
    }


    @Test
    public void shouldGenerateImpexHeaderForNotLocalizedField()
    {
        ExcelClassificationAttribute classificationAttribute = mockClassificationAttribute();
        ImpexHeaderValue mediaHeader = this.excelClassificationMediaTranslator.createMediaHeader(classificationAttribute);
        Assertions.assertThat(mediaHeader.getName()).isEqualTo("@images(code,catalogVersion(version, catalog(id)))[system='Default',attributeType='null',version='Online',translator=de.hybris.platform.catalog.jalo.classification.impex.ClassificationAttributeTranslator]");
    }


    @Test
    public void shouldGenerateImpexHeaderForLocalizedField()
    {
        ExcelClassificationAttribute classificationAttribute = mockClassificationAttribute();
        BDDMockito.given(classificationAttribute.getIsoCode()).willReturn("en");
        ImpexHeaderValue mediaHeader = this.excelClassificationMediaTranslator.createMediaHeader(classificationAttribute);
        Assertions.assertThat(mediaHeader.getName()).isEqualTo("@images(code,catalogVersion(version, catalog(id)))[system='Default',attributeType='null',version='Online',lang=en,translator=de.hybris.platform.catalog.jalo.classification.impex.ClassificationAttributeTranslator]");
    }


    @Test
    public void shouldGenerateImpexForSingleValue()
    {
        ExcelClassificationAttribute attribute = mockClassificationAttribute();
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        ExcelImportContext excelImportContext = (ExcelImportContext)Mockito.mock(ExcelImportContext.class);
        Map<String, String> params = new HashMap<>();
        params.put("code", "img1");
        params.put("filePath", "img.jpg");
        params.put("catalog", "Default");
        params.put("version", "Online");
        BDDMockito.given(importParameters.getMultiValueParameters()).willReturn(Collections.singletonList(params));
        BDDMockito.given(importParameters.getTypeCode()).willReturn("Product");
        Impex impex = this.excelClassificationMediaTranslator.importData((ExcelAttribute)attribute, importParameters, excelImportContext);
        Assertions.assertThat(impex.getImpexes()).hasSize(2);
        verifyProductImpex(impex);
        verifyMediaImpex(impex);
    }


    private void verifyProductImpex(Impex impex)
    {
        ImpexForType productImpex = impex.findUpdates("Product");
        Assertions.assertThat(productImpex.getImpexTable().columnKeySet()).hasSize(1);
        List<ImpexHeaderValue> impexProductHeader = new ArrayList<>(productImpex.getImpexTable().row(Integer.valueOf(0)).keySet());
        List<Object> impexProductValue = new ArrayList(productImpex.getImpexTable().row(Integer.valueOf(0)).values());
        Assertions.assertThat(((ImpexHeaderValue)impexProductHeader.get(0)).getName())
                        .isEqualTo("@images(code,catalogVersion(version, catalog(id)))[system='Default',attributeType='null',version='Online',translator=de.hybris.platform.catalog.jalo.classification.impex.ClassificationAttributeTranslator]");
        Assertions.assertThat(impexProductValue.get(0)).isEqualTo("img1:Online:Default");
    }


    private void verifyMediaImpex(Impex impex)
    {
        ImpexForType mediaImpex = impex.findUpdates("Media");
        Assertions.assertThat(mediaImpex.getImpexTable().columnKeySet()).hasSize(4);
        Assertions.assertThat(mediaImpex.getImpexTable().columnKeySet()).extracting("name").contains(new Object[] {"code", "catalogVersion(version,catalog(id))", "folder(qualifier)", "@media"});
        Assertions.assertThat(mediaImpex.getImpexTable().columnKeySet()).extracting("translator").contains(new Object[] {null, null, null, "de.hybris.platform.impex.jalo.media.MediaDataTranslator"});
        Assertions.assertThat(mediaImpex.getImpexTable().values()).contains(new Object[] {"img1", "Online:Default", "", "img.jpg"});
    }


    private ExcelClassificationAttribute mockClassificationAttribute()
    {
        ExcelClassificationAttribute classificationAttribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ClassificationClassModel classificationClass = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        ClassificationSystemModel classificationSystem = (ClassificationSystemModel)Mockito.mock(ClassificationSystemModel.class);
        ClassificationSystemVersionModel classificationSystemVersion = (ClassificationSystemVersionModel)Mockito.mock(ClassificationSystemVersionModel.class);
        BDDMockito.given(classificationAttribute.getAttributeAssignment()).willReturn(classAttributeAssignment);
        BDDMockito.given(classAttributeAssignment.getClassificationClass()).willReturn(classificationClass);
        BDDMockito.given(classificationClass.getCatalogVersion()).willReturn(classificationSystemVersion);
        BDDMockito.given(classificationAttribute.getQualifier()).willReturn("images");
        BDDMockito.given(classificationSystemVersion.getCatalog()).willReturn(classificationSystem);
        BDDMockito.given(classificationSystem.getId()).willReturn("Default");
        BDDMockito.given(classificationSystemVersion.getVersion()).willReturn("Online");
        return classificationAttribute;
    }
}
