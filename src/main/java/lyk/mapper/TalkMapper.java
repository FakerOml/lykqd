package lyk.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import lyk.po.Talk;

@Repository
public interface TalkMapper {
	public Talk selectTalk(@Param("openid") String openid);

	public boolean deleteTalk(@Param("openid") String openid);

	public int insertTalk(@Param("openid") String openid,@Param("createtime") String createtime,@Param("times") String times,@Param("keyward") String keyward);

	public int insertOpenid(@Param("openid") String openid,@Param("id") String id);
	
	public String selectOpenid(@Param("openid") String openid);
}
