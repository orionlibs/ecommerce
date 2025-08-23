package de.hybris.platform.configurablebundlecockpits.productcockpit.components.navigationarea.renderer;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.productcockpit.components.navigationarea.CatalogNavigationAreaModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import java.util.List;
import java.util.Locale;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.impl.XulElement;

public class CatalogSectionSingleItemRenderer implements ListitemRenderer
{
    private static final int TIMEOUT = 500;
    private static final int UI_TOOLS_RESULTS_SIZE = 2;
    private final CatalogNavigationAreaModel sectionPanelModel;
    private final CommonI18NService commonI18NService;


    public CatalogSectionSingleItemRenderer(CatalogNavigationAreaModel sectionPanelModel, CommonI18NService commonI18NService)
    {
        this.sectionPanelModel = sectionPanelModel;
        this.commonI18NService = commonI18NService;
    }


    public void render(Listitem item, Object data) throws Exception
    {
        String label = null;
        String mnemonicLabel = null;
        String labelLanguage = null;
        String sclass = null;
        String catalogname = "";
        Listcell listCell = new Listcell();
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            String id = "NavigationArea_Catalog_item_element";
            UITools.applyTestID((Component)listCell, "NavigationArea_Catalog_item_element");
            listCell.getUuid();
        }
        CatalogVersionModel catalogVersion = (CatalogVersionModel)data;
        if(getSectionPanelModel().getNavigationArea().getSelectedCatalogVersion() != null)
        {
            boolean selected = getSectionPanelModel().getNavigationArea().getSelectedCatalogVersion().equals(data);
            if(selected)
            {
                listCell.setFocus(selected);
                item.setSelected(selected);
                ((Listbox)item.getParent()).setSelectedIndex(item.getIndex());
            }
        }
        CatalogModel catalog = getSectionPanelModel().getNavigationArea().getProductCockpitCatalogService().getCatalog(catalogVersion);
        String langIso = UISessionUtils.getCurrentSession().getGlobalDataLanguageIso();
        LanguageModel languageModel = getCommonI18NService().getLanguage(langIso);
        Locale locale = getCommonI18NService().getLocaleForLanguage(languageModel);
        if(catalog.getName(locale) != null)
        {
            catalogname = catalog.getName(locale);
        }
        else
        {
            List<String> result = UITools.searchForLabel(catalog, CatalogModel.class.getMethod("getName", new Class[] {Locale.class}), catalogVersion
                            .getLanguages());
            if(result.size() == 2)
            {
                catalogname = result.get(0);
                labelLanguage = result.get(1);
            }
        }
        label = ((catalogname != null) ? catalogname : ("<" + catalog.getId() + ">")) + " " + ((catalogname != null) ? catalogname : ("<" + catalog.getId() + ">"));
        item.setAttribute("name", label);
        mnemonicLabel = catalogVersion.getMnemonic();
        sclass = catalogVersion.getActive().booleanValue() ? "activeCatalogVersionTreeItem" : "catalogVersionTreeItem";
        listCell.setLabel(label);
        UITools.modifySClass((HtmlBasedComponent)listCell, sclass, true);
        if(mnemonicLabel != null)
        {
            Label mnemLabel = new Label(" (" + mnemonicLabel + ")");
            mnemLabel.setSclass("catalog-mnemonic-label");
            listCell.appendChild((Component)mnemLabel);
        }
        if(labelLanguage != null)
        {
            Label langLabel = new Label(" [" + labelLanguage + "]");
            langLabel.setSclass("catalog-language-label");
            listCell.appendChild((Component)langLabel);
        }
        UITools.addDragHoverClickEventListener((XulElement)listCell, (EventListener)new Object(this, item), 500, "PerspectiveDND");
        item.appendChild((Component)listCell);
    }


    public CatalogNavigationAreaModel getSectionPanelModel()
    {
        return this.sectionPanelModel;
    }


    protected CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }
}
