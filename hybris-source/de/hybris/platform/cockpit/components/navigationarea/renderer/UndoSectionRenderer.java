package de.hybris.platform.cockpit.components.navigationarea.renderer;

import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanel;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.CockpitEventAcceptor;
import de.hybris.platform.cockpit.events.impl.UndoRedoEvent;
import de.hybris.platform.cockpit.model.undo.UndoableOperation;
import de.hybris.platform.cockpit.model.undo.impl.CannotRedoException;
import de.hybris.platform.cockpit.model.undo.impl.CannotUndoException;
import de.hybris.platform.cockpit.services.undo.UndoManager;
import de.hybris.platform.cockpit.session.UINavigationArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.util.UndoTools;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vbox;

public class UndoSectionRenderer extends AbstractNavigationAreaSectionRenderer implements CockpitEventAcceptor
{
    protected static final String SEPARATOR = " | ";
    protected static final String UNDO_BUTTONS_SCLASS = "undoRedoButtons";
    protected static final String UNDO_BUTTONS_DIVIDER_SCLASS = "undoRedoButtonsDivider";
    protected static final String UNDO_ACTIONS_SCLASS = "undoRedoActions";
    protected static final String UNDO_ACTION_SCLASS = "undoAction";
    protected static final String LATEST_UNDO_ACTION_SCLASS = "latestAction";
    protected static final String LATEST_UNDO_ARROW_ACTION_SCLASS = "latestAction";
    protected static final String LAST_ACTION_SCLASS = "lastAction";
    protected static final String REDO_ACTION_SCLASS = "redoAction";
    protected static final String CONTEXT_DESCR_SCLASS = "undoRedoCtxLabel";
    protected static final String COCKPIT_ID_UNDO_BUTTON = "History_Undo_button";
    protected static final String COCKPIT_ID_REDO_BUTTON = "History_Redo_button";
    UndoManager undoManager = null;
    Component parentComponent = null;
    Div buttonDiv = null;
    Vbox actionBox = null;


    public void render(SectionPanel panel, Component parent, Component captionComponent, Section section)
    {
        this.parentComponent = parent;
        SectionPanel.SectionComponent sectionComponent = panel.getSectionComponent(section);
        if(null != sectionComponent)
        {
            UITools.modifySClass((HtmlBasedComponent)sectionComponent, "advancedPanelHistory", true);
        }
        renderButtons();
        renderActions();
    }


    private void renderButtons()
    {
        this.buttonDiv = new Div();
        this.buttonDiv.setSclass("undoRedoButtons");
        Image undoImage = new Image("cockpit/images/icon_undo.gif");
        UITools.addBusyListener((Component)undoImage, "onClick", getUndoEventListener(null), null, null);
        undoImage.setTooltiptext(Labels.getLabel("undoredocomponent.button.undo.tooltip"));
        this.buttonDiv.appendChild((Component)undoImage);
        Label undoLabel = new Label();
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            UITools.applyTestID((Component)undoLabel, "History_Undo_button");
        }
        undoLabel.setValue(Labels.getLabel("undoredocomponent.button.undo"));
        UITools.addBusyListener((Component)undoLabel, "onClick", getUndoEventListener(null), null, null);
        this.buttonDiv.appendChild((Component)undoLabel);
        Label splitterLabel = new Label(" | ");
        splitterLabel.setSclass("undoRedoButtonsDivider");
        this.buttonDiv.appendChild((Component)splitterLabel);
        Label redoLabel = new Label();
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            UITools.applyTestID((Component)redoLabel, "History_Redo_button");
        }
        redoLabel.setValue(Labels.getLabel("undoredocomponent.button.redo"));
        UITools.addBusyListener((Component)redoLabel, "onClick", getRedoEventListener(null), null, null);
        this.buttonDiv.appendChild((Component)redoLabel);
        Image redoImage = new Image("cockpit/images/icon_redo.gif");
        UITools.addBusyListener((Component)redoImage, "onClick", getRedoEventListener(null), null, null);
        redoImage.setTooltiptext(Labels.getLabel("undoredocomponent.button.redo.tooltip"));
        this.buttonDiv.appendChild((Component)redoImage);
        this.parentComponent.appendChild((Component)this.buttonDiv);
    }


    private void renderActions()
    {
        List<UndoableOperation> undoOperations = getUndoManager().getUndoOperations();
        List<UndoableOperation> redoOperations = getUndoManager().getRedoOperations();
        this.actionBox = new Vbox();
        this.actionBox.setWidth("100%");
        this.actionBox.setSclass("undoRedoActions");
        this.parentComponent.appendChild((Component)this.actionBox);
        Div stackElement = null;
        int i;
        for(i = undoOperations.size() - 1; i >= 0; i--)
        {
            UndoableOperation undo = undoOperations.get(i);
            stackElement = new Div();
            stackElement.setSclass("undoAction");
            if(i == 0)
            {
                UITools.modifySClass((HtmlBasedComponent)stackElement, "latestAction", true);
                Image arrow = new Image("cockpit/images/arrowSelectRight.gif");
                UITools.addBusyListener((Component)arrow, "onClick", getUndoEventListener(null), null, null);
                arrow.setSclass("latestAction");
                stackElement.appendChild((Component)arrow);
            }
            Vbox captionBox = new Vbox();
            Label label = new Label(undo.getUndoPresentationName());
            captionBox.appendChild((Component)label);
            String contextDescr = undo.getUndoContextDescription();
            if(!StringUtils.isBlank(contextDescr))
            {
                Label contextLabel = new Label(contextDescr);
                contextLabel.setSclass("undoRedoCtxLabel");
                captionBox.appendChild((Component)contextLabel);
            }
            stackElement.appendChild((Component)captionBox);
            this.actionBox.appendChild((Component)stackElement);
            UITools.addBusyListener((Component)stackElement, "onClick", getUndoEventListener(undo), null, null);
        }
        for(i = 0; i < redoOperations.size(); i++)
        {
            UndoableOperation redo = redoOperations.get(i);
            stackElement = new Div();
            stackElement.setSclass("redoAction");
            Vbox captionBox = new Vbox();
            Label label = new Label(redo.getRedoPresentationName());
            captionBox.appendChild((Component)label);
            String contextDescr = redo.getRedoContextDescription();
            if(!StringUtils.isBlank(contextDescr))
            {
                Label contextLabel = new Label(contextDescr);
                contextLabel.setSclass("undoRedoCtxLabel");
                captionBox.appendChild((Component)contextLabel);
            }
            stackElement.appendChild((Component)captionBox);
            this.actionBox.appendChild((Component)stackElement);
            UITools.addBusyListener((Component)stackElement, "onClick", getRedoEventListener(redo), null, null);
        }
        if(stackElement != null)
        {
            UITools.modifySClass((HtmlBasedComponent)stackElement, "lastAction", true);
        }
        else
        {
            stackElement = new Div();
            Label label = new Label(Labels.getLabel("undoredocomponent.empty"));
            stackElement.setSclass("lastAction");
            stackElement.appendChild((Component)label);
            this.actionBox.appendChild((Component)stackElement);
        }
    }


    public UndoManager getUndoManager()
    {
        if(this.undoManager == null)
        {
            this.undoManager = UISessionUtils.getCurrentSession().getUndoManager();
        }
        return this.undoManager;
    }


    private EventListener getUndoEventListener(UndoableOperation undo)
    {
        return (EventListener)new Object(this, undo);
    }


    private EventListener getRedoEventListener(UndoableOperation redo)
    {
        return (EventListener)new Object(this, redo);
    }


    public static void doUndoTask(UndoableOperation undoOperation, Object source)
    {
        UndoManager undoManager = UISessionUtils.getCurrentSession().getUndoManager();
        try
        {
            if(undoOperation == null)
            {
                undoOperation = undoManager.peekUndoOperation();
            }
            if(undoOperation != null)
            {
                UndoTools.undo(undoManager, undoOperation);
                UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new UndoRedoEvent(source, undoOperation, true));
            }
        }
        catch(CannotUndoException e)
        {
            Notification notification = new Notification(Labels.getLabel("undoredocomponent.message.undo"), e.getMessage());
            BaseUICockpitPerspective basePerspective = (BaseUICockpitPerspective)UISessionUtils.getCurrentSession().getCurrentPerspective();
            if(basePerspective.getNotifier() != null)
            {
                basePerspective.getNotifier().setNotification(notification);
            }
        }
    }


    public static void doRedoTask(UndoableOperation redoOperation, Object source)
    {
        UndoManager undoManager = UISessionUtils.getCurrentSession().getUndoManager();
        try
        {
            if(redoOperation == null)
            {
                redoOperation = undoManager.peekRedoOperation();
            }
            if(redoOperation != null)
            {
                UndoTools.redo(undoManager, redoOperation);
                UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new UndoRedoEvent(source, redoOperation, false));
            }
        }
        catch(CannotRedoException e)
        {
            Notification notification = new Notification(Labels.getLabel("undoredocomponent.message.redo"), e.getMessage());
            BaseUICockpitPerspective basePerspective = (BaseUICockpitPerspective)UISessionUtils.getCurrentSession().getCurrentPerspective();
            if(basePerspective.getNotifier() != null)
            {
                basePerspective.getNotifier().setNotification(notification);
            }
        }
    }


    private void update()
    {
        if(this.parentComponent != null && !UITools.isFromOtherDesktop(this.parentComponent))
        {
            this.parentComponent.removeChild((Component)this.buttonDiv);
            this.parentComponent.removeChild((Component)this.actionBox);
            renderButtons();
            renderActions();
        }
    }


    public void setNavigationArea(UINavigationArea navigationArea)
    {
        UINavigationArea prevArea = getNavigationArea();
        if(prevArea != null)
        {
            prevArea.removeCockpitEventAcceptor(this);
        }
        super.setNavigationArea(navigationArea);
        navigationArea.removeCockpitEventAcceptor(this);
        navigationArea.addCockpitEventAcceptor(this);
    }


    public void onCockpitEvent(CockpitEvent event)
    {
        if(event instanceof de.hybris.platform.cockpit.events.impl.AbstractUndoRedoEvent || event instanceof de.hybris.platform.cockpit.events.impl.GeneralUpdateEvent)
        {
            update();
        }
    }
}
