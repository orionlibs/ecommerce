package de.hybris.platform.cockpit.services.search.impl;

public class EmployeeCompanyAttributeSearchDescriptor extends SpecialGenericSearchParameterDescriptor
{
    protected GenericQueryParameterCreator createCreator()
    {
        return (GenericQueryParameterCreator)new MyGenericQueryCreator((GenericSearchParameterDescriptor)this);
    }
}
