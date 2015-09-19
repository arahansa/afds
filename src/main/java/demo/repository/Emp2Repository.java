package demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import demo.domain.Emp2;
import demo.dto.EmpDto;

public interface Emp2Repository extends JpaRepository<Emp2, Long> {
	public Emp2 findByName(String name);
	
	@Query("select e.id, e.name, e.birth, e.level from Emp2 e")
	public List<Object[]> projectionEmp2();
	
	@Query("select e.id, e.name, e.level from Emp2 e")
	public List<EmpDto> projectionByDto();
	
	
	
}
