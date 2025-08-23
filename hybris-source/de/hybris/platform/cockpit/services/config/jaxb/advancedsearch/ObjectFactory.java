package de.hybris.platform.cockpit.services.config.jaxb.advancedsearch;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory
{
    public ShortcutValue createShortcutValue()
    {
        return new ShortcutValue();
    }


    public Condition createCondition()
    {
        return new Condition();
    }


    public ConditionList createConditionList()
    {
        return new ConditionList();
    }


    public Label createLabel()
    {
        return new Label();
    }


    public RootGroup createRootGroup()
    {
        return new RootGroup();
    }


    public Parameter createParameter()
    {
        return new Parameter();
    }


    public Property createProperty()
    {
        return new Property();
    }


    public Group createGroup()
    {
        return new Group();
    }


    public Type createType()
    {
        return new Type();
    }


    public ShortcutValueList createShortcutValueList()
    {
        return new ShortcutValueList();
    }


    public RelatedTypes createRelatedTypes()
    {
        return new RelatedTypes();
    }


    public AdvancedSearch createAdvancedSearch()
    {
        return new AdvancedSearch();
    }
}
