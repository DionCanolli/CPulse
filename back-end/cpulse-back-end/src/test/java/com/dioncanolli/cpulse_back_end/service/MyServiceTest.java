package com.dioncanolli.cpulse_back_end.service;

import com.dioncanolli.cpulse_back_end.dto.PaginatedProductsDTO;
import com.dioncanolli.cpulse_back_end.dto.ProductDTO;
import com.dioncanolli.cpulse_back_end.repository.*;
import com.dioncanolli.cpulse_back_end.utility.ClassMapper;
import com.dioncanolli.cpulse_back_end.utility.JWTTokenGenerator;
import org.mockito.Mock;

import com.dioncanolli.cpulse_back_end.entity.Category;
import com.dioncanolli.cpulse_back_end.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MyServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private JWTTokenBlacklistRepository jwtTokenBlacklistRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WishlistItemRepository wishlistItemRepository;

    @Mock
    private ClassMapper classMapper;

    @Mock
    private JWTTokenGenerator jwtTokenGenerator;

    @InjectMocks
    private MyService myService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    ---------------------------------------- Test for findAllCategories method ------------------------------------------
    @Test
    void testFindAllCategories() {
        // Arrange
        Category category1 = new Category();
        category1.setId(1L);
        category1.setCategoryName("Computer");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setCategoryName("Laptop");

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        // Act
        List<Category> categories = myService.findAllCategories();

        // Assert
        assertNotNull(categories);
        assertEquals(2, categories.size());
        assertEquals("Computer", categories.get(0).getCategoryName());
        verify(categoryRepository, times(1)).findAll();
    }

//    --------------------------------------------- Test for findProductByProductName method -----------------------------------

    @Test
    void testFindProductByProductName() {
        // Arrange
        String productName = "Laptop";
        Product product = new Product();
        product.setId(1L);
        product.setProductName(productName);

        when(productRepository.findProductByProductName(productName)).thenReturn(product);

        // Act
        Product foundProduct = myService.findProductByProductName(productName);

        // Assert
        assertNotNull(foundProduct);
        assertEquals(productName, foundProduct.getProductName());
        verify(productRepository, times(1)).findProductByProductName(productName);
    }

//    --------------------------------------------- Tests for getAllProducts method -----------------------------------

    @Test
    void testGetAllProducts_whenCategoryNameIsNullAndProductNameIsNull() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 20);
        List<Product> productList = Arrays.asList(
                new Product(1L, "Product1", "Description1", 10.0, 100, "image1.jpg", new Category("Computer")),
                new Product(1L, "Product2", "Description2", 15.0, 200, "image2.jpg", new Category("Laptop"))
        );
        Page<Product> page = new PageImpl<>(productList);

        when(productRepository.findAll(pageable)).thenReturn(page);
        when(classMapper.mapToProductDTO(any(Product.class))).thenReturn(new ProductDTO(1L, "Product1", "Description1", "All", "url", 10.0, 10));

        // Act
        PaginatedProductsDTO result = myService.getAllProducts(null, null, 0);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getAllProducts().size()); // Adjusted for correct getter
        assertEquals(2, result.getTotalProducts()); // Adjusted for correct getter
        verify(productRepository, times(1)).findAll(pageable);
    }

    @Test
    void testGetAllProducts_whenCategoryNameIsAllAndProductNameIsNull() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 20);
        List<Product> productList = Arrays.asList(
                new Product(1L, "Product1", "Description1", 10.0, 100, "image1.jpg", new Category("Computer")),
                new Product(1L, "Product2", "Description2", 15.0, 200, "image2.jpg", new Category("Laptop"))
        );
        Page<Product> page = new PageImpl<>(productList);

        when(productRepository.findAll(pageable)).thenReturn(page);
        when(classMapper.mapToProductDTO(any(Product.class))).thenReturn(new ProductDTO(1L, "Product1", "Description1", "All", "url", 10.0, 10));

        // Act
        PaginatedProductsDTO result = myService.getAllProducts("All", null, 0);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getAllProducts().size()); // Adjusted for correct getter
        assertEquals(2, result.getTotalProducts()); // Adjusted for correct getter
        verify(productRepository, times(1)).findAll(pageable);
    }

    @Test
    void testGetAllProducts_whenBothCategoryAndProductNameAreProvided() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 20);
        Category category = new Category("Category1");
        List<Product> productList = Arrays.asList(
                new Product("Product1", "Description1", 10.0, 100, "image1.jpg", category)
        );
        Page<Product> page = new PageImpl<>(productList);

        when(categoryRepository.findCategoryByCategoryName("Category1")).thenReturn(category);
        when(productRepository.findAllProductsByCategoryAndProductNameIgnoreCase(category, "Product1", pageable)).thenReturn(page);
        when(classMapper.mapToProductDTO(any(Product.class))).thenReturn(new ProductDTO(1L, "Product1", "Description1", "All", "url", 10.0, 10));

        // Act
        PaginatedProductsDTO result = myService.getAllProducts("Category1", "Product1", 0);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getAllProducts().size()); // Adjusted for correct getter
        assertEquals(1, result.getTotalProducts()); // Adjusted for correct getter
        verify(productRepository, times(1)).findAllProductsByCategoryAndProductNameIgnoreCase(category, "Product1", pageable);
    }

    @Test
    void testGetAllProducts_whenNoProductsMatch() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 20);
        List<Product> productList = Arrays.asList(); // Empty list
        Page<Product> page = new PageImpl<>(productList);

        when(productRepository.findAll(pageable)).thenReturn(page);

        // Act
        PaginatedProductsDTO result = myService.getAllProducts("Computer", "NonExistingProduct", 0);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getAllProducts().size()); // Adjusted for correct getter
        assertEquals(0, result.getTotalProducts()); // Adjusted for correct getter
        verify(productRepository, times(1)).findAll(pageable);
    }

}
