package com.hybris.backoffice.excel.template.cell;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.OrderComparator;

public class DefaultExcelCellService implements ExcelCellService
{
    private static final List<Character> formulaChars = Arrays.asList(new Character[] {Character.valueOf('+'), Character.valueOf('-'), Character.valueOf('='), Character.valueOf('@'), Character.valueOf('|'), Character.valueOf('%')});
    private static final char ESCAPE_CHAR = '\'';
    private List<CellValue> cellValues;


    @Nonnull
    public String getCellValue(@Nullable Cell cell)
    {
        CellValue emptyCell = anyCell -> Optional.empty();
        return (cell == null) ?
                        "" : (
                        (CellValue)getCellValues().stream().filter(cellValue -> cellValue.canHandle(cell.getCellType())).findFirst().orElse(emptyCell)).getValue(cell).map(this::escapeImportFormula).orElse("");
    }


    public void insertAttributeValue(@Nonnull Cell cell, @Nullable Object value)
    {
        String valueToInsert = (value != null) ? escapeExportFormula(value.toString()) : null;
        cell.setCellValue(valueToInsert);
    }


    protected String escapeExportFormula(String value)
    {
        Predicate<String> isNotEmpty = StringUtils::isNotEmpty;
        Predicate<String> startsWithFormulaChar = val -> formulaChars.contains(Character.valueOf(val.charAt(0)));
        return isNotEmpty.and(startsWithFormulaChar).test(value) ? ("'" + value) : value;
    }


    protected String escapeImportFormula(String value)
    {
        Predicate<String> hasAtLeastTwoChars = val -> (val.length() > 1);
        Predicate<String> beginsWithEscapeChar = val -> (val.charAt(0) == '\'');
        Predicate<String> secondCharIsFormulaChar = val -> formulaChars.contains(Character.valueOf(val.charAt(1)));
        return hasAtLeastTwoChars.and(beginsWithEscapeChar).and(secondCharIsFormulaChar).test(value) ? value.substring(1) : value;
    }


    public Collection<CellValue> getCellValues()
    {
        return this.cellValues;
    }


    @Required
    public void setCellValues(List<CellValue> cellValues)
    {
        if(cellValues != null)
        {
            OrderComparator.sort(cellValues);
        }
        this.cellValues = cellValues;
    }
}
