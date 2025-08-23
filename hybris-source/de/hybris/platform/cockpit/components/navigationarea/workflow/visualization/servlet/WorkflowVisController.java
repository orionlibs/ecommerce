package de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.servlet;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping({"/workflowVisualization"})
public class WorkflowVisController
{
    @RequestMapping(method = {RequestMethod.GET})
    public Map generate(@RequestParam String pk, @RequestParam(required = false, defaultValue = "1.0") Double scale, @RequestParam(required = false, defaultValue = "en") String lang) throws Exception
    {
        Map<Object, Object> model = new HashMap<>();
        model.put("pk", pk);
        model.put("scale", scale);
        double scaleD = scale.doubleValue();
        double lower = (scaleD > 0.5D) ? (scaleD - 0.5D) : scaleD;
        double higher = (scaleD < 3.0D) ? (scaleD + 0.5D) : scaleD;
        model.put("lower", Double.valueOf(lower));
        model.put("higher", Double.valueOf(higher));
        model.put("lang", lang);
        return model;
    }
}
