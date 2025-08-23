package com.hybris.backoffice.excel.data;

import de.hybris.platform.core.model.ItemModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Nonnull;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelExportResult
{
    private final Workbook workbook;
    private final Collection<ItemModel> selectedItems;
    private final Collection<SelectedAttribute> selectedAttributes;
    private final Collection<ExcelAttribute> selectedAdditionalAttributes;
    private final Collection<ExcelAttribute> availableAdditionalAttributes;


    public ExcelExportResult(Workbook workbook)
    {
        this(workbook, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }


    public ExcelExportResult(Workbook workbook, Collection<ItemModel> selectedItems, Collection<SelectedAttribute> selectedAttributes, Collection<ExcelAttribute> selectedAdditionalAttributes, Collection<ExcelAttribute> availableAdditionalAttributes)
    {
        this.workbook = workbook;
        this.selectedItems = selectedItems;
        this.selectedAttributes = selectedAttributes;
        this.selectedAdditionalAttributes = selectedAdditionalAttributes;
        this.availableAdditionalAttributes = availableAdditionalAttributes;
    }


    public Workbook getWorkbook()
    {
        return this.workbook;
    }


    public Collection<ItemModel> getSelectedItems()
    {
        return this.selectedItems;
    }


    @Nonnull
    public Collection<SelectedAttribute> getSelectedAttributes()
    {
        return (this.selectedAttributes != null) ? this.selectedAttributes : new ArrayList<>();
    }


    @Nonnull
    public Collection<ExcelAttribute> getSelectedAdditionalAttributes()
    {
        return (this.selectedAdditionalAttributes != null) ? this.selectedAdditionalAttributes : new ArrayList<>();
    }


    @Nonnull
    public Collection<ExcelAttribute> getAvailableAdditionalAttributes()
    {
        return (this.availableAdditionalAttributes != null) ? this.availableAdditionalAttributes : new ArrayList<>();
    }
}
