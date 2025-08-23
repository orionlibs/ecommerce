package de.hybris.platform.catalog.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.util.localization.Localization;
import java.util.ArrayList;
import java.util.List;

public class RemoveCatalogVersionCronJob extends GeneratedRemoveCatalogVersionCronJob
{
    int toDeleteItemCount = 0;
    int deletedItemCount = 0;
    int currentProcessingItemCount = 0;
    private String currentStep = "";
    private final List<String> history = new ArrayList<>();


    public int getPercent()
    {
        if(getTotalDeleteItemCountAsPrimitive() <= 0)
        {
            return 0;
        }
        int result = (this.deletedItemCount + getCurrentProcessingItemCountAsPrimitive()) * 100 / getTotalDeleteItemCountAsPrimitive();
        return (result > 100) ? 100 : result;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setToDeleteItemCount(int todeleteitems)
    {
        this.toDeleteItemCount = todeleteitems;
    }


    public void setDeletedItemCount(int deleteditemcount)
    {
        this.deletedItemCount = deleteditemcount;
    }


    public void addDeletedItemCount(int deleteditemcount)
    {
        this.deletedItemCount += deleteditemcount;
    }


    @ForceJALO(reason = "something else")
    public void setCurrentProcessingItemCount(int prossecingItemCount)
    {
        this.currentProcessingItemCount = prossecingItemCount;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public int getToDeleteItemCount()
    {
        return this.toDeleteItemCount;
    }


    public int getDeletedItemCount()
    {
        return this.deletedItemCount + this.currentProcessingItemCount;
    }


    public void setCurrentProcess(String name)
    {
        this.currentStep = name;
    }


    public String getCurrentProcess()
    {
        return "\"" + this.currentStep + "\" " + Localization.getLocalizedString("removecatalogwizard.itemcount") +
                        getDeletedItemCount() + "/" + getTotalDeleteItemCountAsPrimitive();
    }


    public void addHistory(String cvname, int deleted, int max)
    {
        this.history.add(cvname + ": " + cvname + "/" + deleted);
    }


    public List<String> getHistory()
    {
        return this.history;
    }
}
