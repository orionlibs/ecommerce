package io.github.orionlibs.ecommerce.store.api;

import io.github.orionlibs.ecommerce.core.api.APIService;
import io.github.orionlibs.ecommerce.store.ControllerUtils;
import io.github.orionlibs.ecommerce.store.StoreService;
import io.github.orionlibs.ecommerce.store.model.StoreModel;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerUtils.baseAPIPath)
@Validated
@Tag(name = "Stores", description = "Stores")
public class SaveStoreAPIController extends APIService
{
    @Autowired private StoreService storeService;


    @Operation(
                    summary = "Saves a store",
                    description = "Saves a store",
                    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    required = true,
                                    content = @Content(
                                                    schema = @Schema(implementation = SaveStoreRequest.class)
                                    )
                    ),
                    responses = {@ApiResponse(responseCode = "201", description = "Store saved"),
                                    @ApiResponse(responseCode = "400", description = "Invalid input")}
    )
    @PostMapping(value = "/stores", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    //@PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> saveStore(@Valid @RequestBody SaveStoreRequest requestBean)
    {
        StoreModel model = storeService.save(requestBean);
        return ResponseEntity.created(null).body(Map.of());
    }
}
