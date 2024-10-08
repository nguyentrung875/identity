package com.example.trung.entityusers.controller;

import com.example.trung.entityusers.dto.APIResponse;
import com.example.trung.entityusers.dto.request.RoleRequest;
import com.example.trung.entityusers.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/role")
public class RoleController {
    private RoleService roleService;

    @PostMapping("/create")
    public APIResponse create(@RequestBody RoleRequest request){
        APIResponse apiResponse = new APIResponse();

        apiResponse.setData(roleService.create(request));

        return apiResponse;
    }

    @GetMapping("/getall")
    public APIResponse getAll(){
        APIResponse apiResponse = new APIResponse();

        apiResponse.setData(roleService.getAll());

        return apiResponse;
    }

    @DeleteMapping("/delete")
    public APIResponse delete(@RequestParam String role) {
        APIResponse apiResponse = new APIResponse();
        roleService.delete(role);
        apiResponse.setMessage("Delete!");
        return apiResponse;
    }
}
