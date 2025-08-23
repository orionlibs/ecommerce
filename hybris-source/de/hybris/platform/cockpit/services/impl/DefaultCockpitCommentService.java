package de.hybris.platform.cockpit.services.impl;

import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.meta.impl.ItemAttributePropertyDescriptor;
import de.hybris.platform.cockpit.model.search.Operator;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.model.search.SearchParameterDescriptor;
import de.hybris.platform.cockpit.model.search.SearchParameterValue;
import de.hybris.platform.cockpit.model.search.SearchType;
import de.hybris.platform.cockpit.services.CockpitCommentService;
import de.hybris.platform.cockpit.services.search.impl.ItemAttributeSearchDescriptor;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.comments.constants.GeneratedCommentsConstants;
import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.comments.model.CommentAttachmentModel;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.CommentUserSettingModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.comments.model.DomainModel;
import de.hybris.platform.comments.model.ReplyModel;
import de.hybris.platform.comments.services.CommentService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;

public class DefaultCockpitCommentService extends AbstractServiceImpl implements CockpitCommentService
{
    private CommentService commentService;
    private UserService userService;


    public List<CommentModel> getItemComments(ItemModel item, UserModel user, Collection<ComponentModel> components, Collection<CommentTypeModel> types, int offset, int count)
    {
        return getCommentService().getItemComments(item, user, components, types, offset, count);
    }


    public Query getItemCommentsQuery(ItemModel item, UserModel user, Collection<ComponentModel> components, Collection<CommentTypeModel> types)
    {
        List<SearchType> searchTypes = Collections.singletonList(UISessionUtils.getCurrentSession().getSearchService()
                        .getSearchType(UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(GeneratedCommentsConstants.TC.COMMENT)));
        Query query = new Query(searchTypes, null, 0, -1);
        BaseType baseType = UISessionUtils.getCurrentSession().getTypeService().getBaseType(GeneratedCommentsConstants.TC.COMMENT);
        query.addSortCriterion(UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor((ObjectType)baseType, baseType
                        .getCode() + ".creationtime"), false);
        query.addParameterValue(
                        getParameterValue(GeneratedCommentsConstants.TC.COMMENT, baseType.getCode() + ".relatedItems", item));
        query.addParameterValue(getParameterValue(GeneratedCommentsConstants.TC.COMMENT, baseType
                        .getCode() + ".commentType", types, Operator.IN));
        query.addParameterValue(getParameterValue(GeneratedCommentsConstants.TC.COMMENT, baseType.getCode() + ".component", components, Operator.IN));
        if(user != null)
        {
            List<SearchParameterValue> orValueList = new ArrayList<>();
            orValueList.add(
                            getParameterValue(GeneratedCommentsConstants.TC.COMMENT, baseType.getCode() + ".author", user.getPk()));
            orValueList.add(
                            getParameterValue(GeneratedCommentsConstants.TC.COMMENT, baseType.getCode() + ".watchers", user.getPk()));
            orValueList.add(getParameterValue(GeneratedCommentsConstants.TC.COMMENT, baseType.getCode() + ".assignedTo", user
                            .getPk()));
            query.addParameterOrValues(orValueList);
        }
        return query;
    }


    public Query getCurrentUserCommentsQuery()
    {
        List<SearchType> searchTypes = Collections.singletonList(UISessionUtils.getCurrentSession().getSearchService()
                        .getSearchType(UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(GeneratedCommentsConstants.TC.COMMENT)));
        Query query = new Query(searchTypes, null, 0, -1);
        BaseType baseType = UISessionUtils.getCurrentSession().getTypeService().getBaseType(GeneratedCommentsConstants.TC.COMMENT);
        query.addSortCriterion(UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor((ObjectType)baseType, baseType
                        .getCode() + ".creationtime"), false);
        query.addParameterValue(getParameterValue(GeneratedCommentsConstants.TC.COMMENT, baseType.getCode() + ".author",
                        UISessionUtils.getCurrentSession().getUser().getPk()));
        return query;
    }


    public ReplyModel createReply(UserModel author, AbstractCommentModel comment, String text)
    {
        ReplyModel reply = getCommentService().createReply(author, comment, text);
        getModelService().save(reply);
        return reply;
    }


    public CommentModel createItemComment(UserModel author, ComponentModel component, CommentTypeModel type, Collection<ItemModel> items, String subject, String text)
    {
        CommentModel comment = (CommentModel)getModelService().create(CommentModel.class);
        comment.setAuthor(author);
        comment.setComponent(component);
        comment.setCommentType(type);
        comment.setSubject(subject);
        comment.setText(text);
        comment.setRelatedItems(items);
        getModelService().save(comment);
        return comment;
    }


    public List<ReplyModel> getDirectReplies(CommentModel comment, int offset, int count)
    {
        return getCommentService().getDirectReplies(comment, offset, count);
    }


    public List<CommentModel> getComments(UserModel user, Collection<ComponentModel> components, Collection<CommentTypeModel> types, int offset, int count)
    {
        return getCommentService().getComments(user, components, types, offset, count);
    }


    public List<CommentModel> getCurrentUserComments()
    {
        return getCurrentUserComments(Executions.getCurrent().getDesktop());
    }


    public List<CommentModel> getCurrentUserComments(Desktop desktop)
    {
        String domainCode = UITools.getCockpitParameter("default.commentsection.domaincode", desktop);
        String componentCode = UITools.getCockpitParameter("default.commentsection.componentcode", desktop);
        String commentTypeCode = UITools.getCockpitParameter("default.commentsection.commenttypecode", desktop);
        if(domainCode != null && componentCode != null && commentTypeCode != null)
        {
            DomainModel domain = getCommentService().getDomainForCode(domainCode);
            ComponentModel component = getCommentService().getComponentForCode(domain, componentCode);
            CommentTypeModel commentType = getCommentService().getCommentTypeForCode(component, commentTypeCode);
            return getComments(getUserService().getCurrentUser(), Collections.singleton(component),
                            Collections.singleton(commentType), 0, -1);
        }
        return Collections.EMPTY_LIST;
    }


    private SearchParameterValue getParameterValue(String objectTypeName, String propertyDescriptorName, Object value, Operator operator)
    {
        BaseType baseType = UISessionUtils.getCurrentSession().getTypeService().getBaseType(objectTypeName);
        PropertyDescriptor propertyDescriptor = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor((ObjectType)baseType, propertyDescriptorName);
        ItemAttributeSearchDescriptor searchDescriptor = new ItemAttributeSearchDescriptor((ItemAttributePropertyDescriptor)propertyDescriptor);
        Operator searchOperator = operator;
        if(searchOperator == null)
        {
            searchOperator = searchDescriptor.getDefaultOperator();
        }
        return new SearchParameterValue((SearchParameterDescriptor)searchDescriptor, value, searchOperator);
    }


    private SearchParameterValue getParameterValue(String objectTypeName, String propertyDescriptorName, Object value)
    {
        return getParameterValue(objectTypeName, propertyDescriptorName, value, null);
    }


    public boolean isRead(TypedObject commentItem)
    {
        Object object = commentItem.getObject();
        UserModel user = UISessionUtils.getCurrentSession().getUser();
        if(object instanceof AbstractCommentModel)
        {
            AbstractCommentModel comment = (AbstractCommentModel)object;
            Boolean read = getCommentService().getUserSetting(user, comment).getRead();
            if(!Boolean.TRUE.equals(read) && comment.getAuthor().equals(user))
            {
                setRead(user, comment, true);
                return true;
            }
            if(read != null)
            {
                return read.booleanValue();
            }
        }
        return false;
    }


    public void setRead(TypedObject commentItem, boolean read)
    {
        if(commentItem.getObject() instanceof AbstractCommentModel)
        {
            setRead(UISessionUtils.getCurrentSession().getUser(), (AbstractCommentModel)commentItem.getObject(), read);
        }
    }


    protected void setRead(UserModel user, AbstractCommentModel commentItemModel, boolean read)
    {
        CommentUserSettingModel userSetting = getCommentService().getUserSetting(user, commentItemModel);
        userSetting.setRead(Boolean.valueOf(read));
        getModelService().save(userSetting);
    }


    @Deprecated
    public CommentService getCommentService()
    {
        return this.commentService;
    }


    @Required
    public void setCommentService(CommentService commentService)
    {
        this.commentService = commentService;
    }


    @Deprecated
    public UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    public List<ItemModel> getItemsFromCommentAttachments(AbstractCommentModel abstractComment)
    {
        List<ItemModel> attachments = new ArrayList<>();
        for(CommentAttachmentModel attachment : abstractComment.getAttachments())
        {
            attachments.add(attachment.getItem());
        }
        return attachments;
    }


    public void saveChangedComments(List<CommentModel> comments)
    {
        for(CommentModel comment : comments)
        {
            if(getModelService().isModified(comment))
            {
                getModelService().save(comment);
            }
        }
    }
}
