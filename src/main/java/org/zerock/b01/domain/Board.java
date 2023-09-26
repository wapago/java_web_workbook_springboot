package org.zerock.b01.domain;

import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Log4j2
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "imageSet")
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 키생성 전략 : auto increment 이용, 데이터베이스에 위임(IDENTITY)
    private Long bno;

    @Column(length = 500, nullable = false) // 칼럼의 길이와 null허용여부
    private String title;

    @Column(length = 2000, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;

    @OneToMany(mappedBy = "board",
               cascade = {CascadeType.ALL}, // Board엔티티 객체의 모든 상태변화에 BoardImage객체들 역시 같이 변경
               fetch = FetchType.LAZY,
               orphanRemoval = true)    // 하위 엔티티의 참조가 더 이상 없는 상태가 되었을 때 실제 삭제가 이루어짐
    @Builder.Default
    @BatchSize(size = 20)
    private Set<BoardImage> imageSet = new HashSet<>();

    public void addImage(String uuid, String fileName) {
        log.info("uuid : " + uuid);
        log.info("fileName : " + fileName);
        BoardImage boardImage = BoardImage.builder()
                .uuid(uuid)
                .fileName(fileName)
                .board(this)
                .ord(imageSet.size())
                .build();

        imageSet.add(boardImage);
    }

    public void clearImages() {
        imageSet.forEach(boardImage -> boardImage.changeBoard(null));
        this.imageSet.clear();
    }

    public void change(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
