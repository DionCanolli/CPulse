package com.dioncanolli.cpulse_back_end.controller;

import com.dioncanolli.cpulse_back_end.dto.ProductDTO;
import com.dioncanolli.cpulse_back_end.entity.Product;
import com.dioncanolli.cpulse_back_end.entity.User;
import com.dioncanolli.cpulse_back_end.entity.WishlistItem;
import com.dioncanolli.cpulse_back_end.service.MyService;
import com.dioncanolli.cpulse_back_end.utility.ClassMapper;
import com.dioncanolli.cpulse_back_end.utility.JWTTokenGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/wishlist")
public class WishlistItemController {

    private final MyService myService;

    public WishlistItemController(MyService myService) {
        this.myService = myService;
    }

    @GetMapping(value = "/user")
    public ResponseEntity<List<ProductDTO>> findAllUserWishlistProducts(@RequestHeader(value = "Authorization") String jwtToken){
        List<WishlistItem> wishlistItems = myService.findAllWishlistItemsByUser(JWTTokenGenerator.extractUsernameFromToken(jwtToken));
        if (wishlistItems != null){
            if(!wishlistItems.isEmpty()){
                List<ProductDTO> productDTOS = new ArrayList<>();
                wishlistItems.forEach(wishlistItem -> productDTOS.add(ClassMapper.mapToProductDTO(wishlistItem.getProduct())));
                return productDTOS.isEmpty() ?
                        new ResponseEntity<>(null, HttpStatus.NOT_FOUND) : new ResponseEntity<>(productDTOS, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/insert")
    public ResponseEntity<Boolean> insertWishlistItem(@RequestHeader(required = false, value = "Authorization") String jwtToken,
                                                      @RequestParam String productName){
        boolean result = false;
        if (jwtToken != null){
            String email = JWTTokenGenerator.extractUsernameFromToken(jwtToken);
            User user = myService.findUserByEmail(email);
            if (user != null){
                result = myService.insertWishtlistItem(user, productName);
            }
        }
        return !result ?
                new ResponseEntity<>(false, HttpStatus.BAD_REQUEST) : new ResponseEntity<>(true, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<Boolean> deleteWishlistItem(@RequestHeader(required = false, value = "Authorization") String jwtToken,
                                                      @RequestParam String productName){
        boolean result = false;
        if (jwtToken != null){
            result = myService.deleteWishlistItem(JWTTokenGenerator.extractUsernameFromToken(jwtToken), productName);
        }
        return !result ?
                new ResponseEntity<>(false, HttpStatus.BAD_REQUEST) : new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping(value = "/item/exists")
    public ResponseEntity<Boolean> wishlistItemExists(@RequestHeader(value = "Authorization") String jwtToken,
                                                      @RequestParam String productName){
        User user = this.myService.findUserByEmail(JWTTokenGenerator.extractUsernameFromToken(jwtToken));
        Product product = this.myService.findProductByProductName(productName);
        boolean result = this.myService.wishlistItemUserAndProductExists(user, product);
        return result ? new ResponseEntity<>(true, HttpStatus.OK) : new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }
}
























