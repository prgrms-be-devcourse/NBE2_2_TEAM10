package com.prgrms2.java.bitta.token.controller;

import com.prgrms2.java.bitta.user.dto.UserDTO;
import com.prgrms2.java.bitta.token.util.JWTUtil;
import com.prgrms2.java.bitta.user.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/token")
@Log4j2
public class TokenController {
    private final UserService userService;
    private final JWTUtil jwtUtil;

    @PostMapping("/make")
    public ResponseEntity<Map<String, Object>> makeToken(@RequestBody UserDTO userDTO) {
        log.info("Making token");

        //사용자 정보 가져오기
        UserDTO foundUserDTO = userService.read(userDTO.getEmail(), userDTO.getPassword());
        log.info("Found member: " + foundUserDTO);

        //토큰 생성
        Map<String, Object> payloadMap = foundUserDTO.getPayload();
        String accessToken = jwtUtil.createToken(payloadMap, 60); //1분 유효
        String refreshToken = jwtUtil.createToken(Map.of("email", foundUserDTO.getEmail()),60 * 24 * 7);
        log.info("Refresh token: " + refreshToken);
        log.info("Access token: " + accessToken);

        return ResponseEntity.ok(Map.of("accessToken", accessToken, "refreshToken", refreshToken));
    }

    // 리프레시 토큰 발행 시 - 예외 상황 코드와 메시지 전송
    public ResponseEntity<Map<String, String>> handleException(String message, int status) {
        return ResponseEntity.status(status)                //1. 엑세스 토큰이 없는 경우      400
                .body(Map.of("error", message));        //2. 리프레시 토큰이 없는 경우    400
        //3. mid가 없는 경우              400
        //4. 리프레시 토큰이 만료된 경우  400
        //5. 그외 예외 발생               400
    }
    //리프레시 토큰 검증
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestHeader("Authorization")String headerAuth,
                                                            @RequestParam("refreshToken")String refreshToken,
                                                            @RequestParam("email")String email) {
        //파라미터 값 전달 확인 - 값이 없으면 400 Bad Request 반환
        log.info("------refreshToken()");
        log.info("------Authorization: " + headerAuth);
        log.info("------Refresh Token: " + refreshToken);
        log.info("------email: " + email);
        if(headerAuth == null || !headerAuth.startsWith("Bearer ")) {
            //액세스 토큰 없는 경우
            return handleException("액세스 토큰이 없습니다.", 400);
        }
        if(refreshToken == null || refreshToken.isEmpty()) { //리프레시 토큰 없는 경우
            return handleException("리프레시 토큰이 없습니다", 400);
        }
        if(email == null || email.isEmpty()) {
            return handleException("아이디가 없습니다",400);
        }

        try {       //1. 액세스 토큰 만료 여부 확인
            String accessToken = headerAuth.substring(7);
            Map<String, Object> claims = jwtUtil.validateToken(accessToken);
            log.info("--- 토큰 유효성 검증 완료 ---");     //1.1 액세스 토큰 유효성 검증

            //새로운 토큰 생성 메서드 호출
        }catch (ExpiredJwtException e) {//2. 만료 기간이 경과한 경우
            log.info("----access token 기간 만료 ----");
            try {
                return ResponseEntity.ok(makeNewToken(email, refreshToken)); //중요
            } catch (ExpiredJwtException ee) {
                log.info("-- 리프레시 토큰 만료 기간 경과 --");
                return handleException("리프레시 토큰 기간 만료 :"+ ee.getMessage(), 400);
            }
        }catch (Exception e){
            e.printStackTrace(); //3. 그 외의 예외
            return handleException("리프레시 토큰 검증 예외 :" + e.getMessage(), 400);
        }
        return null;
    }

    //새로운 토큰 생성
    public Map<String, String> makeNewToken(String email, String refreshToken) {
        // 리프레시 토큰 유효성 검증
        Map<String, Object> claims = jwtUtil.validateToken(refreshToken);
        log.info("리프레시 토큰 유효성 검증 완료 ----");

        // claims의 mid와 매개변수로 전달받은 mid가 일치하지 않는 경우 예외 발생
        if (!claims.get("email").equals(email)) {
            throw new RuntimeException("INVALID REFRESH TOKEN email");  // RuntimeException 발생
        }
        log.info("--- make new token ---");
        //액세스 토큰과 리프레시 토큰 생성하여 mid와 반환
        UserDTO foundUserDTO = userService.read(email);
        Map<String, Object> payloadMap = foundUserDTO.getPayload();
        String newAccessToken = jwtUtil.createToken(payloadMap, 60); //1분 유효
        String newRefreshToken = jwtUtil.createToken(Map.of("email", email),60 * 24 * 7);

        return Map.of( "accessToken", newAccessToken,
                "refreshToken", newRefreshToken,
                "email", email);
    }

    /*  2024.08.28 수업 113 ~ 159  */
    public ResponseEntity<Map<String,String>> sendResponse(String message){
        return new ResponseEntity<>(Map.of("error", message),
                HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/refreshVerify")
    public ResponseEntity<Map<String,String>> refreshVerify(
            @RequestHeader("Authorization")String headerAuth,
            @RequestParam("refreshToken") String refreshToken,
            @RequestParam("email")String email){
        if( !headerAuth.startsWith("Bearer "))  return sendResponse("액세스 토큰 없음");
        if( refreshToken.isEmpty())             return sendResponse("리프레시 토큰 없음");

        try {   //2. 액세스 토큰 유효성 검증
            String accessToken = headerAuth.substring(7);
            Map<String, Object> claims = jwtUtil.validateToken(accessToken);
            log.info("---1. 액세스 토큰 유효");
            //전달받은 데이터 그대로
            return ResponseEntity.ok(Map.of("accessToken", accessToken, "refreshToken", refreshToken, "email", email));

        } catch (ExpiredJwtException e){
            log.info("---2. 액세스 토큰 만료기간 경과");

            try {//3. 리프레시 토큰 유효성 검증
                Map<String, Object> claims = jwtUtil.validateToken(refreshToken);
                log.info("--- 3.리프레시 토큰 유효");

                if(!claims.get("email").equals(email)){ //email 일치여부
                    return sendResponse("INVALID REFRESH TOKEN email");
                }
                log.info("--- 4. 새로운 토큰 생성");
                UserDTO foundUserDTO = userService.read(email);
                Map<String, Object> payloadMap = foundUserDTO.getPayload();
                String newAccessToken = jwtUtil.createToken(payloadMap, 60);
                String newFreshToken = jwtUtil.createToken(Map.of("email", email),60 * 24 * 7);

                //신규 생성 토큰들과 email 반환
                return ResponseEntity.ok(Map.of("accessToken", newAccessToken, "refreshToken", newFreshToken, "email", email));
            }catch (ExpiredJwtException ee) {
                log.info("--- 5. 리프레시 토큰 만료기간 경과");
                return sendResponse("리프레시 토큰 만료기간 경과"+ ee.getMessage());
            }
        } catch (Exception e){
            log.info("--- 리프레시 토큰 처리 기타 예외");
            return sendResponse(e.getMessage());
        }
    }
}