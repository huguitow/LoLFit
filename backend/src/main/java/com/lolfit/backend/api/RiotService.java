package com.lolfit.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.lolfit.backend.api.dto.RiotChampionDto;
import com.lolfit.backend.api.dto.RiotResponse;
import com.lolfit.backend.api.dto.RiotSkinDto;
import com.lolfit.backend.skin.Champion;
import com.lolfit.backend.skin.ChampionRepository;
import com.lolfit.backend.skin.Skin;
import com.lolfit.backend.skin.SkinRepository;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RiotService {
    private final RestTemplate restTemplate;
    private final ChampionRepository championRepository;
    private final SkinRepository skinRepository;

    private static final String DATA_URL = "https://ddragon.leagueoflegends.com/cdn/%s/data/en_US/championFull.json";
    private static final String VERSION_URL = "https://ddragon.leagueoflegends.com/api/versions.json";

    private static final String SPLASH_URL = "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/%s_%d.jpg";
    private static final String LOADING_URL = "https://ddragon.leagueoflegends.com/cdn/img/champion/loading/%s_%d.jpg";

    public String getLatestVersion() {
        List<String> versions = restTemplate.getForObject(VERSION_URL, List.class);
        if (versions == null || versions.isEmpty()) {
            throw new RuntimeException("Impossible de récupérer la version Riot");
        }
        return versions.get(0);
    }

    @Transactional
    public void syncChampions() {
        String version = getLatestVersion();
        System.out.println("--- DÉBUT SYNC RIOT (Version " + version + ") ---");
        String url = String.format(DATA_URL, version);
        RiotResponse response = restTemplate.getForObject(url, RiotResponse.class);
        if (response == null) {
            throw new RuntimeException("Impossible de récupérer les champions Riot");
        }
        for (Map.Entry<String, RiotChampionDto> entry : response.getData().entrySet()) {
            RiotChampionDto championDto = entry.getValue();
            Champion champion = championRepository.findByRiotId(championDto.getId())
                    .orElse(Champion.builder().riotId(championDto.getId()).build());
            champion.setName(championDto.getName());
            champion.setTitle(championDto.getTitle());
            champion.setLore(championDto.getLore());
            champion.setImageUrl(String.format(LOADING_URL, championDto.getId(), 0));
            championRepository.save(champion);
            System.out.println("Champion traité : " + champion.getName());

            for (RiotSkinDto riotSkin : championDto.getSkins()) {
                Skin skin = skinRepository.findByRiotId(riotSkin.getId())
                        .orElse(Skin.builder().riotId(riotSkin.getId()).build());

                skin.setName(riotSkin.getName());
                skin.setRpPrice(0);
                skin.setChampion(champion);

                skin.setImageUrl(String.format(LOADING_URL, championDto.getId(), riotSkin.getNum()));
                skin.setSplashArtUrl(String.format(SPLASH_URL, championDto.getId(), riotSkin.getNum()));

                skinRepository.save(skin);
            }
        }
        System.out.println("--- FIN SYNC RIOT : SUCCÈS ---");
    }
}