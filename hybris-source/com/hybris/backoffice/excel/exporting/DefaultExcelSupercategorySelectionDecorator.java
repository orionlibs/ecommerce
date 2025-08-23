package com.hybris.backoffice.excel.exporting;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ExcelExportParams;
import com.hybris.backoffice.excel.data.SelectedAttribute;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.DescriptorModel;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Required;

public class DefaultExcelSupercategorySelectionDecorator implements ExcelExportParamsDecorator
{
    private TypeService typeService;
    private PermissionCRUDService permissionCRUDService;
    private int order = 0;


    @Nonnull
    public ExcelExportParams decorate(@Nonnull ExcelExportParams excelExportParams)
    {
        if(hasNotSelectedSupercategoriesAttribute(excelExportParams) &&
                        hasAtLeastOneClassificationAttributeSelected(excelExportParams) && canReadSupercategoriesAttribute())
        {
            excelExportParams.getSelectedAttributes().add(new SelectedAttribute(getProductSupercategoriesAttribute()));
        }
        return excelExportParams;
    }


    protected boolean hasNotSelectedSupercategoriesAttribute(ExcelExportParams excelExportParams)
    {
        return excelExportParams.getSelectedAttributes()
                        .stream()
                        .map(SelectedAttribute::getAttributeDescriptor)
                        .map(DescriptorModel::getQualifier)
                        .noneMatch("supercategories"::equals);
    }


    protected boolean hasAtLeastOneClassificationAttributeSelected(ExcelExportParams excelExportParams)
    {
        Objects.requireNonNull(ExcelClassificationAttribute.class);
        return excelExportParams.getAdditionalAttributes().stream().anyMatch(ExcelClassificationAttribute.class::isInstance);
    }


    protected boolean canReadSupercategoriesAttribute()
    {
        return this.permissionCRUDService.canReadAttribute(getProductSupercategoriesAttribute());
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    @Required
    public void setPermissionCRUDService(PermissionCRUDService permissionCRUDService)
    {
        this.permissionCRUDService = permissionCRUDService;
    }


    public int getOrder()
    {
        return this.order;
    }


    public void setOrder(int order)
    {
        this.order = order;
    }


    public ComposedTypeModel getProductType()
    {
        return this.typeService.getComposedTypeForCode("Product");
    }


    public AttributeDescriptorModel getProductSupercategoriesAttribute()
    {
        return this.typeService.getAttributeDescriptor(getProductType(), "supercategories");
    }
}
