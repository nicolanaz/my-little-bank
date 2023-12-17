package com.example.mylittlebank.repository;

import com.example.mylittlebank.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(value = "SELECT t.* " +
            "FROM transaction t " +
            "JOIN accounts a ON t.account = a.account_number " +
            "JOIN users u ON a.owner = u.id " +
            "WHERE u.id = :userId " +
            "AND t.date_time BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<Transaction> findTransactionsForUserInPeriod(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
