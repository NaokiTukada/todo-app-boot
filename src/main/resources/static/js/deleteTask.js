function deleteTask(taskId) {
    if (!confirm("この目標を削除しますか？")) return;

    fetch(`/tasks/${taskId}`, { 
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("削除に失敗しました。");
        }
        console.log("削除成功、画面リロード...");
        
        // 画面を再読み込みする
        location.reload();  // ✅←これでページ全体をリロード
    })
    .catch(error => {
        console.error("エラー:", error);
        alert(error.message);
    });
}