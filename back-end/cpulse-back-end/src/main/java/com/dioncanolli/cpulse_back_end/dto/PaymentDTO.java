package com.dioncanolli.cpulse_back_end.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentDTO {
    private String cardNumber;
    private String expiryMonth;
    private String expiryYear;
    private String cvv;
    private double amount;
}

//@Builder
//public class PaymentDTO {
//    private String cardNumber;
//    private String cardHolderName;
//    private String expiryMonth;
//    private String expiryYear;
//    private String cvv;
//    private String billingAddress;
//    private int zipCode;
//    private String country;
//    private double amount;
//    private String userEmail;
//
//    public PaymentDTO(String cardNumber, String cardHolderName, String expiryMonth, String expiryYear, String cvv, String billingAddress, int zipCode, String country, double amount) {
//        this.cardNumber = cardNumber;
//        this.cardHolderName = cardHolderName;
//        this.expiryMonth = expiryMonth;
//        this.expiryYear = expiryYear;
//        this.cvv = cvv;
//        this.billingAddress = billingAddress;
//        this.zipCode = zipCode;
//        this.country = country;
//        this.amount = amount;
//    }
//
//    public PaymentDTO(String cardNumber, String cardHolderName, String expiryMonth, String expiryYear, String cvv, String billingAddress, int zipCode, String country, double amount, String userEmail) {
//        this.cardNumber = cardNumber;
//        this.cardHolderName = cardHolderName;
//        this.expiryMonth = expiryMonth;
//        this.expiryYear = expiryYear;
//        this.cvv = cvv;
//        this.billingAddress = billingAddress;
//        this.zipCode = zipCode;
//        this.country = country;
//        this.amount = amount;
//        this.userEmail = userEmail;
//    }
//
//    public PaymentDTO() {
//    }
//
//
//
//    public String getCardNumber() {
//        return cardNumber;
//    }
//
//    public void setCardNumber(String cardNumber) {
//        this.cardNumber = cardNumber;
//    }
//
//    public String getCardHolderName() {
//        return cardHolderName;
//    }
//
//    public void setCardHolderName(String cardHolderName) {
//        this.cardHolderName = cardHolderName;
//    }
//
//    public String getExpiryMonth() {
//        return expiryMonth;
//    }
//
//    public void setExpiryMonth(String expiryMonth) {
//        this.expiryMonth = expiryMonth;
//    }
//
//    public String getExpiryYear() {
//        return expiryYear;
//    }
//
//    public void setExpiryYear(String expiryYear) {
//        this.expiryYear = expiryYear;
//    }
//
//    public String getCvv() {
//        return cvv;
//    }
//
//    public void setCvv(String cvv) {
//        this.cvv = cvv;
//    }
//
//    public String getBillingAddress() {
//        return billingAddress;
//    }
//
//    public void setBillingAddress(String billingAddress) {
//        this.billingAddress = billingAddress;
//    }
//
//    public int getZipCode() {
//        return zipCode;
//    }
//
//    public void setZipCode(int zipCode) {
//        this.zipCode = zipCode;
//    }
//
//    public String getCountry() {
//        return country;
//    }
//
//    public void setCountry(String country) {
//        this.country = country;
//    }
//
//    public double getAmount() {
//        return amount;
//    }
//
//    public void setAmount(double amount) {
//        this.amount = amount;
//    }
//
//    public String getUserEmail() {
//        return userEmail;
//    }
//
//    public void setUserEmail(String userEmail) {
//        this.userEmail = userEmail;
//    }
//}
