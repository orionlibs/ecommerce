package de.hybris.platform.adaptivesearchbackoffice.editors;

import org.zkoss.zk.ui.Component;

public interface EditorRenderer<L extends EditorLogic, D>
{
    default boolean isEnabled(L logic)
    {
        return true;
    }


    default boolean canRender(L logic, Component parent, D data)
    {
        return true;
    }


    default void beforeRender(L logic, Component parent, D data)
    {
    }


    void render(L paramL, Component paramComponent, D paramD);
}
