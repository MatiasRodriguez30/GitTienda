package com.example.TiendaInventario.Controller;

import com.example.TiendaInventario.Entities.Category;
import com.example.TiendaInventario.Entities.Product;
import com.example.TiendaInventario.Services.CategoryService;
import com.example.TiendaInventario.Services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    @Autowired
    private final ProductService productService;

    @Autowired
    private final CategoryService categoryService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping
    public String listProducts(Model model, @RequestParam(required = false) String query, @RequestParam(required = false) Long categoryId) {
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
        return "products/list";
    }

    @GetMapping("/grid")
    public String productGrid(Model model, @RequestParam(required = false) String query, @RequestParam(required = false) Long categoryId) {
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
        return "products/product-grid";
    }

    @GetMapping("/add")
    public String addProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "products/add";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product, @RequestParam("imageFile") MultipartFile imageFile, @RequestParam("categoryIds") List<Long> categoryIds) {
        List<Category> categories = categoryIds.stream()
                .map(categoryService::getCategoryById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        product.setCategories(categories);

        if (!imageFile.isEmpty()) {
            try {
                String fileName = imageFile.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(fileName);
                Files.write(filePath, imageFile.getBytes());
                product.setImage(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            product.setImage(null); // No se subió un archivo, usar la URL proporcionada
        }

        productService.saveProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "products/edit";
    }

    @PostMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id, @ModelAttribute Product product, @RequestParam("imageFile") MultipartFile imageFile, @RequestParam("categoryIds") List<Long> categoryIds) {
        List<Category> categories = categoryIds.stream()
                .map(categoryService::getCategoryById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        product.setCategories(categories);

        if (!imageFile.isEmpty()) {
            try {
                String fileName = imageFile.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(fileName);
                Files.write(filePath, imageFile.getBytes());
                product.setImage(fileName);
                product.setImageUrl(null); // Si se sube un archivo, eliminar la URL
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            product.setImage(null); // No se subió un archivo, usar la URL proporcionada
        }

        product.setId(id);
        productService.saveProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }

    @PostMapping("/incrementStock/{id}")
    public String incrementStock(@PathVariable Long id) {
        Product product = productService.getProductById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        product.setStock(product.getStock() + 1);
        productService.saveProduct(product);
        return "redirect:/products";
    }

    @PostMapping("/decrementStock/{id}")
    public String decrementStock(@PathVariable Long id) {
        Product product = productService.getProductById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        if (product.getStock() > 0) {
            product.setStock(product.getStock() - 1);
            productService.saveProduct(product);
        }
        return "redirect:/products";
    }
}
