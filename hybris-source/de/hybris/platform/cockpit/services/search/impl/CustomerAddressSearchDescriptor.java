package de.hybris.platform.cockpit.services.search.impl;

public class CustomerAddressSearchDescriptor extends SpecialGenericSearchParameterDescriptor
{
    protected GenericQueryParameterCreator createCreator()
    {
        return (GenericQueryParameterCreator)new MyGenericQueryCreator((GenericSearchParameterDescriptor)this);
    }
}
