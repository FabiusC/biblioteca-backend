package com.library.controllers;

import com.library.domain.entities.Loan;
import com.library.domain.services.LoanService;
import com.library.dto.requests.LoanCreateRequest;
import com.library.dto.requests.LoanUpdateRequest;
import com.library.dto.responses.LoanResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    public ResponseEntity<LoanResponse> createLoan(@Valid @RequestBody LoanCreateRequest request) {
        Loan createdLoan = loanService.createLoan(request.getUserId(), request.getCopyId());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(createdLoan));
    }

    @GetMapping("/search")
    public ResponseEntity<List<LoanResponse>> search(@RequestParam Long userId, @RequestParam Long bookId) {
        return ResponseEntity
                .ok(loanService.findByUserIdAndBookId(userId, bookId).stream().map(this::toResponse).toList());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LoanResponse>> findByUser(@PathVariable Long userId) {
        return ResponseEntity
                .ok(loanService.findByUserId(userId).stream().map(this::toResponse).toList());
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<LoanResponse>> findByBook(@PathVariable Long bookId) {
        return ResponseEntity
                .ok(loanService.findByBookId(bookId).stream().map(this::toResponse).toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoanResponse> updateLoan(@PathVariable Long id,
                                                  @Valid @RequestBody LoanUpdateRequest request) {
        Loan updatedLoan = loanService.updateLoan(id, request);
        return ResponseEntity.ok(toResponse(updatedLoan));
    }

    private LoanResponse toResponse(Loan loan) {
        return LoanResponse.builder()
                .id(loan.getId())
                .userId(loan.getUser().getId())
                .bookId(loan.getBook().getId())
                .copyId(loan.getCopy().getId())
                .loanDate(loan.getLoanDate())
                .returnDate(loan.getReturnDate())
                .status(loan.getStatus())
                .build();
    }
}