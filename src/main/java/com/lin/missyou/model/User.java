package com.lin.missyou.model;

import com.lin.missyou.model.BaseEntity.BaseEntity;
import com.lin.missyou.until.MapToJson;
import lombok.*;
import org.hibernate.annotations.Where;


import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Builder
@Where(clause = "delete_time is ull")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String openid;
    private String nickname;
    private Long unifyUid;
    private String email;
    private String password;
    private String mobile;


    @Convert(converter = MapToJson.class)
    private Map<String,Object> wxProfile;


}
