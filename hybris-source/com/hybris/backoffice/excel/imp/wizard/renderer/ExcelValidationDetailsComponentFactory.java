/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.imp.wizard.renderer;

import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import org.zkoss.zk.ui.Component;

/**
 * Allows to create element which displays information about validation issue
 *
 * @param <T>
 */
interface ExcelValidationDetailsComponentFactory<T extends Component>
{
    T createValidationDetails(final Object columnHeader, final ValidationMessage validationMessage);
}
