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
import study.datajpa.repository.MemberRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8889"})
@ActiveProfiles("test")
class MemberControllerTest {

    @Autowired
    MemberRepository memberRepository;

    @LocalServerPort
    private int port;

    private Long memberId;

    @BeforeEach
    public void setUp() {
        RestAssured.port = this.port;
        this.memberId = saveMember();
    }

    private Long saveMember() {
        Member member = new Member("sjhello", 10);
        return memberRepository.save(member).getId();
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
}