package demo.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data 
// id 라는 필드 하나만 가지고 판단을 해라 ! 
@EqualsAndHashCode(of="id")
public class Emp {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(length=30)
	private String name;
	
	private int level;
	
	@Temporal(TemporalType.DATE)
	private Date birth;
	
	@ManyToOne(fetch=FetchType.LAZY)
	Dept dept;
	
	@Enumerated(EnumType.STRING)
	// MySql 의 경우 enum기능을 지원한다.
	// @Column(columnDefinition="VARCHAR(20)")
	@Column(columnDefinition="enum('SILVER', 'BRONZE', 'GOLD')")
	Level2 level2;
}
