package de.hybris.platform.cockpit.components.duallistbox;

import de.hybris.platform.cockpit.components.search.SearchTextboxDiv;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Table;
import org.zkoss.zhtml.Td;
import org.zkoss.zhtml.Tr;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.SimpleListModel;

public abstract class AbstractDualListboxEditor<K>
{
    protected Listbox collectionAssignedItems;
    protected List<K> assignedValuesList;
    protected Listbox collectionAllItems;
    protected boolean singleSelector = false;
    protected SearchTextboxDiv inputComponentDiv;
    private TypeService typeService = null;
    private static final int DEFAULT_AUTOCOMPLETE_TIMEOUT = 10;


    protected abstract String getSearchInfoBox();


    protected abstract void updateCollectionAllItems();


    public abstract EventListener getOnUserSearchListener();


    protected abstract void parseParams(Map<String, ? extends Object> paramMap);


    public AbstractDualListboxEditor(List<K> assignedValuesList)
    {
        this.assignedValuesList = assignedValuesList;
    }


    public HtmlBasedComponent createComponentView(Map<String, ? extends Object> parameters)
    {
        Div mainComponentDiv = new Div();
        parseParams(parameters);
        mainComponentDiv.appendChild((Component)createInternalComponentView());
        return (HtmlBasedComponent)mainComponentDiv;
    }


    protected Table createInternalComponentView()
    {
        Table cellContainer = new Table();
        cellContainer.setSclass("dualbox");
        Tr topRow = new Tr();
        topRow.setParent((Component)cellContainer);
        Tr bottomRow = new Tr();
        bottomRow.setParent((Component)cellContainer);
        Td leftTopCell = new Td();
        leftTopCell.setSclass("dualbox_search_component");
        leftTopCell.setParent((Component)topRow);
        leftTopCell.appendChild((Component)fillLeftTopCell());
        Td leftBottomCell = new Td();
        leftBottomCell.setParent((Component)bottomRow);
        leftBottomCell.appendChild((Component)fillLeftBottomCell());
        Td midTopCell = new Td();
        midTopCell.appendChild((Component)fillMidTopCell());
        midTopCell.setStyle("text-align:center");
        midTopCell.setParent((Component)topRow);
        Td midBottomCell = new Td();
        midBottomCell.appendChild((Component)fillMidBottomCell());
        midBottomCell.setStyle("text-align:center");
        midBottomCell.setParent((Component)bottomRow);
        Td rightTopCell = new Td();
        rightTopCell.appendChild((Component)fillRightTopCell());
        rightTopCell.setParent((Component)topRow);
        Td rightBottomCell = new Td();
        rightBottomCell.setStyle("width:230px;");
        rightBottomCell.appendChild((Component)fillRightBottomCell());
        rightBottomCell.setParent((Component)bottomRow);
        return cellContainer;
    }


    protected HtmlBasedComponent fillLeftBottomCell()
    {
        Div div = new Div();
        Label infoLabel = getAvailableValuesLabel();
        infoLabel.setSclass("dualbox_column_header");
        infoLabel.setParent((Component)div);
        prepareCollectionAllItems();
        this.collectionAllItems.setParent((Component)div);
        return (HtmlBasedComponent)div;
    }


    protected HtmlBasedComponent fillRightBottomCell()
    {
        Div div = new Div();
        Label infoLabel = getAssignedValuesLabel();
        infoLabel.setSclass("dualbox_column_header");
        infoLabel.setParent((Component)div);
        prepareCollectionAssignedItems();
        this.collectionAssignedItems.setParent((Component)div);
        return (HtmlBasedComponent)div;
    }


    protected HtmlBasedComponent fillMidTopCell()
    {
        return (HtmlBasedComponent)new Div();
    }


    protected HtmlBasedComponent fillMidBottomCell()
    {
        return (HtmlBasedComponent)createAssignBtn();
    }


    protected HtmlBasedComponent fillRightTopCell()
    {
        return (HtmlBasedComponent)new Div();
    }


    protected HtmlBasedComponent fillLeftTopCell()
    {
        Div mainDiv = new Div();
        this.inputComponentDiv = new SearchTextboxDiv(getSearchInfoBox());
        prepareSearchInputComponent();
        this.inputComponentDiv.setClass("dualbox_search_component_div");
        this.inputComponentDiv.setParent((Component)mainDiv);
        return (HtmlBasedComponent)mainDiv;
    }


    private void prepareSearchInputComponent()
    {
        String onkeyUpAction =
                        "onkeyup: var pressedKey = event.keyCode; if (typeof deferredSender !== 'undefined') clearTimeout(deferredSender);  if(  event.keyCode == 0 )     pressedKey = event.charCode; var notAllowedCodes = new Array(13,16,17,27,35,36,37,39);for(var item in notAllowedCodes ) if(notAllowedCodes[item]==pressedKey)\treturn true; var inputValue = this.value; deferredSender = setTimeout( function() { comm.sendUser('"
                                        + this.inputComponentDiv.getSearchInputComponent().getId() + "',inputValue,pressedKey); }, 10);";
        this.inputComponentDiv.getSearchInputComponent().setAction(onkeyUpAction);
        this.inputComponentDiv.getSearchInputComponent().addEventListener("onUser", getOnUserSearchListener());
    }


    protected void setResultListData(Listbox collectionItems, List<K> resultList)
    {
        collectionItems.setModel((ListModel)new SimpleListModel(resultList));
        collectionItems.setRows(resultList.size());
    }


    protected Image createAssignBtn()
    {
        Image assignImage = new Image("/cockpit/images/single_reference_selector_arrow_left.png");
        assignImage.setTooltiptext(Labels.getLabel("duallistbox.assign.button.tooltip"));
        assignImage.addEventListener("onClick", (EventListener)new Object(this));
        return assignImage;
    }


    protected void addToAssignedValuesList(Object obj, Listitem item)
    {
        if(item != null)
        {
            List<Listitem> assignedItems = this.collectionAssignedItems.getItems();
            List allItems = this.collectionAllItems.getItems();
            if(!assignedItems.contains(item))
            {
                assignedItems.add(item);
                List<K> myList = new ArrayList<>();
                if(!this.singleSelector)
                {
                    myList.addAll(getAssignedValuesList());
                }
                myList.add((K)obj);
                this.assignedValuesList = myList;
                setResultListData(this.collectionAssignedItems, this.assignedValuesList);
                this.collectionAssignedItems.setSelectedIndex(this.assignedValuesList.size() - 1);
                allItems.remove(item);
                updateCollectionAllItems();
            }
        }
    }


    protected void prepareCollectionAssignedItems()
    {
        this.collectionAssignedItems = new Listbox();
        this.collectionAssignedItems.setDisabled(true);
        this.collectionAssignedItems.setFixedLayout(false);
        this.collectionAssignedItems.setItemRenderer(getAssignedCollectionItemListRenderer());
        setResultListData(this.collectionAssignedItems, this.assignedValuesList);
        this.collectionAssignedItems.setStyle("overflow:auto;");
        this.collectionAssignedItems.setWidth("230px");
        this.collectionAssignedItems.setHeight("410px");
        this.collectionAssignedItems.setDroppable("availableListItem");
        this.collectionAssignedItems.addEventListener("onDrop", (EventListener)new Object(this));
    }


    protected void prepareCollectionAllItems()
    {
        this.collectionAllItems = new Listbox();
        this.collectionAllItems.setDisabled(true);
        this.collectionAllItems.setItemRenderer(getAvailableCollectionItemListRenderer());
        updateCollectionAllItems();
        this.collectionAllItems.setStyle("overflow:auto;");
        this.collectionAllItems.setWidth("230px");
        this.collectionAllItems.setHeight("410px");
    }


    protected EventListener getRemoveButtonListener()
    {
        return (EventListener)new Object(this);
    }


    protected ListitemRenderer getAvailableCollectionItemListRenderer()
    {
        return (ListitemRenderer)new AllCollectionListitemRenderer();
    }


    protected ListitemRenderer getAssignedCollectionItemListRenderer()
    {
        AssignedCollectionListitemRenderer renderer = new AssignedCollectionListitemRenderer();
        renderer.setListener(getRemoveButtonListener());
        return (ListitemRenderer)renderer;
    }


    protected Label getAvailableValuesLabel()
    {
        return new Label();
    }


    protected Label getAssignedValuesLabel()
    {
        return new Label();
    }


    public List<K> getAssignedValuesList()
    {
        return this.assignedValuesList;
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }


    protected boolean isSingleSelector()
    {
        return this.singleSelector;
    }


    public void setSingleSelector(boolean singleSelector)
    {
        this.singleSelector = singleSelector;
    }
}
