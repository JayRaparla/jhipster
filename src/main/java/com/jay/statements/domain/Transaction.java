package com.jay.statements.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Transaction.
 */
@Entity
@Table(name = "transaction")
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private String date;

    @Column(name = "description")
    private String description;

    @Column(name = "amount")
    private String amount;

    @ManyToOne
    private User user;

    @ManyToMany
    @JoinTable(name = "transaction_tag",
               joinColumns = @JoinColumn(name="transactions_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="tags_id", referencedColumnName="id"))
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "transaction_remark",
               joinColumns = @JoinColumn(name="transactions_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="remarks_id", referencedColumnName="id"))
    private Set<Remark> remarks = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public Transaction date(String date) {
        this.date = date;
        return this;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public Transaction description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public Transaction amount(String amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public Transaction user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Transaction tags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public Transaction addTag(Tag tag) {
        this.tags.add(tag);
        tag.getTransactions().add(this);
        return this;
    }

    public Transaction removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getTransactions().remove(this);
        return this;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<Remark> getRemarks() {
        return remarks;
    }

    public Transaction remarks(Set<Remark> remarks) {
        this.remarks = remarks;
        return this;
    }

    public Transaction addRemark(Remark remark) {
        this.remarks.add(remark);
        remark.getTransactions().add(this);
        return this;
    }

    public Transaction removeRemark(Remark remark) {
        this.remarks.remove(remark);
        remark.getTransactions().remove(this);
        return this;
    }

    public void setRemarks(Set<Remark> remarks) {
        this.remarks = remarks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Transaction transaction = (Transaction) o;
        if (transaction.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, transaction.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Transaction{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", description='" + description + "'" +
            ", amount='" + amount + "'" +
            '}';
    }
}
