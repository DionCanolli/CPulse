package com.dioncanolli.cpulse_back_end.service;

import com.dioncanolli.cpulse_back_end.dto.*;
import com.dioncanolli.cpulse_back_end.entity.*;
import com.dioncanolli.cpulse_back_end.repository.*;
import com.dioncanolli.cpulse_back_end.utility.ClassMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class MyService {

    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final CartItemRepository cartItemRepository;
    private final CategoryRepository categoryRepository;
    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final JWTTokenBlacklistRepository jwtTokenBlacklistRepository;

    @Value("${checkout.secret.key}")
    private String checkoutSecretKey;
    private final String paymentUrl = "https://api.sandbox.checkout.com/payments";

    public MyService(CartItemRepository cartItemRepository, CategoryRepository categoryRepository, PaymentRepository paymentRepository, ProductRepository productRepository, RoleRepository roleRepository, UserRepository userRepository, WishlistItemRepository wishlistItemRepository, JWTTokenBlacklistRepository jwtTokenBlacklistRepository) {
        this.cartItemRepository = cartItemRepository;
        this.categoryRepository = categoryRepository;
        this.paymentRepository = paymentRepository;
        this.productRepository = productRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.wishlistItemRepository = wishlistItemRepository;
        this.jwtTokenBlacklistRepository = jwtTokenBlacklistRepository;
    }

    public List<Category> findAllCategories(){
        return categoryRepository.findAll();
    }

    public Product findProductByProductName(String productName){
        return productRepository.findProductByProductName(productName);
    }

    public PaginatedProductsDTO getAllProducts(String categoryName, String productName, int page){
        if (page < 0){
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, 20);
        List<ProductDTO> productDTOs = new ArrayList<>();
        Page<Product> productsPaged = null;

        if(categoryName != null){
            if (!categoryName.equals("All")) {
                Category category = categoryRepository.findCategoryByCategoryName(categoryName);

                if (category == null && productName != null) {
                    productsPaged = productRepository.findAllProductsByProductNameIgnoreCase(productName, pageable);
                } else if (category != null && productName == null) {
                    productsPaged = productRepository.findAllProductsByCategory(category, pageable);
                } else if (category != null) {
                    productsPaged = productRepository.findAllProductsByCategoryAndProductNameIgnoreCase(category, productName, pageable);
                } else {
                    productsPaged = productRepository.findAll(pageable);
                }
            }else{
                if(productName == null){
                    productsPaged = productRepository.findAll(pageable);
                }else{
                    productsPaged = productRepository.findAllProductsByProductNameIgnoreCase(productName, pageable);
                }
            }
        }else{
            if(productName == null){
                productsPaged = productRepository.findAll(pageable);
            }else{
                productsPaged = productRepository.findAllProductsByProductNameIgnoreCase(productName, pageable);
            }
        }

        if (productsPaged != null){
            if (!productsPaged.isEmpty()){
                for (Product product : productsPaged){
                    ProductDTO productDTO = ClassMapper.mapToProductDTO(product);
                    productDTO.setProductImageUrl(productDTO.getProductImageUrl() // me marr veq emriProduktit.extension
                            .substring(productDTO.getProductImageUrl().lastIndexOf("\\") + 1));
                    productDTOs.add(productDTO);
                }
                return new PaginatedProductsDTO(productDTOs, productsPaged.getTotalElements());
            }
        }
        return null;
    }

    public ProductDTO findProduct(String productName){
        Product product = productRepository.findProductByProductName(productName);
        if (product != null){
            ProductDTO productDTO = ClassMapper.mapToProductDTO(product);
            productDTO.setProductImageUrl(productDTO.getProductImageUrl() // me marr veq emriProduktit.extension
                    .substring(productDTO.getProductImageUrl().lastIndexOf("\\") + 1));
            return productDTO;
        }
        return null;
    }

    public List<UserDTO> findAllUsers(){
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = new ArrayList<>();

        for (User user : users){
            userDTOs.add(ClassMapper.mapToUserDTO(user));
        }
        return (userDTOs.isEmpty()) ? null : userDTOs;
    }

    public List<WishlistItem> findAllWishlistItemsByUser(String userEmail){
        User user = findUserByEmail(userEmail);
        if (user == null){
            return null;
        }
        List<WishlistItem> wishlistItems = wishlistItemRepository.findAllWishlistItemsByUser(user);
        return (wishlistItems.isEmpty()) ? null : wishlistItems;
    }

    public UserDTO findUserDTOByEmail(String email){
        User user = userRepository.findByEmail(email);
        return (user == null) ? null : ClassMapper.mapToUserDTO(user);
    }

    public User findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public boolean insertUser(User user){
        if (findUserByEmail(user.getEmail()) == null){
            user.setRole(roleRepository.findByRoleName("ROLE_USER"));
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean insertProduct(ProductDTO productDTO, MultipartFile productImage){
        Category category = categoryRepository.findCategoryByCategoryName(productDTO.getProductCategory());
        if (category == null) return false;

        if (findProductByProductName(productDTO.getProductName()) == null){

            File imageFile = new File(productDTO.getProductImageUrl());

            Product product = new Product(productDTO.getProductName(), productDTO.getProductDescription(), productDTO.getProductPrice(),
                    productDTO.getProductStockQuantity(), productDTO.getProductImageUrl(), category);
            try{
                productRepository.save(product);
                productImage.transferTo(imageFile);
                return true;
            }catch (IOException e){
                System.out.println(e.getMessage());
            }
        }
        return false;
    }

    public boolean wishlistItemUserAndProductExists(User user, Product product){
        WishlistItem wishlistItem = wishlistItemRepository.findByUserAndProduct(user, product);
        return wishlistItem != null;
    }

    public boolean cartItemUserAndProductExists(User user, Product product){
        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product);
        return cartItem != null;
    }

    public boolean insertWishtlistItem(User user, String productName){
        Product product = findProductByProductName(productName);
        if (user != null && product != null && !wishlistItemUserAndProductExists(user, product)){
            wishlistItemRepository.save(new WishlistItem(user, product));
            return true;
        }
        return false;
    }

    public boolean insertCartItem(User user, String productName){
        Product product = findProductByProductName(productName);
        if (user != null && product != null && cartItemRepository.findByUserAndProduct(user, product) == null){
            cartItemRepository.save(new CartItem(user, product));
            return true;
        }
        return false;
    }

    // Emailen nuk mun e ndryshon
    public boolean modifyUser(User user){
        User userFound = findUserByEmail(user.getEmail());
        if (userFound != null){
            userFound.setFirstName(user.getFirstName());
            userFound.setLastName(user.getLastName());
            userFound.setUsername(user.getUsername());
            userFound.setPhoneNumber(user.getPhoneNumber());
            userRepository.save(userFound);
            return true;
        }
        return false;
    }

    public boolean modifyProduct(String productName, int quantity) {
        Product product = productRepository.findProductByProductName(productName);
        if (product != null){
            product.setProductStockQuantity(quantity);
            productRepository.save(product);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean deleteUser(String userEmail){
        User userFound = findUserByEmail(userEmail);
        if (userFound != null){
            cartItemRepository.deleteAllByUser(userFound);
            wishlistItemRepository.deleteAllByUser(userFound);
            paymentRepository.deleteAllByUser(userFound);
            userRepository.delete(userFound);
            return true;
        }
        return false;
    }

    public boolean deleteProduct(String productName) throws IOException {
        Product productFound = findProductByProductName(productName);
        if (productFound != null){
            Files.deleteIfExists(Paths.get(productFound.getProductImageUrl()));
            productRepository.delete(productFound);
            return true;
        }
        return false;
    }

    public boolean deleteWishlistItem(String userEmail, String productName){
        User userFound = findUserByEmail(userEmail);
        Product productFound = findProductByProductName(productName);
        if (userFound != null && productFound != null){
            WishlistItem wishlistItem = wishlistItemRepository.findByUserAndProduct(userFound, productFound);
            if (wishlistItem != null){
                wishlistItemRepository.delete(wishlistItem);
                return true;
            }
        }
        return false;
    }

    public boolean deleteCartItem(User user, String productName){
        Product productFound = findProductByProductName(productName);
        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, productFound);
        if (cartItem != null){
            cartItemRepository.delete(cartItem);
            return true;
        }
        return false;
    }

    public boolean isTokenBlacklisted(String value){
        return jwtTokenBlacklistRepository.findByJwtValue(value) != null;
    }

    public List<CartItem> findAllCartItemsByUser(String userEmail){
        User user = findUserByEmail(userEmail);
        List<CartItem> cartItems = new ArrayList<>();
        if(user != null){
            cartItems = cartItemRepository.findAllCartItemsByUser(user);
        }
        return cartItems.isEmpty() ? null : cartItems;
    }

//    public String processPayment(PaymentDTO paymentDTO, User user) {
//        RestTemplate restTemplate = new RestTemplate();
//
//        // Create headers with Authorization
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Authorization", "Bearer " + checkoutSecretKey);
//
//        // Payment data payload
//        Map<String, Object> paymentData = new HashMap<>();
//        paymentData.put("source", Map.of("type", "card",
//                "number", paymentDTO.getCardNumber(),
//                "expiry_month", paymentDTO.getExpiryMonth(),
//                "expiry_year", paymentDTO.getExpiryYear(),
//                "cvv", paymentDTO.getCvv()));
//        paymentData.put("amount", paymentDTO.getAmount());
//        paymentData.put("currency", "USD");
//        paymentData.put("reference", "order_reference_123");
//        paymentData.put("processing_channel_id", "pc_jju5utte4u5ehm3rhszfdlutdq");
//
//        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(paymentData, headers);
//
//        try{
//            // Send POST request to Checkout API
//            ResponseEntity<String> response = restTemplate.exchange(paymentUrl, HttpMethod.POST, entity, String.class);
//
//            // Handle response from Checkout.com API
//            if (response.getStatusCode() == HttpStatus.CREATED) {
//
//                Payment payment = new Payment(paymentDTO.getAmount(), user);
//                paymentRepository.save(payment);
//
//                // Process successful payment response and return it to frontend
//                return response.getBody();
//            } else {
//                return "Error: " + response.getBody();
//            }
//        }catch (Exception e){
//            return null;
//        }
//    }

    public void processPayment(PaymentDTO paymentDTO, User user) {
        try{
                Payment payment = new Payment(paymentDTO.getAmount(), user);
                paymentRepository.save(payment);
        }catch (Exception ignore){}
    }

    public boolean blacklistToken(String jwtToken) {
        JWTTokenBlacklist result = this.jwtTokenBlacklistRepository.save(new JWTTokenBlacklist(jwtToken));
        return result != null;
    }

    public List<DonePaymentsDTO> findUserPayments(User user) {
        List<DonePaymentsDTO> donePaymentsDTOS = new ArrayList<>();
        List<Payment> payments = paymentRepository.findByUser(user);

        if (!payments.isEmpty()){
            payments.forEach(payment -> donePaymentsDTOS.add(new DonePaymentsDTO(payment.getAmount(), payment.getPaymentDate())));
        }
        return donePaymentsDTOS;
    }

    @Transactional
    public void deleteAllCartItemsByUser(User user){
        this.cartItemRepository.deleteAllByUser(user);
    }

    public List<Payment> getAllPayments(){
        List<Payment> payments = paymentRepository.findAll();
        return payments.isEmpty() ? null : payments;
    }

    public List<ProductDTO> getAllProductsNotPaginated() {
        List<ProductDTO> productDTOS = new ArrayList<>();
        List<Product> products = productRepository.findAll();
        products.forEach(product -> productDTOS.add(ClassMapper.mapToProductDTO(product)));
        return productDTOS.isEmpty() ? null : productDTOS;
    }
}
