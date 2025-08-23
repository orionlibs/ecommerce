package com.hybris.backoffice.excel.exporting;

import com.hybris.backoffice.excel.data.SelectedAttribute;
import de.hybris.platform.core.model.ItemModel;
import java.util.List;
import org.apache.poi.ss.usermodel.Workbook;

public interface ExcelExportService
{
    Workbook exportTemplate(String paramString);


    Workbook exportData(List<ItemModel> paramList, List<SelectedAttribute> paramList1);


    Workbook exportData(String paramString, List<SelectedAttribute> paramList);
}
