package de.hybris.platform.cockpit.components.navigationarea.renderer;

import de.hybris.platform.cockpit.constants.GeneratedCockpitConstants;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.CockpitEventAcceptor;
import de.hybris.platform.cockpit.model.CockpitObjectSpecialCollectionModel;
import de.hybris.platform.cockpit.model.collection.ObjectCollection;
import de.hybris.platform.cockpit.model.query.impl.UICollectionQuery;
import de.hybris.platform.cockpit.services.dragdrop.DragAndDropWrapper;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitNavigationArea;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.model.ItemModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;

public class SpecialCollectionRenderer implements ListitemRenderer, CockpitEventAcceptor
{
    public static final String COCKPITGROUP_UID = "cockpitgroup";
    private BaseUICockpitNavigationArea navigationArea;
    private Label label;
    private static final Logger log = LoggerFactory.getLogger(SpecialCollectionRenderer.class.getName());


    public SpecialCollectionRenderer(BaseUICockpitNavigationArea navigationArea)
    {
        setNavigationArea(navigationArea);
    }


    protected DragAndDropWrapper getDDWrapper()
    {
        return getNavigationArea().getPerspective().getDragAndDropWrapperService().getWrapper();
    }


    public void render(Listitem item, Object data) throws Exception
    {
        Listcell cell = new Listcell();
        Hbox hbox = new Hbox();
        hbox.setSpacing("0");
        hbox.setWidths("3,100%,3");
        Div div = new Div();
        div.setSclass("navigation-query-left");
        hbox.appendChild((Component)div);
        div = new Div();
        div.setSclass("navigation-query-center");
        hbox.appendChild((Component)div);
        div = new Div();
        div.setSclass("navigation-query-right");
        hbox.appendChild((Component)div);
        cell.appendChild((Component)hbox);
        Div labelDiv = new Div();
        labelDiv.setSclass("navigation-query-label");
        this.label = new Label();
        Textbox pasteOperationArea = null;
        UICollectionQuery coll = (UICollectionQuery)data;
        if(coll.getObjectCollection().getUser() == null)
        {
            UITools.modifySClass((HtmlBasedComponent)cell, "global-collection", true);
        }
        this.label.setValue(Labels.getLabel("specialcollection." + ((UICollectionQuery)data).getObjectCollection().getQualifier() + ".name") + " (" + Labels.getLabel("specialcollection." + ((UICollectionQuery)data).getObjectCollection().getQualifier() + ".name") + ")");
        cell.setDroppable("PerspectiveDND");
        cell.addEventListener("onDrop", (EventListener)new Object(this, coll, data));
        pasteOperationArea = new Textbox();
        pasteOperationArea.setSclass("paste_to_collection_input");
        pasteOperationArea.addEventListener("onChanging", (EventListener)new Object(this, coll));
        String actionJsBody = "onkeypress: var pressedKey = String.fromCharCode(event.keyCode).toLowerCase();  if(  event.keyCode == 0 )     pressedKey = String.fromCharCode(event.charCode).toLowerCase();  if( event.ctrlKey ) if( pressedKey == 'v' )     return true;  return false;";
        pasteOperationArea.setAction("onkeypress: var pressedKey = String.fromCharCode(event.keyCode).toLowerCase();  if(  event.keyCode == 0 )     pressedKey = String.fromCharCode(event.charCode).toLowerCase();  if( event.ctrlKey ) if( pressedKey == 'v' )     return true;  return false;");
        labelDiv.appendChild((Component)this.label);
        Textbox finalPasteOperationArea = pasteOperationArea;
        labelDiv.setAction("onclick:document.getElementById('" + pasteOperationArea.getId() + "').focus();");
        labelDiv.addEventListener("onClick", (EventListener)new Object(this, item, finalPasteOperationArea));
        labelDiv.addEventListener("onRightClick", (EventListener)new Object(this));
        labelDiv.appendChild((Component)pasteOperationArea);
        cell.appendChild((Component)labelDiv);
        item.appendChild((Component)cell);
        item.setValue(data);
    }


    public void setNavigationArea(BaseUICockpitNavigationArea navigationArea)
    {
        BaseUICockpitNavigationArea baseUICockpitNavigationArea = this.navigationArea;
        if(baseUICockpitNavigationArea != null)
        {
            baseUICockpitNavigationArea.removeCockpitEventAcceptor(this);
        }
        this.navigationArea = navigationArea;
        this.navigationArea.removeCockpitEventAcceptor(this);
        this.navigationArea.addCockpitEventAcceptor(this);
    }


    public BaseUICockpitNavigationArea getNavigationArea()
    {
        return this.navigationArea;
    }


    public void onCockpitEvent(CockpitEvent event)
    {
        if(event instanceof de.hybris.platform.cockpit.events.impl.ClipboardEvent)
        {
            ObjectCollection clipboard = (ObjectCollection)event.getSource();
            ItemModel model = (ItemModel)UISessionUtils.getCurrentSession().getModelService().get(clipboard.getPK());
            if(model instanceof CockpitObjectSpecialCollectionModel)
            {
                if(GeneratedCockpitConstants.Enumerations.CockpitSpecialCollectionType.CLIPBOARD
                                .equals(((CockpitObjectSpecialCollectionModel)model).getCollectionType().getCode()) && this.label != null &&
                                !UITools.isFromOtherDesktop((Component)this.label))
                {
                    this.label.setValue(Labels.getLabel("specialcollection." + clipboard.getQualifier() + ".name") + " (" + Labels.getLabel("specialcollection." + clipboard.getQualifier() + ".name") + ")");
                }
            }
        }
    }
}
