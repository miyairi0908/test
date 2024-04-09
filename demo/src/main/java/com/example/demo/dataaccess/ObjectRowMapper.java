package com.example.demo.dataaccess;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.demo.model.MemoEntity;

public class ObjectRowMapper implements RowMapper<MemoEntity> {
    @Override
    public MemoEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        MemoEntity memoEntity = new MemoEntity();
        memoEntity.setId(rs.getInt("id"));
        memoEntity.setTitle(rs.getString("title"));
        memoEntity.setContent(rs.getString("content"));
        memoEntity.setCreate_time(rs.getTimestamp("create_time"));

        return memoEntity;
    }
}
