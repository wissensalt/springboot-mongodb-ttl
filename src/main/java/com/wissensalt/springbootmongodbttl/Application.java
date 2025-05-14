package com.wissensalt.springbootmongodbttl;

import static org.springframework.data.domain.Sort.Direction.DESC;

import jakarta.validation.Valid;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@SpringBootApplication
public class Application {

  private final DepartmentRepository departmentRepository;
  private final EmployeeRepository employeeRepository;
  private final TtlProperty ttlProperty;
  private final MongoTemplate mongoTemplate;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public ApplicationRunner applicationRunner() {
    return args -> {
      mongoTemplate.indexOps(Employee.class)
          .ensureIndex(new Index()
              .on("expiredAt", DESC)
              .expire(ttlProperty.getEmployee().getExpireAfter(), TimeUnit.SECONDS));
    };
  }

  @PostMapping("/create")
  public boolean create(
      @RequestBody @Valid Request request
  ) {
    final Optional<Department> departmentOptional =
        departmentRepository.findByName(request.getDepartmentName());
    final Department department = departmentOptional.orElseGet(() -> createDepartment(request));
    final Employee employee = new Employee();
    employee.setDepartmentId(department.getId());
    employee.setName(request.getEmployeeName());
    employee.setCreatedAt(Instant.now());
    employee.setExpiredAt(Instant.now().plusSeconds(ttlProperty.getEmployee().getExpireAfter()));
    employeeRepository.save(employee);

    return true;
  }

  @GetMapping("/")
  public List<Response> getAll() {
    List<Response> responseList = new ArrayList<>();
    List<Employee> employees = employeeRepository.findAll();
    for (Employee employee : employees) {
      Optional<Department> departmentOptional = departmentRepository.findById(
          employee.getDepartmentId());
      if (departmentOptional.isPresent()) {
        Department department = departmentOptional.get();
        Response response = new Response();
        response.setDepartmentId(department.getId().toString());
        response.setDepartmentName(department.getName());
        response.setEmployeeId(employee.getId().toString());
        response.setEmployeeName(employee.getName());
        responseList.add(response);
      }
    }

    return responseList;
  }

  private Department createDepartment(Request request) {
    Department department = new Department();
    department.setName(request.getDepartmentName());
    department.setCreatedAt(Instant.now());

    return departmentRepository.save(department);
  }
}
