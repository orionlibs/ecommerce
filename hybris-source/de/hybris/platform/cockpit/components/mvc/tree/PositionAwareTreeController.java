package de.hybris.platform.cockpit.components.mvc.tree;

public interface PositionAwareTreeController<T> extends TreeController<T>
{
    void move(Tree paramTree, T paramT1, T paramT2, boolean paramBoolean1, boolean paramBoolean2);
}
