package com.hybris.backoffice.excel.translators;

import com.google.common.base.Joiner;
import com.hybris.backoffice.excel.data.ImpexHeaderValue;
import com.hybris.backoffice.excel.data.ImpexValue;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.util.ExcelDateUtils;
import com.hybris.backoffice.excel.validators.ExcelEurope1PricesValidator;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.europe1.enums.PriceRowChannel;
import de.hybris.platform.europe1.jalo.impex.Europe1PricesTranslator;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.util.StandardDateRange;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Required;

public class ExcelEurope1PricesTypeTranslator extends AbstractExcelValueTranslator<Collection<PriceRowModel>>
{
    @Deprecated(since = "1811", forRemoval = true)
    public static final String EUROPE1_PRICE_HEADER = "europe1prices";
    public static final String PRICE_CURRENCY = "price currency";
    public static final String NET_GROSS = "N|G";
    public static final String USER_OR_UPG = "user|userPriceGroup";
    public static final String QUANTITY_UNIT = "minQuantity unit";
    public static final String CHANNEL = "channel";
    private static final String PATTERN = "%s %s:%c:%s:%s %s:%s:%s";
    private ExcelDateUtils excelDateUtils;
    private CommonI18NService commonI18NService;


    public boolean canHandle(AttributeDescriptorModel attributeDescriptor)
    {
        return (attributeDescriptor.getAttributeType() instanceof CollectionTypeModel && "PriceRow"
                        .equals(((CollectionTypeModel)attributeDescriptor.getAttributeType()).getElementType().getCode()));
    }


    public Optional<Object> exportData(Collection<PriceRowModel> objectToExport)
    {
        Objects.requireNonNull(Joiner.on(','));
        Objects.requireNonNull(Object.class);
        return CollectionUtils.emptyIfNull(objectToExport).stream().map(this::exportPriceRow).reduce((x$0, x$1) -> rec$.join(x$0, x$1, new Object[0])).map(Object.class::cast);
    }


    protected String exportPriceRow(PriceRowModel priceRow)
    {
        char netGross = BooleanUtils.isTrue(priceRow.getNet()) ? 'N' : 'G';
        String userOrUserPriceGroup = (priceRow.getUg() != null) ? priceRow.getUg().getCode() : getValueOrEmpty(priceRow.getUser(), PrincipalModel::getUid);
        String channel = getValueOrEmpty(priceRow.getChannel(), PriceRowChannel::getCode);
        String unit = getValueOrEmpty(priceRow.getUnit(), UnitModel::getCode);
        return String.format("%s %s:%c:%s:%s %s:%s:%s", new Object[] {priceRow.getPrice(), priceRow.getCurrency().getIsocode(), Character.valueOf(netGross), userOrUserPriceGroup, priceRow
                        .getMinqtd(), unit, getDateRange(priceRow.getDateRange()), channel});
    }


    protected String getDateRange(StandardDateRange range)
    {
        if(range != null && range.getStart() != null && range.getEnd() != null)
        {
            return this.excelDateUtils.exportDateRange(range.getStart(), range.getEnd());
        }
        return "";
    }


    protected <T> String getValueOrEmpty(T reference, Function<T, String> valueSupplier)
    {
        return (reference != null) ? valueSupplier.apply(reference) : "";
    }


    public String referenceFormat(AttributeDescriptorModel attributeDescriptor)
    {
        return String.format("%s:%s:%s:%s:%s:%s", new Object[] {"price currency", "N|G", "user|userPriceGroup", "minQuantity unit", this.excelDateUtils
                        .getDateRangePattern(), "channel"});
    }


    public ImpexValue importValue(AttributeDescriptorModel attributeDescriptor, ImportParameters importParameters)
    {
        List<String> formattedPrices = new ArrayList<>();
        for(Map<String, String> params : (Iterable<Map<String, String>>)importParameters.getMultiValueParameters())
        {
            formattedPrices.add(buildSinglePriceImpexValue(params));
        }
        return new ImpexValue(String.join(", ", (Iterable)formattedPrices), (new ImpexHeaderValue.Builder(attributeDescriptor
                        .getQualifier())).withDateFormat(this.excelDateUtils.getDateTimeFormat())
                        .withTranslator(Europe1PricesTranslator.class.getName()).withQualifier(attributeDescriptor.getQualifier())
                        .build());
    }


    @Deprecated(since = "1905", forRemoval = true)
    protected void adjustPriceToCurrentLocale(ImportParameters importParameters)
    {
        importParameters.getMultiValueParameters().forEach(map -> {
            String priceCurrency = (String)map.get("price currency");
            if(priceCurrency != null)
            {
                Matcher matcher = ExcelEurope1PricesValidator.PATTERN_PRICE_CURRENCY.matcher(priceCurrency);
                if(matcher.matches())
                {
                    String price = matcher.group(1);
                    Double doublePrice = Double.valueOf(price);
                    NumberFormat formatter = NumberFormat.getInstance(getCurrentLanguageLocale());
                    String formattedPrice = formatter.format(doublePrice);
                    String newPriceCurrency = priceCurrency.replace(price, formattedPrice);
                    map.put("price currency", newPriceCurrency);
                }
            }
        });
    }


    private Locale getCurrentLanguageLocale()
    {
        return getCommonI18NService().getLocaleForLanguage(getCommonI18NService().getCurrentLanguage());
    }


    protected String buildSinglePriceImpexValue(Map<String, String> params)
    {
        StringBuilder sb = new StringBuilder();
        appendIfPresent(sb, params.get("user|userPriceGroup"));
        appendIfPresent(sb, params.get("minQuantity unit"));
        if(sb.length() > 0)
        {
            sb.append(" =");
        }
        appendIfPresent(sb, params.get("price currency"));
        appendIfPresent(sb, params.get("N|G"));
        appendIfPresent(sb, getImpexDateRange(params.get(this.excelDateUtils.getDateRangeParamKey())));
        appendIfPresent(sb, params.get("channel"));
        return sb.toString();
    }


    protected void appendIfPresent(StringBuilder sb, String value)
    {
        if(StringUtils.isNotEmpty(value))
        {
            if(sb.length() > 0)
            {
                sb.append(" ");
            }
            sb.append(value);
        }
    }


    protected String getImpexDateRange(String dateRange)
    {
        if(StringUtils.isNotEmpty(dateRange))
        {
            Pair<String, String> range = this.excelDateUtils.extractDateRange(dateRange);
            if(range != null)
            {
                return String.format("[%s%s%s]", new Object[] {this.excelDateUtils.importDate((String)range.getLeft()),
                                Character.valueOf(','), this.excelDateUtils.importDate((String)range.getRight())});
            }
        }
        return null;
    }


    public CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
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
