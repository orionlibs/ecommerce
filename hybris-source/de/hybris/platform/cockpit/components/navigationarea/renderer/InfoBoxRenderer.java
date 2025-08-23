package de.hybris.platform.cockpit.components.navigationarea.renderer;

import de.hybris.platform.cockpit.components.SlideUpGroupbox;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelModel;
import de.hybris.platform.cockpit.components.sectionpanel.SimpleRenderer;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.CockpitCommentService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitNavigationArea;
import de.hybris.platform.cockpit.session.impl.CommunicationBrowserModel;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.util.ValuePair;
import de.hybris.platform.core.Registry;
import de.hybris.platform.workflow.WorkflowActionService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Span;

public class InfoBoxRenderer extends AbstractNavigationAreaSectionRenderer implements SimpleRenderer
{
    private static final String DEFAULT_ATTACHMENT_TYPE = "de.hybris.platform.jalo.product.Product";
    private TypeService typeService;
    private WorkflowActionService workflowActionService;
    private List<String> workflowAttClassNames = Collections.singletonList("de.hybris.platform.jalo.product.Product");
    private Label nameLabel;
    private Label langLabel;
    private Label taskLabel;
    private Label commentLabel;
    private CockpitCommentService cockpitCommentService;
    private CommunicationBrowserModel communicationBrowserModel;


    public void render(SectionPanel panel, Component parent, Component captionComponent, Section section)
    {
        render(parent);
    }


    public void render(Component parent)
    {
        UICockpitPerspective perspective = UISessionUtils.getCurrentSession().getCurrentPerspective();
        setWorkflowAttClassNames(((BaseUICockpitNavigationArea)perspective.getNavigationArea()).getInfoSlotAttachmentTypes());
        List<ValuePair<String, Integer>> headerTokens = new ArrayList<>();
        SlideUpGroupbox infoBoxGroupbox = new SlideUpGroupbox(true);
        infoBoxGroupbox.setTimeOut(UISessionUtils.getCurrentSession().getCurrentPerspective().getInfoBoxTimeout());
        infoBoxGroupbox.setParent(parent);
        infoBoxGroupbox.setWidth("100%");
        if(perspective.getNavigationArea() instanceof BaseUICockpitNavigationArea)
        {
            infoBoxGroupbox.setLabel(((BaseUICockpitNavigationArea)perspective.getNavigationArea()).getInfoSlotLabel());
        }
        infoBoxGroupbox.setAction("onmouseup: comm.sendClick(#{self},null)");
        infoBoxGroupbox.addEventListener("onClick", (EventListener)new Object(this));
        Div infoSlotContent = new Div();
        infoBoxGroupbox.setContent((Component)infoSlotContent);
        infoSlotContent.setHeight("100%");
        Div div = new Div();
        infoSlotContent.appendChild((Component)div);
        div.setStyle("background:white;padding:7px");
        Hbox hbox1 = new Hbox();
        div.appendChild((Component)hbox1);
        hbox1.setWidth("100%");
        hbox1.setWidths("100%,6em");
        this.nameLabel = new Label(getUserName());
        hbox1.appendChild((Component)this.nameLabel);
        this.langLabel = new Label(getLanguage());
        hbox1.appendChild((Component)this.langLabel);
        Hbox hbox2 = new Hbox();
        div.appendChild((Component)hbox2);
        hbox2.setStyle("margin-top:6px;vertical-align:middle");
        Object object = new Object(this);
        List<TypedObject> assignedActions = getTypeService().wrapItems(getWorkflowActionService().getAllUserWorkflowActionsWithAttachments(getWorkflowAttClassNames()));
        Image imgTasks = new Image("cockpit/images/message.gif");
        hbox2.appendChild((Component)imgTasks);
        UITools.addBusyListener((Component)imgTasks, "onClick", (EventListener)object, null, null);
        imgTasks.setStyle("cursor:pointer;");
        imgTasks.setTooltiptext(Labels.getLabel("navigationarea.infobox.tasks.tooltip"));
        this.taskLabel = new Label("" + assignedActions.size() + " " + assignedActions.size());
        headerTokens.add(new ValuePair(Labels.getLabel("navigationarea.infobox.tasks.short"),
                        Integer.valueOf(assignedActions.size())));
        UITools.addBusyListener((Component)this.taskLabel, "onClick", (EventListener)object, null, null);
        this.taskLabel.setStyle("cursor:pointer;");
        hbox2.appendChild((Component)this.taskLabel);
        if(BooleanUtils.toBoolean(UITools.getCockpitParameter("default.comments.enabled", Executions.getCurrent())))
        {
            Hbox hbox3 = new Hbox();
            div.appendChild((Component)hbox3);
            hbox3.setStyle("margin-top:6px;vertical-align:middle");
            Object object1 = new Object(this);
            Image imgComment = new Image("cockpit/images/icon_func_comment_available.png");
            hbox3.appendChild((Component)imgComment);
            UITools.addBusyListener((Component)imgComment, "onClick", (EventListener)object1, null, null);
            imgComment.setStyle("cursor:pointer;");
            imgComment.setTooltiptext(Labels.getLabel("navigationarea.infobox.comments.tooltip"));
            int nrComments = getCockpitCommentService().getCurrentUserComments().size();
            this.commentLabel = new Label("" + nrComments + " " + nrComments);
            headerTokens.add(new ValuePair(Labels.getLabel("navigationarea.infobox.comments.short"),
                            Integer.valueOf(nrComments)));
            UITools.addBusyListener((Component)this.commentLabel, "onClick", (EventListener)object1, null, null);
            this.commentLabel.setStyle("cursor:pointer;");
            hbox3.appendChild((Component)this.commentLabel);
        }
        renderAdditionalContent((Component)div, headerTokens);
        Component headerInfo = createHeaderInfo(headerTokens);
        if(headerInfo != null)
        {
            infoBoxGroupbox.setCaption(headerInfo);
        }
    }


    protected void renderAdditionalContent(Component parent, List<ValuePair<String, Integer>> tokens)
    {
    }


    protected Component createHeaderInfo(List<ValuePair<String, Integer>> tokens)
    {
        if(CollectionUtils.isEmpty(tokens))
        {
            return null;
        }
        Div container = new Div();
        container.setSclass("shortInfoContainer");
        for(ValuePair<String, Integer> valuePair : tokens)
        {
            Div innerContainer = new Div();
            innerContainer.setSclass("shortInfoInner");
            container.appendChild((Component)innerContainer);
            innerContainer.appendChild(createHeaderTokenComponent(valuePair));
        }
        return (Component)container;
    }


    protected Component createHeaderTokenComponent(ValuePair<String, Integer> token)
    {
        Span tokenComponent = new Span();
        String shortLabel = (String)token.getFirst();
        Integer number = (Integer)token.getSecond();
        if(number != null)
        {
            Label numberLabel = new Label(number.toString());
            numberLabel.setSclass("numberLabel");
            tokenComponent.appendChild((Component)numberLabel);
        }
        Label label = new Label(shortLabel);
        label.setSclass("shortLabel");
        tokenComponent.appendChild((Component)label);
        return (Component)tokenComponent;
    }


    public SectionPanelModel getSectionPanelModel()
    {
        return getNavigationArea().getSectionModel();
    }


    private WorkflowActionService getWorkflowActionService()
    {
        if(this.workflowActionService == null)
        {
            this.workflowActionService = (WorkflowActionService)Registry.getApplicationContext().getBean("workflowActionService");
        }
        return this.workflowActionService;
    }


    public List<String> getWorkflowAttClassNames()
    {
        return this.workflowAttClassNames;
    }


    public void setWorkflowAttClassNames(List<String> workflowAttClassNames)
    {
        this.workflowAttClassNames = workflowAttClassNames;
    }


    protected String getUserName()
    {
        String name = "Anonymous";
        if(UISessionUtils.getCurrentSession() != null)
        {
            if(UISessionUtils.getCurrentSession().getUser() != null)
            {
                name = UISessionUtils.getCurrentSession().getUser().getName();
            }
        }
        return name;
    }


    protected String getLanguage()
    {
        String lang = "";
        if(UISessionUtils.getCurrentSession() != null)
        {
            lang = UISessionUtils.getCurrentSession().getGlobalDataLanguageIso();
        }
        return lang;
    }


    public CockpitCommentService getCockpitCommentService()
    {
        if(this.cockpitCommentService == null)
        {
            this.cockpitCommentService = (CockpitCommentService)SpringUtil.getBean("cockpitCommentService");
        }
        return this.cockpitCommentService;
    }


    public CommunicationBrowserModel getCommunicationBrowserModel()
    {
        if(this.communicationBrowserModel == null)
        {
            this.communicationBrowserModel = (CommunicationBrowserModel)SpringUtil.getBean("CommunicationBrowserModel");
        }
        return this.communicationBrowserModel;
    }


    private TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = (TypeService)SpringUtil.getBean("cockpitTypeService");
        }
        return this.typeService;
    }
}
