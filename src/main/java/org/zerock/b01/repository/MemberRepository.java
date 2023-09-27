package org.zerock.b01.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.b01.domain.Member;

import java.util.Optional;

// - 로그인 시에 MemberRole을 같이 로딩할 수 있도록 메소드 추가
// - 직접 로그인 시에는 소셜서비스를 통해서 회원 가입된 회원들이 같은 패스워드를 가지므로
// 일반 회원들만 가져오도록 social 속성값이 false인 사용자들만을 대상으로 처리함.
public interface MemberRepository extends JpaRepository<Member, String> {

    @EntityGraph(attributePaths = "roleSet")
    @Query("select m from Member m where m.mid = :mid and m.social = false")
    Optional<Member> getWithRoles(@Param("mid") String mid);

    @EntityGraph(attributePaths = "roleSet")
    Optional<Member> findByEmail(String email);

    // 소셜로그인하면 무조건 패스워드가 1111을 인코딩한 값으로 저장되므로
    // 패스워드가 변경된 상황에서의 테스트가 불가능함, 따라서 패스워드를 수정하는 기능 추가
    @Modifying
    @Transactional
    @Query("update Member m set m.mpw = :mpw where m.email = :email")
    void updatePassword(@Param("mpw") String password, @Param("email") String email);
}
