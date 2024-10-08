package com.example.trung.entityusers.controller;

import com.example.trung.entityusers.dto.APIResponse;
import com.example.trung.entityusers.dto.request.PermissionRequest;
import com.example.trung.entityusers.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/permission")
public class PermissionController {
    private PermissionService permissionService;

    @PostMapping("/create")
    public APIResponse create(@RequestBody PermissionRequest request){
        APIResponse apiResponse = new APIResponse();

        apiResponse.setData(permissionService.create(request));

        return apiResponse;
    }

    @GetMapping("/getall")
    public APIResponse getAll(){
        APIResponse apiResponse = new APIResponse();

        apiResponse.setData(permissionService.getAll());

        return apiResponse;
    }

    @DeleteMapping("/delete")
    public APIResponse delete(@RequestParam String permission) {
        APIResponse apiResponse = new APIResponse();
        permissionService.delete(permission);
        apiResponse.setMessage("Delete!");
        return apiResponse;
    }
}
