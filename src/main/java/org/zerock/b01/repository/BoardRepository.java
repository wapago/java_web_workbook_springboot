package org.zerock.b01.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.b01.domain.Board;
import org.zerock.b01.repository.search.BoardSearch;

import java.util.Optional;

//Spring Data JPA를 이용할 때는 JpaRepository라는 인터페이스를 이용해서
//인터페이스 선언만으로 데이터베이스 관련 작업을 어느 정도 처리할 수 있다.
//마치 MyBatis를 이용할 때 매퍼 인터페이스만을 선언하는 것과 유사하다.

// JpaRepository인터페이스를 상속할 때는 엔티티 타입과 @Id타입을 지정해 주어야 한다
public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {

    @Query(value = "select now()", nativeQuery = true)
    String getTime();

    @EntityGraph(attributePaths = {"imageSet"}) // 같이 로딩해야 하는 속성 명시
    @Query("select b from Board b where b.bno =:bno")
    Optional<Board> findByIdWithImages(@Param("bno") Long bno);
}
