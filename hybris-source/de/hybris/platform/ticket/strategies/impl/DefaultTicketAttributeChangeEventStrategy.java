package de.hybris.platform.ticket.strategies.impl;

import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.ticket.events.model.CsTicketChangeEventEntryModel;
import de.hybris.platform.ticket.model.CsAgentGroupModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.strategies.TicketAttributeChangeEventStrategy;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultTicketAttributeChangeEventStrategy implements TicketAttributeChangeEventStrategy
{
    private static final Logger LOG = Logger.getLogger(DefaultTicketAttributeChangeEventStrategy.class);
    private TypeService typeService;
    private ModelService modelService;
    private Map<String, String> valueType2ChangeRecordType;
    private String defaultChangeRecordType;


    public Set<CsTicketChangeEventEntryModel> getEntriesForChangedAttributes(CsTicketModel ticket)
    {
        Set<CsTicketChangeEventEntryModel> changedEntries = new HashSet<>(getContext((AbstractItemModel)ticket).getValueHistory().getDirtyAttributes().size());
        for(String attr : getContext((AbstractItemModel)ticket).getValueHistory().getDirtyAttributes())
        {
            CsTicketChangeEventEntryModel changeEntry = createChangeEntryForAttribute(ticket, attr);
            if(changeEntry != null)
            {
                changedEntries.add(changeEntry);
            }
        }
        return changedEntries;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    @Required
    public void setValueType2ChangeRecordType(Map<String, String> valueType2ChangeRecordType)
    {
        this.valueType2ChangeRecordType = valueType2ChangeRecordType;
    }


    @Required
    public void setDefaultChangeRecordType(String defaultChangeRecordType)
    {
        this.defaultChangeRecordType = defaultChangeRecordType;
    }


    protected CsTicketChangeEventEntryModel createChangeEntryForAttribute(CsTicketModel ticket, String attribute)
    {
        AttributeDescriptorModel attributeDescriptor = this.typeService.getAttributeDescriptor(this.typeService
                        .getComposedType(ticket.getClass()), attribute);
        String changeAttributeTypeCode = this.valueType2ChangeRecordType.get(attributeDescriptor.getAttributeType().getCode());
        if(changeAttributeTypeCode == null)
        {
            LOG.warn("Not able to find change attribute type for attribute [" + attributeDescriptor + "], using default");
            changeAttributeTypeCode = this.defaultChangeRecordType;
        }
        Object originalValue = getOriginalAttributeValue((ItemModel)ticket, attribute);
        Object newValue = getNewAttributeValue((ItemModel)ticket, attribute);
        if(ObjectUtils.equals(originalValue, newValue))
        {
            return null;
        }
        ComposedTypeModel changeAttributeType = this.typeService.getComposedType(changeAttributeTypeCode);
        CsTicketChangeEventEntryModel changeEventEntry = (CsTicketChangeEventEntryModel)getModelService().create(changeAttributeType.getCode());
        changeEventEntry.setAlteredAttribute(attributeDescriptor);
        if(!changeAttributeTypeCode.equals(this.defaultChangeRecordType))
        {
            if(this.typeService.getAttributeDescriptor(changeAttributeType, "oldValue") == null || this.typeService
                            .getAttributeDescriptor(changeAttributeType, "newValue") == null)
            {
                LOG.error("Cannot find oldval or newval methods on type changeEventEntry for types [" + attributeDescriptor
                                .getAttributeType().getItemtype() + "]");
                return null;
            }
            getModelService().setAttributeValue(changeEventEntry, "oldValue", originalValue);
            getModelService().setAttributeValue(changeEventEntry, "newValue", newValue);
        }
        changeEventEntry.setOldStringValue(stringValueOf(originalValue));
        changeEventEntry.setNewStringValue(stringValueOf(newValue));
        changeEventEntry.setOldBinaryValue(originalValue);
        changeEventEntry.setNewBinaryValue(newValue);
        return changeEventEntry;
    }


    protected Object getOriginalAttributeValue(ItemModel item, String attribute)
    {
        if(!getContext((AbstractItemModel)item).getValueHistory().isValueLoaded(attribute))
        {
            return getContext((AbstractItemModel)item).getAttributeProvider().getAttribute(attribute);
        }
        return getContext((AbstractItemModel)item).getValueHistory().getOriginalValue(attribute);
    }


    protected Object getNewAttributeValue(ItemModel item, String attribute)
    {
        return getModelService().getAttributeValue(item, attribute);
    }


    protected String stringValueOf(Object object)
    {
        if(object == null)
        {
            return "None";
        }
        if(object instanceof EmployeeModel)
        {
            return StringUtils.isNotEmpty(((EmployeeModel)object).getName()) ? ((EmployeeModel)object).getName() : (
                            (EmployeeModel)object).getUid();
        }
        if(object instanceof CsAgentGroupModel)
        {
            return StringUtils.isNotEmpty(((CsAgentGroupModel)object).getName()) ? ((CsAgentGroupModel)object).getName() : (
                            (CsAgentGroupModel)object).getUid();
        }
        if(object instanceof HybrisEnumValue)
        {
            return ((HybrisEnumValue)object).getCode();
        }
        if(object instanceof AbstractOrderModel)
        {
            return ((AbstractOrderModel)object).getCode();
        }
        return object.toString();
    }


    protected ItemModelContextImpl getContext(AbstractItemModel model)
    {
        return (ItemModelContextImpl)ModelContextUtils.getItemModelContext(model);
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
