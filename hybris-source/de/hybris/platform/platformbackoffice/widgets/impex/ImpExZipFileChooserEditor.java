package de.hybris.platform.platformbackoffice.widgets.impex;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import de.hybris.platform.impex.jalo.ImpExMedia;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.platformbackoffice.widgets.impex.exception.ZipHandlingException;
import de.hybris.platform.servicelayer.model.ModelService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

public class ImpExZipFileChooserEditor extends AbstractCockpitEditorRenderer<String>
{
    private static final String FORM = "importForm";
    @Resource
    private ModelService modelService;


    public void render(Component parent, EditorContext<String> context, EditorListener<String> listener)
    {
        Validate.notNull("All parameters are mandatory", new Object[] {parent, context, listener});
        Div zipFileChooserView = new Div();
        zipFileChooserView.setParent(parent);
        WidgetInstanceManager widgetInstanceManager = ((Editor)parent).getWidgetInstanceManager();
        ImpExImportForm form = (ImpExImportForm)widgetInstanceManager.getModel().getValue("importForm", ImpExImportForm.class);
        Validate.notNull("Editor is bound to non-existent form object", new Object[] {form});
        widgetInstanceManager.getModel().addObserver("importForm", () -> {
            ImpExMediaModel impExMedia = form.getImpExMedia();
            if(impExMedia != null)
            {
                renderZipFileChooser((Component)zipFileChooserView, extractZipEntries(impExMedia));
            }
        });
        renderBlankZipFileChooser((Component)zipFileChooserView);
    }


    private List<String> extractZipEntries(ImpExMediaModel impExMedia)
    {
        List<String> zipEntries;
        ImpExMedia jaloMedia = (ImpExMedia)this.modelService.getSource(impExMedia);
        try
        {
            zipEntries = jaloMedia.getZipEntryNames();
        }
        catch(IOException | de.hybris.platform.jalo.JaloBusinessException e)
        {
            throw new ZipHandlingException("Could not fetch zip entry names", e);
        }
        return zipEntries;
    }


    public void renderZipFileChooser(Component zipFileChooserView, List<String> zipEntries)
    {
        if(zipEntries.isEmpty())
        {
            renderBlankZipFileChooser(zipFileChooserView);
        }
        else
        {
            zipFileChooserView.getChildren().clear();
            Combobox combobox = new Combobox();
            ListModelList listModelList = new ListModelList(zipEntries);
            combobox.setModel((ListModel)listModelList);
            combobox.setParent(zipFileChooserView);
        }
    }


    public void renderBlankZipFileChooser(Component parent)
    {
        parent.getChildren().clear();
        Combobox combobox = new Combobox();
        ListModelList listModelList = new ListModelList(new ArrayList());
        combobox.setModel((ListModel)listModelList);
        combobox.setDisabled(true);
        combobox.setParent(parent);
    }
}
