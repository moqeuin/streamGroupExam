package streamGroupExam;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class streamGroupingExam {
	
	public static void main(String[] args) {
		
		// 그룹핑 바이 : 파티션바이와 마찬가지로 스트림을 그룹핑하지만 반환형은 Function이다. 
		// value의 반환형 List<T>이다.
		
		Student2[] stu = { new Student2("김하나", false, 3, 1, 91),
			      new Student2("김둘", true, 1, 1, 84),
			      new Student2("김셋", true, 3, 2, 53),
			      new Student2("김넷", false, 2, 2, 97),
			      new Student2("김다섯", false, 2, 3, 74),
			      new Student2("김여섯", true, 1, 3, 81),
			      new Student2("김일곱", false, 2, 4, 77)
			};
		Stream<Student2> stream = Arrays.stream(stu);
		
		// collectingAndThen은 컬렉팅을 하면서 다음 실행을 할 수 있다.
		
		
		// 반별
		
		Map<Integer, List<Student2>> ban = stream.collect(Collectors
											.groupingBy(Student2::getBan));
		
		// 맵의 value로 리스트를 받는 방법
		for(List<Student2> s : ban.values()) {
			System.out.println(s);
		}
		
		// 성적별
		
		stream = Arrays.stream(stu);
		Map<Student2.Level, List<Student2>>  map2 = stream.collect( Collectors.groupingBy(
													s -> {
													if (s.score >= 100) return Student2.Level.HIGH;
													else if (s.score >= 70) return Student2.Level.MID;
													else return Student2.Level.LOW;}));
		// key만 추출해서 대응하는 value를 받는 방법
		TreeSet<Student2.Level> keySet = new TreeSet<>(map2.keySet());
		
		for (Student2.Level  key : keySet) {
			System.out.println("[" + key + "]");
			
			for (Student2 s : map2.get(key)) {
				System.out.println(s);
			}
			System.out.println();
		}
		
		// 성적별, 인원수
		
		stream = Arrays.stream(stu);
		Map<Student2.Level, Long> map3 = stream.collect(Collectors
										.groupingBy(
											s->{ 
											if(s.score >= 90) return Student2.Level.HIGH;
											else if(s.score >= 70) return Student2.Level.MID;
											else return Student2.Level.LOW;}, Collectors.counting()));
		
		for (Student2.Level key : map3.keySet()) {
			System.out.println( "[" + key + "]" +  map3.get(key)); // 4 1 2
		}
		
		// 학년, 반
		
		Map<Integer, Map<Integer, List<Student2>>> map4 = Stream.of(stu).
															collect(Collectors.groupingBy(Student2::getHak,
																	Collectors.groupingBy(Student2::getBan)
																	));
		for (Map<Integer, List<Student2>> m : map4.values()) {
			for (List<Student2> l : m.values()) {
				System.out.println(l);
			}
		}
		System.out.println();
		// 학년별, 반별 1등
		
		Map<Integer, Map<Integer, Student2>> map5 = Stream.of(stu).
															collect(Collectors.groupingBy(Student2::getHak,
															Collectors.groupingBy(Student2::getBan
															, Collectors.collectingAndThen(
															Collectors.maxBy(Comparator.comparingInt(Student2::getScore))
																,Optional::get	))));
		for (Map<Integer, Student2> m : map5.values()) {
			for (Student2 s : m.values()) {
				System.out.println(s);
			}
		}
		
		// 학년-반 , 점수별 그레이드
		Map<String, Set<Student2.Level>> map6 = Stream.of(stu).
												collect(
												Collectors.groupingBy(
													s->  s.getHak() + "-" + s.getBan(),
													Collectors.mapping(s -> { 
											if(s.score >= 90) return Student2.Level.HIGH;
											else if(s.score >= 70) return Student2.Level.MID;
											else return Student2.Level.LOW;}, Collectors.toSet() )
														)
												);
		Set<String> keySet2 = map6.keySet();
		
		for (String key : keySet2) {
			System.out.println(key + map6.get(key));
		}
															
	}
}


class Student2{
	
	String name;
	boolean isMale;
	int hak;
	int ban;
	int score;
	public Student2(String name, boolean isMale, int hak, int ban, int score) {
		super();
		this.name = name;
		this.isMale = isMale;
		this.hak = hak;
		this.ban = ban;
		this.score = score;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isMale() {
		return isMale;
	}
	public void setMale(boolean isMale) {
		this.isMale = isMale;
	}
	public int getHak() {
		return hak;
	}
	public void setHak(int hak) {
		this.hak = hak;
	}
	public int getBan() {
		return ban;
	}
	public void setBan(int ban) {
		this.ban = ban;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	@Override
	public String toString() {
		return "Student [name=" + name + ", isMale=" + isMale + ", hak=" + hak + ", ban=" + ban + ", score=" + score
				+ "]";
	}
	
	
	enum Level{ HIGH, MID, LOW }
}