package com.jay.statements.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jay.statements.domain.Transaction;

import com.jay.statements.repository.TransactionRepository;
import com.jay.statements.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Transaction.
 */
@RestController
@RequestMapping("/api")
public class TransactionResource {

    private final Logger log = LoggerFactory.getLogger(TransactionResource.class);

    private static final String ENTITY_NAME = "transaction";
        
    private final TransactionRepository transactionRepository;

    public TransactionResource(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * POST  /transactions : Create a new transaction.
     *
     * @param transaction the transaction to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transaction, or with status 400 (Bad Request) if the transaction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/transactions")
    @Timed
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody Transaction transaction) throws URISyntaxException {
        log.debug("REST request to save Transaction : {}", transaction);
        if (transaction.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new transaction cannot already have an ID")).body(null);
        }
        Transaction result = transactionRepository.save(transaction);
        return ResponseEntity.created(new URI("/api/transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /transactions : Updates an existing transaction.
     *
     * @param transaction the transaction to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transaction,
     * or with status 400 (Bad Request) if the transaction is not valid,
     * or with status 500 (Internal Server Error) if the transaction couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/transactions")
    @Timed
    public ResponseEntity<Transaction> updateTransaction(@Valid @RequestBody Transaction transaction) throws URISyntaxException {
        log.debug("REST request to update Transaction : {}", transaction);
        if (transaction.getId() == null) {
            return createTransaction(transaction);
        }
        Transaction result = transactionRepository.save(transaction);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transaction.getId().toString()))
            .body(result);
    }

    /**
     * GET  /transactions : get all the transactions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of transactions in body
     */
    @GetMapping("/transactions")
    @Timed
    public List<Transaction> getAllTransactions() {
        log.debug("REST request to get all Transactions");
        List<Transaction> transactions = transactionRepository.findAllWithEagerRelationships();
        return transactions;
    }

    /**
     * GET  /transactions/:id : get the "id" transaction.
     *
     * @param id the id of the transaction to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transaction, or with status 404 (Not Found)
     */
    @GetMapping("/transactions/{id}")
    @Timed
    public ResponseEntity<Transaction> getTransaction(@PathVariable Long id) {
        log.debug("REST request to get Transaction : {}", id);
        Transaction transaction = transactionRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(transaction));
    }

    /**
     * DELETE  /transactions/:id : delete the "id" transaction.
     *
     * @param id the id of the transaction to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/transactions/{id}")
    @Timed
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        log.debug("REST request to delete Transaction : {}", id);
        transactionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}