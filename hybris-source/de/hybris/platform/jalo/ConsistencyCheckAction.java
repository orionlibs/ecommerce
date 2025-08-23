package de.hybris.platform.jalo;

public interface ConsistencyCheckAction
{
    void execute(Item.ItemAttributeMap paramItemAttributeMap) throws ConsistencyCheckException;
}
