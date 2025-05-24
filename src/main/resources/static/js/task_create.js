document.addEventListener('DOMContentLoaded', function () {
    // ページを読み込んだときに「目標名が空のタスクがないかチェックする」関数
    function validateTaskTitles() {
        let taskTitles = document.querySelectorAll('span[data-task-title]');
        let hasEmptyTitle = false;

        taskTitles.forEach(function (titleElement) {
            if (titleElement.textContent.trim() === "") {
                hasEmptyTitle = true;
            }
        });

        if (hasEmptyTitle) {
            alert("目標名が空のタスクがあります。設定してください。");
        }
    }

    validateTaskTitles(); // ページを読み込んだらこのチェックを実行

    // ここは今のままでOK！
    // テーブルの中にある「チェックボックス」「編集ボタン」「OKボタン（編集用）」「キャンセルボタン（編集用）」が
    // 押されたときの動きを設定しています。
    // `document.querySelector('table tbody').addEventListener('click', ...)`
    // のように、テーブル全体でクリックを監視することで、後から追加された目標（行）でも
    document.querySelector('table tbody').addEventListener('click', function (event) {
        let target = event.target;
        let row = target.closest('tr.task-item'); // 「task-item」クラスがついた行（目標の行）を探す

        if (target.type === 'checkbox') {
            // チェックボックスが押されたら、そのチェックボックスが入っているフォームを送信
            target.closest('form').submit();
        } else if (target.classList.contains('edit-button')) {
            // 編集ボタンが押されたら、その行に「editing」クラスを追加して、見た目を編集モードにする
            if (row) {
                row.classList.add('editing');
            }
        } else if (target.classList.contains('edit-ok-button')) {
            // 編集モードのOKボタンが押されたら
            let inputTitle = row.querySelector('.edit-title-input');
            if (inputTitle.value.trim() === "") {
                alert("目標名を入力してください。");
                event.preventDefault(); // 空だったらフォーム送信をキャンセル！
            }
            // OKボタンの処理 (ここはサーバーに更新情報を送る役割なので、今のままでOK)
        } else if (target.classList.contains('edit-cancel-button')) {
            // 編集モードのキャンセルボタンが押されたら
            if (row) {
                row.classList.remove('editing'); // 「editing」クラスを外して、見た目を元に戻す
            }
        }
    });

    // --- ここからが「新規作成」に関する変更です！ ---

    // 新規作成に関わるHTMLの要素（ボタンや入力欄、新しく追加される行の親）を見つける
    const createButton = document.getElementById('createButton'); // 「新規作成」ボタン
    const newRow = document.getElementById('newRow'); // 新しい目標を入力する隠れた行
    const newRowOkButton = document.getElementById('newRowOkButton'); // 新規作成の「OK」ボタン
    const newRowCancelButton = document.getElementById('newRowCancelButton'); // 新規作成の「キャンセル」ボタン
    const newTaskTitleInput = newRow.querySelector('.new-task-title-input'); // 新規作成の目標名入力欄
    const newTaskDueDateInput = newRow.querySelector('.new-task-duedate-input'); // 新規作成の予定時刻入力欄
    const taskTableBody = document.querySelector('table tbody'); // 目標のリストがあるテーブルの「本体」（ここに新しい行を追加する）

    // 「新規作成」ボタンが押されたら
    if (createButton && newRow) { // ボタンと行がHTMLにあることを確認
        createButton.addEventListener('click', function () {
            newRow.classList.remove('hidden'); // 隠れていた「新しい目標を入力する行」を表示する
            newRow.classList.add('editing'); // 必要なら「editing」クラスも追加して見た目を整える
            newTaskTitleInput.value = ''; // 入力欄を空にする（前に入力したものが残らないように）
            newTaskDueDateInput.value = ''; // 入力欄を空にする
            newTaskTitleInput.focus(); // 目標名の入力欄にカーソルを合わせる
        });
    }

    // --- 新規作成の「OK」ボタンが押されたときの動き（ここが一番大事！） ---
    // ここで、ページを切り替えずに新しい目標を追加する処理を書きます。
    newRowOkButton.addEventListener('click', async function (event) {
        // ☆ココがポイント1☆: デフォルトのフォーム送信（ページを切り替える動き）を止めます！
        event.preventDefault();

        // ユーザーが入力した目標名と予定時刻を取得する
        const title = newTaskTitleInput.value.trim(); // 目標名の前後のスペースを削除
        const dueDate = newTaskDueDateInput.value; // 予定時刻

        // 目標名が空だったら警告を出して、処理を止める
        if (!title) {
            alert('目標名を入力してください。');
            return;
        }

        // 予定時刻が入力されていなければnull、入力されていればその値をそのまま使う
        // (サーバー側で「HH:mm」形式の文字列として受け取ります)
        const payloadDueDate = dueDate || null;

        // サーバーに送るデータを準備する
        const newTaskData = {
            title: title,
            dueDate: payloadDueDate,
            // userId: サーバーで自動的に判断してもらうのが良いですが、
            // もしJavaScriptから送る必要があるなら、HTMLのどこかに隠しておいて取得します。
        };

        try {
            // ☆ココがポイント2☆: サーバーにデータを非同期で送信します (Ajaxリクエスト)
            // `Workspace`という機能を使って、ページを切り替えずに裏側でサーバーと通信します。
            const response = await fetch('/tasks/create', { // HTMLのフォームのth:actionと同じパスを使います
                method: 'POST', // データを送信する（POST）
                headers: {
                    'Content-Type': 'application/json', // 送るデータの形式はJSONだよ、と伝える
                    // もし、CSRFトークン（セキュリティのための合い言葉）が必要なら、ここに書きます
                    // 例: 'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').getAttribute('content')
                },
                body: JSON.stringify(newTaskData) // 準備したデータをJSON形式に変換して送る
            });

            // サーバーからの返事が「成功」だったら
            if (response.ok) {
                // ☆ココがポイント3☆: サーバーから新しく作られた目標の情報を受け取る
                const createdTask = await response.json(); // サーバーから「こんな目標作ったよ！」という情報をもらう

                // 2. 受け取った情報を使って、今のページに新しい目標の行（<tr>）を追加する！
                const newTr = document.createElement('tr'); // 新しい行（<tr>）を作る
                newTr.classList.add('task-item'); // クラスを付けて、CSSが適用されるようにする
                // サーバーからもらったタスクのIDを、その行の「data-task-id」に設定する
                newTr.setAttribute('data-task-id', createdTask.taskId);

                // 今日の日付を「YYYY/MM/DD」形式で準備する（設定日に使う）
                const today = new Date();
                const formattedCreatedAt = today.toLocaleDateString('ja-JP', {
                    year: 'numeric',
                    month: '2-digit',
                    day: '2-digit'
                }).replace(/\//g, '/');

                // 新しい行の中身（<td>要素など）をHTMLの文字列で作る
                // ここは、HTMLの既存の目標の行の見た目を参考にして作ってくださいね。
                newTr.innerHTML = `
                    <td class="display-mode">
                        <form action="/tasks/${createdTask.taskId}/toggle" method="post">
                            <input type="checkbox" name="completed">
                        </form>
                        <span data-task-title>${createdTask.title}</span>
                    </td>
                    <td class="display-mode">
                        <span>${createdTask.dueDate ? createdTask.dueDate.substring(0, 5) : 'ー'}</span>
                    </td>
                    <td class="display-mode">
                        <span>${formattedCreatedAt}</span>
                    </td>
                    <td class="display-mode">
                        <span>0日</span>
                    </td>
                    <td class="display-mode action-buttons">
                        <button class="edit-button">編集</button>
                        <form action="/tasks/${createdTask.taskId}/delete" method="post" onsubmit="return confirm('この目標を削除しますか？');">
                            <button type="submit" class="delete-button">削除</button>
                        </form>
                    </td>
                    <td class="edit-mode" colspan="5">
                        <form action="/tasks/${createdTask.taskId}/update" method="post">
                            <input type="text" name="title" value="${createdTask.title}" placeholder="目標名を入力してください" class="edit-title-input">
                            <input type="time" name="dueDate" value="${createdTask.dueDate ? createdTask.dueDate.substring(0, 5) : ''}" placeholder="予定時刻" class="edit-duedate-input">
                            <span>設定日: <span>${formattedCreatedAt}</span></span>
                            <span>連続日数: <span>0日</span></span>
                            <button type="submit" class="edit-ok-button">OK</button>
                            <button type="button" class="edit-cancel-button">キャンセル</button>
                        </form>
                    </td>
                `;

                // ☆ココがポイント4☆: 作った新しい行を、テーブルの本体（tbody）の最後に追加する
                taskTableBody.appendChild(newTr);

                // 3. 入力フォームをクリアして、非表示に戻す
                newRow.classList.add('hidden'); // 新規作成行を隠す
                newRow.classList.remove('editing'); // 「editing」クラスも外す
                newTaskTitleInput.value = ''; // 入力欄を空にする
                newTaskDueDateInput.value = ''; // 入力欄を空にする

                // 「新しい目標を作成しました」というメッセージは、もう不要になるかもしれません。
                // 目標が追加されたのが目に見えるので。
                // alert('新しい目標を作成しました。');

            } else {
                // サーバーからの返事が「失敗」だったら
                const errorData = await response.json(); // エラーの内容を受け取る
                alert('目標の作成に失敗しました: ' + (errorData.message || '不明なエラー'));
            }
        } catch (error) {
            // サーバーとの通信中に何か問題が起こったら
            console.error('通信エラー:', error); // エラーを開発者ツールに出す
            alert('サーバーとの通信中にエラーが発生しました。');
        }
    });

    // 新規作成の「キャンセル」ボタンが押されたら
    newRowCancelButton.addEventListener('click', function () {
        newRow.classList.add('hidden'); // 隠す
        newRow.classList.remove('editing'); // 「editing」クラスも外す
        newTaskTitleInput.value = ''; // 入力欄を空にする
        newTaskDueDateInput.value = ''; // 入力欄を空にする
    });
});