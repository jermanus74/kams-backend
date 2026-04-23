package itsdax.kams.features.user.repository;

import itsdax.kams.features.user.model.entity.PasswordHistory;
import itsdax.kams.features.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordHistoryRepo extends JpaRepository<PasswordHistory, Long> {

    List<PasswordHistory> findTop5ByUserOrderByCreatedAtDesc(User user);
}
