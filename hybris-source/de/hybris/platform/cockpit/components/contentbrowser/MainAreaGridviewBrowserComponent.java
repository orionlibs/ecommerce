package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.model.general.ListComponentModelListener;
import de.hybris.platform.cockpit.model.general.ListModel;
import de.hybris.platform.cockpit.model.general.UIItemView;
import de.hybris.platform.cockpit.model.gridview.GridItemRenderer;
import de.hybris.platform.cockpit.model.gridview.GridViewListener;
import de.hybris.platform.cockpit.model.gridview.UIGridView;
import de.hybris.platform.cockpit.model.gridview.impl.GridView;
import de.hybris.platform.cockpit.services.dragdrop.DragAndDropContext;
import de.hybris.platform.cockpit.services.dragdrop.impl.DefaultDragAndDropContext;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.util.UITools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class MainAreaGridviewBrowserComponent extends AbstractMainAreaBrowserComponent
{
    private static final Logger log = LoggerFactory.getLogger(MainAreaGridviewBrowserComponent.class);
    protected transient UIGridView gridView = null;
    private transient GridItemRenderer gridItemRenderer = null;
    private GridViewListener gridViewListener = (GridViewListener)new Object(this);
    private final ListComponentModelListener listComponentModelListener = (ListComponentModelListener)new Object(this);


    public MainAreaGridviewBrowserComponent(AdvancedBrowserModel model, AbstractContentBrowser contentBrowser)
    {
        super(model, contentBrowser);
    }


    public boolean update()
    {
        boolean success = false;
        if(this.initialized)
        {
            loadListModel();
            resetSelection();
            this.listComponentModel.setListModel((ListModel)getResultModel());
            UIItemView currentView = null;
            this.gridView = loadGridView();
            UIGridView uIGridView = this.gridView;
            if(uIGridView == null)
            {
                this.mainArea.getChildren().clear();
                this.mainArea.appendChild((Component)new Label(Labels.getLabel("browser.emptymessage")));
            }
            else
            {
                if(this.mainArea.getChildren().isEmpty() || !this.mainArea.getFirstChild().equals(uIGridView))
                {
                    this.mainArea.getChildren().clear();
                    this.mainArea.appendChild((Component)uIGridView);
                }
                if(this.listComponentModel != null)
                {
                    this.listComponentModel.setSelectedIndexesDirectly(getModel().getSelectedIndexes());
                    this.listComponentModel.setActiveItem(getModel().getActiveItem());
                }
                success = true;
            }
            getContentBrowser().updateStatusBar();
        }
        else
        {
            success = initialize();
        }
        return success;
    }


    protected Div createMainArea()
    {
        Div marginHelper = new Div();
        marginHelper.setStyle("margin: 0px; background: white;");
        UITools.maximize((HtmlBasedComponent)marginHelper);
        Div mainContainer = new Div();
        mainContainer.setWidth("99%");
        mainContainer.setHeight("99%");
        loadListModel();
        this.gridView = loadGridView();
        if(getResultModel() == null || getResultModel().isEmpty())
        {
            mainContainer.appendChild((Component)new Label(Labels.getLabel("browser.emptymessage")));
        }
        else
        {
            mainContainer.appendChild((Component)this.gridView);
        }
        this.mainArea = mainContainer;
        marginHelper.appendChild((Component)mainContainer);
        return marginHelper;
    }


    protected String getCustomStyleClass()
    {
        return "grid_default";
    }


    protected String getGridConfigCode()
    {
        return null;
    }


    protected UIGridView loadGridView()
    {
        UIGridView gridView = null;
        if(getModel() != null)
        {
            if(this.gridView == null)
            {
                GridView gridView1 = new GridView();
                gridView1.setSclass(getCustomStyleClass());
                gridView1.setConfigContextCode(getGridConfigCode());
                gridView1.addGridViewListener(this.gridViewListener);
                gridView1.setHeight("100%");
                gridView1.setWidth("100%");
                gridView1.setDDContext((DragAndDropContext)new DefaultDragAndDropContext((BrowserModel)getModel()));
                if(getGridItemRenderer() != null)
                {
                    gridView1.setGridItemRenderer(getGridItemRenderer());
                }
            }
            else
            {
                gridView = this.gridView;
            }
            updateResult();
            this.lastResultType = getRootType();
            gridView.setRootType(this.lastResultType);
            this.listComponentModel.addListComponentModelListener(this.listComponentModelListener);
            gridView.setModel(this.listComponentModel);
            this.listComponentModel.setSelectedIndexes(getModel().getSelectedIndexes());
            this.listComponentModel.setActiveItem(getModel().getActiveItem());
        }
        else
        {
            this.lastResultType = null;
        }
        return gridView;
    }


    protected void cleanup()
    {
        if(this.listComponentModel != null)
        {
            this.listComponentModel.removeListComponentModelListener(this.listComponentModelListener);
        }
        if(this.gridView != null)
        {
            this.gridView.removeGridViewListener(this.gridViewListener);
        }
    }


    protected UIItemView getCurrentItemView()
    {
        return (UIItemView)this.gridView;
    }


    public void setGridItemRenderer(GridItemRenderer gridItemRenderer)
    {
        this.gridItemRenderer = gridItemRenderer;
    }


    public GridItemRenderer getGridItemRenderer()
    {
        return this.gridItemRenderer;
    }


    public void setGridViewListener(GridViewListener gridViewListener)
    {
        this.gridViewListener = gridViewListener;
    }
}
