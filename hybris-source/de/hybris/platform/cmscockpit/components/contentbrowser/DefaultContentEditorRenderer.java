package de.hybris.platform.cmscockpit.components.contentbrowser;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageLockingService;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cmscockpit.services.config.ContentEditorConfiguration;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.DefaultCockpitTemplateParser;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.impl.XulElement;

public class DefaultContentEditorRenderer extends AbstractContentEditorRenderer
{
    public DefaultContentEditorRenderer(ContentEditorConfiguration conf)
    {
        this(conf, true);
    }


    public DefaultContentEditorRenderer(ContentEditorConfiguration conf, boolean autoPersist)
    {
        super(conf, autoPersist);
    }


    public void renderContentEditor(TypedObject item, String template, ObjectValueContainer valueContainer, HtmlBasedComponent parent, Map<String, ? extends Object> params, boolean readOnly) throws IllegalArgumentException
    {
        if(item == null)
        {
            throw new IllegalArgumentException("Item can not be null.");
        }
        if(StringUtils.isBlank(template))
        {
            throw new IllegalArgumentException("No template specified.");
        }
        DefaultCockpitTemplateParser defaultCockpitTemplateParser = new DefaultCockpitTemplateParser();
        String parsedTemplate = null;
        try
        {
            parsedTemplate = defaultCockpitTemplateParser.parseTemplate(template);
        }
        catch(IllegalArgumentException iae)
        {
            throw new IllegalArgumentException("Could not parse template.", iae);
        }
        if(StringUtils.isBlank(parsedTemplate))
        {
            throw new IllegalArgumentException("Parsed template is empty.");
        }
        CMSContentEditorInjector injector = getCmsContentEditorInjector();
        injector.setConfig(getContentEditorConfiguration());
        injector.setItem(item);
        injector.setValueContainer(valueContainer);
        injector.setAutoPersist(isAutoPersist());
        boolean componentLocked = false;
        if(UISessionUtils.getCurrentSession().getTypeService().getBaseType(GeneratedCms2Constants.TC.ABSTRACTCMSCOMPONENT)
                        .isAssignableFrom((ObjectType)item.getType()))
        {
            componentLocked = getCmsPageLockingService().isComponentLockedForUser((AbstractCMSComponentModel)item.getObject(),
                            UISessionUtils.getCurrentSession().getSystemService().getCurrentUser());
        }
        injector.setHideReadOnly((getContentEditorConfiguration().isHideReadOnly() && !componentLocked));
        injector.setHideEmpty(getContentEditorConfiguration().isHideEmpty());
        injector.setGroupCollections(getContentEditorConfiguration().isGroupCollections());
        injector.setRootComponent(parent);
        injector.setLocationInfoObject(params.get("locationInfo"));
        Component templateComponent = Executions.createComponentsDirectly(parsedTemplate, "zul", (Component)parent,
                        Collections.singletonMap("injector", injector));
        templateComponent.detach();
        for(Object child : Arrays.<Object>asList(templateComponent.getChildren().toArray()))
        {
            parent.appendChild((Component)child);
        }
        if(readOnly)
        {
            Div readOnlyLayer = new Div();
            readOnlyLayer.setParent((Component)parent);
            readOnlyLayer.setSclass("readOnlyLayer");
            if(parent instanceof XulElement)
            {
                String readOnlyLayerId = readOnlyLayer.getId();
                String onMouseOver = String.format("onmouseover:readonlydiv=getElementById('%s');readonlydiv.style.height=this.scrollHeight + 'px';", new Object[] {readOnlyLayerId});
                ((XulElement)parent).setAction(onMouseOver);
            }
        }
    }


    protected CMSContentEditorInjector getCmsContentEditorInjector()
    {
        CMSContentEditorInjector cmsContentEditorInjector = (CMSContentEditorInjector)SpringUtil.getBean("cmsContentEditorInjector");
        if(cmsContentEditorInjector == null)
        {
            cmsContentEditorInjector = new CMSContentEditorInjector();
        }
        return cmsContentEditorInjector;
    }


    protected CMSPageLockingService getCmsPageLockingService()
    {
        return (CMSPageLockingService)SpringUtil.getBean("cmsPageLockingService");
    }


    protected CMSPageService getCmsPageService()
    {
        return (CMSPageService)SpringUtil.getBean("cmsPageService");
    }
}
