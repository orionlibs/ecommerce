package de.hybris.platform.cockpit.components.editorarea.renderer;

import de.hybris.platform.cockpit.components.listview.ActionColumnConfiguration;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.components.listview.impl.AbstractCommentAction;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionRenderer;
import de.hybris.platform.cockpit.components.sectionpanel.TooltipRenderer;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.CockpitCommentService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ValueService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.CommunicationBrowserModel;
import de.hybris.platform.cockpit.session.impl.CustomEditorSection;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.comments.model.DomainModel;
import de.hybris.platform.comments.services.CommentService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.Treecol;
import org.zkoss.zul.Treecols;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Vbox;

public class CommentsSectionRenderer implements SectionRenderer
{
    private static final String OPENED_NODES = "openedNodes";
    private static final String IMG_ATTACHMENT = "/cockpit/images/icon_attachement.png";
    private static final String IMG_NEW_COMMENT = "/cockpit/images/green_add_plus.gif";
    private String[] widths = new String[] {"auto", "100px", "50px", "60px"};
    private transient DomainModel domain;
    private transient ComponentModel component;
    private transient CommentTypeModel commentType;
    private transient int maxReplyLevel = 0;
    private transient CommentService commentService;
    private transient CommunicationBrowserModel communicationBrowserModel;
    private transient CockpitCommentService cockpitCommentService;
    private transient TypeService typeService;
    private transient ValueService valueService;
    private transient Section section;
    private transient TooltipRenderer tooltipRenderer;
    private static final Logger LOG = LoggerFactory.getLogger(CommentsSectionRenderer.class);


    public void render(SectionPanel panel, Component parent, Component captionComponent, Section section)
    {
        if(BooleanUtils.toBoolean(UITools.getCockpitParameter("default.comments.enabled", Executions.getCurrent())))
        {
            TypedObject currentItem = null;
            Map<String, Object> context = panel.getModel().getContext();
            if(context != null)
            {
                currentItem = (TypedObject)context.get("currentObject");
            }
            if(currentItem == null)
            {
                LOG.error("Can not render section, current item is null.");
                return;
            }
            parent.getChildren().clear();
            String domainCode = UITools.getCockpitParameter("default.commentsection.domaincode", Executions.getCurrent());
            String componentCode = UITools.getCockpitParameter("default.commentsection.componentcode", Executions.getCurrent());
            String commentTypeCode = UITools.getCockpitParameter("default.commentsection.commenttypecode",
                            Executions.getCurrent());
            if(UITools.getCockpitParameter("default.commentsection.maxreplylevel", Executions.getCurrent()) != null)
            {
                this.maxReplyLevel = Integer.parseInt(UITools.getCockpitParameter("default.commentsection.maxreplylevel",
                                Executions.getCurrent()));
            }
            if(domainCode != null && componentCode != null && commentTypeCode != null)
            {
                this.domain = getCommentService().getDomainForCode(domainCode);
                this.component = getCommentService().getComponentForCode(this.domain, componentCode);
                this.commentType = getCommentService().getCommentTypeForCode(this.component, commentTypeCode);
            }
            Div div = new Div();
            div.setSclass("commentSection");
            Vbox vbox = new Vbox();
            vbox.setWidth("100%");
            Tree commentTree = new Tree();
            boolean permission = UISessionUtils.getCurrentSession().getSystemService().checkPermissionOn(getTypeService().getObjectType("Reply").getCode(), "create");
            if(permission)
            {
                Div captionDiv = new Div();
                Image addImage = new Image();
                addImage.setSrc("/cockpit/images/green_add_plus.gif");
                addImage.setTooltip(Labels.getLabel("comment.addComment"));
                addImage.setSclass("addCommentBtn");
                List<TypedObject> relatedItemList = Collections.singletonList(currentItem);
                addImage.addEventListener("onClick", getAddCommentButtonEventListener(captionDiv, relatedItemList));
                captionDiv.appendChild((Component)addImage);
                captionDiv.setSclass("commentSectionCaption");
                captionComponent.appendChild((Component)captionDiv);
            }
            Treecols columns = new Treecols();
            Treecol commentCol = new Treecol();
            columns.appendChild((Component)commentCol);
            Treecol attachmentCol = new Treecol();
            columns.appendChild((Component)attachmentCol);
            Treecol userCol = new Treecol();
            columns.appendChild((Component)userCol);
            Treecol dateCol = new Treecol();
            columns.appendChild((Component)dateCol);
            Treecol actionCol = new Treecol();
            columns.appendChild((Component)actionCol);
            UITools.setComponentWidths(getWidths(), new HtmlBasedComponent[] {(HtmlBasedComponent)commentCol, (HtmlBasedComponent)attachmentCol, (HtmlBasedComponent)userCol, (HtmlBasedComponent)dateCol, (HtmlBasedComponent)actionCol});
            commentTree.appendChild((Component)columns);
            commentTree.setMultiple(true);
            commentTree.setFixedLayout(true);
            commentTree.setSclass("z-commenttree");
            CommentTreeModel commentTreeModel = new CommentTreeModel(this, new TreeNodeWrapper(this), currentItem);
            commentTree.setModel((TreeModel)commentTreeModel);
            commentTree.setTreeitemRenderer((TreeitemRenderer)new CommentTreeRenderer(this, currentItem, commentTree));
            vbox.appendChild((Component)commentTree);
            div.appendChild((Component)vbox);
            parent.appendChild((Component)div);
            this.section = section;
            if(section instanceof CustomEditorSection)
            {
                List<List<Integer>> openNodes = (List<List<Integer>>)((CustomEditorSection)section).getAttribute("openedNodes");
                if(openNodes == null || !restoreOpenedState(commentTree, openNodes))
                {
                    ((CustomEditorSection)section).setAttribute("openedNodes", getOpenedNodes(commentTree, 10));
                }
            }
        }
        else
        {
            Div div = new Div();
            div.setSclass("editor_save_info");
            div.appendChild((Component)new Label(Labels.getLabel("comment.notEnabled")));
            parent.appendChild((Component)div);
        }
    }


    protected boolean restoreOpenedState(Tree tree, List<List<Integer>> openedNodes)
    {
        boolean result = true;
        for(List<Integer> path : openedNodes)
        {
            result |= openPath(tree, path);
        }
        return result;
    }


    public CommentService getCommentService()
    {
        if(this.commentService == null)
        {
            this.commentService = (CommentService)SpringUtil.getBean("commentService");
        }
        return this.commentService;
    }


    private void renderActions(AbstractCommentModel commentItem, Div parent, Tree commentTree, TypedObject item)
    {
        if(parent != null && commentItem != null)
        {
            TypedObject typedObj = getTypeService().wrapItem(commentItem);
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
                contextMap.put("browserArea", "editorArea");
                contextMap.put("parent", parent);
                contextMap.put(AbstractCommentAction.UPDATELISTENER, new Object(this, item, commentTree));
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
                        actionImg.addEventListener("onClick", listener);
                        actionImg.addEventListener("onLater", listener);
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
        return (ActionColumnConfiguration)SpringUtil.getBean("CommentActionColumnsEditorArea");
    }


    protected String formatModifiedDate(Date modifiedDate)
    {
        return (new SimpleDateFormat("dd.MM.", UISessionUtils.getCurrentSession().getLocale())).format(modifiedDate);
    }


    private void update(TypedObject currentItem, Tree commentTree)
    {
        List<List<Integer>> openNodes = getOpenedNodes(commentTree, 10);
        CommentTreeModel commentTreeModel = new CommentTreeModel(this, new TreeNodeWrapper(this), currentItem);
        commentTree.setModel((TreeModel)commentTreeModel);
        restoreOpenedState(commentTree, openNodes);
    }


    protected List<List<Integer>> getOpenedNodes(Tree tree, int depth)
    {
        List<List<Integer>> ret = new ArrayList<>();
        for(Object treeitem : tree.getItems())
        {
            Treeitem item = (Treeitem)treeitem;
            if(item.getLevel() <= depth && item.isOpen() && item.getTreechildren() != null && item
                            .getTreechildren().getChildren().size() > 0)
            {
                List<Integer> path = getPathToRoot(item);
                if(!path.isEmpty())
                {
                    ret.add(path);
                }
            }
        }
        return ret;
    }


    protected List<Integer> getPathToRoot(Treeitem item)
    {
        List<Integer> ret = new ArrayList<>();
        Treeitem currentItem = item;
        while(currentItem != null)
        {
            ret.add(Integer.valueOf(currentItem.indexOf()));
            if(!currentItem.isOpen() || !currentItem.isVisible())
            {
                ret.clear();
                break;
            }
            currentItem = currentItem.getParentItem();
        }
        Collections.reverse(ret);
        return ret;
    }


    protected boolean openPath(Tree tree, List<Integer> path)
    {
        List<Treeitem> treeitems = tree.getTreechildren().getChildren();
        for(Integer integer : path)
        {
            int index = integer.intValue();
            if(treeitems != null && treeitems.size() > index)
            {
                Treeitem item = treeitems.get(index);
                item.setOpen(true);
                treeitems = (item.getTreechildren() == null) ? null : item.getTreechildren().getChildren();
                continue;
            }
            return false;
        }
        return true;
    }


    public void setWidths(String[] widths)
    {
        this.widths = widths;
    }


    public String[] getWidths()
    {
        return this.widths;
    }


    @Required
    public void setCommunicationBrowserModel(CommunicationBrowserModel communicationBrowserModel)
    {
        this.communicationBrowserModel = communicationBrowserModel;
    }


    public CommunicationBrowserModel getCommunicationBrowserModel()
    {
        return this.communicationBrowserModel;
    }


    public CockpitCommentService getCockpitCommentService()
    {
        if(this.cockpitCommentService == null)
        {
            this.cockpitCommentService = (CockpitCommentService)SpringUtil.getBean("cockpitCommentService");
        }
        return this.cockpitCommentService;
    }


    public TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }


    public ValueService getValueService()
    {
        if(this.valueService == null)
        {
            this.valueService = (ValueService)SpringUtil.getBean("valueService");
        }
        return this.valueService;
    }


    protected EventListener getAddCommentButtonEventListener(Div captionDiv, List<TypedObject> relatedItemList)
    {
        return (EventListener)new Object(this, relatedItemList, captionDiv);
    }


    @Required
    public void setTooltipRenderer(TooltipRenderer tooltipRenderer)
    {
        this.tooltipRenderer = tooltipRenderer;
    }
}
