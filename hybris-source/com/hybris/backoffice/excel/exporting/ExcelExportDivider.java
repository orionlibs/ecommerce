package com.hybris.backoffice.excel.exporting;

import com.hybris.backoffice.excel.data.SelectedAttribute;
import de.hybris.platform.core.model.ItemModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ExcelExportDivider
{
    default Map<String, Set<ItemModel>> groupItemsByType(Collection<ItemModel> items)
    {
        return groupItemsByType(new ArrayList<>(items));
    }


    default Map<String, Set<SelectedAttribute>> groupAttributesByType(Collection<String> typeCodes, Collection<SelectedAttribute> selectedAttributes)
    {
        return groupAttributesByType(new HashSet<>(typeCodes), new ArrayList<>(selectedAttributes));
    }


    @Deprecated(since = "1808", forRemoval = true)
    Map<String, Set<ItemModel>> groupItemsByType(List<ItemModel> paramList);


    @Deprecated(since = "1808", forRemoval = true)
    Map<String, Set<SelectedAttribute>> groupAttributesByType(Set<String> paramSet, List<SelectedAttribute> paramList);
}
