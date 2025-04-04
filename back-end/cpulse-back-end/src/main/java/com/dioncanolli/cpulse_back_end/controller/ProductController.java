package com.dioncanolli.cpulse_back_end.controller;

import com.dioncanolli.cpulse_back_end.dto.ProductDTO;
import com.dioncanolli.cpulse_back_end.service.MyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "/admin/products")
public class ProductController {

    private final MyService myService;

    public ProductController(MyService myService) {
        this.myService = myService;
    }

    @PostMapping(value = "/insert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Boolean> insertProduct(@RequestPart(value = "productDTO") ProductDTO productDTO,
                                                 @RequestPart(value = "productImage") MultipartFile productImage){
        boolean result = myService.insertProduct(productDTO, productImage);
        return !result ?
                new ResponseEntity<>(false, HttpStatus.BAD_REQUEST) : new ResponseEntity<>(true, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<Boolean> deleteProduct(@RequestParam String productName) throws IOException {
        boolean result = myService.deleteProduct(productName);
        return !result ?
                new ResponseEntity<>(false, HttpStatus.BAD_REQUEST) : new ResponseEntity<>(true, HttpStatus.OK);
    }
}














