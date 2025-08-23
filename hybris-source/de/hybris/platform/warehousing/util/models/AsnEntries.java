package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.warehousing.model.AdvancedShippingNoticeEntryModel;
import de.hybris.platform.warehousing.util.builder.AsnEntryModelBuilder;

public class AsnEntries extends AbstractItems<AdvancedShippingNoticeEntryModel>
{
    public static final String CAMERA_CODE = "camera";
    public static final String MEMORY_CARD_CODE = "memorycard";
    public static final int CAMERA_QTY = 3;
    public static final int MEMORY_CARD_QTY = 2;


    public AdvancedShippingNoticeEntryModel CameraEntry()
    {
        return getOrCreateAsnEntry("camera", 3);
    }


    public AdvancedShippingNoticeEntryModel MemoryCardEntry()
    {
        return getOrCreateAsnEntry("memorycard", 2);
    }


    protected AdvancedShippingNoticeEntryModel getOrCreateAsnEntry(String productCode, int qty)
    {
        return AsnEntryModelBuilder.aModel().withProductCode(productCode).withQuantity(qty)
                        .build();
    }
}
