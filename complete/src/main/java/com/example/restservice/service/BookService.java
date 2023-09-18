package com.example.restservice.service;

import java.util.HashSet;
import com.example.restservice.modal.Book;

public interface BookService {
    HashSet<Book> findAllBook();
    Book findBookByID(long id);
    void addBook(Book b);
    void deleteAllData();
}