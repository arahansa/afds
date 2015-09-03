package demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import demo.domain.Dept;

public interface DeptRepository extends JpaRepository<Dept, Long>{
	public Dept findByName(String name);
}
