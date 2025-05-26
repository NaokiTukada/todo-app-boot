function deleteTask(taskId) {
    if (!confirm("この目標を削除しますか？")) return;

    fetch(`/tasks/${taskId}`, { 
        method: 'POST',  // 削除はPOSTで進める
        headers: {
            'Authorization': `Bearer ${jwtToken}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("削除に失敗しました。");
        }
        console.log("削除成功、画面更新...");

        // **削除したタスクをDOMから削除**
        const taskElement = document.getElementById(`task-${taskId}`);
        if (taskElement) {
            taskElement.remove();  // 画面上のタスク要素を削除
        }
    })
    .catch(error => {
        console.error("エラー:", error);
        alert(error.message);
    });
}