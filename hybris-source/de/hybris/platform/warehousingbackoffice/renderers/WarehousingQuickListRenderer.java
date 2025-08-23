package de.hybris.platform.warehousingbackoffice.renderers;

import com.hybris.backoffice.widgets.quicklist.renderer.DefaultQuickListItemRenderer;
import com.hybris.cockpitng.config.quicklist.jaxb.QuickList;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelUtils;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vbox;

public class WarehousingQuickListRenderer extends DefaultQuickListItemRenderer
{
    protected static final String SCLASS_HBOX_CONTAINER = "yw-quicklist-warehousing-hbox-container";
    protected static final String SCLASS_VBOX_CONTAINER = "yw-quicklist-warehousing-vbox-container";
    protected static final String SCLASS_YW_QUICK_LIST_TILE_TITLE_CUST = "yw-quicklist-warehousing--tile-title";
    protected static final String SCLASS_YW_QUICK_LIST_TILE_CUST = "yw-quicklist-warehousing--tile";
    protected static final String SCLASS_YW_QUICK_LIST_TILE_IMG_CUST = "yw-quicklist-warehousing--tile--img";
    protected static final String SCLASS_YW_QUICK_LIST_TILE_PROD_CODE = "yw-quicklist-warehousing--tile--prod-code";
    protected static final String SCLASS_YW_QUICK_LIST_TILE_PROD_NAME = "yw-quicklist-warehousing--tile--prod-name";
    protected static final String SCLASS_YW_QUICK_LIST_TILE_PROD_QTY = "yw-quicklist-warehousing--tile--prod-qty";


    public void render(HtmlBasedComponent parent, QuickList configuration, Object data, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        if(data instanceof WorkflowActionModel)
        {
            parent.setSclass("yw-quicklist-warehousing--tile");
            ConsignmentModel consignment = ((WorkflowActionModel)data).getAttachmentItems().iterator().next();
            Label consignmentCodeLabel = new Label(resolveLabel("warehousingbackoffice.quick.list.consignment.code") + " " + resolveLabel("warehousingbackoffice.quick.list.consignment.code"));
            consignmentCodeLabel.setSclass("yw-quicklist-warehousing--tile-title");
            parent.appendChild((Component)consignmentCodeLabel);
            consignment.getConsignmentEntries().stream().filter(consignmentEntry -> (consignmentEntry.getQuantity().longValue() != 0L))
                            .forEach(consignmentEntry -> renderConsignmentEntry(parent, consignmentEntry));
        }
    }


    protected Label labelizeProperty(String attributeToRender)
    {
        Label attributeLabel = new Label(attributeToRender);
        attributeLabel.setSclass("yw-quicklist-tile-subtitle");
        return attributeLabel;
    }


    protected String resolveLabel(String labelKey)
    {
        String defaultValue = LabelUtils.getFallbackLabel(labelKey);
        return Labels.getLabel(labelKey, defaultValue);
    }


    protected void renderConsignmentEntry(HtmlBasedComponent parent, ConsignmentEntryModel consignmentEntry)
    {
        ProductModel product = consignmentEntry.getOrderEntry().getProduct();
        Hbox consignmentEntryBox = new Hbox();
        consignmentEntryBox.setSclass("yw-quicklist-warehousing-hbox-container");
        Vbox infoList = new Vbox();
        infoList.setSclass("yw-quicklist-warehousing-vbox-container");
        if(product.getThumbnail() != null)
        {
            Image productImage = new Image(product.getThumbnail().getURL());
            productImage.setSclass("yw-quicklist-warehousing--tile--img");
            consignmentEntryBox.appendChild((Component)productImage);
        }
        Label label1 = labelizeProperty(product.getCode());
        label1.setSclass("yw-quicklist-warehousing--tile--prod-code");
        infoList.appendChild((Component)label1);
        Label label2 = labelizeProperty(product.getName());
        label2.setSclass("yw-quicklist-warehousing--tile--prod-name");
        infoList.appendChild((Component)label2);
        Label label3 = labelizeProperty(
                        resolveLabel("warehousingbackoffice.quick.list.quantity.pending") + " " + resolveLabel("warehousingbackoffice.quick.list.quantity.pending"));
        label3.setSclass("yw-quicklist-warehousing--tile--prod-qty");
        infoList.appendChild((Component)label3);
        consignmentEntryBox.appendChild((Component)infoList);
        parent.appendChild((Component)consignmentEntryBox);
    }
}
