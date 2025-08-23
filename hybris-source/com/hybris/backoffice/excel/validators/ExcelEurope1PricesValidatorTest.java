package com.hybris.backoffice.excel.validators;

import com.google.common.collect.Sets;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.util.DefaultExcelDateUtils;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.europe1.enums.PriceRowChannel;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.product.UnitService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.i18n.daos.CurrencyDao;
import de.hybris.platform.servicelayer.user.UserService;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExcelEurope1PricesValidatorTest
{
    public static final String EXISTING_USER = "existingUser";
    public static final String EXISTING_CHANNEL = "existingChannel";
    public static final String EXISTING_PRICE_GROUP = "existingPriceGroup";
    public static final String EXISTING_UNIT = "existingUnit";
    public static final String CORRECT_PRICE_QUANTITY = "10 existingUnit";
    public static final String CORRECT_PRICE_CURRENCY = "10 usd";
    public static final String NOT_EXISTING_UPG_USER = "abc";
    public static final String NON_EXISTING_CHANNEL = "anyChannel";
    public static final String NOT_BLANK = "not blank";
    @Mock
    private CurrencyDao currencyDao;
    @Mock
    private UnitService unitService;
    @Mock
    private EnumerationService enumerationService;
    @Mock
    private UserService userService;
    @Spy
    private final DefaultExcelDateUtils excelDateUtils = new DefaultExcelDateUtils();
    @InjectMocks
    private ExcelEurope1PricesValidator excelPriceValidator;
    private Date dateFrom;
    private Date dateTo;


    @Before
    public void setup()
    {
        this.dateFrom = Date.from(LocalDateTime.of(2017, 10, 23, 10, 46).toInstant(ZoneOffset.UTC));
        this.dateTo = Date.from(LocalDateTime.of(2017, 12, 11, 4, 22).toInstant(ZoneOffset.UTC));
        CurrencyModel usdModel = (CurrencyModel)Mockito.mock(CurrencyModel.class);
        CurrencyModel euroModel = (CurrencyModel)Mockito.mock(CurrencyModel.class);
        BDDMockito.given(usdModel.getIsocode()).willReturn("usd");
        BDDMockito.given(euroModel.getIsocode()).willReturn("euro");
        BDDMockito.given(this.currencyDao.findCurrencies()).willReturn(Arrays.asList(new CurrencyModel[] {usdModel, euroModel}));
        UserModel user = (UserModel)Mockito.mock(UserModel.class);
        BDDMockito.given(this.userService.getUserForUID("existingUser")).willReturn(user);
        BDDMockito.given(this.userService.getUserForUID("abc")).willThrow(new Throwable[] {(Throwable)new UnknownIdentifierException("")});
        PriceRowChannel channel = (PriceRowChannel)Mockito.mock(PriceRowChannel.class);
        BDDMockito.given(this.enumerationService.getEnumerationValue(PriceRowChannel.class, "anyChannel"))
                        .willThrow(new Throwable[] {(Throwable)new UnknownIdentifierException("")});
        BDDMockito.given(this.enumerationService.getEnumerationValue(PriceRowChannel.class, "existingChannel")).willReturn(channel);
        UserPriceGroup priceGroup = (UserPriceGroup)Mockito.mock(UserPriceGroup.class);
        BDDMockito.given(this.enumerationService.getEnumerationValue(UserPriceGroup.class, "abc"))
                        .willThrow(new Throwable[] {(Throwable)new UnknownIdentifierException("")});
        BDDMockito.given(this.enumerationService.getEnumerationValue(UserPriceGroup.class, "existingUser"))
                        .willThrow(new Throwable[] {(Throwable)new UnknownIdentifierException("")});
        BDDMockito.given(this.enumerationService.getEnumerationValue(UserPriceGroup.class, "existingPriceGroup")).willReturn(priceGroup);
        UnitModel unit = (UnitModel)Mockito.mock(UnitModel.class);
        Mockito.when(unit.getCode()).thenReturn("existingUnit");
        BDDMockito.given(this.unitService.getAllUnits()).willReturn(Sets.newHashSet((Object[])new UnitModel[] {unit}));
        I18NService i18NService = (I18NService)Mockito.mock(I18NService.class);
        Mockito.when(i18NService.getCurrentTimeZone()).thenReturn(TimeZone.getDefault());
        this.excelDateUtils.setI18NService(i18NService);
    }


    @Test
    public void shouldHandleWhenDescriptorTypeIsPriceRow()
    {
        ImportParameters importParameters = new ImportParameters("Product", null, "not blank", null, new ArrayList());
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        CollectionTypeModel typeModel = (CollectionTypeModel)Mockito.mock(CollectionTypeModel.class);
        TypeModel elementTypeModel = (TypeModel)Mockito.mock(TypeModel.class);
        BDDMockito.given(attributeDescriptor.getAttributeType()).willReturn(typeModel);
        BDDMockito.given(typeModel.getElementType()).willReturn(elementTypeModel);
        BDDMockito.given(elementTypeModel.getCode()).willReturn("PriceRow");
        boolean canHandle = this.excelPriceValidator.canHandle(importParameters, attributeDescriptor);
        Assertions.assertThat(canHandle).isTrue();
    }


    @Test
    public void shouldNotHandleWhenCellIsEmpty()
    {
        ImportParameters importParameters = new ImportParameters("Product", null, "", null, new ArrayList());
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        CollectionTypeModel typeModel = (CollectionTypeModel)Mockito.mock(CollectionTypeModel.class);
        TypeModel elementTypeModel = (TypeModel)Mockito.mock(TypeModel.class);
        Mockito.lenient().when(attributeDescriptor.getAttributeType()).thenReturn(typeModel);
        Mockito.lenient().when(typeModel.getElementType()).thenReturn(elementTypeModel);
        Mockito.lenient().when(elementTypeModel.getCode()).thenReturn("PriceRow");
        boolean canHandle = this.excelPriceValidator.canHandle(importParameters, attributeDescriptor);
        Assertions.assertThat(canHandle).isFalse();
    }


    @Test
    public void shouldNotHandleWhenDescriptorTypeIsNotPriceRow()
    {
        ImportParameters importParameters = new ImportParameters("Product", null, "not blank", null, new ArrayList());
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        CollectionTypeModel typeModel = (CollectionTypeModel)Mockito.mock(CollectionTypeModel.class);
        TypeModel elementTypeModel = (TypeModel)Mockito.mock(TypeModel.class);
        BDDMockito.given(attributeDescriptor.getAttributeType()).willReturn(typeModel);
        BDDMockito.given(typeModel.getElementType()).willReturn(elementTypeModel);
        BDDMockito.given(elementTypeModel.getCode()).willReturn("Product2OwnEurope1Prices");
        boolean canHandle = this.excelPriceValidator.canHandle(importParameters, attributeDescriptor);
        Assertions.assertThat(canHandle).isFalse();
    }


    @Test
    public void shouldReturnPriceFormatError()
    {
        testPricesIncorrectFormat("10");
        testPricesIncorrectFormat("usd");
        testPricesIncorrectFormat("usd 10");
        testPricesIncorrectFormat("10.usd");
        testPricesIncorrectFormat("-10 usd");
    }


    public void testPricesIncorrectFormat(String priceCurrencyValue)
    {
        Map<String, String> params = new HashMap<>();
        params.put("price currency", priceCurrencyValue);
        ExcelValidationResult validationCellResult = validateWithParams(params);
        Assertions.assertThat(validationCellResult.hasErrors()).isTrue();
        Assertions.assertThat(validationCellResult.getValidationErrors()).hasSize(1);
        Assertions.assertThat(((ValidationMessage)validationCellResult.getValidationErrors().get(0)).getMessageKey())
                        .isEqualTo("excel.import.validation.price.currency.incorrect");
    }


    @Test
    public void shouldNotReturnPriceFormatError()
    {
        testPricesHasCorrectFormat("10 usd");
        testPricesHasCorrectFormat("4.3 usd");
        testPricesHasCorrectFormat("4.3   usd");
        testPricesHasCorrectFormat("333.3usd");
    }


    public void testPricesHasCorrectFormat(String priceCurrencyValue)
    {
        Map<String, String> params = new HashMap<>();
        params.put("price currency", priceCurrencyValue);
        ExcelValidationResult validationCellResult = validateWithParams(params);
        Assertions.assertThat(validationCellResult.hasErrors()).isFalse();
    }


    @Test
    public void shouldReturnNonExistingCurrencyError()
    {
        Map<String, String> params = new HashMap<>();
        params.put("price currency", "10 pln");
        ExcelValidationResult validationCellResult = validateWithParams(params);
        Assertions.assertThat(validationCellResult.hasErrors()).isTrue();
        Assertions.assertThat(validationCellResult.getValidationErrors()).hasSize(1);
        Assertions.assertThat(((ValidationMessage)validationCellResult.getValidationErrors().get(0)).getMessageKey())
                        .isEqualTo("excel.import.validation.price.currency.doesnt.exist");
    }


    @Test
    public void shouldReturnErrorWhenNetValueIsInCorrect()
    {
        testNetGrossIncorrect("B");
        testNetGrossIncorrect("Gross");
        testNetGrossIncorrect(" N");
        testNetGrossIncorrect("N ");
    }


    public void testNetGrossIncorrect(String netGross)
    {
        ExcelValidationResult validationCellResult = validateWithCorrectPriceAnd("N|G", netGross);
        Assertions.assertThat(validationCellResult.hasErrors()).isTrue();
        Assertions.assertThat(validationCellResult.getValidationErrors()).hasSize(1);
        Assertions.assertThat(((ValidationMessage)validationCellResult.getValidationErrors().get(0)).getMessageKey())
                        .isEqualTo("excel.import.validation.price.netgross.incorrect");
    }


    @Test
    public void shouldNotReturnErrorWhenNetValueIsCorrect()
    {
        testNetGrossCorrect("N");
        testNetGrossCorrect("G");
    }


    public void testNetGrossCorrect(String netGross)
    {
        ExcelValidationResult validationCellResult = validateWithCorrectPriceAnd("N|G", netGross);
        Assertions.assertThat(validationCellResult.hasErrors()).isFalse();
    }


    @Test
    public void shouldNotReturnErrorWhenQuantityUnitIsCorrect()
    {
        testQuantityUnitIsCorrect("10 existingUnit");
        testQuantityUnitIsCorrect("10existingUnit");
        testQuantityUnitIsCorrect("2existingUnit");
        testQuantityUnitIsCorrect("2 existingUnit");
    }


    public void testQuantityUnitIsCorrect(String quantityUnit)
    {
        ExcelValidationResult validationCellResult = validateWithCorrectPriceAnd("minQuantity unit", quantityUnit);
        Assertions.assertThat(validationCellResult.hasErrors()).isFalse();
    }


    @Test
    public void shouldReturnErrorWhenQuantityUnitIsIncorrect()
    {
        testQuantityUnitIsIncorrect("10 cows", "excel.import.validation.price.unit.incorrect");
        testQuantityUnitIsIncorrect("10_ cows", "excel.import.validation.price.quantityunit.incorrect");
        testQuantityUnitIsIncorrect("-10 existingUnit", "excel.import.validation.price.quantityunit.incorrect");
        testQuantityUnitIsIncorrect("1.1 existingUnit", "excel.import.validation.price.quantityunit.incorrect");
        testQuantityUnitIsIncorrect("0 existingUnit", "excel.import.validation.price.quantity.lowerthanone");
    }


    public void testQuantityUnitIsIncorrect(String quantityUnit, String validationMsg)
    {
        ExcelValidationResult validationCellResult = validateWithCorrectPriceAnd("minQuantity unit", quantityUnit);
        Assertions.assertThat(validationCellResult.hasErrors()).isTrue();
        Assertions.assertThat(validationCellResult.getValidationErrors()).hasSize(1);
        Assertions.assertThat(((ValidationMessage)validationCellResult.getValidationErrors().get(0)).getMessageKey()).isEqualTo(validationMsg);
    }


    @Test
    public void shouldNotReturnErrorWhenGivenUserPriceGroupExistsOrUser()
    {
        testGivenUserOrUserGroupExists("existingUser");
        testGivenUserOrUserGroupExists("existingPriceGroup");
    }


    public void testGivenUserOrUserGroupExists(String userPriceGroupOrUser)
    {
        Map<String, String> params = new HashMap<>();
        params.put("minQuantity unit", "10 existingUnit");
        params.put("user|userPriceGroup", userPriceGroupOrUser);
        ExcelValidationResult validationCellResult = validateWithCorrectPriceAnd(params);
        Assertions.assertThat(validationCellResult.hasErrors()).isFalse();
    }


    @Test
    public void shouldReturnErrorWhenGivenUserPriceGroupDoesNotExist()
    {
        Map<String, String> params = new HashMap<>();
        params.put("minQuantity unit", "10 existingUnit");
        params.put("user|userPriceGroup", "abc");
        ExcelValidationResult validationCellResult = validateWithCorrectPriceAnd(params);
        Assertions.assertThat(validationCellResult.hasErrors()).isTrue();
        Assertions.assertThat(validationCellResult.getValidationErrors()).hasSize(1);
        Assertions.assertThat(((ValidationMessage)validationCellResult.getValidationErrors().get(0)).getMessageKey())
                        .isEqualTo("excel.import.validation.price.user.incorrect");
    }


    @Test
    public void shouldReturnErrorWhenUserGroupDefinedButNoPriceQuantity()
    {
        Map<String, String> params = new HashMap<>();
        params.put("user|userPriceGroup", "existingPriceGroup");
        ExcelValidationResult validationCellResult = validateWithCorrectPriceAnd(params);
        Assertions.assertThat(validationCellResult.hasErrors()).isTrue();
        Assertions.assertThat(validationCellResult.getValidationErrors()).hasSize(1);
        Assertions.assertThat(((ValidationMessage)validationCellResult.getValidationErrors().get(0)).getMessageKey())
                        .isEqualTo("excel.import.validation.price.quantityunit.missing.user.defined");
    }


    @Test
    public void shouldReturnErrorChannelDoesNotExist()
    {
        ExcelValidationResult validationCellResult = validateWithCorrectPriceAnd("channel", "anyChannel");
        Assertions.assertThat(validationCellResult.hasErrors()).isTrue();
        Assertions.assertThat(validationCellResult.getValidationErrors()).hasSize(1);
        Assertions.assertThat(((ValidationMessage)validationCellResult.getValidationErrors().get(0)).getMessageKey())
                        .isEqualTo("excel.import.validation.price.channel.does.not.exist");
    }


    @Test
    public void shouldNotReturnErrorChannelExists()
    {
        ExcelValidationResult validationCellResult = validateWithCorrectPriceAnd("channel", "existingChannel");
        Assertions.assertThat(validationCellResult.hasErrors()).isFalse();
    }


    @Test
    public void shouldReturnErrorWhenDateHasWrongFormat()
    {
        testIncorrectDateRangeFormat("" + this.dateFrom + " to " + this.dateFrom);
        String start = this.excelDateUtils.exportDate(this.dateFrom);
        String end = this.excelDateUtils.exportDate(this.dateTo);
        testIncorrectDateRangeFormat(start + start);
    }


    protected void testIncorrectDateRangeFormat(String dateRange)
    {
        ExcelValidationResult validationCellResult = validateWithCorrectPriceAnd(this.excelDateUtils.getDateRangeParamKey(), dateRange);
        Assertions.assertThat(validationCellResult.hasErrors()).isTrue();
        Assertions.assertThat(validationCellResult.getValidationErrors()).hasSize(1);
        Assertions.assertThat(((ValidationMessage)validationCellResult.getValidationErrors().get(0)).getMessageKey())
                        .isEqualTo("excel.import.validation.price.date.incorrect.format");
    }


    @Test
    public void shouldNotReturnErrorWhenDateIsOk()
    {
        String start = this.excelDateUtils.exportDate(this.dateFrom);
        String end = this.excelDateUtils.exportDate(this.dateTo);
        testCorrectDateRangeFormat(start + " to " + start);
        testCorrectDateRangeFormat(start + "to " + start);
        testCorrectDateRangeFormat(start + " to" + start);
        testCorrectDateRangeFormat(start + "to" + start);
    }


    protected void testCorrectDateRangeFormat(String dateRange)
    {
        ExcelValidationResult validationCellResult = validateWithCorrectPriceAnd(this.excelDateUtils.getDateRangeParamKey(), dateRange);
        Assertions.assertThat(validationCellResult.hasErrors()).isFalse();
    }


    @Test
    public void shouldReturnErrorWhenStartDateIsAfterEndDate()
    {
        ExcelValidationResult validationCellResult = validateWithCorrectPriceAnd(this.excelDateUtils.getDateRangeParamKey(), this.excelDateUtils
                        .exportDate(this.dateTo) + " to " + this.excelDateUtils.exportDate(this.dateTo));
        Assertions.assertThat(validationCellResult.hasErrors()).isTrue();
        Assertions.assertThat(validationCellResult.getValidationErrors()).hasSize(1);
        Assertions.assertThat(((ValidationMessage)validationCellResult.getValidationErrors().get(0)).getMessageKey())
                        .isEqualTo("excel.import.validation.price.date.start.after.end");
    }


    protected ExcelValidationResult validateWithCorrectPriceAnd(String param, String value)
    {
        Map<String, String> params = new HashMap<>();
        params.put(param, value);
        return validateWithCorrectPriceAnd(params);
    }


    protected ExcelValidationResult validateWithCorrectPriceAnd(Map<String, String> params)
    {
        params.put("price currency", "10 usd");
        return validateWithParams(params);
    }


    protected ExcelValidationResult validateWithParams(Map<String, String> params)
    {
        List<Map<String, String>> parametersList = new ArrayList<>();
        parametersList.add(params);
        ImportParameters importParameters = new ImportParameters("Product", null, "not blank", null, parametersList);
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        return this.excelPriceValidator.validate(importParameters, attributeDescriptor, new HashMap<>());
    }
}
