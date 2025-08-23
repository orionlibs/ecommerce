package de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.servlet;

import de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.EdgeStyle;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.workflow.model.WorkflowModel;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({"/workflowDiagramRenderer"})
public class WorkflowDiagramImageController
{
    private static final Logger LOG = LoggerFactory.getLogger(WorkflowDiagramImageController.class);
    @Resource
    private VisualizationService visualizationService;
    @Resource
    ModelService modelService;


    @RequestMapping(method = {RequestMethod.GET}, produces = {"image/svg+xml;charset=UTF-8"})
    @ResponseBody
    public String generateDiagram(@RequestParam String pk, @RequestParam String style, @RequestParam(defaultValue = "1.0", required = false) double scale, @RequestParam(defaultValue = "en", required = false) String lang) throws Exception
    {
        Registry.activateMasterTenant();
        WorkflowModel workflow = (WorkflowModel)this.modelService.get(PK.parse(pk));
        EdgeStyle graphEdgeStyle = EdgeStyle.LABELS_IN_CIRCLE;
        if(style != null)
        {
            try
            {
                graphEdgeStyle = EdgeStyle.valueOf(style);
            }
            catch(IllegalArgumentException iae)
            {
                LOG.error("No value for given enum type. Using default value.", iae);
            }
        }
        String response = this.visualizationService.prepareVisualization(workflow, graphEdgeStyle, scale, lang);
        return response;
    }
}
