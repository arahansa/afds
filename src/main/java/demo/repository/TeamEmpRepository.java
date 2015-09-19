package demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import demo.domain.TeamEmp;

public interface TeamEmpRepository extends JpaRepository<TeamEmp, Long>{

}
