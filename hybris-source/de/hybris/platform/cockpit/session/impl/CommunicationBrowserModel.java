package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.CommentMainAreaComponentFactory;
import de.hybris.platform.cockpit.components.contentbrowser.MainAreaComponentFactory;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.CockpitEventAcceptor;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.ConfigurableBrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.comments.model.ReplyModel;
import de.hybris.platform.comments.services.CommentService;
import de.hybris.platform.core.PK;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.zkoss.util.resource.Labels;
import org.zkoss.zkplus.spring.SpringUtil;

public class CommunicationBrowserModel extends DefaultSearchBrowserModel implements ConfigurableBrowserModel, CockpitEventAcceptor
{
    private CommentService commentService;
    private List<MainAreaComponentFactory> viewModes = null;
    private final HashSet<PK> openComments = new HashSet<>();


    public List<MainAreaComponentFactory> getAvailableViewModes()
    {
        if(this.viewModes == null)
        {
            this.viewModes = new ArrayList<>();
            this.viewModes.add(new CommentMainAreaComponentFactory());
        }
        return this.viewModes;
    }


    public CommentService getCommentService()
    {
        if(this.commentService == null)
        {
            this.commentService = (CommentService)SpringUtil.getBean("commentService");
        }
        return this.commentService;
    }


    public String getLabel()
    {
        return Labels.getLabel("communicationcenter.browser.label");
    }


    public void setOpen(List<TypedObject> abstractComments)
    {
        this.openComments.clear();
        for(TypedObject typedObject : abstractComments)
        {
            if(typedObject != null)
            {
                Object object = typedObject.getObject();
                if(object instanceof AbstractCommentModel)
                {
                    this.openComments.add(((AbstractCommentModel)object).getPk());
                    List<TypedObject> parents = getParentComments(typedObject);
                    for(TypedObject parent : parents)
                    {
                        if(parent != null)
                        {
                            Object parentObject = parent.getObject();
                            if(parentObject instanceof AbstractCommentModel)
                            {
                                this.openComments.add(((AbstractCommentModel)parentObject).getPk());
                            }
                        }
                    }
                }
            }
        }
        fireChanged();
    }


    public HashSet<PK> getOpenComments()
    {
        return this.openComments;
    }


    private List<TypedObject> getParentComments(TypedObject abstractComment)
    {
        List<TypedObject> parentComments = new ArrayList<>();
        if(abstractComment != null)
        {
            Object object = abstractComment.getObject();
            if(object instanceof ReplyModel)
            {
                ReplyModel parent = ((ReplyModel)object).getParent();
                if(parent != null)
                {
                    parentComments.add(UISessionUtils.getCurrentSession().getTypeService().wrapItem(parent));
                    parentComments.addAll(getParentComments(UISessionUtils.getCurrentSession().getTypeService().wrapItem(parent)));
                }
                else if(((ReplyModel)object).getComment() != null)
                {
                    parentComments.add(UISessionUtils.getCurrentSession().getTypeService()
                                    .wrapItem(((ReplyModel)object).getComment()));
                }
            }
        }
        return parentComments;
    }


    public void onCockpitEvent(CockpitEvent event)
    {
        if(event instanceof ItemChangedEvent)
        {
            ItemChangedEvent ice = (ItemChangedEvent)event;
            boolean typeMatch = false;
            if(ice.getItem() != null)
            {
                Object object = ice.getItem().getObject();
                if(object instanceof AbstractCommentModel || object instanceof de.hybris.platform.comments.model.CommentAttachmentModel)
                {
                    typeMatch = true;
                }
            }
            boolean propertyMatch = ice.getProperties().contains(UISessionUtils.getCurrentSession().getTypeService()
                            .getPropertyDescriptor("Item.comments"));
            if((ItemChangedEvent.ChangeType.CREATED.equals(ice.getChangeType()) || ItemChangedEvent.ChangeType.CHANGED.equals(ice.getChangeType())) && (propertyMatch || typeMatch))
            {
                updateItems();
            }
        }
    }


    public String getBrowserCode()
    {
        return null;
    }


    public Class<? extends AbstractContentBrowser> getViewClass()
    {
        return null;
    }


    public boolean isInitiallyOpen()
    {
        return false;
    }


    public void setBrowserCode(String browserCode)
    {
    }


    public void setInitiallyOpen(boolean open)
    {
    }


    public void setViewClass(Class<? extends AbstractContentBrowser> viewClass)
    {
    }
}
