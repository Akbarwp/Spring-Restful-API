package springboot.restful.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchProductRequest {

    private String name;
    
    private Double priceBuy;
    
    private Double priceSell;
    
    private Integer stock;

    @NotNull
    private Integer page;

    @NotNull
    private Integer size;
}
