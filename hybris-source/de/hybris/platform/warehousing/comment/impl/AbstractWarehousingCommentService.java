package de.hybris.platform.warehousing.comment.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.comments.model.DomainModel;
import de.hybris.platform.comments.services.impl.DefaultCommentService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.warehousing.comment.WarehousingCommentService;
import de.hybris.platform.warehousing.data.comment.WarehousingCommentContext;
import de.hybris.platform.warehousing.data.comment.WarehousingCommentEventType;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractWarehousingCommentService extends DefaultCommentService implements WarehousingCommentService
{
    protected static final String DOMAIN_CODE = "warehousing";
    protected static final String DOMAIN_NAME = "Warehousing Domain";
    private transient TimeService timeService;
    private transient UserService userService;


    public CommentModel createAndSaveComment(WarehousingCommentContext context, String code)
    {
        Preconditions.checkArgument(!Objects.isNull(context.getItem()), "WarehousingCommentContext.item argument cannot be null.");
        Preconditions.checkArgument(!Objects.isNull(context.getText()), "WarehousingCommentContext.text argument cannot be null.");
        UserModel currentUser = getUserService().getCurrentUser();
        CommentModel comment = (CommentModel)getModelService().create(CommentModel.class);
        comment.setCode(code);
        comment.setSubject(context.getSubject());
        comment.setText(context.getText());
        comment.setCreationtime(getTimeService().getCurrentTime());
        comment.setOwner(context.getItem());
        comment.setAuthor(currentUser);
        comment.setCommentType(getOrCreateCommentType(context.getCommentType()));
        comment.setComponent(getOrCreateComponent(context.getCommentType()));
        List<CommentModel> comments = Lists.newArrayList();
        ItemModel item = context.getItem();
        if(!item.getComments().isEmpty())
        {
            comments.addAll(item.getComments());
        }
        comments.add(comment);
        item.setComments(comments);
        getModelService().save(comment);
        getModelService().save(item);
        return comment;
    }


    public DomainModel getOrCreateDomainForCodeWarehousing()
    {
        DomainModel domain = getDomainForCode("warehousing");
        if(Objects.isNull(domain))
        {
            domain = (DomainModel)getModelService().create(DomainModel.class);
            domain.setCode("warehousing");
            domain.setName("Warehousing Domain");
            getModelService().save(domain);
        }
        return domain;
    }


    public CommentTypeModel getOrCreateCommentType(WarehousingCommentEventType eventType)
    {
        CommentTypeModel commentType = getCommentTypeForCode(getOrCreateComponent(eventType), eventType.getCommentTypeCode());
        if(Objects.isNull(commentType))
        {
            commentType = (CommentTypeModel)getModelService().create(CommentTypeModel.class);
            commentType.setCode(eventType.getCommentTypeCode());
            commentType.setName(eventType.getCommentTypeName());
            commentType.setDomain(getOrCreateDomainForCodeWarehousing());
            getModelService().save(commentType);
        }
        return commentType;
    }


    public ComponentModel getOrCreateComponent(WarehousingCommentEventType eventType)
    {
        DomainModel domain = getOrCreateDomainForCodeWarehousing();
        ComponentModel component = getComponentForCode(domain, eventType.getComponentCode());
        if(Objects.isNull(component))
        {
            component = (ComponentModel)getModelService().create(ComponentModel.class);
            component.setCode(eventType.getComponentCode());
            component.setName(eventType.getComponentName());
            component.setDomain(domain);
            getModelService().save(component);
        }
        return component;
    }


    protected TimeService getTimeService()
    {
        return this.timeService;
    }


    @Required
    public void setTimeService(TimeService timeService)
    {
        this.timeService = timeService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}
