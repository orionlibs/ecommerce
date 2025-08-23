/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.defaultclass;

import com.hybris.cockpitng.core.util.ClassLoaderUtils;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import java.text.MessageFormat;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.mesg.MZul;

/**
 * Editor for properties of type java.lang.Class
 */
public class DefaultClassEditor extends AbstractCockpitEditorRenderer<Class>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultClassEditor.class);
    protected static final Pattern CLASS_NAME_PATTERN = Pattern.compile("([\\w]+\\.)*+[\\w]+");
    protected Textbox textbox;


    @Override
    public void render(final Component parent, final EditorContext<Class> context, final EditorListener<Class> editorListener)
    {
        Validate.notNull("All parameters are mandatory", parent, context, editorListener);
        textbox = new Textbox();
        textbox.setParent(parent);
        textbox.setInstant(false);
        final Class currentClass = context.getInitialValue();
        textbox.setText(currentClass == null ? StringUtils.EMPTY : currentClass.getCanonicalName());
        final boolean isOptional = context.isOptional();
        textbox.setConstraint(new Constraint()
        {
            @Override
            public void validate(final Component comp, final Object value)
            {
                final String text = (String)value;
                if(StringUtils.isNotEmpty(text))
                {
                    if(!isValidClassName(text))
                    {
                        final String msg = getL10nDecorator(context, "defaultclasseditor.classnotfound",
                                        "defaultclasseditor.classnotfound");
                        final String formattedMessage = MessageFormat.format(msg, text);
                        throw new WrongValueException(comp, formattedMessage);
                    }
                }
                else if(!isOptional)
                {
                    throw new WrongValueException(comp, MZul.EMPTY_NOT_ALLOWED);
                }
            }
        });
        textbox.addEventListener(Events.ON_CHANGING, new EventListener<InputEvent>()
        {
            @Override
            public void onEvent(final InputEvent event)
            {
                final String cname = event.getValue();
                if(isValidClassName(cname))
                {
                    editorListener.onValueChanged(loadClass(cname));
                }
                else if(StringUtils.isBlank(cname) && isOptional)
                {
                    editorListener.onValueChanged(null);
                }
            }
        });
        textbox.setReadonly(!context.isEditable());
    }


    private boolean isValidClassName(final String text)
    {
        if(StringUtils.isNotEmpty(text) && CLASS_NAME_PATTERN.matcher(text).matches())
        {
            return loadClass(text) != null;
        }
        return false;
    }


    protected Class loadClass(final String className)
    {
        try
        {
            return ClassLoaderUtils.getCurrentClassLoader(this.getClass()).loadClass(className);
        }
        catch(final ClassNotFoundException ex)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Error while loading class", ex);
            }
            return null;
        }
    }
}
