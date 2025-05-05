package org.example.demo.models;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class Transaction {
    private long id;
    private User user;
    private LocalDate date;
    private BigDecimal amount;
    private String note;
    private String mainCategory;
    private String subCategory;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
