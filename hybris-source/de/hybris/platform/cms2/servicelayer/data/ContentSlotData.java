package de.hybris.platform.cms2.servicelayer.data;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import java.io.Serializable;
import java.util.Collection;

public interface ContentSlotData extends Serializable
{
    boolean isFromMaster();


    String getPosition();


    String getName();


    String getUid();


    ContentSlotModel getContentSlot();


    boolean isAllowOverwrite();


    boolean isOverrideSlot();


    Collection<ComposedTypeModel> getAvailableCMSComponents();


    void setAvailableCMSComponents(Collection<ComposedTypeModel> paramCollection);


    Collection<ComposedTypeModel> getAvailableCMSComponentContainers();


    void setAvailableCMSComponentContainers(Collection<ComposedTypeModel> paramCollection);


    Collection<AbstractCMSComponentModel> getCMSComponents();


    void setIsOverrideSlot(Boolean paramBoolean);


    String getPageId();
}
