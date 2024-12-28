package com.example.TiendaInventario.Config;

import com.example.TiendaInventario.Entities.Category;
import com.example.TiendaInventario.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        // Crear y guardar las categorías predefinidas
        Category gato = new Category("Gato");
        Category gatito = new Category("Gatito");
        Category adulto = new Category("Adulto");
        Category cachorro = new Category("Cachorro");
        Category limpieza = new Category("Limpieza");
        Category mascotas = new Category("Mascotas");

        categoryRepository.saveAll(Arrays.asList(gato, gatito, adulto, cachorro, limpieza, mascotas));
    }
}
