package de.hybris.platform.core.model.order;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.fraud.model.FraudReportModel;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.ordermodify.model.OrderModificationRecordModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.orderscheduling.model.CartToOrderCronJobModel;
import de.hybris.platform.orderscheduling.model.OrderScheduleCronJobModel;
import de.hybris.platform.orderscheduling.model.OrderTemplateToOrderCronJobModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.sap.sapmodel.model.SAPOrderModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class OrderModel extends AbstractOrderModel
{
    public static final String _TYPECODE = "Order";
    public static final String _ORDER2CARTTOORDERCRONJOB = "Order2CartToOrderCronJob";
    public static final String VERSIONID = "versionID";
    public static final String ORIGINALVERSION = "originalVersion";
    public static final String FRAUDULENT = "fraudulent";
    public static final String POTENTIALLYFRAUDULENT = "potentiallyFraudulent";
    public static final String STATUSDISPLAY = "statusDisplay";
    public static final String FRAUDREPORTS = "fraudReports";
    public static final String HISTORYENTRIES = "historyEntries";
    public static final String ORDERTEMPLATECRONJOB = "orderTemplateCronJob";
    public static final String ORDERSCHEDULECRONJOB = "orderScheduleCronJob";
    public static final String MODIFICATIONRECORDS = "modificationRecords";
    public static final String RETURNREQUESTS = "returnRequests";
    public static final String ORDERPROCESS = "orderProcess";
    public static final String SALESAPPLICATION = "salesApplication";
    public static final String LANGUAGE = "language";
    public static final String PLACEDBY = "placedBy";
    public static final String QUOTEREFERENCE = "quoteReference";
    public static final String EXHAUSTEDAPPROVERS = "exhaustedApprovers";
    public static final String SCHEDULINGCRONJOB = "schedulingCronJob";
    public static final String SAPORDERS = "sapOrders";
    public static final String SAPPLANTCODE = "sapPlantCode";
    public static final String SAPREJECTIONREASON = "sapRejectionReason";
    public static final String SAPGOODSISSUEDATE = "sapGoodsIssueDate";


    public OrderModel()
    {
    }


    public OrderModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderModel(CurrencyModel _currency, Date _date, UserModel _user)
    {
        setCurrency(_currency);
        setDate(_date);
        setUser(_user);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderModel(CurrencyModel _currency, Date _date, OrderModel _originalVersion, ItemModel _owner, UserModel _user, String _versionID)
    {
        setCurrency(_currency);
        setDate(_date);
        setOriginalVersion(_originalVersion);
        setOwner(_owner);
        setUser(_user);
        setVersionID(_versionID);
    }


    @Accessor(qualifier = "exhaustedApprovers", type = Accessor.Type.GETTER)
    public Set<B2BCustomerModel> getExhaustedApprovers()
    {
        return (Set<B2BCustomerModel>)getPersistenceContext().getPropertyValue("exhaustedApprovers");
    }


    @Accessor(qualifier = "fraudReports", type = Accessor.Type.GETTER)
    public Set<FraudReportModel> getFraudReports()
    {
        return (Set<FraudReportModel>)getPersistenceContext().getPropertyValue("fraudReports");
    }


    @Accessor(qualifier = "fraudulent", type = Accessor.Type.GETTER)
    public Boolean getFraudulent()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("fraudulent");
    }


    @Accessor(qualifier = "historyEntries", type = Accessor.Type.GETTER)
    public List<OrderHistoryEntryModel> getHistoryEntries()
    {
        return (List<OrderHistoryEntryModel>)getPersistenceContext().getPropertyValue("historyEntries");
    }


    @Accessor(qualifier = "language", type = Accessor.Type.GETTER)
    public LanguageModel getLanguage()
    {
        return (LanguageModel)getPersistenceContext().getPropertyValue("language");
    }


    @Accessor(qualifier = "modificationRecords", type = Accessor.Type.GETTER)
    public Set<OrderModificationRecordModel> getModificationRecords()
    {
        return (Set<OrderModificationRecordModel>)getPersistenceContext().getPropertyValue("modificationRecords");
    }


    @Accessor(qualifier = "orderProcess", type = Accessor.Type.GETTER)
    public Collection<OrderProcessModel> getOrderProcess()
    {
        return (Collection<OrderProcessModel>)getPersistenceContext().getPropertyValue("orderProcess");
    }


    @Accessor(qualifier = "orderScheduleCronJob", type = Accessor.Type.GETTER)
    public Collection<OrderScheduleCronJobModel> getOrderScheduleCronJob()
    {
        return (Collection<OrderScheduleCronJobModel>)getPersistenceContext().getPropertyValue("orderScheduleCronJob");
    }


    @Accessor(qualifier = "orderTemplateCronJob", type = Accessor.Type.GETTER)
    public Collection<OrderTemplateToOrderCronJobModel> getOrderTemplateCronJob()
    {
        return (Collection<OrderTemplateToOrderCronJobModel>)getPersistenceContext().getPropertyValue("orderTemplateCronJob");
    }


    @Accessor(qualifier = "originalVersion", type = Accessor.Type.GETTER)
    public OrderModel getOriginalVersion()
    {
        return (OrderModel)getPersistenceContext().getPropertyValue("originalVersion");
    }


    @Accessor(qualifier = "placedBy", type = Accessor.Type.GETTER)
    public UserModel getPlacedBy()
    {
        return (UserModel)getPersistenceContext().getPropertyValue("placedBy");
    }


    @Accessor(qualifier = "potentiallyFraudulent", type = Accessor.Type.GETTER)
    public Boolean getPotentiallyFraudulent()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("potentiallyFraudulent");
    }


    @Accessor(qualifier = "quoteReference", type = Accessor.Type.GETTER)
    public QuoteModel getQuoteReference()
    {
        return (QuoteModel)getPersistenceContext().getPropertyValue("quoteReference");
    }


    @Accessor(qualifier = "returnRequests", type = Accessor.Type.GETTER)
    public List<ReturnRequestModel> getReturnRequests()
    {
        return (List<ReturnRequestModel>)getPersistenceContext().getPropertyValue("returnRequests");
    }


    @Accessor(qualifier = "salesApplication", type = Accessor.Type.GETTER)
    public SalesApplication getSalesApplication()
    {
        return (SalesApplication)getPersistenceContext().getPropertyValue("salesApplication");
    }


    @Accessor(qualifier = "sapGoodsIssueDate", type = Accessor.Type.GETTER)
    public Date getSapGoodsIssueDate()
    {
        return (Date)getPersistenceContext().getPropertyValue("sapGoodsIssueDate");
    }


    @Accessor(qualifier = "sapOrders", type = Accessor.Type.GETTER)
    public Set<SAPOrderModel> getSapOrders()
    {
        return (Set<SAPOrderModel>)getPersistenceContext().getPropertyValue("sapOrders");
    }


    @Accessor(qualifier = "sapPlantCode", type = Accessor.Type.GETTER)
    public String getSapPlantCode()
    {
        return (String)getPersistenceContext().getPropertyValue("sapPlantCode");
    }


    @Accessor(qualifier = "sapRejectionReason", type = Accessor.Type.GETTER)
    public String getSapRejectionReason()
    {
        return (String)getPersistenceContext().getPropertyValue("sapRejectionReason");
    }


    @Accessor(qualifier = "schedulingCronJob", type = Accessor.Type.GETTER)
    public CartToOrderCronJobModel getSchedulingCronJob()
    {
        return (CartToOrderCronJobModel)getPersistenceContext().getPropertyValue("schedulingCronJob");
    }


    @Accessor(qualifier = "statusDisplay", type = Accessor.Type.GETTER)
    public String getStatusDisplay()
    {
        return (String)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "statusDisplay");
    }


    @Accessor(qualifier = "versionID", type = Accessor.Type.GETTER)
    public String getVersionID()
    {
        return (String)getPersistenceContext().getPropertyValue("versionID");
    }


    @Accessor(qualifier = "exhaustedApprovers", type = Accessor.Type.SETTER)
    public void setExhaustedApprovers(Set<B2BCustomerModel> value)
    {
        getPersistenceContext().setPropertyValue("exhaustedApprovers", value);
    }


    @Accessor(qualifier = "fraudReports", type = Accessor.Type.SETTER)
    public void setFraudReports(Set<FraudReportModel> value)
    {
        getPersistenceContext().setPropertyValue("fraudReports", value);
    }


    @Accessor(qualifier = "fraudulent", type = Accessor.Type.SETTER)
    public void setFraudulent(Boolean value)
    {
        getPersistenceContext().setPropertyValue("fraudulent", value);
    }


    @Accessor(qualifier = "historyEntries", type = Accessor.Type.SETTER)
    public void setHistoryEntries(List<OrderHistoryEntryModel> value)
    {
        getPersistenceContext().setPropertyValue("historyEntries", value);
    }


    @Accessor(qualifier = "language", type = Accessor.Type.SETTER)
    public void setLanguage(LanguageModel value)
    {
        getPersistenceContext().setPropertyValue("language", value);
    }


    @Accessor(qualifier = "modificationRecords", type = Accessor.Type.SETTER)
    public void setModificationRecords(Set<OrderModificationRecordModel> value)
    {
        getPersistenceContext().setPropertyValue("modificationRecords", value);
    }


    @Accessor(qualifier = "orderProcess", type = Accessor.Type.SETTER)
    public void setOrderProcess(Collection<OrderProcessModel> value)
    {
        getPersistenceContext().setPropertyValue("orderProcess", value);
    }


    @Accessor(qualifier = "orderScheduleCronJob", type = Accessor.Type.SETTER)
    public void setOrderScheduleCronJob(Collection<OrderScheduleCronJobModel> value)
    {
        getPersistenceContext().setPropertyValue("orderScheduleCronJob", value);
    }


    @Accessor(qualifier = "orderTemplateCronJob", type = Accessor.Type.SETTER)
    public void setOrderTemplateCronJob(Collection<OrderTemplateToOrderCronJobModel> value)
    {
        getPersistenceContext().setPropertyValue("orderTemplateCronJob", value);
    }


    @Accessor(qualifier = "originalVersion", type = Accessor.Type.SETTER)
    public void setOriginalVersion(OrderModel value)
    {
        getPersistenceContext().setPropertyValue("originalVersion", value);
    }


    @Accessor(qualifier = "placedBy", type = Accessor.Type.SETTER)
    public void setPlacedBy(UserModel value)
    {
        getPersistenceContext().setPropertyValue("placedBy", value);
    }


    @Accessor(qualifier = "potentiallyFraudulent", type = Accessor.Type.SETTER)
    public void setPotentiallyFraudulent(Boolean value)
    {
        getPersistenceContext().setPropertyValue("potentiallyFraudulent", value);
    }


    @Accessor(qualifier = "quoteReference", type = Accessor.Type.SETTER)
    public void setQuoteReference(QuoteModel value)
    {
        getPersistenceContext().setPropertyValue("quoteReference", value);
    }


    @Accessor(qualifier = "returnRequests", type = Accessor.Type.SETTER)
    public void setReturnRequests(List<ReturnRequestModel> value)
    {
        getPersistenceContext().setPropertyValue("returnRequests", value);
    }


    @Accessor(qualifier = "salesApplication", type = Accessor.Type.SETTER)
    public void setSalesApplication(SalesApplication value)
    {
        getPersistenceContext().setPropertyValue("salesApplication", value);
    }


    @Accessor(qualifier = "sapGoodsIssueDate", type = Accessor.Type.SETTER)
    public void setSapGoodsIssueDate(Date value)
    {
        getPersistenceContext().setPropertyValue("sapGoodsIssueDate", value);
    }


    @Accessor(qualifier = "sapOrders", type = Accessor.Type.SETTER)
    public void setSapOrders(Set<SAPOrderModel> value)
    {
        getPersistenceContext().setPropertyValue("sapOrders", value);
    }


    @Accessor(qualifier = "sapPlantCode", type = Accessor.Type.SETTER)
    public void setSapPlantCode(String value)
    {
        getPersistenceContext().setPropertyValue("sapPlantCode", value);
    }


    @Accessor(qualifier = "sapRejectionReason", type = Accessor.Type.SETTER)
    public void setSapRejectionReason(String value)
    {
        getPersistenceContext().setPropertyValue("sapRejectionReason", value);
    }


    @Accessor(qualifier = "schedulingCronJob", type = Accessor.Type.SETTER)
    public void setSchedulingCronJob(CartToOrderCronJobModel value)
    {
        getPersistenceContext().setPropertyValue("schedulingCronJob", value);
    }


    @Accessor(qualifier = "versionID", type = Accessor.Type.SETTER)
    public void setVersionID(String value)
    {
        getPersistenceContext().setPropertyValue("versionID", value);
    }
}
