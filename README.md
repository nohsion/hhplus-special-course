# 특강 신청 서비스

# 1. Entity-Relationship Diagram (ERD)

![img.png](assets/img.png)

## 1-1. user 엔티티

`user` 엔티티를 `student`와 `instructor`로 분리하지 않았습니다.

- 유연성: 항해플러스 토요일 특강은 누구든 강연자로 나설 수 있습니다. 또한, 특강이라는 특성상 이번주에는 수강생이지만 다음주에는 강연자가 될 수 있기 때문에 명확히 구분하지 않았습니다.
- 확장성: 만약 특강이 아닌 일반 강의가 추가된다면 구체적인 강사 정보(ex. 강사 소개, 강사 커리어 등)이 필요하기에 추가 엔티티가 필요합니다.

## 1-2. user-course 관계

`user_course_enrollment` 엔티티를 통해 user와 course의 다대다 관계를 구현했습니다.

- 한 학생이 여러 특강을 수강하고, 한 특강에 여러 학생이 수강할 수 있는 구조입니다.
- 중복 신청 방지: `(student_id, course_id)` 컬럼을 `unique`로 설정하여 특강 중복 신청을 방지합니다.

## 1-3. course 엔티티

`course` 엔티티는 특강 정보를 담고 있습니다.

- 수강 인원 제한: `max_students` 컬럼을 통해 수강 인원을 제한합니다. 매 특강(ex. 온라인, 오프라인 장소에 따라)마다 인원이 바뀔 수 있기 때문에 DB default 값은 설정하지 않습니다.
    - 날짜는 `yyyy-MM-dd`(e.g. 2024-12-25) 형식이기 때문에 `CHAR(10)`으로 고정시켰습니다.
- 특강은 토요일마다 열립니다. 단, 평일이나 일요일에 특강이 열릴 수도 있기 때문에 관련 constraint는 추가하지 않았습니다. (확장 가능 고려)

# 2. Requirements

RDBMS는 MariaDB를 Docker로 띄워 사용합니다.

## 2-1. MariaDB Container 설치

1. Docker Image 다운로드
   ```
   docker pull mariadb:latest
   ```
2. Docker Container 실행
   ```
   docker run -d --name hh-mariadb -e MARIADB_ROOT_PASSWORD=1234 -p 23306:3306 mariadb:latest
   ```

3. MariaDB 접속
   ```
   docker exec -it hh-mariadb mariadb -uroot -p
   ```

4. database 스키마 생성
   ```
   create database hh_special_course;
   use hh_special_course;
   ```

5. ddl 실행
   ```
   source /src/main/resources/ddl/hh_special_course.sql
   ```

### 주의사항

1. dbdiagram 함수 사용시에는 반드시 백틱 사용해야 한다. e.g. `current_timestamp()`

# 3. 용어 사전 (유비쿼터스 언어)

프로젝트의 모든 곳(회의, 기획, 디자인, 개발 등)에서 모두 이해하는 도메인 용어를 사용하기 위해 용어 사전을 정의합니다.

한글 이름과 영문 이름을 모두 정하고, 용어 사전은 지속적으로 업데이트해야 합니다.

## 유저

| 한글명 | 영문명        | 설명                                          |
|-----|------------|---------------------------------------------|
| 유저  | user       | 항해 플러스 특강 서비스를 이용하는 사용자. 수강생과 강연자 모두를 지칭한다. |
| 수강생 | student    | 항해 플러스 특강을 수강하는 유저. (a.k.a. 특강 신청자)         |
| 강연자 | instructor | 항해 플러스 특강을 진행하는 유저                          |

## 특강

| 한글명   | 영문명               | 설명                                        |
|-------|-------------------|-------------------------------------------|
| 특강    | course            | 항해 플러스의 특강을 지칭한다.                         |
| 특강 신청 | course enrollment | 항해 플러스 특강을 신청하는 행위를 지칭한다.                 |
| 정원    | max_students      | 특강의 최대 수강 인원                              |
| 특강 날짜 | course date       | 특강이 열리는 날짜 (`yyyy-mm-dd`) e.g. 2024-12-25 |

# 4. 분산서버 동시성 제어 방식

## 4-1. JPA lock

### 낙관적 락 (Optimistic Lock)

- 버전(`@Version`) 관리로 동시성을 제어하는데, 업데이트 시 버전이 다르면(버전 충돌) `OptimisticLockException`이 발생합니다.
- 따라서, 재시도 로직을 애플리케이션 레벨에서 구현해야 합니다.
  - ex: "최대 100번까지 재시도 하되, delay를 10ms씩 주기"
- 최초 요청 순서가 아닌 retry 타이밍에 따라 결정됩니다. (순서 보장 X)

**결론**: Retry 로직을 추가하여 동시성 제어가 가능하긴 하지만, 충돌 가능성이 높은 선착순 서비스에는 적합하지 않다고 판단했습니다. (오히려, 트랜잭션 작업이 오래 걸린다면 유리하다고 생각합니다.)

### 비관적 락 (Pessimistic Lock)

- `select for update` 쿼리문을 통해서 DB의 특정 레코드를 잠금 처리하는 방식입니다.
- 종류
  - `LockModeType.PESSIMISTIC_READ`(읽기 잠금, 공유락): 사용 불가
    - 이유: "특강 잔여 수 체크 -> 특강 신청"을 하기 때문에, 중간에 다른 사람이 읽어간다면 문제가 발생할 수 있습니다.
    - ex: 특강 잔여 수가 1개 남았는데, 동시에 2명이 신청하면 둘다 잔여 수는 1로 read해갔기 때문에 인원 수를 초과하게 됩니다.
  - `LockModeType.PESSIMISTIC_WRITE`(쓰기 잠금, 배타락): 사용 가능
    - 이유: 특강 신청 중에 다른 사람이 잔여 수를 확인해서 업데이트한다면 정원을 초과할 수 있기 때문에, 배타적 잠금으로 해결하여야 합니다.

결론: 선착순 서비스에 `PESSIMISTIC_WRITE`가 적합하다고 판단했습니다.

## 4-2. User-level Lock (Named Lock)

- MySQL(MariaDB)에서 제공하는 `GET_LOCK(str, timeout)`, `RELEASE_LOCK(str)` 함수를 사용하여 이름을 통한 lock 획득, 해제를 통해 동시성을 제어합니다.
- DB 레코드나 테이블 level이 아닌, 사용자가 지정한 문자열 이름을 통해 lock을 관리합니다.
- 

### 고려사항
- 많은 스레드가 Lock 요청을 시도한다면, DB 커넥션 풀의 대부분의 커넥션을 사용할 수 있습니다. 따라서 Named Lock만을 위한 별도의 커넥션 풀을 사용하는 것이 좋을 것 같습니다.
- 동시에 Lock 획득 대기 시, 순서를 보장하지 않습니다.

## 비관적 락이 아닌 Named Lock을 채택한 이유

1. 비관적 락을 사용할 경우, 단순히 특강 잔여 수를 조회할 때도 레코드 단에 Lock이 걸려있어서 대기해야 하는 문제가 있다고 판단했습니다.
2. 비관적 락은 특강 신청자 수(잔여 수) 컬럼을 추가하는 반정규화를 해야 하기 때문에, 데이터 무결성이 깨질 수 있다고 판단했습니다.
   - 질문: 테스트가 보장되었다는 전제 하에, 동시성 제어를 했으니 매핑테이블의 row수와 컬럼 값이 매번 일치한다고 가정하는 건가요? 혹시 사람이 실수로 DB를 건드릴 수도 있지 않을까 생각이 들어서요.
3. 애플리케이션 레벨에서 세밀하게 Lock을 제어할 수 있다고 판단했습니다.


# 참고

- https://dkswnkk.tistory.com/697
- https://cheese10yun.github.io/spring-guide-directory/
- https://techblog.woowahan.com/2631/
- https://tecoble.techcourse.co.kr/post/2023-08-16-concurrency-managing/
- https://ksh-coding.tistory.com/125