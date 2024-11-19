package com.entregable.sistema.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Task {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
	@Id
    @JsonProperty("shippingId")
    private Integer shippingId;
    
    @JsonProperty("time-start-in-seg")
    private Integer timeStartInSeg;
    
    @JsonProperty("next-state")
    private boolean nextState;
    
    private String status;	// IN_PROGRESS, SUCCESS, FAILED
}
