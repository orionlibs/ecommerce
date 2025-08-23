package com.hybris.backoffice.excel.validators;

import com.google.common.collect.Lists;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.importing.ExcelImportService;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExcelMediaCollectionImportValidatorTest
{
    @InjectMocks
    @Spy
    private ExcelMediaCollectionImportValidator validator;
    @Mock
    private TypeService typeService;
    @Mock
    private ExcelImportService importService;


    @Before
    public void setUp()
    {
        this.validator.setSingleMediaValidators(Lists.newArrayList((Object[])new ExcelSingleMediaValidator[] {(ExcelSingleMediaValidator)new ExcelMediaImportValidator()}));
        Mockito.when(Boolean.valueOf(this.typeService.isAssignableFrom("Media", "Media"))).thenReturn(Boolean.valueOf(true));
    }


    @Test
    public void shouldHandleMediaType()
    {
        Map<String, String> params = new HashMap<>();
        params.put("code", "theCode");
        AttributeDescriptorModel attrDesc = mockCollectionOfType("Media");
        ImportParameters importParameters = new ImportParameters("a", "b", "c", "d", Lists.newArrayList((Object[])new Map[] {params}));
        Assertions.assertThat(this.validator.canHandle(importParameters, attrDesc)).isTrue();
    }


    @Test
    public void shouldNotHandleProductType()
    {
        Map<String, String> params = new HashMap<>();
        params.put("code", "theCode");
        AttributeDescriptorModel attrDesc = mockCollectionOfType("Product");
        ImportParameters importParameters = new ImportParameters("a", "b", "c", "d", Lists.newArrayList((Object[])new Map[] {params}));
        Assertions.assertThat(this.validator.canHandle(importParameters, attrDesc)).isFalse();
    }


    @Test
    public void shouldValidateAllEntries()
    {
        Map<String, String> params = new HashMap<>();
        Map<String, String> params2 = new HashMap<>();
        AttributeDescriptorModel attrDesc = mockCollectionOfType("Media");
        ImportParameters importParameters = new ImportParameters("a", "b", "c", "d", Lists.newArrayList((Object[])new Map[] {params, params2}));
        ExcelValidationResult validate = this.validator.validate(importParameters, attrDesc, new HashMap<>());
        Assertions.assertThat(validate.getValidationErrors()).hasSize(2);
        Assertions.assertThat(((ValidationMessage)validate.getValidationErrors().get(0)).getMessageKey())
                        .isEqualTo("excel.import.validation.media.pathandcode.empty");
        Assertions.assertThat(((ValidationMessage)validate.getValidationErrors().get(1)).getMessageKey())
                        .isEqualTo("excel.import.validation.media.pathandcode.empty");
    }


    protected AttributeDescriptorModel mockCollectionOfType(String typecode)
    {
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        CollectionTypeModel collectionType = (CollectionTypeModel)Mockito.mock(CollectionTypeModel.class);
        Mockito.lenient().when(collectionType.getCode()).thenReturn("CollectionType");
        Mockito.when(attributeDescriptor.getAttributeType()).thenReturn(collectionType);
        TypeModel typeModel = (TypeModel)Mockito.mock(TypeModel.class);
        Mockito.when(typeModel.getCode()).thenReturn(typecode);
        Mockito.when(collectionType.getElementType()).thenReturn(typeModel);
        return attributeDescriptor;
    }
}
