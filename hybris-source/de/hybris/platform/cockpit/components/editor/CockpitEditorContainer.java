package de.hybris.platform.cockpit.components.editor;

import de.hybris.platform.cockpit.model.editor.EditorHelper;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.ListUIEditor;
import de.hybris.platform.cockpit.model.editor.ReferenceUIEditor;
import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.EditorRowRenderer;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class CockpitEditorContainer extends Div
{
    private static final Logger LOG = LoggerFactory.getLogger(CockpitEditorContainer.class);
    private ObjectValueContainer valueContainer;
    private String propertyQualifier;
    private String valueTypeCode;
    private String editorCode;
    private Object editorValue;
    private boolean localized = false;


    public CockpitEditorContainer()
    {
        addEventListener("onCreate", (EventListener)new Object(this));
    }


    public void update()
    {
        getChildren().clear();
        initialize();
    }


    public void initialize()
    {
        applyProperties();
        Map<String, Object> params = new HashMap<>();
        params.putAll(getAttributes());
        if(this.propertyQualifier == null)
        {
            ObjectType valueType = null;
            if(this.valueTypeCode != null)
            {
                String editorType;
                try
                {
                    valueType = UISessionUtils.getCurrentSession().getTypeService().getObjectType(this.valueTypeCode);
                }
                catch(UnknownIdentifierException e)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Retrieving composed type '' failed, using valueType directly as editor type.", (Throwable)e);
                    }
                }
                if(valueType == null)
                {
                    editorType = this.valueTypeCode;
                }
                else
                {
                    editorType = "REFERENCE";
                }
                UIEditor uiEditor = EditorHelper.getUIEditor((PropertyDescriptor)new Object(this, editorType), this.editorCode);
                if(uiEditor instanceof ReferenceUIEditor)
                {
                    ((ReferenceUIEditor)uiEditor).setRootType(valueType);
                    if(!StringUtils.isEmpty((String)params.get("allowCreate")))
                    {
                        ((ReferenceUIEditor)uiEditor).setAllowCreate(Boolean.valueOf((String)params
                                        .get("allowCreate")));
                    }
                    else
                    {
                        ((ReferenceUIEditor)uiEditor).setAllowCreate(Boolean.TRUE);
                    }
                }
                if(uiEditor instanceof ListUIEditor)
                {
                    ((ListUIEditor)uiEditor).setAvailableValues(EditorHelper.getAvailableValues(params, null));
                }
                if(this.localized)
                {
                    List<String> availableLanguageIsos = new ArrayList<>(UISessionUtils.getCurrentSession().getSystemService().getAvailableLanguageIsos());
                    EditorRowRenderer.renderLocalizedStructure((Component)this, availableLanguageIsos, availableLanguageIsos, (EditorRowRenderer.SingleEditorRenderer)new Object(this, uiEditor, params));
                }
                else
                {
                    HtmlBasedComponent viewComponent = uiEditor.createViewComponent(this.editorValue, params, (EditorListener)new Object(this));
                    appendChild((Component)viewComponent);
                }
            }
        }
        else
        {
            try
            {
                PropertyDescriptor propertyDescriptor = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor(this.propertyQualifier);
                ObjectType valueType = UISessionUtils.getCurrentSession().getTypeService().getObjectTypeFromPropertyQualifier(this.propertyQualifier);
                if(this.valueContainer == null)
                {
                    Set<String> isos = UISessionUtils.getCurrentSession().getSystemService().getAvailableLanguageIsos();
                    this.valueContainer = new ObjectValueContainer(valueType, null);
                    if(propertyDescriptor.isLocalized())
                    {
                        for(String iso : isos)
                        {
                            this.valueContainer.addValue(propertyDescriptor, iso, null);
                        }
                    }
                    else
                    {
                        this.valueContainer.addValue(propertyDescriptor, null, null);
                    }
                }
                if(propertyDescriptor.isLocalized())
                {
                    EditorHelper.renderLocalizedEditor(null, propertyDescriptor, (HtmlBasedComponent)this, this.valueContainer, false, this.editorCode, params, false,
                                    createEditorListener(propertyDescriptor));
                }
                else
                {
                    EditorHelper.renderSingleEditor(null, propertyDescriptor, (HtmlBasedComponent)this, this.valueContainer, false, this.editorCode, params, null, false,
                                    createEditorListener(propertyDescriptor));
                }
            }
            catch(Exception e)
            {
                appendChild((Component)new Label("[Could not render editor for '" + this.propertyQualifier + "']"));
                LOG.error("Could not render editor for '" + this.propertyQualifier + "', reason was: ", e);
            }
        }
    }


    protected Object getLocalizedValueIfMap(Object value, String isoCode)
    {
        return (value instanceof Map) ? ((Map)value).get(isoCode) : value;
    }


    protected EditorListener createEditorListener(PropertyDescriptor propertyDescriptor)
    {
        return (EditorListener)new Object(this, propertyDescriptor);
    }


    public void setValueContainer(ObjectValueContainer valueContainer)
    {
        this.valueContainer = valueContainer;
    }


    public ObjectValueContainer getValueContainer()
    {
        return this.valueContainer;
    }


    public void setPropertyQualifier(String propertyQualifier)
    {
        this.propertyQualifier = propertyQualifier;
    }


    public String getPropertyQualifier()
    {
        return this.propertyQualifier;
    }


    public void setEditorCode(String editorCode)
    {
        this.editorCode = editorCode;
    }


    public String getEditorCode()
    {
        return this.editorCode;
    }


    public void setEditorValue(Object editorValue)
    {
        this.editorValue = editorValue;
    }


    public Object getEditorValue()
    {
        return this.editorValue;
    }


    protected void setEditorValue(Object value, String langIso)
    {
        if(langIso == null)
        {
            this.editorValue = value;
        }
        else
        {
            if(this.editorValue == null)
            {
                this.editorValue = new HashMap<>();
            }
            if(this.editorValue instanceof Map)
            {
                ((Map<String, Object>)this.editorValue).put(langIso, value);
            }
        }
        Events.sendEvent((Component)this, new Event("onValueChange", (Component)this));
    }


    public void setValueTypeCode(String valueTypeCode)
    {
        this.valueTypeCode = valueTypeCode;
    }


    public String getValueTypeCode()
    {
        return this.valueTypeCode;
    }


    public void setLocalized(boolean localized)
    {
        this.localized = localized;
    }


    public boolean isLocalized()
    {
        return this.localized;
    }
}
