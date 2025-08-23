package de.hybris.platform.cockpit.model.editor;

import de.hybris.platform.cockpit.components.CockpitFCKEditor;
import de.hybris.platform.cockpit.model.editor.impl.AbstractUIEditor;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Html;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.InputElement;

public class WysiwygUIEditor extends AbstractUIEditor
{
    protected static final String QUESTION_MARK = "?";
    private static final Logger LOG = LoggerFactory.getLogger(WysiwygUIEditor.class);
    private static final String DEFAULT_EDITOR_WIDTH = "650px";
    private static final String DEFAULT_EDITOR_HEIGHT = "300px";
    private static final int DEFAULT_EDITOR_ROWS = 8;
    private static final int MAX_LABEL_LENGTH = 50;
    private static final String PREVIEW_BTN = "/cockpit/images/icon_func_preview_available.png";
    private static final String OPEN_BTN = "/cockpit/images/icon_func_inlinetext_openwysiwyg.png";
    private static final String REVERT_BTN = "/cockpit/images/icon_func_inlinetext_revert.png";
    private static final String DEFAULT_FCK_EMPTY_STRING_REGEXP = "^[\\s\\u00A0]*$";
    protected boolean inline = false;


    @Deprecated
    protected String getSpellcheckerPath()
    {
        return null;
    }


    protected CockpitFCKEditor createCockpitFCKEditor(Map<String, ? extends Object> editorParameters)
    {
        return new CockpitFCKEditor(UISessionUtils.getCurrentSession().getLanguageIso(), getSpellcheckerPath(), CockpitFCKEditor.Skin.SILVER);
    }


    protected HtmlBasedComponent createWysiwygInline(String html, EditorListener listener, Map<String, ? extends Object> parameters)
    {
        Div parentComponent = new Div();
        CockpitFCKEditor fckEditor = createCockpitFCKEditor(parameters);
        String fckToolbarConfig = (String)parameters.get("fckToolbarConfiguration");
        if(fckToolbarConfig != null)
        {
            fckEditor.setToolbarIcons(fckToolbarConfig);
        }
        else if(getDefaultToolbarConfig() != null)
        {
            fckEditor.setToolbarIcons(getDefaultToolbarConfig());
        }
        String width = (String)parameters.get("editorWidth");
        if(width == null)
        {
            width = "650px";
        }
        String height = (String)parameters.get("editorHeight");
        if(height == null)
        {
            height = "300px";
        }
        fckEditor.setWidth(width);
        fckEditor.setHeight(height);
        fckEditor.setValue(html);
        fckEditor.addEventListener("onChange", (EventListener)new Object(this, fckEditor, parameters, listener));
        if(isEditable())
        {
            parentComponent.appendChild((Component)fckEditor);
        }
        else
        {
            parentComponent.appendChild((Component)createReadonlyRepresentation(html, width, height));
        }
        return (HtmlBasedComponent)parentComponent;
    }


    protected Div createReadonlyRepresentation(String html, String width, String height)
    {
        Div readonlyDiv = new Div();
        readonlyDiv.setSclass("wysiwyg-readonly");
        readonlyDiv.setHeight(height);
        readonlyDiv.setWidth(width);
        Html contentHtml = new Html(html);
        readonlyDiv.appendChild((Component)contentHtml);
        return readonlyDiv;
    }


    @Deprecated
    protected Window createPopupWysiwyg(Textbox textbox, EditorListener listener)
    {
        return createPopupWysiwyg(textbox, listener, null);
    }


    protected Window createPopupWysiwyg(Textbox textbox, EditorListener listener, Map<String, ? extends Object> parameters)
    {
        Window ret = new Window();
        String label = Labels.getLabel("general.edit");
        if(!isEditable())
        {
            label = label + " - " + label;
            ret.setClosable(true);
        }
        ret.setTitle(label);
        ret.setShadow(false);
        Vbox box = new Vbox();
        ret.appendChild((Component)box);
        box.setHeights("none,25px");
        CockpitFCKEditor fckEditor = createCockpitFCKEditor(parameters);
        String fckToolbarConfig = (parameters == null) ? null : (String)parameters.get("fckToolbarConfiguration");
        if(fckToolbarConfig != null)
        {
            fckEditor.setToolbarIcons(fckToolbarConfig);
        }
        else if(getDefaultToolbarConfig() != null)
        {
            fckEditor.setToolbarIcons(getDefaultToolbarConfig());
        }
        fckEditor.setWidth("600px");
        fckEditor.setHeight("400px");
        if(isEditable())
        {
            fckEditor.setParent((Component)box);
        }
        else
        {
            Div readonlyDiv = createReadonlyRepresentation(textbox.getText(), "600px", "400px");
            readonlyDiv.setParent((Component)box);
        }
        fckEditor.setValue(textbox.getText());
        if(isEditable())
        {
            Div buttonFrame = new Div();
            buttonFrame.setSclass("wizardButtonFrame");
            Hbox buttonBox = new Hbox();
            buttonBox.setWidth("100%");
            Div leftDiv = new Div();
            Div rightDiv = new Div();
            rightDiv.setAlign("right");
            Checkbox checkbox = new Checkbox();
            buttonFrame.appendChild((Component)checkbox);
            checkbox.setStyle("left: -10000px; position: absolute;");
            Button okBtn = new Button(Labels.getLabel("general.ok"));
            okBtn.setTooltiptext(Labels.getLabel("general.ok"));
            okBtn.addEventListener("onClick", (EventListener)new Object(this, checkbox));
            okBtn.setAction("onclick: focusFck(document.getElementById('" + fckEditor.getUuid() + "'));");
            fckEditor.addEventListener("onUser", (EventListener)new Object(this, fckEditor, parameters, listener, textbox, ret));
            Button cancelBtn = new Button(Labels.getLabel("general.cancel"));
            cancelBtn.setSclass("cancelButton");
            cancelBtn.setTooltiptext(Labels.getLabel("general.cancel"));
            cancelBtn.addEventListener("onClick", (EventListener)new Object(this, ret, listener));
            UITools.applyTestID((Component)leftDiv, "wizardCancelButton");
            UITools.applyTestID((Component)rightDiv, "wizardDoneButton");
            leftDiv.appendChild((Component)cancelBtn);
            rightDiv.appendChild((Component)okBtn);
            buttonBox.appendChild((Component)leftDiv);
            buttonBox.appendChild((Component)rightDiv);
            buttonFrame.appendChild((Component)buttonBox);
            buttonFrame.setParent((Component)box);
        }
        return ret;
    }


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        if(parameters != null && !parameters.isEmpty())
        {
            this.inline = (parameters.get("inline") == null) ? false : Boolean.parseBoolean((String)parameters.get("inline"));
        }
        if(this.inline)
        {
            return createWysiwygInline((String)initialValue, listener, parameters);
        }
        String value = null;
        if(initialValue instanceof String)
        {
            value = (String)initialValue;
        }
        else if(initialValue != null)
        {
            LOG.error("Initial value not of type String.");
        }
        Div editorView = new Div();
        Textbox contentComponent = new Textbox(value);
        contentComponent.setRows(8);
        Object rows = (parameters == null) ? null : parameters.get("rows");
        if(rows instanceof String)
        {
            try
            {
                contentComponent.setRows(Integer.parseInt((String)rows));
            }
            catch(Exception e)
            {
                LOG.error(e.getMessage(), e);
            }
        }
        String attQual = (String)parameters.get("attributeQualifier");
        Object object1 = new Object(this, parameters, contentComponent, listener, editorView);
        if(isEditable())
        {
            contentComponent.setParent((Component)editorView);
            editorView.setSclass("wysiwygEditor");
            WysiwygButtonContainer container = createViewComponentInternal((InputElement)contentComponent, listener, parameters, (EventListener)object1);
            container.setParent((Component)editorView);
            setTestId((HtmlBasedComponent)editorView, attQual);
            return (HtmlBasedComponent)editorView;
        }
        Hbox editorContainer = new Hbox();
        editorContainer.setWidth("100%");
        editorContainer.setWidths(",22px");
        editorContainer.setParent((Component)editorView);
        Label label = new Label();
        label.setMaxlength(50);
        label.setValue(value);
        label.setParent((Component)editorContainer);
        Toolbarbutton btnPreview = new Toolbarbutton("", "/cockpit/images/icon_func_preview_available.png");
        btnPreview.setParent((Component)editorContainer);
        btnPreview.addEventListener("onClick", (EventListener)object1);
        setTestId((HtmlBasedComponent)editorView, attQual);
        return (HtmlBasedComponent)editorView;
    }


    private boolean isEmptyString(String convertedString, String regexp)
    {
        if(StringUtils.isBlank(regexp))
        {
            return convertedString.matches("^[\\s\\u00A0]*$");
        }
        return convertedString.matches(regexp);
    }


    public String getEditorType()
    {
        return "TEXT";
    }


    public boolean isInline()
    {
        return this.inline;
    }


    public void setFocus(HtmlBasedComponent rootEditorComponent, boolean selectAll)
    {
        Events.echoEvent("onClick", (Component)rootEditorComponent, null);
    }


    public String getDefaultToolbarConfig()
    {
        return UITools.getCockpitParameter("default.fckToolbarConfig", Executions.getCurrent());
    }


    private void setTestId(HtmlBasedComponent component, String attQual)
    {
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            String id = "Wysiwyg_";
            if(attQual != null)
            {
                attQual = attQual.replaceAll("\\W", "");
                id = id + id;
            }
            UITools.applyTestID((Component)component, id);
        }
    }


    private WysiwygButtonContainer createViewComponentInternal(InputElement inputElement, EditorListener listener, Map<String, ? extends Object> parameters, EventListener openListener)
    {
        parseInitialInputString(parameters);
        if(!isEditable())
        {
            if(Executions.getCurrent().getUserAgent().contains("MSIE"))
            {
                inputElement.setReadonly(true);
            }
            else
            {
                inputElement.setDisabled(true);
            }
        }
        this.inEditMode = false;
        WysiwygButtonContainer wysiwygButtonContainer = new WysiwygButtonContainer(this, (AbstractUIEditor.CancelListener)new Object(this, listener, inputElement), openListener);
        inputElement.addEventListener("onFocus", (EventListener)new Object(this, inputElement, wysiwygButtonContainer));
        inputElement.addEventListener("onBlur", (EventListener)new Object(this, inputElement, listener, wysiwygButtonContainer));
        inputElement.addEventListener("onChange", (EventListener)new Object(this, inputElement, listener));
        inputElement.addEventListener("onOK", (EventListener)new Object(this, inputElement, listener));
        inputElement.addEventListener("onCancel", (EventListener)new Object(this, inputElement, listener));
        return wysiwygButtonContainer;
    }
}
