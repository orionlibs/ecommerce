package io.github.orionlibs.ecommerce.lifecycle.api;

import io.github.orionlibs.ecommerce.core.api.APIService;
import io.github.orionlibs.ecommerce.lifecycle.ControllerUtils;
import io.github.orionlibs.ecommerce.lifecycle.LifecycleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerUtils.baseAPIPath)
@Validated
@Tag(name = "Lifecycle manager", description = "Lifecycle manager")
public class SaveStateTransitionAPIController extends APIService
{
    @Autowired private LifecycleService lifecycleService;


    @Operation(
                    summary = "Save state transition",
                    description = "Save state transition",
                    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    required = true,
                                    content = @Content(
                                                    schema = @Schema(implementation = StateTransitionRequest.class)
                                    )
                    ),
                    responses = {@ApiResponse(responseCode = "201", description = "State transition saved"),
                                    @ApiResponse(responseCode = "400", description = "Invalid input")}
    )
    @PostMapping(value = "/lifecycles/instances/{instanceID}/transitions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    //@PreAuthorize("hasAuthority('DATABASE_MANAGER')")
    public ResponseEntity<?> saveStateTransition(@PathVariable String instanceID, @Valid @RequestBody StateTransitionRequest requestBean)
    {
        lifecycleService.processStateTransition(instanceID, requestBean);
        return ResponseEntity.created(null).body(Map.of());
    }
}
