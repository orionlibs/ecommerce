package de.hybris.platform.cms2.jalo.contents.containers;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.jalo.contents.components.AbstractCMSComponent;
import de.hybris.platform.cms2.jalo.contents.components.SimpleCMSComponent;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedAbstractCMSComponentContainer extends AbstractCMSComponent
{
    public static final String CURRENTCMSCOMPONENTS = "currentCMSComponents";
    public static final String SIMPLECMSCOMPONENTS = "simpleCMSComponents";
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


    public List<SimpleCMSComponent> getCurrentCMSComponents()
    {
        return getCurrentCMSComponents(getSession().getSessionContext());
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("SimpleCMSComponent");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(ELEMENTSFORCONTAINER_MARKMODIFIED);
        }
        return true;
    }


    public List<SimpleCMSComponent> getSimpleCMSComponents(SessionContext ctx)
    {
        List<SimpleCMSComponent> items = getLinkedItems(ctx, true, GeneratedCms2Constants.Relations.ELEMENTSFORCONTAINER, "SimpleCMSComponent", null,
                        Utilities.getRelationOrderingOverride(ELEMENTSFORCONTAINER_SRC_ORDERED, true), false);
        return items;
    }


    public List<SimpleCMSComponent> getSimpleCMSComponents()
    {
        return getSimpleCMSComponents(getSession().getSessionContext());
    }


    public long getSimpleCMSComponentsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCms2Constants.Relations.ELEMENTSFORCONTAINER, "SimpleCMSComponent", null);
    }


    public long getSimpleCMSComponentsCount()
    {
        return getSimpleCMSComponentsCount(getSession().getSessionContext());
    }


    public void setSimpleCMSComponents(SessionContext ctx, List<SimpleCMSComponent> value)
    {
        setLinkedItems(ctx, true, GeneratedCms2Constants.Relations.ELEMENTSFORCONTAINER, null, value,
                        Utilities.getRelationOrderingOverride(ELEMENTSFORCONTAINER_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(ELEMENTSFORCONTAINER_MARKMODIFIED));
    }


    public void setSimpleCMSComponents(List<SimpleCMSComponent> value)
    {
        setSimpleCMSComponents(getSession().getSessionContext(), value);
    }


    public void addToSimpleCMSComponents(SessionContext ctx, SimpleCMSComponent value)
    {
        addLinkedItems(ctx, true, GeneratedCms2Constants.Relations.ELEMENTSFORCONTAINER, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(ELEMENTSFORCONTAINER_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(ELEMENTSFORCONTAINER_MARKMODIFIED));
    }


    public void addToSimpleCMSComponents(SimpleCMSComponent value)
    {
        addToSimpleCMSComponents(getSession().getSessionContext(), value);
    }


    public void removeFromSimpleCMSComponents(SessionContext ctx, SimpleCMSComponent value)
    {
        removeLinkedItems(ctx, true, GeneratedCms2Constants.Relations.ELEMENTSFORCONTAINER, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(ELEMENTSFORCONTAINER_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(ELEMENTSFORCONTAINER_MARKMODIFIED));
    }


    public void removeFromSimpleCMSComponents(SimpleCMSComponent value)
    {
        removeFromSimpleCMSComponents(getSession().getSessionContext(), value);
    }


    public abstract List<SimpleCMSComponent> getCurrentCMSComponents(SessionContext paramSessionContext);
}
