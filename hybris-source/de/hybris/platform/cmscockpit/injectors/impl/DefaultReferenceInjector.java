package de.hybris.platform.cmscockpit.injectors.impl;

import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminComponentService;
import de.hybris.platform.cmscockpit.components.contentbrowser.ComponentInjectorHelper;
import de.hybris.platform.cmscockpit.components.contentbrowser.DefaultContentEditorRenderer;
import de.hybris.platform.cmscockpit.injectors.ReferenceInjector;
import de.hybris.platform.cmscockpit.services.config.ContentEditorConfiguration;
import de.hybris.platform.cmscockpit.session.impl.ViewStatePersistenceProvider;
import de.hybris.platform.cockpit.components.AdvancedGroupbox;
import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.editor.EditorHelper;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.EditorConfiguration;
import de.hybris.platform.cockpit.services.config.EditorRowConfiguration;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.security.UIAccessRightService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ValueService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.ViewUpdateUtils;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;

public class DefaultReferenceInjector implements ReferenceInjector
{
    private static final Logger LOG = Logger.getLogger(DefaultReferenceInjector.class);
    private TypeService typeService;
    private UIConfigurationService configurationService;
    private CMSAdminComponentService adminCompService;
    private HtmlBasedComponent rootComponent;
    private ValueService valueService = null;


    public DefaultReferenceInjector()
    {
        this(null);
    }


    public DefaultReferenceInjector(HtmlBasedComponent rootComponent)
    {
        this.rootComponent = rootComponent;
    }


    public HtmlBasedComponent getRootComponent()
    {
        return this.rootComponent;
    }


    public void setRootComponent(HtmlBasedComponent rootComponent)
    {
        this.rootComponent = rootComponent;
    }


    protected int getDepthWithinSection(Component parent)
    {
        int ret = 0;
        Component current = parent;
        while(!(current instanceof de.hybris.platform.cockpit.components.contentbrowser.BrowserSectionComponent) && current != null)
        {
            current = current.getParent();
            ret++;
        }
        return ret;
    }


    public void injectReference(TypedObject referenceValue, HtmlBasedComponent parent, ContentEditorConfiguration config, Object locationInfo, boolean hideReadOnly, List<HtmlBasedComponent> captionComponents)
    {
        ViewStatePersistenceProvider vspp;
        Object refGroupView;
        boolean renderOpened;
        ObjectValueContainer refValueContainer = TypeTools.createValueContainer(referenceValue, referenceValue.getType()
                        .getPropertyDescriptors(), UISessionUtils.getCurrentSession().getSystemService().getAvailableLanguageIsos(), true);
        if(locationInfo instanceof ViewStatePersistenceProvider)
        {
            vspp = (ViewStatePersistenceProvider)locationInfo;
        }
        else
        {
            vspp = null;
        }
        if(referenceValue.getObject() instanceof de.hybris.platform.core.model.ItemModel)
        {
            refGroupView = new Object(this, referenceValue, parent);
        }
        else
        {
            refGroupView = null;
        }
        AdvancedGroupbox refGroup = new AdvancedGroupbox();
        parent.appendChild((Component)refGroup);
        boolean closeable = ComponentInjectorHelper.hasEditableProperties(referenceValue);
        refGroup.setClosable(closeable);
        refGroup.setSclass(closeable ? "contentEditorGroupbox" :
                        "contentEditorGroupboxNotExpandable");
        refGroup.setLabel(UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabelForTypedObject(referenceValue));
        ViewUpdateUtils.setUpdateCallback((Component)refGroup, (ViewUpdateUtils.UpdateCallbackObject)new Object(this, referenceValue, refGroup));
        if(vspp != null && refGroupView != null)
        {
            Map<String, Object> refGroupVS = vspp.getViewStateForComponent((ViewStatePersistenceProvider.ViewStatePersisteable)refGroupView);
            renderOpened = refGroupVS.containsKey("renderOpended");
        }
        else
        {
            renderOpened = false;
        }
        refGroup.setOpen(renderOpened);
        if(captionComponents != null && !captionComponents.isEmpty())
        {
            Hbox captionBox = new Hbox();
            captionBox.setAlign("center");
            captionBox.setWidth("100%");
            refGroup.getCaptionContainer().appendChild((Component)captionBox);
            captionBox.setSclass("contentElementActionBox");
            for(HtmlBasedComponent comp : captionComponents)
            {
                captionBox.appendChild((Component)comp);
            }
        }
        refGroup.setAttribute("ondemandinit", Boolean.FALSE);
        refGroup.addEventListener("onOpen", (EventListener)new Object(this, refGroup, vspp, (ViewStatePersistenceProvider.ViewStatePersisteable)refGroupView));
        refGroup.addEventListener("onLater", (EventListener)new Object(this, referenceValue, refValueContainer, refGroup, config, locationInfo, hideReadOnly));
        if(renderOpened)
        {
            ObjectTemplate objectTemplate = UISessionUtils.getCurrentSession().getTypeService().getBestTemplate(referenceValue);
            ContentEditorConfiguration editorConfiguration = getContentEditorConfiguration(objectTemplate);
            if(editorConfiguration.isDefaultConfiguration())
            {
                injectNormalReference(referenceValue, refValueContainer, (HtmlBasedComponent)refGroup, config, locationInfo, hideReadOnly);
            }
            else
            {
                injectCmsReference(referenceValue, refValueContainer, (HtmlBasedComponent)refGroup, locationInfo);
            }
        }
    }


    protected void injectNormalReference(TypedObject referenceValue, ObjectValueContainer refValueContainer, HtmlBasedComponent refGroup, ContentEditorConfiguration config, Object locationInfo, boolean hideReadOnly)
    {
        Set<PropertyDescriptor> descriptors = referenceValue.getType().getPropertyDescriptors();
        UIConfigurationService uiConfigurationService = getConfigurationService();
        Map<PropertyDescriptor, EditorRowConfiguration> editorMapping = Collections.EMPTY_MAP;
        if(uiConfigurationService != null)
        {
            ObjectTemplate template = UISessionUtils.getCurrentSession().getTypeService().getBestTemplate(referenceValue);
            EditorConfiguration componentConfiguration = (EditorConfiguration)uiConfigurationService.getComponentConfiguration(template, "editorArea", EditorConfiguration.class);
            editorMapping = ComponentInjectorHelper.getAllEditorRowConfigurations(componentConfiguration);
        }
        if(descriptors != null)
        {
            for(PropertyDescriptor propDescr : descriptors)
            {
                if(!config.isEditorVisible(propDescr.getQualifier()))
                {
                    continue;
                }
                EditorRowConfiguration rowConfig = editorMapping.get(propDescr);
                Map<String, Object> edParams = new HashMap<>();
                boolean editable = true;
                String editorCode = null;
                if(rowConfig != null)
                {
                    if(!rowConfig.isVisible())
                    {
                        continue;
                    }
                    editable = rowConfig.isEditable();
                    edParams.putAll(rowConfig.getParameters());
                    editorCode = rowConfig.getEditor();
                }
                else
                {
                    edParams.putAll(config.getParameterMap(propDescr.getQualifier()));
                    editable = getUiAccessRightService().isWritable((ObjectType)referenceValue.getType(), referenceValue, propDescr, false);
                    editorCode = config.getEditorCode(propDescr.getQualifier());
                }
                if(editable || !hideReadOnly)
                {
                    CreateContext ctx = new CreateContext(null, referenceValue, propDescr, null);
                    ctx.setExcludedTypes(EditorHelper.parseTemplateCodes((String)edParams.get("excludeCreateTypes"), getTypeService()));
                    ctx.setAllowedTypes(EditorHelper.parseTemplateCodes((String)edParams.get("restrictToCreateTypes"),
                                    getTypeService()));
                    edParams.put("createContext", ctx);
                    if(locationInfo != null)
                    {
                        edParams.put("eventSource", locationInfo);
                    }
                    edParams.put(AdditionalReferenceEditorListener.class.getName(), new Object(this));
                    boolean labelVisible = (editable && getUiAccessRightService().isWritable((ObjectType)referenceValue.getType(), referenceValue, propDescr, false));
                    HtmlBasedComponent editorParent = ComponentInjectorHelper.renderEditorRow((Component)refGroup, edParams, labelVisible,
                                    ComponentInjectorHelper.getPropertyLabel(propDescr), propDescr.getDescription());
                    Div editorCnt = new Div();
                    editorParent.appendChild((Component)editorCnt);
                    Map<String, Object> listenerParams = new HashMap<>(edParams);
                    listenerParams.put("cp_update_cmp", editorCnt);
                    EditorListener editorListener = ComponentInjectorHelper.createEditorListener(referenceValue, refValueContainer, listenerParams, propDescr, config
                                    .getEditorCode(propDescr.getQualifier()), editorParent, this.rootComponent, true);
                    EditorHelper.createEditor(referenceValue, propDescr, (HtmlBasedComponent)editorCnt, refValueContainer, true, editorCode, edParams, editorListener);
                    String f_editorCode = editorCode;
                    ViewUpdateUtils.setUpdateCallback((Component)editorCnt, (ViewUpdateUtils.UpdateCallbackObject)new Object(this, referenceValue, propDescr, refValueContainer, editorCnt, f_editorCode, edParams, editorListener));
                }
            }
        }
    }


    protected void injectCmsReference(TypedObject referenceValue, ObjectValueContainer refValueContainer, HtmlBasedComponent refGroup, Object locationInfo)
    {
        renderEditor(referenceValue, refValueContainer, refGroup, locationInfo);
    }


    protected void renderEditor(TypedObject refValue, ObjectValueContainer valueContainer, HtmlBasedComponent parent, Object locationInfo)
    {
        ObjectTemplate objectTemplate = UISessionUtils.getCurrentSession().getTypeService().getBestTemplate(refValue);
        ContentEditorConfiguration configuration = getContentEditorConfiguration(objectTemplate);
        if(configuration == null)
        {
            LOG.error("Aborting rendering of content element editor. Reason: Configuration could not be loaded.");
            return;
        }
        DefaultContentEditorRenderer defaultContentEditorRenderer = new DefaultContentEditorRenderer(configuration);
        try
        {
            String templateString = ComponentInjectorHelper.getParsedVelocityTemplateString(refValue, configuration
                            .getCockpitTemplate());
            if(StringUtils.isBlank(templateString))
            {
                LOG.warn("Aborting rendering of content element editor. Reason: No cockpit template could be loaded.");
            }
            else
            {
                defaultContentEditorRenderer.renderContentEditor(refValue, templateString, valueContainer, parent,
                                Collections.singletonMap("locationInfo", locationInfo), false);
            }
        }
        catch(IllegalArgumentException iae)
        {
            LOG.error("Rendering of editor failed. Reason: '" + iae.getMessage() + "'.", iae);
        }
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }


    protected UIConfigurationService getConfigurationService()
    {
        if(this.configurationService == null)
        {
            this.configurationService = (UIConfigurationService)SpringUtil.getBean("uiConfigurationService");
        }
        return this.configurationService;
    }


    protected UIAccessRightService getUiAccessRightService()
    {
        return UISessionUtils.getCurrentSession().getUiAccessRightService();
    }


    protected CMSAdminComponentService getCMSAdminComponentService()
    {
        if(this.adminCompService == null)
        {
            this.adminCompService = (CMSAdminComponentService)SpringUtil.getBean("cmsAdminComponentService");
        }
        return this.adminCompService;
    }


    protected ContentEditorConfiguration getContentEditorConfiguration(ObjectTemplate objectTemplate)
    {
        return (ContentEditorConfiguration)getConfigurationService().getComponentConfiguration(objectTemplate, "contentEditor", ContentEditorConfiguration.class);
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
