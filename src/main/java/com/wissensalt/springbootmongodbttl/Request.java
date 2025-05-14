package com.wissensalt.springbootmongodbttl;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Request {

  @NotBlank
  private String departmentName;

  @NotBlank
  private String employeeName;
}
