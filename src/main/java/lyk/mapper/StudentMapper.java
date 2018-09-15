package lyk.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import lyk.po.Student;

@Repository
public interface StudentMapper {
	public Student selectStudent(@Param("id") String id);
}
