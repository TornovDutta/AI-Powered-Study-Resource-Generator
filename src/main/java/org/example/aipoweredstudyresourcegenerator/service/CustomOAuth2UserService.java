package org.example.aipoweredstudyresourcegenerator.service;

import lombok.RequiredArgsConstructor;
import org.example.aipoweredstudyresourcegenerator.Model.Users;
import org.example.aipoweredstudyresourcegenerator.Repo.UsersRepo;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

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
        String email = (String) attributes.get("email");
        String avatarUrl = (String) attributes.get("avatar_url");


        Users user = userRepository.findByGithubId(githubId)
                .orElse(new Users());

        user.setGithubId(githubId);
        user.setName(name);
        user.setEmail(email);
        user.setAvatarUrl(avatarUrl);

        userRepository.save(user);

        return oauthUser;
    }
}
