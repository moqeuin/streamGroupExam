package streamGroupExam;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamPartitionExam {

	public static void main(String[] args) {
		
		Student[] stu = { new Student("김하나", false, 3, 12, 89),
					      new Student("김둘", true, 1, 12, 84),
					      new Student("김셋", true, 3, 3, 53),
					      new Student("김넷", false, 2, 3, 97),
					      new Student("김다섯", false, 2, 1, 74),
					      new Student("김여섯", true, 1, 3, 81),
					      new Student("김일곱", false, 2, 2, 77)
					};
		Stream<Student> stream = Arrays.stream(stu);
		
		//  파티션 바이 : 지정한 기준을 가지고 key로 사용해서 맵으로 반환사킨다, 반환형은 Predicate(반환값이 boolean)
		
		// 기본 분할
		Map<Boolean, List<Student>> stuBySex =  stream.collect(Collectors.partitioningBy(Student::isMale));
		
		List<Student> list1 = stuBySex.get(true);
		List<Student> list2 = stuBySex.get(false);
		
		for (Student s : list1) {
			System.out.println(s);
		}
		/*
		 	Student [name=김둘, isMale=true, hak=1, ban=12, score=84]
			Student [name=김셋, isMale=true, hak=3, ban=3, score=53]
			Student [name=김여섯, isMale=true, hak=1, ban=3, score=81]
		 */
		for (Student s : list2) {
			System.out.println(s);
		}
		/*
		 	Student [name=김하나, isMale=false, hak=3, ban=12, score=89]
			Student [name=김넷, isMale=false, hak=2, ban=3, score=97]
			Student [name=김다섯, isMale=false, hak=2, ban=1, score=74]
			Student [name=김일곱, isMale=false, hak=2, ban=2, score=77]
		 */
		
		// 기본 분할 + 통계정보
		
		stream = Arrays.stream(stu);
		Map<Boolean, Long> stuNumSex = stream.collect(Collectors.partitioningBy(Student::isMale, Collectors.counting()));
		
		System.out.println(" 남학생 수 : " + stuNumSex.get(true) + " 여학생 수 : " + stuNumSex.get(false));
		//  남학생 수 : 3 여학생 수 : 4
		
		stream = Arrays.stream(stu);
		Map<Boolean, Long> stuSumSex = stream.collect(Collectors.partitioningBy(Student::isMale, Collectors.summingLong(Student::getScore)));
		System.out.println(" 남학생 총점 : " + stuSumSex.get(true) + " 여학생 총점 : " + stuSumSex.get(false));
		//  남학생 총점 : 218 여학생 총점 : 337
		
		stream = Arrays.stream(stu);
		Map<Boolean, Optional<Student>> topScoreBySex = stream
								.collect(Collectors
								.partitioningBy(Student::isMale, Collectors
								.maxBy(Comparator.comparingInt(Student::getScore))));
		System.out.println(" 남학생 1등 : " + topScoreBySex.get(true));
		System.out.println(" 여학생 1등 : " + topScoreBySex.get(false));
		/*
		 maxBy의 반환값이 Optional<T>이다
		  남학생 1등 : Optional[Student [name=김둘, isMale=true, hak=1, ban=12, score=84]]
 		  여학생 1등 : Optional[Student [name=김넷, isMale=false, hak=2, ban=3, score=97]]	
		*/
		
		stream = Arrays.stream(stu);
		Map<Boolean, Student> topScoreBySex2 = stream
										.collect(Collectors
										.partitioningBy(Student::isMale, Collectors.collectingAndThen(
										Collectors.maxBy(Comparator.comparingInt(Student::getScore))
										,Optional::get)) 
								);
		System.out.println(" 남학생 1등 : " + topScoreBySex2.get(true));
		System.out.println(" 여학생 1등 : " + topScoreBySex2.get(false));
		
		/*
		 collectingAndThen을 사용
		  남학생 1등 : Student [name=김둘, isMale=true, hak=1, ban=12, score=84]
 		  여학생 1등 : Student [name=김넷, isMale=false, hak=2, ban=3, score=97]
		 
		*/
		
		stream = Arrays.stream(stu);		
		
		Map<Boolean, Map<Boolean, List<Student>>> failedStuBySex =
		stream.collect(Collectors.partitioningBy(Student::isMale,
					   Collectors.partitioningBy( s -> s.getScore() < 80)));
						// 두 번째 파티션바이는 80점 아래인 (람다식이 true) 객체만 가져온다.
		List<Student> failedMale = failedStuBySex.get(true).get(true);
		List<Student> failedfeMale = failedStuBySex.get(false).get(true);
		
		for (Student s : failedMale) {
			System.out.println(s);
		}
		// Student [name=김셋, isMale=true, hak=3, ban=3, score=53]
		for (Student s : failedfeMale) {
			System.out.println(s);
		}
		// Student [name=김다섯, isMale=false, hak=2, ban=1, score=74]
		// Student [name=김일곱, isMale=false, hak=2, ban=2, score=77]
	}

}


class Student{
	
	String name;
	boolean isMale;
	int hak;
	int ban;
	int score;
	public Student(String name, boolean isMale, int hak, int ban, int score) {
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