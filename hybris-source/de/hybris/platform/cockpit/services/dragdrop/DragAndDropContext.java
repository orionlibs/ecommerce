package de.hybris.platform.cockpit.services.dragdrop;

import de.hybris.platform.cockpit.session.BrowserModel;

public interface DragAndDropContext
{
    BrowserModel getBrowser();


    DragAndDropWrapper getWrapper();
}
