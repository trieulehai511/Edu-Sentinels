package com.chrollo_dev.EduSentinel.modules.auth.serivce;


import com.chrollo_dev.EduSentinel.common.exception.AppException;
import com.chrollo_dev.EduSentinel.common.exception.ErrorCode;
import com.chrollo_dev.EduSentinel.modules.auth.dto.AuthenticationResponse;
import com.chrollo_dev.EduSentinel.modules.auth.dto.LoginRequest;
import com.chrollo_dev.EduSentinel.modules.auth.dto.RegisterRequest;
import com.chrollo_dev.EduSentinel.modules.user.dto.UserResponse;
import com.chrollo_dev.EduSentinel.modules.user.entity.User;
import com.chrollo_dev.EduSentinel.modules.user.mapper.UserMapper;
import com.chrollo_dev.EduSentinel.modules.user.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.experimental.NonFinal;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthService {
    @NonFinal
    @Value("${jwt.signerKey}")
    String SIGNER_KEY;
    @NonFinal
    @Value("${jwt.valid-duration}")
    long VALID_DURATION;

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getRepeatPassword())) {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User newUser = User.builder().
                username(request.getUsername())
                .fullName(request.getFullName())
                .email(request.getEmail())
                .role(request.getRole())
                // 3. Quan trọng: Mã hóa mật khẩu trước khi lưu
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(newUser);
        return userMapper.toUserResponse(newUser);
    }

    public AuthenticationResponse login(LoginRequest rq) {
        var user = userRepository.findUserByUsername(rq.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean authenticated = passwordEncoder.matches(rq.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        var token = generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    String generateToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername()) // Token này của ai?
                .issuer("edusentinel.com")   // Ai cấp token?
                .issueTime(new Date())       // Cấp lúc nào?
                .expirationTime(new Date(    // Hết hạn lúc nào?
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .claim("scope", user.getRole().name())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize(); // Biến thành chuỗi string dài ngoằng
        } catch (JOSEException e) {
            log.error("Không thể tạo token", e);
            throw new RuntimeException(e);
        }
    }
    public SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        // 1. Kiểm tra chữ ký (Có bị hack sửa đổi không?)
        boolean verified = signedJWT.verify(verifier);

        // 2. Kiểm tra hạn sử dụng (Expired?)
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        if (!verified || expiryTime.before(new Date())) {
            throw new RuntimeException("Token không hợp lệ hoặc đã hết hạn");
        }

        return signedJWT;
    }
}
