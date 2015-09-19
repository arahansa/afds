package demo.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;

// Team  * -- *  Emp
// 		 TeamEmp
//	(team_id, emp_id)
// 1--many , many -- 1
// many -----------many
@Entity
@Data @EqualsAndHashCode(of="id")
public class Team {
	
	@Id @GeneratedValue
	Long id;
	
	@Column
	String name;
	
	@OneToMany(mappedBy="team")
	Set<TeamEmp> teamEmps = new HashSet<>();
	
}
