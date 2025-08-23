package de.hybris.platform.basecommerce.backoffice.editor;

import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractSection;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Attribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.ProxyRenderer;
import com.hybris.cockpitng.widgets.editorarea.renderer.impl.DefaultEditorAreaSectionRenderer;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.GeoWebServiceWrapper;
import de.hybris.platform.storelocator.data.AddressData;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;

public class GeoCodePosSectionRenderer extends DefaultEditorAreaSectionRenderer
{
    private static final String GEOCODE_BUTTON_LABEL = "hmc.geocode";
    private GeoWebServiceWrapper geoServiceWrapper;
    private NotificationService notificationService;


    public void render(Component parent, AbstractSection abstractSectionConfiguration, Object object, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        Div cnt = new Div();
        Button button = new Button(Labels.getLabel("hmc.geocode"));
        PointOfServiceModel pos = (PointOfServiceModel)widgetInstanceManager.getModel().getValue("currentObject", PointOfServiceModel.class);
        if(hasUserPermissionToWrite(pos))
        {
            button.setDisabled(true);
        }
        AddressData address = new AddressData(pos.getAddress());
        button.addEventListener("onClick", (EventListener)new Object(this, address, pos, widgetInstanceManager));
        int columns = 2;
        String width = calculateWidthPercentage(2);
        List<Attribute> attributes = getAttributes();
        ProxyRenderer proxyRenderer = new ProxyRenderer((AbstractWidgetComponentRenderer)this, parent, abstractSectionConfiguration, object);
        renderAttributes(attributes, proxyRenderer, 2, width, dataType, widgetInstanceManager, object);
        parent.appendChild((Component)cnt);
        cnt.appendChild((Component)button);
    }


    protected boolean geoCodeAddress(AddressData address, PointOfServiceModel pos, WidgetInstanceManager widgetInstanceManager)
    {
        if(address == null || pos == null || widgetInstanceManager == null)
        {
            return false;
        }
        GPS gps = this.geoServiceWrapper.geocodeAddress(address);
        if(gps != null)
        {
            Date geocodeTimestamp = new Date();
            updatePointOfService(gps, geocodeTimestamp, pos);
            desynchronizeView(gps, geocodeTimestamp, widgetInstanceManager);
            return true;
        }
        return false;
    }


    protected void desynchronizeView(GPS gps, Date geocodeTimestamp, WidgetInstanceManager widgetInstanceManager)
    {
        if(widgetInstanceManager != null && widgetInstanceManager.getModel() != null && gps != null)
        {
            widgetInstanceManager.getModel().setValue("currentObject.latitude", Double.valueOf(gps.getDecimalLatitude()));
            widgetInstanceManager.getModel().setValue("currentObject.longitude", Double.valueOf(gps.getDecimalLongitude()));
            widgetInstanceManager.getModel().setValue("currentObject.geocodeTimestamp", geocodeTimestamp);
        }
    }


    protected void updatePointOfService(GPS gps, Date geocodeTimestamp, PointOfServiceModel pos)
    {
        if(gps != null && pos != null)
        {
            pos.setLatitude(Double.valueOf(gps.getDecimalLatitude()));
            pos.setLongitude(Double.valueOf(gps.getDecimalLongitude()));
            pos.setGeocodeTimestamp(geocodeTimestamp);
        }
    }


    protected boolean hasUserPermissionToWrite(PointOfServiceModel pos)
    {
        return (!getPermissionFacade().canChangeInstanceProperty(pos, "latitude") ||
                        !getPermissionFacade().canChangeInstanceProperty(pos, "longitude") ||
                        !getPermissionFacade().canChangeInstanceProperty(pos, "geocodeTimestamp"));
    }


    protected List<Attribute> getAttributes()
    {
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(createAttribute("latitude"));
        attributes.add(createAttribute("longitude"));
        attributes.add(createAttribute("geocodeTimestamp"));
        return attributes;
    }


    private Attribute createAttribute(String qualifier)
    {
        Attribute attribute = new Attribute();
        attribute.setQualifier(qualifier);
        return attribute;
    }


    @Required
    public void setGeoServiceWrapper(GeoWebServiceWrapper geoServiceWrapper)
    {
        this.geoServiceWrapper = geoServiceWrapper;
    }


    protected GeoWebServiceWrapper getGeoServiceWrapper()
    {
        return this.geoServiceWrapper;
    }


    protected NotificationService getNotificationService()
    {
        return this.notificationService;
    }


    @Required
    public void setNotificationService(NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }
}
