package lx.project.dementia_care.repository;

import lx.project.dementia_care.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * User 엔티티용 JPA 리포지터리
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 필요 시 이메일로 조회 같은 커스텀 메서드 추가 가능
}
