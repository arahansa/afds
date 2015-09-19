package demo.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data 
// id 라는 필드 하나만 가지고 판단을 해라 ! 
@EqualsAndHashCode(of="id")
public class Emp2 {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(length=30)
	private String name;
	
	private int level;
	
	@Temporal(TemporalType.DATE)
	private Date birth;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Dept dept;
	
	@Enumerated(EnumType.STRING)
	// MySql 의 경우 enum기능을 지원한다.
	// @Column(columnDefinition="VARCHAR(20)")
	@Column(columnDefinition="enum('SILVER', 'BRONZE', 'GOLD')")
	private Level2 level2;
	
	// 양방향관계에서는 관계의 책임을 mappedby 가 안 붙은 쪽이 책임을 진다.
	@ManyToMany
	private Set<Team2> teams = new HashSet<>();
	
	// 양방향에서의 한쪽에서의 세팅 권장
	public void addTeam(Team2 team){
		teams.add(team);
		team.getEmps().add(this);
	}
	
	public void removeTeam(Team2 team){
		teams.remove(team);
		team.getEmps().remove(this);
	}
	
}
