package de.hybris.platform.configurablebundlecockpits.productcockpit.navigationnode.browserarea.tree;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.configurablebundlecockpits.servicelayer.services.BundleNavigationService;
import java.util.HashMap;
import java.util.Map;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Window;

public final class MoveItemModalDialogRenderer
{
    public static Window getMoveItemModalDialog(Tree tree, TypedObject typedObject, TypedObject targetNodeObj, BundleNavigationService bundleNavigationService)
    {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("title", Labels.getLabel("general.choose"));
        arguments.put("message", Labels.getLabel("configurablebundlecockpits.bundle.label.copymove"));
        Button moveNodeButton = new Button(Labels.getLabel("general.move"));
        moveNodeButton.addEventListener("onClick", (EventListener)new Object(tree, targetNodeObj, typedObject, bundleNavigationService, moveNodeButton));
        arguments.put("moveNodeButton", moveNodeButton);
        Button copyNodeButton = new Button(Labels.getLabel("general.copy"));
        copyNodeButton.addEventListener("onClick", (EventListener)new Object(targetNodeObj, typedObject, bundleNavigationService, copyNodeButton));
        arguments.put("copyNodeButton", copyNodeButton);
        return (Window)Executions.createComponents("/productcockpit/messagedialog.zul", null, arguments);
    }
}
