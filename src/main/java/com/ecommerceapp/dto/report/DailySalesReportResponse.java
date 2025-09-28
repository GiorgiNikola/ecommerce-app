package com.ecommerceapp.dto.report;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DailySalesReportResponse {
    private Long id;
    private Long storeId;
    private String storeName;
    private LocalDate reportDate;
    private double totalSales;
}
