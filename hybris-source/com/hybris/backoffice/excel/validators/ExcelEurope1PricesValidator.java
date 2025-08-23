package com.hybris.backoffice.excel.validators;

import com.google.common.collect.Lists;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.util.ExcelDateUtils;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.core.model.c2l.C2LItemModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.europe1.enums.PriceRowChannel;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.product.UnitService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.daos.CurrencyDao;
import de.hybris.platform.servicelayer.user.UserService;
import java.io.Serializable;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class ExcelEurope1PricesValidator implements ExcelValidator
{
    private static final Logger LOG = LoggerFactory.getLogger(ExcelEurope1PricesValidator.class);
    protected static final String UNIT_KEY = "Unit";
    protected static final String CURRENCY_KEY = "Currency";
    public static final Pattern PATTERN_PRICE_CURRENCY = Pattern.compile("(\\d+(\\.?\\d+)?)\\s*([a-zA-z]+)");
    public static final Pattern PATTERN_QUANTITY_UNIT = Pattern.compile("(\\d+)\\s*(\\w+)");
    public static final Pattern PATTERN_DATE_RANGE = Pattern.compile("(.+)\\s*to\\s*(.+)");
    public static final String VALIDATION_CURRENCY_DOESNT_EXIST = "excel.import.validation.price.currency.doesnt.exist";
    public static final String VALIDATION_INCORRECT_PRICE_CURRENCY = "excel.import.validation.price.currency.incorrect";
    public static final String VALIDATION_EMPTY_PRICE_CURRENCY = "excel.import.validation.priceandcurrency.empty";
    public static final String VALIDATION_INCORRECT_PRICE_VALUE = "excel.import.validation.price.value.incorrect";
    public static final String VALIDATION_INVALID_NET_GROSS = "excel.import.validation.price.netgross.incorrect";
    public static final String VALIDATION_INCORRECT_QUANTITY_UNIT = "excel.import.validation.price.quantityunit.incorrect";
    public static final String VALIDATION_INCORRECT_QUANTITY = "excel.import.validation.price.quantity.incorrect";
    public static final String VALIDATION_INCORRECT_UNIT = "excel.import.validation.price.unit.incorrect";
    public static final String VALIDATION_INCORRECT_QUANTITY_LOWE_THAN_ONE = "excel.import.validation.price.quantity.lowerthanone";
    public static final String VALIDATION_INCORRECT_USER_OR_USER_PRICE_GROUP = "excel.import.validation.price.user.incorrect";
    public static final String VALIDATION_QUANTITY_UNIT_CANNOT_BE_EMPTY_WHEN_USER_DEFINED = "excel.import.validation.price.quantityunit.missing.user.defined";
    public static final String VALIDATION_NO_SUCH_CHANNEL = "excel.import.validation.price.channel.does.not.exist";
    public static final String VALIDATION_INCORRECT_DATE_RANGE = "excel.import.validation.price.date.incorrect.format";
    public static final String VALIDATION_START_DATE_AFTER_END_DATE = "excel.import.validation.price.date.start.after.end";
    private CurrencyDao currencyDao;
    private UnitService unitService;
    private EnumerationService enumerationService;
    private UserService userService;
    private ExcelDateUtils excelDateUtils;


    public ExcelValidationResult validate(ImportParameters importParameters, AttributeDescriptorModel attributeDescriptor, Map<String, Object> ctx)
    {
        if(!ctx.containsKey("Currency"))
        {
            populateContext(ctx);
        }
        List<ValidationMessage> validationMessages = new ArrayList<>();
        for(Map<String, String> parameters : (Iterable<Map<String, String>>)importParameters.getMultiValueParameters())
        {
            validationMessages.addAll(validateSingleValue(ctx, parameters));
        }
        return validationMessages.isEmpty() ? ExcelValidationResult.SUCCESS : new ExcelValidationResult(validationMessages);
    }


    protected List<ValidationMessage> validateSingleValue(Map<String, Object> ctx, Map<String, String> parameters)
    {
        List<ValidationMessage> validations = new ArrayList<>();
        Objects.requireNonNull(validations);
        validatePrice(ctx, parameters.get("price currency")).ifPresent(validations::addAll);
        Objects.requireNonNull(validations);
        validateNetGross(parameters.get("N|G")).ifPresent(validations::add);
        Objects.requireNonNull(validations);
        validateQuantityUnit(ctx, parameters.get("minQuantity unit"), parameters.get("user|userPriceGroup")).ifPresent(validations::addAll);
        Objects.requireNonNull(validations);
        validateUserPriceGroup(parameters.get("user|userPriceGroup")).ifPresent(validations::add);
        Objects.requireNonNull(validations);
        validateDateRange(parameters.get(this.excelDateUtils.getDateRangeParamKey())).ifPresent(validations::add);
        Objects.requireNonNull(validations);
        validateChannel(parameters.get("channel")).ifPresent(validations::add);
        return validations;
    }


    protected Optional<List<ValidationMessage>> validatePrice(Map<String, Object> ctx, String value)
    {
        if(StringUtils.isEmpty(value))
        {
            return Optional.of(Lists.newArrayList((Object[])new ValidationMessage[] {new ValidationMessage("excel.import.validation.priceandcurrency.empty", new Serializable[] {value})}));
        }
        Matcher matcher = PATTERN_PRICE_CURRENCY.matcher(value);
        if(matcher.matches())
        {
            List<ValidationMessage> validation = new ArrayList<>();
            Objects.requireNonNull(validation);
            validatePriceValue(matcher.group(1)).ifPresent(validation::add);
            Objects.requireNonNull(validation);
            validateCurrency(ctx, matcher.group(3)).ifPresent(validation::add);
            return validation.isEmpty() ? Optional.<List<ValidationMessage>>empty() : Optional.<List<ValidationMessage>>of(validation);
        }
        return Optional.of(Lists.newArrayList((Object[])new ValidationMessage[] {new ValidationMessage("excel.import.validation.price.currency.incorrect", new Serializable[] {value})}));
    }


    protected Optional<ValidationMessage> validatePriceValue(String price)
    {
        if(StringUtils.isBlank(price) || !NumberUtils.isCreatable(price))
        {
            return Optional.of(new ValidationMessage("excel.import.validation.price.value.incorrect", new Serializable[] {price}));
        }
        return Optional.empty();
    }


    protected Optional<ValidationMessage> validateCurrency(Map<String, Object> ctx, String currency)
    {
        if(!containsCurrency(ctx, currency))
        {
            return Optional.of(new ValidationMessage("excel.import.validation.price.currency.doesnt.exist", new Serializable[] {currency}));
        }
        return Optional.empty();
    }


    protected Optional<ValidationMessage> validateNetGross(String netGross)
    {
        if(StringUtils.isNotEmpty(netGross) && (netGross.length() != 1 || (netGross
                        .charAt(0) != 'G' && netGross.charAt(0) != 'N')))
        {
            return Optional.of(new ValidationMessage("excel.import.validation.price.netgross.incorrect", new Serializable[] {netGross, Character.valueOf('N'),
                            Character.valueOf('G')}));
        }
        return Optional.empty();
    }


    protected Optional<ValidationMessage> validateUserPriceGroup(String groupOrUser)
    {
        if(StringUtils.isNotEmpty(groupOrUser))
        {
            try
            {
                this.enumerationService.getEnumerationValue(UserPriceGroup.class, groupOrUser);
            }
            catch(UnknownIdentifierException uie)
            {
                try
                {
                    this.userService.getUserForUID(groupOrUser);
                }
                catch(UnknownIdentifierException e)
                {
                    return Optional.of(new ValidationMessage("excel.import.validation.price.user.incorrect", new Serializable[] {groupOrUser}));
                }
            }
        }
        return Optional.empty();
    }


    protected Optional<ValidationMessage> validateDateRange(String dateRange)
    {
        if(StringUtils.isEmpty(dateRange))
        {
            return Optional.empty();
        }
        Matcher matcher = PATTERN_DATE_RANGE.matcher(dateRange);
        if(matcher.matches())
        {
            Date from = parseDate(matcher.group(1));
            Date to = parseDate(matcher.group(2));
            if(from != null && to != null)
            {
                if(from.after(to))
                {
                    return
                                    Optional.of(new ValidationMessage("excel.import.validation.price.date.start.after.end", new Serializable[] {matcher.group(1), matcher.group(2)}));
                }
                return Optional.empty();
            }
        }
        return Optional.of(new ValidationMessage("excel.import.validation.price.date.incorrect.format", new Serializable[] {dateRange}));
    }


    protected Date parseDate(String date)
    {
        try
        {
            return this.excelDateUtils.convertToImportedDate(date);
        }
        catch(DateTimeParseException e)
        {
            LOG.debug(String.format("Wrong date format %s", new Object[] {date}), e);
            return null;
        }
    }


    protected Optional<ValidationMessage> validateChannel(String channel)
    {
        if(StringUtils.isNotEmpty(channel))
        {
            try
            {
                this.enumerationService.getEnumerationValue(PriceRowChannel.class, channel);
            }
            catch(UnknownIdentifierException nie)
            {
                return Optional.of(new ValidationMessage("excel.import.validation.price.channel.does.not.exist", new Serializable[] {channel}));
            }
        }
        return Optional.empty();
    }


    protected Optional<List<ValidationMessage>> validateQuantityUnit(Map<String, Object> ctx, String quantityUnit, String groupOrUser)
    {
        if(StringUtils.isEmpty(quantityUnit))
        {
            if(StringUtils.isNotEmpty(groupOrUser))
            {
                return Optional.of(
                                Lists.newArrayList((Object[])new ValidationMessage[] {new ValidationMessage("excel.import.validation.price.quantityunit.missing.user.defined", new Serializable[] {groupOrUser})}));
            }
            return Optional.empty();
        }
        Matcher matcher = PATTERN_QUANTITY_UNIT.matcher(quantityUnit);
        if(matcher.matches())
        {
            List<ValidationMessage> validation = new ArrayList<>();
            Objects.requireNonNull(validation);
            validateQuantity(matcher.group(1)).ifPresent(validation::add);
            Objects.requireNonNull(validation);
            validateUnit(ctx, matcher.group(2)).ifPresent(validation::add);
            return validation.isEmpty() ? Optional.<List<ValidationMessage>>empty() : Optional.<List<ValidationMessage>>of(validation);
        }
        return Optional.of(Lists.newArrayList((Object[])new ValidationMessage[] {new ValidationMessage("excel.import.validation.price.quantityunit.incorrect", new Serializable[] {quantityUnit})}));
    }


    protected Optional<ValidationMessage> validateQuantity(String quantity)
    {
        try
        {
            if(Integer.parseInt(quantity) < 1)
            {
                return Optional.of(new ValidationMessage("excel.import.validation.price.quantity.lowerthanone", new Serializable[] {quantity}));
            }
        }
        catch(NumberFormatException nfe)
        {
            return Optional.of(new ValidationMessage("excel.import.validation.price.quantity.incorrect", new Serializable[] {quantity}));
        }
        return Optional.empty();
    }


    protected Optional<ValidationMessage> validateUnit(Map<String, Object> ctx, String unit)
    {
        if(!containsUnit(ctx, unit))
        {
            return Optional.of(new ValidationMessage("excel.import.validation.price.unit.incorrect", new Serializable[] {unit}));
        }
        return Optional.empty();
    }


    protected boolean containsCurrency(Map<String, Object> ctx, String currency)
    {
        return ((Set)ctx.get("Currency")).contains(currency);
    }


    protected boolean containsUnit(Map<String, Object> ctx, String unit)
    {
        return ((Set)ctx.get("Unit")).contains(unit);
    }


    protected void populateContext(Map<String, Object> ctx)
    {
        Set<String> units = (Set<String>)this.unitService.getAllUnits().stream().map(UnitModel::getCode).collect(Collectors.toSet());
        ctx.put("Unit", units);
        Set<String> currencies = (Set<String>)this.currencyDao.findCurrencies().stream().map(C2LItemModel::getIsocode).collect(Collectors.toSet());
        ctx.put("Currency", currencies);
    }


    protected boolean checkIfCurrencyExist(Map<String, Object> ctx, String currency)
    {
        Set<String> currencies = (ctx.get("Currency") instanceof Set) ? (Set<String>)ctx.get("Currency") : new HashSet<>();
        return currencies.contains(currency);
    }


    public boolean canHandle(ImportParameters importParameters, AttributeDescriptorModel attributeDescriptor)
    {
        return (importParameters.isCellValueNotBlank() && attributeDescriptor.getAttributeType() instanceof CollectionTypeModel && "PriceRow"
                        .equals(((CollectionTypeModel)attributeDescriptor.getAttributeType()).getElementType().getCode()));
    }


    public CurrencyDao getCurrencyDao()
    {
        return this.currencyDao;
    }


    @Required
    public void setCurrencyDao(CurrencyDao currencyDao)
    {
        this.currencyDao = currencyDao;
    }


    public UnitService getUnitService()
    {
        return this.unitService;
    }


    @Required
    public void setUnitService(UnitService unitService)
    {
        this.unitService = unitService;
    }


    public EnumerationService getEnumerationService()
    {
        return this.enumerationService;
    }


    @Required
    public void setEnumerationService(EnumerationService enumerationService)
    {
        this.enumerationService = enumerationService;
    }


    public UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    public ExcelDateUtils getExcelDateUtils()
    {
        return this.excelDateUtils;
    }


    @Required
    public void setExcelDateUtils(ExcelDateUtils excelDateUtils)
    {
        this.excelDateUtils = excelDateUtils;
    }
}
