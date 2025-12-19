package com.davonlibrary.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

/** Book entity representing books in the library system. */
@Entity
@Table(name = "books")
public class Book extends PanacheEntity {

  @NotBlank(message = "Title is required")
  @Size(max = 255, message = "Title must not exceed 255 characters")
  @Column(name = "title", nullable = false)
  public String title;

  @Pattern(
      regexp = "^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)[\\d-]+$",
      message = "Invalid ISBN format")
  @Column(name = "isbn", unique = true, length = 20)
  public String isbn;

  @Column(name = "available_copies")
  public Integer availableCopies = 1;

  @Column(name = "total_copies")
  public Integer totalCopies = 1;

  @NotNull(message = "Author is required")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id", nullable = false)
  public Author author;

  @JsonIgnore
  @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  public List<BookCopy> bookCopies;

  /**
   * Logical status of a Book aggregated from its copies.
   */
  public enum BookStatus {
    AVAILABLE,
    UNAVAILABLE
  }

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  public BookStatus status = BookStatus.UNAVAILABLE;

  /** Default constructor for JPA. */
  public Book() {}

  /**
   * Constructor with title and author (minimal information).
   *
   * @param title the book title
   * @param author the book author
   * @throws IllegalArgumentException if author is null
   */
  public Book(String title, Author author) {
    if (author == null) {
      throw new IllegalArgumentException("Author cannot be null");
    }
    this.title = title;
    this.author = author;
    this.availableCopies = 1;
    this.totalCopies = 1;
    this.status = BookStatus.AVAILABLE;
  }

  /**
   * Constructor with title, ISBN, author, and total copies.
   *
   * @param title the book title
   * @param isbn the ISBN
   * @param author the book author
   * @param totalCopies the total number of copies (must be non-negative, 0 is allowed)
   * @throws IllegalArgumentException if author is null or totalCopies is negative
   */
  public Book(String title, String isbn, Author author, int totalCopies) {
    if (author == null) {
      throw new IllegalArgumentException("Author cannot be null");
    }
    if (totalCopies < 0) {
      throw new IllegalArgumentException("Total copies cannot be negative");
    }
    this.title = title;
    this.isbn = isbn;
    this.author = author;
    this.totalCopies = totalCopies;
    this.availableCopies = totalCopies;
    this.status = totalCopies > 0 ? BookStatus.AVAILABLE : BookStatus.UNAVAILABLE;
  }

  /**
   * Expose persisted status.
   */
  public BookStatus getStatus() {
    return status;
  }

  /**
   * Allow manually overriding status.
   */
  public void setStatus(BookStatus status) {
    this.status = status;
  }

  /**
   * Checks if the book is available for borrowing.
   *
   * @return true if available copies > 0
   */
  public boolean isAvailable() {
    if (bookCopies == null || bookCopies.isEmpty()) {
      return false;
    }
    return bookCopies.stream().anyMatch(copy -> copy.status == BookCopy.BookCopyStatus.AVAILABLE);
  }

  /**
   * Borrows a copy of the book, if available.
   *
   * @return the borrowed BookCopy, or null if none are available
   */
  public BookCopy borrowCopy() {
    BookCopy copy = getAvailableCopy();
    if (copy != null) {
      copy.checkOut();
      availableCopies--;
    }
    return copy;
  }

  /**
   * Returns a book copy.
   *
   * @param copy the BookCopy to return
   */
  public void returnCopy(BookCopy copy) {
    if (copy != null && copy.book != null && copy.book.id.equals(this.id)) {
      if (availableCopies < totalCopies) {
        availableCopies++;
      }
    }
  }

  /**
   * Gets detailed information about the book.
   *
   * @return formatted book information
   */
  public String getDetailedInfo() {
    String authorName = (author != null) ? author.getFullName() : "Unknown";
    return String.format(
        "Title: %s, ISBN: %s, Author: %s, Available Copies: %d, Total Copies: %d",
        title, isbn, authorName, availableCopies, totalCopies);
  }

  /**
   * Gets all copies of this book.
   *
   * @return list of all book copies
   */
  public List<BookCopy> getAllCopies() {
    if (bookCopies == null) {
      return List.of();
    }
    return bookCopies;
  }

  /**
   * Gets current active reservations for this book.
   *
   * @return list of current reservations
   */
  public List<Reservation> getCurrentReservations() {
    return List.of(); // Simplified - no reservations relationship
  }

  public BookCopy getAvailableCopy() {
    if (bookCopies == null) {
      return null;
    }
    return bookCopies.stream()
        .filter(copy -> copy.status == BookCopy.BookCopyStatus.AVAILABLE)
        .findFirst()
        .orElse(null);
  }

  @Override
  public String toString() {
    return "Book{"
        + "id="
        + id
        + ", title='"
        + title
        + '\''
        + ", isbn='"
        + isbn
        + '\''
        + ", author="
        + (author != null ? author.getFullName() : "null")
        + ", availableCopies="
        + availableCopies
        + '}';
  }
}
