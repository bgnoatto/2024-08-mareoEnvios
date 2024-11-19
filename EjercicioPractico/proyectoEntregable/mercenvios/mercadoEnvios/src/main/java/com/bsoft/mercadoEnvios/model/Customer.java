package com.bsoft.mercadoEnvios.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_Name")
    private String lastName;
    
    private String address;
    private String city;
    
    @OneToMany(mappedBy = "customer")
    @JsonIgnore  // Ignorar la serialización de la relación OneToMany para evitar recursión
    private List<Shipping> shippings;
}
