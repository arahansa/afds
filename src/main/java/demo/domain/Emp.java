package demo.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.ToString;

@Entity
@Data 
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
}
