package de.hybris.platform.platformbackoffice.classification.editor;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.YTestTools;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.classification.ClassificationSystemService;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.platformbackoffice.classification.ClassificationInfo;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

public class FeatureValueEditor extends AbstractCockpitEditorRenderer<FeatureValue>
{
    public static final String YW_FEATURE_VALUE_CONTAINER_WITH_UNIT = "yw-feature-value-container-with-unit";
    public static final String YW_FEATURE_VALUE_UNIT_CONTAINER = "yw-feature-value-unit-container";
    public static final String YW_FEATURE_VALUE_CONTAINER = "yw-feature-value-container";
    public static final String YW_FEATURE_VALUE_SUBEDITOR = "yw-feature-value-subeditor";
    protected static final ClassificationAttributeUnitModel NULL_UNIT = new ClassificationAttributeUnitModel();
    private static final String SCLASS_CELL_LABEL = "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-attrcell-label";
    private static final String SCLASS_MANDATORY_ATTRIBUTE_LABEL = "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-attrcell-label-mandatory-attribute";
    @Resource
    private transient LabelService labelService;
    @Resource
    private transient ClassificationSystemService classificationSystemService;


    public void render(Component parent, EditorContext<FeatureValue> context, EditorListener<FeatureValue> listener)
    {
        Div container = new Div();
        container.setSclass("yw-feature-value-container");
        ClassificationInfo info = (ClassificationInfo)context.getParameter("classificationInfo");
        Editor subEditor = prepareEditor(context);
        subEditor.setType(extractEmbeddedType(context));
        subEditor.setReadOnly(!context.isEditable());
        subEditor.setOptional(context.isOptional());
        subEditor.setOrdered(context.isOrdered());
        Object initialValue = extractValue((FeatureValue)context.getInitialValue());
        ClassificationAttributeUnitModel initialUnit = extractInitialUnit((FeatureValue)context.getInitialValue(), info);
        subEditor.setValue(initialValue);
        subEditor.addParameters(context.getParameters());
        UITools.addSClass((HtmlBasedComponent)subEditor, "yw-feature-value-subeditor");
        subEditor.afterCompose();
        ListenerWrapper listenerWrapper = new ListenerWrapper(listener, initialValue, initialUnit);
        subEditor.addEventListener("onValueChanged", event -> listenerWrapper.onValueChanged(subEditor.getValue()));
        container.appendChild((Component)createEditorLabel(context));
        container.appendChild((Component)subEditor);
        if(info.isUnitDisplayed() && !subEditor.isFallbackEditorRendered())
        {
            UITools.modifySClass((HtmlBasedComponent)container, "yw-feature-value-container-with-unit", true);
            Div selecttorContainer = new Div();
            selecttorContainer.setSclass("yw-feature-value-unit-container");
            Combobox unitSelector = prepareUnitSelector(info, initialUnit, context);
            unitSelector.addEventListener("onSelect", event -> {
                Comboitem selectedItem = unitSelector.getSelectedItem();
                if(selectedItem != null && !NULL_UNIT.equals(selectedItem.getValue()))
                {
                    listenerWrapper.onUnitChanged((ClassificationAttributeUnitModel)selectedItem.getValue());
                }
                else
                {
                    listenerWrapper.onUnitChanged(null);
                }
            });
            selecttorContainer.appendChild((Component)unitSelector);
            container.appendChild((Component)selecttorContainer);
        }
        parent.appendChild((Component)container);
    }


    protected Editor prepareEditor(EditorContext<FeatureValue> context)
    {
        return new Editor();
    }


    protected Div createEditorLabel(EditorContext editorContext)
    {
        Div wrapperCaption = new Div();
        wrapperCaption.setSclass("yw-editorarea-label-container");
        if(StringUtils.isNotBlank(editorContext.getEditorLabel()))
        {
            Label label = new Label(editorContext.getEditorLabel());
            label.setParent((Component)wrapperCaption);
            if(editorContext.isOptional())
            {
                UITools.modifySClass((HtmlBasedComponent)label, "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-attrcell-label", true);
            }
            else
            {
                UITools.modifySClass((HtmlBasedComponent)label, "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-attrcell-label-mandatory-attribute", true);
            }
            setTooltip(editorContext, label);
        }
        return wrapperCaption;
    }


    private void setTooltip(EditorContext editorContext, Label label)
    {
        Object tooltip = editorContext.getParameter("headerLabelTooltip");
        if(tooltip instanceof String && StringUtils.isNotBlank((String)tooltip))
        {
            label.setTooltiptext((String)tooltip);
        }
    }


    private Combobox prepareUnitSelector(ClassificationInfo info, ClassificationAttributeUnitModel initialUnit, EditorContext<FeatureValue> editorContext)
    {
        List<ClassificationAttributeUnitModel> selection;
        Combobox selector = new Combobox();
        ListModelList<ClassificationAttributeUnitModel> model = new ListModelList();
        model.add(NULL_UNIT);
        model.addAll(findAllApplicableUnits(info));
        if(initialUnit == null)
        {
            selection = Collections.singletonList(NULL_UNIT);
        }
        else
        {
            selection = Collections.singletonList(initialUnit);
        }
        model.setSelection(selection);
        selector.setModel((ListModel)model);
        selector.setMultiline(false);
        selector.setReadonly(true);
        selector.setDisabled(!editorContext.isEditable());
        selector.setAutocomplete(true);
        selector.setAutodrop(true);
        YTestTools.modifyYTestId((Component)selector, "unitCombobox");
        selector.setItemRenderer((ComboitemRenderer)new Object(this));
        return selector;
    }


    private Collection<ClassificationAttributeUnitModel> findAllApplicableUnits(ClassificationInfo info)
    {
        if(info.getAssignment().getUnit() != null && info.getAssignment().getUnit().getUnitType() != null)
        {
            return this.classificationSystemService.getUnitsOfTypeForSystemVersion(info
                            .getAssignment().getClassificationAttribute().getSystemVersion(), info
                            .getAssignment().getUnit().getUnitType());
        }
        return this.classificationSystemService.getAttributeUnitsForSystemVersion(info.getAssignment().getSystemVersion());
    }


    private ClassificationAttributeUnitModel extractInitialUnit(FeatureValue initialValue, ClassificationInfo info)
    {
        if(initialValue == null)
        {
            return info.getAssignment().getUnit();
        }
        return initialValue.getUnit();
    }


    private Object extractValue(FeatureValue initialValue)
    {
        return (initialValue == null) ? null : initialValue.getValue();
    }
}
