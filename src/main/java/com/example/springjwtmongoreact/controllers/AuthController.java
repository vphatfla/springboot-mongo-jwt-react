package com.example.springjwtmongoreact.controllers;

import com.example.springjwtmongoreact.models.ERole;
import com.example.springjwtmongoreact.models.Role;
import com.example.springjwtmongoreact.models.User;
import com.example.springjwtmongoreact.payload.request.LoginRequest;
import com.example.springjwtmongoreact.payload.request.SignupRequest;
import com.example.springjwtmongoreact.payload.response.JwtResponse;
import com.example.springjwtmongoreact.payload.response.MessageResponse;
import com.example.springjwtmongoreact.repository.RoleRepository;
import com.example.springjwtmongoreact.repository.UserRepository;
import com.example.springjwtmongoreact.security.jwt.AuthEntryPointJwt;
import com.example.springjwtmongoreact.security.jwt.JwtUtils;
import com.example.springjwtmongoreact.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins="*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;



    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest)
    {
        System.out.print("Check 0");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        System.out.print("Check 1");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.print("Check 1.5");
        String jwt = jwtUtils.generateJwtToken(authentication);
        System.out.print("Check 2");
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        System.out.print("Check 3");
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        System.out.print("Check 4");
        return ResponseEntity.ok(new JwtResponse(jwt,userDetails.getId(), userDetails.getUsername(),
                userDetails.getEmail(), roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest)
    {
        if (userRepository.existsByUsername(signupRequest.getUsername()))
        {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is taken"));
        }
        if (userRepository.existsByEmail(signupRequest.getEmail()))
        {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in used"));
        }

        // create
        User user = new User(signupRequest.getUsername(), signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword()));

        Set<String> strRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("ERROR: role is not found"));
            roles.add(userRole);
        } else {
            strRoles.forEach(
                    role -> {
                        switch (role) {
                        case "admin":
                            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Error: role is not found"));
                            roles.add(adminRole);

                            break;

                        case "mode":
                            Role modeRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                    .orElseThrow(() -> new RuntimeException("Error: mode role is not found"));

                            roles.add(modeRole);

                            break;

                        default:
                            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                    .orElseThrow(() -> new RuntimeException("Error: user Role is not found"));

                            roles.add(userRole);


                        }

                    }
            );
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User register successfully"));

    }
}
