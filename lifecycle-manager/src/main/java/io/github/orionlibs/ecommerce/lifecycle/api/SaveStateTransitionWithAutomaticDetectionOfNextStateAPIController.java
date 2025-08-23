package io.github.orionlibs.ecommerce.lifecycle.api;

import io.github.orionlibs.ecommerce.core.api.APIService;
import io.github.orionlibs.ecommerce.core.api.idempotency.Idempotent;
import io.github.orionlibs.ecommerce.lifecycle.ControllerUtils;
import io.github.orionlibs.ecommerce.lifecycle.LifecycleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerUtils.baseAPIPath)
@Validated
@Tag(name = "Lifecycle manager", description = "Lifecycle manager")
public class SaveStateTransitionWithAutomaticDetectionOfNextStateAPIController extends APIService
{
    @Autowired private LifecycleService lifecycleService;


    @Operation(
                    summary = "Save state transition",
                    description = "Save state transition",
                    parameters = @io.swagger.v3.oas.annotations.Parameter(
                                    name = "instanceID",
                                    description = "The ID of the lifecycle instance whose state we want to update",
                                    required = true,
                                    in = ParameterIn.PATH,
                                    schema = @Schema(type = "string")
                    ),
                    responses = {@ApiResponse(responseCode = "201", description = "State transition saved"),
                                    @ApiResponse(responseCode = "400", description = "Invalid input")}
    )
    @PostMapping(value = "/lifecycles/instances/{instanceID}/transitions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    //@PreAuthorize("hasAuthority('DATABASE_MANAGER')")
    @Idempotent
    public ResponseEntity<Map<String, Object>> saveStateTransitionWithAutomaticDetectionOfNextState(@PathVariable UUID instanceID)
    {
        try
        {
            lifecycleService.processStateTransition(instanceID);
            //the idempotency aspect will add the Idempotency-Key header in stored responses and persist it
            return ResponseEntity.created(null).body(Map.of("is_state_transition_saved", true));
        }
        catch(Exception e)
        {
            return ResponseEntity.created(null).body(Map.of("is_state_transition_saved", false));
        }
    }
}
