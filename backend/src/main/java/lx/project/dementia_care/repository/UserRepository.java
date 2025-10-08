package lx.project.dementia_care.repository;

import lx.project.dementia_care.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * User 엔티티에 대한 CRUD 및 페이징/정렬 기능 제공
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 추가 조회가 필요할 경우 아래 예시처럼 메서드를 선언할 수 있습니다.
    // Optional<User> findByEmail(String email);
}
