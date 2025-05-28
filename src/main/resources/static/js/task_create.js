document.addEventListener('DOMContentLoaded', function () {
    
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

    validateTaskTitles(); 

  
    document.querySelector('table tbody').addEventListener('click', function (event) {
        let target = event.target;
        let row = target.closest('tr.task-item'); 

        if (target.type === 'checkbox') {
            
            target.closest('form').submit();
        } else if (target.classList.contains('edit-button')) {
            
            if (row) {
                row.classList.add('editing');
            }
        } else if (target.classList.contains('edit-ok-button')) {
            
            let inputTitle = row.querySelector('.edit-title-input');
            if (inputTitle.value.trim() === "") {
                alert("目標名を入力してください。");
                event.preventDefault(); 
            }
          
        } else if (target.classList.contains('edit-cancel-button')) {
           
            if (row) {
                row.classList.remove('editing'); 
            }
        }
    });

    // --- 新規作成に関する要素の取得 ---
    const createButton = document.getElementById('createButton'); 
    const newRow = document.getElementById('newRow'); 
    const newRowOkButton = document.getElementById('newRowOkButton'); 
    const newRowCancelButton = document.getElementById('newRowCancelButton'); 
    const newTaskTitleInput = newRow.querySelector('.new-task-title-input'); 
    const newTaskDueDateInput = newRow.querySelector('.new-task-duedate-input'); 
    const taskTableBody = document.querySelector('table tbody'); 

    // 「新規作成」ボタンが押されたら
    if (createButton && newRow) { 
        createButton.addEventListener('click', function () {
            newRow.classList.remove('hidden'); 
            newRow.classList.add('editing'); 
            newTaskTitleInput.value = ''; 
            newTaskDueDateInput.value = ''; 
            newTaskTitleInput.focus(); 
        });
    }

    // --- 新規作成の「OK」ボタンが押されたときの動き ---
    newRowOkButton.addEventListener('click', async function (event) {
        event.preventDefault(); 
        const title = newTaskTitleInput.value.trim(); 
        const dueDate = newTaskDueDateInput.value; 
        

        if (!title) {
            alert('目標名を入力してください。');
            return;
        }

        
        const userIdElement = document.getElementById("userId");
        let userId = null;
        if (userIdElement) {
            userId = parseInt(userIdElement.value);
        } else {
            console.warn("HTMLにid='userId'の要素が見つかりません。ユーザーIDが送信されません。");
            
        }

        const payloadDueDate = dueDate || null;
            
        const newTaskData = {
            title: title,
            dueDate: payloadDueDate,
            user: {
                userId: userId
            }
        };
        
        try { 
            const response = await fetch('/tasks', { 
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(newTaskData)
            });

            
            if (response.ok) {
                const createdTask = await response.json(); 
                
                const newTr = document.createElement('tr'); 
                newTr.classList.add('task-item'); 
                newTr.setAttribute('data-task-id', createdTask.taskId); 

                
                const today = new Date();
                const formattedCreatedAt = today.toLocaleDateString('ja-JP', {
                    year: 'numeric',
                    month: '2-digit',
                    day: '2-digit'
                }).replace(/\//g, '/');

                // 新しい行の中身（<td>要素など）をHTMLの文字列で作る
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
                    <td class="display-mode action-buttons" style="display: table-cell;">
                        <button class="edit-button">編集</button>
                        <form action="/tasks/${createdTask.taskId}/delete" method="post" onsubmit="return confirm('この目標を削除しますか？');">
                            <button type="submit" class="delete-button">削除</button>
                        </form>
                    </td>
                    <td class="edit-mode" colspan="6">
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

                
                if (taskTableBody) { // nullチェックを追加
                    taskTableBody.appendChild(newTr);
                } else {
                    console.error("taskTableBody が見つかりません。HTMLを確認してください。");
                }

                //  入力フォームをクリアして、非表示に戻す
                newRow.classList.add('hidden'); // 新規作成行を隠す
                newRow.classList.remove('editing'); // 「editing」クラスも外す
                newTaskTitleInput.value = ''; // 入力欄を空にする
                newTaskDueDateInput.value = ''; // 入力欄を空にする

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

    
    newRowCancelButton.addEventListener('click', function () {
        newRow.classList.add('hidden'); // 隠す
        newRow.classList.remove('editing'); // 「editing」クラスも外す
        newTaskTitleInput.value = ''; // 入力欄を空にする
        newTaskDueDateInput.value = ''; // 入力欄を空にする
    });
});