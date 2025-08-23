package de.hybris.platform.cockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.AbstractListViewAction;
import de.hybris.platform.cockpit.services.CockpitCommentService;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.comments.model.DomainModel;
import de.hybris.platform.comments.services.CommentService;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkplus.spring.SpringUtil;

public abstract class AbstractCommentAction extends AbstractListViewAction
{
    public static final String AREA = "browserArea";
    public static final String EDITOR_AREA = "editorArea";
    public static final String BROWSER_AREA = "contentArea";
    public static final String PARENT = "parent";
    public static final Object UPDATELISTENER = "updateListener";
    private DomainModel domain;
    private ComponentModel component;
    private CommentTypeModel commentType;
    private CommentService commentService;
    private CockpitCommentService cockpitCommentService;


    public CommentService getCommentService()
    {
        if(this.commentService == null)
        {
            this.commentService = (CommentService)SpringUtil.getBean("commentService");
        }
        return this.commentService;
    }


    public CockpitCommentService getCockpitCommentService()
    {
        if(this.cockpitCommentService == null)
        {
            this.cockpitCommentService = (CockpitCommentService)SpringUtil.getBean("cockpitCommentService");
        }
        return this.cockpitCommentService;
    }


    public void setDomain(DomainModel domain)
    {
        this.domain = domain;
    }


    public DomainModel getDomain()
    {
        if(this.domain == null)
        {
            String domainCode = UITools.getCockpitParameter("default.commentsection.domaincode", Executions.getCurrent());
            if(domainCode != null)
            {
                setDomain(getCommentService().getDomainForCode(domainCode));
            }
        }
        return this.domain;
    }


    public void setComponent(ComponentModel component)
    {
        this.component = component;
    }


    public ComponentModel getComponent()
    {
        if(this.component == null)
        {
            String componentCode = UITools.getCockpitParameter("default.commentsection.componentcode", Executions.getCurrent());
            if(getDomain() != null && componentCode != null)
            {
                setComponent(getCommentService().getComponentForCode(getDomain(), componentCode));
            }
        }
        return this.component;
    }


    public void setCommentType(CommentTypeModel commentType)
    {
        this.commentType = commentType;
    }


    public CommentTypeModel getCommentType()
    {
        if(this.commentType == null)
        {
            String commentTypeCode = UITools.getCockpitParameter("default.commentsection.commenttypecode",
                            Executions.getCurrent());
            if(getDomain() != null && getComponent() != null && commentTypeCode != null)
            {
                setCommentType(getCommentService().getCommentTypeForCode(getComponent(), commentTypeCode));
            }
        }
        return this.commentType;
    }
}
