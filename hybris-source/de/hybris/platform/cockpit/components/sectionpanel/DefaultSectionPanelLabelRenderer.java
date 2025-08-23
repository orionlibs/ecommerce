package de.hybris.platform.cockpit.components.sectionpanel;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.dragdrop.DragAndDropWrapper;
import de.hybris.platform.cockpit.services.dragdrop.DraggedItem;
import de.hybris.platform.cockpit.services.dragdrop.impl.DefaultDraggedItem;
import de.hybris.platform.cockpit.session.UIEditorArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.jalo.Item;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class DefaultSectionPanelLabelRenderer implements SectionPanelLabelRenderer
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSectionPanelLabelRenderer.class);
    protected static final String SECTION_PANEL_LABEL_DIV_SCLASS = "sectionPanelLabelDiv";
    private final UIEditorArea editorArea;


    public DefaultSectionPanelLabelRenderer(UIEditorArea editorArea)
    {
        if(editorArea == null)
        {
            throw new IllegalArgumentException("Editor area can not be null.");
        }
        this.editorArea = editorArea;
    }


    public void render(String label, String imageUrl, Component parent)
    {
        Div mainDiv = new Div();
        mainDiv.setParent(parent);
        mainDiv.setSclass("sectionPanelLabelDiv");
        mainDiv.setDroppable("PerspectiveDND");
        mainDiv.setDraggable("PerspectiveDND");
        mainDiv.addEventListener("onDrop", (EventListener)new Object(this));
        DragAndDropWrapper wrapper = getEditorArea().getPerspective().getDragAndDropWrapperService().getWrapper();
        wrapper.attachDraggedItem((DraggedItem)new DefaultDraggedItem(getEditorArea().getCurrentObject()), (Component)mainDiv);
        Div labelDiv = new Div();
        labelDiv.setParent((Component)mainDiv);
        labelDiv.appendChild((Component)new Label(label));
        TypedObject currentObject = getEditorArea().getCurrentObject();
        if(currentObject != null)
        {
            Object object = currentObject.getObject();
            if(object instanceof ItemModel)
            {
                ItemModel itemModel = (ItemModel)object;
                Object source = UISessionUtils.getCurrentSession().getModelService().getSource(itemModel);
                if(source instanceof Item && CatalogManager.getInstance().isCatalogItem((Item)source))
                {
                    CatalogVersion catalogVersionJalo = CatalogManager.getInstance().getCatalogVersion((Item)source);
                    if(catalogVersionJalo != null)
                    {
                        CatalogVersionModel catVersion = (CatalogVersionModel)TypeTools.getModelService().get(catalogVersionJalo.getPK());
                        String mnemonic = catVersion.getMnemonic();
                        if(!StringUtils.isBlank(mnemonic))
                        {
                            Label mnemLabel = new Label(" (" + mnemonic + ")");
                            mnemLabel.setParent((Component)labelDiv);
                            mnemLabel.setSclass("catalog-mnemonic-label");
                        }
                    }
                }
            }
        }
    }


    protected UIEditorArea getEditorArea()
    {
        return this.editorArea;
    }
}
