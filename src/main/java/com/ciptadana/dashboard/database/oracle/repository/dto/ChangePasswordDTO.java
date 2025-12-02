package com.ciptadana.dashboard.database.oracle.repository.dto;

import lombok.Data;

@Data
public class ChangePasswordDTO {
    private String oldPassword;
    private String newPassword;
}