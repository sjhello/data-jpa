package study.datajpa.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

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
}