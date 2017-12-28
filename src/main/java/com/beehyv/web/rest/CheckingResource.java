package com.beehyv.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.beehyv.domain.Checking;

import com.beehyv.repository.CheckingRepository;
import com.beehyv.web.rest.errors.BadRequestAlertException;
import com.beehyv.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Checking.
 */
@RestController
@RequestMapping("/api")
public class CheckingResource {

    private final Logger log = LoggerFactory.getLogger(CheckingResource.class);

    private static final String ENTITY_NAME = "checking";

    private final CheckingRepository checkingRepository;

    public CheckingResource(CheckingRepository checkingRepository) {
        this.checkingRepository = checkingRepository;
    }

    /**
     * POST  /checkings : Create a new checking.
     *
     * @param checking the checking to create
     * @return the ResponseEntity with status 201 (Created) and with body the new checking, or with status 400 (Bad Request) if the checking has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/checkings")
    @Timed
    public ResponseEntity<Checking> createChecking(@RequestBody Checking checking) throws URISyntaxException {
        log.debug("REST request to save Checking : {}", checking);
        if (checking.getId() != null) {
            throw new BadRequestAlertException("A new checking cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Checking result = checkingRepository.save(checking);
        return ResponseEntity.created(new URI("/api/checkings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /checkings : Updates an existing checking.
     *
     * @param checking the checking to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated checking,
     * or with status 400 (Bad Request) if the checking is not valid,
     * or with status 500 (Internal Server Error) if the checking couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/checkings")
    @Timed
    public ResponseEntity<Checking> updateChecking(@RequestBody Checking checking) throws URISyntaxException {
        log.debug("REST request to update Checking : {}", checking);
        if (checking.getId() == null) {
            return createChecking(checking);
        }
        Checking result = checkingRepository.save(checking);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, checking.getId().toString()))
            .body(result);
    }

    /**
     * GET  /checkings : get all the checkings.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of checkings in body
     */
    @GetMapping("/checkings")
    @Timed
    public List<Checking> getAllCheckings() {
        log.debug("REST request to get all Checkings");
        return checkingRepository.findAll();
        }

    /**
     * GET  /checkings/:id : get the "id" checking.
     *
     * @param id the id of the checking to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the checking, or with status 404 (Not Found)
     */
    @GetMapping("/checkings/{id}")
    @Timed
    public ResponseEntity<Checking> getChecking(@PathVariable Long id) {
        log.debug("REST request to get Checking : {}", id);
        Checking checking = checkingRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(checking));
    }

    /**
     * DELETE  /checkings/:id : delete the "id" checking.
     *
     * @param id the id of the checking to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/checkings/{id}")
    @Timed
    public ResponseEntity<Void> deleteChecking(@PathVariable Long id) {
        log.debug("REST request to delete Checking : {}", id);
        checkingRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
