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
	Set<Emp> emps = new HashSet<>();
	
	// 직원에게 부서 추가 메서드
	public void addEmp(Emp emp){
		emps.add(emp);
		emp.setDept(this);
	}
	
	// 직원에게 부서 제거
	public void removeEmp(Emp emp){
		emps.remove(emp);
		emp.setDept(null);
	}

}
