package de.hybris.platform.orderhistory.impl;

import de.hybris.platform.basecommerce.jalo.BasecommerceManager;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.order.OrderCloneHelper;
import de.hybris.platform.orderhistory.OrderHistoryService;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class DefaultOrderHistoryService implements OrderHistoryService
{
    private KeyGenerator versionIDGenerator;
    private ModelService modelService;


    public KeyGenerator getVersionIDGenerator()
    {
        return this.versionIDGenerator;
    }


    @Required
    public void setVersionIDGenerator(KeyGenerator versionIDGenerator)
    {
        this.versionIDGenerator = versionIDGenerator;
    }


    public OrderModel createHistorySnapshot(OrderModel currentVersion)
    {
        if(currentVersion == null)
        {
            throw new IllegalArgumentException("current order version was null");
        }
        if(currentVersion.getVersionID() != null)
        {
            throw new IllegalArgumentException("order is already snapshot");
        }
        OrderModel copy = (OrderModel)getModelService().clone(currentVersion);
        copy.setVersionID((String)getVersionIDGenerator().generate());
        copy.setOriginalVersion(currentVersion);
        return copy;
    }


    public void saveHistorySnapshot(OrderModel snapshot)
    {
        if(snapshot == null)
        {
            throw new IllegalArgumentException("snapshot was null");
        }
        if(snapshot.getVersionID() == null)
        {
            throw new IllegalArgumentException("order is no snapshot");
        }
        if(!getModelService().isNew(snapshot))
        {
            throw new IllegalArgumentException("snapshot has already been persisted");
        }
        getModelService().save(snapshot);
        Order copyItem = (Order)getModelService().getSource(snapshot);
        Order originalItem = BasecommerceManager.getInstance().getOriginalVersion(copyItem);
        if(originalItem != null)
        {
            OrderCloneHelper.postProcess((AbstractOrder)originalItem, (AbstractOrder)copyItem);
            getModelService().refresh(snapshot);
            for(AbstractOrderEntryModel entry : snapshot.getEntries())
            {
                getModelService().refresh(entry);
            }
        }
    }


    public Collection<OrderModel> getHistorySnapshots(OrderModel ownerOrder)
    {
        if(ownerOrder == null)
        {
            throw new JaloInvalidParameterException("Missing ORDER for getting history entries ", 0);
        }
        Collection<OrderModel> result = null;
        List<OrderHistoryEntryModel> entries = ownerOrder.getHistoryEntries();
        for(OrderHistoryEntryModel entry : entries)
        {
            OrderModel snapshot = entry.getOrder();
            if(snapshot != null && snapshot.getVersionID() != null)
            {
                if(result == null)
                {
                    result = new ArrayList<>(entries.size());
                }
                result.add(snapshot);
            }
        }
        return (result == null) ? Collections.<OrderModel>emptyList() : result;
    }


    protected Collection getHistoryEntries(OrderModel ownerOrder, Date dateFrom, Date dateTo, boolean descriptionOnly)
    {
        if(ownerOrder == null)
        {
            throw new JaloInvalidParameterException("Missing ORDER for getting history entries ", 0);
        }
        if(null == dateFrom && null == dateTo)
        {
            if(descriptionOnly)
            {
                return getEntriesDescriptions(ownerOrder.getHistoryEntries());
            }
            return ownerOrder.getHistoryEntries();
        }
        List<OrderHistoryEntryModel> entries = ownerOrder.getHistoryEntries();
        List<OrderHistoryEntryModel> result = (List<OrderHistoryEntryModel>)entries.stream().filter(e -> Objects.nonNull(e.getTimestamp())).filter(e -> isWithingDateRange(e, dateFrom, dateTo)).collect(Collectors.toList());
        if(descriptionOnly)
        {
            return getEntriesDescriptions(result);
        }
        return result;
    }


    protected boolean isWithingDateRange(OrderHistoryEntryModel entry, Date from, Date to)
    {
        boolean isInRange = false;
        if(null != from && from.before(entry.getTimestamp()))
        {
            if(null == to || to.after(entry.getTimestamp()))
            {
                isInRange = true;
            }
        }
        else if(null != to && to.after(entry.getTimestamp()) && (null == from || from
                        .before(entry.getTimestamp())))
        {
            isInRange = true;
        }
        return isInRange;
    }


    public Collection<OrderHistoryEntryModel> getHistoryEntries(OrderModel ownerOrder, EmployeeModel employee)
    {
        if(employee == null)
        {
            throw new JaloInvalidParameterException("Missing EMPLOYEE for getting history entries ", 0);
        }
        if(ownerOrder == null)
        {
            throw new JaloInvalidParameterException("Missing ORDER for getting history entries ", 0);
        }
        return (Collection<OrderHistoryEntryModel>)ownerOrder.getHistoryEntries().stream()
                        .filter(entry -> (Objects.nonNull(entry.getEmployee()) && entry.getEmployee().equals(employee))).collect(Collectors.toList());
    }


    public Collection<OrderHistoryEntryModel> getHistoryEntries(UserModel user, Date dateFrom, Date dateTo)
    {
        if(user == null)
        {
            throw new JaloInvalidParameterException("Missing USER for getting history entries ", 0);
        }
        Collection<OrderHistoryEntryModel> result = new ArrayList<>();
        Collection<OrderModel> orders = user.getOrders();
        for(OrderModel order : orders)
        {
            result.addAll(getHistoryEntries(order, dateFrom, dateTo, false));
        }
        return result;
    }


    protected Collection<String> getEntriesDescriptions(Collection<OrderHistoryEntryModel> entries)
    {
        return (Collection<String>)entries.stream().map(OrderHistoryEntryModel::getDescription).collect(Collectors.toList());
    }


    public Collection<OrderHistoryEntryModel> getHistoryEntries(OrderModel ownerOrder, Date dateFrom, Date dateTo)
    {
        return getHistoryEntries(ownerOrder, dateFrom, dateTo, false);
    }


    public Collection<String> getHistoryEntriesDescriptions(OrderModel ownerOrder, Date dateFrom, Date dateTo)
    {
        return getHistoryEntries(ownerOrder, dateFrom, dateTo, true);
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
