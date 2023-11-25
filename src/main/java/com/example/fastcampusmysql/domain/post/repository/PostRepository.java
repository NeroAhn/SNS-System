package com.example.fastcampusmysql.domain.post.repository;

import com.example.fastcampusmysql.domain.post.dto.DailyPostInfo;
import com.example.fastcampusmysql.domain.post.dto.DailyPostRecordRequest;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.util.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    private static final RowMapper<Post> POST_ROW_MAPPER = (ResultSet rs, int rowNum) -> Post.builder()
            .id(rs.getLong("id"))
            .memberId(rs.getLong("memberId"))
            .contents(rs.getString("contents"))
            .likeCount(rs.getLong("likeCount"))
            .createdDate(rs.getObject("createdDate", LocalDate.class))
            .createdAt(rs.getObject("createdAt", LocalDateTime.class))
            .version(rs.getLong("version"))
            .build();

    public Post save(Post post) {
        if (post.getId() == null) {
            return insert(post);
        } else {
            return update(post);
        }
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

    public Page<Post> findAllByMemberIdOffset(Long memberId, Pageable pageable) {
        var sql = String.format("SELECT * " +
                "FROM %s " +
                "WHERE memberId = :memberId " +
                "ORDER BY %s " +
                "limit :size " +
                "offset :offset ", TABLE, PageHelper.orderBy(pageable.getSort()));

        var params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("size", pageable.getPageSize())
                .addValue("offset", pageable.getOffset());

        var posts = namedParameterJdbcTemplate.query(sql, params, POST_ROW_MAPPER);

        return new PageImpl<>(posts, pageable, getCount(memberId));
    }

    public List<Post> findAllByMemberIdAndOrderByIdDesc(Long memberId, Long size) {
        var sql = String.format("SELECT * " +
                "FROM %s " +
                "WHERE memberId = :memberId " +
                "ORDER BY id DESC " +
                "LIMIT :size", TABLE);

        var params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("size", size);

        return namedParameterJdbcTemplate.query(sql, params, POST_ROW_MAPPER);
    }

    public List<Post> findAllByMemberIdsAndOrderByIdDesc(List<Long> memberIds, Long size) {
        if (memberIds.isEmpty()) return List.of();

        var sql = String.format("SELECT * " +
                "FROM %s " +
                "WHERE memberId IN (:memberIds) " +
                "ORDER BY id DESC " +
                "LIMIT :size", TABLE);

        var params = new MapSqlParameterSource()
                .addValue("memberIds", memberIds)
                .addValue("size", size);

        return namedParameterJdbcTemplate.query(sql, params, POST_ROW_MAPPER);
    }

    public List<Post> findAllByLessThanIdAndMemberIdAndOrderByIdDesc(Long id, Long memberId, Long size) {
        var sql = String.format("SELECT * " +
                "FROM %s " +
                "WHERE memberId = :memberId AND id < :id " +
                "ORDER BY id DESC " +
                "LIMIT :size", TABLE);

        var params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("id", id)
                .addValue("size", size);

        return namedParameterJdbcTemplate.query(sql, params, POST_ROW_MAPPER);
    }

    public List<Post> findAllByLessThanIdAndMemberIdsAndOrderByIdDesc(Long id, List<Long> memberIds, Long size) {
        if (memberIds.isEmpty()) return List.of();

        var sql = String.format("SELECT * " +
                "FROM %s " +
                "WHERE memberId IN (:memberIds) AND id < :id " +
                "ORDER BY id DESC " +
                "LIMIT :size", TABLE);

        var params = new MapSqlParameterSource()
                .addValue("memberIds", memberIds)
                .addValue("id", id)
                .addValue("size", size);

        return namedParameterJdbcTemplate.query(sql, params, POST_ROW_MAPPER);
    }

    private Long getCount(Long memberId) {
        var sql = String.format("SELECT COUNT(*) " +
                "FROM %s " +
                "WHERE memberId = :memberId ", TABLE);

        var params = new MapSqlParameterSource()
                .addValue("memberId", memberId);

        return namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
    }

    public List<Post> findAllByIdIn(List<Long> ids) {
        if (ids.isEmpty()) return List.of();

        var sql = String.format("SELECT * " +
                "FROM %s " +
                "WHERE id IN (:ids) " +
                "ORDER BY id DESC ", TABLE);

        var params = new MapSqlParameterSource()
                .addValue("ids", ids);

        return namedParameterJdbcTemplate.query(sql, params, POST_ROW_MAPPER);
    }

    public Optional<Post> findById(Long id, boolean requiredLock) {
        var sql = String.format("SELECT * FROM %s WHERE id = :id", TABLE);
        if (requiredLock) sql += " FOR UPDATE";
        var params = new MapSqlParameterSource()
                .addValue("id", id);
        var nullablePost = namedParameterJdbcTemplate.queryForObject(sql, params, POST_ROW_MAPPER);
        return Optional.ofNullable(nullablePost);
    }

    private Post update(Post post) {
        var sql = String.format("UPDATE %s SET " +
                "memberId = :memberId " +
                ", contents = :contents " +
                ", likeCount = :likeCount " +
                ", createdDate = :createdDate " +
                ", createdAt = :createdAt " +
                ", version = :version + 1 " +
                "WHERE id = :id " +
                "AND version = :version", TABLE);
        SqlParameterSource params = new BeanPropertySqlParameterSource(post);
        var updatedCount = namedParameterJdbcTemplate.update(sql, params);
        if (updatedCount == 0) {
            throw new OptimisticLockingFailureException("Optimistic Locking Failure Exception 으로 인한 Post 업데이트 실패");
        }
        return post;
    }
}
