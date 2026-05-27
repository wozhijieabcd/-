package com.example.blog.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageResult<T> {
    private List<T> records;
    private int total;
    private int page;
    private int size;

    public int getTotalPages() {
        return total == 0 ? 0 : (int) Math.ceil((double) total / size);
    }
}
