package dev.momory.moneymindbackend.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class PageResponseDTO<T> {

    private List<T> content;
    private int number;
    private int size;
    private long totalElements;
    private int totalPages;
}
