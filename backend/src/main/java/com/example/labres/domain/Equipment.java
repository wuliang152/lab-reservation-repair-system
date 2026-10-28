package com.example.labres.domain;

import java.time.LocalDate;

public class Equipment {
    private Long id;
    private Long labId;
    private String code;
    private String name;
    private String model;
    private String category;
    private String status;
    private LocalDate purchaseDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getLabId() { return labId; }
    public void setLabId(Long labId) { this.labId = labId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }
}
