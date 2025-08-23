package de.hybris.platform.patches.data;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;

public class ImpexImportPack implements Cloneable
{
    private List<ImpexImportUnit> importUnits;
    private boolean optional = false;


    public ImpexImportPack clone()
    {
        return clone(new ImpexImportPack());
    }


    protected ImpexImportPack clone(ImpexImportPack clone)
    {
        List<ImpexImportUnit> newImportUnits = new ArrayList<>(this.importUnits.size());
        for(ImpexImportUnit unit : getImportUnits())
        {
            newImportUnits.add(unit.clone());
        }
        clone.setOptional(this.optional);
        clone.setImportUnits(newImportUnits);
        return clone;
    }


    public List<ImpexImportUnit> getImportUnits()
    {
        return this.importUnits;
    }


    public void setImportUnits(List<ImpexImportUnit> importUnits)
    {
        this.importUnits = importUnits;
    }


    public void addImportUnit(ImpexImportUnit importUnit)
    {
        if(CollectionUtils.isEmpty(this.importUnits))
        {
            this.importUnits = new ArrayList<>();
        }
        this.importUnits.add(importUnit);
    }


    public boolean isOptional()
    {
        return this.optional;
    }


    public void setOptional(boolean optional)
    {
        this.optional = optional;
    }
}
