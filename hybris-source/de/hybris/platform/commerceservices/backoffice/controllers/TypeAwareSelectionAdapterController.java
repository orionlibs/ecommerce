package de.hybris.platform.commerceservices.backoffice.controllers;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.data.TypeAwareSelectionContext;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.util.DefaultWidgetController;
import java.util.Collections;

public class TypeAwareSelectionAdapterController extends DefaultWidgetController
{
    protected transient TypeFacade typeFacade;


    @SocketEvent(socketId = "object")
    public void adjustForNavigationNode(Object object)
    {
        if(object != null)
        {
            String type = this.typeFacade.getType(object);
            TypeAwareSelectionContext typeAwareObject = new TypeAwareSelectionContext(type, object, Collections.singletonList(object));
            sendOutput("typeAwareObject", typeAwareObject);
        }
    }
}
