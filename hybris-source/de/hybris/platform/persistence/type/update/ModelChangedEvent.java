package de.hybris.platform.persistence.type.update;

import de.hybris.platform.persistence.type.AttributeDescriptorRemote;
import de.hybris.platform.util.jdbc.DBColumn;

public interface ModelChangedEvent
{
    String getSourceType();


    String getTargetType();


    DBColumn getCurrentColumnModel();


    AttributeDescriptorRemote getAttributeDescriptor();
}
