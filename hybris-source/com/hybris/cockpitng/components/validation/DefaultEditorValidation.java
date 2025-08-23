/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.validation;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.model.ModelObserver;
import com.hybris.cockpitng.core.model.StandardModelKeys;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.validation.ValidationHandler;
import com.hybris.cockpitng.validation.impl.ValidationInfoFactoryWithPrefix;
import com.hybris.cockpitng.validation.model.ValidationInfo;
import com.hybris.cockpitng.validation.model.ValidationResult;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Div;

public class DefaultEditorValidation implements EditorValidation
{
    private final String path;
    private static final String LOCALIZED_EDITOR_SCLASS = "ye-localized";
    private transient ModelObserver validationObserver;
    private transient ValidationRenderer validationRenderer;
    private ValidationHandler validationHandler;
    private Div validationInfoPanel;
    private ValidationResult validationResult = ValidationResult.EMPTY;
    private ValidatableContainer validatableContainer;
    private Editor editor;


    public DefaultEditorValidation(final String path)
    {
        this.path = path;
    }


    public DefaultEditorValidation(final String path, final ValidationHandler validationHandler)
    {
        this.path = path;
        this.validationHandler = validationHandler;
    }


    public String getPrefix()
    {
        final String qualifier = getQualifier();
        final String pattern = Pattern.quote("." + qualifier) + "$";
        return getFullPath().replaceFirst(pattern, StringUtils.EMPTY);
    }


    public String getQualifier()
    {
        return validatableContainer.getCurrentObjectPath(getFullPath());
    }


    public String getFullPath()
    {
        return path;
    }


    protected boolean isValidationChanged(final ValidationResult violations)
    {
        return this.validationResult == ValidationResult.EMPTY || !Objects.equals(this.validationResult, violations);
    }


    @Override
    public <T> void init(final ValidatableContainer validatableContainer, final Editor editor,
                    final EditorContext<T> editorContext)
    {
        this.validatableContainer = validatableContainer;
        this.editor = editor;
        validationObserver = new ModelObserver()
        {
            @Override
            public void modelChanged(final String property)
            {
                if(property.equals(getFullPath()) || property.startsWith(getFullPath() + ".")
                                || getFullPath().startsWith(property + ".") || property.equals(StandardModelKeys.VALIDATION_RESULT_KEY))
                {
                    modelChanged();
                }
            }


            @Override
            public void modelChanged()
            {
                processValidationResult(editorContext);
            }
        };
        validatableContainer.addValidationObserver(getFullPath(), validationObserver);
        validationResult = validatableContainer.getCurrentValidationResult(getFullPath());
    }


    @Override
    public void editorRendered()
    {
        this.validationInfoPanel = new Div();
        UITools.modifySClass(this.validationInfoPanel, "ye-validation-panel", true);
        UITools.modifySClass(this.validationInfoPanel, LOCALIZED_EDITOR_SCLASS, editor.isLocalized());
        editor.getChildren().add(0, validationInfoPanel);
        editor.addEventListener(ValidationFocusTransferHandler.ON_FOCUS_TRANSFERRED, e -> applyValidationCss(true));
        applyValidationCss();
    }


    @Override
    public void editorValidationChanged()
    {
        final Object currentObject = validatableContainer.getCurrentObject(getFullPath());
        final String currentObjectPath = validatableContainer.getCurrentObjectPath(getFullPath());
        final List<ValidationInfo> validationViolations = getValidationHandler().validate(currentObject,
                        Collections.singletonList(currentObjectPath));
        updateValidationResultsIfNeeded(validationViolations);
    }


    protected <T> void processValidationResult(final EditorContext<T> editorContext)
    {
        final ValidationResult currentValidationResult = getPersistedValidationResult();
        final boolean changed = isValidationChanged(currentValidationResult);
        if(changed)
        {
            this.validationResult = currentValidationResult;
            applyValidationCss();
            notifyValidationChange();
        }
    }


    private void updateValidationResultsIfNeeded(final List<ValidationInfo> validationViolations)
    {
        final ValidationResult currentValidationResult = ValidationInfoFactoryWithPrefix
                        .addPrefix(new ValidationResult(validationViolations), getPrefix());
        if(isValidationChanged(currentValidationResult))
        {
            validationResult = currentValidationResult;
            final ValidationResult globalValidationResult = validatableContainer.getCurrentValidationResult();
            if(globalValidationResult != null)
            {
                validatableContainer.setPreventBroadcastValidationChange(false);
                globalValidationResult.updateValidationInfo(getFullPath(), currentValidationResult.getAll());
            }
            applyValidationCss();
        }
    }


    protected void applyValidationCss()
    {
        applyValidationCss(false);
    }


    protected String getValidationObservedKey()
    {
        if(StringUtils.isNotEmpty(getFullPath()))
        {
            return String.format("%s.%s", StandardModelKeys.VALIDATION_RESULT_KEY, getFullPath());
        }
        else
        {
            return StringUtils.EMPTY;
        }
    }


    protected void applyValidationCss(final boolean expanded)
    {
        getValidationRenderer().cleanAllValidationCss(editor);
        if(validationResult != null)
        {
            final String styleID = getValidationRenderer().getSeverityStyleClass(validationResult.getHighestSeverity());
            if(StringUtils.isNotBlank(styleID))
            {
                UITools.modifySClass(editor, styleID, true);
            }
            Components.removeAllChildren(validationInfoPanel);
            final Component validationInfo = getValidationRenderer().createValidationMessageBtn(validationResult, expanded);
            if(validationInfo != null)
            {
                validationInfoPanel.appendChild(validationInfo);
            }
            notifyValidationChange();
        }
    }


    protected void notifyValidationChange()
    {
        Events.postEvent(ON_VALIDATION_CHANGED, editor, validationResult);
    }


    protected ValidationResult getPersistedValidationResult()
    {
        return validatableContainer.getCurrentValidationResult(getFullPath());
    }


    @Override
    public void cleanup()
    {
        if(validationObserver != null)
        {
            validatableContainer.removeValidationObserver(validationObserver);
            validationObserver = null;
        }
    }


    public ValidationRenderer getValidationRenderer()
    {
        if(validationRenderer == null)
        {
            validationRenderer = (ValidationRenderer)SpringUtil.getBean("validationRenderer");
        }
        return validationRenderer;
    }


    public ValidationHandler getValidationHandler()
    {
        if(this.validationHandler == null)
        {
            this.validationHandler = (ValidationHandler)SpringUtil.getBean("validationHandler", ValidationHandler.class);
        }
        return this.validationHandler;
    }
}
