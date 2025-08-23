package de.hybris.platform.configurablebundlecockpits.productcockpit.components.inspector.impl;

import de.hybris.platform.cockpit.components.inspector.impl.DefaultCoverageInspectorRenderer;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.components.listview.impl.CoverageInfoAction;
import de.hybris.platform.cockpit.model.general.ListComponentModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.ListActionHelper;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.Registry;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class DefaultBundleInspectorRenderer extends DefaultCoverageInspectorRenderer
{
    public void render(Component parent, TypedObject object)
    {
        String objectTextLabel = UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabelForTypedObject(object);
        Label label = new Label(objectTextLabel);
        label.setTooltiptext(objectTextLabel);
        Div headerDiv = new Div();
        headerDiv.setSclass("infoAreaLabel");
        UITools.applyTestID((Component)headerDiv, "inspectorMode");
        headerDiv.appendChild((Component)label);
        parent.appendChild((Component)headerDiv);
        Div toolbarDiv = new Div();
        toolbarDiv.setSclass("infoHeaderToolbar");
        headerDiv.appendChild((Component)toolbarDiv);
        renderToolbar((Component)toolbarDiv, object);
        Div propertiesCnt = new Div();
        propertiesCnt.setSclass("inspectorPropertiesCnt");
        parent.appendChild((Component)propertiesCnt);
        propertiesCnt.appendChild(createCoverageComponent(object));
        propertiesCnt.appendChild(createFilledValuesComponent(object));
    }


    protected void renderToolbar(Component parent, TypedObject item)
    {
        prepareEditActionButton(parent, item);
        Div coverageActionCnt = new Div();
        coverageActionCnt.setSclass("inspectorToolbarButton roundedCorners");
        UITools.applyTestID((Component)coverageActionCnt, "inspectorToolbarCoverageButton");
        parent.appendChild((Component)coverageActionCnt);
        CoverageInfoAction coverageAction = (CoverageInfoAction)Registry.getApplicationContext().getBean("CoverageInfoAction", CoverageInfoAction.class);
        ListViewAction.Context context = coverageAction.createContext((ListComponentModel)ListActionHelper.createDefaultListModel(item), item);
        ListActionHelper.renderSingleAction((ListViewAction)coverageAction, context, (Component)coverageActionCnt, "infoToolbarAction");
    }
}
