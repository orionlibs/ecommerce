package de.hybris.platform.b2bcommercefacades.company.data;

import de.hybris.platform.b2bapprovalprocessfacades.company.data.B2BPermissionData;
import de.hybris.platform.commercefacades.user.data.UserGroupData;
import java.util.List;

public class B2BUserGroupData extends UserGroupData
{
    private List<String> roles;
    private B2BUnitData unit;
    private boolean selected;
    private String normalizedId;
    private boolean editable;
    private List<B2BPermissionData> permissions;


    public void setRoles(List<String> roles)
    {
        this.roles = roles;
    }


    public List<String> getRoles()
    {
        return this.roles;
    }


    public void setUnit(B2BUnitData unit)
    {
        this.unit = unit;
    }


    public B2BUnitData getUnit()
    {
        return this.unit;
    }


    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }


    public boolean isSelected()
    {
        return this.selected;
    }


    public void setNormalizedId(String normalizedId)
    {
        this.normalizedId = normalizedId;
    }


    public String getNormalizedId()
    {
        return this.normalizedId;
    }


    public void setEditable(boolean editable)
    {
        this.editable = editable;
    }


    public boolean isEditable()
    {
        return this.editable;
    }


    public void setPermissions(List<B2BPermissionData> permissions)
    {
        this.permissions = permissions;
    }


    public List<B2BPermissionData> getPermissions()
    {
        return this.permissions;
    }
}
