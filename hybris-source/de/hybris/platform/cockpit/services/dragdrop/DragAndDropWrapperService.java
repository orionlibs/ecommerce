package de.hybris.platform.cockpit.services.dragdrop;

import org.springframework.beans.factory.annotation.Required;

public class DragAndDropWrapperService
{
    private DragAndDropWrapper wrapper;


    @Required
    public void setWrapper(DragAndDropWrapper wrapper)
    {
        this.wrapper = wrapper;
    }


    public DragAndDropWrapper getWrapper()
    {
        return this.wrapper;
    }
}
