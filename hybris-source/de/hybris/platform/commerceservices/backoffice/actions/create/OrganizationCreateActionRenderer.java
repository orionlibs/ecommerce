package de.hybris.platform.commerceservices.backoffice.actions.create;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.impl.DefaultActionRenderer;

public class OrganizationCreateActionRenderer extends DefaultActionRenderer<String, Object>
{
    public static final String LBL_SALES_UNIT = "organization.action.create.sales";


    protected String getLocalizedName(ActionContext<?> context)
    {
        return context.getLabel("organization.action.create.sales");
    }
}
