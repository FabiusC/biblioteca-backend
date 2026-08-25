package com.library.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String isbn;

    @NotNull
    private Integer edition;

    @NotNull
    private LocalDate publicationDate;

    @NotBlank
    private String author;
}