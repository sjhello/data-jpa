package study.datajpa.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @Test
    @DisplayName("이름과_나이로_회원을_조회한다")
    void findByUserNameAndAgeGreaterThan() {
        Member sjhello = new Member("sjhello", 30);
        Member sjhello2 = new Member("sjhello", 40);
        Member sjhello3 = new Member("sjhello", 50);
        Member sjhello4 = new Member("sjhello", 60);

        memberRepository.save(sjhello);
        memberRepository.save(sjhello2);
        memberRepository.save(sjhello3);
        memberRepository.save(sjhello4);

        List<Member> members = memberRepository.findByUsernameAndAgeGreaterThan("sjhello", 30);

        assertThat(members).hasSize(3);
    }

    @Test
    @DisplayName("이름으로 회원을 조회하다 - Named Query")
    void findByUsername() {
        Member sjhello = new Member("sjhello", 30);
        memberRepository.save(sjhello);

        List<Member> members = memberRepository.findByUsername(sjhello.getUsername());

        assertThat(members).hasSize(1);
    }

    @Test
    void findUser() {
        Member sjhello = new Member("sjhello", 30);
        Member bailey = new Member("bailey", 30);

        memberRepository.save(sjhello);
        memberRepository.save(bailey);

        List<Member> members = memberRepository.findUser("sjhello", 30);
        assertThat(members).hasSize(1);
    }

    @Test
    @DisplayName("이름으로 회원을 조회한다 - 파라미터 바인딩")
    void 회원을_조회한다() {
        Member sjhello = new Member("sjhello", 30);
        Member bailey = new Member("sjhello", 20);

        memberRepository.save(sjhello);
        memberRepository.save(bailey);

        List<Member> members = memberRepository.findUsers("sjhello");
        assertThat(members).contains(sjhello, bailey);
    }

    @Test
    @DisplayName("회원이름으로 검색한다 - 컬렉션 파라미터 바인딩")
    void 회원이름으로_검색한다() {
        Member sjhello = new Member("sjhello");
        Member bailey = new Member("bailey");
        memberRepository.save(sjhello);
        memberRepository.save(bailey);

        List<Member> members = memberRepository.findByNames(List.of("sjhello", "bailey"));

        assertThat(members).contains(sjhello, bailey);
    }

    @Test
    @DisplayName("회원이름을_검색한다 - 단순 값 조회")
    void 회원이름을_검색한다() {
        Member sjhello = new Member("sjhello");
        Member bailey = new Member("bailey");

        memberRepository.save(sjhello);
        memberRepository.save(bailey);

        List<String> usernames = memberRepository.findUsernames();
        assertThat(usernames).contains("sjhello", "bailey");
    }

    @Test
    @DisplayName("회원의_팀을_검색한다 - DTO로 직접 조회")
    void 회원의_팀을_검색한다() {
        Team team = new Team("teamA");
        Member sjhello = new Member("sjhello", 30, team);
        teamRepository.save(team);
        memberRepository.save(sjhello);

        List<MemberDto> memberDto = memberRepository.findMemberDto();

        assertThat(memberDto).hasSize(1);
    }

    @Test
    @DisplayName("회원 페이징")
    void 회원을_페이징하여_조회한다() {
        Integer age = 20;
        memberRepository.save(new Member("sjhello1", age));
        memberRepository.save(new Member("sjhello2", age));
        memberRepository.save(new Member("sjhello3", age));
        memberRepository.save(new Member("sjhello4", age));
        memberRepository.save(new Member("sjhello5", age));

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        Page<Member> pageMember = memberRepository.findByAge(age, pageRequest);

        assertThat(pageMember.getContent()).hasSize(3);     // 조회된 데이터 수
        assertThat(pageMember.getTotalElements()).isEqualTo(5);     // 총 데이터 수
        assertThat(pageMember.getTotalPages()).isEqualTo(2);        // 총 몇 페이지야?
        assertThat(pageMember.isFirst()).isTrue();      // 첫번쨰 페이지?
        assertThat(pageMember.hasNext()).isTrue();      // 다음 페이지 있어?
    }

    @Test
    void 회원의_count를_조회한다() {
        Integer age = 20;
        memberRepository.save(new Member("sjhello1", age));
        memberRepository.save(new Member("sjhello2", age));
        memberRepository.save(new Member("sjhello3", age));

        PageRequest pageRequest = PageRequest.ofSize(2);
        Page<Member> memberAllCountBy = memberRepository.findMemberAllCountBy(pageRequest);

        assertThat(memberAllCountBy.getTotalElements()).isEqualTo(3);
    }
}