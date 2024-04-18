package server.api.controllers;


import commons.Participant;
import org.hibernate.Hibernate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import server.api.services.ParticipantService;
@RestController
@RequestMapping("/api/participants")
public class ParticipantController {
    private final ParticipantService participantService;
    private final SimpMessagingTemplate smt;

    /**
     * Creates a new ParticipantController instance
     * @param participantService the participantService instance used
     * @param smt the mesaging template used
     */
    public ParticipantController(ParticipantService participantService, SimpMessagingTemplate smt) {
        this.participantService = participantService;
        this.smt = smt;
    }

    /**
     * Creates new participant
     * @param participant the participant to be added
     * @return saved participant
     */
    @PostMapping(path = {"","/"})
    public ResponseEntity<Participant> add(@RequestBody Participant participant){
        Participant saved = participantService.add(participant);
        if(saved==null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(saved);
    }

    /**
     * Updates a new participant
     * @param id id of participant to modify
     * @param participant participant to change
     * @return updated participant
     */
    @PutMapping(path = {"/{id}"})
    public ResponseEntity<Participant> put(@PathVariable("id") long id, @RequestBody Participant participant){
        Participant update = participantService.put(id,participant);
        if(update==null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(update);
    }

    /**
     * Deletes a participant
     * @param id participant to delete
     * @return the deleted participant
     */
    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<Participant> delete(@PathVariable("id") long id){
        Participant deleted = participantService.delete(id);
        if(deleted==null) return ResponseEntity.badRequest().build();
        smt.convertAndSend("/topic/participants", Hibernate.unproxy(deleted));
        return ResponseEntity.ok(deleted);
    }
}
