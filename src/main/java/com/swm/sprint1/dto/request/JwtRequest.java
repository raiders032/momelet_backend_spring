package com.swm.sprint1.dto.request;

import io.swagger.annotations.ApiParam;
import lombok.*;

import javax.validation.constraints.NotBlank;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JwtRequest {

    @ApiParam(value = "jwt 토큰", required = true)
    @NotBlank(message = "jwt 토큰이 비어있습니다.")
    private String jwt;

}
