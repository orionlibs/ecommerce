package de.hybris.platform.persistence.type.update.strategy;

import de.hybris.platform.persistence.type.AttributeDescriptorRemote;
import de.hybris.platform.util.jdbc.DBColumn;

public interface ChangeColumnStrategy
{
    boolean doChangeColumn(String paramString, DBColumn paramDBColumn, AttributeDescriptorRemote paramAttributeDescriptorRemote);
}
