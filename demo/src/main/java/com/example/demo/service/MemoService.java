package com.example.demo.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.example.demo.model.MemoEntity;
import com.example.demo.dataaccess.DataAccessApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemoService {

    @Autowired
    private DataAccessApplication daa;

    public MemoEntity selectMemo(int id) {
        // 参照
        MemoEntity memoEntity = daa.findMemo(id);
        return memoEntity;
    }

    public List<MemoEntity> getAllMemo() {
        // 検索
        List<MemoEntity> listMemo = daa.findMemoAll();

        return listMemo;
    }

    public void createMemo(String title, String content) {
        // 登録
        daa.insertMemo(title, content);
    }

    public void deleteMemo(int id) {
        // 削除
        daa.deleteMemo(id);
    }

    public List<MemoEntity> sort(List<MemoEntity> memoList, String key, String direction) {
        Stream<MemoEntity> entityStream = memoList.stream();

        if (direction.equals("asc")) {
            switch (key) {
                case "id":
                    entityStream = entityStream.sorted(Comparator.comparing(MemoEntity::getId));
                    break;
                case "title":
                    entityStream = entityStream.sorted(Comparator.comparing(MemoEntity::getTitle));
                    break;
                case "create_time":
                    entityStream = entityStream.sorted(Comparator.comparing(MemoEntity::getCreate_time));
                    break;
                default:
                    throw new IllegalArgumentException("Invalid sort key: " + key);
            }

        } else {
            switch (key) {
                case "id":
                    entityStream = entityStream.sorted(Comparator.comparing(MemoEntity::getId).reversed());
                    break;
                case "title":
                    entityStream = entityStream.sorted(Comparator.comparing(MemoEntity::getTitle).reversed());
                    break;
                case "create_time":
                    entityStream = entityStream.sorted(Comparator.comparing(MemoEntity::getCreate_time).reversed());
                    break;
                default:
                    throw new IllegalArgumentException("Invalid sort key: " + key);
            }
        }

        return entityStream.collect(Collectors.toList());
    }

}