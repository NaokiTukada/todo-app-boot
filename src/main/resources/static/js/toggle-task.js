function toggleTaskCompletion(taskId, event) {

    fetch(`/tasks/${taskId}/toggle`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('完了状態の切り替えに失敗しました。');
        }

        // 見た目のタイトルのclassをtoggle（チェックボックスの状態はクリックで変わるので不要）
        const taskRow = document.querySelector(`[data-task-id='${taskId}']`);
        const titleElement = taskRow.querySelector('[data-task-title]');
        titleElement.classList.toggle('completed');
    })
    .catch(error => {
        console.error('エラー:', error);
        alert(error.message);

        // エラー時はチェックボックスの状態を元に戻す
        event.target.checked = !event.target.checked;
    });
}
