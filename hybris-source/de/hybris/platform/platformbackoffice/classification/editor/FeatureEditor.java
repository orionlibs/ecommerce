package de.hybris.platform.platformbackoffice.classification.editor;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.UITools;
import de.hybris.platform.platformbackoffice.classification.ClassificationInfo;
import de.hybris.platform.platformbackoffice.classification.util.BackofficeClassificationUtils;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class FeatureEditor extends AbstractCockpitEditorRenderer<ClassificationInfo>
{
    public static final String CLASSIFICATION_INFO = "classificationInfo";
    protected static final String SCLASS_LABEL = "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-attrcell-label";
    protected static final String SCLASS_LABEL_WRAPPER = "yw-editorarea-label-container";
    protected static final String SCLASS_MANDATORY_ATTRIBUTE_LABEL = "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-attrcell-label-mandatory-attribute";


    public void render(Component parent, EditorContext<ClassificationInfo> context, EditorListener<ClassificationInfo> listener)
    {
        if(parent == null || context == null || listener == null)
        {
            return;
        }
        Div featureEditorContainer = new Div();
        parent.appendChild((Component)featureEditorContainer);
        ClassificationInfo initialValue = (ClassificationInfo)context.getInitialValue();
        String editorType = extractEmbeddedType(context);
        Editor subEditor = prepareEditor(context);
        if(!isFeatureLocalized(initialValue).booleanValue())
        {
            Div labelWrapper = new Div();
            UITools.addSClass((HtmlBasedComponent)labelWrapper, "yw-editorarea-label-container");
            featureEditorContainer.appendChild((Component)labelWrapper);
            Label label = createLabel(context);
            labelWrapper.appendChild((Component)label);
        }
        else
        {
            subEditor.setEditorLabel(context.getEditorLabel());
            subEditor.setTooltiptext(context.getTooltiptext());
        }
        featureEditorContainer.appendChild((Component)subEditor);
        subEditor.setReadableLocales(context.getReadableLocales());
        subEditor.setWritableLocales(context.getWritableLocales());
        subEditor.setType(editorType);
        subEditor.setReadOnly(!context.isEditable());
        subEditor.setOrdered(context.isOrdered());
        subEditor.setValue(initialValue.getValue());
        subEditor.setOptional(context.isOptional());
        subEditor.setWidgetInstanceManager((WidgetInstanceManager)context.getParameter("wim"));
        subEditor.addEventListener("onValueChanged", event -> onValueChanged(subEditor, initialValue, listener));
        subEditor.addParameter("classificationInfo", initialValue);
        subEditor.addParameters(context.getParameters());
        subEditor.afterCompose();
    }


    protected Editor prepareEditor(EditorContext<ClassificationInfo> context)
    {
        return new Editor();
    }


    protected void preventSubEditorLabelCreation(Editor subEditor)
    {
        subEditor.setEditorLabel(null);
        subEditor.setTooltiptext(null);
    }


    protected Label createLabel(EditorContext<ClassificationInfo> context)
    {
        Label label = new Label(context.getEditorLabel());
        label.setSclass("yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-attrcell-label");
        label.setTooltiptext(context.getTooltiptext());
        if(context.isOptional())
        {
            UITools.modifySClass((HtmlBasedComponent)label, "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-attrcell-label", true);
        }
        else
        {
            UITools.modifySClass((HtmlBasedComponent)label, "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-attrcell-label-mandatory-attribute", true);
        }
        return label;
    }


    protected Boolean isFeatureLocalized(ClassificationInfo initialValue)
    {
        return initialValue.getAssignment().getLocalized();
    }


    protected void onValueChanged(Editor subEditor, ClassificationInfo initialValue, EditorListener<ClassificationInfo> listener)
    {
        Object value = subEditor.getValue();
        if(initialValue.isLocalized())
        {
            Map<Locale, Object> localizedValue = (Map<Locale, Object>)value;
            if(localizedValue != null)
            {
                value = localizedValue.entrySet().stream().filter(entry -> (entry.getValue() != null)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            }
        }
        listener.onValueChanged(new ClassificationInfo(initialValue.getAssignment(), value));
    }


    protected String extractEmbeddedType(EditorContext<ClassificationInfo> context)
    {
        ClassificationInfo value = (ClassificationInfo)context.getInitialValue();
        return BackofficeClassificationUtils.getType(value);
    }
}
