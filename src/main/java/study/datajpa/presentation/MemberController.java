package study.datajpa.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
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

    @GetMapping(value = "/members", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<Member> members(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    @GetMapping(value = "/members_page", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<Member> membersPage(@PageableDefault(size = 12, sort = "username", direction = Sort.Direction.ASC) Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    @GetMapping(value = "/members_dto", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<MemberDto> membersDto(Pageable pageable) {
        return memberRepository.findAll(pageable).map(MemberDto::new);
    }
}
