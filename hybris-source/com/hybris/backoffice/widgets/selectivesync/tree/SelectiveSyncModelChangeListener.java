/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.selectivesync.tree;

import de.hybris.platform.catalog.model.SyncAttributeDescriptorConfigModel;
import java.util.Collection;

/** Listener for changes in attributes model. */
public interface SelectiveSyncModelChangeListener
{
    void onValueChanged(final Object source, final Collection<SyncAttributeDescriptorConfigModel> syncAttributeDescriptors);
}
