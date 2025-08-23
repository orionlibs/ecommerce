package de.hybris.platform.cockpit.widgets.controllers.impl;

import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.CockpitEventAcceptor;
import de.hybris.platform.cockpit.widgets.adapters.WidgetAdapter;
import de.hybris.platform.cockpit.widgets.controllers.WidgetController;
import de.hybris.platform.cockpit.widgets.events.WidgetEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractWidgetController implements WidgetController
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractWidgetController.class);
    private final Map<String, List<CockpitEventAcceptor>> acceptorMap = new HashMap<>();


    protected Map<String, List<CockpitEventAcceptor>> getAcceptors()
    {
        return this.acceptorMap;
    }


    public void addCockpitEventAcceptor(String context, CockpitEventAcceptor acceptor)
    {
        if(this.acceptorMap.get(context) == null)
        {
            this.acceptorMap.put(context, new LinkedList<>());
        }
        if(!((List)this.acceptorMap.get(context)).contains(acceptor))
        {
            ((List<CockpitEventAcceptor>)this.acceptorMap.get(context)).add(acceptor);
        }
    }


    public void removeCockpitEventAcceptor(String context, CockpitEventAcceptor acceptor)
    {
        List<CockpitEventAcceptor> acceptors = this.acceptorMap.get(context);
        if(acceptors != null)
        {
            acceptors.remove(acceptor);
        }
    }


    public void setCockpitEventAcceptors(Map<String, List<CockpitEventAcceptor>> accMap)
    {
        if(accMap != null && !accMap.isEmpty())
        {
            for(String ctx : accMap.keySet())
            {
                List<CockpitEventAcceptor> acceptors = accMap.get(ctx);
                if(CollectionUtils.isNotEmpty(acceptors))
                {
                    this.acceptorMap.put(ctx, acceptors);
                    for(CockpitEventAcceptor cockpitEventAcceptor : acceptors)
                    {
                        if(cockpitEventAcceptor instanceof WidgetAdapter && ((WidgetAdapter)cockpitEventAcceptor)
                                        .isAutoControllerEnabled())
                        {
                            ((WidgetAdapter)cockpitEventAcceptor).setWidgetController(this);
                        }
                    }
                }
            }
        }
    }


    public abstract void dispatchEvent(String paramString, Object paramObject, Map<String, Object> paramMap);


    protected void dispatchEvent(String context, WidgetEvent event)
    {
        List<CockpitEventAcceptor> ctxAcceptors = getAcceptors().get(context);
        if(ctxAcceptors != null && !ctxAcceptors.isEmpty())
        {
            Object source = event.getSource();
            List<CockpitEventAcceptor> acceptors = new LinkedList<>(ctxAcceptors);
            for(CockpitEventAcceptor cockpitEventAcceptor : acceptors)
            {
                if(cockpitEventAcceptor.equals(source))
                {
                    continue;
                }
                try
                {
                    cockpitEventAcceptor.onCockpitEvent((CockpitEvent)event);
                }
                catch(Exception e)
                {
                    LOG.error("Could not dispatch event to acceptor '" + cockpitEventAcceptor + "'.", e);
                }
            }
        }
    }
}
