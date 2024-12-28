package com.example.TiendaInventario.Controller;

import com.example.TiendaInventario.Entities.Category;
import com.example.TiendaInventario.Entities.Product;
import com.example.TiendaInventario.Services.CategoryService;
import com.example.TiendaInventario.Services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CatalogController {

    @Autowired
    private final ProductService productService;

    @Autowired
    private final CategoryService categoryService;

    @GetMapping("/catalog")
    public String catalog(Model model, @RequestParam(required = false) String query, @RequestParam(required = false) Long categoryId) {
        List<Product> products;
        if (categoryId != null) {
            Category category = categoryService.getCategoryById(categoryId).orElse(null);
            if (category != null) {
                products = productService.getProductsByCategory(category);
            } else {
                products = List.of();
            }
        } else if (query != null && !query.isEmpty()) {
            products = productService.searchProductsByName(query);
        } else {
            products = productService.getAllProducts();
        }
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "catalog/catalog";
    }
}
