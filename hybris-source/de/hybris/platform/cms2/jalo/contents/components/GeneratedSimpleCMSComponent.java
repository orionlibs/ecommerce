package de.hybris.platform.cms2.jalo.contents.components;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.jalo.contents.containers.AbstractCMSComponentContainer;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedSimpleCMSComponent extends AbstractCMSComponent
{
    public static final String CONTAINERS = "containers";
    protected static String ELEMENTSFORCONTAINER_SRC_ORDERED = "relation.ElementsForContainer.source.ordered";
    protected static String ELEMENTSFORCONTAINER_TGT_ORDERED = "relation.ElementsForContainer.target.ordered";
    protected static String ELEMENTSFORCONTAINER_MARKMODIFIED = "relation.ElementsForContainer.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractCMSComponent.DEFAULT_INITIAL_ATTRIBUTES);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<AbstractCMSComponentContainer> getContainers(SessionContext ctx)
    {
        List<AbstractCMSComponentContainer> items = getLinkedItems(ctx, false, GeneratedCms2Constants.Relations.ELEMENTSFORCONTAINER, "AbstractCMSComponentContainer", null,
                        Utilities.getRelationOrderingOverride(ELEMENTSFORCONTAINER_SRC_ORDERED, true), false);
        return items;
    }


    public Collection<AbstractCMSComponentContainer> getContainers()
    {
        return getContainers(getSession().getSessionContext());
    }


    public long getContainersCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedCms2Constants.Relations.ELEMENTSFORCONTAINER, "AbstractCMSComponentContainer", null);
    }


    public long getContainersCount()
    {
        return getContainersCount(getSession().getSessionContext());
    }


    public void setContainers(SessionContext ctx, Collection<AbstractCMSComponentContainer> value)
    {
        setLinkedItems(ctx, false, GeneratedCms2Constants.Relations.ELEMENTSFORCONTAINER, null, value,
                        Utilities.getRelationOrderingOverride(ELEMENTSFORCONTAINER_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(ELEMENTSFORCONTAINER_MARKMODIFIED));
    }


    public void setContainers(Collection<AbstractCMSComponentContainer> value)
    {
        setContainers(getSession().getSessionContext(), value);
    }


    public void addToContainers(SessionContext ctx, AbstractCMSComponentContainer value)
    {
        addLinkedItems(ctx, false, GeneratedCms2Constants.Relations.ELEMENTSFORCONTAINER, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(ELEMENTSFORCONTAINER_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(ELEMENTSFORCONTAINER_MARKMODIFIED));
    }


    public void addToContainers(AbstractCMSComponentContainer value)
    {
        addToContainers(getSession().getSessionContext(), value);
    }


    public void removeFromContainers(SessionContext ctx, AbstractCMSComponentContainer value)
    {
        removeLinkedItems(ctx, false, GeneratedCms2Constants.Relations.ELEMENTSFORCONTAINER, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(ELEMENTSFORCONTAINER_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(ELEMENTSFORCONTAINER_MARKMODIFIED));
    }


    public void removeFromContainers(AbstractCMSComponentContainer value)
    {
        removeFromContainers(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("AbstractCMSComponentContainer");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(ELEMENTSFORCONTAINER_MARKMODIFIED);
        }
        return true;
    }
}
