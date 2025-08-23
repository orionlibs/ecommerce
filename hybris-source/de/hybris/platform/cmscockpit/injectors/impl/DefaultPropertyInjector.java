package de.hybris.platform.cmscockpit.injectors.impl;

import de.hybris.platform.cmscockpit.components.contentbrowser.ComponentInjectorHelper;
import de.hybris.platform.cmscockpit.injectors.PropertyInjector;
import de.hybris.platform.cmscockpit.services.config.ContentEditorConfiguration;
import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.editor.EditorHelper;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.security.UIAccessRightService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ValueService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.util.ViewUpdateUtils;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;

public class DefaultPropertyInjector implements PropertyInjector
{
    private static final Logger LOG = Logger.getLogger(DefaultPropertyInjector.class);
    private TypeService typeService;
    private HtmlBasedComponent rootComponent;
    private ValueService valueService = null;


    public HtmlBasedComponent getRootComponent()
    {
        return this.rootComponent;
    }


    public void setRootComponent(HtmlBasedComponent rootComponent)
    {
        this.rootComponent = rootComponent;
    }


    public DefaultPropertyInjector()
    {
        this(null);
    }


    public DefaultPropertyInjector(HtmlBasedComponent rootComponent)
    {
        this.rootComponent = rootComponent;
    }


    public void injectProperty(TypedObject item, HtmlBasedComponent parent, ContentEditorConfiguration config, ObjectValueContainer valueContainer, Object locationInfo, boolean hideReadOnly, boolean autoPersist, Map<String, ? extends Object> params)
    {
        if(item == null)
        {
            LOG.warn("Property specified, but there is no item available.");
            return;
        }
        if(params.get("value") == null)
        {
            LOG.warn("Property attributes also requires a value set");
            return;
        }
        String propDescrStr = params.get("value").toString();
        try
        {
            PropertyDescriptor propDescr = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor(propDescrStr);
            if(propDescr == null)
            {
                LOG.warn("Injection of property editor failed. Reason: Corresponding property descriptor could not be retrieved.");
            }
            else if(config.isEditorVisible(propDescrStr))
            {
                boolean editable = getUiAccessRightService().isWritable((ObjectType)item.getType(), item, propDescr, false);
                if(editable || !hideReadOnly)
                {
                    Div entryDiv = new Div();
                    parent.appendChild((Component)entryDiv);
                    entryDiv.setSclass("contentEditorEntry" + (
                                    editable ? "" : " contentEditorEntryReadOnly"));
                    Map<String, Object> edParams = new HashMap<>();
                    edParams.putAll(config.getParameterMap(propDescrStr));
                    Hbox hbox = new Hbox();
                    entryDiv.appendChild((Component)hbox);
                    hbox.setWidth("100%");
                    hbox.setStyle("margin-top: 3px;");
                    hbox.setWidths(ComponentInjectorHelper.getEditorWiths(edParams));
                    Label label = new Label(ComponentInjectorHelper.getPropertyLabel(propDescr) + ":");
                    hbox.appendChild((Component)label);
                    if(ComponentInjectorHelper.createLocalizedTooltip(propDescr.getDescription(), (Component)hbox, label) != null)
                    {
                        hbox.setWidths(ComponentInjectorHelper.getEditorWithsTooltip(edParams));
                    }
                    if(locationInfo != null)
                    {
                        edParams.put("eventSource", locationInfo);
                    }
                    CreateContext ctx = new CreateContext(null, item, propDescr, null);
                    ctx.setExcludedTypes(EditorHelper.parseTemplateCodes((String)edParams.get("excludeCreateTypes"),
                                    getTypeService()));
                    ctx.setAllowedTypes(EditorHelper.parseTemplateCodes((String)edParams.get("restrictToCreateTypes"),
                                    getTypeService()));
                    edParams.put("createContext", ctx);
                    edParams.put(AdditionalReferenceEditorListener.class.getName(), new Object(this));
                    Div editorCnt = new Div();
                    hbox.appendChild((Component)editorCnt);
                    Map<String, Object> listenerParams = new HashMap<>(params);
                    listenerParams.put("cp_update_cmp", editorCnt);
                    EditorListener editorListener = ComponentInjectorHelper.createEditorListener(item, valueContainer, listenerParams, propDescr, config
                                    .getEditorCode(propDescr.getQualifier()), parent, this.rootComponent, autoPersist);
                    EditorHelper.createEditor(item, propDescr, (HtmlBasedComponent)editorCnt, valueContainer, autoPersist, config
                                    .getEditorCode(propDescrStr), edParams, editorListener);
                    ViewUpdateUtils.setUpdateCallback((Component)editorCnt, (ViewUpdateUtils.UpdateCallbackObject)new Object(this, item, propDescr, valueContainer, editorCnt, autoPersist, config, propDescrStr, edParams, editorListener));
                }
            }
        }
        catch(Exception e)
        {
            LOG.warn("Component injection failed. \t: Could not get property descriptor '" + propDescrStr + "'", e);
            return;
        }
    }


    protected UIAccessRightService getUiAccessRightService()
    {
        return UISessionUtils.getCurrentSession().getUiAccessRightService();
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }


    public ValueService getValueService()
    {
        if(this.valueService == null)
        {
            this.valueService = (ValueService)SpringUtil.getBean("valueService");
        }
        return this.valueService;
    }
}
