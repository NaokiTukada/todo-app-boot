document.addEventListener('DOMContentLoaded', () => {
  // 編集ボタンを全て取得しクリックイベントを設定
  document.querySelectorAll('.edit-button').forEach(button => {
    button.addEventListener('click', (e) => {
      e.preventDefault(); // もしボタンがフォーム内なら送信防止

      const row = button.closest('tr'); // 編集ボタンのある行を取得

      // display-modeのtdを非表示にする
      row.querySelectorAll('.display-mode').forEach(el => {
        el.style.display = 'none';
      });

      // edit-modeのtdを表示する
      row.querySelectorAll('.edit-mode').forEach(el => {
        el.style.display = 'table-cell';
      });
    });
  });

  // キャンセルボタンを全て取得しクリックイベントを設定
  document.querySelectorAll('.edit-cancel-button').forEach(button => {
    button.addEventListener('click', (e) => {
      e.preventDefault(); // フォーム送信防止

      const row = button.closest('tr'); // キャンセルボタンのある行を取得

      // edit-modeのtdを非表示にする
      row.querySelectorAll('.edit-mode').forEach(el => {
        el.style.display = 'none';
      });

      // display-modeのtdを表示する
      row.querySelectorAll('.display-mode').forEach(el => {
        el.style.display = 'table-cell';
      });
    });
  });

  // 編集フォームのsubmitイベントにfetchで更新処理を追加
  document.querySelectorAll('form').forEach(form => {
    if (form.querySelector('.edit-ok-button')) { // 編集フォームかチェック
      form.addEventListener('submit', async (e) => {
        e.preventDefault(); // 画面遷移防止

        const row = form.closest('tr');
        const taskId = row.getAttribute('data-task-id');
        const title = form.querySelector('.edit-title-input').value;
        const dueDate = form.querySelector('.edit-duedate-input').value;

        console.log('送信:', taskId, title, dueDate);

        try {
          const res = await fetch(`/tasks/${taskId}`, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify({ title, dueDate }),
          });

          if (!res.ok) {
            throw new Error(`HTTP error! status: ${res.status}`);
          }

          // 編集モードを非表示、表示モードに戻す
          row.querySelectorAll('.edit-mode').forEach(el => el.style.display = 'none');
          row.querySelectorAll('.display-mode').forEach(el => el.style.display = 'table-cell');

          // 表示テキストを更新（必要なら）
          const titleSpan = row.querySelector('[data-task-title]');
          if (titleSpan) titleSpan.textContent = title;

          const dueDateSpan = row.querySelector('.display-mode:nth-child(2) > span');
          if (dueDateSpan) {
            dueDateSpan.textContent = dueDate ? dueDate : 'ー';
          }

        } catch (error) {
          console.error('更新エラー:', error);
          alert('更新に失敗しました。');
        }
      });
    }
  });
});
