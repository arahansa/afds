package demo;

import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import demo.domain.Dept;
import demo.domain.Emp;
import demo.repository.DeptRepository;
import demo.repository.EmpRepository;
import javassist.bytecode.SignatureAttribute.ClassSignature;

@SpringBootApplication
public class SpringStudyJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringStudyJpaApplication.class, args);
    }
    
    @Autowired EmpRepository repoEmp;
    @Autowired DeptRepository repoDept;
    @Autowired PlatformTransactionManager tm;
    @Autowired EmpService servEmp;
    
    @Bean
    InitializingBean hello(){
    	return () -> {
    		System.out.println("happy coding!");
    		repoEmp.deleteAll();
    		repoDept.deleteAll();
    		
    		// 01. 직원 Arahansa와 May 를 만들어서 저장해본다 ! 
    		Emp emp = new Emp();
    		emp.setBirth(new GregorianCalendar(1999, 2, 20).getTime());
    		emp.setName("Arahansa");
    		emp.setLevel(50);
    		
    		Emp emp2 = new Emp();
    		emp2.setBirth(new GregorianCalendar(2000, 1, 20).getTime());
    		emp2.setName("May");
    		emp2.setLevel(25); // huhu
    		
    		repoEmp.save(emp);
    		repoEmp.save(emp2);
    		
    		System.out.println("현재 있는 직원들");
    		repoEmp.findAll().forEach(System.out::println);
    		
    		// 02. Spring data jpa  의 방식으로 직원을 불러와본다 ! 
    		System.out.println("이름이 Arahansa인 직원");
    		Emp arahansa = repoEmp.findByName("Arahansa");
    		System.out.println(arahansa);
    		
    		
    		// 03. @Query 로 불러와 본다!
    		System.out.println("레벨이 25인 직원");
    	    List<Emp> emps = repoEmp.findLevel(25);
    		emps.forEach(System.out::println);
    		Emp emp3 = emps.get(0);

    		// 04. 일차캐시 테스트!
    		// 메이님의 레벨을 변경해보자 ! 
    		new TransactionTemplate(tm).execute((s) -> {
    			Emp emp4 = repoEmp.findOne(emp3.getId());
    			emp4.setLevel(99); // update 발생 !!
    			
    			System.out.println("킴님 저장!");
    			Emp kim = new Emp();
    			kim.setName("Kim");
    			repoEmp.save(kim);
    			
    			kim.setLevel(50); // kim 레벨 변경 !!
    			return null;
    		});
    		
    		// 변경된 레벨 체크 ! (메이님, 킴님)
    		Emp may = repoEmp.findByName("May");
    		System.out.println("메이"+may);
    		Emp kim = repoEmp.findByName("Kim");
    		System.out.println("킴"+kim);
    		
    		// 부서별 도입!!
    		Dept dept = new Dept();
    		dept.setName("IT");
    		
    		Dept dept2 = new Dept();
    		dept2.setName("HR");
    		
    		repoDept.save(dept);
    		repoDept.save(dept2);
    		
    		// 관계맺기!
    		servEmp.relation();
    		servEmp.changeDepartmentName();
    		servEmp.checkDepartmentName();
    		
    		// 부서를 맺고 여러 명의 인원을 선택된 부서에서 불러오자 ! 
    		servEmp.setAllMemberDeptName();
    		servEmp.showItmembers();
    	};
    }
    
    @Service
    @Transactional
    public static class EmpService{
    	@Autowired EmpRepository repoEmp;
    	@Autowired DeptRepository repoDept;
    	
    	public void relation(){
    		Emp arahansa  = repoEmp.findByName("Arahansa");
    		Dept d = repoDept.findByName("IT");
    		arahansa.setDept(d); // 자동 변경!! 
    		System.out.println("부서가 배치된 아라한사 :"+arahansa);
    	}
    	
    	public void changeDepartmentName(){
    		Emp arahansa  = repoEmp.findByName("Arahansa");
    		System.out.println("현재 부서 :"+arahansa);
    		arahansa.getDept().setName("IT2");
    	}
    	public void checkDepartmentName(){
    		Emp arahansa  = repoEmp.findByName("Arahansa");
    		System.out.println("현재 부서 :"+arahansa);
    	}
    	
    	public void setAllMemberDeptName(){
    		Dept d = repoDept.findByName("IT2");
    		d.setName("IT");
    		System.out.println("전체인원 ");
    		repoEmp.findAll().forEach(n-> n.setDept(d));
    	}
    	
    	public void showItmembers(){
    		Dept d = repoDept.findByName("IT");
    		System.out.println("IT 부서 인원");
    		System.out.println(d.getEmps());
    	}
    }
    
}
