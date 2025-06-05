function deleteTask(taskId, event) {
    if (event) event.preventDefault(); 

    if (!confirm("この目標を削除しますか？")) return;

    fetch(`/tasks/${taskId}`, { 
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("削除に失敗しました。");
        }
        console.log("削除成功、画面リロード...");
        location.reload();
    })
    .catch(error => {
        console.error("エラー:", error);
        alert(error.message);
    });
}