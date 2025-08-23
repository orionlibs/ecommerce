package de.hybris.platform.cockpit.model.editor.impl;

import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.meta.EditorFactory;
import de.hybris.platform.cockpit.model.meta.PropertyEditorDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.meta.impl.ClassAttributePropertyDescriptor;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.search.impl.ClassAttributeSearchDescriptor;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Hbox;

public abstract class AbstractFeatureUIEditor extends AbstractUIEditor
{
    protected ClassAttributePropertyDescriptor propertyDescriptor;
    protected ClassAttributeAssignmentModel assignment;
    private TypeService typeService;
    private EditorFactory editorFactory;
    private ModelService modelService;
    private ClassificationService classificationService;
    protected FeatureValue featureValue = null;
    protected UIEditor editor = null;
    protected HtmlBasedComponent editorViewComponent = (HtmlBasedComponent)new Hbox();
    protected DefaultFeatureUnitUIEditor unitEditor = null;


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        Hbox hbox = new Hbox();
        ClassAttributePropertyDescriptor property = extractPropertyDescriptor(parameters);
        if(property != null)
        {
            setPropertyDescriptor(property);
            setAssignment((ClassAttributeAssignmentModel)getModelService().get(property.getAttributeAssignment()));
        }
        if(initialValue instanceof FeatureValue)
        {
            this.featureValue = (FeatureValue)initialValue;
        }
        return (HtmlBasedComponent)hbox;
    }


    protected ClassAttributePropertyDescriptor extractPropertyDescriptor(Map<String, ? extends Object> parameters)
    {
        Object propObject = parameters.get("propertyDescriptor");
        if(propObject instanceof ClassAttributePropertyDescriptor)
        {
            return (ClassAttributePropertyDescriptor)propObject;
        }
        if(propObject instanceof ClassAttributeSearchDescriptor)
        {
            return ((ClassAttributeSearchDescriptor)propObject).getPropertyDescriptor();
        }
        return null;
    }


    protected DefaultFeatureUnitUIEditor createUnitUIEditor()
    {
        DefaultFeatureUnitUIEditor editor = new DefaultFeatureUnitUIEditor();
        if(getAssignment() != null)
        {
            List<Object> selectable = new ArrayList();
            selectable.add("");
            if(this.assignment.getUnit() == null)
            {
                selectable.addAll(getTypeService().wrapItems(
                                getClassificationService().getAttributeUnits(this.assignment.getSystemVersion())));
            }
            else
            {
                selectable.add(getTypeService().wrapItem(getAssignment().getUnit()));
                selectable.addAll(getTypeService().wrapItems(getAssignment().getUnit().getConvertibleUnits()));
            }
            editor.setAvailableValues(selectable);
        }
        return editor;
    }


    protected UIEditor getEditor(ClassAttributeAssignmentModel assignment)
    {
        UIEditor editor = null;
        String editorType = getInternalEditorType(assignment);
        Collection<PropertyEditorDescriptor> matching = getEditorFactory().getMatchingEditorDescriptors(editorType);
        PropertyEditorDescriptor edDescr = matching.isEmpty() ? null : matching.iterator().next();
        if(edDescr != null)
        {
            editor = edDescr.createUIEditor();
        }
        return editor;
    }


    private String getInternalEditorType(ClassAttributeAssignmentModel assignment)
    {
        String editorType = null;
        if(assignment != null)
        {
            ClassificationAttributeTypeEnum type = assignment.getAttributeType();
            String code = type.getCode();
            if("string".equals(code))
            {
                editorType = "TEXT";
            }
            else if("number".equals(code))
            {
                editorType = "DECIMAL";
            }
            else if("boolean".equals(code))
            {
                editorType = "BOOLEAN";
            }
            else if("date".equals(code))
            {
                editorType = "DATE";
            }
            else if("enum".equals(code))
            {
                editorType = "ENUM";
            }
            else
            {
                editorType = "DUMMY";
            }
        }
        return editorType;
    }


    public String getEditorType()
    {
        return "FEATURE";
    }


    protected Object getEditorInitialValue(Object initialValue)
    {
        FeatureValue featureValue = null;
        if(initialValue instanceof FeatureValue)
        {
            featureValue = (FeatureValue)initialValue;
        }
        if(featureValue != null && "ENUM".equals(getInternalEditorType(this.assignment)))
        {
            return this.typeService.wrapItem(featureValue.getValue());
        }
        if(featureValue != null)
        {
            return featureValue.getValue();
        }
        return initialValue;
    }


    protected EditorListener getEditorListener(EditorListener listener)
    {
        return (EditorListener)new Object(this, listener);
    }


    public void setFocus(HtmlBasedComponent rootEditorComponent, boolean selectAll)
    {
        this.editor.setFocus(this.editorViewComponent, selectAll);
    }


    public void setValue(Object value, Object unitValue)
    {
        Object valueObject = (value instanceof TypedObject) ? ((TypedObject)value).getObject() : value;
        Object unitValueObject = (unitValue instanceof TypedObject) ? ((TypedObject)unitValue).getObject() : null;
        ClassificationAttributeUnitModel unitModel = (unitValueObject instanceof ClassificationAttributeUnitModel) ? (ClassificationAttributeUnitModel)unitValueObject : null;
        if(valueObject == null)
        {
            this.featureValue = null;
        }
        else if(this.featureValue == null)
        {
            this.featureValue = new FeatureValue(valueObject, null, unitModel);
        }
        else
        {
            boolean valueModified = (this.featureValue.getValue() != valueObject && (this.featureValue.getValue() == null || !this.featureValue.getValue().equals(valueObject)));
            boolean unitModified = (this.featureValue.getUnit() != unitValueObject && (this.featureValue.getUnit() == null || !this.featureValue.getUnit().equals(unitValueObject)));
            if(valueModified || unitModified)
            {
                this.featureValue = new FeatureValue(valueObject, this.featureValue.getDescription(), unitModel);
            }
        }
    }


    public ClassAttributeAssignmentModel getAssignment()
    {
        return this.assignment;
    }


    public void setAssignment(ClassAttributeAssignmentModel assignment)
    {
        this.assignment = assignment;
    }


    public ClassAttributePropertyDescriptor getPropertyDescriptor()
    {
        return this.propertyDescriptor;
    }


    public void setPropertyDescriptor(ClassAttributePropertyDescriptor propertyDescriptor)
    {
        this.propertyDescriptor = propertyDescriptor;
    }


    public TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }


    protected boolean isNumber()
    {
        if(getAssignment() != null)
        {
            return getAssignment().getAttributeType().equals(ClassificationAttributeTypeEnum.NUMBER);
        }
        return false;
    }


    public boolean supportUnits()
    {
        if(getAssignment() != null)
        {
            ClassificationAttributeTypeEnum type = this.assignment.getAttributeType();
            String code = type.getCode();
            if("string".equals(code))
            {
                return false;
            }
            if("number".equals(code))
            {
                return true;
            }
            if("boolean".equals(code))
            {
                return false;
            }
            if("date".equals(code))
            {
                return false;
            }
            if("enum".equals(code))
            {
                return false;
            }
        }
        return false;
    }


    public void setEditorFactory(EditorFactory editorFactory)
    {
        this.editorFactory = editorFactory;
    }


    public EditorFactory getEditorFactory()
    {
        if(this.editorFactory == null)
        {
            this.editorFactory = (EditorFactory)SpringUtil.getBean("EditorFactory");
        }
        return this.editorFactory;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public ModelService getModelService()
    {
        if(this.modelService == null)
        {
            this.modelService = (ModelService)SpringUtil.getBean("modelService");
        }
        return this.modelService;
    }


    public ClassificationService getClassificationService()
    {
        if(this.classificationService == null)
        {
            this.classificationService = (ClassificationService)SpringUtil.getBean("classificationService");
        }
        return this.classificationService;
    }


    public void setClassificationService(ClassificationService classificationService)
    {
        this.classificationService = classificationService;
    }
}
