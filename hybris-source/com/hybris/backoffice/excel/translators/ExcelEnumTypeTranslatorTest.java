package com.hybris.backoffice.excel.translators;

import com.hybris.backoffice.excel.data.ImpexValue;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.template.filter.ExcelFilter;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.enumeration.EnumerationMetaTypeModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExcelEnumTypeTranslatorTest
{
    @Mock
    private ExcelFilter<AttributeDescriptorModel> mandatoryFilter;
    @Mock
    private ExcelFilter<AttributeDescriptorModel> uniqueFilter;
    private final ExcelEnumTypeTranslator translator = new ExcelEnumTypeTranslator();


    @Before
    public void setUp()
    {
        ((ExcelFilter)Mockito.doAnswer(inv -> ((AttributeDescriptorModel)inv.getArguments()[0]).getUnique()).when(this.uniqueFilter)).test(Matchers.any());
        this.translator.setExcelUniqueFilter(this.uniqueFilter);
        this.translator.setMandatoryFilter(this.mandatoryFilter);
    }


    @Test
    public void shouldExportDataBeNullSafe()
    {
        Assertions.assertThat(this.translator.exportData(null).isPresent()).isFalse();
    }


    @Test
    public void shouldExportedDataBeInProperFormat()
    {
        String code = "some";
        HybrisEnumValue hybrisEnumValue = (HybrisEnumValue)Mockito.mock(HybrisEnumValue.class);
        BDDMockito.given(hybrisEnumValue.getCode()).willReturn("some");
        Objects.requireNonNull(String.class);
        String output = this.translator.exportData(hybrisEnumValue).map(String.class::cast).get();
        Assertions.assertThat(output).isEqualTo("some");
    }


    @Test
    public void shouldGivenTypeBeHandled()
    {
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        EnumerationMetaTypeModel enumerationMetaType = (EnumerationMetaTypeModel)Mockito.mock(EnumerationMetaTypeModel.class);
        BDDMockito.given(attributeDescriptor.getAttributeType()).willReturn(enumerationMetaType);
        boolean canHandle = this.translator.canHandle(attributeDescriptor);
        Assertions.assertThat(canHandle).isTrue();
    }


    @Test
    public void shouldImportEnumValue()
    {
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(attributeDescriptor.getQualifier()).willReturn("approvalStatus");
        String cellValue = "approved";
        ImportParameters importParameters = new ImportParameters("Product", null, "approved", UUID.randomUUID().toString(), Collections.emptyList());
        ImpexValue impexValue = this.translator.importValue(attributeDescriptor, importParameters);
        Assertions.assertThat(impexValue.getValue()).isEqualTo("approved");
        Assertions.assertThat(impexValue.getHeaderValue().getName()).isEqualTo(String.format("%s(code)", new Object[] {"approvalStatus"}));
    }
}
