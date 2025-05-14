package com.wissensalt.springbootmongodbttl;

import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DepartmentRepository extends MongoRepository<Department, ObjectId> {

  Optional<Department> findByName(String name);
}
