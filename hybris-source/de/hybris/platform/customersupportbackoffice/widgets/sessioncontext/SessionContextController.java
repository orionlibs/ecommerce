package de.hybris.platform.customersupportbackoffice.widgets.sessioncontext;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.components.Stopwatch;
import com.hybris.cockpitng.util.CockpitSessionService;
import com.hybris.cockpitng.util.DefaultWidgetController;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customersupportbackoffice.widgets.sessioncontext.model.SessionContextModel;
import de.hybris.platform.customersupportbackoffice.widgets.sessioncontext.util.AsmUtils;
import de.hybris.platform.customersupportbackoffice.widgets.sessioncontext.util.SessionContextUtil;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.ticket.model.CsTicketModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

public class SessionContextController extends DefaultWidgetController
{
    protected static final String USER_ANCHOR_COMPONENT_ID = "userAnchor";
    protected static final String TICKET_ANCHOR_COMPONENT_ID = "ticketAnchor";
    protected static final String ORDER_ANCHOR_COMPONENT_ID = "orderAnchor";
    protected static final String END_SESSION_BUTTON_COMPONENT_ID = "endSessionBtn";
    protected static final String SESSION_CALL_BUTTON_COMPONENT_ID = "callContextBtn";
    protected static final String ASM_LAUNCH_BUTTON_COMPONENT_ID = "asmBtn";
    protected static final String START_CALL_LABEL_KEY = "sessionContext.call.button.start";
    protected static final String END_CALL_LABEL_KEY = "sessionContext.call.button.end";
    protected static final String CURRENT_SESSION_CALL_MODE = "enabled.call.mode";
    protected static final String IN_CALL_MODE = "in.call";
    protected static final String FREE_CALL_MODE = "free";
    protected static final String START_CALL_CSS_CLASS = "y-start-call-btn";
    protected static final String END_CALL_CSS_CLASS = "y-end-call-btn";
    protected static final String ANCHOR_CSS_CLASS = "y-session-context-label-value";
    protected static final String ANCHOR_CSS_COLOR_CLASS = "y-session-context-label-value-color";
    protected final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("mm 'min' ':' ss 'sec'");
    protected A userAnchor;
    protected A ticketAnchor;
    protected A orderAnchor;
    protected Button endSessionBtn;
    protected Button asmBtn;
    protected Button callContextBtn;
    protected Label customerPlaceholder;
    protected Label ticketPlaceholder;
    protected Label orderPlaceholder;
    protected Image userImage;
    protected Image ticketImage;
    protected Image orderImage;
    protected Combobox availableSites;
    protected Stopwatch stopWatch;
    protected Div sessionTimerDiv;
    @Resource
    protected transient BaseSiteService baseSiteService;
    @Resource
    protected transient CockpitSessionService cockpitSessionService;


    public void initialize(Component comp)
    {
        super.initialize(comp);
        ListModelList<BaseSiteModel> model = getAvailableSites();
        if(null != model && model.getSize() > 0)
        {
            model.addToSelection(model.get(0));
        }
        else
        {
            this.asmBtn.setVisible(false);
        }
        this.availableSites.setModel((ListModel)model);
    }


    @ViewEvent(componentID = "userAnchor", eventName = "onClick")
    public void getUserDetails()
    {
        UserModel user = SessionContextUtil.createOrReturnSessionContext(getWidgetInstanceManager().getModel()).getCurrentCustomer();
        if(null != user)
        {
            sendOutput("itemToShow", user);
        }
    }


    @ViewEvent(componentID = "ticketAnchor", eventName = "onClick")
    public void getTicketDetails()
    {
        CsTicketModel ticket = SessionContextUtil.createOrReturnSessionContext(getWidgetInstanceManager().getModel()).getCurrentTicket();
        if(null != ticket)
        {
            sendOutput("itemToShow", ticket);
        }
    }


    @ViewEvent(componentID = "orderAnchor", eventName = "onClick")
    public void getOrderDetails()
    {
        AbstractOrderModel order = SessionContextUtil.createOrReturnSessionContext(getWidgetInstanceManager().getModel()).getCurrentOrder();
        if(null != order)
        {
            sendOutput("itemToShow", order);
        }
    }


    @SocketEvent(socketId = "selectedItem")
    public void itemSelected(Object msg)
    {
        if(!(msg instanceof CustomerModel) && !(msg instanceof OrderModel) && !(msg instanceof ReturnRequestModel) && !(msg instanceof CsTicketModel))
        {
            return;
        }
        resetToDefault();
        String currentSessionCustomerId = "";
        SessionContextModel currentContextSession = SessionContextUtil.getCurrentSessionContext(getWidgetInstanceManager().getModel());
        if(msg instanceof CustomerModel)
        {
            CustomerModel customer = (CustomerModel)msg;
            currentSessionCustomerId = customerSelected(customer, currentContextSession);
        }
        else if(msg instanceof CsTicketModel)
        {
            CsTicketModel currentTicket = (CsTicketModel)msg;
            currentSessionCustomerId = currentTicketSelected(currentTicket, currentContextSession);
        }
        else if(msg instanceof OrderModel || msg instanceof ReturnRequestModel)
        {
            currentSessionCustomerId = currentOrderSelected(msg, currentContextSession);
        }
        this.callContextBtn.setAttribute("enabled.call.mode", "free");
        handleUIComponents();
        handleSessionCallMode();
        this.cockpitSessionService.setAttribute("sessionContextUID", currentSessionCustomerId);
        this.sessionTimerDiv.setVisible(true);
    }


    protected String customerSelected(CustomerModel customer, SessionContextModel currentContextSession)
    {
        applyAnchorValueSelected(this.userAnchor, customer.getName());
        if(null == currentContextSession || !customer.equals(currentContextSession.getCurrentCustomer()))
        {
            SessionContextUtil.populateCustomer(getWidgetInstanceManager().getModel(), (UserModel)customer);
            restartTimer();
        }
        return customer.getUid();
    }


    protected String currentTicketSelected(CsTicketModel currentTicket, SessionContextModel currentContextSession)
    {
        String currentSessionCustomerId = "";
        UserModel previousCustomer = null;
        if(null != currentContextSession)
        {
            previousCustomer = currentContextSession.getCurrentCustomer();
        }
        SessionContextUtil.populateTicket(getWidgetInstanceManager().getModel(), currentTicket);
        if(null != currentTicket.getOrder() && currentTicket.getOrder() instanceof OrderModel)
        {
            applyAnchorValueSelected(this.orderAnchor, currentTicket.getOrder().getCode());
        }
        if(null != currentTicket.getCustomer())
        {
            applyAnchorValueSelected(this.userAnchor, currentTicket.getCustomer().getName());
            currentSessionCustomerId = currentTicket.getCustomer().getUid();
            if(null == previousCustomer || !previousCustomer.getUid().equals(currentSessionCustomerId))
            {
                restartTimer();
            }
        }
        applyAnchorValueSelected(this.ticketAnchor, currentTicket.getTicketID());
        handleSiteUpdate(currentTicket.getBaseSite());
        return currentSessionCustomerId;
    }


    protected String currentOrderSelected(Object msg, SessionContextModel currentContextSession)
    {
        String currentSessionCustomerId = "";
        UserModel previousCustomer = null;
        OrderModel currentOrder = null;
        if(msg instanceof ReturnRequestModel)
        {
            ReturnRequestModel returnModel = (ReturnRequestModel)msg;
            currentOrder = returnModel.getOrder();
        }
        else
        {
            currentOrder = (OrderModel)msg;
        }
        if(null != currentContextSession)
        {
            previousCustomer = currentContextSession.getCurrentCustomer();
        }
        SessionContextUtil.populateOrder(getWidgetInstanceManager().getModel(), currentOrder);
        applyAnchorValueSelected(this.orderAnchor, currentOrder.getCode());
        applyAnchorValueSelected(this.userAnchor, currentOrder.getUser().getName());
        currentSessionCustomerId = currentOrder.getUser().getUid();
        if(null == previousCustomer || !previousCustomer.getUid().equals(currentSessionCustomerId))
        {
            restartTimer();
        }
        handleSiteUpdate(currentOrder.getSite());
        return currentSessionCustomerId;
    }


    @ViewEvent(componentID = "endSessionBtn", eventName = "onClick")
    public void endCurrentSession()
    {
        SessionContextUtil.clearSessionContext(getWidgetInstanceManager().getModel());
        resetToDefault();
        handleUIComponents();
        sendOutput("itemToShow", null);
        sendOutput("viewData", null);
        this.stopWatch.stop();
        this.sessionTimerDiv.setVisible(false);
        this.cockpitSessionService.removeAttribute("sessionContextUID");
    }


    @SocketEvent(socketId = "itemCreated")
    public void itemCreated(Object msg)
    {
        if(msg instanceof CsTicketModel || msg instanceof CustomerModel)
        {
            itemSelected(msg);
            sendOutput("itemToShow", msg);
        }
    }


    protected void applyAnchorValueSelected(A anchor, String labelValue)
    {
        anchor.setLabel(labelValue);
        anchor.addSclass("y-session-context-label-value-color");
    }


    protected void applyAnchorValueUnSelected(A anchor)
    {
        anchor.setLabel(null);
        anchor.removeSclass("y-session-context-label-value-color");
    }


    protected void restartTimer()
    {
        this.stopWatch.reset();
        this.stopWatch.start();
    }


    protected void resetToDefault()
    {
        this.callContextBtn.removeAttribute("enabled.call.mode");
        applyAnchorValueUnSelected(this.userAnchor);
        applyAnchorValueUnSelected(this.ticketAnchor);
        applyAnchorValueUnSelected(this.orderAnchor);
        this.availableSites.setDisabled(false);
        if(this.availableSites.getModel().getSize() > 0)
        {
            this.availableSites.setSelectedIndex(0);
        }
    }


    protected void handleSiteUpdate(BaseSiteModel baseSiteModel)
    {
        if(baseSiteModel == null)
        {
            this.availableSites.setDisabled(true);
            return;
        }
        ListModel<BaseSiteModel> sites = this.availableSites.getModel();
        for(int i = 0; i < sites.getSize(); i++)
        {
            if(((BaseSiteModel)sites.getElementAt(i)).getUid().equals(baseSiteModel.getUid()))
            {
                this.availableSites.setSelectedIndex(i);
                this.availableSites.setDisabled(true);
                break;
            }
        }
    }


    @ViewEvent(componentID = "callContextBtn", eventName = "onClick")
    public void handleSessionCall()
    {
        handleSessionCallMode();
    }


    @ViewEvent(componentID = "asmBtn", eventName = "onClick")
    public void launchASM()
    {
        SessionContextModel currentSessionContext = SessionContextUtil.getCurrentSessionContext(getWidgetInstanceManager().getModel());
        Executions.getCurrent()
                        .sendRedirect(AsmUtils.getAsmDeepLink((BaseSiteModel)this.availableSites
                                        .getModel().getElementAt(this.availableSites.getSelectedIndex()), currentSessionContext), "_blank");
    }


    public boolean showASMButton()
    {
        return AsmUtils.showAsmButton();
    }


    protected void handleUIComponents()
    {
        SessionContextModel currentSessionContext = SessionContextUtil.getCurrentSessionContext(getWidgetInstanceManager().getModel());
        this.endSessionBtn.setVisible((null != currentSessionContext));
        this.callContextBtn.setVisible((null != currentSessionContext));
        this.customerPlaceholder.setVisible((null == currentSessionContext || null == currentSessionContext.getCurrentCustomer()));
        this.ticketPlaceholder.setVisible((null == currentSessionContext || null == currentSessionContext.getCurrentTicket()));
        this.orderPlaceholder.setVisible((null == currentSessionContext || null == currentSessionContext.getCurrentOrder()));
    }


    protected void handleSessionCallMode()
    {
        if(null != this.callContextBtn.getAttribute("enabled.call.mode") && this.callContextBtn
                        .getAttribute("enabled.call.mode").equals("in.call"))
        {
            this.callContextBtn.setLabel(getWidgetInstanceManager().getLabel("sessionContext.call.button.end"));
            this.callContextBtn.setAttribute("enabled.call.mode", "free");
            this.callContextBtn.setSclass("y-end-call-btn");
        }
        else
        {
            this.callContextBtn.setLabel(getWidgetInstanceManager().getLabel("sessionContext.call.button.start"));
            this.callContextBtn.setAttribute("enabled.call.mode", "in.call");
            this.callContextBtn.setSclass("y-start-call-btn");
        }
    }


    protected ListModelList<BaseSiteModel> getAvailableSites()
    {
        ListModelList<BaseSiteModel> model = new ListModelList();
        Collection<BaseSiteModel> allBaseSites = this.baseSiteService.getAllBaseSites();
        if(CollectionUtils.isNotEmpty(allBaseSites))
        {
            List<BaseSiteModel> availableBaseSites = new ArrayList<>(allBaseSites.size());
            for(BaseSiteModel baseSite : allBaseSites)
            {
                availableBaseSites.add(baseSite);
            }
            Collections.sort(availableBaseSites, (Comparator<? super BaseSiteModel>)new Object(this));
            model.addAll(availableBaseSites);
        }
        return model;
    }
}
