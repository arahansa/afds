package demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import demo.domain.Team2;

public interface Team2Repository extends JpaRepository<Team2, Long>{
	public Team2 findByName(String name); 
}
