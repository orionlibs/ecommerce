package de.hybris.platform.cockpit.components.mvc.tree;

public class PositionAwareTreeControllerWrapper<T> extends TreeControllerWrapper<T> implements PositionAwareTreeController<T>
{
    public PositionAwareTreeControllerWrapper(PositionAwareTreeController<T> controllerToWrap)
    {
        super((TreeController)controllerToWrap);
    }


    public void move(Tree tree, Object node, Object target, boolean addAsChild, boolean append)
    {
        ((PositionAwareTreeController)this.controller).move(tree, node, target, addAsChild, append);
    }
}
