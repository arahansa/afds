package demo.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data @EqualsAndHashCode(of="id")
public class TeamEmp {

	@Id @GeneratedValue
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	Team team;
	
	@ManyToOne(fetch=FetchType.LAZY)
	Emp emp;
	
}
