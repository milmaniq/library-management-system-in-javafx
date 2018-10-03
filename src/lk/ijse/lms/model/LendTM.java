/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.lms.model;

/**
 *
 * @author Ilman Iqbal
 */
public class LendTM {
    private String isbn;
    private String name;
    private String author;
    private String publisher;
    private String lendDate;
    private String returned;

    public LendTM() {
    }

    public LendTM(String isbn, String name, String author, String publisher, String lendDate, String returned) {
        this.isbn = isbn;
        this.name = name;
        this.author = author;
        this.publisher = publisher;
        this.lendDate = lendDate;
        this.returned = returned;
    }

    
    /**
     * @return the isbn
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * @param isbn the isbn to set
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return the publisher
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * @param publisher the publisher to set
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * @return the lendDate
     */
    public String getLendDate() {
        return lendDate;
    }

    /**
     * @param lendDate the lendDate to set
     */
    public void setLendDate(String lendDate) {
        this.lendDate = lendDate;
    }

    /**
     * @return the returned
     */
    public String getReturned() {
        return returned;
    }

    /**
     * @param returned the returned to set
     */
    public void setReturned(String returned) {
        this.returned = returned;
    }

   
}
