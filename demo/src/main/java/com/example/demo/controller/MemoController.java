package com.example.demo.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dataaccess.MemoRepository;
import com.example.demo.model.Memo;
import com.example.demo.model.MemoEntity;
import com.example.demo.service.MemoService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MemoController {

	@Autowired
	private MemoRepository memoRepository;
	@Autowired
	private MemoService memoService;

	// ホーム画面
	@RequestMapping("/")
	String index(HttpSession session, Model model) {
		session.invalidate();

		model.addAttribute("sortMemoList", findAll());
		return "memoHome";
	}

	// 新規作成画面へ遷移
	@GetMapping("/toMemoCreate")
	public String toMemoCreate(@ModelAttribute Memo memo) {
		return "memoCreate";
	}

	// 更新画面へ遷移
	@GetMapping("/toUpdate")
	public String toUpdate() {
		return "memoEdit";
	}

	// 戻る（ホーム画面）
	@GetMapping("/toHome")
	public String toHome(Model model, HttpSession session) {
		model.addAttribute("sortMemoList", findAll());
		return "memoHome";
	}

	// 戻る（詳細画面）
	@GetMapping("/toDetail")
	public String toDetail() {
		return "memoDetail";
	}

	// 詳細表示
	@PostMapping("/select")
	public String selectMemo(@RequestParam("id") int id, HttpSession session, MemoEntity memoEntity) {
		// memoEntity = memoService.selectMemo(id);
		// 検索
		memoEntity = memoRepository.findById(id);

		session.setAttribute("memo", memoEntity);
		return "memoDetail";
	}

	// 登録
	@PostMapping("/createMemo")
	public String createMemo(MemoEntity memoEntity, Memo memo, HttpSession session, Model model) {

		if ((memo.getTitle()).isEmpty()) {
			return "memoCreate";
		} else {
			memoEntity.setTitle(memo.getTitle());
			memoEntity.setContent(memo.getContent());

			memoEntity.setCreate_time(new Timestamp(System.currentTimeMillis()));

			// 登録
			memoRepository.save(memoEntity);

			model.addAttribute("sortMemoList", findAll());
		}
		return "memoHome";
	}

	// 更新
	@PostMapping("/update")
	public String updateMemo(@RequestParam("id") int id, HttpSession session, MemoEntity memoEntity, Memo memo) {

		memoEntity = memoRepository.findById(id);
		if (!memoEntity.getTitle().equals(memo.getTitle()) || !memoEntity.getContent().equals(memo.getContent())) {
			memoEntity.setTitle(memo.getTitle());
			memoEntity.setContent(memo.getContent());
			memoEntity.setCreate_time(new Timestamp(System.currentTimeMillis()));

			// 更新
			memoRepository.save(memoEntity);

			// 検索
			memoEntity = memoRepository.findById(id);

			session.setAttribute("memo", memoEntity);
		}
		return "memoDetail";
	}

	// 削除
	@PostMapping("/delete")
	public String deleteMemo(@RequestParam("id") int id, Model model) {
		// 削除
		memoRepository.deleteById(id);

		model.addAttribute("sortMemoList", findAll());
		return "memoHome";
	}

	// ソート
	@GetMapping("/sort")
	public String getMemoList(@RequestParam("sortKey") String sortKey,
			@RequestParam("sortDirection") String sortDirection, Model model, HttpSession session) {
		List<MemoEntity> memoList = findAll();

		memoList = memoService.sort(memoList, sortKey, sortDirection);

		session.setAttribute("sortKey", sortKey);
		session.setAttribute("sortDirection", sortDirection);

		model.addAttribute("sortMemoList", memoList);
		return "memoHome";
	}

	// 全件検索
	public List<MemoEntity> findAll() {
		List<MemoEntity> memoEntity = memoRepository.findAll();
		return memoEntity;
	}

	// 全件検索(jdbcTemplate)
	public List<MemoEntity> getAllMemo() {
		List<MemoEntity> memolist = memoService.getAllMemo();
		return memolist;
	}
}