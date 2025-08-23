package de.hybris.platform.customersupportbackoffice.dataaccess.facades.type;

import com.google.common.base.Preconditions;
import com.hybris.backoffice.cockpitng.dataaccess.facades.type.DefaultPlatformTypeFacadeStrategy;
import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.facades.type.CollectionDataType;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;

public class CustomerTypeFacadeStrategy extends DefaultPlatformTypeFacadeStrategy
{
    private static final String SAVED_CARTS_ATTR = "savedCarts";
    private static final String SAVED_CARTS_LIST = "savedCartsList";
    private DataType customerDataType;


    public boolean canHandle(String typeCode)
    {
        return "Customer".equals(typeCode);
    }


    public DataType load(String code, Context ctx) throws TypeNotFoundException
    {
        Preconditions.checkArgument((code != null), "code is null");
        if(this.customerDataType == null)
        {
            DataType orginalCustomerType = super.load(code, ctx);
            CollectionDataType.CollectionBuilder savedCartsListBuilder = new CollectionDataType.CollectionBuilder("savedCartsList");
            savedCartsListBuilder.valueType(orginalCustomerType).supertype("Item");
            DataAttribute.Builder attributeBuilder = (new DataAttribute.Builder("savedCarts")).primitive(false).ordered(true);
            attributeBuilder.searchable(false).localized(false).unique(false).writable(true).mandatory(false)
                            .valueType(savedCartsListBuilder.build());
            DataType.Builder finalCustomerTypeBuilder = new DataType.Builder(code);
            for(DataAttribute dataAttr : orginalCustomerType.getAttributes())
            {
                finalCustomerTypeBuilder.attribute(dataAttr);
            }
            finalCustomerTypeBuilder.labels(orginalCustomerType.getAllLabels());
            finalCustomerTypeBuilder.clazz(orginalCustomerType.getClazz());
            finalCustomerTypeBuilder.supertype(orginalCustomerType.getSuperType());
            finalCustomerTypeBuilder.type(orginalCustomerType.getType());
            finalCustomerTypeBuilder.allSuperTypes(orginalCustomerType.getAllSuperTypes());
            this.customerDataType = finalCustomerTypeBuilder.attribute(attributeBuilder.build()).build();
        }
        return this.customerDataType;
    }
}
