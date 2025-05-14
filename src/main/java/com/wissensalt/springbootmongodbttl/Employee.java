package com.wissensalt.springbootmongodbttl;

import java.time.Instant;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "employee")
public class Employee {

  @Id
  private ObjectId id;

  @Indexed(name = "ttl")
  private Instant expiredAt;

  private Instant createdAt;

  private ObjectId departmentId;
  private String name;
}
