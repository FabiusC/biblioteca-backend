package com.library.dto.requests;

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
public class LoanUpdateRequest {

    private LocalDate returnDate;

    private LoanStatus status;
}
