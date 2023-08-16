package study.datajpa.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    @DisplayName("회원을 생성한다")
    void 회원을_생성한다() {
        Member sjhello = new Member("sjhello", 30);

        memberJpaRepository.save(sjhello);

        Optional<Member> findMember = memberJpaRepository.findById(sjhello.getId());
        assertThat(findMember.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("회원 조회에 실패한다")
    void 회원을_조회에_실패한다() {
        Optional<Member> findMember = memberJpaRepository.findById(2L);

        assertThat(findMember.isEmpty()).isTrue();
        assertThat(findMember.isPresent()).isTrue();    // fail, actual false
        assertThat(findMember.isPresent()).isFalse();    // success, the value is not present
    }

    @Test
    void 회원을_삭제한다() {
        Member sjhello = new Member("sjhello", 30);
        memberJpaRepository.save(sjhello);

        memberJpaRepository.delete(sjhello);

        List<Member> members = memberJpaRepository.findAll();
        assertThat(members.size()).isEqualTo(0);
    }

    @Test
    void 회원목록을_조회한다() {
        Member sjhello = new Member("sjhello", 30);
        Member sjhello2 = new Member("sjhello2", 30);
        memberJpaRepository.save(sjhello);
        memberJpaRepository.save(sjhello2);

        List<Member> members = memberJpaRepository.findAll();

        assertThat(members.size()).isEqualTo(2);
        assertThat(members).contains(sjhello, sjhello2);
    }

    @Test
    void 회원_수를_검증한다() {
        Member sjhello = new Member("sjhello", 30);
        Member sjhello2 = new Member("sjhello2", 30);
        memberJpaRepository.save(sjhello);
        memberJpaRepository.save(sjhello2);

        long count = memberJpaRepository.count();

        assertThat(count).isEqualTo(2);
    }
}