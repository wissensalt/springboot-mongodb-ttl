package com.wissensalt.springbootmongodbttl;

import java.time.Instant;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "department")
public class Department {

  @Id
  private ObjectId id;

  @Indexed(name = "ttl", expireAfter = "1s")
  private Instant createdAt;

  private String name;
}
