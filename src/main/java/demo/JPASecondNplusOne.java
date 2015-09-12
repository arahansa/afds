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

import demo.JPASecondStudy.EmpService;
import demo.domain.Dept;
import demo.domain.Emp;
import demo.domain.Level2;
import demo.repository.DeptRepository;
import demo.repository.EmpRepository;

@SpringBootApplication
public class JPASecondNplusOne {

	public static void main(String[] args) {
		SpringApplication.run(JPASecondNplusOne.class, args);
	}
	
	@Autowired EmpRepository empRepository;
	@Autowired DeptRepository deptRepository;
	@Autowired EmpService empService;
	@Bean
    InitializingBean hello(){
		return ()->{
			
			
			empService.insertDatas();
			System.out.println("===== 조인 n+1 테스트! (여기는 좌라락 나온다)=== ");
			empService.nPlusOneTestBad();
			System.out.println("=====  조인 페치 테스트! ======");
			empService.nPlusOneTestGood();
		};
	}
	
	@Service
	@Transactional
	public static class EmpService{
		@Autowired EmpRepository empRepository;
		@Autowired DeptRepository deptRepository;
		
		public void insertDatas(){
			empRepository.deleteAll();
			deptRepository.deleteAll();
			
			Dept dept = new Dept();
			dept.setName("IT");
			Dept dept2 = new Dept();
			dept2.setName("IT2");
			Dept dept3 = new Dept();
			dept3.setName("IT3");
			Dept dept4 = new Dept();
			dept4.setName("IT4");
			deptRepository.save(Arrays.asList(dept,dept2,dept3, dept4));
			
			Emp e = new Emp();
			e.setName("arahansa");
			e.setDept(dept);
			Emp e2 = new Emp();
			e2.setName("arahansa01");
			e2.setDept(dept2);
			Emp e3 = new Emp();
			e3.setName("arahansa02");
			e3.setDept(dept3);
			Emp e4 = new Emp();
			e4.setName("arhansa04");
			e4.setDept(dept4);
			
			empRepository.save(Arrays.asList(e, e2, e3, e4));
			
		}
		
		public void nPlusOneTestBad() {
			List<Emp> e = empRepository.findAll();
			e.forEach(s -> {
				if(s.getDept() != null){
					System.out.println(s.getDept().getName());
				}
			});
		}
		
		public void nPlusOneTestGood(){
			List<Emp> e = empRepository.findAll2();
			e.forEach(s -> {
				if(s.getDept() != null){
					System.out.println(s.getDept().getName());
				}
			});
		}



	}
}
