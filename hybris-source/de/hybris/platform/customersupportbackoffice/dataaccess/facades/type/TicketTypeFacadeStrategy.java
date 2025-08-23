package de.hybris.platform.customersupportbackoffice.dataaccess.facades.type;

import com.google.common.base.Preconditions;
import com.hybris.backoffice.cockpitng.dataaccess.facades.type.DefaultPlatformTypeFacadeStrategy;
import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.facades.type.CollectionDataType;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;

public class TicketTypeFacadeStrategy extends DefaultPlatformTypeFacadeStrategy
{
    private static final String TICKETS_ATTR = "tickets";
    private static final String CS_TICKET_LIST = "CsTicketList";
    private DataType ticketDataType;


    public boolean canHandle(String typeCode)
    {
        return "CsTicket".equals(typeCode);
    }


    public DataType load(String code, Context ctx) throws TypeNotFoundException
    {
        Preconditions.checkArgument((code != null), "code is null");
        if(this.ticketDataType == null)
        {
            DataType orginalTicketType = super.load(code, ctx);
            CollectionDataType.CollectionBuilder csTicketListBuilder = new CollectionDataType.CollectionBuilder("CsTicketList");
            csTicketListBuilder.valueType(orginalTicketType).supertype("Item");
            DataAttribute.Builder attributeBuilder = (new DataAttribute.Builder("tickets")).primitive(false).ordered(true);
            attributeBuilder.searchable(false).localized(false).unique(false).writable(true).mandatory(false)
                            .valueType(csTicketListBuilder.build());
            DataType.Builder finalTicketTypeBuilder = new DataType.Builder(code);
            for(DataAttribute dataAttr : orginalTicketType.getAttributes())
            {
                finalTicketTypeBuilder.attribute(dataAttr);
            }
            finalTicketTypeBuilder.labels(orginalTicketType.getAllLabels());
            finalTicketTypeBuilder.clazz(orginalTicketType.getClazz());
            finalTicketTypeBuilder.supertype(orginalTicketType.getSuperType());
            finalTicketTypeBuilder.type(orginalTicketType.getType());
            this.ticketDataType = finalTicketTypeBuilder.attribute(attributeBuilder.build()).build();
        }
        return this.ticketDataType;
    }
}
