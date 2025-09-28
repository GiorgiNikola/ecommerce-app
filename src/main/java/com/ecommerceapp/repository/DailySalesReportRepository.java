package com.ecommerceapp.repository;

import com.ecommerceapp.model.DailySalesReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailySalesReportRepository extends JpaRepository<DailySalesReport, Long> {
    List<DailySalesReport> findByStoreId(Long storeId);
    Optional<DailySalesReport> findByStoreIdAndReportDate(Long storeId, LocalDate reportDate);
}
