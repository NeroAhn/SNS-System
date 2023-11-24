package com.example.fastcampusmysql.domain.post.repository;

import com.example.fastcampusmysql.domain.member.entity.Member;
import com.example.fastcampusmysql.domain.post.dto.DailyPostInfo;
import com.example.fastcampusmysql.domain.post.dto.DailyPostRecordRequest;
import com.example.fastcampusmysql.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class PostRepository {

    private static final String TABLE = "Post";
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final RowMapper<DailyPostInfo> DAILY_POST_COUNT_MAPPER = (ResultSet rs, int rowNum) -> new DailyPostInfo(
            rs.getLong("memberId"),
            rs.getObject("createdDate", LocalDate.class),
            rs.getLong("cnt")
    );

    public Post save(Post post) {
        if (post.getId() == null) {
            return insert(post);
        }

        throw new UnsupportedOperationException("Post는 갱신을 지원하지 않습니다.");
    }

    private Post insert(Post post) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE)
                .usingGeneratedKeyColumns("id");
        SqlParameterSource params = new BeanPropertySqlParameterSource(post);
        var id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return Post.builder()
                .id(id)
                .memberId(post.getMemberId())
                .contents(post.getContents())
                .createdDate(post.getCreatedDate())
                .createdAt(post.getCreatedAt())
                .build();
    }

    public void bulkInsert(List<Post> posts) {
        var sql = String.format("INSERT INTO %s (memberId, contents, createdDate, createdAt) " +
                "VALUES (:memberId, :contents, :createdDate, :createdAt)", TABLE);

        SqlParameterSource[] params = posts
                .stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);

        namedParameterJdbcTemplate.batchUpdate(sql, params);
    }

    public List<DailyPostInfo> groupByCreatedDate(DailyPostRecordRequest request) {
        var sql = String.format("SELECT memberId, createdDate, count(id) as cnt" +
                "   FROM %s" +
                "   WHERE memberId = :memberId" +
                "   AND createdDate BETWEEN :startDate AND :endDate " +
                "   GROUP BY createdDate", TABLE);
        var params = new BeanPropertySqlParameterSource(request);
        return namedParameterJdbcTemplate.query(sql, params, DAILY_POST_COUNT_MAPPER);
    }

}
