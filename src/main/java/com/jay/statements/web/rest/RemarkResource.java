package com.jay.statements.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jay.statements.domain.Remark;

import com.jay.statements.repository.RemarkRepository;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Remark.
 */
@RestController
@RequestMapping("/api")
public class RemarkResource {

    private final Logger log = LoggerFactory.getLogger(RemarkResource.class);

    private static final String ENTITY_NAME = "remark";
        
    private final RemarkRepository remarkRepository;

    public RemarkResource(RemarkRepository remarkRepository) {
        this.remarkRepository = remarkRepository;
    }

    /**
     * POST  /remarks : Create a new remark.
     *
     * @param remark the remark to create
     * @return the ResponseEntity with status 201 (Created) and with body the new remark, or with status 400 (Bad Request) if the remark has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/remarks")
    @Timed
    public ResponseEntity<Remark> createRemark(@Valid @RequestBody Remark remark) throws URISyntaxException {
        log.debug("REST request to save Remark : {}", remark);
        if (remark.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new remark cannot already have an ID")).body(null);
        }
        Remark result = remarkRepository.save(remark);
        return ResponseEntity.created(new URI("/api/remarks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /remarks : Updates an existing remark.
     *
     * @param remark the remark to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated remark,
     * or with status 400 (Bad Request) if the remark is not valid,
     * or with status 500 (Internal Server Error) if the remark couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/remarks")
    @Timed
    public ResponseEntity<Remark> updateRemark(@Valid @RequestBody Remark remark) throws URISyntaxException {
        log.debug("REST request to update Remark : {}", remark);
        if (remark.getId() == null) {
            return createRemark(remark);
        }
        Remark result = remarkRepository.save(remark);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, remark.getId().toString()))
            .body(result);
    }

    /**
     * GET  /remarks : get all the remarks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of remarks in body
     */
    @GetMapping("/remarks")
    @Timed
    public ResponseEntity<List<Remark>> getAllRemarks(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Remarks");
        Page<Remark> page = remarkRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/remarks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /remarks/:id : get the "id" remark.
     *
     * @param id the id of the remark to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the remark, or with status 404 (Not Found)
     */
    @GetMapping("/remarks/{id}")
    @Timed
    public ResponseEntity<Remark> getRemark(@PathVariable Long id) {
        log.debug("REST request to get Remark : {}", id);
        Remark remark = remarkRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(remark));
    }

    /**
     * DELETE  /remarks/:id : delete the "id" remark.
     *
     * @param id the id of the remark to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/remarks/{id}")
    @Timed
    public ResponseEntity<Void> deleteRemark(@PathVariable Long id) {
        log.debug("REST request to delete Remark : {}", id);
        remarkRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
