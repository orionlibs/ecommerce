package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.FocusEvent;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.dragdrop.DragAndDropWrapperService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.FocusablePerspectiveArea;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zkplus.spring.SpringUtil;

public abstract class AbstractUIPerspective implements UICockpitPerspective
{
    private String label;
    private String viewURI;
    private FocusablePerspectiveArea focusedArea;
    private TypeService typeService = null;
    private boolean selectable = true;
    private DragAndDropWrapperService dragAndDropWrapperService;
    protected TypedObject activeItem = null;
    private List<TemplateListEntry> templateList = new ArrayList<>();
    private boolean activationEffectEnabled = true;
    private double effectDuration = 1.0D;
    private int moveTargetX = 150;
    private int moveTargetY = 50;
    private String effectBorderColor = "blue";
    private String uid;
    private int infoBoxTimeout = 5000;


    @Required
    public void setUid(String uid)
    {
        this.uid = uid;
    }


    public String getUid()
    {
        return this.uid;
    }


    public void setActivationEffectEnabled(boolean enabled)
    {
        this.activationEffectEnabled = enabled;
    }


    public boolean isActivationEffectEnabled()
    {
        return this.activationEffectEnabled;
    }


    public void setMoveTargetX(int moveTargetX)
    {
        this.moveTargetX = moveTargetX;
    }


    public int getMoveTargetX()
    {
        return this.moveTargetX;
    }


    public void setMoveTargetY(int moveTargetY)
    {
        this.moveTargetY = moveTargetY;
    }


    public int getMoveTargetY()
    {
        return this.moveTargetY;
    }


    public void setEffectDuration(double effectDuration)
    {
        this.effectDuration = effectDuration;
    }


    public double getEffectDuration()
    {
        return this.effectDuration;
    }


    public void setEffectBorderColor(String borderColor)
    {
        this.effectBorderColor = borderColor;
    }


    public String getEffectBorderColor()
    {
        return this.effectBorderColor;
    }


    public String getLabel()
    {
        return this.label;
    }


    @Required
    public void setLabel(String label)
    {
        this.label = label;
    }


    @Required
    public void setViewURI(String uri)
    {
        this.viewURI = uri;
    }


    public String getViewURI()
    {
        return this.viewURI;
    }


    public abstract void onShow();


    public abstract void onHide();


    public FocusablePerspectiveArea getFocusedArea()
    {
        return this.focusedArea;
    }


    public void setFocusedArea(FocusablePerspectiveArea focusedArea)
    {
        if(this.focusedArea == null || !this.focusedArea.equals(focusedArea))
        {
            this.focusedArea = focusedArea;
            if(this.focusedArea != null)
            {
                onCockpitEvent((CockpitEvent)new FocusEvent(this, focusedArea));
            }
        }
    }


    public void setActiveItem(TypedObject activeItem)
    {
        this.activeItem = activeItem;
    }


    public TypedObject getActiveItem()
    {
        return this.activeItem;
    }


    @Required
    public void setCockpitTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = (TypeService)SpringUtil.getBean("cockpitTypeService");
        }
        return this.typeService;
    }


    public List<TemplateListEntry> getTemplateList()
    {
        return this.templateList;
    }


    public void setTemplateList(List<TemplateListEntry> templateList)
    {
        this.templateList = templateList;
    }


    @Required
    public void setDragAndDropWrapperService(DragAndDropWrapperService dragAndDropWrapperService)
    {
        this.dragAndDropWrapperService = dragAndDropWrapperService;
    }


    public DragAndDropWrapperService getDragAndDropWrapperService()
    {
        return this.dragAndDropWrapperService;
    }


    public void setSelectable(boolean selectable)
    {
        this.selectable = selectable;
    }


    public boolean isSelectable()
    {
        return this.selectable;
    }


    public void setInfoBoxTimeout(int millis)
    {
        this.infoBoxTimeout = millis;
    }


    public int getInfoBoxTimeout()
    {
        return this.infoBoxTimeout;
    }
}
