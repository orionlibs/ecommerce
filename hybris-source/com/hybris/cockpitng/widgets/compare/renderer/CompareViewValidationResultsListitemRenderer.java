/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.renderer;

import com.hybris.cockpitng.core.util.ObjectValuePath;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.validation.model.ValidationInfo;
import com.hybris.cockpitng.validation.model.ValidationSeverity;
import org.apache.commons.lang.StringUtils;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Vlayout;

public class CompareViewValidationResultsListitemRenderer implements ListitemRenderer<Object>
{
    private static final String SCLASS_RESULTS_CELL = "ye-validation-results-list-cell";
    private static final String SCLASS_RESULT_MESSAGE = "ye-validation-results-list-message";
    private static final String SCLASS_RESULT_ATTRIBUTE = "ye-validation-results-list-attribute";
    private static final String SCLASS_YE_VALIDATION_ERROR = "ye-validation-error";
    private static final String SCLASS_YE_VALIDATION_WARN = "ye-validation-warn";
    private static final String SCLASS_YE_VALIDATION_INFO = "ye-validation-info";
    private final LabelService labelService;
    private final TypeFacade typeFacade;
    private final CockpitLocaleService cockpitLocaleService;
    private Object currentObject = null;


    public CompareViewValidationResultsListitemRenderer(final LabelService labelService, final TypeFacade typeFacade,
                    final CockpitLocaleService cockpitLocaleService)
    {
        this.labelService = labelService;
        this.typeFacade = typeFacade;
        this.cockpitLocaleService = cockpitLocaleService;
    }


    @Override
    public void render(final Listitem item, final Object itemData, final int i)
    {
        if(itemData instanceof ValidationInfo && currentObject != null)
        {
            final ValidationInfo validationInfo = (ValidationInfo)itemData;
            final var cell = new Listcell();
            UITools.modifySClass(cell, SCLASS_RESULTS_CELL, true);
            if(validationInfo.getValidationSeverity().equals(ValidationSeverity.ERROR))
            {
                UITools.modifySClass(cell, SCLASS_YE_VALIDATION_ERROR, true);
            }
            else if(validationInfo.getValidationSeverity().equals(ValidationSeverity.WARN))
            {
                UITools.modifySClass(cell, SCLASS_YE_VALIDATION_WARN, true);
            }
            else if(validationInfo.getValidationSeverity().equals(ValidationSeverity.INFO))
            {
                UITools.modifySClass(cell, SCLASS_YE_VALIDATION_INFO, true);
            }
            item.appendChild(cell);
            final String path = validationInfo.getInvalidPropertyPath();
            final var cellLayout = new Vlayout();
            cellLayout.setSpacing("auto");
            cellLayout.setParent(cell);
            final var attrLabel = new Label(getAttributeLabel(currentObject, path));
            UITools.modifySClass(attrLabel, SCLASS_RESULT_ATTRIBUTE, true);
            cellLayout.appendChild(attrLabel);
            final var messageLabel = new Label(validationInfo.getValidationMessage());
            UITools.modifySClass(messageLabel, SCLASS_RESULT_MESSAGE, true);
            cellLayout.appendChild(messageLabel);
        }
        else
        {
            currentObject = itemData;
            final var cell = new Listcell();
            UITools.modifySClass(cell, SCLASS_RESULTS_CELL, true);
            item.appendChild(cell);
            final String currentItemlabel = labelService.getObjectLabel(currentObject);
            final var attrLabel = new Label(currentItemlabel);
            UITools.modifySClass(attrLabel, SCLASS_RESULT_ATTRIBUTE, true);
            cell.appendChild(attrLabel);
        }
    }


    private String getAttributeLabel(final Object object, final String path)
    {
        var attributeDescription = "";
        if(StringUtils.isEmpty(path))
        {
            attributeDescription = labelService.getObjectLabel(object);
        }
        else
        {
            final String locale = ObjectValuePath.getLocaleFromPath(path);
            final DataAttribute attribute = typeFacade.getAttribute(object, ObjectValuePath.getNotLocalizedPath(path));
            if(attribute != null)
            {
                attributeDescription = attribute.getLabel(cockpitLocaleService.getCurrentLocale());
                if(!StringUtils.isEmpty(attributeDescription) && !StringUtils.isEmpty(locale))
                {
                    attributeDescription = attributeDescription.concat(String.format(" [%s]", locale));
                }
            }
        }
        return StringUtils.isEmpty(attributeDescription) ? path : attributeDescription;
    }


    public void setCurrentObject(final Object currentObject)
    {
        this.currentObject = currentObject;
    }
}
