package com.dioncanolli.cpulse_back_end.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class DonePaymentsDTO {
    private double amount;
    private Timestamp paymentDate;
}
