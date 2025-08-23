package de.hybris.platform.cockpit.wizards.generic;

import org.zkoss.zk.ui.Component;

public class DefaultGenericItemPage extends AbstractGenericItemPage
{
    public Component createRepresentationItself()
    {
        return (Component)this.pageContainer;
    }
}
