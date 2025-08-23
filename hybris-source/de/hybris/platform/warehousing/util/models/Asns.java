package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousing.enums.AsnStatus;
import de.hybris.platform.warehousing.model.AdvancedShippingNoticeEntryModel;
import de.hybris.platform.warehousing.model.AdvancedShippingNoticeModel;
import de.hybris.platform.warehousing.util.builder.AsnModelBuilder;
import de.hybris.platform.warehousing.util.dao.impl.AsnDaoImpl;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class Asns extends AbstractItems<AdvancedShippingNoticeModel>
{
    public static final String INTERNAL_ID_CAMERA_BOSTON = "B0006";
    public static final String INTERNAL_ID_CAMERA_MEMORY_CARD_BOSTON = "B0002";
    public static final String INTERNAL_ID_MEMORY_CARD_BOSTON = "B0003";
    public static final String INTERNAL_ID_CAMERA_MONTREAL = "B0004";
    public static final String INTERNAL_ID_MEMORY_CARD_MONTREAL = "B0005";
    public static final String INTERNAL_ID = "B0001";
    public static final String EXTERNAL_ID = "EXT123";
    public static final AsnStatus STATUS = AsnStatus.CREATED;
    private AsnDaoImpl asnsDao;
    private PointsOfService pointsOfService;
    private Warehouses warehouses;
    private AsnEntries asnEntries;


    public AdvancedShippingNoticeModel CameraAsn_Boston()
    {
        AdvancedShippingNoticeModel advancedShippingNotice = getOrCreateAsn("B0006", new Date(),
                        getWarehouses().Boston());
        advancedShippingNotice.setAsnEntries(Collections.singletonList(this.asnEntries.CameraEntry()));
        advancedShippingNotice.getAsnEntries().forEach(asnEntry -> asnEntry.setAsn(advancedShippingNotice));
        return advancedShippingNotice;
    }


    public AdvancedShippingNoticeModel CameraAsn_Montreal()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
        AdvancedShippingNoticeModel advancedShippingNotice = getOrCreateAsn("B0004", cal.getTime(),
                        getWarehouses().Montreal());
        advancedShippingNotice.setAsnEntries(Collections.singletonList(this.asnEntries.CameraEntry()));
        advancedShippingNotice.getAsnEntries().forEach(asnEntry -> asnEntry.setAsn(advancedShippingNotice));
        return advancedShippingNotice;
    }


    public AdvancedShippingNoticeModel CameraAndMemoryCardAsn_Boston()
    {
        AdvancedShippingNoticeModel advancedShippingNotice = getOrCreateAsn("B0002", new Date(),
                        getWarehouses().Boston());
        List<AdvancedShippingNoticeEntryModel> asnEntriesList = new ArrayList<>();
        asnEntriesList.add(this.asnEntries.CameraEntry());
        asnEntriesList.add(this.asnEntries.MemoryCardEntry());
        advancedShippingNotice.setAsnEntries(asnEntriesList);
        advancedShippingNotice.getAsnEntries().forEach(asnEntry -> asnEntry.setAsn(advancedShippingNotice));
        return advancedShippingNotice;
    }


    public AdvancedShippingNoticeModel EXT123(Date date)
    {
        return getOrCreateAsn("B0001", date, getWarehouses().Boston());
    }


    protected AdvancedShippingNoticeModel getOrCreateAsn(String internalId, Date releaseDate, WarehouseModel warehouse)
    {
        return (AdvancedShippingNoticeModel)getOrSaveAndReturn(() -> (AdvancedShippingNoticeModel)getAsnsDao().getByCode(internalId),
                        () -> AsnModelBuilder.aModel().withInternalId(internalId).withExternalId("EXT123").withStatus(STATUS).withPoS(getPointsOfService().Boston()).withWarehouse(warehouse).withReleaseDate(releaseDate).build());
    }


    protected AsnDaoImpl getAsnsDao()
    {
        return this.asnsDao;
    }


    @Required
    public void setAsnsDao(AsnDaoImpl asnsDao)
    {
        this.asnsDao = asnsDao;
    }


    protected PointsOfService getPointsOfService()
    {
        return this.pointsOfService;
    }


    @Required
    public void setPointsOfService(PointsOfService pointsOfService)
    {
        this.pointsOfService = pointsOfService;
    }


    protected Warehouses getWarehouses()
    {
        return this.warehouses;
    }


    @Required
    public void setWarehouses(Warehouses warehouses)
    {
        this.warehouses = warehouses;
    }


    protected AsnEntries getAsnEntries()
    {
        return this.asnEntries;
    }


    @Required
    public void setAsnEntries(AsnEntries asnEntries)
    {
        this.asnEntries = asnEntries;
    }
}
