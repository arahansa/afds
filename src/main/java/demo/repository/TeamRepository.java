package demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import demo.domain.Team;

public interface TeamRepository extends JpaRepository<Team, Long>{
	public Team findByName(String name);
}
