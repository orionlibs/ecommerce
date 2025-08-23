package com.hybris.backoffice.listeners;

import com.hybris.backoffice.ApplicationUtils;
import com.hybris.backoffice.catalogversioneventhandling.AvailableCatalogVersionsTag;
import de.hybris.platform.tx.AfterSaveEvent;
import de.hybris.platform.tx.AfterSaveListener;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Required;

public class CatalogVersionAfterSaveListener implements AfterSaveListener
{
    private static final int CATALOG_VERSION_DEPLOYMENT_CODE = 601;
    private AvailableCatalogVersionsTag availableCatalogVersionsTag;


    public void afterSave(Collection<AfterSaveEvent> collection)
    {
        if(shouldPerform())
        {
            collection.stream().filter(this::isCatalogVersionRelatedEvent).findAny().ifPresent(afterSaveEvent -> handleEvent());
        }
    }


    protected boolean shouldPerform()
    {
        return ApplicationUtils.isPlatformReady();
    }


    protected boolean isCatalogVersionRelatedEvent(AfterSaveEvent event)
    {
        return (601 == event.getPk().getTypeCode() && (4 == event
                        .getType() || 2 == event.getType()));
    }


    protected void handleEvent()
    {
        getAvailableCatalogVersionsTag().refresh();
    }


    protected AvailableCatalogVersionsTag getAvailableCatalogVersionsTag()
    {
        return this.availableCatalogVersionsTag;
    }


    @Required
    public void setAvailableCatalogVersionsTag(AvailableCatalogVersionsTag availableCatalogVersionsTag)
    {
        this.availableCatalogVersionsTag = availableCatalogVersionsTag;
    }
}
