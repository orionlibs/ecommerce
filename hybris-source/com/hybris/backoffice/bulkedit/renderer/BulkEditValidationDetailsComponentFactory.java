/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.bulkedit.renderer;

import com.hybris.cockpitng.validation.model.ValidationInfo;
import org.zkoss.zk.ui.Component;

/**
 * Allows to create element which displays information about validation issue
 *
 * @param <T>
 */
public interface BulkEditValidationDetailsComponentFactory<T extends Component>
{
    T createValidationDetails(final ValidationInfo validationMessage);
}
