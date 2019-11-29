package snod.com.cn.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import snod.com.cn.entity.Nickname;
import snod.com.cn.entity.Users;
import snod.com.cn.entity.UsersDetail;

public interface UsersDetailRepository extends JpaRepository<UsersDetail, Long>,JpaSpecificationExecutor<UsersDetail>{
	void deleteById(Long id);
}
