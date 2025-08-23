package de.hybris.platform.adaptivesearchbackoffice.editors.configurablemultireference;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.UITools;
import de.hybris.platform.adaptivesearchbackoffice.components.ActionsMenu;
import de.hybris.platform.adaptivesearchbackoffice.data.AbstractEditorData;
import de.hybris.platform.adaptivesearchbackoffice.editors.EditorLogic;
import de.hybris.platform.adaptivesearchbackoffice.editors.EditorRenderer;
import java.util.StringJoiner;
import javax.annotation.Resource;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class DefaultAsMultiReferenceItemMasterRenderer<D extends AbstractEditorData, V> implements EditorRenderer<MultiReferenceEditorLogic<D, V>, D>
{
    protected static final String TOGGLE_SCLASS = "yas-toggle";
    protected static final String TOGGLE_DISABLED_SCLASS = "yas-toggle-disabled";
    protected static final String TOGGLE_ICON_SCLASS = "yas-toggle-icon";
    protected static final String TOGGLE_CNG_ACTION_ICON = "cng-action-icon";
    protected static final String TOGGLE_CNG_FONT_ICON = "cng-font-icon";
    protected static final String TOGGLE_FONT_ICON_NAVIGATION_DOWN_ARROW = "font-icon--navigation-down-arrow";
    protected static final String TOGGLE_FONT_ICON_NAVIGATION_RIGHT_ARROW = "font-icon--navigation-right-arrow";
    protected static final String INFO_SCLASS = "yas-info";
    protected static final String INFO_ICON_SCLASS = "yas-info-icon";
    protected static final String LABEL_SCLASS = "yas-label";
    protected static final String PROPERTY_SCLASS = "yas-property";
    protected static final String EDITABLE_SCLASS = "yas-editable";
    protected static final String ACTIONS_CONTEXT_SUFFIX = "-actions";
    protected static final String EDITOR_LOGIC_KEY = "editorLogic";
    @Resource
    private LabelService labelService;


    public boolean isEnabled(MultiReferenceEditorLogic<D, V> logic)
    {
        return true;
    }


    public boolean canRender(MultiReferenceEditorLogic<D, V> logic, Component parent, D data)
    {
        return true;
    }


    public void render(MultiReferenceEditorLogic<D, V> logic, Component parent, D data)
    {
        renderToggle(logic, parent, data);
        renderInfo(logic, parent, data);
        renderLabel(logic, parent, data);
        renderProperties(logic, parent, data);
        renderActions(logic, parent, data);
    }


    protected void renderToggle(MultiReferenceEditorLogic<D, V> logic, Component parent, D data)
    {
        EditorRenderer itemDetailRenderer = logic.getItemDetailRenderer();
        if(itemDetailRenderer.isEnabled((EditorLogic)logic))
        {
            Div itemToggleDiv = new Div();
            itemToggleDiv.setParent(parent);
            Div itemToggleIconDiv = new Div();
            itemToggleIconDiv.setParent((Component)itemToggleDiv);
            if(itemDetailRenderer.canRender((EditorLogic)logic, parent, data))
            {
                Component item = logic.findEditorItem(parent);
                boolean open = logic.isOpen(item);
                itemToggleDiv.setSclass(buildToggleSclass(true));
                itemToggleIconDiv.setSclass(buildToggleIconSclass(open));
                item.addEventListener("onOpen", event -> {
                    OpenEvent openEvent = (OpenEvent)event;
                    itemToggleIconDiv.setSclass(buildToggleIconSclass(openEvent.isOpen()));
                });
            }
            else
            {
                itemToggleDiv.setSclass(buildToggleSclass(false));
                itemToggleIconDiv.setSclass(buildToggleIconSclass(false));
            }
        }
    }


    protected String buildToggleSclass(boolean enabled)
    {
        StringJoiner styleClass = new StringJoiner(" ");
        styleClass.add("yas-toggle");
        if(!enabled)
        {
            styleClass.add("yas-toggle-disabled");
        }
        return styleClass.toString();
    }


    protected String buildToggleIconSclass(boolean open)
    {
        StringJoiner styleClass = new StringJoiner(" ");
        styleClass.add("yas-toggle-icon");
        styleClass.add("cng-action-icon");
        styleClass.add("cng-font-icon");
        if(open)
        {
            styleClass.add("font-icon--navigation-down-arrow");
        }
        else
        {
            styleClass.add("font-icon--navigation-right-arrow");
        }
        return styleClass.toString();
    }


    protected void renderInfo(MultiReferenceEditorLogic<D, V> logic, Component parent, D data)
    {
        Div infoDiv = new Div();
        infoDiv.setParent(parent);
        infoDiv.setSclass("yas-info");
        Div infoIconDiv = new Div();
        infoIconDiv.setParent((Component)infoDiv);
        infoIconDiv.setSclass("yas-info-icon");
    }


    protected void renderLabel(MultiReferenceEditorLogic<D, V> logic, Component parent, D data)
    {
        Div labelDiv = new Div();
        labelDiv.setParent(parent);
        labelDiv.setSclass("yas-label");
        Label label = new Label();
        label.setParent((Component)labelDiv);
        label.setValue(data.getLabel());
    }


    protected void renderProperties(MultiReferenceEditorLogic<D, V> logic, Component parent, D data)
    {
        logic.getColumns().stream().forEach(property -> renderProperty(logic, parent, (D)data, property));
    }


    protected void renderProperty(MultiReferenceEditorLogic<D, V> logic, Component parent, D data, String property)
    {
        DataHandler dataHandler = logic.getDataHandler();
        Object attributeValue = dataHandler.getAttributeValue((AbstractEditorData)data, property);
        Class<?> attributeType = dataHandler.getAttributeType((AbstractEditorData)data, property);
        Div propertyDiv = new Div();
        propertyDiv.setParent(parent);
        propertyDiv.setSclass(buildPropertySclass(property));
        if(logic.getEditableColumns().contains(property) && data.isFromSearchConfiguration())
        {
            UITools.modifySClass((HtmlBasedComponent)propertyDiv, "yas-editable", true);
            Editor editor = new Editor();
            editor.setParent((Component)propertyDiv);
            editor.setInitialValue(attributeValue);
            editor.setDefaultEditor("com.hybris.cockpitng.editor.instanteditor");
            editor.setType(attributeType.getName());
            editor.setReadOnly(false);
            editor.setOptional(false);
            editor.afterCompose();
            editor.addEventListener("onValueChanged", event -> logic.updateAttributeValue(data, property, event.getData()));
        }
        else if(attributeValue != null)
        {
            Label label = new Label();
            label.setParent((Component)propertyDiv);
            label.setValue(this.labelService.getObjectLabel(attributeValue));
        }
    }


    protected String buildPropertySclass(String property)
    {
        StringBuilder styleClass = new StringBuilder();
        styleClass.append("yas-property");
        styleClass.append(' ');
        styleClass.append("yas-property").append('-').append(property);
        return styleClass.toString();
    }


    protected void renderActions(MultiReferenceEditorLogic<D, V> logic, Component parent, D data)
    {
        String actionsContext = logic.getContext() + "-actions";
        WidgetInstanceManager widgetInstanceManager = logic.getWidgetInstanceManager();
        ActionsMenu actionsMenu = new ActionsMenu();
        actionsMenu.setInputValue(data);
        actionsMenu.setConfig(actionsContext);
        actionsMenu.setWidgetInstanceManager(widgetInstanceManager);
        actionsMenu.setAttribute("widgetInstanceManager", widgetInstanceManager);
        actionsMenu.setAttribute("editorLogic", logic);
        actionsMenu.initialize();
        actionsMenu.setParent(parent);
    }
}
