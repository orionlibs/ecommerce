package de.hybris.platform.cockpit.components.navigationarea;

import org.zkoss.util.resource.Labels;

public class NotSharedQuerySectionModel implements QueryTypeSectionModel
{
    public String getQueryTypeId()
    {
        return "notshared";
    }


    public String getInactiveButtonUrl()
    {
        return "/cockpit/images/button_view_savedquery_default.png";
    }


    public String getButtonTooltip()
    {
        return Labels.getLabel("navigationarea.queries.nonshared");
    }


    public String getActiveButtonUrl()
    {
        return "/cockpit/images/button_view_savedquery_active.png";
    }


    public String getListItemSclass()
    {
        return "saved-query-list-item-img-nonshared";
    }
}
