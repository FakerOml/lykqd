package lyk.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import lyk.po.Teacher;

@Repository
public interface TeacherMapper {
	public Teacher selectTeacher(@Param("id") String id);
}
