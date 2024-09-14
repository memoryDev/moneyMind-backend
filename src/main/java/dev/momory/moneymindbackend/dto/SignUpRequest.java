package dev.momory.moneymindbackend.dto;

import dev.momory.moneymindbackend.entity.AuthProvider;
import dev.momory.moneymindbackend.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String userid;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "비밀번호 확인을 입력해주세요.")
    private String confirmPassword;

    @NotBlank(message = "이름을 입력해주세요.")
    @Pattern(regexp = "^[가-힣]*$", message = "한글만 입력해 주세요.")
    private String name;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "이메일 형식으로 입력해주세요.")
    private String email;

    private AuthProvider authProvider;

    // dto -> Entity
    public User toEntity() {
        User entity = new User();
        entity.setUserid(userid);
        entity.setPassword(password);
        entity.setUsername(name);
        entity.setEmail(email);
        entity.setAuthProvider(authProvider);

        return entity;
    }
}
