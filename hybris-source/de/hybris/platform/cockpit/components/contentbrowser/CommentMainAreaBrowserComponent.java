package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.components.AdvancedGroupbox;
import de.hybris.platform.cockpit.components.listview.ActionColumnConfiguration;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.components.listview.impl.AbstractCommentAction;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.general.UIItemView;
import de.hybris.platform.cockpit.model.listview.ValueHandler;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.CockpitCommentService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ValueService;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.CommunicationBrowserModel;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.comments.constants.GeneratedCommentsConstants;
import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.comments.model.CommentAttachmentModel;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.ReplyModel;
import de.hybris.platform.core.model.media.MediaModel;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Span;

public class CommentMainAreaBrowserComponent extends AbstractMainAreaBrowserComponent
{
    protected Div mainDiv = null;
    protected final Map<TypedObject, Div> groupboxList = new HashMap<>();
    protected final String IMG_USER_DUMMY_SMALL = "/cockpit/images/user_dummy_klein.jpg";
    private final String IMG_USER_DUMMY_TINY = "/cockpit/images/user_dummy_tiny.jpg";
    private final String IMG_SHOW_ITEM = "/cockpit/images/icon_showitem.png";
    protected final String IMG_ATTACHMENT = "/cockpit/images/icon_attachement.png";
    private final String IMG_REMOVE = "/cockpit/images/remove.png";
    private CockpitCommentService cockpitCommentService;
    private ValueService valueService;
    private TypeService typeService;


    public CommentMainAreaBrowserComponent(AdvancedBrowserModel model, AbstractContentBrowser contentBrowser)
    {
        super(model, contentBrowser);
        setSclass("commentSectionComponent");
        setDroppable("false");
    }


    protected Div renderView()
    {
        Div div = new Div();
        div.setSclass("commentDiv");
        Set<CommentModel> resultSet = new LinkedHashSet<>();
        for(TypedObject comment : getModel().getItems())
        {
            Object object = comment.getObject();
            if(object instanceof CommentModel)
            {
                resultSet.add((CommentModel)object);
                continue;
            }
            if(object instanceof ReplyModel)
            {
                CommentModel comm = ((ReplyModel)object).getComment();
                if(comm != null)
                {
                    resultSet.add(comm);
                }
            }
        }
        for(CommentModel commentModel : resultSet)
        {
            Div groupboxDiv = new Div();
            div.appendChild((Component)groupboxDiv);
            AdvancedGroupbox groupbox = renderSingleComment(commentModel, groupboxDiv);
            Div groupboxContentDiv = (Div)groupboxDiv.getAttribute("groupboxContentDiv");
            Div replyDiv = (Div)groupboxContentDiv.getAttribute("replyDiv");
            if(groupbox.isOpen())
            {
                replyDiv.appendChild((Component)renderReplies((AbstractCommentModel)commentModel, replyDiv));
            }
            groupbox.addEventListener("onOpen", (EventListener)new Object(this, replyDiv, commentModel));
        }
        return div;
    }


    protected void markAsRead(TypedObject commentItem)
    {
        if(!getCockpitCommentService().isRead(commentItem))
        {
            getCockpitCommentService().setRead(commentItem, true);
            UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(this, commentItem, Collections.EMPTY_LIST));
        }
    }


    protected AdvancedGroupbox renderSingleComment(CommentModel commentModel, Div parent)
    {
        Div groupboxContentDiv = new Div();
        groupboxContentDiv.setSclass("commentBoxContent");
        parent.setAttribute("groupboxContentDiv", groupboxContentDiv);
        AdvancedGroupbox groupbox = renderGroupbox((AbstractCommentModel)commentModel, groupboxContentDiv);
        this.groupboxList.put(UISessionUtils.getCurrentSession().getTypeService().wrapItem(commentModel), parent);
        parent.appendChild((Component)groupbox);
        groupbox.appendChild((Component)groupboxContentDiv);
        groupboxContentDiv.setHeight("100%");
        renderCommentDetailsBox(groupboxContentDiv, commentModel);
        renderCommentTextBox(groupboxContentDiv, commentModel);
        Div replyDiv = new Div();
        groupboxContentDiv.appendChild((Component)replyDiv);
        groupboxContentDiv.setAttribute("replyDiv", replyDiv);
        return groupbox;
    }


    protected Div renderAttachmentDiv(AbstractCommentModel commentModel)
    {
        Div detailsAttachmentDiv = new Div();
        detailsAttachmentDiv.setSclass("detailsAttachmentDiv");
        detailsAttachmentDiv.appendChild((Component)new Label(Labels.getLabel("communicationcenter.section.comment.attachment") + ": "));
        boolean firstAttachment = true;
        for(CommentAttachmentModel attachment : commentModel.getAttachments())
        {
            if(!firstAttachment)
            {
                detailsAttachmentDiv.appendChild((Component)createSeparator());
            }
            else
            {
                firstAttachment = false;
            }
            Image attachmentDeleteImage = new Image("/cockpit/images/remove.png");
            attachmentDeleteImage.addEventListener("onClick", (EventListener)new Object(this, attachment, commentModel));
            detailsAttachmentDiv.appendChild((Component)new Label(UISessionUtils.getCurrentSession().getLabelService()
                            .getObjectTextLabelForTypedObject(UISessionUtils.getCurrentSession().getTypeService().wrapItem(attachment))));
            detailsAttachmentDiv.appendChild((Component)attachmentDeleteImage);
        }
        if(commentModel.getAttachments().isEmpty())
        {
            detailsAttachmentDiv.appendChild((Component)new Label(Labels.getLabel("communicationcenter.section.comment.noattachments")));
            UITools.modifySClass((HtmlBasedComponent)detailsAttachmentDiv, "noAttachments", true);
        }
        return detailsAttachmentDiv;
    }


    protected AdvancedGroupbox renderGroupbox(AbstractCommentModel commentItemModel, Div contentContainer)
    {
        AdvancedGroupbox groupbox = new AdvancedGroupbox();
        groupbox.setArrowLeft(true);
        groupbox.setSclass("commentBox");
        groupbox.setOpen(false);
        if(!getCockpitCommentService().isRead(UISessionUtils.getCurrentSession().getTypeService().wrapItem(commentItemModel)))
        {
            UITools.modifySClass((HtmlBasedComponent)groupbox, "notRead", true);
        }
        markGroupBoxSelected(groupbox, commentItemModel);
        Div captionContainer = groupbox.getCaptionContainer();
        captionContainer.setSclass("caption");
        Hbox commentHeader = new Hbox();
        commentHeader.setWidth("100%");
        commentHeader.setWidths(",100%");
        captionContainer.appendChild((Component)commentHeader);
        CommunicationBrowserModel browserModel = (getModel() instanceof CommunicationBrowserModel) ? (CommunicationBrowserModel)getModel() : null;
        if(browserModel != null && browserModel.getOpenComments().contains(commentItemModel.getPk()))
        {
            groupbox.setOpen(true);
        }
        if(groupbox.isOpen())
        {
            captionContainer.setSclass("caption open");
            markAsRead(UISessionUtils.getCurrentSession().getTypeService().wrapItem(commentItemModel));
            renderGroupboxHeaderOpen(commentItemModel, commentHeader);
        }
        else
        {
            captionContainer.setSclass("caption closed");
            renderGroupboxHeaderClosed(commentItemModel, commentHeader);
        }
        groupbox.addEventListener("onOpen", (EventListener)new Object(this, commentItemModel, browserModel, captionContainer, commentHeader));
        addEventListeners(groupbox, commentItemModel);
        Div actionDiv = new Div();
        actionDiv.setSclass("commentActions");
        actionDiv.setAction("onclick: return true;");
        Div answerDiv = new Div();
        contentContainer.appendChild((Component)answerDiv);
        renderActions(commentItemModel, actionDiv, answerDiv, groupbox);
        captionContainer.appendChild((Component)actionDiv);
        return groupbox;
    }


    private void renderActions(AbstractCommentModel item, Div parent, Div contentContainer, AdvancedGroupbox groupbox)
    {
        if(parent != null && item != null)
        {
            TypedObject typedObj = UISessionUtils.getCurrentSession().getTypeService().wrapItem(item);
            parent.getChildren().clear();
            ActionColumnConfiguration actionConfiguration = getCommentActionConfiguration();
            if(actionConfiguration == null)
            {
                return;
            }
            List<ListViewAction> listViewActions = actionConfiguration.getActions();
            boolean firstIter = true;
            for(ListViewAction listViewAction : listViewActions)
            {
                ListViewAction.Context context = listViewAction.createContext(null, typedObj);
                Map<String, String> contextMap = context.getMap();
                contextMap.put("browserArea", "contentArea");
                contextMap.put("parent", contentContainer);
                contextMap.put(AbstractCommentAction.UPDATELISTENER, new Object(this));
                extendContextMap(contextMap);
                String imgURI = listViewAction.getImageURI(context);
                if(imgURI != null && imgURI.length() > 0)
                {
                    if(firstIter)
                    {
                        firstIter = false;
                    }
                    else
                    {
                        parent.appendChild((Component)createSeparator());
                    }
                    Image actionImg = new Image(imgURI);
                    actionImg.setStyle("display: inline; cursor: pointer;");
                    EventListener listener = listViewAction.getEventListener(context);
                    if(listener != null)
                    {
                        actionImg.addEventListener("onClick", (EventListener)new Object(this, groupbox, listener));
                    }
                    if(listViewAction.getTooltip(context) != null && listViewAction.getTooltip(context).length() > 0)
                    {
                        actionImg.setTooltiptext(listViewAction.getTooltip(context));
                    }
                    Menupopup popup = listViewAction.getPopup(context);
                    if(popup != null)
                    {
                        parent.appendChild((Component)popup);
                        actionImg.setPopup((Popup)popup);
                    }
                    parent.appendChild((Component)actionImg);
                    Menupopup contextPopup = listViewAction.getContextPopup(context);
                    if(contextPopup != null)
                    {
                        parent.appendChild((Component)contextPopup);
                        actionImg.setContext((Popup)contextPopup);
                    }
                }
            }
        }
    }


    protected void extendContextMap(Map contextMap)
    {
    }


    protected void renderGroupboxHeaderClosed(AbstractCommentModel commentItemModel, Hbox commentHeader)
    {
        commentHeader.getChildren().clear();
        renderStatusSection(commentHeader, commentItemModel);
        Object profilePicture = TypeTools.getPropertyValue(getValueService(),
                        getTypeService().wrapItem(commentItemModel.getAuthor()),
                        getTypeService().getPropertyDescriptor("Principal.profilePicture"));
        String profilePictureUrl = "/cockpit/images/user_dummy_tiny.jpg";
        if(profilePicture != null && !ValueHandler.NOT_READABLE_VALUE.equals(profilePicture))
        {
            profilePictureUrl = UITools.getAdjustedUrl(((MediaModel)((TypedObject)profilePicture).getObject()).getURL());
        }
        Image userImage = new Image(profilePictureUrl);
        userImage.setHeight("20px");
        String author = UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabelForTypedObject(
                        UISessionUtils.getCurrentSession().getTypeService().wrapItem(commentItemModel.getAuthor()));
        userImage.setTooltiptext(author);
        userImage.setSclass("userimage");
        commentHeader.appendChild((Component)userImage);
        renderLabelDiv(commentHeader, commentItemModel);
    }


    protected void renderGroupboxHeaderOpen(AbstractCommentModel commentItemModel, Hbox commentHeader)
    {
        commentHeader.getChildren().clear();
        Object profilePicture = TypeTools.getPropertyValue(getValueService(),
                        getTypeService().wrapItem(commentItemModel.getAuthor()),
                        getTypeService().getPropertyDescriptor("Principal.profilePicture"));
        String profilePictureUrl = "/cockpit/images/user_dummy_klein.jpg";
        if(profilePicture != null && !ValueHandler.NOT_READABLE_VALUE.equals(profilePicture))
        {
            profilePictureUrl = UITools.getAdjustedUrl(((MediaModel)((TypedObject)profilePicture).getObject()).getURL());
        }
        Image userImage = new Image(profilePictureUrl);
        userImage.setHeight("50px");
        String author = UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabelForTypedObject(
                        UISessionUtils.getCurrentSession().getTypeService().wrapItem(commentItemModel.getAuthor()));
        userImage.setTooltiptext(author);
        userImage.setSclass("userimage");
        commentHeader.appendChild((Component)userImage);
        Div lineDiv = new Div();
        commentHeader.appendChild((Component)lineDiv);
        Div firstLine = new Div();
        StringBuilder firstLinelabel = new StringBuilder(author);
        if(firstLinelabel.length() > 0)
        {
            firstLinelabel.append(", ");
        }
        Object modifiedDate = TypeTools.getPropertyValue(getValueService(), getTypeService().wrapItem(commentItemModel),
                        getTypeService().getPropertyDescriptor("Item.modifiedtime"));
        if(ValueHandler.NOT_READABLE_VALUE.equals(modifiedDate))
        {
            firstLinelabel.append(modifiedDate.toString());
        }
        else
        {
            firstLinelabel.append(DateFormat.getDateInstance(1, UISessionUtils.getCurrentSession().getLocale())
                            .format((Date)modifiedDate));
        }
        Label label = new Label(firstLinelabel.toString());
        label.setSclass("commentUserAndDate");
        firstLine.appendChild((Component)label);
        lineDiv.appendChild((Component)firstLine);
        Div secondLine = new Div();
        Label secondLinelabel = new Label();
        secondLinelabel.setSclass("commentDisplayLabel");
        String subject = TypeTools.getPropertyValueAsString(getValueService(), getTypeService().wrapItem(commentItemModel),
                        getTypeService().getPropertyDescriptor(GeneratedCommentsConstants.TC.ABSTRACTCOMMENT + ".subject"));
        if(subject != null)
        {
            secondLinelabel.setValue(subject);
        }
        secondLine.appendChild((Component)secondLinelabel);
        lineDiv.appendChild((Component)secondLine);
        renderGroupboxHeaderIcons(commentItemModel, (HtmlBasedComponent)firstLine);
    }


    protected void renderGroupboxHeaderIcons(AbstractCommentModel commentItemModel, HtmlBasedComponent parent)
    {
        Span buttonContainer = new Span();
        buttonContainer.setAction("onclick: return false;");
        parent.appendChild((Component)buttonContainer);
        if(!commentItemModel.getAttachments().isEmpty())
        {
            Image attachmentImage = new Image("/cockpit/images/icon_attachement.png");
            attachmentImage.setTooltiptext(Labels.getLabel("comment.attachment"));
            attachmentImage.setSclass("header");
            buttonContainer.appendChild((Component)attachmentImage);
            Object object = new Object(this, commentItemModel);
            attachmentImage.addEventListener("onClick", (EventListener)object);
        }
        createViewItemImage(commentItemModel);
        if(commentItemModel instanceof CommentModel)
        {
            Image showItemImage = new Image("/cockpit/images/icon_showitem.png");
            showItemImage.setTooltiptext(Labels.getLabel("communicationcenter.section.comment.showitems"));
            showItemImage.setSclass("header");
            buttonContainer.appendChild((Component)showItemImage);
            Object object = new Object(this, commentItemModel);
            showItemImage.addEventListener("onClick", (EventListener)object);
        }
    }


    protected void createViewItemImage(AbstractCommentModel commentItemModel)
    {
    }


    protected Div renderReplies(AbstractCommentModel commentItemModel, Div parent)
    {
        Div div = new Div();
        div.setSclass("replyDiv");
        List<ReplyModel> replies = new ArrayList<>();
        if(commentItemModel instanceof CommentModel)
        {
            replies.addAll(getCockpitCommentService().getDirectReplies((CommentModel)commentItemModel, 0, -1));
        }
        else if(commentItemModel instanceof ReplyModel)
        {
            replies.addAll(((ReplyModel)commentItemModel).getReplies());
        }
        if(!replies.isEmpty())
        {
            parent.getChildren().clear();
            for(ReplyModel reply : replies)
            {
                Div groupboxDiv = new Div();
                div.appendChild((Component)groupboxDiv);
                Div spacerDiv = new Div();
                spacerDiv.setSclass("commentSpacer");
                groupboxDiv.appendChild((Component)spacerDiv);
                AdvancedGroupbox groupbox = renderSingleReply(reply, groupboxDiv);
                Div groupboxContentDiv = (Div)groupboxDiv.getAttribute("groupboxContentDiv");
                Div replyDiv = (Div)groupboxContentDiv.getAttribute("replyDiv");
                if(groupbox.isOpen())
                {
                    replyDiv.appendChild((Component)renderReplies((AbstractCommentModel)reply, replyDiv));
                }
                groupbox.addEventListener("onOpen", (EventListener)new Object(this, reply, replyDiv));
            }
        }
        return div;
    }


    protected AdvancedGroupbox renderSingleReply(ReplyModel replyModel, Div parent)
    {
        Div groupboxContentDiv = new Div();
        groupboxContentDiv.setSclass("replyBoxContent");
        parent.setAttribute("groupboxContentDiv", groupboxContentDiv);
        AdvancedGroupbox groupbox = renderGroupbox((AbstractCommentModel)replyModel, groupboxContentDiv);
        parent.appendChild((Component)groupbox);
        groupbox.appendChild((Component)groupboxContentDiv);
        groupboxContentDiv.setHeight("100%");
        this.groupboxList.put(UISessionUtils.getCurrentSession().getTypeService().wrapItem(replyModel), parent);
        Div textDiv = new Div();
        textDiv.setSclass("commentTextCnt");
        textDiv.appendChild((Component)new Label(TypeTools.getPropertyValueAsString(getValueService(), getTypeService().wrapItem(replyModel),
                        getTypeService().getPropertyDescriptor(GeneratedCommentsConstants.TC.ABSTRACTCOMMENT + ".text"))));
        groupboxContentDiv.appendChild((Component)textDiv);
        groupboxContentDiv.appendChild((Component)renderAttachmentDiv((AbstractCommentModel)replyModel));
        Div replyDiv = new Div();
        groupboxContentDiv.appendChild((Component)replyDiv);
        groupboxContentDiv.setAttribute("replyDiv", replyDiv);
        return groupbox;
    }


    protected Separator createSeparator()
    {
        Separator sep = new Separator();
        sep.setBar(true);
        sep.setOrient("vertical");
        sep.setSclass("action_spacer");
        return sep;
    }


    public ActionColumnConfiguration getCommentActionConfiguration()
    {
        String actionSpringBeanID = "CommentActionColumnsCommunicationBrowser";
        return (ActionColumnConfiguration)SpringUtil.getBean("CommentActionColumnsCommunicationBrowser");
    }


    protected void cleanup()
    {
    }


    protected Div createMainArea()
    {
        if(getModel() != null)
        {
            setWidth("100%");
            getChildren().clear();
            this.mainDiv = renderView();
            if(this.mainDiv == null)
            {
                throw new IllegalStateException("Main div was not created successfully.");
            }
            return this.mainDiv;
        }
        return null;
    }


    protected UIItemView getCurrentItemView()
    {
        return null;
    }


    public void setActiveItem(TypedObject activeItem)
    {
    }


    public boolean update()
    {
        this.mainDiv.getChildren().clear();
        removeChild((Component)this.mainDiv);
        this.mainDiv = renderView();
        if(this.mainDiv == null)
        {
            throw new IllegalStateException("Section grid was not created successfully.");
        }
        appendChild((Component)this.mainDiv);
        return true;
    }


    public void updateActiveItems()
    {
    }


    public void updateItem(TypedObject item, Set<PropertyDescriptor> modifiedProperties)
    {
        Div groupboxItemDiv = this.groupboxList.get(item);
        if(groupboxItemDiv != null && item != null)
        {
            Object object = item.getObject();
            if(object != null)
            {
                Div groupboxContentDiv = (Div)groupboxItemDiv.getAttribute("groupboxContentDiv");
                Div replyDiv = (Div)groupboxContentDiv.getAttribute("replyDiv");
                groupboxItemDiv.getChildren().clear();
                if(object instanceof CommentModel)
                {
                    renderSingleComment((CommentModel)object, groupboxItemDiv);
                }
                else if(object instanceof ReplyModel)
                {
                    renderSingleReply((ReplyModel)object, groupboxItemDiv);
                }
                groupboxContentDiv = (Div)groupboxItemDiv.getAttribute("groupboxContentDiv");
                Div newReplyDiv = (Div)groupboxContentDiv.getAttribute("replyDiv");
                for(Object child : new ArrayList(replyDiv.getChildren()))
                {
                    if(child instanceof HtmlBasedComponent)
                    {
                        newReplyDiv.appendChild((Component)child);
                    }
                }
            }
        }
    }


    public void updateSelectedItems()
    {
    }


    public CockpitCommentService getCockpitCommentService()
    {
        if(this.cockpitCommentService == null)
        {
            this.cockpitCommentService = (CockpitCommentService)SpringUtil.getBean("cockpitCommentService");
        }
        return this.cockpitCommentService;
    }


    public ValueService getValueService()
    {
        if(this.valueService == null)
        {
            this.valueService = (ValueService)SpringUtil.getBean("valueService");
        }
        return this.valueService;
    }


    public TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }


    protected void renderCommentDetailsBox(Div groupboxContentDiv, CommentModel comment)
    {
        AdvancedGroupbox commentDetailsGroupbox = new AdvancedGroupbox();
        commentDetailsGroupbox.setSclass("commentDetailsBox");
        commentDetailsGroupbox.setOpen(false);
        commentDetailsGroupbox.setLabel(Labels.getLabel("communicationcenter.section.comment.commentdetails"));
        Div detailsContainer = renderDetailsContainer(comment);
        commentDetailsGroupbox.appendChild((Component)detailsContainer);
        groupboxContentDiv.appendChild((Component)commentDetailsGroupbox);
    }


    protected Div renderDetailsContainer(CommentModel commentModel)
    {
        Div detailsContainer = new Div();
        detailsContainer.setSclass("commentDetailsBoxContent");
        Div detailsBasicDiv = new Div();
        detailsBasicDiv.appendChild((Component)new Label(Labels.getLabel("communicationcenter.section.comment.type") + ": " + Labels.getLabel("communicationcenter.section.comment.type")));
        detailsContainer.appendChild((Component)detailsBasicDiv);
        Div detailsAssignDiv = new Div();
        detailsAssignDiv.appendChild((Component)new Label(""));
        detailsContainer.appendChild((Component)detailsAssignDiv);
        detailsContainer.appendChild((Component)renderAttachmentDiv((AbstractCommentModel)commentModel));
        return detailsContainer;
    }


    protected void renderCommentTextBox(Div groupboxContentDiv, CommentModel commentModel)
    {
        Div textDiv = new Div();
        textDiv.setSclass("commentTextCnt");
        textDiv.appendChild((Component)new Label(TypeTools.getPropertyValueAsString(getValueService(), getTypeService().wrapItem(commentModel),
                        getTypeService().getPropertyDescriptor(GeneratedCommentsConstants.TC.ABSTRACTCOMMENT + ".text"))));
        groupboxContentDiv.appendChild((Component)textDiv);
    }


    protected void renderStatusSection(Hbox commentHeader, AbstractCommentModel commentModel)
    {
    }


    protected void markGroupBoxSelected(AdvancedGroupbox groupbox, AbstractCommentModel commentItemModel)
    {
    }


    protected void renderLabelDiv(Hbox commentHeader, AbstractCommentModel commentItemModel)
    {
        String author = UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabelForTypedObject(
                        UISessionUtils.getCurrentSession().getTypeService().wrapItem(commentItemModel.getAuthor()));
        Div labelDiv = new Div();
        StringBuilder label = new StringBuilder(author);
        if(label.length() > 0)
        {
            label.append(", ");
        }
        Object modifiedDate = TypeTools.getPropertyValue(getValueService(), getTypeService().wrapItem(commentItemModel),
                        getTypeService().getPropertyDescriptor("Item.modifiedtime"));
        if(ValueHandler.NOT_READABLE_VALUE.equals(modifiedDate))
        {
            label.append(modifiedDate.toString());
        }
        else
        {
            label.append(DateFormat.getDateInstance(3, UISessionUtils.getCurrentSession().getLocale()).format((Date)modifiedDate));
        }
        String subject = TypeTools.getPropertyValueAsString(getValueService(), getTypeService().wrapItem(commentItemModel),
                        getTypeService().getPropertyDescriptor(GeneratedCommentsConstants.TC.ABSTRACTCOMMENT + ".subject"));
        Label subjectLabel = new Label();
        if(subject != null)
        {
            subjectLabel.setValue(subject);
        }
        subjectLabel.setSclass("commentDisplayLabel");
        Label userDateLabel = new Label(label.toString());
        userDateLabel.setSclass("commentUserAndDate");
        labelDiv.appendChild((Component)userDateLabel);
        commentHeader.appendChild((Component)labelDiv);
        renderGroupboxHeaderIcons(commentItemModel, (HtmlBasedComponent)labelDiv);
        labelDiv.appendChild((Component)subjectLabel);
    }


    protected void addEventListeners(AdvancedGroupbox groupbox, AbstractCommentModel commentItemModel)
    {
    }


    protected Map<TypedObject, Div> getGroupboxList()
    {
        return this.groupboxList;
    }
}
