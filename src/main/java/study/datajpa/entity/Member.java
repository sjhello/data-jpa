package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@NamedQuery(name = "Member.findByUsername",
            query = "select m from Member m where m.username = :username")
@NamedQuery(name = "Member.findByAge",
            query = "select m from Member m where m.age = :age")
@NamedEntityGraph(name = "Member.all", attributeNodes = @NamedAttributeNode("team"))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})
public class Member extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;

    private Integer age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String username) {
        this(username, 0);
    }

    public Member(String username, Integer age) {
        this(username, age, null);
    }

    public Member(String username, Integer age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
