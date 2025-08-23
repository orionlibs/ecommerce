package com.hybris.backoffice.excel.data;

import de.hybris.platform.core.model.ItemModel;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class ExcelExportParams
{
    private final List<ItemModel> itemsToExport;
    private final List<SelectedAttribute> selectedAttributes;
    private final Collection<ExcelAttribute> additionalAttributes;


    public ExcelExportParams(List<ItemModel> itemsToExport, List<SelectedAttribute> selectedAttributes, Collection<ExcelAttribute> additionalAttributes)
    {
        this.itemsToExport = Objects.<List<ItemModel>>requireNonNull(itemsToExport, "ItemsToExport collection cannot be null");
        this.selectedAttributes = Objects.<List<SelectedAttribute>>requireNonNull(selectedAttributes, "SelectedAttributes collection cannot be null");
        this.additionalAttributes = Objects.<Collection<ExcelAttribute>>requireNonNull(additionalAttributes, "AdditionalAttributes collection cannot be null");
    }


    public List<ItemModel> getItemsToExport()
    {
        return this.itemsToExport;
    }


    public List<SelectedAttribute> getSelectedAttributes()
    {
        return this.selectedAttributes;
    }


    public Collection<ExcelAttribute> getAdditionalAttributes()
    {
        return this.additionalAttributes;
    }
}
