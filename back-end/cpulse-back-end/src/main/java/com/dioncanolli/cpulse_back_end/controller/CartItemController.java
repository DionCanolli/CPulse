package com.dioncanolli.cpulse_back_end.controller;

import com.dioncanolli.cpulse_back_end.dto.ProductDTO;
import com.dioncanolli.cpulse_back_end.entity.CartItem;
import com.dioncanolli.cpulse_back_end.entity.Product;
import com.dioncanolli.cpulse_back_end.entity.User;
import com.dioncanolli.cpulse_back_end.service.MyService;
import com.dioncanolli.cpulse_back_end.utility.ClassMapper;
import com.dioncanolli.cpulse_back_end.utility.JWTTokenGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/cart")
public class CartItemController {

    private final MyService myService;

    public CartItemController(MyService myService) {
        this.myService = myService;
    }

    @GetMapping(value = "/user")
    public ResponseEntity<List<ProductDTO>> findAllCartItemsByUser(@RequestHeader(value = "Authorization") String jwtToken){
        List<CartItem> cartItems = myService.findAllCartItemsByUser(JWTTokenGenerator.extractUsernameFromToken(jwtToken));
        if (cartItems != null){
            if(!cartItems.isEmpty()){
                List<ProductDTO> productDTOS = new ArrayList<>();
                cartItems.forEach(cartItem -> productDTOS.add(ClassMapper.mapToProductDTO(cartItem.getProduct())));
                return productDTOS.isEmpty() ?
                        new ResponseEntity<>(null, HttpStatus.NOT_FOUND) : new ResponseEntity<>(productDTOS, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/insert")
    public ResponseEntity<Boolean> insertCartItem(@RequestHeader(value = "Authorization") String jwtToken, @RequestParam String productName){
        if (jwtToken != null) {
            String email = JWTTokenGenerator.extractUsernameFromToken(jwtToken);
            User user = myService.findUserByEmail(email);
            boolean result = myService.insertCartItem(user, productName);
            return !result ? new ResponseEntity<>(false, HttpStatus.BAD_REQUEST) : new ResponseEntity<>(true, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(false, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<Boolean> deleteCartItem(@RequestHeader(value = "Authorization") String jwtToken, @RequestParam String productName){
        if (jwtToken != null) {
            String email = JWTTokenGenerator.extractUsernameFromToken(jwtToken);
            User user = myService.findUserByEmail(email);
            boolean result = myService.deleteCartItem(user, productName);
            return !result ? new ResponseEntity<>(false, HttpStatus.BAD_REQUEST) : new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/delete/all")
    public ResponseEntity<Boolean> deleteAllUserCartItems(@RequestHeader(value = "Authorization") String jwtToken){
        try{
            if (jwtToken != null) {
                String email = JWTTokenGenerator.extractUsernameFromToken(jwtToken);
                User user = myService.findUserByEmail(email);
                myService.deleteAllCartItemsByUser(user);
                return new ResponseEntity<>(true, HttpStatus.OK);
            }
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/item/exists")
    public ResponseEntity<Boolean> cartItemExists(@RequestHeader(value = "Authorization") String jwtToken,
                                                      @RequestParam String productName){
        User user = this.myService.findUserByEmail(JWTTokenGenerator.extractUsernameFromToken(jwtToken));
        Product product = this.myService.findProductByProductName(productName);
        boolean result = this.myService.cartItemUserAndProductExists(user, product);
        return result ? new ResponseEntity<>(true, HttpStatus.OK) : new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }
}
























