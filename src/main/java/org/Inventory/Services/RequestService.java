package org.Inventory.Services;

import org.Inventory.DTO.ReqRequest;
import org.Inventory.Enums.RequestStatus;
import org.Inventory.Enums.Role;
import org.Inventory.Models.Item;
import org.Inventory.Models.Request;
import org.Inventory.Models.User;
import org.Inventory.Models.Warehouse;
import org.Inventory.Repositories.ItemRepository;
import org.Inventory.Repositories.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.Inventory.Repositories.UserRepository;
import org.Inventory.Repositories.WarehouseRepository;
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

    public Request createRequest(ReqRequest request) {
        User currentUser = getCurrentUser();

        if (!isManager(currentUser) && !currentUser.getId().equals(request.getUserID())) {
            throw new SecurityException("Access denied");
        }

        Item item = itemRepository.findById(request.getItemID())
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        User user = userRepository.findById(request.getUserID())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseID())
                .orElseThrow(() -> new IllegalArgumentException("Warehouse not found"));

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
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!isManager(currentUser) && !currentUser.getId().equals(request.getUser().getId())) {
            throw new SecurityException("Access denied");
        }

        request.setStatus(RequestStatus.APPROVED);
        return requestRepository.save(request);
    }

    public Request rejectRequest(UUID id) {
        User currentUser = getCurrentUser();

        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!isManager(currentUser) && !currentUser.getId().equals(request.getUser().getId())) {
            throw new SecurityException("Access denied");
        }
        request.setStatus(RequestStatus.REJECTED);
        return requestRepository.save(request);
    }

    public void deleteRequest(UUID id) {
        User currentUser = getCurrentUser();


        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!isManager(currentUser) && !currentUser.getId().equals(request.getUser().getId())) {
            throw new SecurityException("Access denied");
        }

        if (RequestStatus.APPROVED.equals(request.getStatus())) {
            throw new IllegalStateException("Cannot delete request: already approved");
        }

        requestRepository.deleteById(id);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User user) {
            return user;
        }
        throw new IllegalStateException("User is not authenticated");
    }

    private boolean isManager(User user) {
        return user.getRoles().contains(Role.MANAGER);
    }
}


