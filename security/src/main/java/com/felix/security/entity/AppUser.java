package com.felix.security.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.felix.security.entity.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer")
public class AppUser {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "customer_id")
  private long id;

  private String name;

  private String email;

  @Column(name = "mobile_number")
  private String mobileNumber;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String pwd;

  private String role;

  @Column(name = "create_dt")
  @JsonIgnore
  private Date createDt;

}
