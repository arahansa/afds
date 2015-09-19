package demo.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;

//Team  * -- *  Emp
// TeamEmp
//(team_id, emp_id)
//1--many , many -- 1
//many -----------many
@Entity
@Data
@EqualsAndHashCode(of = "id")
public class Team2 {

	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String name;

	// 양방향관계에서는 관계의 책임을 mappedby 가 안 붙은 쪽이 책임을 진다.
	@ManyToMany(mappedBy="teams")
	private Set<Emp2> emps = new HashSet<>();

}