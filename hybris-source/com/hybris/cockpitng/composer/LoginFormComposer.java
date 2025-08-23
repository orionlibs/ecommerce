/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.composer;

import com.google.common.base.Splitter;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.core.CNG;
import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.handler.login.LoginInformationConfigData;
import com.hybris.cockpitng.handler.login.LoginInformationHandler;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.renderers.common.TypedSettingsRenderer;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import com.hybris.cockpitng.util.YTestTools;
import com.hybris.cockpitng.util.web.authorization.BackofficeAuthenticationFailureHandler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.util.Locales;
import org.zkoss.util.resource.Labels;
import org.zkoss.web.Attributes;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.ClientInfoEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.impl.InputElement;
import org.zkoss.zul.impl.LabelElement;

public class LoginFormComposer extends BaseBookmarkAwareLoginFormComposer
{
    public static final String DEFAULT_LOGIN_SCREEN_LOCALE = "en";
    public static final String LANG_PACKS_PROPERTY = "lang.packs";
    public static final String ON_LANGUAGE_CHANGED = "onLanguageChanged";
    public static final String ATTRIBUTE_LABEL_KEY = "labelKey";
    public static final String REMEMBER_ME_AUTH_FAILED_PARAM = "rememberMeAuthFailed";
    private static final Logger LOG = LoggerFactory.getLogger(LoginFormComposer.class);
    private static final String LOGIN_ERROR_MESSAGE = "login.error.message";
    private static final String LOGIN_ACCESSDENIED_MESSAGE = "login.accessdenied.message";
    private static final String LOGIN_LANGUAGE_LABEL = "login.language.label";
    private static final String LOGIN_BUTTON_LANGUAGE_LABEL = "login.button.label";
    private static final String SINGLESIGNON_ANCHOR_LABEL = "login.singlesignon.label";
    @WireVariable
    private Popup localeListboxPopup;
    @WireVariable
    private Button localeListboxBtn;
    @WireVariable
    private Textbox currentLanguageLabel;
    @WireVariable
    private Listbox localeListbox;
    @WireVariable
    private Label status;
    @WireVariable
    private Label accessdenied;
    @WireVariable
    private Label languageLabel;
    @WireVariable
    private Div loginFormContainer;
    @WireVariable
    private Div configuredFieldPanel;
    @WireVariable
    private A ssoAnchor;
    @WireVariable
    private Button loginButton;
    private transient TypedSettingsRenderer loginInfoRenderer;
    private transient CockpitLocaleService cockpitLocaleService;
    private transient CockpitProperties cockpitProperties;
    private transient CockpitProperties globalProperties;
    private Collection<String> availableLangPacks;


    @Override
    public void doAfterCompose(final Component comp) throws Exception
    {
        super.doAfterCompose(comp);
        initLangPacks();
        if(SecurityContextHolder.getContext().getAuthentication() == null
                        || SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)
        {
            final Object rememberMeAuthFailedAttrib = Sessions.getCurrent().getAttribute(REMEMBER_ME_AUTH_FAILED_PARAM);
            if(Objects.equals(rememberMeAuthFailedAttrib, true))
            {
                Sessions.getCurrent().removeAttribute(REMEMBER_ME_AUTH_FAILED_PARAM);
                Executions.sendRedirect(BackofficeAuthenticationFailureHandler.DEFAULT_FAILURE_URL);
                return;
            }
            loginFormContainer.setVisible(true);
        }
        else
        {
            Executions.sendRedirect("/");
            return;
        }
        if(cockpitProperties.getBoolean(CNG.PROPERTY_DEVELOPMENT_MODE))
        {
            Labels.reset();
        }
        renderConfiguredFields(true);
        configuredFieldPanel.addEventListener(ON_LANGUAGE_CHANGED, this::onLanguageChanged);
        localeListbox.addEventListener(Events.ON_SELECT, event -> switchLocale());
        final Component widgetContainer = loginFormContainer.getParent();
        if(Objects.nonNull(widgetContainer))
        {
            widgetContainer.addEventListener(Events.ON_CLICK, event -> closePopover());
        }
        populateLocales();
        setSelected();
        updateLabels();
    }


    protected void closePopover()
    {
        if(localeListboxPopup.isVisible())
        {
            localeListboxPopup.close();
        }
    }


    protected void onLanguageChanged(final Event event)
    {
        doForEachChildComponent(event.getTarget(),
                        component -> StringUtils.isNotBlank((String)component.getAttribute(ATTRIBUTE_LABEL_KEY)), this::resetComponentLabel);
    }


    protected void doForEachChildComponent(final Component parent, final Predicate<Component> predicate,
                    final Consumer<Component> consumer)
    {
        parent.getChildren().forEach(component -> {
            if(predicate.test(component))
            {
                consumer.accept(component);
            }
            doForEachChildComponent(component, predicate, consumer);
        });
    }


    public void initLangPacks()
    {
        final String isoCodes = getGlobalProperties().getProperty(LANG_PACKS_PROPERTY);
        if(StringUtils.isNotBlank(isoCodes))
        {
            this.availableLangPacks = new ArrayList<>();
            final Iterable<String> codes = Splitter.on(',').trimResults().omitEmptyStrings().split(isoCodes);
            codes.forEach(code -> this.availableLangPacks.add(code));
            LOG.info("Available UI locales for backoffice: {}", availableLangPacks);
        }
        else
        {
            LOG.info("No valid UI languages were defined. All available locales will be used as valid UI languages.");
        }
    }


    protected void renderConfiguredFields(final boolean updateFocus)
    {
        boolean shouldSetFocus = updateFocus;
        configuredFieldPanel.getChildren().clear();
        for(final LoginInformationConfigData loginTimeData : getLoginInformationHandler().getConfiguration())
        {
            final Hbox hbox = new Hbox();
            hbox.setSclass("rowCnt");
            final Div labelDiv = new Div();
            labelDiv.setSclass("labelRowCnt");
            final Div compDiv = new Div();
            compDiv.setSclass("compRowCnt");
            hbox.appendChild(labelDiv);
            hbox.appendChild(compDiv);
            configuredFieldPanel.appendChild(hbox);
            final Label label = new Label(Labels.getLabel(loginTimeData.getName()));
            label.setAttribute(ATTRIBUTE_LABEL_KEY, loginTimeData.getName());
            labelDiv.appendChild(label);
            final Component view = loginInfoRenderer.renderSetting(getLoginInformationHandler().getLoginInformation(),
                            loginTimeData.getKey(), false);
            if(view != null)
            {
                compDiv.appendChild(view);
                if(shouldSetFocus && view instanceof InputElement)
                {
                    ((InputElement)view).setFocus(true);
                    shouldSetFocus = false;
                }
            }
        }
    }


    protected void resetComponentLabel(final Component component)
    {
        final String newLabel = Labels.getLabel((String)component.getAttribute(ATTRIBUTE_LABEL_KEY));
        if(StringUtils.isBlank(newLabel))
        {
            return;
        }
        if(component instanceof Label)
        {
            ((Label)component).setValue(newLabel);
        }
        else if(component instanceof LabelElement)
        {
            ((LabelElement)component).setLabel(newLabel);
        }
        component.invalidate();
    }


    protected void populateLocales()
    {
        final Collection<String> supportedLangPacks = getAvailableLangPacks();
        for(final Locale locale : cockpitLocaleService.getAllLocales())
        {
            if(!supportedLangPacks.contains(locale.toString()))
            {
                continue;
            }
            // the Locale from getAllLocales seems not to be compatible with ZUL's Locale, so the following must be done
            try
            {
                final Locale localeZul = Locales.getLocale(locale.toString());
                final Listitem listitem = localeListbox.appendItem(localeZul.getDisplayName(localeZul), localeZul.toString());
                YTestTools.modifyYTestId(listitem, "locale-" + locale.toString());
            }
            catch(final IllegalArgumentException iae)
            {
                LOG.warn(String.format("Invalid locale: %s", locale.toString()));
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(String.format("Could not convert %s to %s instance", locale.toString(), Locale.class.getCanonicalName()),
                                    iae);
                }
            }
        }
    }


    protected void switchLocale()
    {
        final String locale = localeListbox.getSelectedItem().getValue().toString();
        cockpitLocaleService.setCurrentLocale(Locales.getLocale(locale));
        updateLabels();
        Events.sendEvent(ON_LANGUAGE_CHANGED, configuredFieldPanel, null);
    }


    protected void updateLabels()
    {
        currentLanguageLabel.setValue(localeListbox.getSelectedItem().getLabel());
        languageLabel.setValue(getLabel(LOGIN_LANGUAGE_LABEL));
        status.setValue(getLabel(LOGIN_ERROR_MESSAGE));
        accessdenied.setValue(getLabel(LOGIN_ACCESSDENIED_MESSAGE));
        loginButton.setLabel(getLabel(LOGIN_BUTTON_LANGUAGE_LABEL));
        if(ssoAnchor != null)
        {
            ssoAnchor.setLabel(getLabel(SINGLESIGNON_ANCHOR_LABEL));
        }
    }


    protected String getLabel(final String labelKey)
    {
        return Labels.getLabel(labelKey);
    }


    protected void setSelected()
    {
        final String currentLocale = cockpitLocaleService.getCurrentLocale().toString();
        for(final Listitem localeItem : localeListbox.getItems())
        {
            // equalsIgnoreCase must be used because getCurrentLocale returns uppercase version
            if(localeItem.getValue().toString().equalsIgnoreCase(currentLocale))
            {
                localeListbox.setSelectedItem(localeItem);
                cockpitLocaleService.setCurrentLocale(Locales.getLocale(currentLocale));
                return;
            }
        }
        for(final Listitem localeItem : localeListbox.getItems())
        {
            if(localeItem.getValue().toString().toLowerCase(Locale.getDefault()).startsWith(currentLocale)
                            || currentLocale.toLowerCase(Locale.getDefault()).startsWith(localeItem.getValue().toString()))
            {
                localeListbox.setSelectedItem(localeItem);
                cockpitLocaleService.setCurrentLocale(Locales.getLocale(Objects.toString(localeItem.getValue())));
                return;
            }
        }
        if(CollectionUtils.isNotEmpty(localeListbox.getItems()))
        {
            final Listitem fallbackLocale = findFallbackLocale();
            if(fallbackLocale != null)
            {
                localeListbox.setSelectedItem(fallbackLocale);
                cockpitLocaleService.setCurrentLocale(Locales.getLocale(Objects.toString(fallbackLocale.getValue())));
            }
        }
    }


    protected Listitem findFallbackLocale()
    {
        final Optional<Listitem> fallbackLocale = localeListbox.getItems().stream()
                        .filter(item -> item.getValue().toString().startsWith(DEFAULT_LOGIN_SCREEN_LOCALE)).findFirst();
        return fallbackLocale.orElse(localeListbox.getItems().stream().findFirst().orElse(null));
    }


    @ViewEvent(eventName = Events.ON_CLIENT_INFO)
    public void publishClientInfo(final ClientInfoEvent event)
    {
        if(event != null)
        {
            final Session session = Sessions.getCurrent();
            session.setAttribute(Attributes.PREFERRED_TIME_ZONE, event.getTimeZone());
        }
    }


    public LoginInformationHandler getLoginInformationHandler()
    {
        return BackofficeSpringUtil.getBean("loginInformationHandler");
    }


    public Listbox getLocaleListbox()
    {
        return localeListbox;
    }


    public void setLocaleListbox(final Listbox localeListbox)
    {
        this.localeListbox = localeListbox;
    }


    public Label getStatus()
    {
        return status;
    }


    public void setStatus(final Label status)
    {
        this.status = status;
    }


    public Label getAccessdenied()
    {
        return accessdenied;
    }


    public void setAccessdenied(final Label accessdenied)
    {
        this.accessdenied = accessdenied;
    }


    public Label getLanguageLabel()
    {
        return languageLabel;
    }


    public void setLanguageLabel(final Label languageLabel)
    {
        this.languageLabel = languageLabel;
    }


    public Div getLoginFormContainer()
    {
        return loginFormContainer;
    }


    public void setLoginFormContainer(final Div loginFormContainer)
    {
        this.loginFormContainer = loginFormContainer;
    }


    public Div getConfiguredFieldPanel()
    {
        return configuredFieldPanel;
    }


    public void setConfiguredFieldPanel(final Div configuredFieldPanel)
    {
        this.configuredFieldPanel = configuredFieldPanel;
    }


    public A getSsoAnchor()
    {
        return ssoAnchor;
    }


    public void setSsoAnchor(final A ssoAnchor)
    {
        this.ssoAnchor = ssoAnchor;
    }


    public TypedSettingsRenderer getLoginInfoRenderer()
    {
        return loginInfoRenderer;
    }


    @Required
    public void setLoginInfoRenderer(final TypedSettingsRenderer loginInfoRenderer)
    {
        this.loginInfoRenderer = loginInfoRenderer;
    }


    public CockpitLocaleService getCockpitLocaleService()
    {
        return cockpitLocaleService;
    }


    public void setCockpitLocaleService(final CockpitLocaleService cockpitLocaleService)
    {
        this.cockpitLocaleService = cockpitLocaleService;
    }


    public Button getLoginButton()
    {
        return loginButton;
    }


    public void setLoginButton(final Button loginButton)
    {
        this.loginButton = loginButton;
    }


    public CockpitProperties getCockpitProperties()
    {
        return cockpitProperties;
    }


    public Textbox getCurrentLanguageLabel()
    {
        return currentLanguageLabel;
    }


    public void setCurrentLanguageLabel(final Textbox languageLabel)
    {
        this.currentLanguageLabel = languageLabel;
    }


    public Popup getLocaleListboxPopup()
    {
        return localeListboxPopup;
    }


    public void setLocaleListboxPopup(Popup localeListboxPopup)
    {
        this.localeListboxPopup = localeListboxPopup;
    }


    public Button getLocaleListboxBtn()
    {
        return localeListboxBtn;
    }


    public void setLocaleListboxBtn(Button localeListboxBtn)
    {
        this.localeListboxBtn = localeListboxBtn;
    }


    @Required
    public void setCockpitProperties(final CockpitProperties cockpitProperties)
    {
        this.cockpitProperties = cockpitProperties;
    }


    public CockpitProperties getGlobalProperties()
    {
        return globalProperties;
    }


    @Required
    public void setGlobalProperties(final CockpitProperties globalProperties)
    {
        this.globalProperties = globalProperties;
    }


    public Collection<String> getAvailableLangPacks()
    {
        return availableLangPacks == null ? Collections.emptyList() : Collections.unmodifiableCollection(availableLangPacks);
    }
}
