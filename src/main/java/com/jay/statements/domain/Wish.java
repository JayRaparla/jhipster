package com.jay.statements.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Wish.
 */
@Entity
@Table(name = "wish")
public class Wish implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "added_on")
    private LocalDate addedOn;

    @Column(name = "complete_by")
    private ZonedDateTime completeBy;

    @ManyToOne
    private WishList wishList;

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Wish description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getAddedOn() {
        return addedOn;
    }

    public Wish addedOn(LocalDate addedOn) {
        this.addedOn = addedOn;
        return this;
    }

    public void setAddedOn(LocalDate addedOn) {
        this.addedOn = addedOn;
    }

    public ZonedDateTime getCompleteBy() {
        return completeBy;
    }

    public Wish completeBy(ZonedDateTime completeBy) {
        this.completeBy = completeBy;
        return this;
    }

    public void setCompleteBy(ZonedDateTime completeBy) {
        this.completeBy = completeBy;
    }

    public WishList getWishList() {
        return wishList;
    }

    public Wish wishList(WishList wishList) {
        this.wishList = wishList;
        return this;
    }

    public void setWishList(WishList wishList) {
        this.wishList = wishList;
    }

    public User getUser() {
        return user;
    }

    public Wish user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Wish wish = (Wish) o;
        if (wish.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, wish.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Wish{" +
            "id=" + id +
            ", description='" + description + "'" +
            ", addedOn='" + addedOn + "'" +
            ", completeBy='" + completeBy + "'" +
            '}';
    }
}
