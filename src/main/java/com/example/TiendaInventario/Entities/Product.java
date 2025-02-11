package com.example.TiendaInventario.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
    private Double pricePerKilo; // Asegúrate de que este campo esté definido
    private String description;
    private String image; // Nombre del archivo de imagen subido
    private String imageUrl; // URL de la imagen si se proporciona
    private Integer stock;
    @Enumerated(EnumType.STRING)
    private Availability availability;

    @ManyToMany
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;
}
