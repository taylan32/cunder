package com.example.cunder.utils;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

@Data
public class BasePageableModel<T> {

    private int pageNumber;
    private int pageSize;
    private Sort sort;
    private int totalPages;
    private long totalElements;
    private List<T> elements;
    private boolean hasNext;
    private boolean hasPrevious;

    public BasePageableModel(Page page, List<T> list) {
        this.pageNumber = page.getNumber() + 1;
        this.pageSize = page.getSize();
        this.sort = page.getSort();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.elements = list;
        this.hasNext = pageNumber < totalPages ? true : false;
        this.hasPrevious = pageNumber > 1 ? true : false;

    }


}
