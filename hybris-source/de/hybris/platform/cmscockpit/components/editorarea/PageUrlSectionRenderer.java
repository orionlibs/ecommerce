package de.hybris.platform.cmscockpit.components.editorarea;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.resolvers.CMSUrlResolver;
import de.hybris.platform.cmscockpit.components.editorarea.model.SimpleSectionRowModel;
import de.hybris.platform.cmscockpit.components.editorarea.view.SimpleSectionRowComponent;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionRenderer;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zul.Div;

public class PageUrlSectionRenderer implements SectionRenderer
{
    private Map<String, CMSUrlResolver<AbstractPageModel>> resolvers;


    public void render(SectionPanel panel, Component parent, Component captionComponent, Section section)
    {
        if(this.resolvers != null)
        {
            AbstractPageModel page = (AbstractPageModel)((TypedObject)panel.getModel().getContext().get("currentObject")).getObject();
            for(String resolverkey : this.resolvers.keySet())
            {
                Div rowContainer = new Div();
                parent.appendChild((Component)rowContainer);
                String url = ((CMSUrlResolver)this.resolvers.get(resolverkey)).resolve((CMSItemModel)page);
                if(url != null)
                {
                    String helpText = Labels.getLabel("tooltip.page.url." + resolverkey);
                    String label = Labels.getLabel("label.page.url." + resolverkey);
                    createSimpleRowSection((HtmlBasedComponent)rowContainer, label, url, helpText);
                }
            }
        }
    }


    protected void createSimpleRowSection(HtmlBasedComponent rowContainer, String label, Object value, String helpText)
    {
        SimpleSectionRowModel sectionModel = new SimpleSectionRowModel(label, value);
        sectionModel.setToolTipText(helpText);
        sectionModel.setEditable(false);
        SimpleSectionRowComponent rowDiv = new SimpleSectionRowComponent(sectionModel);
        rowDiv.setParent((Component)rowContainer);
    }


    @Required
    public void setResolvers(Map<String, CMSUrlResolver<AbstractPageModel>> resolvers)
    {
        this.resolvers = resolvers;
    }
}
