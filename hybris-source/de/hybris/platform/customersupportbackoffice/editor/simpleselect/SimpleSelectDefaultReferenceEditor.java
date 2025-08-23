package de.hybris.platform.customersupportbackoffice.editor.simpleselect;

import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.CockpitConfigurationNotFoundException;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Base;
import com.hybris.cockpitng.editor.commonreferenceeditor.AbstractReferenceEditor;
import com.hybris.cockpitng.editor.commonreferenceeditor.ReferenceEditorLayout;
import com.hybris.cockpitng.editor.defaultreferenceeditor.DefaultReferenceEditor;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleSelectDefaultReferenceEditor<T> extends DefaultReferenceEditor<T>
{
    private static final Logger LOG = LoggerFactory.getLogger(SimpleSelectDefaultReferenceEditor.class);


    public ReferenceEditorLayout<T> createReferenceLayout(EditorContext context)
    {
        SimpleSelectLayout simpleSelectLayout = new SimpleSelectLayout((AbstractReferenceEditor)this, loadBaseConfiguration(getTypeCode(), (WidgetInstanceManager)context
                        .getParameter("wim")));
        simpleSelectLayout.setPlaceholderKey(getPlaceholderKey());
        return (ReferenceEditorLayout<T>)simpleSelectLayout;
    }


    protected Base loadBaseConfiguration(String typeCode, WidgetInstanceManager wim)
    {
        Base config = null;
        DefaultConfigContext configContext = new DefaultConfigContext("base");
        configContext.setType(typeCode);
        try
        {
            config = (Base)wim.loadConfiguration((ConfigContext)configContext, Base.class);
            if(config == null)
            {
                LOG.warn("Loaded UI configuration is null. Ignoring.");
            }
        }
        catch(CockpitConfigurationNotFoundException ccnfe)
        {
            LOG.debug(String.format("Could not find UI configuration for given context (%s).", new Object[] {configContext}), (Throwable)ccnfe);
        }
        catch(CockpitConfigurationException cce)
        {
            LOG.error(String.format("Could not load cockpit config for the given context '%s'.", new Object[] {configContext}), (Throwable)cce);
        }
        return config;
    }
}
