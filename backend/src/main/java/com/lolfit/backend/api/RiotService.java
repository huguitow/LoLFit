package com.lolfit.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RiotService {
    private final RestTemplate restTemplate;
    private static final String VERSION_URL = "https://ddragon.leagueoflegends.com/api/versions.json";

    public String getLatestVersion() {
        List<String> versions = restTemplate.getForObject(VERSION_URL, List.class);
        if (versions == null || versions.isEmpty()) {
            throw new RuntimeException("Impossible de récupérer la version Riot");
        }
        return versions.get(0);
    }
}
