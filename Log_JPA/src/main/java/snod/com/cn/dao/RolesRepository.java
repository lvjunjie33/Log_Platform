package snod.com.cn.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import snod.com.cn.entity.Roles;
import snod.com.cn.entity.Users;

public interface RolesRepository extends JpaRepository<Roles, Long>,JpaSpecificationExecutor<Roles>{

}
