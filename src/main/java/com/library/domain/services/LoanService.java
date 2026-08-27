package com.library.domain.services;

import com.library.domain.entities.Book;
import com.library.domain.entities.Copy;
import com.library.domain.entities.Loan;
import com.library.domain.entities.User;
import com.library.domain.enums.CopyStatus;
import com.library.domain.enums.LoanStatus;
import com.library.domain.exceptions.BusinessRuleException;
import com.library.domain.exceptions.ResourceNotFoundException;
import com.library.domain.repositories.CopyRepository;
import com.library.domain.repositories.LoanRepository;
import com.library.domain.repositories.UserRepository;
import com.library.dto.requests.LoanUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private static final int DEFAULT_LOAN_DAYS = 15;

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final CopyRepository copyRepository;

    @Transactional
    public Loan createLoan(Long userId, Long copyId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
        Copy copy = copyRepository.findById(copyId)
                .orElseThrow(() -> new ResourceNotFoundException("Copy not found with id " + copyId));

        long activeLoans = loanRepository.countByUserAndStatus(user, LoanStatus.ACTIVE);
        if (activeLoans > 0) {
            throw new BusinessRuleException("The user already has active loans");
        }

        if (copy.getStatus() != CopyStatus.AVAILABLE) {
            throw new BusinessRuleException("The copy is not available for loan");
        }

        Book book = copy.getBook();
        Loan loan = Loan.builder()
                .user(user)
                .book(book)
                .copy(copy)
                .loanDate(LocalDate.now())
                .returnDate(LocalDate.now().plusDays(DEFAULT_LOAN_DAYS))
                .status(LoanStatus.ACTIVE)
                .build();

        copy.setStatus(CopyStatus.LOANED);
        copyRepository.save(copy);

        return loanRepository.save(loan);
    }

    @Transactional(readOnly = true)
    public List<Loan> findByUserIdAndBookId(Long userId, Long bookId) {
        return loanRepository.findByUserIdAndBookId(userId, bookId);
    }

    @Transactional(readOnly = true)
    public List<Loan> findByUserId(Long userId) {
        return loanRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Loan> findByBookId(Long bookId) {
        return loanRepository.findByBookId(bookId);
    }

    @Transactional
    public Loan updateLoan(Long id, LoanUpdateRequest request) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id " + id));

        LoanStatus previousStatus = loan.getStatus();

        if (request.getReturnDate() != null) {
            loan.setReturnDate(request.getReturnDate());
        }
        if (request.getStatus() != null) {
            loan.setStatus(request.getStatus());
        }

        if (previousStatus != LoanStatus.RETURNED && loan.getStatus() == LoanStatus.RETURNED) {
            Copy copy = loan.getCopy();
            copy.setStatus(CopyStatus.AVAILABLE);
            copyRepository.save(copy);
        }

        return loanRepository.save(loan);
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void markOverdueLoans() {
        List<Loan> overdueLoans = loanRepository.findByStatusAndReturnDateBefore(LoanStatus.ACTIVE, LocalDate.now());
        for (Loan loan : overdueLoans) {
            loan.setStatus(LoanStatus.OVERDUE);
        }
        loanRepository.saveAll(overdueLoans);
    }
}