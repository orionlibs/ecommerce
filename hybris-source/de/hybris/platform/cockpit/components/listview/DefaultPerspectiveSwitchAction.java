package de.hybris.platform.cockpit.components.listview;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Menupopup;

public class DefaultPerspectiveSwitchAction extends AbstractPerspectiveSwitchAction
{
    private static final String IMAGE_URI = "/images/close_btn.png";


    protected void doCreateContext(ListViewAction.Context context)
    {
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        return null;
    }


    public Menupopup getContextPopup(ListViewAction.Context context)
    {
        return null;
    }


    public String getImageURI(ListViewAction.Context context)
    {
        return "/images/close_btn.png";
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("general.edit");
    }
}
