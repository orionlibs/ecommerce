package de.hybris.platform.cockpit.components.navigationarea;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.Stack;

public class AbstractDrillableSelectorSection extends DefaultSectionSelectorSection implements DrillableSelectorSection
{
    protected Stack<TypedObject> drillableElements = new Stack<>();
    protected int drilldownLevel = 0;


    public TypedObject getLastElement()
    {
        TypedObject ret = null;
        if(!this.drillableElements.isEmpty())
        {
            ret = this.drillableElements.peek();
        }
        return ret;
    }


    public void appendAsLastElement(TypedObject element)
    {
        this.drillableElements.push(element);
    }


    public void removeLastElement()
    {
        if(!this.drillableElements.isEmpty())
        {
            this.drillableElements.pop();
        }
    }


    public void disable()
    {
        super.disable();
        this.drillableElements.clear();
    }


    public void clear()
    {
        this.drillableElements.clear();
    }


    public int getDrilldownLevel()
    {
        return this.drilldownLevel;
    }


    public void setDrilldownLevel(int drilldownLevel)
    {
        this.drilldownLevel = drilldownLevel;
    }


    public int currentLevel()
    {
        return (this.drillableElements != null) ? this.drillableElements.size() : 0;
    }
}
