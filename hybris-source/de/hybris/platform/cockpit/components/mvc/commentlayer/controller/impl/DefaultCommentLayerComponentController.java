package de.hybris.platform.cockpit.components.mvc.commentlayer.controller.impl;

import de.hybris.platform.cockpit.components.CockpitBasicFCKEditor;
import de.hybris.platform.cockpit.components.CockpitFCKEditor;
import de.hybris.platform.cockpit.components.contentbrowser.comments.CommentIcon;
import de.hybris.platform.cockpit.components.mvc.commentlayer.CommentLayerComponent;
import de.hybris.platform.cockpit.components.mvc.commentlayer.CommentLayerContextComponent;
import de.hybris.platform.cockpit.components.mvc.commentlayer.CommentLayerUtils;
import de.hybris.platform.cockpit.components.mvc.commentlayer.DivCommentIcon;
import de.hybris.platform.cockpit.components.mvc.commentlayer.controller.CommentLayerComponentController;
import de.hybris.platform.cockpit.components.mvc.commentlayer.model.CommentIconModel;
import de.hybris.platform.cockpit.components.mvc.commentlayer.model.CommentLayerAwareModel;
import de.hybris.platform.cockpit.components.mvc.commentlayer.model.CommentLayerComponentModel;
import de.hybris.platform.cockpit.components.mvc.commentlayer.model.CommentLayerContext;
import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.components.sectionpanel.TooltipRenderer;
import de.hybris.platform.cockpit.helpers.ModelHelper;
import de.hybris.platform.cockpit.model.CommentMetadataModel;
import de.hybris.platform.cockpit.model.editor.EditorHelper;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.search.impl.ResultObjectWrapper;
import de.hybris.platform.cockpit.services.comments.CommentLayerService;
import de.hybris.platform.cockpit.services.comments.modes.CommentModeExecutor;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.security.UIAccessRightService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.comments.model.CommentAttachmentModel;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.CommentUserSettingModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.comments.model.DomainModel;
import de.hybris.platform.comments.services.CommentService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Br;
import org.zkoss.zhtml.Div;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Box;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Window;

public class DefaultCommentLayerComponentController implements CommentLayerComponentController<CommentLayerComponent>
{
    protected static final String NORMAL_BORDER = "normal";
    protected static final Logger LOG = LoggerFactory.getLogger(DefaultCommentLayerComponentController.class);
    protected static final String COMMENT_LAYER_ARG_NAME = "commentLayerComponent";
    protected static final String COMMENTLAYER_INDEX = "commentlayerpopup_index";
    protected DomainModel domain;
    protected ComponentModel component;
    protected CommentTypeModel commentType;
    protected CommentService commentService;
    protected ModelService modelService;
    protected ModelHelper cockpitModelHelper;
    protected TypeService cockpitTypeService;
    protected CommentLayerService commentLayerService;
    protected UIAccessRightService uiAccessRightService;
    protected TooltipRenderer commentTooltipRenderer;


    public void drawCommentLayerOverArea(HtmlBasedComponent parent, CommentLayerComponent commentLayerComponent)
    {
        validateCommentLayer(commentLayerComponent);
        ServicesUtil.validateParameterNotNullStandardMessage("parent", parent);
        commentLayerComponent.setSclass("maincanvascontainer");
        CommentLayerComponentModel model = commentLayerComponent.getModel();
        double[] expectedWidthAndHeight = getDimensionsFromParent(parent);
        model.setWidth(expectedWidthAndHeight[0]);
        model.setHeight(expectedWidthAndHeight[1]);
        addExistingComments(commentLayerComponent);
        commentLayerComponent.refresh();
        parent.appendChild((Component)commentLayerComponent);
    }


    public Menupopup createContextMenu(CommentLayerComponent component)
    {
        validateCommentLayer(component);
        Menupopup ret = new Menupopup();
        Menuitem createCommentMenuItem = new Menuitem(Labels.getLabel("page.structure.toolbar.comments.action.createComment"));
        createCommentMenuItem.addEventListener("onClick", event -> {
            int currentX = component.getModel().getCurrentRightClickX();
            int currentY = component.getModel().getCurrentRightClickY();
            changeCommentLayerMode(component, "editComment");
            addCommentIcon(component, currentX, currentY);
            ret.detach();
            component.removeChild((Component)ret);
        });
        UITools.applyTestID((Component)createCommentMenuItem, "createComment");
        createCommentMenuItem.setParent((Component)ret);
        createCommentMenuItem.setDisabled(!canCreateComment(component.getModel().getCommentTarget()));
        return ret;
    }


    public boolean canCreateComment()
    {
        return this.commentLayerService.canUserCreateComment(getCurentUser());
    }


    protected boolean canCreateComment(TypedObject commentTarget)
    {
        ItemModel itemModel = (ItemModel)commentTarget.getObject();
        return (this.commentLayerService.canUserCreateComment(getCurentUser()) && this.uiAccessRightService.isWritable((ObjectType)commentTarget.getType(), commentTarget,
                        getTypeService().getPropertyDescriptor(itemModel.getItemtype() + ".comments"), false));
    }


    public Menupopup createIconContextMenu(CommentLayerComponent component, CommentIcon icon)
    {
        validateCommentLayer(component);
        ServicesUtil.validateParameterNotNullStandardMessage("icon", icon);
        Menupopup ret = new Menupopup();
        CommentModel comment = icon.getModel().getComment();
        CommentLayerAwareModel clAwareModel = component.getModel().getCommentLayerAwareModel();
        Menuitem replyCommentMenuItem = new Menuitem(Labels.getLabel("page.structure.toolbar.comments.action.replyComment"));
        replyCommentMenuItem.addEventListener("onClick", event -> {
            if(canReplyToComment(comment))
            {
                replyComment(component.getModel().getCommentLayerAwareModel(), (AbstractCommentModel)comment);
            }
            else
            {
                try
                {
                    Messagebox.show(Labels.getLabel("reply.denied.msg.tomanythreads"));
                }
                catch(InterruptedException e2)
                {
                    LOG.error("Could not show messagebox, reason: ", e2);
                }
            }
            ret.detach();
        });
        replyCommentMenuItem.setDisabled(!canReplyToComment(icon.getModel().getComment()));
        UITools.applyTestID((Component)replyCommentMenuItem, "replyComment");
        replyCommentMenuItem.setParent((Component)ret);
        Menuitem editCommentMenuItem = new Menuitem(Labels.getLabel("page.structure.toolbar.comments.action.editComment"));
        editCommentMenuItem.addEventListener("onClick", event -> {
            editCommentPopup(clAwareModel, (AbstractCommentModel)comment);
            ret.detach();
        });
        UITools.applyTestID((Component)editCommentMenuItem, "editComment");
        editCommentMenuItem.setDisabled(!canEditComment(icon.getModel().getComment()));
        editCommentMenuItem.setParent((Component)ret);
        Menuitem deleteCommentMenuItem = new Menuitem(Labels.getLabel("page.structure.toolbar.comments.action.deleteComment"));
        deleteCommentMenuItem.addEventListener("onClick", event -> {
            deleteCommentFromCommentLayer(component, icon);
            ret.detach();
        });
        UITools.applyTestID((Component)deleteCommentMenuItem, "deleteComment");
        deleteCommentMenuItem.setDisabled(!canDeleteComment(icon.getModel().getComment()));
        deleteCommentMenuItem.setParent((Component)ret);
        return ret;
    }


    protected void addExistingComments(CommentLayerComponent commentLayerComponent)
    {
        CommentLayerComponentModel clModel = commentLayerComponent.getModel();
        ItemModel commentTarget = (ItemModel)clModel.getCommentTarget().getObject();
        List<CommentModel> commentItems = this.commentLayerService.getCommentsForCommentLayer(getCurentUser(), commentTarget);
        TypedObject commentLayerTarget = clModel.getCommentLayerTarget();
        for(CommentModel commentItem : commentItems)
        {
            for(CommentMetadataModel metaData : commentItem.getCommentMetadata())
            {
                if(displayCommentIcon(commentLayerTarget, metaData, clModel.getPageIndex()))
                {
                    int scaledX = metaData.getX().intValue();
                    int scaledY = metaData.getY().intValue();
                    CommentIconModel model = new CommentIconModel(commentItem, scaledX, scaledY);
                    model.setParentComponentId(commentLayerComponent.getId());
                    CommentIcon icon = createNewIcon(model);
                    icon.refresh();
                    commentLayerComponent.addIconComponent(icon);
                    clModel.addIconModel(model);
                }
            }
        }
    }


    protected boolean displayCommentIcon(TypedObject commentLayerTarget, CommentMetadataModel metaData)
    {
        if(commentLayerTarget == null)
        {
            return true;
        }
        int targetPreviewIndex = getPagePreviewIndex((ItemModel)commentLayerTarget.getObject());
        if(metaData.getItem() == null)
        {
            return (targetPreviewIndex == 1);
        }
        int currentPreviewIndex = getPagePreviewIndex(metaData.getItem());
        return (currentPreviewIndex == targetPreviewIndex);
    }


    protected boolean displayCommentIcon(TypedObject commentLayerTarget, CommentMetadataModel metaData, int pageIndex)
    {
        if(commentLayerTarget == null)
        {
            if(metaData.getPageIndex() != null)
            {
                return metaData.getPageIndex().equals(Integer.valueOf(pageIndex));
            }
            return false;
        }
        int targetPreviewIndex = getPagePreviewIndex((ItemModel)commentLayerTarget.getObject());
        if(metaData.getItem() == null)
        {
            return (targetPreviewIndex == 1);
        }
        int currentPreviewIndex = getPagePreviewIndex(metaData.getItem());
        return (currentPreviewIndex == targetPreviewIndex);
    }


    protected int getPagePreviewIndex(ItemModel commentLayerTarget)
    {
        try
        {
            String code = (String)this.modelService.getAttributeValue(commentLayerTarget, "code");
            int start = code.lastIndexOf('(');
            int end = code.lastIndexOf(')');
            return Integer.parseInt(code.substring(start + 1, end));
        }
        catch(Exception e)
        {
            LOG.error("Could not parse preview index value. Returning fallback value 1", e);
            return 1;
        }
    }


    protected double[] getDimensionsFromParent(HtmlBasedComponent area)
    {
        double width = CommentLayerUtils.getSizeNumberValue(area.getWidth());
        double height = CommentLayerUtils.getSizeNumberValue(area.getHeight());
        return new double[] {width, height};
    }


    public void changeCommentLayerMode(CommentLayerComponent component, String mode)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("component", component);
        ServicesUtil.validateParameterNotNullStandardMessage("mode", mode);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Change clc mode to " + mode);
        }
        component.getModel().setMode(mode);
        CommentModeExecutor modeExecutor = this.commentLayerService.getModeExecutor(mode);
        modeExecutor.executeCommentAction(component);
        component.refresh();
    }


    public void changeCommentLayerToDefaultMode(CommentLayerComponent component)
    {
        changeCommentLayerMode(component, "selectComment");
    }


    public void addCommentIcon(CommentLayerComponent commentLayerComponent, int x_Position, int y_Position)
    {
        if(canCreateComment(commentLayerComponent.getModel().getCommentTarget()))
        {
            validateCommentLayer(commentLayerComponent);
            if(x_Position < 0)
            {
                throw new IllegalArgumentException("X position must be non-negative value");
            }
            if(y_Position < 0)
            {
                throw new IllegalArgumentException("Y position must be non-negative value");
            }
            String domainCode = UITools.getCockpitParameter("default.commentsection.domaincode", Executions.getCurrent());
            String componentCode = UITools.getCockpitParameter("default.commentsection.componentcode",
                            Executions.getCurrent());
            String commentTypeCode = UITools.getCockpitParameter("default.commentsection.commenttypecode",
                            Executions.getCurrent());
            if(domainCode != null && componentCode != null && commentTypeCode != null)
            {
                this.domain = this.commentService.getDomainForCode(domainCode);
                this.component = this.commentService.getComponentForCode(this.domain, componentCode);
                this.commentType = this.commentService.getCommentTypeForCode(this.component, commentTypeCode);
            }
            CommentLayerAwareModel clAwareModel = commentLayerComponent.getModel().getCommentLayerAwareModel();
            CommentModel comment = (CommentModel)this.modelService.create(CommentModel.class);
            comment.setCommentType(getCommentType());
            comment.setAuthor(getCurentUser());
            comment.setComponent(getCurrentComponent());
            CommentIconModel iconModel = new CommentIconModel(comment, x_Position, y_Position);
            iconModel.setParentComponentId(commentLayerComponent.getId());
            CommentIcon icon = createNewIcon(iconModel);
            commentLayerComponent.addIconComponent(icon);
            commentLayerComponent.getModel().addIconModel(iconModel);
            Window createCommentWindow = createEditPopupForComment(commentLayerComponent, clAwareModel, icon, null, true);
            clAwareModel.getParentAreaComponent().appendChild((Component)createCommentWindow);
        }
        else
        {
            Notification notification = new Notification(Labels.getLabel("no_permission"));
            BaseUICockpitPerspective basePerspective = (BaseUICockpitPerspective)UISessionUtils.getCurrentSession().getCurrentPerspective();
            if(basePerspective.getNotifier() != null)
            {
                basePerspective.getNotifier().setNotification(notification);
            }
        }
    }


    public void deleteCommentFromCommentLayer(CommentLayerComponent component, CommentIcon icon)
    {
        try
        {
            String currentMessage = Labels.getLabel("prompt.confirm_remove_comment");
            if(Messagebox.show(currentMessage, Labels.getLabel("dialog.confirmRemove.title"), 48, "z-msgbox z-msgbox-question") == 16)
            {
                validateCommentLayer(component);
                ServicesUtil.validateParameterNotNullStandardMessage("icon", icon);
                CommentModel comment = icon.getModel().getComment();
                if(comment == null)
                {
                    throw new IllegalStateException("Underlying comment instance is null");
                }
                List<CommentIcon> iconComponents = component.getIconComponents();
                if(iconComponents == null || !iconComponents.contains(icon))
                {
                    throw new IllegalStateException("Comment layer does not contain given comment icon");
                }
                if(this.commentLayerService.deleteComment(getCurentUser(), (AbstractCommentModel)comment))
                {
                    component.removeIcon(icon);
                    CommentLayerComponentModel clModel = component.getModel();
                    clModel.removeIcon(icon.getModel());
                    refreshContextList(clModel.getCommentLayerAwareModel(), true);
                }
            }
        }
        catch(InterruptedException e2)
        {
            LOG.error("Could not show messagebox, reason: " + e2.getMessage(), e2);
        }
    }


    public void selectCommentIcon(CommentLayerComponent commentLayerComponent, CommentIcon icon)
    {
        validateCommentLayer(commentLayerComponent);
        ServicesUtil.validateParameterNotNullStandardMessage("icon", icon);
        CommentLayerComponentModel clModel = commentLayerComponent.getModel();
        CommentIconModel selectedIconModel = icon.getModel();
        selectedIconModel.toggleSelected();
        CommentLayerContext pageCommentsContext = clModel.getCommentLayerAwareModel().getCommentLayerContext();
        pageCommentsContext.setSelectedCommentModel(selectedIconModel);
        for(CommentLayerComponent clComponent : pageCommentsContext.getCommentLayerComponents())
        {
            List<CommentIcon> icons = clComponent.getIconComponents();
            for(CommentIcon iconIter : icons)
            {
                CommentIconModel iModel = iconIter.getModel();
                if(!iModel.equals(selectedIconModel))
                {
                    iModel.setSelected(false);
                }
            }
            clComponent.refresh();
        }
        refreshContextList(clModel.getCommentLayerAwareModel(), false);
    }


    public CommentIcon getCommentIconForComment(CommentLayerComponent component, CommentModel comment)
    {
        validateCommentLayer(component);
        ServicesUtil.validateParameterNotNullStandardMessage("comment", comment);
        if(component.getIconComponents() != null)
        {
            for(CommentIcon icon : component.getIconComponents())
            {
                if(comment.equals(icon.getModel().getComment()))
                {
                    return icon;
                }
            }
        }
        return null;
    }


    protected UserModel getCurentUser()
    {
        return UISessionUtils.getCurrentSession().getUser();
    }


    protected DomainModel getDomain()
    {
        if(this.domain == null)
        {
            String domainCode = UITools.getCockpitParameter("default.commentsection.domaincode", Executions.getCurrent());
            this.domain = this.commentService.getDomainForCode(domainCode);
        }
        return this.domain;
    }


    protected ComponentModel getCurrentComponent()
    {
        if(this.component == null)
        {
            String componentCode = UITools.getCockpitParameter("default.commentsection.componentcode",
                            Executions.getCurrent());
            this.component = this.commentService.getComponentForCode(getDomain(), componentCode);
        }
        return this.component;
    }


    protected CommentTypeModel getCommentType()
    {
        if(this.commentType == null)
        {
            String commentTypeCode = UITools.getCockpitParameter("default.commentsection.commenttypecode",
                            Executions.getCurrent());
            this.commentType = this.commentService.getCommentTypeForCode(getCurrentComponent(), commentTypeCode);
        }
        return this.commentType;
    }


    protected void prepareCommentUpdate(Window newCommentPopup, AbstractCommentModel commentToSave, boolean create)
    {
    }


    protected void prepareReplyUpdate(Window replyPopup, AbstractCommentModel abstractCommentModel, boolean edit)
    {
    }


    protected void updateComment(CommentModel comment, ObjectValueContainer ovc, String text)
    {
        List<ObjectValueContainer.ObjectValueHolder> valueHolders = new ArrayList<>();
        for(PropertyDescriptor pd : ovc.getPropertyDescriptors())
        {
            ObjectValueContainer.ObjectValueHolder ovh = ovc.getValue(pd, null);
            if("AbstractComment.subject".equals(pd.getQualifier()))
            {
                comment.setSubject((String)ovh.getCurrentValue());
            }
            else if("AbstractComment.attachments".equals(pd.getQualifier()))
            {
                setCommentAttachments(comment, ovh.getCurrentValue());
            }
            valueHolders.add(ovh);
        }
        for(ObjectValueContainer.ObjectValueHolder ovh : valueHolders)
        {
            ovc.removeValue(ovh);
        }
        comment.setText(text);
    }


    protected void setCommentAttachments(CommentModel comment, Object currentValue)
    {
        if(currentValue instanceof Collection)
        {
            Collection<CommentAttachmentModel> attachments = null;
            Collection resultCollection = (Collection)currentValue;
            for(Object element : resultCollection)
            {
                Object singleObject;
                if(element instanceof ResultObjectWrapper)
                {
                    singleObject = ((ResultObjectWrapper)element).getObject();
                }
                else if(element instanceof TypedObject)
                {
                    singleObject = ((TypedObject)element).getObject();
                }
                else
                {
                    singleObject = null;
                }
                if(singleObject instanceof CommentAttachmentModel)
                {
                    if(attachments == null)
                    {
                        attachments = new ArrayList<>();
                    }
                    attachments.add((CommentAttachmentModel)singleObject);
                    continue;
                }
                LOG.warn("Unsupported element type: " + singleObject);
            }
            comment.setAttachments(attachments);
        }
    }


    protected Integer getRealPositionCoordinate(int scaledCoordinate, double scaleFactor)
    {
        return Integer.valueOf((int)Math.round(scaledCoordinate / scaleFactor));
    }


    protected void validateCommentLayer(CommentLayerComponent component)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("commentLayerComponent", component);
        if(component.getModel() == null)
        {
            throw new IllegalStateException("Comment layer component has no model");
        }
    }


    public void refreshContextList(CommentLayerAwareModel commentLayerAwareModel, boolean refreshItems)
    {
        CommentLayerContextComponent contextComponent = commentLayerAwareModel.getCommentLayerContext().getCommentContextAreaComponent();
        if(refreshItems)
        {
            ItemModel page = (ItemModel)commentLayerAwareModel.getCommentLayerTarget();
            List<CommentModel> comments = this.commentLayerService.getCommentsForCommentLayer(getCurentUser(), page);
            commentLayerAwareModel.getCommentLayerContext().getCommentContextAreaComponent().getCommentTreeModel()
                            .setComments(getTypeService().wrapItems(comments));
        }
        contextComponent.update();
    }


    protected TypeService getTypeService()
    {
        return UISessionUtils.getCurrentSession().getTypeService();
    }


    public boolean canEditComment(CommentModel comment)
    {
        return this.commentLayerService.canUserEditComment(getCurentUser(), (AbstractCommentModel)comment);
    }


    public boolean canReplyToComment(CommentModel comment)
    {
        return this.commentLayerService.canUserReplyToComment(getCurentUser(), (AbstractCommentModel)comment);
    }


    public boolean canMoveCommentIcon(CommentIcon icon)
    {
        CommentModel comment = icon.getModel().getComment();
        return this.commentLayerService.canUserMoveComment(getCurentUser(), comment);
    }


    public boolean canDeleteComment(CommentModel comment)
    {
        return this.commentLayerService.canUserDeleteComment(getCurentUser(), (AbstractCommentModel)comment);
    }


    protected void createEditPopupContentForComment(Window newCommentPopup, CommentLayerComponent clComponent, CommentLayerAwareModel clAwareModel, CommentIcon newIcon, CommentModel commentToEdit, boolean create)
    {
    }


    protected Window createEditPopupForComment(CommentLayerComponent clComponent, CommentLayerAwareModel clAwareModel, CommentIcon newIcon, CommentModel commentToEdit, boolean create)
    {
        Component parent = clAwareModel.getParentAreaComponent();
        CommentModel commentToSave = create ? newIcon.getModel().getComment() : commentToEdit;
        TypedObject wrappedComment = this.cockpitTypeService.wrapItem(commentToSave);
        PropertyDescriptor commentSubjectPropertyDescriptor = this.cockpitTypeService.getPropertyDescriptor("AbstractComment.subject");
        PropertyDescriptor commentPriorityPropertyDescriptor = this.cockpitTypeService.getPropertyDescriptor("Comment.priority");
        PropertyDescriptor commentAttachmentsPropertyDescriptor = this.cockpitTypeService.getPropertyDescriptor("AbstractComment.attachments");
        Set<PropertyDescriptor> props = new HashSet<>();
        props.add(commentSubjectPropertyDescriptor);
        props.add(commentPriorityPropertyDescriptor);
        props.add(commentAttachmentsPropertyDescriptor);
        ObjectValueContainer ovc = TypeTools.createValueContainer(wrappedComment, props,
                        UISessionUtils.getCurrentSession().getSystemService().getAllReadableLanguageIsos());
        Window newCommentPopup = new Window(Labels.getLabel("popup.create.edid.comment"), "normal", true);
        EventListener cancelPopupEventListener = event -> {
            if(create)
            {
                if(clComponent != null)
                {
                    clComponent.removeIcon(newIcon);
                    clComponent.getModel().removeIcon(newIcon.getModel());
                }
                UISessionUtils.getCurrentSession().getModelService().detach(commentToSave);
            }
            newCommentPopup.detach();
            changeCommentLayerToDefaultMode(clAwareModel.getCommentLayerContext());
        };
        newCommentPopup.addEventListener("onClose", cancelPopupEventListener);
        Label subjectLabel = new Label();
        subjectLabel.setValue(Labels.getLabel("commentlayer.popup.subject"));
        newCommentPopup.appendChild((Component)subjectLabel);
        EditorHelper.createEditor(wrappedComment, commentSubjectPropertyDescriptor, (HtmlBasedComponent)newCommentPopup, ovc, false);
        newCommentPopup.appendChild((Component)new Br());
        newCommentPopup.appendChild((Component)new Br());
        Box validationInfoBox = new Box();
        validationInfoBox.setVisible(false);
        validationInfoBox.setSclass("wizardErrorMessage");
        validationInfoBox.setWidth("100%");
        newCommentPopup.appendChild((Component)validationInfoBox);
        Label textLabel = new Label();
        textLabel.setValue(Labels.getLabel("commentlayer.popup.description"));
        newCommentPopup.appendChild((Component)textLabel);
        CockpitBasicFCKEditor fckEditor = createCockpitFCKEditor(Collections.singletonMap("inline", "true"));
        fckEditor.setValue(commentToSave.getText());
        newCommentPopup.appendChild((Component)fckEditor);
        newCommentPopup.appendChild((Component)new Br());
        newCommentPopup.appendChild((Component)new Br());
        Label attachmentLabel = new Label();
        attachmentLabel.setValue(Labels.getLabel("commentlayer.popup.attachments"));
        newCommentPopup.appendChild((Component)attachmentLabel);
        EditorHelper.createEditor(wrappedComment, commentAttachmentsPropertyDescriptor, (HtmlBasedComponent)newCommentPopup, ovc, false);
        newCommentPopup.appendChild((Component)new Br());
        newCommentPopup.appendChild((Component)new Br());
        newCommentPopup.setSclass("commentlayerpopup");
        UITools.modifySClass((HtmlBasedComponent)newCommentPopup, "commentlayerpopup_index", true);
        newCommentPopup.setVisible(true);
        newCommentPopup.doHighlighted();
        newCommentPopup.setContentSclass("commentlayerpopupcontent");
        createEditPopupContentForComment(newCommentPopup, clComponent, clAwareModel, newIcon, commentToEdit, create);
        Button doneButton = new Button();
        doneButton.setLabel(Labels.getLabel("done"));
        doneButton.addEventListener("onClick", event -> {
            String text = fckEditor.getValue();
            if(!CommentLayerUtils.isValidCommentText(text))
            {
                if(!validationInfoBox.isVisible())
                {
                    validationInfoBox.setVisible(true);
                    validationInfoBox.appendChild((Component)new Label(Labels.getLabel("commentlayer.comment.text.notempty")));
                }
                return;
            }
            updateComment(commentToSave, ovc, text);
            commentToSave.setAssignedTo(Collections.singletonList(getCurentUser()));
            prepareCommentUpdate(newCommentPopup, (AbstractCommentModel)commentToSave, create);
            this.cockpitModelHelper.saveModel((ItemModel)commentToSave, false);
            if(create)
            {
                CommentLayerComponentModel model = clComponent.getModel();
                List<CommentModel> comments = new ArrayList<>();
                ItemModel itemModel = (ItemModel)model.getCommentTarget().getObject();
                comments.addAll(itemModel.getComments());
                comments.add(commentToSave);
                itemModel.setComments(comments);
                this.cockpitModelHelper.saveModel(itemModel, false);
                CommentMetadataModel commentMetadata = createCommentMetadata(model, commentToSave, newIcon);
                this.cockpitModelHelper.saveModel((ItemModel)commentMetadata, false);
                updateComponentIcon(clComponent, newIcon, commentToSave);
                refreshContextList(model.getCommentLayerAwareModel(), true);
            }
            if(clComponent != null)
            {
                clComponent.refresh();
            }
            if(parent != null)
            {
                parent.removeChild((Component)newCommentPopup);
            }
            newCommentPopup.detach();
            changeCommentLayerToDefaultMode(clAwareModel.getCommentLayerContext());
        });
        UITools.applyTestID((Component)doneButton, "commentLayerEditPopup_DONE");
        Button cancelButton = new Button();
        cancelButton.setLabel(Labels.getLabel("cancel"));
        cancelButton.addEventListener("onClick", cancelPopupEventListener);
        renderSimpleDecisionNavigation((HtmlBasedComponent)newCommentPopup, doneButton, cancelButton);
        return newCommentPopup;
    }


    protected CommentMetadataModel createCommentMetadata(CommentLayerComponentModel model, CommentModel commentToSave, CommentIcon newIcon)
    {
        CommentMetadataModel commentMetadata = (CommentMetadataModel)this.modelService.create(CommentMetadataModel.class);
        commentMetadata.setX(getRealPositionCoordinate(newIcon.getModel().getX(), model.getScaleFactor()));
        commentMetadata.setY(getRealPositionCoordinate(newIcon.getModel().getY(), model.getScaleFactor()));
        commentMetadata.setPageIndex(Integer.valueOf(model.getPageIndex()));
        commentMetadata.setComment(commentToSave);
        if(model.getCommentLayerTarget() != null)
        {
            commentMetadata.setItem((ItemModel)model.getCommentLayerTarget().getObject());
        }
        return commentMetadata;
    }


    protected void updateComponentIcon(CommentLayerComponent clComponent, CommentIcon newIcon, CommentModel commentToSave)
    {
        clComponent.removeIcon(newIcon);
        CommentIconModel newModel = newIcon.getModel();
        newModel.setComment(commentToSave);
        newIcon.detach();
        CommentIcon instantiatedIcon = createNewIcon(newModel);
        clComponent.addIconComponent(instantiatedIcon);
    }


    protected Window createEditPopupForComment(CommentLayerAwareModel clAwareModel, CommentIcon newIcon, CommentModel commentToEdit, boolean create)
    {
        if(create)
        {
            CommentModel newComment = newIcon.getModel().getComment();
            CommentLayerComponent commentLayerComponent = CommentLayerUtils.getOwningCommentLayer(clAwareModel, newComment);
            return createEditPopupForComment(commentLayerComponent, clAwareModel, newIcon, commentToEdit, create);
        }
        CommentLayerComponent clComponent = CommentLayerUtils.getOwningCommentLayer(clAwareModel, commentToEdit);
        return createEditPopupForComment(clComponent, clAwareModel, null, commentToEdit, create);
    }


    protected void renderSimpleDecisionNavigation(HtmlBasedComponent container, Button positiveButton, Button negativeButton)
    {
        UITools.modifySClass((HtmlBasedComponent)negativeButton, "negative", true);
        UITools.modifySClass((HtmlBasedComponent)positiveButton, "positive", true);
        Div positiveContainer = new Div();
        positiveContainer.setSclass("positiveContainer");
        positiveContainer.appendChild((Component)positiveButton);
        Div negativeContainer = new Div();
        negativeContainer.setSclass("negativeContainer");
        negativeContainer.appendChild((Component)negativeButton);
        container.appendChild((Component)negativeContainer);
        container.appendChild((Component)positiveContainer);
    }


    public void moveCommentIcon(CommentLayerComponent commentLayerComponent, CommentIcon icon, int x_Position, int y_Position)
    {
        CommentModel comment = icon.getModel().getComment();
        CommentMetadataModel positionMetaData = comment.getCommentMetadata().iterator().next();
        TypedObject cLTarget = commentLayerComponent.getModel().getCommentLayerTarget();
        if(cLTarget != null)
        {
            positionMetaData.setItem((ItemModel)cLTarget.getObject());
        }
        try
        {
            positionMetaData.setX(getRealPositionCoordinate(x_Position, commentLayerComponent.getModel().getScaleFactor()));
            positionMetaData.setY(getRealPositionCoordinate(y_Position, commentLayerComponent.getModel().getScaleFactor()));
            this.cockpitModelHelper.saveModel((ItemModel)positionMetaData, false);
        }
        catch(ValueHandlerException e)
        {
            LOG.error("Could not move comment, due to : " + e.getMessage(), (Throwable)e);
        }
        icon.getModel().setX(x_Position);
        icon.getModel().setY(y_Position);
        CommentLayerAwareModel clAwareModel = commentLayerComponent.getModel().getCommentLayerAwareModel();
        if(clAwareModel != null && clAwareModel.getCommentLayerContext() != null && clAwareModel
                        .getCommentLayerContext().getCommentLayerComponents() != null)
        {
            for(CommentLayerComponent clComp : clAwareModel.getCommentLayerContext().getCommentLayerComponents())
            {
                clComp.refresh();
            }
        }
    }


    protected CockpitBasicFCKEditor createCockpitFCKEditor(Map<String, ? extends Object> editorParameters)
    {
        return new CockpitBasicFCKEditor(UISessionUtils.getCurrentSession().getLanguageIso(), CockpitFCKEditor.Skin.SILVER);
    }


    protected Window createReplyPopup(CommentLayerAwareModel model, AbstractCommentModel abstractCommentModel, boolean edit)
    {
        Component parent = model.getParentAreaComponent();
        Window replyPopup = new Window(Labels.getLabel("popup.reply.comment"), "normal", true);
        Map<String, String> wysiwygParams = new HashMap<>();
        wysiwygParams.put("inline", "true");
        Box validationInfoBox = new Box();
        validationInfoBox.setVisible(false);
        validationInfoBox.setSclass("wizardErrorMessage");
        validationInfoBox.setWidth("100%");
        replyPopup.appendChild((Component)validationInfoBox);
        CockpitBasicFCKEditor cockpitBasicFCKEditor = createCockpitFCKEditor((Map)wysiwygParams);
        if(edit)
        {
            cockpitBasicFCKEditor.setValue(abstractCommentModel.getText());
        }
        replyPopup.appendChild((Component)cockpitBasicFCKEditor);
        EventListener cancelPopupEventListener = event -> {
            parent.removeChild((Component)replyPopup);
            changeCommentLayerToDefaultMode(model.getCommentLayerContext());
            refreshContextList(model, false);
        };
        replyPopup.addEventListener("onClose", cancelPopupEventListener);
        Button doneButton = new Button(Labels.getLabel("done"));
        doneButton.addEventListener("onClick", event -> {
            String text = fckEditor.getValue();
            if(!CommentLayerUtils.isValidCommentText(text))
            {
                if(!validationInfoBox.isVisible())
                {
                    validationInfoBox.setVisible(true);
                    validationInfoBox.appendChild((Component)new Label(Labels.getLabel("commentlayer.reply.text.notempty")));
                }
                return;
            }
            prepareReplyUpdate(replyPopup, abstractCommentModel, edit);
            if(edit)
            {
                abstractCommentModel.setText(fckEditor.getValue());
                this.cockpitModelHelper.saveModel((ItemModel)abstractCommentModel, false);
            }
            else
            {
                this.commentLayerService.replyToComment(fckEditor.getValue(), abstractCommentModel);
            }
            parent.removeChild((Component)replyPopup);
            changeCommentLayerToDefaultMode(model.getCommentLayerContext());
            refreshContextList(model, false);
        });
        UITools.applyTestID((Component)doneButton, "commentLayerReplyPopup_DONE");
        Button cancelButton = new Button(Labels.getLabel("cancel"));
        cancelButton.addEventListener("onClick", cancelPopupEventListener);
        renderSimpleDecisionNavigation((HtmlBasedComponent)replyPopup, doneButton, cancelButton);
        replyPopup.setSclass("commentlayerpopupcontent");
        replyPopup.setVisible(true);
        replyPopup.doHighlighted();
        replyPopup.setSclass("commentlayerpopupcontent");
        return replyPopup;
    }


    protected CommentIcon createNewIcon(CommentIconModel model)
    {
        return (CommentIcon)new DivCommentIcon(model);
    }


    public void replyComment(CommentLayerAwareModel model, AbstractCommentModel abstractCommentModel)
    {
        Component parent = model.getParentAreaComponent();
        Window replyPopup = createReplyPopup(model, abstractCommentModel, false);
        parent.appendChild((Component)replyPopup);
    }


    public CommentIcon refreshCommentIcon(CommentIcon icon)
    {
        CommentModel comment = icon.getModel().getComment();
        if(this.modelService.isRemoved(comment) || this.modelService.isNew(comment))
        {
            return null;
        }
        this.modelService.refresh(comment);
        Popup popup = this.commentTooltipRenderer.renderItemTooltip((ItemModel)comment);
        icon.setTooltip(popup);
        icon.appendChild((Component)popup);
        CommentUserSettingModel setting = this.commentService.getUserSetting(getCurentUser(), (AbstractCommentModel)comment);
        if(setting != null && setting.getHidden() != null)
        {
            icon.getModel().setVisible(!setting.getHidden().booleanValue());
        }
        else
        {
            icon.getModel().setVisible(true);
        }
        if(canMoveCommentIcon(icon))
        {
            icon.setDraggable(icon.getModel().getParentComponentId());
        }
        icon.refresh();
        return icon;
    }


    public void editCommentPopup(CommentLayerAwareModel model, AbstractCommentModel abstractComment)
    {
        Component parent = model.getParentAreaComponent();
        Window popup = null;
        if(abstractComment instanceof de.hybris.platform.comments.model.ReplyModel)
        {
            popup = createReplyPopup(model, abstractComment, true);
        }
        else if(abstractComment instanceof CommentModel)
        {
            popup = createEditPopupForComment(model, null, (CommentModel)abstractComment, false);
        }
        parent.appendChild((Component)popup);
    }


    public double[] getWidthAndHeight(CommentLayerComponent component, double scaleFactor)
    {
        HtmlBasedComponent parent = component.getParent();
        double width = CommentLayerUtils.getSizeNumberValue(parent.getWidth());
        double height = CommentLayerUtils.getSizeNumberValue(parent.getHeight());
        return new double[] {width, height};
    }


    @Required
    public void setCommentLayerService(CommentLayerService commentLayerService)
    {
        this.commentLayerService = commentLayerService;
    }


    @Required
    public void setCommentService(CommentService commentService)
    {
        this.commentService = commentService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setCockpitTypeService(TypeService cockpitTypeService)
    {
        this.cockpitTypeService = cockpitTypeService;
    }


    @Required
    public void setUiAccessRightService(UIAccessRightService uiAccessRightService)
    {
        this.uiAccessRightService = uiAccessRightService;
    }


    @Required
    public void setCockpitModelHelper(ModelHelper cockpitModelHelper)
    {
        this.cockpitModelHelper = cockpitModelHelper;
    }


    public void changeCommentLayerMode(CommentLayerContext context, String mode)
    {
        if(context != null)
        {
            for(CommentLayerComponent clComponent : context.getCommentLayerComponents())
            {
                changeCommentLayerMode(clComponent, mode);
            }
        }
    }


    public void changeCommentLayerToDefaultMode(CommentLayerContext context)
    {
        if(context != null)
        {
            for(CommentLayerComponent clComponent : context.getCommentLayerComponents())
            {
                changeCommentLayerMode(clComponent, "selectComment");
            }
        }
    }


    @Required
    public void setCommentTooltipRenderer(TooltipRenderer commentTooltipRenderer)
    {
        this.commentTooltipRenderer = commentTooltipRenderer;
    }
}
