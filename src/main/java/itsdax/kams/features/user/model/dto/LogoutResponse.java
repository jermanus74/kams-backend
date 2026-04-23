package itsdax.kams.features.user.model.dto;

import java.io.Serializable;

public record LogoutResponse
        (String username, String time
        ) implements Serializable {
}
