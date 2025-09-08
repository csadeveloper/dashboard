package com.ciptadana.dashboard.controller;

import com.ciptadana.dashboard.database.oracle.repository.jpa.projection.LoginResult;
import com.ciptadana.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Semaphore;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/login")
    public LoginResult login(
            @RequestParam("username") String username,
            @RequestParam("password") String password
            ){

        LoginResult result = null;
        try {

            log.info("Login Request {} ", username);
            result = dashboardService.login(username, password);
            log.info("Login Request Result {} {} ", username, result);


        }catch (Exception e){
            log.error(e.getMessage());
        }

        return result;
    }

    @GetMapping("/logout")
    public boolean logout(
            @RequestParam("sessionId") String sessionId,
            @RequestParam("username") String username
    ){

        boolean result = false;
        try {

            log.info("Logout Request {} ", username);
            result = dashboardService.logout(sessionId, username);

        }catch (Exception e){
            log.error(e.getMessage());
        }

        return result;
    }

}
