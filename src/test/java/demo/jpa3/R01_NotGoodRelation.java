package demo.jpa3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import demo.DemoApplication;
import demo.domain.Dept;
import demo.domain.Emp;
import demo.domain.Emp2;
import demo.domain.Level2;
import demo.domain.Team;
import demo.domain.Team2;
import demo.domain.TeamEmp;
import demo.dto.EmpDto;
import demo.repository.DeptRepository;
import demo.repository.Emp2Repository;
import demo.repository.EmpRepository;
import demo.repository.Team2Repository;
import demo.repository.TeamEmpRepository;
import demo.repository.TeamRepository;

/**
 * Created by arahansa on 2015-06-28.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class R01_NotGoodRelation{
	
	@Autowired EmpRepository empRepository;
	@Autowired Emp2Repository emp2Repository;
	@Autowired DeptRepository deptRepository;
	@Autowired TeamRepository teamRepository;
	@Autowired Team2Repository team2Repository;
	@Autowired EmpService empService;
	@Autowired PlatformTransactionManager tm;
	
	@Before
	public void setup(){
		empService.setup();
	}
	

	@Test
	public void a_릴레이션테스트() throws Exception {
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
	public void a_직원이름테스트() throws Exception {
		assertEquals("부서를 통해 받은 직원이름이 0 이다", 
				"Emp0", empService.getEmpsNameThroughDept());
	}
	
	@Test
	@Rollback(false)
	public void b_데이터넣어보고_디비로_직접보기() throws Exception {
		empService.manyToManySave();
		
		new TransactionTemplate(tm).execute((s) -> {
			System.out.println("Emp1의 팀 이름 ");
			Emp e1 = empRepository.findByName("Emp1");
			assertEquals("Emp1는 소속팀이 두군데다",
					2, e1.getTeamEmps().size());
			//System.out.println(e1.getTeamEmps().size());
			e1.getTeamEmps().stream().forEach(te-> {System.out.println(te.getTeam().getName());});
			
			System.out.println("Emp2의 팀 이름");
			Emp e2 = empRepository.findByName("Emp2");
			assertEquals("Emp2는 소속팀이 두군데다",
					1, e2.getTeamEmps().size());
			//System.out.println(e2.getTeamEmps().size());
			e2.getTeamEmps().stream().forEach(te-> {System.out.println(te.getTeam().getName());});
			
			Team t2 = teamRepository.findByName("Team2");
			assertEquals("Team2는 소속직원이 두 명이다",
					2, t2.getTeamEmps().size());
			System.out.println("Team2의 직원들 :: ");
			t2.getTeamEmps().stream().forEach(te->{System.out.print(te.getEmp().getName());});
			return null;
		});
	}
	
	@Test
	@Rollback(false)
	public void 매니투매니어노테이션주고_검사해보기() throws Exception {
		emp2Repository.deleteAll();
		team2Repository.deleteAll();
		
		empService.manyTomany_Real_Save();
		new TransactionTemplate(tm).execute((s) ->{
			Emp2 e1 = emp2Repository.findByName("Emp1");
			assertEquals("Emp1의 소속 팀 사이즈",
					2, e1.getTeams().size());
			System.out.println("Emp1 소속 팀 이름들");
			e1.getTeams().stream().forEach(t->{ System.out.println(t.getName());} );
			
			Emp2 e2 = emp2Repository.findByName("Emp2");
			assertEquals("Emp2 소속 팀 사이즈", 
					1, e2.getTeams().size());
			System.out.println("Emp2 소속 팀 이름들 ");
			e2.getTeams().stream().forEach(t->{ System.out.println(t.getName());} );
			
			//e2 팀 제거 테스트
			e2.removeTeam(e2.getTeams().iterator().next());
			assertEquals("Emp2에서 소속 팀을 제거했으니 팀 사이즈가 0이다.", 
					0, e2.getTeams().size());
			
			//e2 팀 추가 테스트 : Team3를 배정해보자.
			Team2 t3 = new Team2();
			team2Repository.save(t3);
			e2.addTeam(t3);
			assertEquals("Emp2에서 Team3를 추가했으니 팀 사이즈가 1이다", 
					1, e2.getTeams().size());
			
			return null;
		});
	}
	
	@Test
	public void 프로젝션_테스트() throws Exception {
		System.out.println("프로젝션 테스트 ");
		List<Object[]> emp2List = emp2Repository.projectionEmp2();
		emp2List.forEach(e->{
			System.out.print("아이디 : "+e[0]);
			System.out.print(", 이름 : "+e[1]);
			System.out.print(", 생일 :"+e[2]);
			System.out.println(", 레벨 :"+e[3]);
		});
		
		System.out.println("프로젝션 by dto");
		List<EmpDto> projectionByDto = emp2Repository.projectionByDto();
		projectionByDto.forEach(System.out::println);
	}
	
	@Test
	public void 프로젝션_Dto() throws Exception {
		
	}
	

	
	@Service
	@Transactional
	public static class EmpService{
		@Autowired EmpRepository empRepository;
		@Autowired Emp2Repository emp2Repository;
		@Autowired DeptRepository deptRepository;
		@Autowired TeamEmpRepository teamEmpRepository;
		@Autowired TeamRepository teamRepository;
		@Autowired Team2Repository team2Repository;
		
		public void setup(){
			teamEmpRepository.deleteAll();
			teamRepository.deleteAll();
			
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
		
		public void manyToManySave(){
			// e1의 소속 : t1, t2
			// e2의 소속 : t2
			// --
			// t1에 속한 것들 e1
			// t2에 속한 것들 e1, e2
			
			
			Emp e1  = new Emp();
			e1.setName("Emp1");
			empRepository.save(e1);
			
			Emp e2 = new Emp();
			e2.setName("Emp2");
			empRepository.save(e2);
			
			Team t1  = new Team();
			t1.setName("Team1");
			teamRepository.save(t1);
			
			Team t2 = new Team();
			t2.setName("Team2");
			teamRepository.save(t2);
			
			TeamEmp te1 = new TeamEmp();
			te1.setTeam(t1);
			te1.setEmp(e1);
			teamEmpRepository.save(te1);
			
			TeamEmp te2 = new TeamEmp();
			te2.setTeam(t2);
			te2.setEmp(e1);
			teamEmpRepository.save(te2);
			
			TeamEmp te3 =new TeamEmp();
			te3.setTeam(t2);
			te3.setEmp(e2);
			teamEmpRepository.save(te3);
				
		}
		
		
		public void manyTomany_Real_Save(){
			Emp2 e1 = new Emp2();
			e1.setName("Emp1");
			e1.setBirth(new GregorianCalendar(1987, 2, 24).getTime());
			e1.setLevel(1);
			e1.setLevel2(Level2.BRONZE);
			emp2Repository.save(e1);
			
			Emp2 e2 = new Emp2();
			e2.setName("Emp2");
			emp2Repository.save(e2);
			
			Team2 t1 = new Team2();
			t1.setName("Team1");
			team2Repository.save(t1);
			
			Team2 t2 = new Team2();
			t2.setName("Team2");
			team2Repository.save(t2);
			
//			e1.getTeams().add(t1);
//			t1.getEmps().add(e1);
//			
//			e1.getTeams().add(t2);
//			t2.getEmps().add(e1);
//			
//			e2.getTeams().add(t2);
//			t2.getEmps().add(e2);
			
			e1.addTeam(t1);
			e1.addTeam(t2);
			e2.addTeam(t2);
			// one To many 는 오너십이 없는쪽에서 이런 걸 만들어주는 것이 낫다. 
		}
	}
}