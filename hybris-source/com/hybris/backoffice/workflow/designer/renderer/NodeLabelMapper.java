/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.renderer;

import java.util.function.UnaryOperator;

/**
 * Allows to create node's label
 */
@FunctionalInterface
public interface NodeLabelMapper extends UnaryOperator<String>
{
}
