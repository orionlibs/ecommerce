package de.hybris.platform.warehousing.util;

import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousing.data.allocation.DeclineEntries;
import de.hybris.platform.warehousing.data.allocation.DeclineEntry;
import de.hybris.platform.warehousing.enums.DeclineReason;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class DeclineEntryBuilder
{
    public static DeclineEntryBuilder aDecline()
    {
        return new DeclineEntryBuilder();
    }


    public DeclineEntries build_Manual(Map<ConsignmentEntryModel, Long> declineEntryInfo, WarehouseModel warehouse, DeclineReason reason)
    {
        DeclineEntries declineEntries = new DeclineEntries();
        Collection<DeclineEntry> entries = new ArrayList<>();
        declineEntryInfo.forEach((key, value) -> {
            DeclineEntry entry = new DeclineEntry();
            entry.setConsignmentEntry(key);
            entry.setQuantity(value);
            entry.setReason(reason);
            entry.setNotes("notes");
            entry.setReallocationWarehouse(warehouse);
            entries.add(entry);
        });
        declineEntries.setEntries(entries);
        return declineEntries;
    }


    public DeclineEntries build_Manual(Map<ConsignmentEntryModel, Long> declineEntryInfo, WarehouseModel warehouse)
    {
        return build_Manual(declineEntryInfo, warehouse, DeclineReason.TOOBUSY);
    }


    public DeclineEntries build_Auto(Map<ConsignmentEntryModel, Long> declineEntryInfo)
    {
        return build_Auto(declineEntryInfo, DeclineReason.TOOBUSY);
    }


    public DeclineEntries build_Auto(Map<ConsignmentEntryModel, Long> declineEntryInfo, DeclineReason reason)
    {
        DeclineEntries declineEntries = new DeclineEntries();
        Collection<DeclineEntry> entries = new ArrayList<>();
        declineEntryInfo.forEach((key, value) -> {
            DeclineEntry entry = new DeclineEntry();
            entry.setConsignmentEntry(key);
            entry.setQuantity(value);
            entry.setReason(reason);
            entry.setNotes("notes");
            entries.add(entry);
        });
        declineEntries.setEntries(entries);
        return declineEntries;
    }
}
