package com.hybris.backoffice.excel.template.populator.typesheet;

import com.hybris.backoffice.excel.template.AttributeNameFormatter;
import com.hybris.backoffice.excel.template.CollectionFormatter;
import com.hybris.backoffice.excel.template.filter.ExcelFilter;
import com.hybris.backoffice.excel.template.populator.ExcelAttributeContext;
import com.hybris.backoffice.excel.translators.ExcelTranslatorRegistry;
import com.hybris.backoffice.excel.translators.ExcelValueTranslator;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import java.util.Collections;
import java.util.Optional;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TypeSystemRowFactoryTest
{
    @Mock
    ExcelFilter<AttributeDescriptorModel> uniqueFilter;
    @Mock
    CommonI18NService mockedCommonI18NService;
    @Mock
    ExcelTranslatorRegistry mockedExcelTranslatorRegistry;
    @Mock
    AttributeNameFormatter mockedAttributeNameFormatter;
    @Mock
    CollectionFormatter mockedCollectionFormatter;
    @InjectMocks
    TypeSystemRowFactory typeSystemRowFactory;


    @Before
    public void setUp()
    {
        ((ExcelFilter)Mockito.doAnswer(inv -> ((AttributeDescriptorModel)inv.getArguments()[0]).getUnique()).when(this.uniqueFilter)).test(Matchers.any());
    }


    @Test
    public void shouldCreateTypeSystemRowFormAttributeDescriptor()
    {
        ComposedTypeModel composedTypeModel = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        BDDMockito.given(composedTypeModel.getCode()).willReturn("enclosingTypeCode");
        BDDMockito.given(composedTypeModel.getName()).willReturn("enclosingTypeName");
        TypeModel typeModel = (TypeModel)Mockito.mock(TypeModel.class);
        BDDMockito.given(typeModel.getCode()).willReturn("attributeTypeCode");
        AttributeDescriptorModel attributeDescriptorModel = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(attributeDescriptorModel.getEnclosingType()).willReturn(composedTypeModel);
        BDDMockito.given(attributeDescriptorModel.getQualifier()).willReturn("qualifier");
        BDDMockito.given(attributeDescriptorModel.getName()).willReturn("name");
        BDDMockito.given(attributeDescriptorModel.getOptional()).willReturn(Boolean.valueOf(true));
        BDDMockito.given(attributeDescriptorModel.getAttributeType()).willReturn(typeModel);
        BDDMockito.given(attributeDescriptorModel.getDeclaringEnclosingType()).willReturn(composedTypeModel);
        BDDMockito.given(attributeDescriptorModel.getLocalized()).willReturn(Boolean.valueOf(true));
        BDDMockito.given(attributeDescriptorModel.getUnique()).willReturn(Boolean.valueOf(true));
        BDDMockito.given(this.mockedCollectionFormatter.formatToString(new String[] {"enclosingTypeCode"})).willReturn("formattedEnclosingTypeCode");
        BDDMockito.given(this.mockedCommonI18NService.getAllLanguages()).willReturn(Collections.emptyList());
        BDDMockito.given(this.mockedExcelTranslatorRegistry.getTranslator(attributeDescriptorModel)).willReturn(Optional.empty());
        TypeSystemRow typeSystemRow = this.typeSystemRowFactory.create(attributeDescriptorModel);
        Assertions.assertThat(typeSystemRow.getTypeCode()).isEqualTo("formattedEnclosingTypeCode");
        Assertions.assertThat(typeSystemRow.getTypeName()).isEqualTo("enclosingTypeName");
        Assertions.assertThat(typeSystemRow.getAttrQualifier()).isEqualTo("qualifier");
        Assertions.assertThat(typeSystemRow.getAttrName()).isEqualTo("name");
        Assertions.assertThat(typeSystemRow.getAttrOptional()).isEqualTo(true);
        Assertions.assertThat(typeSystemRow.getAttrTypeCode()).isEqualTo("attributeTypeCode");
        Assertions.assertThat(typeSystemRow.getAttrTypeItemType()).isEqualTo("enclosingTypeCode");
        Assertions.assertThat(typeSystemRow.getAttrLocalized()).isEqualTo(true);
        Assertions.assertThat(typeSystemRow.getAttrUnique()).isEqualTo(true);
    }


    @Test
    public void shouldCreateTypeSystemWithAttributeDisplayName()
    {
        AttributeDescriptorModel attributeDescriptorModel = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(attributeDescriptorModel.getAttributeType()).willReturn(Mockito.mock(TypeModel.class));
        BDDMockito.given(attributeDescriptorModel.getName()).willReturn("name");
        BDDMockito.given(attributeDescriptorModel.getQualifier()).willReturn("qualifier");
        BDDMockito.given(attributeDescriptorModel.getEnclosingType()).willReturn(Mockito.mock(ComposedTypeModel.class));
        BDDMockito.given(attributeDescriptorModel.getDeclaringEnclosingType()).willReturn(Mockito.mock(ComposedTypeModel.class));
        String isoCode = "isoCode";
        String displayName = "name[qualifier]*";
        String formattedAttributeDisplayName = "formattedAttributeDisplayName";
        LanguageModel languageModel = (LanguageModel)Mockito.mock(LanguageModel.class);
        BDDMockito.given(languageModel.getActive()).willReturn(Boolean.valueOf(true));
        BDDMockito.given(languageModel.getIsocode()).willReturn("isoCode");
        BDDMockito.given(this.mockedAttributeNameFormatter.format((ExcelAttributeContext)Matchers.any())).willReturn("name[qualifier]*");
        BDDMockito.given(this.mockedCommonI18NService.getAllLanguages()).willReturn(Collections.singletonList(languageModel));
        BDDMockito.given(this.mockedExcelTranslatorRegistry.getTranslator(attributeDescriptorModel)).willReturn(Optional.empty());
        BDDMockito.given(this.mockedCollectionFormatter.formatToString(Collections.singletonList("name[qualifier]*")))
                        .willReturn("formattedAttributeDisplayName");
        TypeSystemRow typeSystemRow = this.typeSystemRowFactory.create(attributeDescriptorModel);
        Assertions.assertThat(typeSystemRow.getAttrDisplayName()).isEqualTo("formattedAttributeDisplayName");
    }


    @Test
    public void shouldCreateTypeSystemWithReferenceFormat()
    {
        AttributeDescriptorModel attributeDescriptorModel = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(attributeDescriptorModel.getAttributeType()).willReturn(Mockito.mock(TypeModel.class));
        BDDMockito.given(attributeDescriptorModel.getEnclosingType()).willReturn(Mockito.mock(ComposedTypeModel.class));
        BDDMockito.given(attributeDescriptorModel.getDeclaringEnclosingType()).willReturn(Mockito.mock(ComposedTypeModel.class));
        ExcelValueTranslator excelValueTranslator = (ExcelValueTranslator)Mockito.mock(ExcelValueTranslator.class);
        BDDMockito.given(excelValueTranslator.referenceFormat(attributeDescriptorModel)).willReturn("referenceFormat");
        BDDMockito.given(this.mockedCommonI18NService.getAllLanguages()).willReturn(Collections.emptyList());
        BDDMockito.given(this.mockedExcelTranslatorRegistry.getTranslator(attributeDescriptorModel)).willReturn(Optional.of(excelValueTranslator));
        TypeSystemRow typeSystemRow = this.typeSystemRowFactory.create(attributeDescriptorModel);
        Assertions.assertThat(typeSystemRow.getAttrReferenceFormat()).isEqualTo("referenceFormat");
    }


    @Test
    public void shouldMergeTwoTypeSystemRows()
    {
        TypeSystemRow typeSystemRow1 = createTypeSystemRow("{typeCode}", "typeName", "attrQualifier", "attrName", "attrTypeCode", "attrTypeItemType", "attrLocLang", "attrDisplayName", "attrReferenceFormat");
        TypeSystemRow typeSystemRow2 = createTypeSystemRow("{typeCode2}", "typeName2", "attrQualifier2", "attrName2", "attrTypeCode2", "attrTypeItemType2", "attrLocLang2", "attrDisplayName2", "attrReferenceFormat2");
        TypeSystemRow result = this.typeSystemRowFactory.merge(typeSystemRow1, typeSystemRow2);
        Assertions.assertThat(result.getTypeCode()).isEqualTo("{typeCode},{typeCode2}");
        Assertions.assertThat(result.getTypeName()).isEqualTo("typeName");
        Assertions.assertThat(result.getAttrQualifier()).isEqualTo("attrQualifier");
        Assertions.assertThat(result.getAttrName()).isEqualTo("attrName");
        Assertions.assertThat(result.getAttrOptional()).isEqualTo(true);
        Assertions.assertThat(result.getAttrTypeCode()).isEqualTo("attrTypeCode");
        Assertions.assertThat(result.getAttrTypeItemType()).isEqualTo("attrTypeItemType");
        Assertions.assertThat(result.getAttrLocalized()).isEqualTo(true);
        Assertions.assertThat(result.getAttrLocLang()).isEqualTo("attrLocLang");
        Assertions.assertThat(result.getAttrDisplayName()).isEqualTo("attrDisplayName");
        Assertions.assertThat(result.getAttrUnique()).isEqualTo(true);
        Assertions.assertThat(result.getAttrReferenceFormat()).isEqualTo("attrReferenceFormat");
    }


    private static TypeSystemRow createTypeSystemRow(String typeCode, String typeName, String attrQualifier, String attrName, String attrTypeCode, String attrTypeItemType, String attrLocLang, String attrDisplayName, String attrReferenceFormat)
    {
        TypeSystemRow typeSystemRow = new TypeSystemRow();
        typeSystemRow.setTypeCode(typeCode);
        typeSystemRow.setTypeName(typeName);
        typeSystemRow.setAttrQualifier(attrQualifier);
        typeSystemRow.setAttrName(attrName);
        typeSystemRow.setAttrOptional(Boolean.valueOf(true));
        typeSystemRow.setAttrTypeCode(attrTypeCode);
        typeSystemRow.setAttrTypeItemType(attrTypeItemType);
        typeSystemRow.setAttrLocalized(Boolean.valueOf(true));
        typeSystemRow.setAttrLocLang(attrLocLang);
        typeSystemRow.setAttrDisplayName(attrDisplayName);
        typeSystemRow.setAttrUnique(Boolean.valueOf(true));
        typeSystemRow.setAttrReferenceFormat(attrReferenceFormat);
        return typeSystemRow;
    }
}
