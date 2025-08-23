package de.hybris.platform.platformbackoffice.widgets.impex;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelService;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.impex.enums.ImpExValidationModeEnum;
import de.hybris.platform.servicelayer.impex.ExportConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

public class ImpExExportModeChooser extends AbstractCockpitEditorRenderer<ImpExValidationModeEnum>
{
    private static final String WIZARD_VALIDATION_MODE = "exportForm.validationMode";
    private Combobox combobox;
    @Resource
    EnumerationService enumerationService;
    @Resource
    LabelService labelService;


    public void render(Component parent, EditorContext<ImpExValidationModeEnum> context, EditorListener<ImpExValidationModeEnum> listener)
    {
        ListModelList<ImpExValidationModeComboModel> validationModes = getValidationModeComboModel();
        this.combobox = new Combobox();
        this.combobox.setParent(parent);
        this.combobox.setReadonly(true);
        this.combobox.setModel((ListModel)validationModes);
        ImpExValidationModeEnum initialValue = (ImpExValidationModeEnum)context.getInitialValue();
        if(initialValue != null)
        {
            String label = this.labelService.getObjectLabel(initialValue);
            validationModes.setSelection(Collections.singleton(new ImpExValidationModeComboModel(initialValue, label)));
        }
        this.combobox.addEventListener("onSelect", event -> handleSelectEvent(parent, listener));
    }


    private void handleSelectEvent(Component parent, EditorListener<ImpExValidationModeEnum> listener)
    {
        WidgetInstanceManager widgetInstanceManager = ((Editor)parent).getWidgetInstanceManager();
        ImpExValidationModeComboModel selectedMode = (ImpExValidationModeComboModel)this.combobox.getSelectedItem().getValue();
        widgetInstanceManager.getModel().setValue("exportForm.validationMode", selectedMode.getEnum());
        listener.onValueChanged(selectedMode.getEnum());
    }


    private ListModelList<ImpExValidationModeComboModel> getValidationModeComboModel()
    {
        List<ImpExValidationModeEnum> enumerations = new ArrayList<>();
        List<String> validationEnumCodes = (List<String>)Arrays.<ExportConfig.ValidationMode>stream(ExportConfig.ValidationMode.values()).map(e -> e.getCode()).collect(Collectors.toList());
        for(String enumCode : validationEnumCodes)
        {
            enumerations.add((ImpExValidationModeEnum)this.enumerationService.getEnumerationValue(ImpExValidationModeEnum.class, enumCode));
        }
        List<ImpExValidationModeComboModel> validationModeComboModel = new ArrayList<>();
        for(ImpExValidationModeEnum value : enumerations)
        {
            String label = this.labelService.getObjectLabel(value);
            validationModeComboModel.add(new ImpExValidationModeComboModel(value, label));
        }
        return new ListModelList(validationModeComboModel);
    }
}
