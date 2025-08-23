package de.hybris.platform.cockpit.model.gridview.impl;

import de.hybris.platform.cockpit.components.listview.ActionColumnConfiguration;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.model.general.ListComponentModel;
import de.hybris.platform.cockpit.model.gridview.GridItemRenderer;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.GridViewConfiguration;
import de.hybris.platform.cockpit.services.dragdrop.DragAndDropWrapper;
import de.hybris.platform.cockpit.services.dragdrop.DraggedItem;
import de.hybris.platform.cockpit.services.label.LabelService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Popup;

public class DefaultGridItemRenderer implements GridItemRenderer
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultGridItemRenderer.class);
    private static final String GRID_SHORTINFO_DIV = "gridShortinfoDiv";
    private static final String GRID_DESCRIPTION_DIV = "gridDescriptionDiv";
    private static final String GRID_LABEL_DIV = "gridLabelDiv";
    private static final String GRID_ACTIONS_DIV = "gridActionsDiv";
    private static final String GRID_STATUS_DIV = "gridStatusDiv";
    private static final String GRID_IMAGE_DIV = "gridImageDiv";
    private static final String GRID_IMAGE_SCLASS = "gridImageSclass";
    private static final String GRID_IMAGE_INACTIVE_SCLASS = "gridImageInactiveSclass";
    private static final String GRID_ITEM_MAIN_DIV = "gridItemMainDiv";
    private static final String COCKPIT_ID_BROWSERAREA_ST_SEARCH_RESULT = "BrowserArea_st_search_result_";
    private static final String COCKPIT_ID_BROWSERAREA_ST_SEARCH_RESULT_NAME_LABEL = "BrowserArea_st_search_result_Name_label";
    private LabelService labelService;
    private TypeService typeService;


    protected String getFallbackImage()
    {
        return "cockpit/images/stop_klein.jpg";
    }


    protected String getFallbackImage(TypedObject item)
    {
        return getFallbackImage();
    }


    protected boolean isPopupImageEnabled()
    {
        return false;
    }


    public void render(TypedObject item, Component parent, GridViewConfiguration config, ListComponentModel model, DraggedItem draggedItem, DragAndDropWrapper ddWrapper)
    {
        if(config == null)
        {
            LOG.warn("Config is null.");
            return;
        }
        GridValueHolder gridValueHolder = new GridValueHolder(config, item);
        Div mainDiv = new Div();
        mainDiv.setSclass("gridItemMainDiv");
        parent.appendChild((Component)mainDiv);
        Div imageDiv = new Div();
        imageDiv.setSclass("gridImageDiv");
        String objectAttributeValue = gridValueHolder.getImageURL();
        if(objectAttributeValue == null)
        {
            imageDiv.appendChild((Component)new Image(getFallbackImage(item)));
        }
        else
        {
            imageDiv.appendChild((Component)new Image(UITools.getAdjustedUrl(objectAttributeValue)));
            if(isPopupImageEnabled())
            {
                Popup popup = new Popup();
                imageDiv.appendChild((Component)popup);
                popup.setSclass("grid_image_popup");
                popup.appendChild((Component)new Image(UITools.getAdjustedUrl(objectAttributeValue)));
                imageDiv.setTooltip(popup);
            }
        }
        ddWrapper.attachDraggedItem(draggedItem, (Component)imageDiv);
        imageDiv.setDraggable("PerspectiveDND");
        Div actionsDiv = new Div();
        actionsDiv.setSclass("gridActionsDiv");
        renderActions((Component)actionsDiv, item, config, model);
        Div statusDiv = new Div();
        statusDiv.setSclass("gridStatusDiv");
        renderStatus((Component)statusDiv, item, config, model);
        Div labelDiv = new Div();
        labelDiv.setSclass("gridLabelDiv");
        objectAttributeValue = gridValueHolder.getLabel();
        labelDiv.appendChild((Component)new Label(objectAttributeValue));
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            UITools.applyTestID((Component)labelDiv, "BrowserArea_st_search_result_Name_label");
        }
        Div descriptionDiv = new Div();
        descriptionDiv.setSclass("gridDescriptionDiv");
        objectAttributeValue = gridValueHolder.getDescription();
        descriptionDiv.appendChild((Component)new Label(objectAttributeValue));
        Div shortInfoDiv = new Div();
        shortInfoDiv.setSclass("gridShortinfoDiv");
        objectAttributeValue = gridValueHolder.getShortInfo();
        shortInfoDiv.appendChild((Component)new Label(objectAttributeValue));
        mainDiv.appendChild((Component)imageDiv);
        mainDiv.appendChild((Component)statusDiv);
        mainDiv.appendChild((Component)labelDiv);
        mainDiv.appendChild((Component)descriptionDiv);
        mainDiv.appendChild((Component)shortInfoDiv);
        mainDiv.appendChild((Component)actionsDiv);
    }


    protected void renderActions(Component parent, TypedObject item, GridViewConfiguration config, ListComponentModel model)
    {
        ActionColumnConfiguration actionConfiguration = getActionConfiguration(config);
        if(actionConfiguration == null)
        {
            return;
        }
        List<ListViewAction> listViewActions = actionConfiguration.getActions();
        Set<TypedObject> additionalUpdateNotification = new HashSet<>();
        Div actionBox = new Div();
        parent.appendChild((Component)actionBox);
        for(ListViewAction listViewAction : listViewActions)
        {
            ListViewAction.Context context = listViewAction.createContext(model, item);
            Object object = context.getMap().get("affectedItems");
            if(object instanceof Collection)
            {
                Collection<TypedObject> affectedItems = (Collection<TypedObject>)object;
                additionalUpdateNotification.addAll(affectedItems);
            }
            String imgURI = listViewAction.getImageURI(context);
            if(imgURI != null && imgURI.length() > 0)
            {
                Image actionImg = new Image(imgURI);
                actionImg.setSclass("gridImageSclass");
                if(UISessionUtils.getCurrentSession().isUsingTestIDs())
                {
                    if("cmscockpit".equals(UITools.getWebAppName(UITools.getCurrentZKRoot().getDesktop())))
                    {
                        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
                        {
                            UITools.applyTestID((Component)actionImg, "BrowserArea_st_search_result_" + listViewAction
                                            .getClass().getSimpleName() + "_button");
                        }
                    }
                    else
                    {
                        String id = UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabel(item);
                        id = id.replaceAll("\\W", "") + "_" + id.replaceAll("\\W", "");
                        String statusCode = listViewAction.getStatusCode(context);
                        if(statusCode != null)
                        {
                            id = id + "." + id;
                        }
                        UITools.applyTestID((Component)actionImg, id);
                    }
                }
                EventListener listener = listViewAction.getEventListener(context);
                if(listener != null)
                {
                    actionImg.addEventListener("onClick", listener);
                    actionImg.addEventListener("onLater", listener);
                }
                if(listViewAction.getTooltip(context) != null && listViewAction.getTooltip(context).length() > 0)
                {
                    actionImg.setTooltiptext(listViewAction.getTooltip(context));
                }
                Menupopup popup = listViewAction.getPopup(context);
                if(popup != null)
                {
                    actionBox.appendChild((Component)popup);
                    actionImg.setPopup((Popup)popup);
                }
                actionBox.appendChild((Component)actionImg);
                Menupopup contextPopup = listViewAction.getContextPopup(context);
                if(contextPopup != null)
                {
                    actionBox.appendChild((Component)contextPopup);
                    actionImg.setContext((Popup)contextPopup);
                }
                if(listener == null && popup == null && contextPopup == null)
                {
                    UITools.modifySClass((HtmlBasedComponent)actionImg, "gridImageInactiveSclass", true);
                }
            }
        }
        if(model != null)
        {
            model.addToAdditionalItemChangeUpdateNotificationMap(item, additionalUpdateNotification);
        }
    }


    protected void renderStatus(Component parent, TypedObject item, GridViewConfiguration config, ListComponentModel model)
    {
        ActionColumnConfiguration statusConfiguration = getStatusConfiguration(config);
        if(statusConfiguration == null)
        {
            return;
        }
        List<ListViewAction> listViewActions = statusConfiguration.getActions();
        Set<TypedObject> additionalUpdateNotification = new HashSet<>();
        Div statusBox = new Div();
        parent.appendChild((Component)statusBox);
        for(ListViewAction listViewAction : listViewActions)
        {
            ListViewAction.Context context = listViewAction.createContext(model, item);
            Object object = context.getMap().get("affectedItems");
            if(object instanceof Collection)
            {
                Collection<TypedObject> affectedItems = (Collection<TypedObject>)object;
                additionalUpdateNotification.addAll(affectedItems);
            }
            String imgURI = listViewAction.getImageURI(context);
            if(imgURI != null && imgURI.length() > 0)
            {
                Image actionImg = new Image(imgURI);
                actionImg.setStyle("display: block; cursor: pointer; float:left");
                if(UISessionUtils.getCurrentSession().isUsingTestIDs())
                {
                    if("cmscockpit".equals(UITools.getWebAppName(UITools.getCurrentZKRoot().getDesktop())))
                    {
                        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
                        {
                            UITools.applyTestID((Component)actionImg, "BrowserArea_st_search_result_" + listViewAction
                                            .getClass().getSimpleName() + "_button");
                        }
                    }
                    else
                    {
                        String id = UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabel(item);
                        id = id.replaceAll("\\W", "") + "_" + id.replaceAll("\\W", "");
                        String statusCode = listViewAction.getStatusCode(context);
                        if(statusCode != null)
                        {
                            id = id + "." + id;
                        }
                        UITools.applyTestID((Component)actionImg, id);
                    }
                }
                EventListener listener = listViewAction.getEventListener(context);
                if(listener != null)
                {
                    actionImg.addEventListener("onClick", listener);
                    actionImg.addEventListener("onLater", listener);
                }
                if(listViewAction.getTooltip(context) != null && listViewAction.getTooltip(context).length() > 0)
                {
                    actionImg.setTooltiptext(listViewAction.getTooltip(context));
                }
                Menupopup popup = listViewAction.getPopup(context);
                if(popup != null)
                {
                    statusBox.appendChild((Component)popup);
                    actionImg.setPopup((Popup)popup);
                }
                statusBox.appendChild((Component)actionImg);
                Menupopup contextPopup = listViewAction.getContextPopup(context);
                if(contextPopup != null)
                {
                    statusBox.appendChild((Component)contextPopup);
                    actionImg.setContext((Popup)contextPopup);
                }
            }
        }
        if(model != null)
        {
            model.addToAdditionalItemChangeUpdateNotificationMap(item, additionalUpdateNotification);
        }
    }


    public LabelService getLabelService()
    {
        if(this.labelService == null)
        {
            this.labelService = (LabelService)SpringUtil.getBean("labelService");
        }
        return this.labelService;
    }


    public TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }


    public ActionColumnConfiguration getActionConfiguration(GridViewConfiguration config)
    {
        if(config != null)
        {
            String actionSpringBeanID = config.getActionSpringBeanID();
            if(actionSpringBeanID != null)
            {
                return (ActionColumnConfiguration)SpringUtil.getBean(actionSpringBeanID);
            }
        }
        return null;
    }


    public ActionColumnConfiguration getStatusConfiguration(GridViewConfiguration config)
    {
        if(config != null)
        {
            String actionSpringBeanID = config.getSpecialactionSpringBeanID();
            if(actionSpringBeanID != null)
            {
                return (ActionColumnConfiguration)SpringUtil.getBean(actionSpringBeanID);
            }
        }
        return null;
    }
}
