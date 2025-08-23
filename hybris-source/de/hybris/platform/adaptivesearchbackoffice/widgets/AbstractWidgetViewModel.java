package de.hybris.platform.adaptivesearchbackoffice.widgets;

import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import java.text.MessageFormat;
import org.zkoss.zk.ui.select.annotation.WireVariable;

public abstract class AbstractWidgetViewModel
{
    @WireVariable
    private WidgetInstanceManager widgetInstanceManager;


    protected WidgetInstanceManager getWidgetInstanceManager()
    {
        return this.widgetInstanceManager;
    }


    protected Widgetslot getWidgetslot()
    {
        return this.widgetInstanceManager.getWidgetslot();
    }


    protected TypedSettingsMap getWidgetSettings()
    {
        return this.widgetInstanceManager.getWidgetSettings();
    }


    protected WidgetModel getModel()
    {
        return this.widgetInstanceManager.getModel();
    }


    protected void sendOutput(String socketId, Object data)
    {
        this.widgetInstanceManager.sendOutput(socketId, data);
    }


    public String format(String pattern, Object... arguments)
    {
        return MessageFormat.format(pattern, arguments);
    }
}
