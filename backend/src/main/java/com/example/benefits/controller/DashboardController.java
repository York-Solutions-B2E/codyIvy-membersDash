package com.example.benefits.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.oauth2.jwt.Jwt;

import com.example.benefits.dto.DashboardDTO;
import com.example.benefits.service.DashboardService;

@RestController
@RequestMapping("/api")
public class DashboardController {

        private final DashboardService dashboardService;

        public DashboardController(DashboardService dashboardService) {

                this.dashboardService = dashboardService;
        }

        @GetMapping("dashboard")
        public DashboardDTO getDashboard(@AuthenticationPrincipal Jwt jwt) {

                String email = jwt.getClaim("email");

                return dashboardService.getDashboardData(email);
        }

}
