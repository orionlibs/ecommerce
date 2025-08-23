/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.baseeditorarea;

import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectSavingException;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.validation.ValidationContext;
import com.hybris.cockpitng.validation.ValidationHandler;
import com.hybris.cockpitng.validation.model.ValidationInfo;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class DefaultEditorAreaLogicHandler implements EditorAreaLogicHandler
{
    private ObjectFacade objectFacade;
    private ValidationHandler validationHandler;


    @Override
    public Object performSave(final WidgetInstanceManager widgetInstanceManager, final Object currentObject)
                    throws ObjectSavingException
    {
        final Context ctx = new DefaultContext();
        ctx.addAttribute(ObjectFacade.CTX_PARAM_SUPPRESS_EVENT, Boolean.TRUE);
        return this.objectFacade.save(currentObject, ctx);
    }


    @Override
    public Object performRefresh(final WidgetInstanceManager widgetInstanceManager, final Object currentObject)
                    throws ObjectNotFoundException
    {
        return objectFacade.reload(currentObject);
    }


    @Override
    public List<ValidationInfo> performValidation(final WidgetInstanceManager widgetInstanceManager, final Object currentObject,
                    final ValidationContext validationContext)
    {
        return getValidationHandler().validate(currentObject, validationContext);
    }


    @Override
    public List<ValidationInfo> performValidation(final WidgetInstanceManager widgetInstanceManager, final Object currentObject,
                    final List<String> qualifiers, final ValidationContext validationContext)
    {
        return getValidationHandler().validate(currentObject, qualifiers, validationContext);
    }


    @Override
    public void beforeEditorAreaRender(final WidgetInstanceManager widgetInstanceManager, final Object currentObject)
    {
        //NOOP
    }


    protected ObjectFacade getObjectFacade()
    {
        return objectFacade;
    }


    @Required
    public void setObjectFacade(final ObjectFacade objectFacade)
    {
        this.objectFacade = objectFacade;
    }


    public ValidationHandler getValidationHandler()
    {
        return validationHandler;
    }


    @Required
    public void setValidationHandler(ValidationHandler validationHandler)
    {
        this.validationHandler = validationHandler;
    }
}
