package io.github.orionlibs.ecommerce.store.api;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SaveStoreRequest implements Serializable
{
    @NotBlank(message = "storeName must not be blank")
    private String storeName;
}
