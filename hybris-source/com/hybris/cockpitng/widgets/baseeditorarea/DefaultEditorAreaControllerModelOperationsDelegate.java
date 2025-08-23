/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.baseeditorarea;

import com.hybris.cockpitng.common.model.ObjectWithComponentContext;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.EditorArea;
import com.hybris.cockpitng.core.model.StandardModelKeys;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.validation.model.ValidationResult;
import com.hybris.cockpitng.widgets.util.impl.DefaultReferenceModelProperties;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

public class DefaultEditorAreaControllerModelOperationsDelegate
{
    private static final String SETTING_VIEW_MODE_BTN_VISIBLE = "viewModeBtnVisible";
    private final WidgetInstanceManager widgetInstanceManager;


    public DefaultEditorAreaControllerModelOperationsDelegate(final DefaultEditorAreaController controller)
    {
        widgetInstanceManager = controller.getWidgetInstanceManager();
    }


    public String getCurrentContext()
    {
        return getValue(DefaultEditorAreaController.MODEL_EDITOR_AREA_CONFIG_CTX, String.class);
    }


    public void setCurrentContext(final String configCtx)
    {
        setValue(DefaultEditorAreaController.MODEL_EDITOR_AREA_CONFIG_CTX, configCtx);
    }


    public String getDefaultContext()
    {
        return getWidgetInstanceManager().getWidgetSettings().getString(DefaultEditorAreaController.SETTING_CONFIGURATION_CONTEXT);
    }


    public void initContext()
    {
        if(StringUtils.isBlank(getCurrentContext()))
        {
            setCurrentContext(getDefaultContext());
        }
    }


    public DataType getCurrentType()
    {
        return getValue(DefaultEditorAreaController.MODEL_CURRENT_OBJECT_TYPE, DataType.class);
    }


    public void setCurrentType(final DataType type)
    {
        setValue(DefaultEditorAreaController.MODEL_CURRENT_OBJECT_TYPE, type);
    }


    /**
     * @deprecated since=2005, use {@link #retrieveInputObject(Object)} and {@link #retrieveAndSetCurrentContext(Object)}
     */
    @Deprecated(since = "2005", forRemoval = true)
    public Object setCurrentTypeAndRetrieveInputObject(final Object inputData)
    {
        retrieveAndSetCurrentContext(inputData);
        return retrieveInputObject(inputData);
    }


    public Object retrieveInputObject(final Object inputData)
    {
        return (inputData instanceof ObjectWithComponentContext) ? ((ObjectWithComponentContext)inputData).getInputObject()
                        : inputData;
    }


    public String retrieveAndSetCurrentContext(final Object inputData)
    {
        if(inputData instanceof ObjectWithComponentContext)
        {
            final ObjectWithComponentContext context = (ObjectWithComponentContext)inputData;
            setCurrentContext(StringUtils.defaultIfBlank(context.getComponentContext(), getDefaultContext()));
        }
        else
        {
            setCurrentContext(getDefaultContext());
        }
        return getCurrentContext();
    }


    public void onTypeChange(final DataType genericType)
    {
        setCurrentType(genericType);
        setValue(DefaultEditorAreaController.MODEL_LAST_PINNED_TAB, null);
    }


    public void resetValueChangeState()
    {
        setValue(DefaultEditorAreaController.MODEL_INPUT_OBJECT_IS_NEW, Boolean.FALSE);
        setValue(DefaultEditorAreaController.MODEL_INPUT_OBJECT_IS_MODIFIED, Boolean.FALSE);
        setValue(DefaultEditorAreaController.MODEL_VALUE_CHANGED, Boolean.FALSE);
    }


    public void setValueChangeState(final boolean inputObjectIsNew, final boolean inputObjectIsModified)
    {
        setValue(DefaultEditorAreaController.MODEL_VALUE_CHANGED, Boolean.TRUE);
        setValue(DefaultEditorAreaController.MODEL_INPUT_OBJECT_IS_NEW, Boolean.valueOf(inputObjectIsNew));
        setValue(DefaultEditorAreaController.MODEL_INPUT_OBJECT_IS_MODIFIED, Boolean.valueOf(inputObjectIsModified));
    }


    public Object getCurrentObject()
    {
        return getValue(DefaultEditorAreaController.MODEL_CURRENT_OBJECT, Object.class);
    }


    public String getFluidViewModeCode()
    {
        return getWidgetInstanceManager().getWidgetSettings().getString(DefaultEditorAreaController.SETTING_FLUID_VIEW_CODE);
    }


    public boolean isFluidViewMode()
    {
        return BooleanUtils.isTrue(getValue(DefaultEditorAreaController.MODEL_FLUID_VIEW_MODE, Boolean.class));
    }


    public boolean isViewModeButtonVisible()
    {
        return BooleanUtils.isTrue(getWidgetInstanceManager().getWidgetSettings().getBoolean(SETTING_VIEW_MODE_BTN_VISIBLE));
    }


    public void initFluidViewModeIfNeeded(final String initViewMode)
    {
        if(getValue(DefaultEditorAreaController.MODEL_FLUID_VIEW_MODE, Boolean.class) == null)
        {
            setValue(DefaultEditorAreaController.MODEL_FLUID_VIEW_MODE,
                            Boolean.valueOf(StringUtils.equals(getFluidViewModeCode(), initViewMode)));
        }
    }


    public void changeViewMode()
    {
        final Boolean fluidMode = getValue(DefaultEditorAreaController.MODEL_FLUID_VIEW_MODE, Boolean.class);
        if(fluidMode != null)
        {
            setValue(DefaultEditorAreaController.MODEL_FLUID_VIEW_MODE, BooleanUtils.negate(fluidMode));
        }
    }


    public void prepareValidationResultModel()
    {
        ValidationResult validationResultToSet = getValue(StandardModelKeys.VALIDATION_RESULT_KEY, ValidationResult.class);
        if(validationResultToSet == null)
        {
            validationResultToSet = new ValidationResult();
            setValue(StandardModelKeys.VALIDATION_RESULT_KEY, validationResultToSet);
        }
    }


    public ValidationResult getCurrentValidationResult()
    {
        return getValue(StandardModelKeys.VALIDATION_RESULT_KEY, ValidationResult.class);
    }


    public void resetModel()
    {
        setValue(DefaultEditorAreaController.MODEL_INPUT_OBJECT_IS_NEW, null);
        setValue(DefaultEditorAreaController.MODEL_INPUT_OBJECT_IS_MODIFIED, null);
        setValue(DefaultEditorAreaController.MODEL_CURRENT_OBJECT, null);
        setValue(DefaultEditorAreaController.MODEL_CURRENT_OBJECT_TYPE, null);
        setCurrentType(null);
        setValue(DefaultReferenceModelProperties.MODEL_ALL_REFERENCED_OBJECTS, null);
        setValue(DefaultEditorAreaController.MODEL_VALUE_CHANGED, Boolean.FALSE);
        setValue(DefaultEditorAreaController.MODEL_FLUID_VIEW_MODE, null);
        setValue(StandardModelKeys.VALIDATION_RESULT_KEY, new ValidationResult());
        setValue(DefaultEditorAreaController.MODEL_EDITOR_INPUT_DATA, null);
    }


    public void setCurrentConfiguration(final EditorArea configuration)
    {
        setValue(DefaultEditorAreaController.EDITOR_AREA_CONFIGURATION, configuration);
    }


    private void setValue(final String key, final Object value)
    {
        getWidgetInstanceManager().getModel().setValue(key, value);
    }


    private <T> T getValue(final String expression, final Class<T> valueType)
    {
        return getWidgetInstanceManager().getModel().getValue(expression, valueType);
    }


    protected WidgetInstanceManager getWidgetInstanceManager()
    {
        return widgetInstanceManager;
    }
}
