package de.hybris.platform.cockpit.model.meta;

import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.editor.impl.DefaultDummyUIEditor;
import de.hybris.platform.cockpit.model.editor.impl.GenericCollectionUIEditor;
import de.hybris.platform.cockpit.model.meta.impl.AbstractPropertyEditorDescriptor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultPropertyEditorDescriptor extends AbstractPropertyEditorDescriptor
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultPropertyEditorDescriptor.class);
    private String defaultMode = null;
    private String defaultSearchMode = null;
    private final Map<String, String> editors = new HashMap<>();


    public String getDefaultEditor()
    {
        return this.editors.get("default");
    }


    @Required
    public void setDefaultEditor(String editor)
    {
        this.editors.put("default", editor);
    }


    public void setEditors(Map<String, String> editorMap)
    {
        this.editors.putAll(editorMap);
    }


    public Map<String, String> getEditors()
    {
        return (this.editors == null) ? Collections.EMPTY_MAP : this.editors;
    }


    public PropertyEditorBean createValueBean(Object value)
    {
        DefaultZKPropertyEditorBean defaultZKPropertyEditorBean = new DefaultZKPropertyEditorBean(this);
        defaultZKPropertyEditorBean.initialize(value);
        return (PropertyEditorBean)defaultZKPropertyEditorBean;
    }


    public UIEditor createUIEditor()
    {
        return createUIEditor("default");
    }


    public UIEditor createUIEditor(String mode)
    {
        UIEditor ret = null;
        try
        {
            String edClass = this.editors.get(mode);
            if(edClass == null && this.defaultMode != null && this.defaultMode.equals(mode))
            {
                edClass = this.editors.get("default");
            }
            if(edClass == null)
            {
                if(!"readonly".equalsIgnoreCase(mode))
                {
                    LOG.error("Can not create editor component for type " + getEditorType() + " and mode \"" + mode + "\".");
                }
                return (UIEditor)new DefaultDummyUIEditor();
            }
            Class<?> editorClass = Class.forName(edClass);
            ret = (UIEditor)editorClass.newInstance();
            if(ret instanceof GenericCollectionUIEditor)
            {
                ((GenericCollectionUIEditor)ret).setSingleValueEditorDescriptor((PropertyEditorDescriptor)this);
            }
        }
        catch(InstantiationException e)
        {
            LOG.error("Can not create editor component for type " + getEditorType() + " and mode \"" + mode + "\". Check if editor class has a default constructor.", e);
        }
        catch(Exception e)
        {
            LOG.error("Can not create editor component for type " + getEditorType() + " and mode \"" + mode + "\".", e);
        }
        return ret;
    }


    public void setDefaultMode(String defaultMode)
    {
        this.defaultMode = defaultMode;
    }


    public String getDefaultMode()
    {
        return this.defaultMode;
    }


    public void setDefaultSearchMode(String defaultSearchMode)
    {
        this.defaultSearchMode = defaultSearchMode;
    }


    public String getDefaultSearchMode()
    {
        return this.defaultSearchMode;
    }
}
