package com.bit.geha.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.bit.geha.dao.MemberDao;
import com.bit.geha.dto.MemberDto;
import com.bit.geha.security.SecurityMember;

import lombok.AllArgsConstructor;
@Component
@AllArgsConstructor
public class SocialService {
	MemberDao memberDao;
	
	public UsernamePasswordAuthenticationToken doAuthentication(MemberDto memberDto) {
		if (memberDao.findById(memberDto.getId())!=null) {
	        // 기존 회원일 경우에는 데이터베이스에서 조회해서 인증 처리
	        final User user = new SecurityMember(memberDto);
	        
	        return setAuthenticationToken(user);
	    } else {
	        // 새 회원일 경우 회원가입 이후 인증 처리
	    	memberDao.insertUser(memberDto);
	    	memberDao.userAuth(memberDto.getId());
	        final User user = new SecurityMember(memberDto);
	        return setAuthenticationToken(user);
	    }
	}
	
	 private UsernamePasswordAuthenticationToken setAuthenticationToken(Object user) {
	        return new UsernamePasswordAuthenticationToken(user,
	        		null, getAuthorities("ROLE_USER"));
	    }
	 
	 private Collection<? extends GrantedAuthority> getAuthorities(String role) {
	        List<GrantedAuthority> authorities = new ArrayList<>();
	        authorities.add(new SimpleGrantedAuthority(role));
	        return authorities;
	    }
}