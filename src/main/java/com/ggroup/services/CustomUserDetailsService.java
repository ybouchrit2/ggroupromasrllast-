package com.ggroup.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ggroup.models.User;
import com.ggroup.utils.JwtUtil;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userService.getUserByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(user.getRole()));  // إضافة الدور كـ GrantedAuthority

            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    authorities
            );
        } else {
            throw new UsernameNotFoundException("البريد الإلكتروني غير موجود");
        }
    }

    public String generateJwtForUser(String email) {
        Optional<User> userOptional = userService.getUserByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new JwtUtil().generateJwt(user.getEmail(), user.getId()); // توليد التوكن باستخدام email و userId
        }
        return null;
    }
}
