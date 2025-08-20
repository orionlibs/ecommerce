package io.github.orionlibs.ecommerce.lifecycle.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.orionlibs.ecommerce.core.api.APIService;
import io.github.orionlibs.ecommerce.core.api.error.APIError;
import io.github.orionlibs.ecommerce.core.api.idempotency.IdempotencyConflictException;
import io.github.orionlibs.ecommerce.core.api.idempotency.IdempotencyService;
import io.github.orionlibs.ecommerce.core.api.idempotency.model.IdempotencyRecordModel;
import io.github.orionlibs.ecommerce.lifecycle.ControllerUtils;
import io.github.orionlibs.ecommerce.lifecycle.LifecycleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerUtils.baseAPIPath)
@Validated
@Tag(name = "Lifecycle manager", description = "Lifecycle manager")
public class SaveStateTransitionAPIController extends APIService
{
    @Autowired private LifecycleService lifecycleService;
    @Autowired private IdempotencyService idempotencyService;
    @Autowired private ObjectMapper objectMapper;


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
    public ResponseEntity<?> saveStateTransition(@PathVariable UUID instanceID,
                    @RequestHeader(name = "Idempotency-Key", required = false) String idempotencyKey,
                    HttpServletRequest request)
    {
        String endpoint = request.getRequestURI();
        if(idempotencyKey != null && !idempotencyKey.trim().isEmpty())
        {
            Optional<IdempotencyRecordModel> existingRecord = idempotencyService.findExistingRecord(idempotencyKey, endpoint);
            if(existingRecord.isPresent())
            {
                IdempotencyRecordModel record = existingRecord.get();
                if(!idempotencyService.isRequestConsistent(record, request))
                {
                    throw new IdempotencyConflictException("Idempotency key reused with different request body");
                }
                return buildStoredResponse(record);
            }
            IdempotencyRecordModel record = idempotencyService.createRecord(idempotencyKey, endpoint, request);
            try
            {
                lifecycleService.processStateTransition(instanceID);
                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.add("Idempotency-Key", idempotencyKey);
                idempotencyService.updateRecordWithResponse(record, 201, true, responseHeaders);
                return ResponseEntity.created(null).headers(responseHeaders).body(Map.of());
            }
            catch(Exception e)
            {
                APIError apiError = new APIError(
                                OffsetDateTime.now(),
                                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                                "saveStateTransition: " + e.getMessage(),
                                null);
                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.add("Idempotency-Key", idempotencyKey);
                idempotencyService.updateRecordWithResponse(record, 400, apiError, responseHeaders);
                throw e; // Re-throw to let global exception handler deal with it
            }
        }
        else
        {
            lifecycleService.processStateTransition(instanceID);
            return ResponseEntity.created(null).body(Map.of());
        }
        //lifecycleService.processStateTransition(instanceID);
        //return ResponseEntity.created(null).body(Map.of());
    }


    private ResponseEntity<OrderResponse> buildStoredResponse(IdempotencyRecordModel record)
    {
        try
        {
            // Reconstruct the response from stored data
            OrderResponse responseBody = objectMapper.readValue(record.getResponseBody(), OrderResponse.class);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Idempotency-Key", record.getIdempotencyKey());
            headers.add("X-Idempotent-Replay", "true");
            return ResponseEntity.status(record.getResponseStatus())
                            .headers(headers)
                            .body(responseBody);
        }
        catch(Exception e)
        {
            throw new RuntimeException("Failed to reconstruct idempotent response", e);
        }
    }
}
