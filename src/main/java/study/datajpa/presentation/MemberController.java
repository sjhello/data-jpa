package study.datajpa.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping(value = "/members/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String findMemberNotUserDomainClassConverter(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping(value = "/domain/member/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String findMemberUseDomainClassConverter(@PathVariable("id") Member member) {
        return member.getUsername();
    }
}
