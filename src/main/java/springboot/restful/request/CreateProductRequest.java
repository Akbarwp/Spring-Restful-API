package springboot.restful.request;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProductRequest {

    @NotBlank
    private String categoryId;

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotNull
    @DecimalMin("0.00")
    private Double priceBuy;

    @NotNull
    @DecimalMin("0.00")
    private Double priceSell;
    
    @NotNull
    @Min(0)
    private Integer stock;

    @NotBlank
    @Size(max = 255)
    @Nullable
    private String description;
}
