package org.Inventory.services;

import org.Inventory.dto.ReqRequestDTO;
import org.Inventory.enums.RequestStatus;
import org.Inventory.enums.Role;
import org.Inventory.exceptions.AccessDeniedException;
import org.Inventory.exceptions.AuthenticationRequiredException;
import org.Inventory.exceptions.EntityNotFoundException;
import org.Inventory.exceptions.OperationNotAllowedException;
import org.Inventory.models.Item;
import org.Inventory.models.Request;
import org.Inventory.models.User;
import org.Inventory.models.Warehouse;
import org.Inventory.repositories.ItemRepository;
import org.Inventory.repositories.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.Inventory.repositories.UserRepository;
import org.Inventory.repositories.WarehouseRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final WarehouseRepository warehouseRepository;

    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    public Optional<Request> getRequestById(UUID id) {
        return requestRepository.findById(id);
    }

    public List<Request> getRequestsByUser(UUID userId) {
        return requestRepository.findByUserId(userId);
    }

    public List<Request> getRequestsByItem(UUID itemId) {
        return requestRepository.findByItemId(itemId);
    }

    public Request createRequest(ReqRequestDTO request) {
        User currentUser = getCurrentUser();

        if (!isManager(currentUser) && !currentUser.getId().equals(request.getUserID())) {
            throw new AccessDeniedException("Access denied");
        }

        Item item = itemRepository.findById(request.getItemID())
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));

        User user = userRepository.findById(request.getUserID())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseID())
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found"));

        request.setStatus(RequestStatus.PENDING);
        Request newRequest = new Request();
        newRequest.setItem(item);
        newRequest.setUser(user);
        newRequest.setStatus(request.getStatus());
        newRequest.setWarehouseID(warehouse);
        return requestRepository.save(newRequest);
    }

    public Request approveRequest(UUID id) {
        User currentUser = getCurrentUser();

        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));

        if (!isManager(currentUser) && !currentUser.getId().equals(request.getUser().getId())) {
            throw new AccessDeniedException("Access denied");
        }

        request.setStatus(RequestStatus.APPROVED);
        return requestRepository.save(request);
    }

    public Request rejectRequest(UUID id) {
        User currentUser = getCurrentUser();

        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));

        if (!isManager(currentUser) && !currentUser.getId().equals(request.getUser().getId())) {
            throw new AccessDeniedException("Access denied");
        }
        request.setStatus(RequestStatus.REJECTED);
        return requestRepository.save(request);
    }

    public void deleteRequest(UUID id) {
        User currentUser = getCurrentUser();


        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));

        if (!isManager(currentUser) && !currentUser.getId().equals(request.getUser().getId())) {
            throw new AccessDeniedException("Access denied");
        }

        if (RequestStatus.APPROVED.equals(request.getStatus())) {
            throw new OperationNotAllowedException("Cannot delete request: already approved");
        }

        requestRepository.deleteById(id);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User user) {
            return user;
        }
        throw new AuthenticationRequiredException("User is not authenticated");
    }

    private boolean isManager(User user) {
        return user.getRoles().contains(Role.MANAGER);
    }
}


