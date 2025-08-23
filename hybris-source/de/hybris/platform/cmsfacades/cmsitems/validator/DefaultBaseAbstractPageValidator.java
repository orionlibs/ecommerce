/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.validator;

import static de.hybris.platform.cmsfacades.common.validator.ValidationErrorBuilder.newValidationErrorBuilder;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_CONTAINS_INVALID_CHARS;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_LENGTH_EXCEEDED;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_REQUIRED;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_REQUIRED_L10N;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.NO_RESTRICTION_SET_FOR_VARIATION_PAGE;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cmsfacades.common.function.Validator;
import de.hybris.platform.cmsfacades.common.validator.ValidationErrorsProvider;
import de.hybris.platform.cmsfacades.languages.LanguageFacade;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import java.util.function.Predicate;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of the validator for {@link AbstractPageModel}
 */
public class DefaultBaseAbstractPageValidator implements Validator<AbstractPageModel>
{
    private Predicate<String> onlyHasSupportedCharactersPredicate;
    private LanguageFacade languageFacade;
    private CommonI18NService commonI18NService;
    private ValidationErrorsProvider validationErrorsProvider;
    private Predicate<String> validStringLengthPredicate;


    @Override
    public void validate(final AbstractPageModel validatee)
    {
        if(Strings.isBlank(validatee.getUid()))
        {
            getValidationErrorsProvider().getCurrentValidationErrors().add(
                            newValidationErrorBuilder() //
                                            .field(AbstractPageModel.UID) //
                                            .errorCode(FIELD_REQUIRED) //
                                            .build()
            );
        }
        else if(!getOnlyHasSupportedCharactersPredicate().test(validatee.getUid()))
        {
            getValidationErrorsProvider().getCurrentValidationErrors().add(
                            newValidationErrorBuilder() //
                                            .field(AbstractPageModel.UID) //
                                            .errorCode(FIELD_CONTAINS_INVALID_CHARS) //
                                            .build()
            );
        }
        getLanguageFacade().getLanguages().stream() //
                        .filter(LanguageData::isRequired) //
                        .forEach(languageData -> {
                            if(isBlank(validatee.getTitle(getCommonI18NService().getLocaleForIsoCode(languageData.getIsocode()))))
                            {
                                getValidationErrorsProvider().getCurrentValidationErrors().add(
                                                newValidationErrorBuilder() //
                                                                .field(AbstractPageModel.TITLE) //
                                                                .language(languageData.getIsocode())
                                                                .errorCode(FIELD_REQUIRED_L10N) //
                                                                .errorArgs(new Object[] {languageData.getIsocode()}) //
                                                                .build()
                                );
                            }
                        });
        getLanguageFacade().getLanguages().stream() //
                        .forEach(languageData -> {
                            final String title = validatee.getTitle(getCommonI18NService().getLocaleForIsoCode(languageData.getIsocode()));
                            if(isNotBlank(title) && !getValidStringLengthPredicate().test(title))
                            {
                                getValidationErrorsProvider().getCurrentValidationErrors().add(
                                                newValidationErrorBuilder() //
                                                                .field(AbstractPageModel.TITLE) //
                                                                .language(languageData.getIsocode())
                                                                .errorCode(FIELD_LENGTH_EXCEEDED) //
                                                                .rejectedValue(title)
                                                                .build()
                                );
                            }
                        });
        getLanguageFacade().getLanguages().stream() //
                        .forEach(languageData -> {
                            final String description = validatee.getDescription(getCommonI18NService().getLocaleForIsoCode(languageData.getIsocode()));
                            if(isNotBlank(description) && !getValidStringLengthPredicate().test(description))
                            {
                                getValidationErrorsProvider().getCurrentValidationErrors().add(
                                                newValidationErrorBuilder() //
                                                                .field(AbstractPageModel.DESCRIPTION) //
                                                                .language(languageData.getIsocode())
                                                                .errorCode(FIELD_LENGTH_EXCEEDED) //
                                                                .rejectedValue(description)
                                                                .build()
                                );
                            }
                        });
        if(!validatee.getDefaultPage() && CollectionUtils.isEmpty(validatee.getRestrictions()))
        {
            getValidationErrorsProvider().getCurrentValidationErrors().add(
                            newValidationErrorBuilder() //
                                            .field(AbstractPageModel.RESTRICTIONS) //
                                            .errorCode(NO_RESTRICTION_SET_FOR_VARIATION_PAGE) //
                                            .build()
            );
        }
    }


    protected final Predicate<String> getOnlyHasSupportedCharactersPredicate()
    {
        return onlyHasSupportedCharactersPredicate;
    }


    @Required
    public final void setOnlyHasSupportedCharactersPredicate(final Predicate<String> onlyHasSupportedCharactersPredicate)
    {
        this.onlyHasSupportedCharactersPredicate = onlyHasSupportedCharactersPredicate;
    }


    protected LanguageFacade getLanguageFacade()
    {
        return languageFacade;
    }


    @Required
    public void setLanguageFacade(final LanguageFacade languageFacade)
    {
        this.languageFacade = languageFacade;
    }


    protected CommonI18NService getCommonI18NService()
    {
        return commonI18NService;
    }


    @Required
    public void setCommonI18NService(final CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    protected ValidationErrorsProvider getValidationErrorsProvider()
    {
        return validationErrorsProvider;
    }


    @Required
    public void setValidationErrorsProvider(final ValidationErrorsProvider validationErrorsProvider)
    {
        this.validationErrorsProvider = validationErrorsProvider;
    }


    protected Predicate<String> getValidStringLengthPredicate()
    {
        return validStringLengthPredicate;
    }


    @Required
    public void setValidStringLengthPredicate(final Predicate<String> validStringLengthPredicate)
    {
        this.validStringLengthPredicate = validStringLengthPredicate;
    }
}
