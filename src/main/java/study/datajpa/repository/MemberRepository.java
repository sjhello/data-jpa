package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, Integer age);

    @Query(name = "Member.findByUsername")      // 생략 가능
    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") Integer age);

    @Query("select m from Member m where m.username = :username")
    List<Member> findUsers(@Param("username") String username);

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    @Query("select m.username from Member m")
    List<String> findUsernames();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.age = :age")
    Page<Member> findByAgeAsPage(@Param("age") Integer age, Pageable pageable);

    @Query(value = "select m from Member m", countQuery = "select count(m.username) from Member m")
    Page<Member> findMemberAllCountBy(Pageable pageable);

    @Query("select m from Member m where m.age = :age")
    Slice<Member> findByAgeAsSlice(@Param("age") Integer age, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = :age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") Integer age);

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @Query("select m from Member m")
    @EntityGraph(attributePaths = {"team"})
    List<Member> findMembersEntityGraph();

    @EntityGraph(attributePaths = {"team"})
    List<Member> findByUsernameAndAge(@Param("username") String username, @Param("age") Integer age);

    @EntityGraph("Member.all")
    @Query("select m from Member m")
    List<Member> findMembersNamedEntityGraph();

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    @Query("select m from Member m where m.username = :username")
    Member findMemberByUsername(@Param("username") String username);

    @QueryHints(value = {@QueryHint(name = "org.hibernate.readOnly", value = "true")}, forCounting = true)
    // forCounting = true -> 반환타입으로 Page를 사용하면 추가로 호출하는 페이징을 위한 count 쿼리도 쿼리 힌트 적용
    Page<Member> findByUsername(@Param("username") String username, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select m from Member m where m.username = :username")
    List<Member> findByUsernameUsingLockMode(String username);
}
