package com.library.dto.responses;

import com.library.domain.enums.CopyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CopyResponse {

    private Long id;
    private Long bookId;
    private String isbn;
    private CopyStatus status;
}