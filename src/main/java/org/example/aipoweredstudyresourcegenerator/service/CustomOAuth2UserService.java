package org.example.aipoweredstudyresourcegenerator.service;

import lombok.RequiredArgsConstructor;
import org.example.aipoweredstudyresourcegenerator.Model.Users;
import org.example.aipoweredstudyresourcegenerator.Repo.UsersRepo;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UsersRepo userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {

        OAuth2User oauthUser = super.loadUser(userRequest);
        Map<String, Object> attributes = oauthUser.getAttributes();

        String githubId = attributes.get("id").toString();
        String name = (String) attributes.get("name");
        String avatarUrl = (String) attributes.get("avatar_url");

        String email = fetchGithubEmail(userRequest);

        Users user = userRepository.findByGithubId(githubId)
                .orElse(new Users());

        user.setGithubId(githubId);
        user.setName(name);
        user.setEmail(email);
        user.setAvatarUrl(avatarUrl);

        userRepository.save(user);

        return oauthUser;
    }

    private String fetchGithubEmail(OAuth2UserRequest userRequest) {

        String accessToken = userRequest.getAccessToken().getTokenValue();

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(
                "https://api.github.com/user/emails",
                HttpMethod.GET,
                entity,
                List.class
        );

        List<Map<String, Object>> emails = response.getBody();

        if (emails != null) {
            for (Map<String, Object> emailObj : emails) {
                Boolean primary = (Boolean) emailObj.get("primary");
                Boolean verified = (Boolean) emailObj.get("verified");

                if (Boolean.TRUE.equals(primary) && Boolean.TRUE.equals(verified)) {
                    return (String) emailObj.get("email");
                }
            }
        }

        return null;
    }
}