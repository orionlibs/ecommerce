package com.hybris.backoffice.excel.template.populator.typesheet;

import com.hybris.backoffice.excel.data.ExcelExportResult;
import com.hybris.backoffice.excel.exporting.ExcelExportDivider;
import com.hybris.backoffice.excel.template.populator.ExcelSheetPopulator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Required;

public class PermissionAwareTypeSystemSheetPopulator implements ExcelSheetPopulator
{
    private ExcelExportDivider excelExportDivider;
    private PermissionCRUDService permissionCRUDService;
    private ExcelSheetPopulator populator;


    public void populate(@Nonnull ExcelExportResult excelExportResult)
    {
        Collection<ItemModel> filteredSelectedItems = filterOutInaccessibleItems(excelExportResult.getSelectedItems());
        this.populator.populate(copyExcelExportResultWithNewSelectedItems(excelExportResult, filteredSelectedItems));
    }


    protected Collection<ItemModel> filterOutInaccessibleItems(Collection<ItemModel> selectedItems)
    {
        return (Collection<ItemModel>)this.excelExportDivider.groupItemsByType(selectedItems).entrySet().stream()
                        .filter(entry -> this.permissionCRUDService.canReadType((String)entry.getKey()))
                        .map(Map.Entry::getValue)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
    }


    protected ExcelExportResult copyExcelExportResultWithNewSelectedItems(ExcelExportResult excelExportResult, Collection<ItemModel> filteredSelectedItems)
    {
        return new ExcelExportResult(excelExportResult.getWorkbook(), filteredSelectedItems, excelExportResult
                        .getSelectedAttributes(), excelExportResult.getSelectedAdditionalAttributes(), excelExportResult
                        .getAvailableAdditionalAttributes());
    }


    @Required
    public void setExcelExportDivider(ExcelExportDivider excelExportDivider)
    {
        this.excelExportDivider = excelExportDivider;
    }


    @Required
    public void setPermissionCRUDService(PermissionCRUDService permissionCRUDService)
    {
        this.permissionCRUDService = permissionCRUDService;
    }


    @Required
    public void setPopulator(ExcelSheetPopulator populator)
    {
        this.populator = populator;
    }
}
