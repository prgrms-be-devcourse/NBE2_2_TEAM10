package com.prgrms2.java.bitta.jobpost.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageRequestDTO {

    @Builder.Default
    @Min(1)
    private int page = 1;

    @Builder.Default
    @Min(10)
    @Max(100)
    private int size = 10;

    public Pageable getPageable(Sort sort) {
        int pageNum = page < 0 ? 1 : page - 1;
        int sizeNum = size <= 10 ? 10 : size;

        return PageRequest.of(pageNum, sizeNum, sort);
    }
}
