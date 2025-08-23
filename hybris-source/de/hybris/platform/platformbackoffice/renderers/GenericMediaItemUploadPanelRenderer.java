package de.hybris.platform.platformbackoffice.renderers;

import com.google.common.collect.Maps;
import com.hybris.cockpitng.common.configuration.MenupopupPosition;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractPanel;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.CustomPanel;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Parameter;
import com.hybris.cockpitng.core.model.ModelObserver;
import com.hybris.cockpitng.core.model.ValueObserver;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacadeStrategy;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.editors.EditorUtils;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.services.media.MimeTypeChecker;
import com.hybris.cockpitng.services.media.ObjectPreview;
import com.hybris.cockpitng.services.media.ObjectPreviewService;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.widgets.editorarea.renderer.EditorAreaRendererUtils;
import com.hybris.cockpitng.widgets.editorarea.renderer.impl.AbstractEditorAreaPanelRenderer;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.media.storage.MediaStorageConfigService;
import de.hybris.platform.platformbackoffice.renderers.util.DateFormatter;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.impl.Utils;

public class GenericMediaItemUploadPanelRenderer extends AbstractEditorAreaPanelRenderer<Object>
{
    @Deprecated(since = "1905")
    public static final String PROPERTY_SRC = "src";
    protected static final String LABEL_KEY_PK = "media.info.pk";
    protected static final String LABEL_KEY_TIME_CREATED = "media.info.timecreated";
    protected static final String LABEL_KEY_TIME_MODIFIED = "media.info.timemodified";
    protected static final String IMAGE = "image";
    protected static final long DEFAULT_MAX_FILE_SIZE_KB = 10000L;
    protected static final String MEDIA_TYPE_CODE = "Media";
    protected static final String MAX_SIZE = "maxsize=";
    protected static final String FILE_UPLOAD_MAX_SIZE = "fileUpload.maxSize";
    protected static final String IMG_STYLE = "previewPlaceholder";
    protected static final String MEDIA_INFO_HEADER = "mediaInfoHeader";
    protected static final String MEDIA_INFO_VALUE = "mediaInfoValue";
    protected static final String MEDIA_PREVIEW = "mediaPreview";
    protected static final String MEDIA_PREVIEW_CNT = "mediaPreviewCnt";
    protected static final String MEDIA_INFO_CNT = "mediaInfoCnt";
    protected static final String FILEUPLOAD_SCLASS = "media-fileupload y-btn-primary";
    protected static final String CLEAR_CONTENT_BTN_SCLASS = "media-clear-content-btn";
    protected static final String Y_WARNING_BTN_SCLASS = "y-btn-danger";
    protected static final String DOWNLOAD_BTN_SCLASS = "media-download-btn y-btn-secondary";
    protected static final String ON_UPLOAD_LATER = "onUploadLater";
    protected static final String SMALL_IMAGE_PREVIEW_CNT = "small small-image-preview-cnt";
    protected static final String DATA_MODEL_PROPERTY = "dataModelProperty";
    protected static final String DATA_MODEL_ATTRIBUTE = "dataModelAttribute";
    protected static final String SHOW_CONTENT_BUTTONS = "showContentButtons";
    protected static final String VIEW_MODE_ACTIVE = "active";
    protected static final String VIEW_MODE_INACTIVE = "inactive";
    protected static final String MEDIA_INFO_NOT_AVAILABLE = "media.info.not.available";
    protected static final String PARAM_ACCEPT = "accept";
    private static final Logger LOG = LoggerFactory.getLogger(GenericMediaItemUploadPanelRenderer.class);
    protected String MODEL_MEDIA_SAVED;
    protected String MODEL_MEDIA_CANCELED;
    protected String MODEL_RENDER_PARAMETER_MAP;
    protected String MODEL_ZK_MEDIA_MODEL_KEY;
    protected String MODEL_ZK_MEDIA_CLEARED;
    protected String MODEL_DISPLAY_FROM_MODEL;
    protected String currentlyDisplayedMode;
    protected String currentlyDisplayedMediaInfo;
    protected String initiallyDisplayedMediaModelInfo;
    private ModelService modelService;
    private MediaService mediaService;
    private MimeTypeChecker mimeTypeChecker;
    private MediaStorageConfigService mediaStorageConfigService;
    private CockpitProperties cockpitProperties;
    private PermissionFacadeStrategy permissionFacadeStrategy;
    private WidgetInstanceManager widgetInstanceManager;
    private ObjectPreviewService objectPreviewService;
    private Div tmpImageDiv;
    private Div imgDiv;
    private VersionAwareImage tmpImage;
    private Button downloadButton;
    private Button deleteButton;
    private Fileupload fileupload;
    private Label labelPkValue;
    private Label labelTimeCreatedValue;
    private Label labelTimeModifiedValue;
    private DateFormatter dateFormatter;
    private String format;


    public void render(Component parent, AbstractPanel panel, Object data, DataType type, WidgetInstanceManager widgetInstanceManager)
    {
        this.widgetInstanceManager = widgetInstanceManager;
        initPreRenderModelConstants(getEntityId(data, panel));
        initializeRenderParametersMap(panel);
        Div rendererPanel = new Div();
        parent.appendChild((Component)rendererPanel);
        String accept = (String)((Map)widgetInstanceManager.getModel().getValue(this.MODEL_RENDER_PARAMETER_MAP, Map.class)).get("accept");
        rendererPanel.setClientAttribute("accept", accept);
        adjustView((Component)rendererPanel, type);
        addModelObservers((Component)rendererPanel, data, type);
    }


    protected void adjustView(Component rendererPanel, DataType type)
    {
        if(isMediaModelAvailable())
        {
            if("active".equals(this.currentlyDisplayedMode))
            {
                refreshActiveViewForMediaModelOrZkMedia(type);
            }
            else
            {
                removeAfterSaveAndCancelListeners();
                Components.removeAllChildren(rendererPanel);
                initializeActiveView(rendererPanel, type);
            }
        }
        else if(!"inactive".equals(this.currentlyDisplayedMode))
        {
            removeAfterSaveAndCancelListeners();
            Components.removeAllChildren(rendererPanel);
            initializeInactiveView(rendererPanel, type);
        }
    }


    private void refreshActiveViewForMediaModelOrZkMedia(DataType type)
    {
        if(isDisplayContentFromMediaModel())
        {
            if(ObjectUtils.notEqual(this.currentlyDisplayedMediaInfo, getMediaModelInfo(getMediaModel())))
            {
                refreshActiveView(type);
            }
        }
        else if(ObjectUtils.notEqual(this.currentlyDisplayedMediaInfo, getZKMediaInfo(getZkMedia())))
        {
            refreshActiveView(type);
        }
    }


    protected void initializeInactiveView(Component rendererPanel, DataType type)
    {
        String qualifier = getModelAttributeToMedia();
        DataAttribute attribute = (qualifier == null) ? null : type.getAttribute(qualifier);
        if(attribute != null)
        {
            Editor editor = new Editor();
            editor.setProperty(getPathToMediaObjectInModel());
            editor.setWidgetInstanceManager(getWidgetInstanceManager());
            editor.setType(EditorUtils.getEditorType(attribute, true));
            editor.setReadableLocales(getPermissionFacade().getAllReadableLocalesForCurrentUser());
            editor.setWritableLocales(getPermissionFacade().getAllReadableLocalesForCurrentUser());
            editor.setOptional(!attribute.isMandatory());
            editor.setReadOnly((!attribute.isWritable() || !canEdit(type)));
            editor.afterCompose();
            rendererPanel.appendChild((Component)getAttributeLabel(attribute));
            rendererPanel.appendChild((Component)editor);
            this.currentlyDisplayedMode = "inactive";
        }
        refreshDisplayedMediaInfo();
    }


    protected void initializeActiveView(Component rendererPanel, DataType type)
    {
        if(isMediaNestedInRootObject())
        {
            String qualifier = getModelAttributeToMedia();
            DataAttribute attribute = (qualifier == null) ? null : type.getAttribute(qualifier);
            if(attribute != null)
            {
                rendererPanel.appendChild((Component)getAttributeLabel(attribute));
            }
        }
        if(!canRead(type))
        {
            rendererPanel.appendChild((Component)new Label(Labels.getLabel("upload.media.no.access")));
            return;
        }
        Div mediaPreviewRow = initMediaPreviewRow(rendererPanel);
        Div previewPlaceholder = initPreviewPlaceholder();
        Vlayout mediaInfoPanel = initMediaInfoPanel();
        if(areButtonsVisible())
        {
            initFileUpload((Component)mediaInfoPanel, type);
            initDownloadButton((Component)mediaInfoPanel);
            initDeleteButton((Component)mediaInfoPanel, type);
        }
        initImgDiv((Component)previewPlaceholder);
        initTmpImg((Component)previewPlaceholder);
        if(!isZKMediaCleared() && getZkMedia() == null)
        {
            if(isDisplayable(getMediaModel()))
            {
                createPreviewForDisplayableMedia();
            }
            else
            {
                createPreviewForNonDisplayableMedia();
            }
        }
        mediaPreviewRow.appendChild((Component)mediaInfoPanel);
        mediaPreviewRow.appendChild((Component)previewPlaceholder);
        setAfterSaveListener(type);
        setAfterCancelListener();
        updateButtonsState(type);
        this.currentlyDisplayedMode = "active";
        refreshDisplayedMediaInfo();
    }


    protected Label getAttributeLabel(DataAttribute attribute)
    {
        Label label = new Label(attribute.getLabel(getCockpitLocaleService().getCurrentLocale()));
        label.setTooltiptext(getModelAttributeToMedia());
        return label;
    }


    protected void refreshActiveView(DataType type)
    {
        if(!"active".equals(this.currentlyDisplayedMode))
        {
            return;
        }
        initMediaInfoLabels();
        clearPreviewImage();
        if(isMediaModelAvailable())
        {
            if(isDisplayContentFromMediaModel())
            {
                if(isDisplayable(getMediaModel()))
                {
                    createPreviewForDisplayableMedia();
                }
                else
                {
                    createPreviewForNonDisplayableMedia();
                }
            }
            else if(getZkMedia() != null && !isZKMediaCleared())
            {
                renderPreviewWithZKMedia(getZkMedia());
            }
            updateButtonsState(type);
        }
        refreshDisplayedMediaInfo();
    }


    protected void cleanupModel()
    {
        WidgetModel widgetModel = getWidgetInstanceManager().getModel();
        widgetModel.remove(this.MODEL_MEDIA_SAVED);
        widgetModel.remove(this.MODEL_MEDIA_CANCELED);
        widgetModel.remove(this.MODEL_RENDER_PARAMETER_MAP);
        widgetModel.remove(this.MODEL_ZK_MEDIA_MODEL_KEY);
        widgetModel.remove(this.MODEL_ZK_MEDIA_CLEARED);
        widgetModel.remove(this.MODEL_DISPLAY_FROM_MODEL);
    }


    protected void initPreRenderModelConstants(String suffix)
    {
        this.MODEL_MEDIA_SAVED = "media_saved_" + suffix;
        this.MODEL_MEDIA_CANCELED = "media_canceled_" + suffix;
        this.MODEL_RENDER_PARAMETER_MAP = "render_parameter_map_" + suffix;
        this.MODEL_ZK_MEDIA_MODEL_KEY = "zkMedia_tmp_" + suffix;
        this.MODEL_ZK_MEDIA_CLEARED = "zkMediaCleared_" + suffix;
        this.MODEL_DISPLAY_FROM_MODEL = "displayFromModel" + suffix;
    }


    protected void removeAfterSaveAndCancelListeners()
    {
        EditorAreaRendererUtils.removeAfterSaveListener(getWidgetInstanceManager().getModel(), this.MODEL_MEDIA_SAVED);
        EditorAreaRendererUtils.removeAfterCancelListener(getWidgetInstanceManager().getModel(), this.MODEL_MEDIA_CANCELED);
    }


    protected String getEntityId(Object data, AbstractPanel panel)
    {
        StringBuilder entityId = new StringBuilder();
        if(data instanceof ItemModel && !getObjectFacade().isNew(data))
        {
            entityId.append(((ItemModel)data).getPk().getHex());
        }
        if(panel instanceof CustomPanel)
        {
            Map<String, String> renderParameters = (Map<String, String>)((CustomPanel)panel).getRenderParameter().stream().collect(Collectors.toMap(Parameter::getName, Parameter::getValue));
            String modelProperty = renderParameters.get("dataModelProperty");
            String modelAttribute = renderParameters.get("dataModelAttribute");
            entityId.append("_");
            entityId.append(
                            StringUtils.isNotBlank(modelProperty) ? modelProperty.trim() : "currentObject");
            if(StringUtils.isNotBlank(modelAttribute))
            {
                entityId.append(".");
                entityId.append(modelAttribute);
            }
        }
        else
        {
            entityId.append("currentObject");
        }
        return entityId.toString();
    }


    protected boolean areButtonsVisible()
    {
        return BooleanUtils.isNotFalse(BooleanUtils.toBooleanObject(getRendererParameterValue("showContentButtons")));
    }


    private String getRendererParameterValue(String parameterName)
    {
        Map<String, String> params = (Map<String, String>)getWidgetInstanceManager().getModel().getValue(this.MODEL_RENDER_PARAMETER_MAP, Map.class);
        if(params != null && params.containsKey(parameterName))
        {
            String paramValue = params.get(parameterName);
            return StringUtils.isNotBlank(paramValue) ? paramValue.trim() : null;
        }
        return null;
    }


    private void initializeRenderParametersMap(AbstractPanel panel)
    {
        Map<String, String> renderParameters;
        if(panel instanceof CustomPanel)
        {
            renderParameters = (Map<String, String>)((CustomPanel)panel).getRenderParameter().stream().collect(Collectors.toMap(Parameter::getName, Parameter::getValue));
        }
        else
        {
            renderParameters = Maps.newHashMap();
        }
        getWidgetInstanceManager().getModel().put(this.MODEL_RENDER_PARAMETER_MAP, renderParameters);
    }


    private boolean isMediaModelAvailable()
    {
        return (getMediaModel() != null);
    }


    protected MediaModel getMediaModel()
    {
        Object value = getWidgetInstanceManager().getModel().getValue(getPathToRootObjectInModel(), Object.class);
        String qualifier = getModelAttributeToMedia();
        if(qualifier != null)
        {
            Object result = getPropertyValueService().readValue(value, qualifier);
            try
            {
                return (MediaModel)result;
            }
            catch(ClassCastException cce)
            {
                LOG.error(String.format("Wrong configuration; attribute %s is not of Media type", new Object[] {qualifier}), cce);
                return null;
            }
        }
        return (MediaModel)value;
    }


    private boolean isMediaNestedInRootObject()
    {
        return (getModelAttributeToMedia() != null);
    }


    private String getModelAttributeToMedia()
    {
        return getRendererParameterValue("dataModelAttribute");
    }


    private String getPathToRootObjectInModel()
    {
        String modelProperty = getRendererParameterValue("dataModelProperty");
        return (modelProperty != null) ? modelProperty : "currentObject";
    }


    private String getPathToMediaObjectInModel()
    {
        String rootObject = getPathToRootObjectInModel();
        String attribute = getModelAttributeToMedia();
        StringBuilder path = new StringBuilder();
        path.append(rootObject);
        if(attribute != null)
        {
            path.append(".");
            path.append(attribute);
        }
        return path.toString();
    }


    protected void refreshModelAndViewAfterSaveListeners(DataType type)
    {
        refreshWidgetModelAfterSaveListeners();
        refreshActiveView(type);
    }


    protected void refreshWidgetModelAfterSaveListeners()
    {
        try
        {
            setDisplayContentFromMediaModel(true);
            getWidgetInstanceManager().getModel().put(this.MODEL_ZK_MEDIA_MODEL_KEY, null);
            getWidgetInstanceManager().getModel().put(this.MODEL_ZK_MEDIA_CLEARED, Boolean.FALSE);
            MediaModel freshMediaModel = (MediaModel)getObjectFacade().reload(getMediaModel());
            getWidgetInstanceManager().getModel().setValue(getPathToMediaObjectInModel(), freshMediaModel);
        }
        catch(ObjectNotFoundException notFoundException)
        {
            LOG.error("Media not found on reload", (Throwable)notFoundException);
        }
    }


    private void createPreviewForNonDisplayableMedia()
    {
        Image img = new Image();
        MediaModel mediaModel = getMediaModel();
        if(mediaModel != null)
        {
            ObjectPreview preview = this.objectPreviewService.getPreview((this.mediaService.hasData(mediaModel) && StringUtils.isNotBlank(mediaModel.getMime())) ? mediaModel.getMime() :
                            "");
            if(!preview.isFallback())
            {
                img.setSrc(preview.getUrl());
                this.imgDiv.appendChild((Component)img);
            }
        }
    }


    private void initImgDiv(Component parent)
    {
        this.imgDiv = new Div();
        this.imgDiv.setSclass("previewPlaceholder");
        this.imgDiv.setVisible(true);
        parent.appendChild((Component)this.imgDiv);
    }


    private Div initPreviewPlaceholder()
    {
        Div previewPlaceholder = new Div();
        previewPlaceholder.setSclass("mediaPreviewCnt");
        return previewPlaceholder;
    }


    private Div initMediaPreviewRow(Component parent)
    {
        Div mediaPreviewRow = new Div();
        mediaPreviewRow.setSclass("mediaPreview");
        parent.appendChild((Component)mediaPreviewRow);
        return mediaPreviewRow;
    }


    private void initTmpImg(Component parent)
    {
        this.tmpImage = new VersionAwareImage();
        this.tmpImageDiv = new Div();
        this.tmpImageDiv.appendChild((Component)this.tmpImage);
        this.tmpImageDiv.setSclass("previewPlaceholder");
        this.tmpImageDiv.setVisible(false);
        parent.appendChild((Component)this.tmpImageDiv);
        if(getZkMedia() != null)
        {
            renderPreviewWithZKMedia(getZkMedia());
        }
    }


    private boolean canRead(DataType type)
    {
        boolean canReadProperty = true;
        if(isMediaNestedInRootObject())
        {
            canReadProperty = this.permissionFacadeStrategy.canReadProperty(type.getCode(), getModelAttributeToMedia());
        }
        MediaModel mediaModel = getMediaModel();
        if(canReadProperty && mediaModel != null)
        {
            return (this.permissionFacadeStrategy.canReadType(type.getCode()) && this.permissionFacadeStrategy.canReadInstance(getMediaModel()));
        }
        return canReadProperty;
    }


    private boolean canEdit(DataType type)
    {
        boolean canChangeProperty = true;
        if(isMediaNestedInRootObject())
        {
            canChangeProperty = this.permissionFacadeStrategy.canChangeProperty(type.getCode(), getModelAttributeToMedia());
        }
        MediaModel mediaModel = getMediaModel();
        if(canChangeProperty && mediaModel != null)
        {
            boolean canChangeMimeProperty = (this.permissionFacadeStrategy.canChangeProperty("Media", "mime") && this.permissionFacadeStrategy.canChangeInstanceProperty(mediaModel, "mime"));
            boolean canChangeRealFileNameProperty = (this.permissionFacadeStrategy.canChangeProperty("Media", "realFileName") && this.permissionFacadeStrategy.canChangeInstanceProperty(mediaModel, "realFileName"));
            return (canChangeMimeProperty && canChangeRealFileNameProperty && canRead(type) && canChangeMedia(mediaModel));
        }
        return canChangeProperty;
    }


    private boolean canChangeMedia(MediaModel mediaModel)
    {
        return (this.permissionFacadeStrategy.canChangeType("Media") && this.permissionFacadeStrategy.canChangeInstance(mediaModel));
    }


    void initFileUpload(Component parent, DataType type)
    {
        boolean canChangeInstance = getPermissionFacade().canChangeInstance(getMediaModel());
        this.fileupload = new Fileupload(Labels.getLabel("upload.media.upload"));
        setLimitOnMaxFileSize(this.fileupload);
        parent.appendChild((Component)this.fileupload);
        boolean disabled = (!canChangeInstance || !canEdit(type));
        this.fileupload.setDisabled(disabled);
        this.fileupload.setSclass("media-fileupload y-btn-primary");
        if(canChangeInstance)
        {
            this.fileupload.addEventListener("onUploadLater", event -> {
                String url = getDynamicUrlForMedia(this.tmpImage);
                this.imgDiv.setVisible(false);
                this.tmpImageDiv.setVisible(true);
                this.tmpImage.setVisible(false);
                clearTmpImageDivButTmpImage();
                this.tmpImageDiv.appendChild((Component)createFakeImageZoomCnt(UITools.adjustURL(url)));
            });
            this.fileupload.addEventListener("onUpload", event -> {
                Media zkMedia = event.getMedia();
                Component panel = parent.getParent().getParent();
                if(this.mimeTypeChecker.isMediaAcceptable(zkMedia, panel.getClientAttribute("accept")))
                {
                    setDisplayContentFromMediaModel(false);
                    getWidgetInstanceManager().getModel().put(this.MODEL_ZK_MEDIA_CLEARED, Boolean.FALSE);
                    getWidgetInstanceManager().getModel().put(this.MODEL_ZK_MEDIA_MODEL_KEY, zkMedia);
                    getWidgetInstanceManager().getModel().changed();
                }
                else
                {
                    showUnsupportedMediaMessage((Component)this.fileupload, zkMedia);
                }
            });
        }
    }


    protected void showUnsupportedMediaMessage(Component parent, Media media)
    {
        String fileName = (media != null) ? StringEscapeUtils.escapeHtml(media.getName()) : "";
        String label = Labels.getLabel("editor.unsupported.filetype", (Object[])new String[] {fileName});
        showUnsupportedMediaMessage(parent, label);
    }


    private void showUnsupportedMediaMessage(Component parent, String label)
    {
        int durationOfNotification = 5000;
        Clients.showNotification(label, "warning", parent, MenupopupPosition.BEFORE_END.getName(), 5000);
    }


    private void renderPreviewWithZKMedia(Media zkMedia)
    {
        if(zkMedia instanceof Image)
        {
            this.tmpImage.setContent((Image)zkMedia);
            Events.echoEvent("onUploadLater", (Component)this.fileupload, null);
        }
        else
        {
            ObjectPreview imgv = this.objectPreviewService.getPreview(zkMedia.getContentType());
            this.tmpImage.setSrc(imgv.getUrl());
            this.tmpImage.setVisible(true);
            clearTmpImageDivButTmpImage();
        }
        if(areButtonsVisible())
        {
            this.downloadButton.setDisabled(isMediaEmpty(getMediaModel(), zkMedia));
            this.deleteButton.setDisabled(false);
        }
        this.tmpImageDiv.setVisible(true);
        this.imgDiv.setVisible(false);
        refreshDisplayedMediaInfo();
    }


    private void setAfterCancelListener()
    {
        EditorAreaRendererUtils.setAfterCancelListener(getWidgetInstanceManager().getModel(), this.MODEL_MEDIA_CANCELED, event -> {
            setDisplayContentFromMediaModel(true);
            getWidgetInstanceManager().getModel().put(this.MODEL_ZK_MEDIA_CLEARED, Boolean.FALSE);
            getWidgetInstanceManager().getModel().put(this.MODEL_ZK_MEDIA_MODEL_KEY, null);
        } false);
    }


    private void setAfterSaveListener(DataType type)
    {
        EditorAreaRendererUtils.setAfterSaveListener(getWidgetInstanceManager().getModel(), this.MODEL_MEDIA_SAVED, event -> handleModelMediaSaved(type), false);
    }


    private void handleModelMediaSaved(DataType type)
    {
        Media zkMedia = getZkMedia();
        if(zkMedia != null && !getModelService().isNew(getMediaModel()))
        {
            updateMediaMetadata(zkMedia);
            if(zkMedia.isBinary())
            {
                getMediaService().setStreamForMedia(getMediaModel(), zkMedia.getStreamData());
            }
            else
            {
                getMediaService().setDataForMedia(getMediaModel(),
                                (zkMedia.getStringData() != null) ? zkMedia.getStringData().getBytes() : null);
            }
            refreshModelAndViewAfterSaveListeners(type);
        }
        else if(zkMedia == null && isZKMediaCleared())
        {
            this.mediaService.removeDataFromMedia(getMediaModel());
            refreshModelAndViewAfterSaveListeners(type);
        }
    }


    private void updateMediaMetadata(Media zkMedia)
    {
        MediaModel mediaModel = getMediaModel();
        if(mediaModel != null)
        {
            mediaModel.setMime(zkMedia.getContentType());
            mediaModel.setRealFileName(zkMedia.getName());
        }
    }


    private boolean isZKMediaCleared()
    {
        Boolean zkMediaCleared = (Boolean)getWidgetInstanceManager().getModel().getValue(this.MODEL_ZK_MEDIA_CLEARED, Boolean.class);
        return (zkMediaCleared != null && zkMediaCleared.booleanValue());
    }


    private void addModelObservers(Component rendererPanel, Object initialObject, DataType type)
    {
        WidgetModel model = getWidgetInstanceManager().getModel();
        String pathToRootObjectInModel = getPathToRootObjectInModel();
        ModelObserver zkContentObserver = () -> {
            Object currentRoot = model.getValue(pathToRootObjectInModel, Object.class);
            if(Objects.equals(currentRoot, initialObject))
            {
                adjustView(rendererPanel, type);
            }
        };
        this.initiallyDisplayedMediaModelInfo = getMediaModelInfo(getMediaModel());
        ModelObserver mediaObjectChangeObserver = () -> {
            Object currentRoot = model.getValue(pathToRootObjectInModel, Object.class);
            if(Objects.equals(currentRoot, initialObject))
            {
                MediaModel currentMedia = getMediaModel();
                String currentMediaInfo = getMediaModelInfo(currentMedia);
                if(!StringUtils.equals(this.initiallyDisplayedMediaModelInfo, currentMediaInfo))
                {
                    setDisplayContentFromMediaModel(true);
                    this.initiallyDisplayedMediaModelInfo = currentMediaInfo;
                }
                adjustView(rendererPanel, type);
            }
        };
        Object object = new Object(this, model, pathToRootObjectInModel, initialObject, zkContentObserver, mediaObjectChangeObserver);
        model.addObserver(this.MODEL_ZK_MEDIA_MODEL_KEY, (ValueObserver)zkContentObserver);
        model.addObserver(pathToRootObjectInModel, (ValueObserver)object);
        model.addObserver(getPathToMediaObjectInModel(), (ValueObserver)mediaObjectChangeObserver);
    }


    private void refreshDisplayedMediaInfo()
    {
        if(getZkMedia() != null && !isZKMediaCleared())
        {
            this.currentlyDisplayedMediaInfo = getZKMediaInfo(getZkMedia());
        }
        else
        {
            this.currentlyDisplayedMediaInfo = getMediaModelInfo(getMediaModel());
        }
    }


    private String getMediaModelInfo(MediaModel mediaModel)
    {
        if(mediaModel != null)
        {
            return String.format("%s_created:%s_modified:%s_url:%s", new Object[] {String.valueOf(mediaModel.getPk()),
                            String.valueOf(mediaModel.getModifiedtime()), String.valueOf(mediaModel.getCreationtime()),
                            getMediaURL(mediaModel)});
        }
        return "media.is.null";
    }


    private String getMediaURL(MediaModel mediaModel)
    {
        return mediaModel.getURL();
    }


    protected boolean isInSecureFolder(MediaModel mediaModel)
    {
        boolean isSecure = false;
        if(mediaModel.getFolder() != null)
        {
            MediaStorageConfigService.MediaFolderConfig cfg = this.mediaStorageConfigService.getConfigForFolder(mediaModel.getFolder().getQualifier());
            if(cfg != null)
            {
                isSecure = cfg.isSecured();
            }
        }
        return isSecure;
    }


    private String getZKMediaInfo(Media zkMedia)
    {
        if(zkMedia != null)
        {
            return String.format("zkMedia name:%s_contentType:%s_format:%s_objIdentity:%s", new Object[] {zkMedia.getName(), zkMedia
                            .getContentType(), zkMedia.getFormat(), String.valueOf(zkMedia.hashCode())});
        }
        return "zk.is.null";
    }


    private boolean isDisplayContentFromMediaModel()
    {
        return (
                        (Boolean)ObjectUtils.defaultIfNull(getWidgetInstanceManager().getModel().getValue(this.MODEL_DISPLAY_FROM_MODEL, Boolean.class), Boolean.TRUE))
                        .booleanValue();
    }


    private void setDisplayContentFromMediaModel(boolean displayContentFromMediaModel)
    {
        getWidgetInstanceManager().getModel().setValue(this.MODEL_DISPLAY_FROM_MODEL, Boolean.valueOf(displayContentFromMediaModel));
    }


    private void createPreviewForDisplayableMedia()
    {
        MediaModel mediaModel = getMediaModel();
        if(mediaModel != null)
        {
            String mediaURL = getMediaURL(mediaModel);
            Div fakeImageZoomCnt = createFakeImageZoomCnt(mediaURL);
            this.imgDiv.appendChild((Component)fakeImageZoomCnt);
        }
    }


    private void updateButtonsState(DataType type)
    {
        if(areButtonsVisible())
        {
            boolean hasData = isMediaEmpty(getMediaModel(), getZkMedia());
            this.deleteButton.setDisabled((hasData || !canEdit(type)));
            this.downloadButton.setDisabled(hasData);
        }
    }


    private void clearTmpImageDivButTmpImage()
    {
        for(Iterator<Component> iterator = this.tmpImageDiv.getChildren().iterator(); iterator.hasNext(); )
        {
            Component cmp = iterator.next();
            if(cmp != this.tmpImage)
            {
                iterator.remove();
            }
        }
    }


    private Div createFakeImageZoomCnt(String url)
    {
        Div imageZoomContainer = new Div();
        Image fakeSmallImage = new Image();
        fakeSmallImage.setSrc(url);
        Div smallImageCnt = new Div();
        smallImageCnt.setSclass("small small-image-preview-cnt");
        smallImageCnt.appendChild((Component)fakeSmallImage);
        smallImageCnt.setParent((Component)imageZoomContainer);
        imageZoomContainer.addEventListener("onMouseOver", (EventListener)new Object(this, imageZoomContainer, fakeSmallImage));
        return imageZoomContainer;
    }


    protected Vlayout initMediaInfoPanel()
    {
        Vlayout mediaInfoPanel = new Vlayout();
        mediaInfoPanel.setSclass("mediaInfoCnt");
        initMediaInfoLabels();
        if(this.labelPkValue != null)
        {
            mediaInfoPanel.appendChild((Component)createMediaInfoHeaderLabel(Labels.getLabel("media.info.pk")));
            mediaInfoPanel.appendChild((Component)this.labelPkValue);
        }
        if(this.labelTimeCreatedValue != null)
        {
            mediaInfoPanel.appendChild((Component)createMediaInfoHeaderLabel(Labels.getLabel("media.info.timecreated")));
            mediaInfoPanel.appendChild((Component)this.labelTimeCreatedValue);
        }
        if(this.labelTimeModifiedValue != null)
        {
            mediaInfoPanel.appendChild((Component)createMediaInfoHeaderLabel(Labels.getLabel("media.info.timemodified")));
            mediaInfoPanel.appendChild((Component)this.labelTimeModifiedValue);
        }
        return mediaInfoPanel;
    }


    protected void initMediaInfoLabels()
    {
        MediaModel mediaModel = getMediaModel();
        if(mediaModel != null)
        {
            if(getObjectFacade().isNew(mediaModel))
            {
                this.labelPkValue = createMediaInfoValueLabel("media.info.pk", Labels.getLabel("media.info.not.available"));
                this.labelTimeCreatedValue = createMediaInfoValueLabel("media.info.timecreated", Labels.getLabel("media.info.not.available"));
                this.labelTimeModifiedValue = createMediaInfoValueLabel("media.info.timemodified",
                                Labels.getLabel("media.info.not.available"));
            }
            else
            {
                this.labelPkValue = createMediaInfoValueLabel("media.info.pk", mediaModel.getPk().toString());
                Locale currentLocale = getCockpitLocaleService().getCurrentLocale();
                this.labelTimeCreatedValue = createMediaInfoValueLabel("media.info.timecreated",
                                getDateFormatter().format(mediaModel.getCreationtime(), this.format, currentLocale));
                this.labelTimeModifiedValue = createMediaInfoValueLabel("media.info.timemodified",
                                getDateFormatter().format(mediaModel.getModifiedtime(), this.format, currentLocale));
            }
        }
    }


    protected Label createMediaInfoHeaderLabel(String initialValue)
    {
        Label label = new Label(initialValue);
        label.setClass("mediaInfoHeader");
        return label;
    }


    protected Label createMediaInfoValueLabel(String infoLabel, String initialValue)
    {
        Label label = new Label(initialValue);
        label.setClass("mediaInfoValue");
        return label;
    }


    private Button initDownloadButton(Component parent)
    {
        this.downloadButton = new Button(Labels.getLabel("download.media.download"));
        this.downloadButton.setSclass("media-download-btn y-btn-secondary");
        this.downloadButton.setDisabled(isMediaEmpty(getMediaModel(), getZkMedia()));
        parent.appendChild((Component)this.downloadButton);
        this.downloadButton.addEventListener("onClick", event -> {
            Media zkMedia = getZkMedia();
            if(zkMedia != null)
            {
                if(zkMedia.isBinary())
                {
                    InputStream streamFromMedia = zkMedia.getStreamData();
                    Filedownload.save(streamFromMedia, zkMedia.getContentType(), zkMedia.getName());
                }
                else
                {
                    byte[] bytes = (zkMedia.getStringData() != null) ? zkMedia.getStringData().getBytes() : null;
                    if(bytes != null)
                    {
                        Filedownload.save(bytes, zkMedia.getContentType(), zkMedia.getName());
                    }
                }
            }
            else
            {
                InputStream streamFromMedia = getMediaService().getStreamFromMedia(getMediaModel());
                Filedownload.save(streamFromMedia, getMediaModel().getMime(), getMediaModel().getRealFileName());
            }
        });
        return this.downloadButton;
    }


    private void initDeleteButton(Component parent, DataType type)
    {
        this.deleteButton = new Button(Labels.getLabel("media.clear.content.command"));
        this.deleteButton.setDisabled(!canEdit(type));
        UITools.modifySClass((HtmlBasedComponent)this.deleteButton, "media-clear-content-btn", true);
        UITools.modifySClass((HtmlBasedComponent)this.deleteButton, "y-btn-danger", true);
        this.deleteButton.addEventListener("onClick", event -> {
            clearPreviewImage();
            this.downloadButton.setDisabled(true);
            this.deleteButton.setDisabled(true);
            MediaModel mediaModel = getMediaModel();
            mediaModel.setURL("");
            mediaModel.setRealFileName("");
            mediaModel.setMime("");
            setDisplayContentFromMediaModel(true);
            getWidgetInstanceManager().getModel().put(this.MODEL_ZK_MEDIA_CLEARED, Boolean.TRUE);
            getWidgetInstanceManager().getModel().put(this.MODEL_ZK_MEDIA_MODEL_KEY, null);
            getWidgetInstanceManager().getModel().setValue(getPathToMediaObjectInModel(), mediaModel);
        });
        parent.appendChild((Component)this.deleteButton);
    }


    private void clearPreviewImage()
    {
        this.tmpImageDiv.getChildren().clear();
        this.tmpImage.setVisible(false);
        this.tmpImageDiv.setVisible(false);
        this.tmpImageDiv.appendChild((Component)this.tmpImage);
        this.imgDiv.getChildren().clear();
        this.imgDiv.setVisible(true);
    }


    private boolean isMediaEmpty(MediaModel mediaModel, Media zkMedia)
    {
        return (StringUtils.isEmpty(getMediaURL(mediaModel)) && zkMedia == null);
    }


    protected void setLimitOnMaxFileSize(Fileupload fileupload)
    {
        String propertyMaxFileSize = getCockpitProperties().getProperty("fileUpload.maxSize");
        long sizeInKB = 10000L;
        if(StringUtils.isNumeric(propertyMaxFileSize))
        {
            sizeInKB = NumberUtils.toLong(propertyMaxFileSize);
        }
        fileupload.setUpload("maxsize=" + sizeInKB);
    }


    protected boolean isDisplayable(MediaModel media)
    {
        return (media.getMime() != null && media.getMime().contains("image"));
    }


    private String getDynamicUrlForMedia(VersionAwareImage media)
    {
        Media zkMedia = getZkMedia();
        return Utils.getDynamicMediaURI((AbstractComponent)media, media.getVersion(), "c/" + zkMedia.getName(), zkMedia.getFormat());
    }


    protected Media getZkMedia()
    {
        return (Media)getWidgetInstanceManager().getModel().getValue(this.MODEL_ZK_MEDIA_MODEL_KEY, Media.class);
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected MediaService getMediaService()
    {
        return this.mediaService;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }


    public MimeTypeChecker getMimeTypeChecker()
    {
        return this.mimeTypeChecker;
    }


    @Required
    public void setMimeTypeChecker(MimeTypeChecker mimeTypeChecker)
    {
        this.mimeTypeChecker = mimeTypeChecker;
    }


    protected MediaStorageConfigService getMediaStorageConfigService()
    {
        return this.mediaStorageConfigService;
    }


    @Required
    public void setMediaStorageConfigService(MediaStorageConfigService mediaStorageConfigService)
    {
        this.mediaStorageConfigService = mediaStorageConfigService;
    }


    public CockpitProperties getCockpitProperties()
    {
        return this.cockpitProperties;
    }


    @Required
    public void setCockpitProperties(CockpitProperties cockpitProperties)
    {
        this.cockpitProperties = cockpitProperties;
    }


    protected PermissionFacadeStrategy getPermissionFacadeStrategy()
    {
        return this.permissionFacadeStrategy;
    }


    @Required
    public void setPermissionFacadeStrategy(PermissionFacadeStrategy permissionFacadeStrategy)
    {
        this.permissionFacadeStrategy = permissionFacadeStrategy;
    }


    protected ObjectPreviewService getObjectPreviewService()
    {
        return this.objectPreviewService;
    }


    @Required
    public void setObjectPreviewService(ObjectPreviewService objectPreviewService)
    {
        this.objectPreviewService = objectPreviewService;
    }


    protected WidgetInstanceManager getWidgetInstanceManager()
    {
        return this.widgetInstanceManager;
    }


    protected Div getTmpImageDiv()
    {
        return this.tmpImageDiv;
    }


    protected Div getImgDiv()
    {
        return this.imgDiv;
    }


    protected VersionAwareImage getTmpImage()
    {
        return this.tmpImage;
    }


    protected Button getDownloadButton()
    {
        return this.downloadButton;
    }


    protected Button getDeleteButton()
    {
        return this.deleteButton;
    }


    protected Fileupload getFileupload()
    {
        return this.fileupload;
    }


    protected Label getLabelPkValue()
    {
        return this.labelPkValue;
    }


    protected Label getLabelTimeCreatedValue()
    {
        return this.labelTimeCreatedValue;
    }


    protected Label getLabelTimeModifiedValue()
    {
        return this.labelTimeModifiedValue;
    }


    public DateFormatter getDateFormatter()
    {
        return this.dateFormatter;
    }


    public void setDateFormatter(DateFormatter dateFormatter)
    {
        this.dateFormatter = dateFormatter;
    }


    public String getFormat()
    {
        return this.format;
    }


    public void setFormat(String format)
    {
        this.format = format;
    }
}
