package de.hybris.platform.commons.translator.prerenderers;

import de.hybris.platform.commons.translator.nodes.AbstractNode;

public interface Prerenderer
{
    AbstractNode prepareNode(AbstractNode paramAbstractNode);
}
