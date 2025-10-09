// DailyRecordMapper.java
package lx.project.dementia_care.mapper;

import lx.project.dementia_care.entity.DailyRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.Optional;

@Mapper
public interface DailyRecordMapper {
    Optional<DailyRecord> findByUserIdAndDate(@Param("userId") Long userId,
                                              @Param("recordDate") LocalDate recordDate);
    Optional<DailyRecord> findById(@Param("recordId") Long recordId);                                          
    void save(DailyRecord record);
    void update(DailyRecord record);
    void deleteById(@Param("recordId") Long recordId);
}
