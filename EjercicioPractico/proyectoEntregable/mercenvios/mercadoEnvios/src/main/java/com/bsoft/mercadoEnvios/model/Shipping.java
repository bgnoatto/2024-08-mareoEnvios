package com.bsoft.mercadoEnvios.model;

import java.time.LocalDate;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Shipping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private String state;
    
    @Column(name = "send_date")
    private LocalDate sendDate;
    
    @Column(name = "arrive_date")
    private LocalDate arriveDate;
    
    private int priority;
    
    @OneToMany(mappedBy = "shipping")
    @JsonManagedReference
    private List<ShippingItem> shippingItems;
}
