package br.com.achimid.api.avtklab.controller;

import br.com.achimid.api.avtklab.controller.documentation.EventControllerDoc;
import br.com.achimid.api.avtklab.dto.EventDTO;
import br.com.achimid.api.avtklab.model.Event;
import br.com.achimid.api.avtklab.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/event")
public class EventController implements EventControllerDoc {

    @Autowired
    private EventService eventService;

    @GetMapping
    public HttpEntity<Iterable<Event>> all() {
        return ResponseEntity.ok(eventService.findAll());
    }

    @GetMapping("/{id}")
    public HttpEntity<?> get(@PathVariable Integer id) {
        if(!eventService.exists(id)) return ResponseEntity.notFound().build();
        try {
            return ResponseEntity.ok(eventService.findById(id));
        }catch (IllegalStateException ie){
            return ResponseEntity.badRequest().body(ie.getMessage());
        }
    }

    @PostMapping
    public HttpEntity<Event> create(@Valid @RequestBody EventDTO event) {
        return ResponseEntity.ok(eventService.save(event.getEvent()));
    }

    @PutMapping
    public HttpEntity<?> update(@Validated @RequestBody EventDTO event) {
        if(event.getId() == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(eventService.save(event.getEvent()));
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Integer id) {
        if(!eventService.exists(id)) return ResponseEntity.notFound().build();
        eventService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public void handleBadRequests(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}
