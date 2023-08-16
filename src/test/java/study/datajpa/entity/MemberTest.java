package study.datajpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Test
    @Transactional
    @Rollback(value = false)
    public void createMember() {
        Team teamA = new Team("A Team");
        Team teamB = new Team("B Team");
        em.persist(teamA);
        em.persist(teamB);

        Member sjhello = new Member("sjhello", 30, teamA);
        Member bailey = new Member("bailey", 20, teamB);

        em.persist(sjhello);
        em.persist(bailey);

        em.flush();
        em.clear();

        List<Long> teamAMemberIds = em.createQuery("select m from Member m where m.team = :team", Member.class)
                .setParameter("team", teamA)
                .getResultList()
                .stream()
                .map(Member::getId)
                .collect(Collectors.toList());

        List<Long> teamBMemberIds = em.createQuery("select m from Member m where m.team = :team", Member.class)
                .setParameter("team", teamB)
                .getResultList()
                .stream()
                .map(Member::getId)
                .collect(Collectors.toList());


        assertThat(teamAMemberIds).contains(sjhello.getId());
        assertThat(teamBMemberIds).contains(bailey.getId());
    }
}