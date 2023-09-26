package study.datajpa.presentation;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;
import study.datajpa.repository.MemberRepository;
import study.datajpa.repository.TeamRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8889"})
@ActiveProfiles("test")
class MemberControllerTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @LocalServerPort
    private int port;

    private Long memberId;

    @BeforeEach
    public void setUp() {
        RestAssured.port = this.port;
        this.memberId = saveMemberAndTeam();
    }

    private Long saveMemberAndTeam() {
        Member member = new Member("sjhello", 10, saveTeam());
        return memberRepository.save(member).getId();
    }

    private Team saveTeam() {
        Team teamA = new Team("teamA");
        return teamRepository.save(teamA);
    }

    @Test
    @DisplayName("회원을 조회한다 - 도메인 클래스 컨버터 미사용")
    void findMemberTest() {
        RestAssured.given().log().all()
                .when().get("/members/" + memberId)
                .then().log().all();
    }

    @Test
    @DisplayName("회원을 조회한다 - 도메인 클래스 컨버터 사용")
    void findMemberDominClassConverterTest() {
        RestAssured.given().log().all()
                .when().get("/domain/member/" + memberId)
                .then().log().all();
    }

    @Test
    @DisplayName("회원을 조회한다 - 페이징")
    void pagingMembersTest() {
        RestAssured.given().log().all()
                .when().get("/members?page=0&size=3&sort=username,desc")
                .then().log().all();
    }

    @Test
    @DisplayName("회원을 조회한다 - PageDefault")
    void pageDefaultMembersTest() {
        // 파라미터를 보내지 않으면 @PageableDefault에서 처리함
        RestAssured.given().log().all()
                .when().get("/members_page")
                .then().log().all();
    }

    @Test
    @DisplayName("회원을 조회한다 - Page.map()")
    void pageMapMembers() {
        RestAssured.given().log().all()
                .when().get("/members_dto?page=0&size=3&sort=username,desc")
                .then().log().all();
    }
}