package snod.com.cn.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import snod.com.cn.entity.Nickname;
import snod.com.cn.entity.Users;

public interface NicknameRepository extends JpaRepository<Nickname, Long>,JpaSpecificationExecutor<Nickname>{
	void deleteById(Long id);
}
