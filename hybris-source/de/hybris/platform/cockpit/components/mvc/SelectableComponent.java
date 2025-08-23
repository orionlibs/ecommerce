package de.hybris.platform.cockpit.components.mvc;

import java.util.Set;

public interface SelectableComponent<COMPONENT extends org.zkoss.zk.ui.Component, ITEM extends org.zkoss.zk.ui.Component, ELEMENT>
{
    Set<ELEMENT> getSelected();


    void selected(COMPONENT paramCOMPONENT, Set<ITEM> paramSet);
}
