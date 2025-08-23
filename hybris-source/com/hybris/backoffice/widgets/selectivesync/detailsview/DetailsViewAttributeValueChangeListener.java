/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.selectivesync.detailsview;

import com.hybris.backoffice.widgets.selectivesync.renderer.SelectiveSyncRenderer;
import de.hybris.platform.catalog.model.SyncAttributeDescriptorConfigModel;

/** Listener for changes in details view of {@link SelectiveSyncRenderer}. */
public interface DetailsViewAttributeValueChangeListener
{
    /**
     * Fired when some attribute has changed in syncAttributeDescriptorConfigModel.
     *
     * @param syncAttributeDescriptorConfigModel
     *           attribute descriptor model
     * @param attribute
     *           name of changed attribute
     * @param value
     *           new value of the changed attribute
     */
    void attributeChanged(final SyncAttributeDescriptorConfigModel syncAttributeDescriptorConfigModel, final String attribute,
                    final Object value);
}
