package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.PushComponent;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.CommentCockpitEvent;
import de.hybris.platform.cockpit.services.CockpitCommentService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.comments.model.CommentModel;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.zkoss.zk.ui.Desktop;

public class CommentPushController extends AbstractPushController
{
    private static final Logger LOG = LoggerFactory.getLogger(CommentPushController.class);
    private final List<CommentModel> lastUserComments = new ArrayList<>();
    private TypeService cockpitTypeService;
    private boolean commentsEnabled = false;


    public CommentPushController()
    {
        this(null);
    }


    public CommentPushController(int updateInterval)
    {
        this(null, updateInterval);
    }


    public CommentPushController(PushComponent component)
    {
        this(component, 60000);
    }


    public CommentPushController(PushComponent component, int updateInterval)
    {
        super(component, updateInterval);
    }


    protected void after()
    {
    }


    protected void before()
    {
    }


    public boolean isUpdateNeeded()
    {
        boolean updateNeeded = false;
        if(this.commentsEnabled)
        {
            CockpitCommentService commentService = getCockpitCommentService();
            if(commentService == null)
            {
                LOG.warn("Could not retrieve Cockpit comment service.");
            }
            else
            {
                List<CommentModel> userComments = commentService.getCurrentUserComments(getDesktop());
                if(!this.lastUserComments.equals(userComments))
                {
                    this.lastUserComments.clear();
                    this.lastUserComments.addAll(userComments);
                    updateNeeded = true;
                }
            }
        }
        return updateNeeded;
    }


    public void update()
    {
        addEvent((CockpitEvent)new CommentCockpitEvent(getComponent(), getCockpitTypeService().wrapItems(this.lastUserComments)));
    }


    protected void loadParameters()
    {
        super.loadParameters();
        Desktop desktop = getDesktop();
        if(desktop != null)
        {
            this.commentsEnabled = BooleanUtils.toBoolean(UITools.getCockpitParameter("default.comments.enabled", desktop));
        }
    }


    protected CockpitCommentService getCockpitCommentService()
    {
        CockpitCommentService commentService = null;
        WebApplicationContext webCtx = null;
        try
        {
            webCtx = getWebApplicationContext();
            if(webCtx == null)
            {
                LOG.warn("Could not retrieve Cockpit Comment service. Reason: No web application context available.");
            }
            else
            {
                commentService = (CockpitCommentService)webCtx.getBean("cockpitCommentService");
            }
        }
        catch(ClassCastException cce)
        {
            LOG.error("Could not retrieve Cockpit Comment service.", cce);
        }
        catch(IllegalStateException ise)
        {
            LOG.error("Can not retrieve Cockpit Comment service. Reason: Error occurred while retrieving web application context.", ise);
        }
        return commentService;
    }


    public TypeService getCockpitTypeService()
    {
        if(this.cockpitTypeService == null && !getParameters().isEmpty() && getParameters().get("cockpitTypeService") != null)
        {
            this.cockpitTypeService = (TypeService)getParameters().get("cockpitTypeService");
        }
        return this.cockpitTypeService;
    }
}
