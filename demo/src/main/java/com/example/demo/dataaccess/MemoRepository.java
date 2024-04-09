package com.example.demo.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.MemoEntity;

@Repository
public interface MemoRepository extends JpaRepository<MemoEntity, Integer> {

    MemoEntity findById(int id);

}