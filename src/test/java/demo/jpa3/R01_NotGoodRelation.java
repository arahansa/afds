package demo.jpa3;

import static org.junit.Assert.*;

import org.hibernate.annotations.SelectBeforeUpdate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.cypher.internal.compiler.v2_1.commands.expressions.ProjectedPath.singleOutgoingRelationshipProjector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import demo.DemoApplication;
import demo.domain.Dept;
import demo.domain.Emp;
import demo.repository.DeptRepository;
import demo.repository.EmpRepository;

/**
 * Created by arahansa on 2015-06-28.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoApplication.class)
public class R01_NotGoodRelation{
	
	@Autowired EmpRepository empRepository;
	@Autowired DeptRepository deptRepository;
	@Autowired EmpService empService;
	
	@Before
	public void setup(){
		empService.setup();
	}
	
	@Test
	public void 첫번째_테스트() throws Exception {
		System.out.println("헬로월드");
		assertEquals(1, 1);
	}
	
	

	@Test
	public void 릴레이션테스트() throws Exception {
		assertEquals("단방향 세팅- 직원get부서로 받은 사이즈는 0 이다.", 
				0, empService.getEmpsDeptSize_Unidirection());
		
		assertEquals("양방향 세팅 - 직원 get 부서로 받은 사이즈는 1이다", 
				1, empService.getEmpsDeptSize_Bidirection());
		
		assertNull("d 기준으로 emp를 등록해주고 emp에서 d를 구해보면 널값", 
				empService.expectNullEmpsDept());
		// 오너라는 것의 의미를 기억하자. 
		
		assertEquals("부서에서 받아온 정보", 
				"Emp0", empService.getEmpsNameThroughDept());
		
		
	}
	
	@Test
	public void 이건머() throws Exception {
		empService.getEmpsNameThroughDept();
	}
	
	@Service
	@Transactional
	public static class EmpService{
		@Autowired EmpRepository empRepository;
		@Autowired DeptRepository deptRepository;
		
		public void setup(){
			empRepository.deleteAll();
			deptRepository.deleteAll();
			
			Emp e = new Emp();
			e.setName("Emp0");
			empRepository.save(e);
			
			Dept d = new Dept();
			d.setName("Dept0");
			
			e.setDept(d);        // JPA ! 저장을 할 때 jpa가 이것을 봄.
			d.getEmps().add(e);  // 저장을 할 때는 무시 ! 
			deptRepository.save(d);
		}
		
		public int getEmpsDeptSize_Unidirection(){
			Emp e = new Emp();
			e.setName("Emp1");
			empRepository.save(e);
			
			Dept d = new Dept();
			d.setName("Dept1");
			deptRepository.save(d);
			
			e.setDept(d);
			return e.getDept().getEmps().size(); 
		}
		
		public int getEmpsDeptSize_Bidirection(){
			Emp e = new Emp();
			e.setName("Emp1");
			empRepository.save(e);
			
			Dept d = new Dept();
			d.setName("Dept1");
			deptRepository.save(d);
			
			e.setDept(d);         // unidirectional (OK)
			d.getEmps().add(e);  // bidirectional (!)
			return e.getDept().getEmps().size(); 
		}
		
		public Dept expectNullEmpsDept(){
			Emp e = new Emp();
			e.setName("Emp1");
			empRepository.save(e);
			
			Dept d = new Dept();
			d.setName("Dept1");
			deptRepository.save(d);
			
			
			d.getEmps().add(e);  // bidirectional (!)
			return e.getDept();
		}
		
		public String getEmpsNameThroughDept(){
			Dept d = deptRepository.findByName("Dept0");
			System.out.println("받아온 부서"+d);
			System.out.println("부서의 사이즈"+d.getEmps().size());
			return d.getEmps().iterator().next().getName();
		}
	}
}