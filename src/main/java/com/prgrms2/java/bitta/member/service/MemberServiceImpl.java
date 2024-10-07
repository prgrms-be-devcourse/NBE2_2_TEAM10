package com.prgrms2.java.bitta.member.service;

import com.prgrms2.java.bitta.member.dto.MemberDTO;
import com.prgrms2.java.bitta.member.dto.SignUpDTO;
import com.prgrms2.java.bitta.member.entity.Member;
import com.prgrms2.java.bitta.member.exception.NoChangeException;
import com.prgrms2.java.bitta.member.repository.MemberRepository;
import com.prgrms2.java.bitta.security.JwtToken;
import com.prgrms2.java.bitta.security.JwtTokenProvider;
import com.prgrms2.java.bitta.security.exception.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final String defaultProfileImg = "/images/default_avatar.png";

    @Value("${file.root.path}")
    private String fileRootPath;

    @Transactional
    @Override
    public JwtToken signIn(String username, String password) {
        // 1. username + password 를 기반으로 Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        return jwtToken;
    }

    @Transactional
    @Override
    public MemberDTO signUp(SignUpDTO signUpDTO) {
        if (memberRepository.existsByUsername(signUpDTO.getUsername())) {
            throw new IllegalArgumentException("이미 사용 중인 사용자 이름입니다.");
        }

        // Password 암호화
        String encodedPassword = passwordEncoder.encode(signUpDTO.getPassword());

        // USER 권한 부여
        List<String> roles = new ArrayList<>();
        roles.add("USER");

        return MemberDTO.toDTO(memberRepository.save(signUpDTO.toEntity(encodedPassword, roles)));
    }

    @Transactional
    @Override
    public JwtToken reissueToken(String accessToken, String refreshToken) {
        try {
            JwtToken newToken = jwtTokenProvider.reissueToken(accessToken, refreshToken);

            if (newToken == null) {
                // 액세스 토큰이 아직 유효한 경우
                return null;
            }

            return newToken;
        } catch (RuntimeException e) {
            // 리프레시 토큰이 만료된 경우 등의 예외 처리
            log.info("토큰 갱신 실패: {}", e.getMessage());
            // 필요한 경우 여기서 로그아웃 처리를 할 수 있습니다.
            throw new InvalidTokenException("토큰 갱신에 실패했습니다. 다시 로그인해주세요.");
        }
    }


    @Override
    public MemberDTO getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        String profile = member.getProfile() != null ? member.getProfile() : defaultProfileImg;
        File thumbnailFile = getThumbnailFile(profile);

        if (thumbnailFile.exists()) {
            profile = thumbnailFile.getPath();
        }

        MemberDTO memberDTO = new MemberDTO(member);
        memberDTO.setProfile(profile);

        return memberDTO;
    }

    @Transactional
    @Override
    public MemberDTO updateMember(Long id, MemberDTO memberDTO, MultipartFile profileImage, boolean removeProfileImage) throws IOException {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        boolean isUpdated = false;

        if (removeProfileImage) {
            deleteProfileImage(member.getProfile());
            member.setProfile(defaultProfileImg);
            isUpdated = true;
        } else if (profileImage != null && !profileImage.isEmpty()) {
            deleteProfileImage(member.getProfile());
            String imagePath = saveProfileImage(profileImage);

            String thumbnailPath = createThumbnail(imagePath);
            member.setProfile(imagePath);
            isUpdated = true;
        }

        if (memberDTO.getNickname() != null && !memberDTO.getNickname().isBlank()) {
            member.setNickname(memberDTO.getNickname());
            isUpdated = true;
        }

        if (memberDTO.getAddress() != null && !memberDTO.getAddress().isBlank()) {
            member.setAddress(memberDTO.getAddress());
            isUpdated = true;
        }

        if (memberDTO.getPassword() != null && !memberDTO.getPassword().isBlank()) {
            member.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
            isUpdated = true;
        }

        if (!isUpdated) {
            throw new NoChangeException("변경된 내용이 없습니다.");
        }

        memberRepository.save(member);
        return MemberDTO.toDTO(member);
    }

    @Transactional
    @Override
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID가 " + id + "인 회원을 찾을 수 없습니다."));
        memberRepository.delete(member);
    }

    private File getProfileImageFile(String profileImg) {
        return new File(profileImg);
    }

    private String saveProfileImage(MultipartFile profileImage) throws IOException {
        String directory = fileRootPath + "/uploads/profile_images/";
        Path uploadPath = Paths.get(directory);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = System.currentTimeMillis() + "_" + profileImage.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        profileImage.transferTo(filePath.toFile());

        return directory + fileName;
    }

    public void deleteProfileImage(String profileImg) {
        if (profileImg != null && !profileImg.equals(defaultProfileImg)) {
            File profileFile = getProfileImageFile(profileImg);
            File thumbnailFile = getThumbnailFile(profileImg);

            if (profileFile.exists() && profileFile.isFile()) {
                profileFile.delete();
            }

            if (thumbnailFile.exists() && thumbnailFile.isFile()) {
                thumbnailFile.delete();
            }
        }
    }

    private String createThumbnail(String imagePath) throws IOException {
        String thumbnailDirectory = fileRootPath + "/uploads/profile_images/thumbnail/";
        Path thumbnailPath = Paths.get(thumbnailDirectory);

        if (!Files.exists(thumbnailPath)) {
            Files.createDirectories(thumbnailPath);
        }

        String originalFileName = Paths.get(imagePath).getFileName().toString();
        String thumbnailFileName = "thumb_" + originalFileName;
        Path thumbnailFilePath = thumbnailPath.resolve(thumbnailFileName);

        Thumbnails.of(new File(imagePath))
                .size(200, 200)
                .keepAspectRatio(true)
                .toFile(thumbnailFilePath.toFile());

        return thumbnailFilePath.toString();
    }

    public void outputThumbnail(String profileImagePath, OutputStream outputStream) throws IOException {
        File thumbnailFile = getThumbnailFile(profileImagePath);
        if (thumbnailFile.exists() && thumbnailFile.isFile()) {
            Thumbnails.of(thumbnailFile)
                    .size(200, 200)
                    .keepAspectRatio(true)
                    .outputFormat("jpg")
                    .toOutputStream(outputStream);
        } else {
            throw new IOException("썸네일 파일을 찾을 수 없습니다: " + thumbnailFile.getAbsolutePath());
        }
    }

    private File getThumbnailFile(String profileImg) {
        String thumbnailImgPath = profileImg.replace("profile_images", "profile_images/thumbnail");
        String thumbnailFileName = "thumb_" + Paths.get(profileImg).getFileName().toString();
        return new File(thumbnailImgPath.replace(Paths.get(profileImg).getFileName().toString(), thumbnailFileName));
    }

    private Path getThumbnailDirectory() throws IOException {
        String thumbnailDirectory = fileRootPath + "/uploads/profile_images/thumbnail/";
        Path thumbnailPath = Paths.get(thumbnailDirectory);

        if (!Files.exists(thumbnailPath)) {
            Files.createDirectories(thumbnailPath);
        }

        return thumbnailPath;
    }
}