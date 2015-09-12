package demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import demo.domain.Emp;

public interface EmpRepository extends JpaRepository<Emp, Long>{
	
	public Emp findByName(String name);
	
	
	
	@Query("select e from Emp e where level = :level")
	public List<Emp> findLevel(@Param("level") int level);
	
	@Query("select e from Emp e join fetch e.dept")
	List<Emp> findAll2(); 
}
