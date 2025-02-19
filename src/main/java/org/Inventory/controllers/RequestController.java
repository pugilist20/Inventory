package org.Inventory.controllers;

import lombok.RequiredArgsConstructor;
import org.Inventory.dto.ReqRequestDTO;
import org.Inventory.models.Request;
import org.Inventory.services.RequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<Request>> getRequests() {
        return ResponseEntity.ok(requestService.getAllRequests());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Request> getRequestById(@PathVariable UUID id) {
        Optional<Request> request = requestService.getRequestById(id);
        return request.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Request>> getRequestsByUser(@PathVariable UUID userId) {
        List<Request> requests = requestService.getRequestsByUser(userId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/item/{itemId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Request>> getRequestsByItem(@PathVariable UUID itemId) {
        List<Request> requests = requestService.getRequestsByItem(itemId);
        return ResponseEntity.ok(requests);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Request> createRequest(@RequestBody ReqRequestDTO request) {
        return ResponseEntity.ok(requestService.createRequest(request));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Request> approveRequest(@PathVariable UUID id) {
        return ResponseEntity.ok(requestService.approveRequest(id));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Request> rejectRequest(@PathVariable UUID id) {
        return ResponseEntity.ok(requestService.rejectRequest(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable UUID id) {
        requestService.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }
}
