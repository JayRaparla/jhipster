package com.jay.statements.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Remark.
 */
@Entity
@Table(name = "remark")
public class Remark implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "content", nullable = false)
    private String content;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private ZonedDateTime date;

    @ManyToMany(mappedBy = "remarks")
    @JsonIgnore
    private Set<Transaction> transactions = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Remark title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public Remark content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Remark date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public Remark transactions(Set<Transaction> transactions) {
        this.transactions = transactions;
        return this;
    }

    public Remark addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.getRemarks().add(this);
        return this;
    }

    public Remark removeTransaction(Transaction transaction) {
        this.transactions.remove(transaction);
        transaction.getRemarks().remove(this);
        return this;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Remark remark = (Remark) o;
        if (remark.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, remark.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Remark{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", content='" + content + "'" +
            ", date='" + date + "'" +
            '}';
    }
}
