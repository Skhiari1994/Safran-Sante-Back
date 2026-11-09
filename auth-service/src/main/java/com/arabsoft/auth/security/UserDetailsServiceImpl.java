package com.arabsoft.auth.security;

import com.arabsoft.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    /*
     * private final UserRepository userRepository ;
     * 
     * @Override
     * public UserDetails loadUserByUsername(String userEmail) throws
     * UsernameNotFoundException {
     * return this.userRepository.findByUselogin(userEmail)
     * .orElseThrow(() -> new UsernameNotFoundException("User Not Found by email  "
     * + userEmail))
     * ;
     * }
     */

    // new logic
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username => " + username);
        String[] parts = username.split("\\|", 2);

        if (parts.length != 2) {
            throw new UsernameNotFoundException("Invalid authentication username");
        }

        String useLogin = parts[0];
        String codSoc = parts[1];

        return userRepository
                .findByUseloginAndCodSoc(useLogin, codSoc)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found for login " + useLogin
                                + " and company " + codSoc));
    }

}