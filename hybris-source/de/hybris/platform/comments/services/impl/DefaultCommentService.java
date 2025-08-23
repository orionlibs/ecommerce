package de.hybris.platform.comments.services.impl;

import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.CommentUserSettingModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.comments.model.DomainModel;
import de.hybris.platform.comments.model.ReplyModel;
import de.hybris.platform.comments.services.CommentDao;
import de.hybris.platform.comments.services.CommentService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCommentService extends AbstractBusinessService implements CommentService
{
    private static final Logger LOG = Logger.getLogger(DefaultCommentService.class);
    @Deprecated(since = "ages", forRemoval = true)
    private FlexibleSearchService flexibleSearchService;
    private CommentDao commentDao;


    public List<CommentModel> getComments(UserModel user, Collection<ComponentModel> components, Collection<CommentTypeModel> types, int offset, int count)
    {
        List<CommentModel> result = this.commentDao.findCommentsByType(user, components, types, offset, count);
        return (result == null) ? Collections.EMPTY_LIST : Collections.<CommentModel>unmodifiableList(result);
    }


    public List<CommentModel> getComments(UserModel user, Collection<ComponentModel> components, int offset, int count)
    {
        List<CommentModel> result = this.commentDao.findCommentsByUser(user, components, offset, count);
        return (result == null) ? Collections.EMPTY_LIST : Collections.<CommentModel>unmodifiableList(result);
    }


    public List<CommentModel> getComments(UserModel user, DomainModel domain, Collection<CommentTypeModel> types, int offset, int count)
    {
        if(domain == null)
        {
            throw new IllegalArgumentException("Domain must be specified.");
        }
        List<CommentModel> result = this.commentDao.findCommentsByType(user, domain.getComponents(), types, offset, count);
        return (result == null) ? Collections.EMPTY_LIST : Collections.<CommentModel>unmodifiableList(result);
    }


    public List<CommentModel> getComments(UserModel user, DomainModel domain, int offset, int count)
    {
        if(domain == null)
        {
            throw new IllegalArgumentException("Domain must be specified.");
        }
        List<CommentModel> result = this.commentDao.findCommentsByUser(user, domain.getComponents(), offset, count);
        return (result == null) ? Collections.EMPTY_LIST : Collections.<CommentModel>unmodifiableList(result);
    }


    public List<ReplyModel> getDirectReplies(CommentModel comment, int offset, int count)
    {
        if(comment == null)
        {
            throw new IllegalArgumentException("Comment must be specified.");
        }
        List<ReplyModel> result = this.commentDao.findDirectRepliesByComment(comment, offset, count);
        return (result == null) ? Collections.EMPTY_LIST : Collections.<ReplyModel>unmodifiableList(result);
    }


    public List<CommentModel> getItemComments(ItemModel item, UserModel user, Collection<ComponentModel> components, Collection<CommentTypeModel> types, int offset, int count)
    {
        if(item == null)
        {
            throw new IllegalArgumentException("Item must be specified.");
        }
        List<CommentModel> result = this.commentDao.findCommentsByItemAndType(user, components, item, types, offset, count);
        return (result == null) ? Collections.EMPTY_LIST : Collections.<CommentModel>unmodifiableList(result);
    }


    public List<CommentModel> getItemComments(ItemModel item, UserModel user, Collection<ComponentModel> components, int offset, int count)
    {
        if(item == null)
        {
            throw new IllegalArgumentException("Item must be specified.");
        }
        List<CommentModel> result = this.commentDao.findCommentsByItem(user, components, item, offset, count);
        return (result == null) ? Collections.EMPTY_LIST : Collections.<CommentModel>unmodifiableList(result);
    }


    public List<CommentModel> getItemComments(ItemModel item, UserModel user, DomainModel domain, Collection<CommentTypeModel> types, int offset, int count)
    {
        if(domain == null)
        {
            throw new IllegalArgumentException("Domain must be specified.");
        }
        if(item == null)
        {
            throw new IllegalArgumentException("Item must be specified.");
        }
        List<CommentModel> result = this.commentDao.findCommentsByItemAndType(user, domain.getComponents(), item, types, offset, count);
        return (result == null) ? Collections.EMPTY_LIST : Collections.<CommentModel>unmodifiableList(result);
    }


    public List<CommentModel> getItemComments(ItemModel item, UserModel user, DomainModel domain, int offset, int count)
    {
        if(domain == null)
        {
            throw new IllegalArgumentException("Domain must be specified.");
        }
        if(item == null)
        {
            throw new IllegalArgumentException("Item must be specified.");
        }
        List<CommentModel> result = this.commentDao.findCommentsByItem(user, domain.getComponents(), item, offset, count);
        return (result == null) ? Collections.EMPTY_LIST : Collections.<CommentModel>unmodifiableList(result);
    }


    public ComponentModel getComponentForCode(DomainModel domain, String componentCode)
    {
        if(domain == null)
        {
            throw new IllegalArgumentException("Domain must be specified.");
        }
        if(StringUtils.isBlank(componentCode))
        {
            throw new IllegalArgumentException("Component code must be specified.");
        }
        ComponentModel component = null;
        List<ComponentModel> components = this.commentDao.findComponentsByDomainAndCode(domain, componentCode);
        if(!components.isEmpty())
        {
            component = components.iterator().next();
            if(components.size() > 1)
            {
                LOG.warn("Ambigious components found with code '" + componentCode + "'. Returning first match.");
            }
        }
        return component;
    }


    public DomainModel getDomainForCode(String domainCode)
    {
        if(StringUtils.isBlank(domainCode))
        {
            throw new IllegalArgumentException("Domain code must be specified.");
        }
        DomainModel domain = null;
        List<DomainModel> domains = this.commentDao.findDomainsByCode(domainCode);
        if(!domains.isEmpty())
        {
            domain = domains.iterator().next();
            if(domains.size() > 1)
            {
                LOG.warn("Ambigious domains found with code '" + domainCode + "'. Returning first match.");
            }
        }
        return domain;
    }


    public CommentTypeModel getCommentTypeForCode(ComponentModel comp, String commentTypeCode)
    {
        if(comp == null)
        {
            throw new IllegalArgumentException("Component must be specified.");
        }
        if(StringUtils.isBlank(commentTypeCode))
        {
            throw new IllegalArgumentException("Comment type code must be specified.");
        }
        CommentTypeModel commentType = null;
        for(CommentTypeModel comType : getAvailableCommentTypes(comp))
        {
            if(comType.getCode().equals(commentTypeCode))
            {
                commentType = comType;
                break;
            }
        }
        return commentType;
    }


    @Required
    public void setCommentDao(CommentDao commentDao)
    {
        this.commentDao = commentDao;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public CommentDao getCommentDao()
    {
        return this.commentDao;
    }


    @Required
    @Deprecated(since = "ages", forRemoval = true)
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    @Deprecated(since = "ages", forRemoval = true)
    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    public CommentUserSettingModel getUserSetting(UserModel user, AbstractCommentModel commentItem)
    {
        List<CommentUserSettingModel> result = this.commentDao.findUserSettingsByComment(user, commentItem);
        if(result == null)
        {
            LOG.error("SearchResult was null when searching for UserSetting for commentItem '" + commentItem + "' and user '" + user + "'");
            return null;
        }
        if(result.size() > 1)
        {
            LOG.warn("More than one UserSetting found for commentItem '" + commentItem + "' and user '" + user + "'");
            return result.iterator().next();
        }
        if(result.isEmpty())
        {
            CommentUserSettingModel newUserSettingModel = (CommentUserSettingModel)getModelService().create(CommentUserSettingModel.class);
            newUserSettingModel.setUser(user);
            newUserSettingModel.setComment(commentItem);
            getModelService().save(newUserSettingModel);
            return newUserSettingModel;
        }
        return result.iterator().next();
    }


    public ReplyModel createReply(UserModel author, AbstractCommentModel comment, String text)
    {
        ReplyModel reply = (ReplyModel)getModelService().create(ReplyModel.class);
        CommentModel rootComment = null;
        if(comment instanceof ReplyModel)
        {
            rootComment = ((ReplyModel)comment).getComment();
            reply.setParent((ReplyModel)comment);
        }
        else if(comment instanceof CommentModel)
        {
            rootComment = (CommentModel)comment;
        }
        reply.setComment(rootComment);
        reply.setAuthor(author);
        reply.setSubject(comment.getSubject());
        reply.setText(text);
        return reply;
    }


    public Collection<CommentTypeModel> getAvailableCommentTypes(ComponentModel component)
    {
        Collection<CommentTypeModel> commentTypes = null;
        DomainModel domain = component.getDomain();
        if(domain != null)
        {
            commentTypes = domain.getCommentTypes();
        }
        return (commentTypes == null) ? Collections.EMPTY_LIST : Collections.<CommentTypeModel>unmodifiableCollection(commentTypes);
    }


    public CommentTypeModel getCommentTypeByCode(ComponentModel component, String commentTypeCode)
    {
        return getCommentTypeForCode(component, commentTypeCode);
    }


    public ComponentModel getComponentByCode(DomainModel domain, String componentCode)
    {
        return getComponentForCode(domain, componentCode);
    }


    public DomainModel getDomainByCode(String domainCode)
    {
        return getDomainForCode(domainCode);
    }
}
