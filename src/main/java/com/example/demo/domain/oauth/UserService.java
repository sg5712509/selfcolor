package com.example.demo.domain.oauth;

import com.example.demo.domain.User;
import com.example.demo.domain.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;

    public User upsertSocialUser(String socialTypeCode, String socialId, String email, String nickname) {
        User found = userMapper.findBySocial(socialTypeCode, socialId);

        // DB 수정 금지 조건 때문에 email이 없으면 임시 이메일 생성
        if (email == null || email.isBlank()) {
            email = socialTypeCode.toLowerCase() + "_" + socialId + "@local.social";
        }
        if (nickname == null || nickname.isBlank()) {
            nickname = socialTypeCode + "User";
        }

        if (found == null) {
            User u = new User();
            u.setSocialTypeCode(socialTypeCode);
            u.setSocialId(socialId);
            u.setEmail(email);
            u.setNickname(nickname);
            userMapper.insertUser(u);
            return u;
        } else {
            found.setEmail(email);
            found.setNickname(nickname);
            userMapper.updateUser(found);
            return found;
        }
    }
}

