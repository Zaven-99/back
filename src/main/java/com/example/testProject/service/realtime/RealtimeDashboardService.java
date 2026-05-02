package com.example.testProject.service.realtime;

import com.example.testProject.dto.realtime.DashboardUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RealtimeDashboardService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendUpdate(DashboardUpdateDTO dto) {
        messagingTemplate.convertAndSend(
                "/topic/dashboard/" + dto.getBusinessId(),
                dto
        );
    }
}