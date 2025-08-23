package com.hybris.backoffice.excel.exporting;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ExcelExportResult;
import de.hybris.platform.core.model.ItemModel;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Workbook;

public class DefaultExcelExportClassificationWorkbookDecorator extends AbstractExcelExportWorkbookDecorator
{
    public void decorate(ExcelExportResult excelExportResult)
    {
        Workbook workbook = excelExportResult.getWorkbook();
        Collection<ExcelClassificationAttribute> selectedAdditionalAttributes = extractClassificationAttributes(excelExportResult
                        .getSelectedAdditionalAttributes());
        Collection<ItemModel> selectedItems = excelExportResult.getSelectedItems();
        decorate(workbook, selectedAdditionalAttributes, selectedItems);
    }


    private static List<ExcelClassificationAttribute> extractClassificationAttributes(Collection<ExcelAttribute> attributes)
    {
        Objects.requireNonNull(ExcelClassificationAttribute.class);
        Objects.requireNonNull(ExcelClassificationAttribute.class);
        return (List<ExcelClassificationAttribute>)attributes.stream().filter(ExcelClassificationAttribute.class::isInstance).map(ExcelClassificationAttribute.class::cast)
                        .collect(Collectors.toList());
    }


    public int getOrder()
    {
        return 0;
    }
}
