package de.hybris.platform.cmscockpit.components.contentbrowser;

import de.hybris.platform.cmscockpit.services.config.ContentEditorConfiguration;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import java.util.Map;
import org.zkoss.zk.ui.HtmlBasedComponent;

public abstract class AbstractContentEditorRenderer implements ContentEditorRenderer
{
    protected boolean autoPersist = true;
    private final ContentEditorConfiguration config;


    public AbstractContentEditorRenderer(ContentEditorConfiguration conf)
    {
        this(conf, true);
    }


    public AbstractContentEditorRenderer(ContentEditorConfiguration conf, boolean autoPersist)
    {
        this.config = conf;
        this.autoPersist = autoPersist;
    }


    public boolean isAutoPersist()
    {
        return this.autoPersist;
    }


    public void setAutoPersist(boolean autoPersist)
    {
        this.autoPersist = autoPersist;
    }


    protected ContentEditorConfiguration getContentEditorConfiguration()
    {
        return this.config;
    }


    public void renderContentEditor(TypedObject item, String template, ObjectValueContainer valueContainer, HtmlBasedComponent parent, Map<String, ? extends Object> params) throws IllegalArgumentException
    {
        renderContentEditor(item, template, valueContainer, parent, params, false);
    }
}
