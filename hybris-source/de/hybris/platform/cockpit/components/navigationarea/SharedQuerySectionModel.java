package de.hybris.platform.cockpit.components.navigationarea;

import org.zkoss.util.resource.Labels;

public class SharedQuerySectionModel implements QueryTypeSectionModel
{
    public String getQueryTypeId()
    {
        return "shared";
    }


    public String getInactiveButtonUrl()
    {
        return "/cockpit/images/button_view_savedqueryshared_default.png";
    }


    public String getButtonTooltip()
    {
        return Labels.getLabel("navigationarea.queries.shared");
    }


    public String getActiveButtonUrl()
    {
        return "/cockpit/images/button_view_savedqueryshared_active.png";
    }


    public String getListItemSclass()
    {
        return "saved-query-list-item-img-shared";
    }
}
