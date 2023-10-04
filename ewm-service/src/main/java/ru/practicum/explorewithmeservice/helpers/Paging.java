package ru.practicum.explorewithmeservice.helpers;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@NoArgsConstructor
public class Paging {
    public Pageable getPageable(int start, int pageSize) {
        return PageRequest.of(start / pageSize, pageSize);
    }

    public Pageable getPageable(int start, int pageSize, String sortField) {
        return PageRequest.of(start / pageSize, pageSize, Sort.by(sortField).descending());
    }
}
