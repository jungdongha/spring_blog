package me.shinsunyoung.springbootdeveloper.service;


import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper.domain.User;
import me.shinsunyoung.springbootdeveloper.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service

//시큐리티에서 사용자 정보를 가져오는 인터페이스이다.
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    //사용자 이름(email)으로 정보 가져오는 메서드
    @Override
    public User loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException((email)));
    }
}
