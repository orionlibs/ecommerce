package de.hybris.platform.cockpit.components.editorarea.export;

import de.hybris.platform.cockpit.components.listview.AbstractListViewAction;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.EditorConfiguration;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.exporter.EditorAreaExporter;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;

public class DefaultExportEditorAreaAction extends AbstractListViewAction
{
    private UIConfigurationService uIConfigurationService;
    private TypeService typeService;
    private EditorAreaExporter editorAreaExporter;
    private String fileName;
    private String imageURI = "/cockpit/images/icon_func_get_pdf_available.png";
    private String tooltip = "editorarea.pdf.preview.tooltip";


    protected void doCreateContext(ListViewAction.Context context)
    {
    }


    public Menupopup getContextPopup(ListViewAction.Context context)
    {
        return null;
    }


    public EventListener getEventListener(ListViewAction.Context context)
    {
        return (EventListener)new Object(this, context);
    }


    protected String getFileName(TypedObject item)
    {
        if(getFileName() == null)
        {
            return TypeTools.getValueAsString(UISessionUtils.getCurrentSession().getLabelService(), item);
        }
        return getFileName();
    }


    public String getImageURI(ListViewAction.Context context)
    {
        return this.imageURI;
    }


    public void setImageURI(String imageURI)
    {
        this.imageURI = imageURI;
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        return null;
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel(this.tooltip);
    }


    public void setEditorAreaExporter(EditorAreaExporter editorAreaExporter)
    {
        this.editorAreaExporter = editorAreaExporter;
    }


    public EditorConfiguration getConfiguration(String typecode)
    {
        ObjectTemplate template = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(typecode);
        return (EditorConfiguration)this.uIConfigurationService.getComponentConfiguration(template, "editorArea", EditorConfiguration.class);
    }


    @Required
    public void setuIConfigurationService(UIConfigurationService uIConfigurationService)
    {
        this.uIConfigurationService = uIConfigurationService;
    }


    protected EditorAreaExporter getEditorAreaExporter()
    {
        return this.editorAreaExporter;
    }


    protected String getFileName()
    {
        return this.fileName;
    }


    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }


    public void setTooltip(String tooltip)
    {
        this.tooltip = tooltip;
    }


    private TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }
}
