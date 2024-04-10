package com.example.demo.dataaccess;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.model.MemoEntity;

@Repository
public class DataAccessApplication {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // メモの検索
    public MemoEntity findMemo(int id) {

        // SELECT文
        String query = "SELECT * "
                + "FROM memo "
                + "WHERE id = ?";

        // 検索実行
        List<MemoEntity> result = jdbcTemplate.query(query, new ObjectRowMapper(), id);
        return result.get(0);
    }

    // メモの全件検索
    public List<MemoEntity> findMemoAll() {

        // SELECT文
        String query = "SELECT * "
                + "FROM memo ";

        // 検索実行
        List<MemoEntity> listmemo = jdbcTemplate.query(query, new ObjectRowMapper());

        return listmemo;
    }

    // メモの登録
    public int insertMemo(String title, String content) {

        // INSERT文
        String query = "INSERT INTO memo(title,content) "
                + "VALUES(?,?)";

        // 検索実行
        return jdbcTemplate.update(query, title, content);

    }

    // メモの削除
    public int deleteMemo(int id) {

        // DELETE文
        String query = "DELETE FROM memo "
                + "WHERE id = ?";

        // 検索実行
        System.out.println(jdbcTemplate.update(query, id));
        return 1;

    }

}
