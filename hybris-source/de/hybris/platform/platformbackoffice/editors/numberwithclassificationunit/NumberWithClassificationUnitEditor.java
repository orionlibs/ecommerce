package de.hybris.platform.platformbackoffice.editors.numberwithclassificationunit;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.YTestTools;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.classification.ClassificationSystemService;
import de.hybris.platform.platformbackoffice.services.ClassificationAttributeAssignmentService;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Resource;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

public class NumberWithClassificationUnitEditor extends AbstractCockpitEditorRenderer<Number>
{
    public static final String YW_FEATURE_VALUE_CONTAINER_WITH_UNIT = "yw-feature-value-container-with-unit";
    protected static final String CLASSIFICATION_ATTRIBUTE_QUALIFIER = "classificationAttributeQualifier";
    protected static final String CATALOG_ID = "catalogId";
    protected static final String SYSTEM_VERSION = "systemVersion";
    protected static final String CLASSIFICATION_CLASS_CODE = "classificationClassCode";
    protected static final String CLASSIFICATION_ATTRIBUTE_CODE = "classificationAttributeCode";
    protected static final String CLASSIFICATION_ATTRIBUTE_UNIT_CODE = "classificationAttributeUnitCode";
    @Resource
    private ClassificationAttributeAssignmentService classificationAttributeAssignmentService;
    @Resource
    private ClassificationSystemService classificationSystemService;
    @Resource
    private LabelService labelService;


    public void render(Component parent, EditorContext context, EditorListener listener)
    {
        Validate.notNull("parent component can not be null", new Object[] {parent});
        Validate.notNull("context can not be null", new Object[] {context});
        Div container = new Div();
        YTestTools.modifyYTestId((Component)container, "yw-number-with-classification-unit");
        UITools.modifySClass((HtmlBasedComponent)container, "yw-feature-value-container-with-unit", true);
        parent.appendChild((Component)container);
        ClassificationAttributeUnitModel baseUnit = findClassificationUnit(context);
        Collection<ClassificationAttributeUnitModel> convertibleUnits = findAllConvertibleUnits(baseUnit);
        Editor valueEditor = renderValueInput((Component)container, context);
        Combobox unitComboBox = renderUnitComboBox((Component)container, baseUnit, convertibleUnits);
        valueEditor.addEventListener("onValueChanged", event -> listener.onValueChanged(onValueChange(valueEditor, unitComboBox)));
        unitComboBox.addEventListener("onChange", event -> listener.onValueChanged(onValueChange(valueEditor, unitComboBox)));
    }


    protected Editor renderValueInput(Component parent, EditorContext context)
    {
        Editor subEditor = new Editor();
        YTestTools.modifyYTestId((Component)subEditor, "yw-number-with-classification-unit-editor");
        subEditor.setType(context.getValueType());
        subEditor.setReadOnly(!context.isEditable());
        subEditor.setOptional(context.isOptional());
        subEditor.setOrdered(context.isOrdered());
        subEditor.setValue(context.getInitialValue());
        subEditor.addParameters(context.getParameters());
        subEditor.afterCompose();
        parent.appendChild((Component)subEditor);
        return subEditor;
    }


    protected Combobox renderUnitComboBox(Component parent, ClassificationAttributeUnitModel baseUnit, Collection<ClassificationAttributeUnitModel> units)
    {
        Div selectorContainer = new Div();
        Combobox unitCombobox = new Combobox();
        YTestTools.modifyYTestId((Component)unitCombobox, "yw-number-with-classification-unit-combobox");
        unitCombobox.setReadonly(true);
        ListModelList<ClassificationAttributeUnitModel> model = new ListModelList();
        model.add(baseUnit);
        model.addAll(units);
        unitCombobox.setModel((ListModel)model);
        model.setSelection(Collections.singletonList(baseUnit));
        unitCombobox.setItemRenderer((comboItem, unit, position) -> {
            comboItem.setValue(unit);
            comboItem.setLabel(getLabelService().getObjectLabel(unit));
        });
        selectorContainer.appendChild((Component)unitCombobox);
        parent.appendChild((Component)selectorContainer);
        return unitCombobox;
    }


    protected ClassificationAttributeUnitModel findClassificationUnit(EditorContext context)
    {
        if(context.getParameter("catalogId") != null && context.getParameter("systemVersion") != null && context
                        .getParameter("classificationClassCode") != null && context
                        .getParameter("classificationAttributeCode") != null)
        {
            ClassAttributeAssignmentModel classAttributeAssignmentModel = getClassificationAttributeAssignmentService().findClassAttributeAssignment(context.getParameter("catalogId").toString(), context
                            .getParameter("systemVersion").toString(), context.getParameter("classificationClassCode").toString(), context
                            .getParameter("classificationAttributeCode").toString());
            if(classAttributeAssignmentModel != null)
            {
                return classAttributeAssignmentModel.getUnit();
            }
        }
        if(context.getParameter("catalogId") != null && context.getParameter("systemVersion") != null && context
                        .getParameter("classificationAttributeUnitCode") != null)
        {
            ClassificationSystemVersionModel systemVersion = getClassificationSystemService().getSystemVersion(context.getParameter("catalogId").toString(), context.getParameter("systemVersion").toString());
            return getClassificationSystemService().getAttributeUnitForCode(systemVersion, context
                            .getParameter("classificationAttributeUnitCode").toString());
        }
        if(context.getParameter("classificationAttributeQualifier") != null)
        {
            ClassAttributeAssignmentModel classAttributeAssignmentModel = getClassificationAttributeAssignmentService().findClassAttributeAssignment(context.getParameter("classificationAttributeQualifier").toString());
            if(classAttributeAssignmentModel != null)
            {
                return classAttributeAssignmentModel.getUnit();
            }
        }
        return null;
    }


    protected Number onValueChange(Editor valueEditor, Combobox unitCombobox)
    {
        if(valueEditor.getValue() instanceof Number && isUnitSelected(unitCombobox))
        {
            double value = ((Number)valueEditor.getValue()).doubleValue();
            double conversionFactor = ((ClassificationAttributeUnitModel)unitCombobox.getSelectedItem().getValue()).getConversionFactor().doubleValue();
            return Double.valueOf(value * conversionFactor);
        }
        return Integer.valueOf(0);
    }


    protected boolean isUnitSelected(Combobox unitCombobox)
    {
        return (unitCombobox.getSelectedItem() != null && unitCombobox.getSelectedItem().getValue() != null && unitCombobox
                        .getSelectedItem().getValue() instanceof ClassificationAttributeUnitModel);
    }


    protected Collection<ClassificationAttributeUnitModel> findAllConvertibleUnits(ClassificationAttributeUnitModel baseUnit)
    {
        return this.classificationSystemService.getConvertibleUnits(baseUnit);
    }


    public ClassificationAttributeAssignmentService getClassificationAttributeAssignmentService()
    {
        return this.classificationAttributeAssignmentService;
    }


    public ClassificationSystemService getClassificationSystemService()
    {
        return this.classificationSystemService;
    }


    public LabelService getLabelService()
    {
        return this.labelService;
    }
}
