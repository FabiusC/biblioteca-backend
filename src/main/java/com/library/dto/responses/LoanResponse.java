package com.library.dto.responses;

import com.library.domain.enums.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanResponse {

    private Long id;
    private Long userId;
    private Long bookId;
    private Long copyId;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private LoanStatus status;
}