package de.hybris.platform.productcockpit.components.listview.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.util.Config;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;

public class AddProductToExpressUdateAction extends AbstractProductAction
{
    protected static final String EXPRESS_UPDATE_CATALOG_VERSIONS = "expressUpdateCatalogVersions";
    private static final Logger LOG = LoggerFactory.getLogger(AddProductToExpressUdateAction.class);
    private static final String PRODUCT_MESSAGE_DELIMITER = " , ";
    protected String BUTTON_ICON = "productcockpit/images/icon_func_add_validationque.png";
    protected String BUTTON_ICON_INACTIVE = "productcockpit/images/icon_func_add_validationque_inactive.png";
    private MessageChannel productExpressUpdateChannel;


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("expressupdate.addtoqueue");
    }


    public String getImageURI(ListViewAction.Context context)
    {
        if(isAddingToExpressUpdateEnabled(context))
        {
            return this.BUTTON_ICON;
        }
        return null;
    }


    protected boolean isAddingToExpressUpdateEnabled(ListViewAction.Context context)
    {
        List<String> catalogVersions = new ArrayList<>();
        String expressUpdateParameter = Config.getParameter("expressUpdateCatalogVersions");
        if(StringUtils.isNotEmpty(expressUpdateParameter))
        {
            catalogVersions
                            .addAll(Lists.newArrayList(Splitter.on(',').trimResults().omitEmptyStrings().split(expressUpdateParameter)));
        }
        if(!catalogVersions.isEmpty() && context.getItem() != null && context.getItem().getObject() instanceof ProductModel)
        {
            ProductModel product = (ProductModel)context.getItem().getObject();
            for(String catalogVersion : catalogVersions)
            {
                if(catalogVersion
                                .equals(product.getCatalogVersion().getCatalog().getId() + ":" + product.getCatalogVersion().getCatalog().getId()))
                {
                    return true;
                }
            }
        }
        return false;
    }


    public EventListener getEventListener(ListViewAction.Context context)
    {
        EventListener ret = null;
        return (EventListener)new Object(this, context);
    }


    protected boolean sendProductToChannel(ProductModel product)
    {
        try
        {
            Message<ProductModel> message = MessageBuilder.withPayload(product).build();
            return getProductExpressUpdateChannel().send(message);
        }
        catch(MessageDeliveryException exception)
        {
            LOG.warn("Probably there is no listener for productExpressUpdateChannel", (Throwable)exception);
            return false;
        }
    }


    public String getMultiSelectImageURI(ListViewAction.Context context)
    {
        List<TypedObject> selectedItems = getSelectedItems(context);
        if(StringUtils.isNotEmpty(Config.getParameter("expressUpdateCatalogVersions")))
        {
            if(CollectionUtils.isNotEmpty(selectedItems) && isMultiAddingToExpressUpdateEnabled(context))
            {
                return this.BUTTON_ICON;
            }
            return this.BUTTON_ICON_INACTIVE;
        }
        return null;
    }


    protected boolean isMultiAddingToExpressUpdateEnabled(ListViewAction.Context context)
    {
        List<String> catalogVersions = new ArrayList<>();
        String expressUpdateParameter = Config.getParameter("expressUpdateCatalogVersions");
        if(StringUtils.isNotEmpty(expressUpdateParameter))
        {
            catalogVersions
                            .addAll(Lists.newArrayList(Splitter.on(',').trimResults().omitEmptyStrings().split(expressUpdateParameter)));
        }
        List<TypedObject> selectedItems = getSelectedItems(context);
        if(!catalogVersions.isEmpty() && CollectionUtils.isNotEmpty(selectedItems))
        {
            for(TypedObject typeObject : selectedItems)
            {
                ProductModel product = (ProductModel)typeObject.getObject();
                boolean productEnabled = false;
                for(String catalogVersion : catalogVersions)
                {
                    if(catalogVersion
                                    .equals(product.getCatalogVersion().getCatalog().getId() + ":" + product.getCatalogVersion().getCatalog().getId()))
                    {
                        productEnabled = true;
                        break;
                    }
                }
                if(!productEnabled)
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }


    public EventListener getMultiSelectEventListener(ListViewAction.Context context)
    {
        Object object = null;
        List<TypedObject> selectedItems = getSelectedItems(context);
        if(CollectionUtils.isNotEmpty(selectedItems))
        {
            object = new Object(this, selectedItems);
        }
        return (EventListener)object;
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        return null;
    }


    public Menupopup getContextPopup(ListViewAction.Context context)
    {
        return null;
    }


    protected void doCreateContext(ListViewAction.Context context)
    {
    }


    public MessageChannel getProductExpressUpdateChannel()
    {
        return this.productExpressUpdateChannel;
    }


    @Required
    public void setProductExpressUpdateChannel(MessageChannel productExpressUpdateChannel)
    {
        this.productExpressUpdateChannel = productExpressUpdateChannel;
    }
}
