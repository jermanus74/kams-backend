package itsdax.kams.features.user.mapper;

import itsdax.kams.features.user.model.dto.CreateUserRequest;
import itsdax.kams.features.user.model.dto.UserResponse;
import itsdax.kams.features.user.model.entity.User;

public interface AuthMapper{
    User fromDto(CreateUserRequest dto, String encodedPassword, String regNo);
    UserResponse toDto(User user);
}
