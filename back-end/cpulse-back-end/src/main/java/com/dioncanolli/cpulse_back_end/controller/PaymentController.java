package com.dioncanolli.cpulse_back_end.controller;

import com.dioncanolli.cpulse_back_end.dto.DonePaymentsDTO;
import com.dioncanolli.cpulse_back_end.dto.ModifiedPaymentDTO;
import com.dioncanolli.cpulse_back_end.entity.Payment;
import com.dioncanolli.cpulse_back_end.entity.User;
import com.dioncanolli.cpulse_back_end.service.MyService;
import com.dioncanolli.cpulse_back_end.utility.JWTTokenGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PaymentController {

    private final MyService myService;

    public PaymentController(MyService myService) {
        this.myService = myService;
    }

//    @PostMapping("/payments/process")
//    public ResponseEntity<Boolean> processPayment(@RequestBody PaymentDTO paymentDTO, @RequestHeader(value = "Authorization") String jwtToken) {
//        String email = JWTTokenGenerator.extractUsernameFromToken(jwtToken);
//        User user = myService.findUserByEmail(email);
//        String paymentResponse = myService.processPayment(paymentDTO, user);
//        if (paymentResponse == null){
//            return new ResponseEntity<>(false, HttpStatus.OK);
//        }
//        boolean isApproved = paymentResponse.contains("\"approved\":true");
//        return isApproved ? new ResponseEntity<>(true, HttpStatus.OK) : new ResponseEntity<>(false, HttpStatus.OK);
//    }

//    @PostMapping("/payments/process")
//    public ResponseEntity<Boolean> processPayment(@RequestBody PaymentDTO paymentDTO,
//                                                  @RequestHeader(value = "Authorization") String jwtToken) {
//        try {
//            String email = JWTTokenGenerator.extractUsernameFromToken(jwtToken);
//            User user = myService.findUserByEmail(email);
//            myService.processPayment(paymentDTO, user);
//        } catch(Exception exception){
//            return new ResponseEntity<>(false, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(true, HttpStatus.OK);
//    }

    @GetMapping(value = "/payments/find")
    public ResponseEntity<List<DonePaymentsDTO>> findUserPayments(@RequestHeader(value = "Authorization") String jwtToken) {
        List<DonePaymentsDTO> donePaymentsDTOS = new ArrayList<>();
        String email = JWTTokenGenerator.extractUsernameFromToken(jwtToken);
        User user = myService.findUserByEmail(email);
        if (user != null){
           donePaymentsDTOS = myService.findUserPayments(user);
        }
        return donePaymentsDTOS.isEmpty() ?
                new ResponseEntity<>(null, HttpStatus.NOT_FOUND) : new ResponseEntity<>(donePaymentsDTOS, HttpStatus.OK);
    }

    @GetMapping(value = "/admin/payments/find/all")
    public ResponseEntity<List<ModifiedPaymentDTO>> findAllPayments(@RequestHeader(value = "Authorization") String jwtToken) {
        List<Payment> payments = myService.getAllPayments();
        List<ModifiedPaymentDTO> modifiedPaymentDTOS = new ArrayList<>();
        if (payments != null){
            payments.forEach(payment -> modifiedPaymentDTOS.add(
                    new ModifiedPaymentDTO(payment.getUser().getEmail(), payment.getAmount(), payment.getPaymentDate())));
        }
        return modifiedPaymentDTOS.isEmpty() ?
                new ResponseEntity<>(null, HttpStatus.NOT_FOUND) : new ResponseEntity<>(modifiedPaymentDTOS, HttpStatus.OK);
    }
}
