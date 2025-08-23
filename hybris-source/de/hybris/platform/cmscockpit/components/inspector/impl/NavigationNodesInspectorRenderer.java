package de.hybris.platform.cmscockpit.components.inspector.impl;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cockpit.components.inspector.impl.AbstractInspectorRenderer;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.media.MediaInfoService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.ListProvider;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.model.media.MediaModel;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;

public class NavigationNodesInspectorRenderer extends AbstractInspectorRenderer
{
    private static final Logger LOG = Logger.getLogger(NavigationNodesInspectorRenderer.class);
    private Map<String, String> navigationNodeConfig;
    private Map<String, String> contentPageConfig;
    private Map<String, String> mediaConfig;
    private Map<String, String> linkConfig;
    private TypeService typeService;
    private MediaInfoService mediaInfoService;


    public void renderEmpty(Component parent)
    {
        Label label = new Label(Labels.getLabel("editorarea.nothing_selected"));
        Div headerDiv = new Div();
        headerDiv.setSclass("infoAreaLabel");
        headerDiv.appendChild((Component)label);
        parent.appendChild((Component)headerDiv);
    }


    public void render(Component parent, TypedObject object)
    {
        Label label = new Label(prepareHeadingText(object));
        Div headerDiv = new Div();
        headerDiv.setSclass("infoAreaLabel");
        headerDiv.appendChild((Component)label);
        parent.appendChild((Component)headerDiv);
        Div toolbarDiv = new Div();
        toolbarDiv.setSclass("infoHeaderToolbar");
        headerDiv.appendChild((Component)toolbarDiv);
        prepareEditActionButton((Component)toolbarDiv, object);
        renderSections(parent, object);
    }


    protected void renderSections(Component parent, TypedObject object)
    {
        Div ret = new Div();
        Map<String, String> parameters = getParametersMap(object);
        String typeName = getTypeName(object);
        for(String label : parameters.keySet())
        {
            String sectionLabelString = Labels.getLabel(label);
            if(sectionLabelString == null)
            {
                sectionLabelString = "[" + label + "]";
            }
            Label sectionLabel = new Label(sectionLabelString);
            sectionLabel.setSclass("infoAreaSectionLabel");
            Div infoSectionLabelCnt = new Div();
            infoSectionLabelCnt.setSclass("infoSectionLabelCnt");
            infoSectionLabelCnt.appendChild((Component)sectionLabel);
            ret.appendChild((Component)infoSectionLabelCnt);
            for(String qualifier : ((String)parameters.get(label)).split(","))
            {
                qualifier = qualifier.trim();
                PropertyDescriptor propertyDescriptor = getTypeService().getPropertyDescriptor(typeName + "." + typeName);
                if(propertyDescriptor == null)
                {
                    LOG.warn("Property not found: " + typeName + "." + qualifier + " skipping in navigator;");
                }
                else
                {
                    try
                    {
                        Object value = getValueService().getValue(object, propertyDescriptor);
                        Div rowCnt = new Div();
                        rowCnt.setSclass("inspectorRowCnt");
                        Hbox hbox = new Hbox();
                        rowCnt.appendChild((Component)hbox);
                        ret.appendChild((Component)rowCnt);
                        String labelString = propertyDescriptor.getName();
                        if(StringUtils.isEmpty(labelString))
                        {
                            labelString = "[" + propertyDescriptor.getQualifier() + "]";
                        }
                        Label rowLabel = new Label(labelString);
                        Div infoAreaLabelDiv = new Div();
                        infoAreaLabelDiv.setSclass("infoAreaLabelDiv");
                        infoAreaLabelDiv.appendChild((Component)rowLabel);
                        hbox.appendChild((Component)infoAreaLabelDiv);
                        Label valueLabel = new Label((value != null) ? TypeTools.getValueAsString(UISessionUtils.getCurrentSession().getLabelService(), value) : "");
                        infoAreaLabelDiv = new Div();
                        infoAreaLabelDiv.setSclass("infoAreaValueDiv");
                        hbox.appendChild((Component)infoAreaLabelDiv);
                        infoAreaLabelDiv.appendChild((Component)valueLabel);
                    }
                    catch(ValueHandlerException e)
                    {
                        LOG.error("Could not get value for property '" + propertyDescriptor + "', reason: ", (Throwable)e);
                    }
                }
            }
        }
        prepareThumbnailIfPossible((Component)ret, object);
        parent.appendChild((Component)ret);
    }


    protected void prepareThumbnailIfPossible(Component parent, TypedObject object)
    {
        String path = null;
        if(object.getObject() instanceof ContentPageModel)
        {
            ContentPageModel page = (ContentPageModel)object.getObject();
            if(page.getPreviewImage() != null)
            {
                path = UITools.getAdjustedUrl(page.getPreviewImage().getURL2());
            }
        }
        else if(object.getObject() instanceof MediaModel)
        {
            MediaModel media = (MediaModel)object.getObject();
            if(this.mediaInfoService.isWebMedia(media).booleanValue())
            {
                path = UITools.getAdjustedUrl(media.getURL2());
            }
        }
        if(StringUtils.isNotBlank(path))
        {
            Image thumbnail = new Image(path);
            Div infoAreaLabelDiv = new Div();
            infoAreaLabelDiv.setSclass("infoAreaThumbnailDiv");
            infoAreaLabelDiv.appendChild((Component)thumbnail);
            parent.appendChild((Component)infoAreaLabelDiv);
        }
    }


    protected String getTypeName(TypedObject object)
    {
        if(object.getObject() instanceof de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel)
        {
            return "CMSNavigationNode";
        }
        if(object.getObject() instanceof MediaModel)
        {
            return "Media";
        }
        if(object.getObject() instanceof de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel)
        {
            return "CMSLinkComponent";
        }
        if(object.getObject() instanceof ContentPageModel)
        {
            return "ContentPage";
        }
        return null;
    }


    protected Map<String, String> getParametersMap(TypedObject object)
    {
        if(object.getObject() instanceof de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel)
        {
            return this.navigationNodeConfig;
        }
        if(object.getObject() instanceof MediaModel)
        {
            return this.mediaConfig;
        }
        if(object.getObject() instanceof de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel)
        {
            return this.linkConfig;
        }
        if(object.getObject() instanceof ContentPageModel)
        {
            return this.contentPageConfig;
        }
        return Collections.emptyMap();
    }


    protected String prepareHeadingText(TypedObject object)
    {
        if(object.getObject() instanceof CMSItemModel)
        {
            CMSItemModel item = (CMSItemModel)object.getObject();
            if(item.getCatalogVersion() == null)
            {
                return item.getName();
            }
            return item.getName() + "/" + item.getName() + "/" + item.getCatalogVersion().getCatalog().getName();
        }
        if(object.getObject() instanceof MediaModel)
        {
            MediaModel item = (MediaModel)object.getObject();
            if(item.getCatalogVersion() == null)
            {
                return item.getCode();
            }
            return item.getCode() + "/" + item.getCode() + "/" + item.getCatalogVersion().getCatalog().getName();
        }
        return object.getObject().toString();
    }


    public void render(Component parent, ListProvider<TypedObject> objectsProvider)
    {
        Label label = new Label(Labels.getLabel("inspector.multipleSelection", (Object[])new String[] {String.valueOf(objectsProvider.getListSize())}));
        label.setSclass("");
        Div headerDiv = new Div();
        headerDiv.setSclass("infoAreaLabel");
        headerDiv.appendChild((Component)label);
        parent.appendChild((Component)headerDiv);
    }


    public Map<String, String> getNavigationNodeConfig()
    {
        return this.navigationNodeConfig;
    }


    public void setNavigationNodeConfig(Map<String, String> navigationNodeConfig)
    {
        this.navigationNodeConfig = navigationNodeConfig;
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public Map<String, String> getContentPageConfig()
    {
        return this.contentPageConfig;
    }


    public void setContentPageConfig(Map<String, String> contentPageConfig)
    {
        this.contentPageConfig = contentPageConfig;
    }


    public Map<String, String> getMediaConfig()
    {
        return this.mediaConfig;
    }


    public void setMediaConfig(Map<String, String> mediaConfig)
    {
        this.mediaConfig = mediaConfig;
    }


    public Map<String, String> getLinkConfig()
    {
        return this.linkConfig;
    }


    public void setLinkConfig(Map<String, String> linkConfig)
    {
        this.linkConfig = linkConfig;
    }


    public MediaInfoService getMediaInfoService()
    {
        return this.mediaInfoService;
    }


    public void setMediaInfoService(MediaInfoService mediaInfoService)
    {
        this.mediaInfoService = mediaInfoService;
    }
}
