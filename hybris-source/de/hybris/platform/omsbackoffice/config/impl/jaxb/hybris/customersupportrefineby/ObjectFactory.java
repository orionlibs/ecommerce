package de.hybris.platform.omsbackoffice.config.impl.jaxb.hybris.customersupportrefineby;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory
{
    public RefineBy createRefineBy()
    {
        return new RefineBy();
    }


    public FieldList createFieldList()
    {
        return new FieldList();
    }


    public SearchField createSearchField()
    {
        return new SearchField();
    }


    public SearchValue createSearchValue()
    {
        return new SearchValue();
    }


    public GroupMemberValue createGroupMemberValue()
    {
        return new GroupMemberValue();
    }
}
