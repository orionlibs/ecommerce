package de.hybris.platform.processengine.adminapi;

import de.hybris.platform.adminapi.annotation.AdminApiController;
import de.hybris.platform.processengine.BusinessProcessEvent;
import de.hybris.platform.processengine.BusinessProcessService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@AdminApiController
@RequestMapping({"/businessprocess"})
public class BusinessProcessController
{
    private static final Logger LOG = LoggerFactory.getLogger(BusinessProcessController.class);
    private final BusinessProcessService businessProcessService;


    @Autowired
    public BusinessProcessController(BusinessProcessService businessProcessService)
    {
        this.businessProcessService = businessProcessService;
    }


    @ApiOperation("Trigger business process event")
    @ApiResponses({@ApiResponse(code = 200, message = "Operation succeeded"), @ApiResponse(code = 409, message = "Event has been already triggered"), @ApiResponse(code = 404, message = "event can't be null"), @ApiResponse(code = 400, message = "Bad request")})
    @PostMapping({"/events"})
    ResponseEntity<TriggerEventResponse> triggerEvent(@RequestBody TriggerEventRequest request)
    {
        TriggerEventResponse triggerEventResponse = new TriggerEventResponse();
        try
        {
            BusinessProcessEvent businessProcessEvent = BusinessProcessEvent.builder(request.getEvent()).withChoice(request.getChoice()).build();
            boolean businessProcessServiceResponse = this.businessProcessService.triggerEvent(businessProcessEvent);
            if(businessProcessServiceResponse)
            {
                triggerEventResponse.setMessage("Operation succeeded");
                return ResponseEntity.status(HttpStatus.OK).body(triggerEventResponse);
            }
            triggerEventResponse.setMessage("Event has been already triggered");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(triggerEventResponse);
        }
        catch(Exception e)
        {
            LOG.error(e.getMessage(), e);
            triggerEventResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(triggerEventResponse);
        }
    }
}
