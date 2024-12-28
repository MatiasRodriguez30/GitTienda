package com.example.TiendaInventario.Repository;

import com.example.TiendaInventario.Entities.Category;
import com.example.TiendaInventario.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategories(Category category);
    List<Product> findByNameContainingIgnoreCase(String query);
}
