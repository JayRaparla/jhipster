package com.jay.statements.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jay.statements.domain.Wish;

import com.jay.statements.repository.WishRepository;
import com.jay.statements.web.rest.util.HeaderUtil;
import com.jay.statements.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Wish.
 */
@RestController
@RequestMapping("/api")
public class WishResource {

    private final Logger log = LoggerFactory.getLogger(WishResource.class);

    private static final String ENTITY_NAME = "wish";
        
    private final WishRepository wishRepository;

    public WishResource(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    /**
     * POST  /wishes : Create a new wish.
     *
     * @param wish the wish to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wish, or with status 400 (Bad Request) if the wish has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wishes")
    @Timed
    public ResponseEntity<Wish> createWish(@RequestBody Wish wish) throws URISyntaxException {
        log.debug("REST request to save Wish : {}", wish);
        if (wish.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new wish cannot already have an ID")).body(null);
        }
        Wish result = wishRepository.save(wish);
        return ResponseEntity.created(new URI("/api/wishes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wishes : Updates an existing wish.
     *
     * @param wish the wish to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wish,
     * or with status 400 (Bad Request) if the wish is not valid,
     * or with status 500 (Internal Server Error) if the wish couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/wishes")
    @Timed
    public ResponseEntity<Wish> updateWish(@RequestBody Wish wish) throws URISyntaxException {
        log.debug("REST request to update Wish : {}", wish);
        if (wish.getId() == null) {
            return createWish(wish);
        }
        Wish result = wishRepository.save(wish);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, wish.getId().toString()))
            .body(result);
    }

    /**
     * GET  /wishes : get all the wishes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of wishes in body
     */
    @GetMapping("/wishes")
    @Timed
    public ResponseEntity<List<Wish>> getAllWishes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Wishes");
        Page<Wish> page = wishRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/wishes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /wishes/:id : get the "id" wish.
     *
     * @param id the id of the wish to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wish, or with status 404 (Not Found)
     */
    @GetMapping("/wishes/{id}")
    @Timed
    public ResponseEntity<Wish> getWish(@PathVariable Long id) {
        log.debug("REST request to get Wish : {}", id);
        Wish wish = wishRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(wish));
    }

    /**
     * DELETE  /wishes/:id : delete the "id" wish.
     *
     * @param id the id of the wish to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/wishes/{id}")
    @Timed
    public ResponseEntity<Void> deleteWish(@PathVariable Long id) {
        log.debug("REST request to delete Wish : {}", id);
        wishRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
