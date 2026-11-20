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

    // new logic
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username => " + username);
        String[] parts = username.split("\\|", 2);

        if (parts.length != 2) {
            throw new UsernameNotFoundException("Invalid authentication username");
        }

        String identifiant = parts[0].trim();
        String codSoc = parts[1].trim();

        // 1. Recherche par CIN et société choisie
        // 2. Recherche classique par useLogin et société
        // 3. Cas compte privilégié RH/ADMIN par CIN (peut entrer dans n'importe quelle
        // société)
        // 4. Cas compte privilégié RH/ADMIN par useLogin
        return userRepository.findUserByCinAndCodSoc(identifiant, codSoc)
                .or(() -> userRepository.findByUseloginAndCodSoc(identifiant, codSoc))
                .or(() -> userRepository.findPrivilegedByCin(identifiant).stream().findFirst())
                .or(() -> userRepository.findPrivilegedByUselogin(identifiant).stream().findFirst())
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found for identifier " + identifiant
                                + " and company " + codSoc));
    }

}