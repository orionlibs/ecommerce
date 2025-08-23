package com.hybris.backoffice.excel.translators;

import com.google.common.collect.Lists;
import com.hybris.backoffice.excel.data.ImpexValue;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.util.DefaultExcelDateUtils;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.enums.PriceRowChannel;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.europe1.jalo.impex.Europe1PricesTranslator;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.util.StandardDateRange;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
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
public class ExcelEurope1PricesTypeTranslatorTest
{
    private static final String TEN_EUR = "10 EUR";
    @Spy
    private final DefaultExcelDateUtils excelDateUtils = new DefaultExcelDateUtils();
    @Mock
    private CommonI18NService commonI18NService;
    @Spy
    @InjectMocks
    private ExcelEurope1PricesTypeTranslator translator;
    private Date dateFrom;
    private Date dateTo;
    private StandardDateRange dateRange;
    private CurrencyModel currencyUsd;
    private UnitModel pieces;
    private UserModel admin;
    private AttributeDescriptorModel attributeDescriptor;


    @Before
    public void setUp()
    {
        this.dateFrom = Date.from(LocalDateTime.of(2017, 10, 23, 10, 46).toInstant(ZoneOffset.UTC));
        this.dateTo = Date.from(LocalDateTime.of(2017, 12, 11, 4, 22).toInstant(ZoneOffset.UTC));
        this.dateRange = new StandardDateRange(this.dateFrom, this.dateTo);
        this.currencyUsd = (CurrencyModel)Mockito.mock(CurrencyModel.class);
        Mockito.when(this.currencyUsd.getIsocode()).thenReturn("USD");
        this.pieces = (UnitModel)Mockito.mock(UnitModel.class);
        Mockito.when(this.pieces.getCode()).thenReturn("pieces");
        this.admin = (UserModel)Mockito.mock(UserModel.class);
        Mockito.when(this.admin.getUid()).thenReturn("admin");
        this.attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        I18NService i18NService = (I18NService)Mockito.mock(I18NService.class);
        Mockito.when(i18NService.getCurrentTimeZone()).thenReturn(TimeZone.getDefault());
        this.excelDateUtils.setI18NService(i18NService);
    }


    @Test
    public void testExportDataWithAllValues()
    {
        PriceRowModel priceRowModel = mockPriceRow();
        Optional<Object> data = this.translator.exportData(Lists.newArrayList((Object[])new PriceRowModel[] {priceRowModel}));
        Assertions.assertThat(data.isPresent()).isTrue();
        Assertions.assertThat(data.get()).isEqualTo("20.0 USD:N:admin:2 pieces:" + formatExcelDateRange(this.dateFrom, this.dateTo) + ":mobile");
    }


    @Test
    public void testExportDataNullDateRange()
    {
        PriceRowModel priceRowModel = mockPriceRow();
        Mockito.when(priceRowModel.getDateRange()).thenReturn(null);
        Optional<Object> data = this.translator.exportData(Lists.newArrayList((Object[])new PriceRowModel[] {priceRowModel}));
        Assertions.assertThat(data.isPresent()).isTrue();
        Assertions.assertThat(data.get()).isEqualTo("20.0 USD:N:admin:2 pieces::mobile");
    }


    @Test
    public void testExportDataUserGroupGiven()
    {
        UserPriceGroup priceGroup = (UserPriceGroup)Mockito.mock(UserPriceGroup.class);
        Mockito.when(priceGroup.getCode()).thenReturn("priceGroup");
        PriceRowModel priceRowModel = mockPriceRow();
        Mockito.when(priceRowModel.getUg()).thenReturn(priceGroup);
        Optional<Object> data = this.translator.exportData(Lists.newArrayList((Object[])new PriceRowModel[] {priceRowModel}));
        Assertions.assertThat(data.isPresent()).isTrue();
        Assertions.assertThat(data.get()).isEqualTo("20.0 USD:N:priceGroup:2 pieces:" + formatExcelDateRange(this.dateFrom, this.dateTo) + ":mobile");
    }


    @Test
    public void testImportData()
    {
        ImportParameters importParameters = new ImportParameters("a", "b", "c", "d", Lists.newArrayList((Object[])new Map[] {importParams()}));
        ImpexValue impexValue = this.translator.importValue(this.attributeDescriptor, importParameters);
        Assertions.assertThat(impexValue.getValue())
                        .isEqualTo("axel 10 pieces = 10 EUR G " + formatTranslatorDateRange(this.dateFrom, this.dateTo) + " mobile");
        Assertions.assertThat(impexValue.getHeaderValue().getDateFormat()).isEqualTo(this.excelDateUtils.getDateTimeFormat());
        Assertions.assertThat(impexValue.getHeaderValue().getTranslator()).isEqualTo(Europe1PricesTranslator.class.getName());
    }


    @Test
    public void testImportSimpleDate()
    {
        Map<String, String> params = new HashMap<>();
        params.put("price currency", "10 EUR");
        ImportParameters importParameters = new ImportParameters("a", "b", "c", "d", Lists.newArrayList((Object[])new Map[] {params}));
        ImpexValue impexValue = this.translator.importValue(this.attributeDescriptor, importParameters);
        Assertions.assertThat(impexValue.getValue()).isEqualTo("10 EUR");
        Assertions.assertThat(impexValue.getHeaderValue().getDateFormat()).isEqualTo(this.excelDateUtils.getDateTimeFormat());
        Assertions.assertThat(impexValue.getHeaderValue().getTranslator()).isEqualTo(Europe1PricesTranslator.class.getName());
    }


    @Test
    public void testImportDoublePriceInEnglish()
    {
        Map<String, String> params = new HashMap<>();
        params.put("price currency", "10.5 USD");
        ImportParameters importParameters = new ImportParameters("a", "b", "c", "d", Lists.newArrayList((Object[])new Map[] {params}));
        ImpexValue impexValue = this.translator.importValue(this.attributeDescriptor, importParameters);
        Assertions.assertThat(impexValue.getValue()).isEqualTo("10.5 USD");
    }


    @Test
    public void shouldNotThrowErrorIfPriceCurrencyIsNull()
    {
        Map<String, String> params = new HashMap<>();
        params.put("price currency", null);
        ImportParameters importParameters = new ImportParameters("a", "b", "c", "d", Lists.newArrayList((Object[])new Map[] {params}));
        ImpexValue impexValue = this.translator.importValue(this.attributeDescriptor, importParameters);
        Assertions.assertThat(impexValue.getValue()).isEqualTo("");
    }


    @Test
    public void testHeaderValueIsEqualToAttributeDescriptorQualifier()
    {
        ImportParameters importParameters = new ImportParameters("typeCode", "isoCode", "cellValue", "entryRef", "formatError");
        String expectedAttributeDescriptorQualifier = "expectedAttributeDescriptorQualifier";
        Mockito.when(this.attributeDescriptor.getQualifier()).thenReturn("expectedAttributeDescriptorQualifier");
        ImpexValue impexValue = this.translator.importValue(this.attributeDescriptor, importParameters);
        Assertions.assertThat(impexValue.getHeaderValue().getName()).isEqualTo("expectedAttributeDescriptorQualifier");
    }


    protected Map<String, String> importParams()
    {
        Map<String, String> params = new HashMap<>();
        params.put("price currency", "10 EUR");
        params.put("N|G", "G");
        params.put("user|userPriceGroup", "axel");
        params.put("minQuantity unit", "10 pieces");
        params.put(this.excelDateUtils.getDateRangeParamKey(), this.excelDateUtils
                        .exportDate(this.dateFrom) + " to " + this.excelDateUtils.exportDate(this.dateFrom));
        params.put("channel", "mobile");
        return params;
    }


    protected PriceRowModel mockPriceRow()
    {
        PriceRowModel priceRowModel = (PriceRowModel)Mockito.mock(PriceRowModel.class);
        Mockito.when(priceRowModel.getCurrency()).thenReturn(this.currencyUsd);
        Mockito.when(priceRowModel.getPrice()).thenReturn(Double.valueOf(20.0D));
        Mockito.when(priceRowModel.getNet()).thenReturn(Boolean.valueOf(true));
        Mockito.when(priceRowModel.getUnit()).thenReturn(this.pieces);
        Mockito.when(priceRowModel.getMinqtd()).thenReturn(Long.valueOf(2L));
        Mockito.when(priceRowModel.getChannel()).thenReturn(PriceRowChannel.MOBILE);
        Mockito.when(priceRowModel.getDateRange()).thenReturn(this.dateRange);
        Mockito.when(priceRowModel.getUser()).thenReturn(this.admin);
        return priceRowModel;
    }


    protected String formatExcelDateRange(Date from, Date to)
    {
        return String.format("[%s to %s]", new Object[] {this.excelDateUtils.exportDate(from), this.excelDateUtils.exportDate(to)});
    }


    protected String formatTranslatorDateRange(Date from, Date to)
    {
        return String.format("[%s,%s]", new Object[] {this.excelDateUtils.importDate(this.excelDateUtils.exportDate(from)), this.excelDateUtils
                        .importDate(this.excelDateUtils.exportDate(to))});
    }
}
