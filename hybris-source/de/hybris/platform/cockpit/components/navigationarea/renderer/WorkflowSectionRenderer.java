package de.hybris.platform.cockpit.components.navigationarea.renderer;

import de.hybris.platform.cockpit.components.navigationarea.model.TreeNodeWrapper;
import de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.WorkflowFacade;
import de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.WorkflowViewOptions;
import de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.components.MyWorkflowTabpanel;
import de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.components.WorkflowTemplatesTabpanel;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanel;
import de.hybris.platform.cockpit.components.sectionpanel.SimpleRenderer;
import de.hybris.platform.cockpit.components.sectionpanel.TabComponent;
import de.hybris.platform.cockpit.components.sectionpanel.TabFrame;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.CockpitEventAcceptor;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.dragdrop.DragAndDropWrapper;
import de.hybris.platform.cockpit.session.UINavigationArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.user.UserModel;
import java.util.List;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;

public class WorkflowSectionRenderer extends AbstractNavigationAreaSectionRenderer implements SimpleRenderer, CockpitEventAcceptor
{
    public static final Object rootDummy = new TreeNodeWrapper();
    private TabFrame.TabFrameModel tabFrameModel;
    private TabComponent tab1;
    private TabComponent tab2;
    private Component parentComponent = null;
    private final WorkflowViewOptions currentWorkflowViewOptions = new WorkflowViewOptions();
    private final WorkflowViewOptions currentAdhocWorkflowViewOptions = new WorkflowViewOptions();
    private TabFrame tabFrame = null;
    private MyWorkflowTabpanel myWorkflowsTabpanel;


    public int getWorkflowPageSize()
    {
        return (this.myWorkflowsTabpanel != null) ? this.myWorkflowsTabpanel.getPageSize() : 8;
    }


    public void refreshWorkflowPaging(int totalSize, int activePage)
    {
        this.myWorkflowsTabpanel.refreshWorkflowPaging(totalSize, activePage);
    }


    public WorkflowViewOptions getCurrentWorkflowViewOptions()
    {
        return this.currentWorkflowViewOptions;
    }


    public WorkflowViewOptions getCurrentAdhocWorkflowViewOptions()
    {
        return this.currentAdhocWorkflowViewOptions;
    }


    private void dontRenderSection()
    {
        List<Section> sections = getNavigationArea().getSectionModel().getSections();
        for(Section section : sections)
        {
            if(section instanceof de.hybris.platform.cockpit.components.navigationarea.NavigationPanelSection && section
                            .getLabel().equalsIgnoreCase(Labels.getLabel("navigationarea.workflow")))
            {
                getSectionPanelModel().removeSection(section);
                return;
            }
        }
    }


    public DragAndDropWrapper getDDWrapper()
    {
        return UISessionUtils.getCurrentSession().getCurrentPerspective().getDragAndDropWrapperService().getWrapper();
    }


    public void render(SectionPanel panel, Component parent, Component captionComponent, Section section)
    {
        render(parent);
    }


    private void renderWorkflowTemplates(Div container)
    {
        Div div = new Div();
        new WorkflowTemplatesTabpanel(div, this);
        container.appendChild((Component)div);
    }


    private void createMyWorkflow(Div container, Component parent)
    {
        Div div = new Div();
        this.myWorkflowsTabpanel = new MyWorkflowTabpanel(div, this, rootDummy, 0, (Div)parent);
        container.appendChild((Component)div);
    }


    private void update()
    {
        if(this.parentComponent != null && !UITools.isFromOtherDesktop(this.parentComponent))
        {
            this.parentComponent.getChildren().clear();
            render(this.parentComponent);
        }
    }


    public void setNavigationArea(UINavigationArea navigationArea)
    {
        super.setNavigationArea(navigationArea);
        navigationArea.addCockpitEventAcceptor(this);
    }


    public void onCockpitEvent(CockpitEvent event)
    {
        if(event instanceof ItemChangedEvent)
        {
            TypedObject item = ((ItemChangedEvent)event).getItem();
            if(item == null)
            {
                update();
            }
            else
            {
                String typeCode = item.getType().getCode();
                if("Workflow".equals(typeCode))
                {
                    update();
                }
            }
        }
    }


    public WorkflowFacade getWorkflowFacade()
    {
        return (WorkflowFacade)Registry.getApplicationContext().getBean("workflowFacade", WorkflowFacade.class);
    }


    public void render(Component parent)
    {
        this.parentComponent = parent;
        ((Div)parent).setClass("workflow_component_paging_section");
        UserModel userModel = getWorkflowFacade().getCurrentUser();
        if(getWorkflowFacade().hasVisibleWorkflowTemplatesForUser(userModel))
        {
            dontRenderSection();
            return;
        }
        if(this.tabFrame == null)
        {
            this.tabFrame = new TabFrame();
            this.tabFrame.setContainer(parent);
            this.tab1 = new TabComponent();
            TabComponent.TabComponentModel tab1Model = this.tab1.getModel();
            tab1Model.setDefaultImageUrl("cockpit/images/button_view_workflow_default.png");
            tab1Model.setActiveImageUrl("cockpit/images/button_view_workflow_active.png");
            tab1Model.setUnavailableImageUrl("cockpit/images/button_view_workflow_unavailable.png");
            tab1Model.setHoverImageUrl("cockpit/images/button_view_workflow_hover.png");
            this.tab1.setRenderer((TabComponent.TabComponentRenderer)new Object(this));
            this.tab2 = new TabComponent();
            TabComponent.TabComponentModel tab2Model = this.tab2.getModel();
            tab2Model.setDefaultImageUrl("cockpit/images/button_view_workflow_template_default.png");
            tab2Model.setActiveImageUrl("cockpit/images/button_view_workflow_template_active.png");
            tab2Model.setUnavailableImageUrl("cockpit/images/button_view_workflow_template_unavailable.png");
            tab2Model.setHoverImageUrl("cockpit/images/button_view_workflow_template_hover.png");
            this.tab2.setRenderer((TabComponent.TabComponentRenderer)new Object(this));
            this.tabFrameModel = this.tabFrame.getModel();
            this.tabFrameModel.addTabComponent(this.tab1);
            this.tabFrameModel.addTabComponent(this.tab2);
            this.tabFrameModel.setActiveTabComponent(this.tab2);
        }
        if(this.tabFrame != null)
        {
            this.tabFrame.setContainer(parent);
            this.tabFrame.render();
        }
    }


    public void changeSelectionToWorkflows()
    {
        this.tabFrameModel.setActiveTabComponent(this.tab1);
        this.tabFrame.render();
    }
}
