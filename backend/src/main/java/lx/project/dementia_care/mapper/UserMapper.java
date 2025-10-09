package lx.project.dementia_care.mapper;

import lx.project.dementia_care.dto.UserDto;
import lx.project.dementia_care.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Optional;

@Mapper
public interface UserMapper {
    Optional<UserDto> findById(@Param("userId") Long userId);

    void save(User user);
    void update(User user);
    void deleteById(@Param("userId") Long userId);
}
