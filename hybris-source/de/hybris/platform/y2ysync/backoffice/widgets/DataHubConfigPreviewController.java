package de.hybris.platform.y2ysync.backoffice.widgets;

import com.google.common.base.Preconditions;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.components.Codeeditor;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.datahub.client.extension.ExtensionSource;
import com.hybris.datahub.client.extension.ExtensionStringSource;
import com.hybris.datahub.client.extension.InvalidDataHubExtensionException;
import com.hybris.datahub.core.services.DataHubExtensionUploadService;
import de.hybris.deltadetection.model.StreamConfigurationContainerModel;
import de.hybris.deltadetection.model.StreamConfigurationModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.y2ysync.model.Y2YColumnDefinitionModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationModel;
import de.hybris.y2ysync.services.DataHubConfigService;
import de.hybris.y2ysync.services.DataHubExtGenerationConfig;
import java.io.IOException;
import java.util.Set;
import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Textbox;

public class DataHubConfigPreviewController extends DefaultWidgetController
{
    private static final Logger LOG = LoggerFactory.getLogger(DataHubConfigPreviewController.class);
    public static final String DEFAULT_TARGET_TYPE = "HybrisCore";
    public static final String WIDGET_TITLE = "title";
    @WireVariable("rawItemsCheckbox")
    private Checkbox rawItemsCheckbox;
    @WireVariable("canonicalItemsCheckbox")
    private Checkbox canonicalItemsCheckbox;
    @WireVariable("targetItemsCheckbox")
    private Checkbox targetItemsCheckbox;
    @WireVariable("targetPanel")
    private Div targetPanel;
    @WireVariable("targetUserTextbox")
    private Textbox targetUserTextbox;
    @WireVariable("targetPasswordTextbox")
    private Textbox targetPasswordTextbox;
    @WireVariable("targetUrlTextbox")
    private Textbox targetUrlTextbox;
    @WireVariable("targetSystemTextbox")
    private Textbox targetSystemTextbox;
    @WireVariable("generateBtn")
    private Button generateBtn;
    @WireVariable("uploadBtn")
    private Button uploadBtn;
    @WireVariable("saveBtn")
    private Button saveBtn;
    @WireVariable("codeEditor")
    private Codeeditor codeEditor;
    @Resource
    ModelService modelService;
    @Resource
    DataHubExtensionUploadService dataHubExtensionUploadService;
    @Resource
    DataHubConfigService dataHubConfigService;
    @Resource
    NotificationService notificationService;
    private DataHubExtGenerationConfig dataHubExtGenerationConfig;
    private Y2YStreamConfigurationContainerModel streamConfigurationContainer;


    public void initialize(Component comp)
    {
        super.initialize(comp);
        setWidgetTitle(getLabel("title"));
        registerListeners();
        initTargetPanel();
        refreshWidget();
    }


    private void registerListeners()
    {
        this.rawItemsCheckbox.addEventListener("onCheck", e -> refreshWidget());
        this.canonicalItemsCheckbox.addEventListener("onCheck", e -> refreshWidget());
        this.targetItemsCheckbox.addEventListener("onCheck", e -> refreshWidget());
        this.generateBtn.addEventListener("onClick", e -> generateDataHubConfiguration());
        this.uploadBtn.addEventListener("onClick", e -> uploadToDataHub());
        this.saveBtn.addEventListener("onClick", e -> saveConfiguration());
    }


    private void initTargetPanel()
    {
        this.targetUserTextbox.setValue("");
        this.targetPasswordTextbox.setValue("");
        this.targetUrlTextbox.setValue("");
    }


    private void refreshWidget()
    {
        updateTargetPanel();
        updateConfigurationFromView();
    }


    private void refreshConfigurationContainer(StreamConfigurationContainerModel streamConfigurationContainer)
    {
        this.modelService.refresh(streamConfigurationContainer);
        Set<StreamConfigurationModel> configurations = streamConfigurationContainer.getConfigurations();
        for(StreamConfigurationModel config : configurations)
        {
            this.modelService.refresh(config);
            if(config instanceof Y2YStreamConfigurationModel)
            {
                for(Y2YColumnDefinitionModel column : ((Y2YStreamConfigurationModel)config).getColumnDefinitions())
                {
                    this.modelService.refresh(column);
                }
            }
        }
    }


    private void generateDataHubConfiguration()
    {
        refreshWidget();
        refreshConfigurationContainer((StreamConfigurationContainerModel)this.streamConfigurationContainer);
        try
        {
            verifyTargetSystem();
            String dataHubExtension = this.dataHubConfigService.createDataHubExtension(this.streamConfigurationContainer, this.dataHubExtGenerationConfig);
            renderDataHubConfigInEditor(dataHubExtension);
        }
        catch(Exception e)
        {
            this.notificationService.notifyUser(this.notificationService.getWidgetNotificationSource(getWidgetInstanceManager()), "GenerateConfiguration", NotificationEvent.Level.FAILURE, new Object[] {e});
        }
    }


    private void verifyTargetSystem()
    {
        if(this.dataHubExtGenerationConfig.isGenerateTargetItems())
        {
            Preconditions.checkArgument(StringUtils.isNotBlank(this.streamConfigurationContainer.getTargetSystem()),
                            Labels.getLabel("y2y.target.system.required"));
        }
    }


    private void uploadToDataHub()
    {
        try
        {
            saveDataHubExtConfiguration();
            this.dataHubExtensionUploadService.uploadExtension((ExtensionSource)getExtension());
            this.notificationService.notifyUser(this.notificationService.getWidgetNotificationSource(getWidgetInstanceManager()), "UploadToDataHub", NotificationEvent.Level.SUCCESS, new Object[0]);
        }
        catch(IOException | InvalidDataHubExtensionException | de.hybris.platform.servicelayer.exceptions.ModelSavingException e)
        {
            LOG.error("Failed to save and upload DataHub ext configuration", e);
            this.notificationService.notifyUser(this.notificationService.getWidgetNotificationSource(getWidgetInstanceManager()), "UploadToDataHub", NotificationEvent.Level.FAILURE, new Object[] {e});
        }
        catch(Exception e)
        {
            this.notificationService.notifyUser(this.notificationService.getWidgetNotificationSource(getWidgetInstanceManager()), "General", NotificationEvent.Level.FAILURE, new Object[] {e});
            LOG.error("Undefined error has occured", e);
        }
    }


    private ExtensionStringSource getExtension()
    {
        this.modelService.refresh(this.streamConfigurationContainer);
        if(this.streamConfigurationContainer == null || StringUtils.isBlank(this.streamConfigurationContainer.getDataHubExtension()))
        {
            throw new InvalidDataHubExtensionException();
        }
        return new ExtensionStringSource(this.streamConfigurationContainer.getDataHubExtension());
    }


    private void saveDataHubExtConfiguration()
    {
        this.modelService.refresh(this.streamConfigurationContainer);
        this.streamConfigurationContainer.setDataHubExtension(this.codeEditor.getValue());
        this.modelService.save(this.streamConfigurationContainer);
        sendOutput("output", this.streamConfigurationContainer);
    }


    private void saveConfiguration()
    {
        try
        {
            saveDataHubExtConfiguration();
            this.notificationService.notifyUser(this.notificationService.getWidgetNotificationSource(getWidgetInstanceManager()), "SaveConfiguration", NotificationEvent.Level.SUCCESS, new Object[0]);
        }
        catch(Exception ex)
        {
            this.notificationService.notifyUser(this.notificationService.getWidgetNotificationSource(getWidgetInstanceManager()), "SaveConfiguration", NotificationEvent.Level.FAILURE, new Object[] {ex});
        }
    }


    private void renderDataHubConfigInEditor(String configuration)
    {
        this.codeEditor.invalidate();
        this.codeEditor.setValue(configuration);
    }


    private void updateConfigurationFromView()
    {
        this.dataHubExtGenerationConfig = new DataHubExtGenerationConfig();
        this.dataHubExtGenerationConfig.setPrettyFormat(true);
        this.dataHubExtGenerationConfig.setTargetType("HybrisCore");
        this.dataHubExtGenerationConfig.setTargetUserName(this.targetUserTextbox.getValue());
        this.dataHubExtGenerationConfig.setTargetPassword(this.targetPasswordTextbox.getValue());
        this.dataHubExtGenerationConfig.setTargetExportURL(this.targetUrlTextbox.getValue());
        this.dataHubExtGenerationConfig.setTargetExportCodes("");
        this.dataHubExtGenerationConfig.setGenerateRawItems(this.rawItemsCheckbox.isChecked());
        this.dataHubExtGenerationConfig.setGenerateCanonicalItems(this.canonicalItemsCheckbox.isChecked());
        this.dataHubExtGenerationConfig.setGenerateTargetItems(this.targetItemsCheckbox.isChecked());
    }


    @SocketEvent(socketId = "createContext")
    public void displayWidget(Y2YStreamConfigurationContainerModel configurationContainer)
    {
        if(configurationContainer == null)
        {
            LOG.warn("Input object for DataHub Configuration Generator Widget is null");
            return;
        }
        this.streamConfigurationContainer = configurationContainer;
        String dataHubExt = this.streamConfigurationContainer.getDataHubExtension();
        this.codeEditor.setValue(StringUtils.isNotBlank(dataHubExt) ? dataHubExt : "");
        refreshWidget();
    }


    private void updateTargetPanel()
    {
        enableTargetPanelForContainerWithTargetSystem();
        showTargetPanelfTargetItemsGenerationEnabled();
    }


    private void showTargetPanelfTargetItemsGenerationEnabled()
    {
        this.targetPanel.setVisible(this.targetItemsCheckbox.isChecked());
    }


    private void enableTargetPanelForContainerWithTargetSystem()
    {
        if(this.streamConfigurationContainer != null && StringUtils.isNotBlank(this.streamConfigurationContainer.getTargetSystem()))
        {
            this.targetItemsCheckbox.setDisabled(false);
            this.targetSystemTextbox.setValue(this.streamConfigurationContainer.getTargetSystem());
            this.targetSystemTextbox.setDisabled(true);
        }
        else
        {
            this.targetItemsCheckbox.setDisabled(true);
            this.targetItemsCheckbox.setTooltip("Container must have target system assigned");
        }
    }
}
