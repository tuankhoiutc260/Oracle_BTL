package com.example.library.service;
import com.example.library.Dto.AdminDto;
import com.example.library.model.Admin;

public interface AdminService {
    Admin findByUsername(String username);

    Admin save(AdminDto adminDto);
}