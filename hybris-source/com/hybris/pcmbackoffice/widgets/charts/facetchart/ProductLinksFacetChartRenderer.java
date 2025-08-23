package com.hybris.pcmbackoffice.widgets.charts.facetchart;

import com.hybris.backoffice.widgets.advancedsearch.AdvancedSearchMode;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import com.hybris.cockpitng.search.data.pageable.FullTextSearchPageable;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.A;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class ProductLinksFacetChartRenderer implements FacetChartRightPanelRenderer
{
    protected static final String SOCKET_GO_TO_ALL_PRODUCT = "goToAllProducts";
    protected static final String SOCKET_ADD_NEW_PRODUCT = "addNewProduct";
    protected static final String MODEL_FILTERS_COUNTER = "filtersCounter";
    protected static final String SCSS_TOTAL_PRODUCT_CONTAINER = "yw-solrfacetchart-rightpanel-container";
    protected static final String SCSS_TOTAL_PRODUCT_BUTTONS_CONTAINER = "yw-solrfacetchart-rightpanel-container-buttons";
    protected static final String SCSS_TOTAL_PRODUCT_COUNT = "yw-solrfacetchart-rightpanel-total-product-count";
    protected static final String SCSS_TOTAL_PRODUCT_COUNT_LABEL = "yw-solrfacetchart-rightpanel-total-product-count-label";
    protected static final String SCSS_GO_TO_ALL_PRODUCTS_BUTTON = "yw-solrfacetchart-rightpanel-go-to-all-products-button";
    protected static final String SCSS_ADD_NEW_PRODUCT_BUTTON = "yw-solrfacetchart-rightpanel-add-new-product-button";
    private static final String LABEL_BROWSE_ALL_PRODUCTS = "solrchart.productlinks.browase_all_product";
    private static final String LABEL_ADD_NEW_PRODUCT = "solrchart.productlinks.add_new_product";
    private static final String LABEL_TOTAL_PRODUCTS = "solrchart.productlinks.total_products";


    public void render(Div parent, WidgetInstanceManager widgetInstanceManager, FullTextSearchPageable fullTextSearchPagable)
    {
        parent.getChildren().clear();
        Div container = new Div();
        container.setParent((Component)parent);
        container.setSclass("yw-solrfacetchart-rightpanel-container");
        Label totalProductCount = new Label((fullTextSearchPagable != null) ? ("" + fullTextSearchPagable.getTotalCount()) : "0");
        totalProductCount.setParent((Component)container);
        totalProductCount.setSclass("yw-solrfacetchart-rightpanel-total-product-count");
        Label totalProductCountLabel = new Label(getTotalProductCountLabelValue(widgetInstanceManager));
        totalProductCountLabel.setParent((Component)container);
        totalProductCountLabel.setSclass("yw-solrfacetchart-rightpanel-total-product-count-label");
        Div buttonsContainer = new Div();
        buttonsContainer.setParent((Component)container);
        buttonsContainer.setSclass("yw-solrfacetchart-rightpanel-container-buttons");
        A goToAllProducts = new A(Labels.getLabel("solrchart.productlinks.browase_all_product"));
        goToAllProducts.setParent((Component)buttonsContainer);
        goToAllProducts.addEventListener("onClick", event -> goToAllProductsClick(widgetInstanceManager));
        goToAllProducts.setSclass("yw-solrfacetchart-rightpanel-go-to-all-products-button");
        A addNewProduct = new A(Labels.getLabel("solrchart.productlinks.add_new_product"));
        addNewProduct.setParent((Component)buttonsContainer);
        addNewProduct.addEventListener("onClick", event -> addNewProduct(widgetInstanceManager));
        addNewProduct.setSclass("yw-solrfacetchart-rightpanel-add-new-product-button");
    }


    private void addNewProduct(WidgetInstanceManager widgetInstanceManager)
    {
        widgetInstanceManager.sendOutput("addNewProduct", "Product");
    }


    private void goToAllProductsClick(WidgetInstanceManager widgetInstanceManager)
    {
        AdvancedSearchData queryData = createEmptySearchData();
        widgetInstanceManager.sendOutput("goToAllProducts", queryData);
    }


    private AdvancedSearchData createEmptySearchData()
    {
        AdvancedSearchData queryData = new AdvancedSearchData();
        queryData.setTypeCode("Product");
        queryData.setSearchQueryText("");
        queryData.setTokenizable(true);
        queryData.setAdvancedSearchMode(AdvancedSearchMode.SIMPLE);
        queryData.setIncludeSubtypes(Boolean.valueOf(true));
        queryData.setGlobalOperator(ValueComparisonOperator.OR);
        return queryData;
    }


    private boolean isAnyFacetSelected(WidgetInstanceManager widgetInstanceManager)
    {
        Integer numberOfFilters = (Integer)widgetInstanceManager.getModel().getValue("filtersCounter", Integer.class);
        return (numberOfFilters != null && numberOfFilters.intValue() > 0);
    }


    private String getTotalProductCountLabelValue(WidgetInstanceManager widgetInstanceManager)
    {
        boolean isSelectedFacets = isAnyFacetSelected(widgetInstanceManager);
        String totalProductCountLabelValue = Labels.getLabel("solrchart.productlinks.total_products");
        return isSelectedFacets ? (totalProductCountLabelValue + "*") : totalProductCountLabelValue;
    }
}
