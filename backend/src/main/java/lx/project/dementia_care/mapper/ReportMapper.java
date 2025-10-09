package lx.project.dementia_care.mapper;

import lx.project.dementia_care.dto.ReportDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReportMapper {
    void save(@Param("report") ReportDto report);
}
