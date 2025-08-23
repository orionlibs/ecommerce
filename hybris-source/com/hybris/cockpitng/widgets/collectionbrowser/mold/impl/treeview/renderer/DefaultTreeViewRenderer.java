/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.treeview.renderer;

import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.CockpitConfigurationNotFoundException;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Base;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.i18n.FallbackLocaleProvider;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.services.media.ObjectPreview;
import com.hybris.cockpitng.services.media.ObjectPreviewService;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.common.AbstractMoldStrategy;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.treeview.TreeViewCollectionBrowserNode;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.ProxyRenderer;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

/**
 * Renders tree entries in two column form: preview and label. If it gets a node with {@link java.lang.String} as its
 * underlying data object, it treats it as localized label key.
 */
public class DefaultTreeViewRenderer extends AbstractWidgetComponentRenderer<Treeitem, Void, TreeViewCollectionBrowserNode>
{
    protected static final String SCLASS_TREEVIEW_CELL = "yw-treeview-cell";
    protected static final String SCLASS_TREEVIEW_CELL_PREVIEW = "yw-treeview-cell-preview";
    protected static final String SCLASS_TREEVIEW_CELL_DATA_ATTRIBUTE = "yw-treeview-cell-data-attr";
    protected static final String SCLASS_TREEVIEW_CELL_LABEL = "yw-treeview-cell-label";
    protected static final String SCLASS_TREEVIEW_CELL_READ_RESTRICTED = "yw-treeview-cell-restricted";
    protected static final String SCLASS_TREEVIEW_ROW_SELECTED = "yw-treeview-row-sel";
    protected static final String SCLASS_TREEVIEW_ROW_TOPNODE = "yw-treeview-row-topnode";
    protected static final String SCLASS_TREEVIEW_ROW_SUBNODE = "yw-treeview-row-subnode";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultTreeViewRenderer.class);
    private TypeFacade typeFacade;
    private CockpitLocaleService cockpitLocaleService;
    private LabelService labelService;
    private ObjectPreviewService objectPreviewService;
    private FallbackLocaleProvider fallbackLocaleProvider;


    @Override
    public void render(final Treeitem item, final Void config, final TreeViewCollectionBrowserNode entry, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        if(shouldRenderNode(entry, dataType))
        {
            final Treerow treeRow = prepareAndAppendEmptyTreeRow(item);
            if(isString(dataType))
            {
                renderLocalizedLabelNode(item, entry, treeRow, widgetInstanceManager);
            }
            else
            {
                renderItemNode(item, entry, treeRow, dataType, widgetInstanceManager);
            }
            fireComponentRendered(treeRow, item, config, entry);
            fireComponentRendered(item, item, config, entry);
        }
    }


    protected boolean shouldRenderNode(final TreeViewCollectionBrowserNode entry, final DataType dataType)
    {
        return isString(dataType) || (entry != null && !isLeafAndDataAttribute(entry));
    }


    private static boolean isString(final DataType dataType)
    {
        return dataType != null && String.class.getName().equals(dataType.getCode());
    }


    private boolean isLeafAndDataAttribute(final TreeViewCollectionBrowserNode entry)
    {
        return entry.isLeaf() && entry.getData() instanceof DataAttribute;
    }


    protected void renderLocalizedLabelNode(final Treeitem item, final TreeViewCollectionBrowserNode entry, final Treerow treeRow,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        final String labelValue = widgetInstanceManager.getLabel(entry.getData());
        final Label label = labelValue != null ? new Label(labelValue) : new Label(entry.getData());
        final Treecell labelTreeCell = new Treecell();
        labelTreeCell.appendChild(label);
        treeRow.appendChild(labelTreeCell);
        item.setDisabled(true);
    }


    protected void renderItemNode(final Treeitem item, final TreeViewCollectionBrowserNode entry, final Treerow treeRow,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final Treecell contentTreeCell = new Treecell();
        final WidgetComponentRenderer<Treecell, Void, TreeViewCollectionBrowserNode> cellRenderer = createCellRenderer();
        new ProxyRenderer<>(this, item, null, entry).render(cellRenderer, contentTreeCell, null, entry, dataType,
                        widgetInstanceManager);
        treeRow.appendChild(contentTreeCell);
        appendStyleToTreeItemByNestingLevel(treeRow, entry);
        if(!entry.isAccessible())
        {
            UITools.modifySClass(treeRow, SCLASS_TREEVIEW_CELL_READ_RESTRICTED, true);
            item.setDisabled(true);
        }
        fireComponentRendered(contentTreeCell, item, null, entry);
    }


    protected WidgetComponentRenderer<Treecell, Void, TreeViewCollectionBrowserNode> createCellRenderer()
    {
        return new AbstractWidgetComponentRenderer<Treecell, Void, TreeViewCollectionBrowserNode>()
        {
            @Override
            public void render(final Treecell parent, final Void configuration, final TreeViewCollectionBrowserNode data,
                            final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
            {
                Div previewDiv = new Div();
                previewDiv.setSclass(SCLASS_TREEVIEW_CELL);
                UITools.modifySClass(previewDiv, SCLASS_TREEVIEW_CELL_PREVIEW, true);
                Div labelDiv = new Div();
                labelDiv.setSclass(SCLASS_TREEVIEW_CELL);
                final Consumer<HtmlBasedComponent> fireConsumer = cmp -> fireComponentRendered(cmp, parent, null, data);
                if(data.getData() instanceof DataAttribute)
                {
                    labelDiv = renderDataAttribute(labelDiv, data, fireConsumer);
                }
                else
                {
                    if(data.isAccessible())
                    {
                        previewDiv = renderEntryPreview(previewDiv, data, dataType, widgetInstanceManager, fireConsumer);
                        labelDiv = renderEntryLabel(labelDiv, data, fireConsumer);
                    }
                    else
                    {
                        final Label label = new Label(getLabelService().getAccessDeniedLabel(data));
                        labelDiv.appendChild(label);
                        fireConsumer.accept(label);
                    }
                }
                parent.appendChild(previewDiv);
                parent.appendChild(labelDiv);
            }
        };
    }


    private static Treerow prepareAndAppendEmptyTreeRow(final Treeitem item)
    {
        Treerow treeRow = item.getTreerow();
        if(treeRow == null)
        {
            treeRow = new Treerow();
            item.appendChild(treeRow);
        }
        treeRow.getChildren().clear();
        return treeRow;
    }


    /**
     * Renders an attribute
     *
     * @param labelDiv
     *           <code>div</code> component that is to contain attribute's data
     * @param entry
     *           tree node on which it will be displayed
     * @param trigger
     *           notification consumer for each component rendered
     * @return <code>div</code> component that is containing all that needs to be displayed (by default if would be the same
     *         that <code>labelDiv</code>)
     */
    protected Div renderDataAttribute(final Div labelDiv, final TreeViewCollectionBrowserNode entry,
                    final Consumer<HtmlBasedComponent> trigger)
    {
        UITools.modifySClass(labelDiv, SCLASS_TREEVIEW_CELL_DATA_ATTRIBUTE, true);
        final DataAttribute dataAttribute = entry.getData();
        String label = dataAttribute.getLabel(getCockpitLocaleService().getCurrentLocale());
        if(StringUtils.isBlank(label))
        {
            label = getFallbackLabel(dataAttribute);
        }
        final Label labelComponent = new Label(label);
        labelComponent.setAttribute(AbstractMoldStrategy.ATTRIBUTE_HYPERLINK_CANDIDATE, Boolean.TRUE);
        labelDiv.appendChild(labelComponent);
        trigger.accept(labelComponent);
        return labelDiv;
    }


    private String getFallbackLabel(final DataAttribute dataAttribute)
    {
        if(getFallbackLocaleProvider() != null)
        {
            final Map<Locale, String> map = new HashMap();
            for(final Locale fallbackLocale : getFallbackLocaleProvider()
                            .getFallbackLocales(getCockpitLocaleService().getCurrentLocale()))
            {
                final String fallbackLabel = dataAttribute.getLabel(fallbackLocale);
                if(StringUtils.isNotBlank(fallbackLabel))
                {
                    map.put(fallbackLocale, fallbackLabel);
                    dataAttribute.getAllLabels().putAll(map);
                    return fallbackLabel;
                }
            }
        }
        final String englishFallbacklabel = dataAttribute.getLabel(Locale.ENGLISH);
        if(StringUtils.isNotBlank(englishFallbacklabel) && LOG.isDebugEnabled())
        {
            LOG.debug("English locale used to resolve label for attribute: " + dataAttribute.getQualifier());
            return englishFallbacklabel;
        }
        return "";
    }


    /**
     * Renders a preview image of provided entry.
     *
     * @param previewDiv
     *           <code>div</code> component on which to render
     * @param entry
     *           tree node on which it will be displayed
     * @param dataType
     *           type of data which preview is to be rendered
     * @param widgetInstanceManager
     *           widget manager in scope of which renderer is used
     * @param trigger
     *           notification consumer for each component rendered
     * @return <code>div</code> component that is containing all that needs to be displayed (by default if would be the same
     *         that <code>previewDiv</code>)
     */
    protected Div renderEntryPreview(final Div previewDiv, final TreeViewCollectionBrowserNode entry, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager, final Consumer<HtmlBasedComponent> trigger)
    {
        if(dataType != null)
        {
            final ObjectPreview preview = objectPreviewService.getPreview(entry.getData(),
                            loadBaseConfiguration(dataType.getCode(), widgetInstanceManager));
            if(preview != null)
            {
                final Image image = new Image(preview.getUrl());
                UITools.modifySClass(image, "ye-listview-def-preview-img", true);
                trigger.accept(image);
                previewDiv.appendChild(image);
                preparePreviewPopup(previewDiv, preview, image);
            }
        }
        return previewDiv;
    }


    private static Base loadBaseConfiguration(final String typeCode, final WidgetInstanceManager widgetInstanceManager)
    {
        Base config = null;
        final DefaultConfigContext configContext = new DefaultConfigContext("base");
        configContext.setType(typeCode);
        try
        {
            config = widgetInstanceManager.loadConfiguration(configContext, Base.class);
            if(config == null)
            {
                LOG.warn("Loaded UI configuration is null. Ignoring.");
            }
        }
        catch(final CockpitConfigurationNotFoundException ccnfe)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Could not find UI configuration for given context (" + configContext + ").", ccnfe);
            }
        }
        catch(final CockpitConfigurationException cce)
        {
            LOG.error("Could not load cockpit config for the given context '" + configContext + "'.", cce);
        }
        return config;
    }


    private void preparePreviewPopup(final Div parent, final ObjectPreview preview, final Image target)
    {
        if(preview.isFallback())
        {
            return;
        }
        final Popup zoomPopup = new Popup();
        final Image popupImage = new Image(preview.getUrl());
        UITools.modifySClass(popupImage, "ye-listview-preview-popup-image", true);
        zoomPopup.appendChild(popupImage);
        parent.appendChild(zoomPopup);
        target.addEventListener(Events.ON_MOUSE_OVER, event -> zoomPopup.open(target, "before_start"));
        target.addEventListener(Events.ON_MOUSE_OUT, event -> zoomPopup.close());
    }


    /**
     * Renders a label for provided object.
     *
     * @param labelDiv
     *           <code>div</code> component that is to contain data's label
     * @param entry
     *           tree node on which it will be displayed
     * @param trigger
     *           notification consumer for each component rendered
     * @return <code>div</code> component that is containing all that needs to be displayed (by default if would be the same
     *         that <code>labelDiv</code>)
     */
    protected Div renderEntryLabel(final Div labelDiv, final TreeViewCollectionBrowserNode entry,
                    final Consumer<HtmlBasedComponent> trigger)
    {
        UITools.modifySClass(labelDiv, SCLASS_TREEVIEW_CELL_LABEL, true);
        final String labelValue = getLabelService().getObjectLabel(entry.getData());
        final Label label = labelValue != null ? new Label(labelValue) : new Label(entry.getData());
        trigger.accept(label);
        labelDiv.appendChild(label);
        return labelDiv;
    }


    protected void appendStyleToTreeItemByNestingLevel(final Treerow row, final TreeViewCollectionBrowserNode entry)
    {
        if(TreeViewCollectionBrowserNode.TOP_LEVEL >= entry.getLevel())
        {
            UITools.modifySClass(row, SCLASS_TREEVIEW_ROW_TOPNODE, true);
            UITools.modifySClass(row, SCLASS_TREEVIEW_ROW_SUBNODE, false);
        }
        else
        {
            UITools.modifySClass(row, SCLASS_TREEVIEW_ROW_TOPNODE, false);
            UITools.modifySClass(row, SCLASS_TREEVIEW_ROW_SUBNODE, true);
        }
    }


    public TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    @Required
    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    public CockpitLocaleService getCockpitLocaleService()
    {
        return cockpitLocaleService;
    }


    @Required
    public void setCockpitLocaleService(final CockpitLocaleService cockpitLocaleService)
    {
        this.cockpitLocaleService = cockpitLocaleService;
    }


    public LabelService getLabelService()
    {
        return labelService;
    }


    @Required
    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }


    public ObjectPreviewService getObjectPreviewService()
    {
        return objectPreviewService;
    }


    @Required
    public void setObjectPreviewService(final ObjectPreviewService objectPreviewService)
    {
        this.objectPreviewService = objectPreviewService;
    }


    public FallbackLocaleProvider getFallbackLocaleProvider()
    {
        return fallbackLocaleProvider;
    }


    @Required
    public void setFallbackLocaleProvider(final FallbackLocaleProvider fallbackLocaleProvider)
    {
        this.fallbackLocaleProvider = fallbackLocaleProvider;
    }
}
