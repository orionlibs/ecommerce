/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.branding.customthemes.themes;

import com.hybris.backoffice.model.CustomThemeModel;
import com.hybris.backoffice.model.ThemeModel;
import com.hybris.backoffice.theme.BackofficeThemeService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.Executable;
import com.hybris.cockpitng.core.model.StandardModelKeys;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.editor.localized.LocalizedEditor;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.validation.model.ValidationResult;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;

public class ThemesController extends DefaultWidgetController
{
    private static final Logger LOG = LoggerFactory.getLogger(ThemesController.class);
    protected static final String SOCKET_OUTPUT_THEME_VARIABLE_CHANGED = "themeVariablesChanged";
    protected static final String SOCKET_OUTPUT_CUSTOM_THEME_CHANGED = "customThemeChanged";
    protected static final String CUSTOM_THEMES_TITLE_LABEL = "custom.themes.widget.title.";
    protected static final String CANCEL_BUTTON = "cancelButton";
    protected static final String BACK_BUTTON = "backButton";
    protected static final String CREATE_BUTTON = "createButton";
    private static final String CONFIRM_UNSAVED_MSG = "custom.themes.unsaved.msg";
    private static final String CONFIRM_DELETE_MSG = "custom.themes.delete.msg";
    private static final String CONFIRM_DELETE_TITLE = "custom.themes.delete.title";
    private static final String BASETHEME_MISSING_STYLE_MESSAGE = "custom.themes.basetheme.missing.style.msg";
    private static final String CURRENT_THEME_MISSING_STYLE_MESSAGE = "custom.themes.currenttheme.and.basetheme.missing.style.msg";
    private static final String CURRENT_THEME_NOT_EXIST = "custom.themes.theme.not.exist.and.refresh.msg";
    private static final String CUSTOM_THEMES_MESSAGEBOX_TITLE = "custom.themes.widget.title.display";
    protected static final String NOTIFICATION_AREA = "customThemes";
    protected static final String NOTIFICATION_TYPE_CUSTOM_THEMES_CHANGED = "customThemeChanged";
    protected static final String NOTIFICATION_TYPE_CUSTOM_THEMES_CREATED = "customThemeCreated";
    protected static final String NOTIFICATION_TYPE_CUSTOM_THEMES_DELETED = "customThemeDeleted";
    public static final String MODEL_CURRENT_OBJECT = StandardModelKeys.CONTEXT_OBJECT;
    private static final String EDITED_PROPERTY_QUALIFIER = "isExpandInitial";
    protected static final String EVENT_SAVE_THEME = "onSaveTheme";


    protected enum ViewMode
    {
        CREATE, EDIT, DISPLAY;
    }


    @WireVariable
    private transient BackofficeThemeService backofficeThemeService;
    @WireVariable
    private transient NotificationService notificationService;
    @WireVariable
    private transient MediaService mediaService;
    @WireVariable
    private transient ModelService modelService;
    @WireVariable
    private transient PermissionFacade permissionFacade;
    @WireVariable
    private transient Util customThemeUtil;
    @WireVariable
    private transient ThemeSaveHelper themeSaveHelper;
    @WireVariable
    private transient ThemeValidationHelper themeValidationHelper;
    protected AppearanceItemRenderer appearanceItemRenderer;
    @Wire
    private Div customizeThemeContainer;
    @Wire
    protected Div nameEditorContainer;
    @Wire
    protected Div codeContainer;
    @Wire
    private Combobox baseThemeCombox;
    @Wire
    private Div themeEditView;
    @Wire
    private Div themeDisplayView;
    @Wire
    private Div emptyListView;
    @Wire
    private Listbox themeListBox;
    @Wire
    private Label titleLabel;
    @Wire
    private Button backButton;
    @Wire
    private Button createButton;
    @Wire
    private Button saveButton;
    @Wire
    private Button cancelButton;
    private ListModelList<CustomThemeModel> customThemesModel;
    protected ListModelList<ThemeModel> baseThemesModel;
    protected ViewMode currentViewMode;
    private CustomThemeModel currentEditTheme;
    private ThemeModel currentBaseTheme;
    protected Editor nameEditor;
    protected Editor codeTextbox;
    private Component comp;


    @Override
    public void initialize(final Component comp)
    {
        saveButton.addEventListener(EVENT_SAVE_THEME, this::onSave);
        customThemesModel = new ListModelList<>();
        themeListBox.setModel(customThemesModel);
        baseThemesModel = new ListModelList<>(backofficeThemeService.getBaseThemes());
        baseThemeCombox.setModel(baseThemesModel);
        appearanceItemRenderer = new AppearanceItemRenderer(customizeThemeContainer, this);
        this.comp = comp;
        themeValidationHelper.prepareValidationResultModel(this.getModel());
        switchView(ViewMode.DISPLAY);
    }


    protected void switchView(final ViewMode viewMode)
    {
        final boolean isDisplay = ViewMode.DISPLAY == viewMode;
        currentViewMode = viewMode;
        themeDisplayView.setVisible(isDisplay);
        createButton.setVisible(isDisplay);
        themeEditView.setVisible(!isDisplay);
        backButton.setVisible(!isDisplay);
        saveButton.setVisible(!isDisplay);
        cancelButton.setVisible(!isDisplay);
        titleLabel.setValue(getLabel(CUSTOM_THEMES_TITLE_LABEL + viewMode.name().toLowerCase()));
        baseThemeCombox.setDisabled(ViewMode.EDIT == viewMode);
        initViewData();
    }


    protected void initViewData()
    {
        switch(currentViewMode)
        {
            case DISPLAY -> refreshThemeList();
            case EDIT -> initDataForEditView();
            case CREATE -> initDataForCreateView();
        }
    }


    protected void refreshThemeList()
    {
        customThemesModel.clear();
        customThemesModel.addAll(backofficeThemeService.getCustomThemes());
        emptyListView.setVisible(customThemesModel.isEmpty());
    }


    protected void initDataForCreateView()
    {
        currentEditTheme = modelService.create(CustomThemeModel._TYPECODE);
        currentBaseTheme = backofficeThemeService.getDefaultTheme();
        themeValidationHelper.clearValidationResultModel(getModel());
        clearCodeAndNameEditor();
        setCurrentObject(currentEditTheme);
        createCodeEditor();
        createNameEditor();
        baseThemesModel.clearSelection();
        baseThemesModel.addToSelection(currentBaseTheme);
        enableSave(false);
        if(!appearanceItemRenderer.resetAppearanceItemColor(currentBaseTheme.getStyle()))
        {
            Messagebox.show(getLabel(BASETHEME_MISSING_STYLE_MESSAGE, new String[]
                                            {StringUtils.isEmpty(currentBaseTheme.getName()) ? currentBaseTheme.getCode() : currentBaseTheme.getName()}),
                            getLabel(CUSTOM_THEMES_MESSAGEBOX_TITLE), Messagebox.OK, Messagebox.EXCLAMATION);
            baseThemesModel.clearSelection();
        }
    }


    protected void initDataForEditView()
    {
        if(modelService.isRemoved(currentEditTheme))
        {
            showMessagebox(CURRENT_THEME_NOT_EXIST, null, null, () -> switchView(ViewMode.DISPLAY));
            return;
        }
        modelService.refresh(currentEditTheme);
        themeValidationHelper.clearValidationResultModel(getModel());
        clearCodeAndNameEditor();
        setCurrentObject(currentEditTheme);
        createCodeEditor();
        createNameEditor();
        currentBaseTheme = currentEditTheme.getBase();
        baseThemesModel.clearSelection();
        baseThemesModel.addToSelection(currentBaseTheme);
        enableSave(false);
        if(!appearanceItemRenderer.resetAppearanceItemColor(currentEditTheme.getStyle())
                        && !appearanceItemRenderer.resetAppearanceItemColor(currentBaseTheme.getStyle()))
        {
            showMessagebox(CURRENT_THEME_MISSING_STYLE_MESSAGE, null, null, () -> switchView(ViewMode.DISPLAY));
        }
    }


    protected void enableSave(final boolean enabled)
    {
        saveButton.setDisabled(!enabled);
        cancelButton.setDisabled(!enabled);
    }


    public void onGeneralDataChange()
    {
        enableSave(true);
    }


    protected void onColorChange(final Map<String, String> map)
    {
        sendOutput(SOCKET_OUTPUT_THEME_VARIABLE_CHANGED, map);
    }


    public void onBaseThemeSelect()
    {
        if(appearanceItemRenderer.isColorChanged())
        {
            showMessagebox("custom.themes.switch.basetheme.unsaved.msg", null, null, this::baseThemeChange, () -> {
                baseThemesModel.clearSelection();
                baseThemesModel.addToSelection(currentBaseTheme);
            });
            return;
        }
        baseThemeChange();
    }


    protected void baseThemeChange()
    {
        final ThemeModel theme = Objects.nonNull(baseThemeCombox.getSelectedItem()) ? baseThemeCombox.getSelectedItem().getValue() : null;
        if(Objects.nonNull(theme) && !appearanceItemRenderer.resetAppearanceItemColor(theme.getStyle()))
        {
            Messagebox.show(getLabel(BASETHEME_MISSING_STYLE_MESSAGE, new String[]
                                            {StringUtils.isEmpty(theme.getName()) ? theme.getCode() : theme.getName()}), getLabel(CUSTOM_THEMES_MESSAGEBOX_TITLE),
                            Messagebox.OK, Messagebox.EXCLAMATION);
            baseThemesModel.clearSelection();
            return;
        }
        enableSave(true);
        currentBaseTheme = theme;
    }


    @ViewEvent(eventName = Events.ON_CLICK, componentID = CREATE_BUTTON)
    public void onThemeCreate()
    {
        if(backofficeThemeService.getCustomThemes().size() >= backofficeThemeService.getMaximumOfCustomTheme())
        {
            showMessagebox("custom.themes.exceed.maximum", null, new Messagebox.Button[]
                            {Messagebox.Button.OK}, this::refreshThemeList);
            return;
        }
        switchView(ViewMode.CREATE);
    }


    @ViewEvent(eventName = Events.ON_CLICK, componentID = BACK_BUTTON)
    public void onBack()
    {
        if(saveButton.isDisabled())
        {
            switchView(ViewMode.DISPLAY);
            return;
        }
        showMessagebox(CONFIRM_UNSAVED_MSG, null, null, () -> switchView(ViewMode.DISPLAY));
    }


    @ViewEvent(eventName = Events.ON_CLICK, componentID = CANCEL_BUTTON)
    public void onCancel()
    {
        showMessagebox(CONFIRM_UNSAVED_MSG, null, null, this::initViewData);
    }


    public void onThemeEdit(final CustomThemeModel themeModel)
    {
        if(modelService.isRemoved(themeModel))
        {
            showMessagebox(CURRENT_THEME_NOT_EXIST, null, null, this::refreshThemeList);
            return;
        }
        if(!customThemeUtil.isValidThemeStyle(themeModel.getStyle())
                        && !customThemeUtil.isValidThemeStyle(themeModel.getBase().getStyle()))
        {
            Messagebox.show(getLabel(CURRENT_THEME_MISSING_STYLE_MESSAGE), getLabel(CUSTOM_THEMES_MESSAGEBOX_TITLE), Messagebox.OK,
                            Messagebox.EXCLAMATION);
            return;
        }
        currentEditTheme = themeModel;
        switchView(ViewMode.EDIT);
    }


    public void onThemeDelete(final ThemeModel themeModel)
    {
        showMessagebox(CONFIRM_DELETE_MSG, CONFIRM_DELETE_TITLE, null, () -> deleteTheme(themeModel));
    }


    protected void deleteTheme(final ThemeModel themeModel)
    {
        try
        {
            if(!modelService.isRemoved(themeModel))
            {
                modelService.remove(themeModel);
            }
            sendOutput(SOCKET_OUTPUT_CUSTOM_THEME_CHANGED, (Object)null);
            notificationService.notifyUser(NOTIFICATION_AREA, NOTIFICATION_TYPE_CUSTOM_THEMES_DELETED,
                            NotificationEvent.Level.SUCCESS);
            refreshThemeList();
        }
        catch(final Exception ex)
        {
            LOG.error(String.format("Could not delete custom theme: %s", themeModel.getCode()), ex);
            notificationService.notifyUser(NOTIFICATION_AREA, NOTIFICATION_TYPE_CUSTOM_THEMES_DELETED,
                            NotificationEvent.Level.FAILURE, ex);
        }
    }


    protected void showMessagebox(final String message, final String title, final Messagebox.Button[] buttons,
                    final Executable onConfirm)
    {
        showMessagebox(message, title, buttons, onConfirm, () -> {
        });
    }


    protected void showMessagebox(final String message, final String title, final Messagebox.Button[] buttons,
                    final Executable onConfirm, final Executable onCancel)
    {
        final var btns = buttons == null || buttons.length == 0 ? new Messagebox.Button[]
                        {Messagebox.Button.YES, Messagebox.Button.CANCEL} : buttons;
        final var titleKey = StringUtils.isEmpty(title) ? CUSTOM_THEMES_MESSAGEBOX_TITLE : title;
        Messagebox.show(getLabel(message), getLabel(titleKey), btns, Messagebox.QUESTION, event -> {
            if(btns[0] == event.getButton())
            {
                onConfirm.execute();
            }
            else
            {
                onCancel.execute();
            }
        });
    }


    public void onSave(final Event event)
    {
        final var isCreate = ViewMode.CREATE == currentViewMode;
        final var code = (String)codeTextbox.getValue();
        if(StringUtils.isEmpty(code) || appearanceItemRenderer.getStyleVariableMap().isEmpty())
        {
            notificationService.notifyUser(NOTIFICATION_AREA, isCreate ? "customThemeCreateInValid" : "customThemeEditInValid",
                            NotificationEvent.Level.FAILURE);
            Clients.clearBusy();
            return;
        }
        final var name = (HashMap<Locale, String>)nameEditor.getValue();
        final var styleInputStream = customThemeUtil.mapToStyleInputStream(appearanceItemRenderer.getStyleVariableMap());
        final String previewImgData = (String)event.getData();
        final var isSuccess = isCreate ? createTheme(code, name, styleInputStream, previewImgData)
                        : updateTheme(name, styleInputStream, previewImgData);
        if(isSuccess)
        {
            sendOutput(SOCKET_OUTPUT_CUSTOM_THEME_CHANGED, (Object)null);
            notificationService.notifyUser(NOTIFICATION_AREA,
                            isCreate ? NOTIFICATION_TYPE_CUSTOM_THEMES_CREATED : NOTIFICATION_TYPE_CUSTOM_THEMES_CHANGED,
                            NotificationEvent.Level.SUCCESS);
            switchView(ViewMode.DISPLAY);
        }
        Clients.clearBusy();
    }


    protected boolean createTheme(final String code, final HashMap<Locale, String> name, final InputStream styleInputStream,
                    final String previewImgData)
    {
        try
        {
            Transaction.current().execute(new TransactionBody()
            {
                @Override
                public <T> T execute() throws Exception
                {
                    themeSaveHelper.setTheme(null).init(theme -> {
                        theme.setCode(code);
                        theme.setBase(currentBaseTheme);
                        setThemeName(theme, name);
                    }).initStyle(style -> mediaService.setStreamForMedia(style, styleInputStream)).initThumbnail(thumbnail -> {
                        if(StringUtils.isNotEmpty(previewImgData))
                        {
                            mediaService.setDataForMedia(thumbnail, customThemeUtil.getPreviewImageData(previewImgData));
                        }
                    }).save();
                    return null;
                }
            });
        }
        catch(final Exception ex)
        {
            LOG.error("Create custom theme failed", ex);
            notificationService.notifyUser(NOTIFICATION_AREA, NOTIFICATION_TYPE_CUSTOM_THEMES_CREATED,
                            NotificationEvent.Level.FAILURE, ex);
            return false;
        }
        return true;
    }


    protected boolean updateTheme(final HashMap<Locale, String> name, final InputStream styleInputStream,
                    final String previewImgData)
    {
        try
        {
            if(modelService.isRemoved(currentEditTheme))
            {
                showMessagebox(CURRENT_THEME_NOT_EXIST, null, null, () -> switchView(ViewMode.DISPLAY));
                return false;
            }
            modelService.refresh(currentEditTheme);
            Transaction.current().execute(new TransactionBody()
            {
                @Override
                public <T> T execute() throws Exception
                {
                    themeSaveHelper.setTheme(currentEditTheme).init(theme -> setThemeName(theme, name)).initStyle(style -> {
                        if(appearanceItemRenderer.isColorChanged())
                        {
                            mediaService.setStreamForMedia(style, styleInputStream);
                        }
                    }).initThumbnail(thumbnail -> {
                        if(appearanceItemRenderer.isColorChanged() && StringUtils.isNotEmpty(previewImgData))
                        {
                            mediaService.setDataForMedia(thumbnail, customThemeUtil.getPreviewImageData(previewImgData));
                        }
                    }).save();
                    return null;
                }
            });
        }
        catch(final Exception ex)
        {
            LOG.error(String.format("Change custom theme %s failed", name), ex);
            notificationService.notifyUser(NOTIFICATION_AREA, NOTIFICATION_TYPE_CUSTOM_THEMES_CHANGED,
                            NotificationEvent.Level.FAILURE, ex);
            return false;
        }
        return true;
    }


    private void setThemeName(final CustomThemeModel theme, final HashMap<Locale, String> nameMap)
    {
        nameMap.forEach((locale, name) -> theme.setName(name, locale));
    }


    protected void createNameEditor()
    {
        setValue(buildKeyForExpandedProperty(EDITED_PROPERTY_QUALIFIER), false);
        final Set<Locale> writableLocales = permissionFacade.getAllWritableLocalesForCurrentUser();
        final Set<Locale> readableLocales = permissionFacade.getAllReadableLocalesForCurrentUser();
        nameEditor = new Editor();
        nameEditor.setId("nameEditor");
        nameEditor.setType("Localized(java.lang.String)");
        nameEditor.setAtomic(true);
        nameEditor.setEditorLabel(getLabel("custom.themes.field.label.name"));
        nameEditor.setWritableLocales(writableLocales);
        nameEditor.setReadableLocales(readableLocales);
        nameEditor.setWidgetInstanceManager(getWidgetInstanceManager());
        nameEditor.addParameter("editedPropertyQualifier", EDITED_PROPERTY_QUALIFIER);
        nameEditor.setProperty(String.format("%s.%s", MODEL_CURRENT_OBJECT, "name"));
        nameEditor.afterCompose();
        nameEditor.addEventListener("onValueChanged", event -> onGeneralDataChange());
        nameEditorContainer.appendChild(nameEditor);
    }


    protected void createCodeEditor()
    {
        codeTextbox = new Editor();
        codeTextbox.setId("codeTextbox");
        codeTextbox.setType("java.lang.String");
        codeTextbox.setReadOnly(ViewMode.EDIT == currentViewMode);
        codeTextbox.setWidgetInstanceManager(getWidgetInstanceManager());
        codeTextbox.setProperty(String.format("%s.%s", MODEL_CURRENT_OBJECT, "code"));
        codeTextbox.afterCompose();
        codeTextbox.addEventListener("onValueChanged", event -> onGeneralDataChange());
        codeContainer.appendChild(codeTextbox);
        themeValidationHelper.initValidation(comp, this, codeTextbox);
    }


    protected void clearCodeAndNameEditor()
    {
        if(codeTextbox != null)
        {
            codeTextbox.destroy();
            codeContainer.removeChild(codeTextbox);
        }
        if(nameEditor != null)
        {
            nameEditor.destroy();
            nameEditorContainer.removeChild(nameEditor);
        }
    }


    public ValidationResult getCurrentValidationResult()
    {
        return getValue(StandardModelKeys.VALIDATION_RESULT_KEY, ValidationResult.class);
    }


    private String buildKeyForExpandedProperty(final String editedPropertyQualifier)
    {
        return String.format("%s.expanded(%s)", LocalizedEditor.class.getSimpleName(), editedPropertyQualifier);
    }


    public Util getCustomThemeUtil()
    {
        return customThemeUtil;
    }


    public Object getCurrentObject()
    {
        return this.getValue(MODEL_CURRENT_OBJECT, Object.class);
    }


    public void setCurrentObject(final Object value)
    {
        this.setValue(MODEL_CURRENT_OBJECT, value);
    }
}
