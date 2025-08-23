package de.hybris.platform.cockpit.components.navigationarea.workflow.visualization;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import de.hybris.platform.workflow.WorkflowStatus;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.zkoss.zul.Menuitem;

public class WorkflowViewOptions
{
    Date filterFrom;
    Date filterTo;
    Set<Options> selected = Sets.newHashSet((Object[])new Options[] {Options.PLANNED, Options.RUNNING});


    public boolean isSelected(Options option)
    {
        return this.selected.contains(option);
    }


    public void resetFilters()
    {
        this.filterFrom = null;
        this.filterTo = null;
    }


    public Date getFilterFrom()
    {
        return this.filterFrom;
    }


    public void setFilterFrom(Date filterFrom)
    {
        this.filterFrom = filterFrom;
    }


    public Date getFilterTo()
    {
        return this.filterTo;
    }


    public void setFilterTo(Date filterTo)
    {
        this.filterTo = filterTo;
    }


    public void changeSelected(List workflowOptions)
    {
        check(workflowOptions.get(0), Options.PLANNED);
        check(workflowOptions.get(1), Options.RUNNING);
        check(workflowOptions.get(2), Options.FINISHED);
        check(workflowOptions.get(3), Options.FROM_TO);
        check(workflowOptions.get(4), Options.TERMINATED);
    }


    private void check(Object workflowOptionsChildren, Options option)
    {
        if(((Menuitem)workflowOptionsChildren).isChecked())
        {
            select(option);
        }
        else
        {
            unselect(option);
        }
    }


    public void select(Options option)
    {
        this.selected.add(option);
    }


    public void unselect(Options option)
    {
        this.selected.remove(option);
    }


    public EnumSet<WorkflowStatus> selectedStatuses()
    {
        if(this.selected.isEmpty())
        {
            return EnumSet.noneOf(WorkflowStatus.class);
        }
        return EnumSet.copyOf(Collections2.filter(
                        Collections2.transform(this.selected, (Function)new Object(this)), (Predicate)new Object(this)));
    }
}
