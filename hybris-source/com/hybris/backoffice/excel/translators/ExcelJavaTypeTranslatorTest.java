package com.hybris.backoffice.excel.translators;

import com.hybris.backoffice.excel.data.ImpexValue;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.template.filter.ExcelFilter;
import com.hybris.backoffice.excel.util.DefaultExcelDateUtils;
import com.hybris.backoffice.excel.util.ExcelDateUtils;
import de.hybris.platform.core.model.type.AtomicTypeModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.i18n.I18NService;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;
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
public class ExcelJavaTypeTranslatorTest
{
    @Mock
    private ExcelFilter<AttributeDescriptorModel> uniqueFilter;
    @Mock
    private ExcelFilter<AttributeDescriptorModel> mandatoryFilter;
    private final ExcelJavaTypeTranslator translator = new ExcelJavaTypeTranslator();
    private final DefaultExcelDateUtils excelDateUtils = new DefaultExcelDateUtils();


    @Before
    public void setUp()
    {
        ((ExcelFilter)Mockito.doAnswer(inv -> ((AttributeDescriptorModel)inv.getArguments()[0]).getUnique()).when(this.uniqueFilter)).test(Matchers.any());
        this.translator.setExcelUniqueFilter(this.uniqueFilter);
        this.translator.setMandatoryFilter(this.mandatoryFilter);
        this.translator.setExcelDateUtils((ExcelDateUtils)this.excelDateUtils);
        I18NService i18NService = (I18NService)Mockito.mock(I18NService.class);
        Mockito.when(i18NService.getCurrentTimeZone()).thenReturn(TimeZone.getDefault());
        this.excelDateUtils.setI18NService(i18NService);
    }


    @Test
    public void shouldExportDataBeNullSafe()
    {
        Assertions.assertThat(this.translator.exportData(null).isPresent()).isFalse();
    }


    @Test
    public void shouldExportedDataBeInProperFormat()
    {
        String input = "input";
        Objects.requireNonNull(String.class);
        String output = this.translator.exportData("input").map(String.class::cast).get();
        Assertions.assertThat(output).isEqualTo("input");
    }


    @Test
    public void shouldGivenTypeBeHandled()
    {
        AtomicTypeModel atomicType = (AtomicTypeModel)Mockito.mock(AtomicTypeModel.class);
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(attributeDescriptor.getAttributeType()).willReturn(atomicType);
        boolean canHandle = this.translator.canHandle(attributeDescriptor);
        Assertions.assertThat(canHandle).isTrue();
    }


    @Test
    public void shouldDateBeParsedInLocalizedWay()
    {
        Date input = new Date();
        Objects.requireNonNull(String.class);
        String output = this.translator.exportData(input).map(String.class::cast).get();
        Assertions.assertThat(output).isEqualTo(this.excelDateUtils.exportDate(input));
    }


    @Test
    public void shouldImportStringData()
    {
        String value = "value";
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(attributeDescriptor.getQualifier()).willReturn("code");
        BDDMockito.given(attributeDescriptor.getAttributeType()).willReturn(Mockito.mock(TypeModel.class));
        ImportParameters importParameters = new ImportParameters("Product", null, "value", UUID.randomUUID().toString(), Collections.emptyList());
        ImpexValue impexValue = this.translator.importValue(attributeDescriptor, importParameters);
        Assertions.assertThat(impexValue.getValue()).isEqualTo("value");
        Assertions.assertThat(impexValue.getHeaderValue().getName()).isEqualTo("code");
    }


    @Test
    public void shouldImportNumberData()
    {
        Double value = Double.valueOf(3.14D);
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(attributeDescriptor.getQualifier()).willReturn("europe1Discounts");
        BDDMockito.given(attributeDescriptor.getAttributeType()).willReturn(Mockito.mock(TypeModel.class));
        ImportParameters importParameters = new ImportParameters("Product", null, value, UUID.randomUUID().toString(), Collections.emptyList());
        ImpexValue impexValue = this.translator.importValue(attributeDescriptor, importParameters);
        Assertions.assertThat(impexValue.getValue()).isEqualTo(value);
        Assertions.assertThat(impexValue.getHeaderValue().getName()).isEqualTo("europe1Discounts");
    }


    @Test
    public void shouldImportDateData()
    {
        String value = "10.12.2016 18:23:44";
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        TypeModel typeModel = (TypeModel)Mockito.mock(TypeModel.class);
        BDDMockito.given(attributeDescriptor.getQualifier()).willReturn("onlineDate");
        BDDMockito.given(attributeDescriptor.getAttributeType()).willReturn(typeModel);
        BDDMockito.given(typeModel.getCode()).willReturn(Date.class.getCanonicalName());
        ImportParameters importParameters = new ImportParameters("Product", null, "10.12.2016 18:23:44", UUID.randomUUID().toString(), Collections.emptyList());
        ImpexValue impexValue = this.translator.importValue(attributeDescriptor, importParameters);
        Assertions.assertThat(impexValue.getValue()).isEqualTo(this.excelDateUtils.importDate("10.12.2016 18:23:44"));
        Assertions.assertThat(impexValue.getHeaderValue().getName()).isEqualTo("onlineDate");
        Assertions.assertThat(impexValue.getHeaderValue().getDateFormat()).isEqualTo(this.excelDateUtils.getDateTimeFormat());
    }
}
