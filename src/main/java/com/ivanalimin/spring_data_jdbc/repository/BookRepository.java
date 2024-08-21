package com.ivanalimin.spring_data_jdbc.repository;

import com.ivanalimin.spring_data_jdbc.model.Book;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class BookRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Book> rowMapper = BeanPropertyRowMapper.newInstance(Book.class);
    private final SimpleJdbcInsert simpleJdbcInsert;

    public BookRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("books")
                .usingGeneratedKeyColumns("id");

    }

    public List<Book> findAll() {
        String sql = "select * from books";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<Book> findById(Long id) {
        String sql = "select * from books where id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
    }

    @Transactional
    public Book save(Book book) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", book.getId());
        params.addValue("title", book.getTitle());
        params.addValue("author", book.getAuthor());
        params.addValue("publication_year", book.getPublicationYear());
        Number id = simpleJdbcInsert.executeAndReturnKey(params);
        book.setId(id.longValue());
        return book;
    }

    @Transactional
    public Book update(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, publication_year = ? WHERE id = ?";
        if (jdbcTemplate.update(sql, book.getTitle(), book.getAuthor(), book.getPublicationYear(), book.getId()) == 0) {
            return null;
        }
        return book;
    }

    @Transactional
    public boolean delete(Long id) {
        String sql = "delete from books where id = ?";
        return jdbcTemplate.update(sql, id) != 0;
    }
}
