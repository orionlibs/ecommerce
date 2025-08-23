package de.hybris.platform.cmscockpit.callbackevents;

import de.hybris.platform.cmscockpit.components.liveedit.CallbackEventHandler;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.platform.cmscockpit.events.impl.CmsUrlChangeEvent;
import de.hybris.platform.cmscockpit.session.impl.LiveEditBrowserArea;
import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;

public class UrlChangeCallbackEventHandler implements CallbackEventHandler<LiveEditView>
{
    private Converter<String[], CmsUrlChangeEvent> urlChangeEventConverter;


    public String getEventId()
    {
        return "urlChange";
    }


    public void onCallbackEvent(LiveEditView view, Map<String, Object> attributeMap) throws Exception
    {
        view.getContentFrame().setVisible(true);
        UICockpitPerspective currentPerspective = UISessionUtils.getCurrentSession().getCurrentPerspective();
        if(!view.getModel().isPreviewDataValid())
        {
            Notification notification = new Notification(Labels.getLabel("cmscockpit.liveditsession.expired"), Labels.getLabel("cmscockpit.liveditsession.expired.description"));
            currentPerspective.getNotifier().setNotification(notification);
            UIBrowserArea currentBrowserArea = currentPerspective.getBrowserArea();
            if(currentBrowserArea instanceof LiveEditBrowserArea)
            {
                LiveEditBrowserArea liveEditBrowserArea = (LiveEditBrowserArea)currentBrowserArea;
                liveEditBrowserArea.fireModeChange(false);
            }
        }
        String[] passedAttributes = (String[])attributeMap.values().toArray((Object[])new String[0]);
        CmsUrlChangeEvent cmsUrlChangeEvent = (CmsUrlChangeEvent)getUrlChangeEventConverter().convert(passedAttributes);
        UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)cmsUrlChangeEvent);
    }


    public Converter<String[], CmsUrlChangeEvent> getUrlChangeEventConverter()
    {
        return this.urlChangeEventConverter;
    }


    @Required
    public void setUrlChangeEventConverter(Converter<String[], CmsUrlChangeEvent> urlChangeEventConverter)
    {
        this.urlChangeEventConverter = urlChangeEventConverter;
    }
}
