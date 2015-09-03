package demo.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data @ToString(exclude="emps") @EqualsAndHashCode(exclude="emps")
public class Dept {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(length=50)
	private String name;
	
	@OneToMany(mappedBy="dept")
	Set<Emp> emps;

}
