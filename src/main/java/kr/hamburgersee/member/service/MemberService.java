package kr.hamburgersee.member.service;

import kr.hamburgersee.member.Member;
import kr.hamburgersee.member.MemberDto;

public interface MemberService {
    Long join(MemberDto memberDto);
    MemberDto findById(Long id);
    Member convertDtoToMember(MemberDto memberDto);
    MemberDto convertMemberToDto(Member member);
}
