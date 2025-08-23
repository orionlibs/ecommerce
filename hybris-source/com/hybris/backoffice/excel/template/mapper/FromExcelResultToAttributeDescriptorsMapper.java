package com.hybris.backoffice.excel.template.mapper;

import com.hybris.backoffice.excel.data.ExcelExportResult;
import com.hybris.backoffice.excel.template.ExcelTemplateConstants;
import com.hybris.backoffice.excel.template.filter.ExcelFilter;
import com.hybris.backoffice.excel.template.sheet.ExcelSheetService;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Required;

public class FromExcelResultToAttributeDescriptorsMapper implements ToAttributeDescriptorsMapper<ExcelExportResult>
{
    private ExcelMapper<String, AttributeDescriptorModel> mapper;
    private ExcelSheetService excelSheetService;
    private Collection<ExcelFilter<AttributeDescriptorModel>> filters;


    public Collection<AttributeDescriptorModel> apply(ExcelExportResult excelExportResult)
    {
        Predicate<String> isNotUtilitySheet = sheetName -> Stream.<ExcelTemplateConstants.UtilitySheet>of(ExcelTemplateConstants.UtilitySheet.values()).map(ExcelTemplateConstants.UtilitySheet::getSheetName).noneMatch(());
        Objects.requireNonNull(excelExportResult.getWorkbook());
        return (Collection<AttributeDescriptorModel>)IntStream.range(0, excelExportResult.getWorkbook().getNumberOfSheets()).<String>mapToObj(excelExportResult.getWorkbook()::getSheetName)
                        .filter(isNotUtilitySheet)
                        .map(sheetName -> this.excelSheetService.findTypeCodeForSheetName(excelExportResult.getWorkbook(), sheetName))
                        .<AttributeDescriptorModel>map((Function<? super String, ? extends AttributeDescriptorModel>)this.mapper)
                        .flatMap(Collection::stream)
                        .distinct()
                        .filter(attribute -> filter(attribute, this.filters))
                        .collect(Collectors.toList());
    }


    @Required
    public void setExcelSheetService(ExcelSheetService excelSheetService)
    {
        this.excelSheetService = excelSheetService;
    }


    @Required
    public void setMapper(ExcelMapper<String, AttributeDescriptorModel> mapper)
    {
        this.mapper = mapper;
    }


    public void setFilters(Collection<ExcelFilter<AttributeDescriptorModel>> filters)
    {
        this.filters = filters;
    }
}
