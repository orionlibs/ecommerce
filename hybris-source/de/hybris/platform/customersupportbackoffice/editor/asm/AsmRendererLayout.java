package de.hybris.platform.customersupportbackoffice.editor.asm;

import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Base;
import com.hybris.cockpitng.data.TypeAwareSelectionContext;
import com.hybris.cockpitng.editor.commonreferenceeditor.AbstractReferenceEditor;
import com.hybris.cockpitng.editor.commonreferenceeditor.ReferenceEditorLayout;
import com.hybris.cockpitng.editor.commonreferenceeditor.ReferenceEditorLogic;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.YTestTools;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.Config;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

public class AsmRendererLayout<T, K> extends ReferenceEditorLayout<T>
{
    private static final String HTTP_POSTFIX = ".https";
    private static final String LINK_PREFIX = "website.";
    private final AbstractReferenceEditor<T, K> referenceEditor;
    protected static final String CSS_REFERENCE_EDITOR_REMOVE_BTN = "ye-default-reference-editor-remove-button";
    protected static final String CSS_REFERENCE_EDITOR_SELECTED_ITEM_LABEL = "ye-default-reference-editor-selected-item-label";
    protected static final String CSS_REFERENCE_EDITOR_SELECTED_ITEM_CONTAINER = "ye-default-reference-editor-selected-item-container";
    protected static final String YTESTID_REMOVE_BUTTON = "reference-editor-remove-button";
    protected static final String YTESTID_REFERENCE_ENTRY = "reference-editor-reference";
    protected static final String ASM_DEEPLINK_PARAM = "assistedservicestorefront.deeplink.link";
    protected static final String CART = "customersupport_backoffice_asm_cart_prefix";
    protected static final String ORDER = "customersupport_backoffice_asm_order_prefix";
    private final UrlResolver<ProductModel> productModelUrlResolver;
    protected boolean isSimpleSelectEnabled;
    protected boolean showAsmPrefix;
    protected String asmForwardURL;
    protected BaseSiteService baseSiteService;


    public AsmRendererLayout(AbstractReferenceEditor<T, K> referenceEditorInterface, Base configuration, UrlResolver<ProductModel> productModelUrlResolver, BaseSiteService baseSiteService)
    {
        super((ReferenceEditorLogic)referenceEditorInterface, configuration);
        this.productModelUrlResolver = productModelUrlResolver;
        this.referenceEditor = referenceEditorInterface;
        this.baseSiteService = baseSiteService;
    }


    protected ListitemRenderer<T> createSelectedItemsListItemRenderer()
    {
        return (listitem, t, i) -> renderSelectedItemsList(listitem, (T)t, i);
    }


    protected void renderSelectedItemsList(Listitem item, T data, int index)
    {
        Label label = createSelectedItemLabel(data);
        Div removeImage = new Div();
        removeImage.setSclass("ye-default-reference-editor-remove-button");
        YTestTools.modifyYTestId((Component)removeImage, "reference-editor-remove-button");
        removeImage.setVisible(this.referenceEditor.isEditable());
        removeImage.addEventListener("onClick", event -> this.referenceEditor.removeSelectedObject(data));
        Div layout = new Div();
        layout.setSclass("ye-default-reference-editor-selected-item-container");
        Listcell cell = new Listcell();
        UITools.modifySClass((HtmlBasedComponent)label, "ye-editor-disabled", true);
        layout.appendChild((Component)label);
        if(StringUtils.isNotBlank(Config.getParameter("assistedservicestorefront.deeplink.link")))
        {
            layout.setSclass("ye-default-reference-editor-selected-item-container ye-default-asm-deep-link-container");
            appendAsmLinkButton(layout, data);
        }
        if(!this.referenceEditor.isDisableRemoveReference())
        {
            layout.appendChild((Component)removeImage);
            UITools.modifySClass((HtmlBasedComponent)layout, "ye-remove-enabled", true);
        }
        if(!this.isSimpleSelectEnabled && !this.referenceEditor.isDisableDisplayingDetails())
        {
            cell.addEventListener("onDoubleClick", event -> this.referenceEditor.triggerReferenceSelected(new TypeAwareSelectionContext(data, getSelectedElementsListModel().getInnerList())));
        }
        if(this.isSimpleSelectEnabled && !this.referenceEditor.isDisableDisplayingDetails())
        {
            cell.addEventListener("onDoubleClick", event -> this.referenceEditor.sendOutput("itemSelected", data));
        }
        cell.appendChild((Component)layout);
        cell.setParent((Component)item);
    }


    private Label createSelectedItemLabel(T data)
    {
        StringBuilder stringRepresentationOfObject = new StringBuilder();
        if(this.showAsmPrefix && data instanceof de.hybris.platform.core.model.order.CartModel)
        {
            stringRepresentationOfObject.append(Labels.getLabel("customersupport_backoffice_asm_cart_prefix"));
        }
        if(this.showAsmPrefix && data instanceof de.hybris.platform.core.model.order.OrderModel)
        {
            stringRepresentationOfObject.append(Labels.getLabel("customersupport_backoffice_asm_order_prefix"));
        }
        stringRepresentationOfObject.append(" ").append(this.referenceEditor.getStringRepresentationOfObject(data));
        Label label = new Label(stringRepresentationOfObject.toString());
        label.setSclass("ye-default-reference-editor-selected-item-label");
        YTestTools.modifyYTestId((Component)label, "reference-editor-reference");
        label.setMultiline(true);
        return label;
    }


    protected void appendAsmLinkButton(Div layout, Object data)
    {
        Div asmImageWrapper = new Div();
        asmImageWrapper.setSclass("ye-default-asm-deep-link");
        asmImageWrapper.addEventListener("onClick", event -> {
            String deepLink;
            Object parentObject = this.referenceEditor.getParentObject();
            if(parentObject instanceof de.hybris.platform.core.model.order.QuoteModel)
            {
                deepLink = getAsmLink(parentObject);
            }
            else
            {
                deepLink = getAsmLink(data);
            }
            Executions.getCurrent().sendRedirect(deepLink, "_blank");
        });
        layout.appendChild((Component)asmImageWrapper);
    }


    protected String getAsmLink(Object data)
    {
        if(data instanceof AbstractOrderModel)
        {
            return createAbstractOrderAsmLink((AbstractOrderModel)data);
        }
        if(data instanceof CustomerReviewModel)
        {
            return createCustomerReviewAsmLink((CustomerReviewModel)data);
        }
        if(data instanceof ProductModel)
        {
            return createProductAsmLink((ProductModel)data);
        }
        return null;
    }


    private String createProductAsmLink(ProductModel data)
    {
        ProductModel productModel = data;
        Stream<BaseSiteModel> filtered = this.baseSiteService.getAllBaseSites().stream().filter(site -> !((List)this.baseSiteService.getProductCatalogs(site).stream().filter(()).collect(Collectors.toList())).isEmpty());
        Optional<BaseSiteModel> first = filtered.findFirst();
        if(!first.isPresent())
        {
            return null;
        }
        BaseSiteModel siteModel = first.get();
        StringBuilder asmDeepLink = new StringBuilder(Config.getParameter("website." + siteModel.getUid() + ".https"));
        asmDeepLink.append(this.productModelUrlResolver.resolve(productModel));
        addASMPostFix(asmDeepLink);
        return asmDeepLink.toString();
    }


    private String createCustomerReviewAsmLink(CustomerReviewModel data)
    {
        CustomerReviewModel reviewModel = data;
        Stream<BaseSiteModel> filtered = this.baseSiteService.getAllBaseSites().stream().filter(site -> !((List)this.baseSiteService.getProductCatalogs(site).stream().filter(()).collect(Collectors.toList())).isEmpty());
        Optional<BaseSiteModel> first = filtered.findFirst();
        if(!first.isPresent())
        {
            return null;
        }
        BaseSiteModel siteModel = first.get();
        StringBuilder asmDeepLink = new StringBuilder(Config.getParameter("website." + siteModel.getUid() + ".https"));
        asmDeepLink.append(this.productModelUrlResolver.resolve(reviewModel.getProduct()));
        addASMPostFix(asmDeepLink);
        return asmDeepLink.toString();
    }


    private String createAbstractOrderAsmLink(AbstractOrderModel data)
    {
        AbstractOrderModel order = data;
        String typeCode = getTypeCode(data);
        if(order.getSite() != null && typeCode != null)
        {
            StringBuilder asmDeepLink = new StringBuilder(Config.getParameter("website." + order.getSite().getUid() + ".https"));
            CustomerModel customer = (CustomerModel)order.getUser();
            String customerUid = encodeURLString(customer.getUid());
            if(null != getAsmForwardURL())
            {
                asmDeepLink.append(Config.getParameter("assistedservicestorefront.deeplink.link")).append("?customerId=")
                                .append(customerUid).append("&fwd=").append(getAsmForwardURL());
            }
            else
            {
                asmDeepLink.append(Config.getParameter("assistedservicestorefront.deeplink.link")).append("?customerId=")
                                .append(customerUid).append("&").append(typeCode).append("Id=").append(order.getGuid());
            }
            if(data instanceof de.hybris.platform.core.model.order.QuoteModel)
            {
                asmDeepLink.append(data.getCode());
            }
            return asmDeepLink.toString();
        }
        return null;
    }


    protected String getTypeCode(Object data)
    {
        if(data instanceof de.hybris.platform.core.model.order.CartModel)
        {
            return "cart";
        }
        if(data instanceof de.hybris.platform.core.model.order.OrderModel)
        {
            return "order";
        }
        if(data instanceof de.hybris.platform.core.model.order.QuoteModel)
        {
            return "Quote";
        }
        return null;
    }


    public boolean isSimpleSelectEnabled()
    {
        return this.isSimpleSelectEnabled;
    }


    protected void setSimpleSelectEnabled(boolean isSimpleSelectEnabled)
    {
        this.isSimpleSelectEnabled = isSimpleSelectEnabled;
    }


    protected void addASMPostFix(StringBuilder linkage)
    {
        if(linkage.indexOf("?") != -1)
        {
            linkage.append("&asm=true");
        }
        else
        {
            linkage.append("?asm=true");
        }
    }


    private static String encodeURLString(String str)
    {
        try
        {
            return URLEncoder.encode(str, "UTF-8");
        }
        catch(UnsupportedEncodingException uee)
        {
            return str;
        }
    }


    public boolean isShowAsmPrefix()
    {
        return this.showAsmPrefix;
    }


    protected void setShowAsmPrefix(boolean showAsmPrefix)
    {
        this.showAsmPrefix = showAsmPrefix;
    }


    public String getAsmForwardURL()
    {
        return this.asmForwardURL;
    }


    protected void setAsmForwardURL(String asmForwardURL)
    {
        this.asmForwardURL = asmForwardURL;
    }
}
