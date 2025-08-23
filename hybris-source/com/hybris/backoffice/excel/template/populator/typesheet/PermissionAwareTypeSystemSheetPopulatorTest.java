package com.hybris.backoffice.excel.template.populator.typesheet;

import com.hybris.backoffice.excel.data.ExcelExportResult;
import com.hybris.backoffice.excel.exporting.ExcelExportDivider;
import com.hybris.backoffice.excel.template.populator.ExcelSheetPopulator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;
import java.util.Arrays;
import java.util.Collection;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PermissionAwareTypeSystemSheetPopulatorTest
{
    @Mock
    ExcelExportDivider mockedExcelExportDivider;
    @Mock
    PermissionCRUDService mockedPermissionCRUDService;
    @Mock
    ExcelSheetPopulator mockedPopulator;
    @InjectMocks
    PermissionAwareTypeSystemSheetPopulator permissionAwareTypeSystemSheetPopulator;


    @Test
    public void shouldFilterOutItemsThatUserHasNoAccessTo()
    {
        ArgumentCaptor<ExcelExportResult> resultCaptor = ArgumentCaptor.forClass(ExcelExportResult.class);
        ItemModel allowedItem = (ItemModel)Mockito.mock(ItemModel.class);
        ItemModel forbiddenItem = (ItemModel)Mockito.mock(ItemModel.class);
        Collection<ItemModel> items = Arrays.asList(new ItemModel[] {allowedItem, forbiddenItem});
        ExcelExportResult excelExportResult = (ExcelExportResult)Mockito.mock(ExcelExportResult.class);
        BDDMockito.given(excelExportResult.getSelectedItems()).willReturn(items);
        BDDMockito.given(this.mockedExcelExportDivider.groupItemsByType(items)).willReturn(new Object(this, allowedItem, forbiddenItem));
        BDDMockito.given(Boolean.valueOf(this.mockedPermissionCRUDService.canReadType("AllowedItemType"))).willReturn(Boolean.valueOf(true));
        BDDMockito.given(Boolean.valueOf(this.mockedPermissionCRUDService.canReadType("ForbiddenItemType"))).willReturn(Boolean.valueOf(false));
        this.permissionAwareTypeSystemSheetPopulator.populate(excelExportResult);
        ((ExcelSheetPopulator)Mockito.verify(this.mockedPopulator)).populate((ExcelExportResult)resultCaptor.capture());
        ExcelExportResult result = (ExcelExportResult)resultCaptor.getValue();
        Assertions.assertThat(result.getSelectedItems()).containsOnly(new Object[] {allowedItem});
    }
}
