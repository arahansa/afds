package demo;

import java.util.Arrays;
import java.util.List;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.domain.Dept;
import demo.domain.Emp;
import demo.domain.Level2;
import demo.repository.DeptRepository;
import demo.repository.EmpRepository;

@SpringBootApplication
public class JPASecondStudy {
	public static void main(String[] args) {
		SpringApplication.run(JPASecondStudy.class, args);
	}
	
	@Autowired EmpRepository empRepository;
	@Autowired DeptRepository deptRepository;
	@Autowired EmpService empService;
	@Bean
    InitializingBean hello(){
		return ()->{
			System.out.println("자바의 아이덴티티를 배워본 시간이었다 ! ");
			// 같은 아이디로 불러온 녀석은 같은 녀석이다 !!
			Emp e = new Emp();
			e.setName("arahansa");
			empRepository.save(e);
			
			Emp e2 = empRepository.findOne(e.getId());
			
			// 그냥 불러오므로 트랜잭션이 끝난 상태에서 받아온다. 다른 녀석이다 ! 
			System.out.println("같은 아이디로 불러온 녀석은 동일한? e==e2 :" +(e == e2));
			// 롬복이 자동으로 만들어주는 (equals 로 인해 같다 !! )  
			System.out.println("같은 아이디로 불러온 녀석은 동등한가? e.equals(e2) ? : "+ e.equals(e2));
			System.out.println("아이디값의 longValue가 같은가? "+(e.getId().longValue()==e2.getId().longValue()));
			
			System.out.println("==========  트랜잭션 내 테스트 ==============");
			Long empId = empService.identity();
			System.out.println("==========  리스트 내 테스트 ==============");
			empService.listTest(empId);
			
			System.out.println("부서 생성! 후 배치한다 !! ");
			Dept dept = new Dept();
			dept.setName("IT");
			
			deptRepository.save(dept);
			empService.assignDept(dept);
			
			System.out.println("======  직원쪽에서 이름 변경하고 부서에서 이름 확인하기 ======");
			empService.accessDifferentObjectGraph(dept, e);
			
			System.out.println("===== 이늄테스트! =====");
			empService.enumTest();
			
			
		};
	}
	
	@Service
	@Transactional
	public static class EmpService{
		@Autowired EmpRepository empRepository;
		@Autowired DeptRepository deptRepository;
		
		public Long identity() {
			// 여기서의 의도는 e에 이름을 계속 정하고, 마지막에 바뀐 값이 저장이 되는 지 알아본다.
			Emp e = new Emp();
			e.setName("arahansa");
			empRepository.save(e);
			e.setName("arahansa2");
			System.out.println("저장후에 파인드원을 하지만 트랜잭션 내에서는 셀렉트를 안한다! ");
			Emp e2 = empRepository.findOne(e.getId());
			e.setName("arahansa3");
			// 
			System.out.println("같은 트랜잭션 내 : 같은 아이디로 불러온 녀석은 동일한? e==e2 :" +(e == e2));
			// 롬복이 자동으로 만들어주는 (equals 로 인해 같다 !! )  
			System.out.println("같은 아이디로 불러온 녀석은 동등한가? e.equals(e2) ? : "+ e.equals(e2));
			System.out.println("아이디값의 longValue가 같은가? "+(e.getId().longValue()==e2.getId().longValue()));
			return e.getId();
		}
		
		// 아이디값을 하나를 받아 가지고 있고 리스트로 전체를 받아서
		// 리스트의 아이템과 원래 가지고 있던 요소의 아이템이 같은 지 확인한다. 
		public void listTest(Long empId){
			Emp e = empRepository.findOne(empId);
			List<Emp> es = empRepository.findAll();
			es.forEach(x->{
				if(x.getId() == empId){
					System.out.println("리스트내와 "+empId+" 값으로 받아온 객체 간의 비교  e == x :"+(x==e));
				}
			});
		}
		
		public void assignDept(Dept d){
			empRepository.findAll().forEach(e -> {
				e.setDept(d);
			});
			empRepository.findAll().forEach(System.out::println);
		}
		
		public void accessDifferentObjectGraph(Dept dept, Emp e){
			Dept d = deptRepository.findOne(dept.getId());
			Emp e2 = empRepository.findOne(e.getId());
			
			e2.getDept().setName("IT2");
			System.out.println("객체에서 부서로 가서 이름을 바꾸면 부서의 이름이 바뀐다~ e2.getDept.setName후 d.getName = IT2 : "+d.getName());		
		}
		public void enumTest() {
			Emp e = new Emp();
			e.setName("kim");
			e.setLevel2(Level2.SILVER);
			empRepository.save(e);
		}
		
		public void nPlusOneTest() {
			List<Emp> e = empRepository.findAll();
			e.forEach(s -> {
				if(s.getDept() != null){
					System.out.println(s.getDept().getName());
				}
			});
		}



	}
}
