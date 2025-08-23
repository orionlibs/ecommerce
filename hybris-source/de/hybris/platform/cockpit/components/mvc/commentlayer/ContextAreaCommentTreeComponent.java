package de.hybris.platform.cockpit.components.mvc.commentlayer;

import de.hybris.platform.cockpit.components.AdvancedGroupbox;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.listview.ActionColumnConfiguration;
import de.hybris.platform.cockpit.components.mvc.commentlayer.controller.ContextAreaCommentTreeController;
import de.hybris.platform.cockpit.components.mvc.commentlayer.model.CommentLayerAwareModel;
import de.hybris.platform.cockpit.components.sectionpanel.TooltipRenderer;
import de.hybris.platform.cockpit.model.listview.ValueHandler;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.comments.CommentLayerService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.ContextAreaCommentTreeModel;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.ReplyModel;
import de.hybris.platform.core.model.ItemModel;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;

public class ContextAreaCommentTreeComponent extends AbstractCommentTreeContextComponent
{
    private static final String TOGGLE_HIDE_IMG_NOT_ACTIVE = "cockpit/css/images/icon_status_preview_off.png";
    private static final String TOGGLE_HIDE_IMG_ACTIVE = "cockpit/css/images/icon_status_preview_on.png";
    public static final String COMMENT_LAYER_AWARE_MODEL = "commentLayerAwareModel";
    private static final String TEST_ID_PREFIX = "CommentLayerContextTableRecord_";
    private static final String IMG_STATUS_CHECK = "cockpit/css/images/icon_status_work_checked.png";
    private static final String IMG_STATUS_NOCHECK = "cockpit/css/images/icon_status_work_nocheck.png";
    private CommentLayerService commentLayerService;
    private final ContextAreaCommentTreeController controller;
    private TooltipRenderer tooltipRenderer;


    public ContextAreaCommentTreeComponent(ContextAreaCommentTreeController controller, ContextAreaCommentTreeModel model, AbstractContentBrowser contentBrowser)
    {
        super(model, contentBrowser);
        this.controller = controller;
    }


    public List<TypedObject> getComments()
    {
        List<CommentModel> comments = getCommentLayerService().getCommentsForCommentLayer(
                        UISessionUtils.getCurrentSession().getUser(), (ItemModel)getModel().getCommentLayerTarget());
        return UISessionUtils.getCurrentSession().getTypeService().wrapItems(comments);
    }


    public Div renderCommentsTree()
    {
        return renderView();
    }


    protected void renderStatusSection(Hbox commentHeader, AbstractCommentModel abstractComment)
    {
        Hbox customSection = new Hbox();
        customSection.setWidths("40%, 20%, 40%");
        boolean currentStatus = this.controller.getUserWorkingStatus(abstractComment);
        String statusTooltip = "commentlayer.context.status." + (currentStatus ? "checked" : "nocheck");
        String statusImageSrc = currentStatus ? "cockpit/css/images/icon_status_work_checked.png" : "cockpit/css/images/icon_status_work_nocheck.png";
        Div currentStatusDiv = new Div();
        Image statusImage = new Image(statusImageSrc);
        statusImage.setTooltiptext(Labels.getLabel(statusTooltip));
        currentStatusDiv.appendChild((Component)statusImage);
        currentStatusDiv.setSclass(currentStatus ? "commentstatuscheck" :
                        "commentstatusnocheck");
        currentStatusDiv.addEventListener("onClick", (EventListener)new Object(this, abstractComment, currentStatus));
        currentStatusDiv.setAction("onclick: return true;");
        customSection.appendChild((Component)currentStatusDiv);
        customSection.appendChild((Component)new Div());
        if(getModel().getCommentContextAwareModel().isCommentLayerActive() && abstractComment instanceof CommentModel)
        {
            CommentModel comment = (CommentModel)abstractComment;
            boolean commentVisible = this.controller.isCommentVisible(getModel(), comment);
            String visibilityDivTooltip = "commentlayer.context.toggle." + (commentVisible ? "hide" : "show");
            Div visibilityDiv = new Div();
            Image visibilityImage = new Image(commentVisible ? "cockpit/css/images/icon_status_preview_on.png" : "cockpit/css/images/icon_status_preview_off.png");
            visibilityImage.setTooltiptext(Labels.getLabel(visibilityDivTooltip));
            visibilityImage.setAction("onclick: return true;");
            visibilityDiv.appendChild((Component)visibilityImage);
            visibilityDiv.setSclass(currentStatus ? "commentvisibilitynotvisible" :
                            "commentvisibilityvisible");
            visibilityDiv.addEventListener("onClick", (EventListener)new Object(this, comment));
            customSection.appendChild((Component)visibilityDiv);
        }
        if(!abstractComment.getAttachments().isEmpty())
        {
            Image attachmentImage = new Image("/cockpit/images/icon_attachement.png");
            attachmentImage.setTooltiptext(Labels.getLabel("comment.attachment"));
            attachmentImage.setSclass("header");
            customSection.appendChild((Component)attachmentImage);
        }
        commentHeader.appendChild((Component)customSection);
    }


    protected void renderCommentDetailsBox(Div groupboxContentDiv, CommentModel comment)
    {
    }


    protected void renderCommentTextBox(Div groupboxContentDiv, CommentModel commentModel)
    {
    }


    protected void renderGroupboxHeaderClosed(AbstractCommentModel commentItemModel, Hbox commentHeader)
    {
        super.renderGroupboxHeaderClosed(commentItemModel, commentHeader);
        UITools.modifySClass((HtmlBasedComponent)commentHeader, "contextAreaPageCommentHeaderClosed", true);
        UITools.modifySClass((HtmlBasedComponent)commentHeader, "contextAreaPageCommentHeaderOpened", false);
        commentHeader.setWidths("5%, 5%, 90%");
    }


    protected void renderGroupboxHeaderOpen(AbstractCommentModel commentItemModel, Hbox commentHeader)
    {
        super.renderGroupboxHeaderClosed(commentItemModel, commentHeader);
        UITools.modifySClass((HtmlBasedComponent)commentHeader, "contextAreaPageCommentHeaderOpened", true);
        UITools.modifySClass((HtmlBasedComponent)commentHeader, "contextAreaPageCommentHeaderClosed", false);
        commentHeader.setWidths("5%, 5%, 90%");
    }


    protected void markGroupBoxSelected(AdvancedGroupbox groupbox, AbstractCommentModel commentItemModel)
    {
        boolean add = this.controller.isCommentCurrentlySelected(getModel(), commentItemModel);
        UITools.modifySClass((HtmlBasedComponent)groupbox, "activeComment", add);
    }


    public ContextAreaCommentTreeModel getModel()
    {
        return super.getModel();
    }


    protected void renderLabelDiv(Hbox commentHeader, AbstractCommentModel commentItemModel)
    {
        Hbox labelBox = new Hbox();
        String author = UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabelForTypedObject(
                        UISessionUtils.getCurrentSession().getTypeService().wrapItem(commentItemModel.getAuthor()));
        Label commentAuthorLabel = new Label();
        commentAuthorLabel.setSclass("commentAuthorLabelContextArea");
        commentAuthorLabel.setValue(author);
        labelBox.appendChild((Component)commentAuthorLabel);
        Object modifiedDate = TypeTools.getPropertyValue(getValueService(), getTypeService().wrapItem(commentItemModel),
                        getTypeService().getPropertyDescriptor("Item.modifiedtime"));
        String dateString = "";
        if(ValueHandler.NOT_READABLE_VALUE.equals(modifiedDate))
        {
            dateString = modifiedDate.toString();
        }
        else
        {
            dateString = DateFormat.getDateInstance(3, UISessionUtils.getCurrentSession().getLocale()).format((Date)modifiedDate);
        }
        Label commentDateLabel = new Label();
        commentDateLabel.setSclass("commentDateLabelContextArea");
        commentDateLabel.setValue(dateString);
        labelBox.appendChild((Component)commentDateLabel);
        String subject = TypeTools.getPropertyValueAsString(getValueService(), getTypeService().wrapItem(commentItemModel),
                        getTypeService().getPropertyDescriptor("AbstractComment.subject"));
        Label commentSubjectLabel = new Label();
        if(subject != null)
        {
            commentSubjectLabel.setValue(subject);
        }
        commentSubjectLabel.setSclass("commentSubjectLabelContextArea");
        labelBox.appendChild((Component)commentSubjectLabel);
        String text = TypeTools.getPropertyValueAsString(getValueService(), getTypeService().wrapItem(commentItemModel),
                        getTypeService().getPropertyDescriptor("AbstractComment.text"));
        Label textLabel = new Label();
        textLabel.setSclass("commentTextLabel");
        if(!StringUtils.isEmpty(text))
        {
            textLabel.setValue(UITools.removeHtml(text));
        }
        labelBox.appendChild((Component)textLabel);
        Popup popup = getTooltipRenderer().renderItemTooltip((ItemModel)commentItemModel);
        labelBox.setTooltip(popup);
        labelBox.appendChild((Component)popup);
        commentHeader.appendChild((Component)labelBox);
    }


    protected void addEventListeners(AdvancedGroupbox groupbox, AbstractCommentModel abstractComment)
    {
        if(getModel().getCommentContextAwareModel().isCommentLayerActive())
        {
            if(abstractComment instanceof CommentModel)
            {
                groupbox.addEventListener("onClick", (EventListener)new Object(this, abstractComment));
            }
        }
    }


    protected AdvancedGroupbox renderSingleReply(ReplyModel replyModel, Div parent)
    {
        Div groupboxContentDiv = new Div();
        groupboxContentDiv.setSclass("replyBoxContent");
        parent.setAttribute("groupboxContentDiv", groupboxContentDiv);
        AdvancedGroupbox groupbox = renderGroupbox((AbstractCommentModel)replyModel, groupboxContentDiv);
        UITools.applyTestID((Component)groupbox, "reply_");
        parent.appendChild((Component)groupbox);
        groupbox.appendChild((Component)groupboxContentDiv);
        groupboxContentDiv.setHeight("100%");
        this.groupboxList.put(UISessionUtils.getCurrentSession().getTypeService().wrapItem(replyModel), parent);
        Div replyDiv = new Div();
        groupboxContentDiv.appendChild((Component)replyDiv);
        groupboxContentDiv.setAttribute("replyDiv", replyDiv);
        return groupbox;
    }


    public CommentLayerService getCommentLayerService()
    {
        if(this.commentLayerService == null)
        {
            this.commentLayerService = (CommentLayerService)SpringUtil.getBean("commentLayerService");
        }
        return this.commentLayerService;
    }


    public void openRecord(AbstractCommentModel abstractComment)
    {
        if(getGroupboxList() != null)
        {
            for(TypedObject key : getGroupboxList().keySet())
            {
                AbstractCommentModel keyAbstractComment = (AbstractCommentModel)key.getObject();
                if(abstractComment.equals(keyAbstractComment))
                {
                    Div groupbox = (Div)getGroupboxList().get(key);
                    for(Object cmp : groupbox.getChildren())
                    {
                        if(cmp instanceof AdvancedGroupbox)
                        {
                            AdvancedGroupbox grpBox = (AdvancedGroupbox)cmp;
                            Events.echoEvent("onOpen", (Component)grpBox, grpBox.isOpen() ? "close" : "open");
                        }
                    }
                    break;
                }
            }
        }
    }


    public ActionColumnConfiguration getCommentActionConfiguration()
    {
        String actionSpringBeanID = "CommentLayerActionColumnsCommunicationBrowser";
        return (ActionColumnConfiguration)SpringUtil.getBean("CommentLayerActionColumnsCommunicationBrowser");
    }


    protected AdvancedGroupbox renderSingleComment(CommentModel commentModel, Div parent)
    {
        UITools.applyTestID((Component)parent, "CommentLayerContextTableRecord_" + commentModel.getCode() + "_");
        return super.renderSingleComment(commentModel, parent);
    }


    protected void extendContextMap(Map<String, CommentLayerAwareModel> contextMap)
    {
        super.extendContextMap(contextMap);
        contextMap.put("commentLayerAwareModel", getModel().getCommentContextAwareModel());
    }


    protected AdvancedGroupbox renderGroupbox(AbstractCommentModel commentItemModel, Div contentContainer)
    {
        AdvancedGroupbox grpBox = super.renderGroupbox(commentItemModel, contentContainer);
        grpBox.setHandleVisibility(false);
        grpBox.setClosable(this.controller.isCommentRecordCollapsable(commentItemModel));
        return grpBox;
    }


    protected TooltipRenderer getTooltipRenderer()
    {
        if(this.tooltipRenderer == null)
        {
            this.tooltipRenderer = (TooltipRenderer)SpringUtil.getBean("commentTooltipRenderer");
        }
        return this.tooltipRenderer;
    }


    public boolean update()
    {
        boolean update = super.update();
        cleanRemovedComments();
        return update;
    }


    protected void cleanRemovedComments()
    {
        Set<TypedObject> keysToREmove = new HashSet<>();
        for(TypedObject typedObject : getGroupboxList().keySet())
        {
            if(typedObject.getObject() == null)
            {
                keysToREmove.add(typedObject);
            }
        }
        for(TypedObject keyToRemove : keysToREmove)
        {
            getGroupboxList().remove(keyToRemove);
        }
    }
}
